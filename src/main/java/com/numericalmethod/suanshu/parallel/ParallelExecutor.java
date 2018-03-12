/*
 * Copyright (c) Numerical Method Inc.
 * http://www.numericalmethod.com/
 * 
 * THIS SOFTWARE IS LICENSED, NOT SOLD.
 * 
 * YOU MAY USE THIS SOFTWARE ONLY AS DESCRIBED IN THE LICENSE.
 * IF YOU ARE NOT AWARE OF AND/OR DO NOT AGREE TO THE TERMS OF THE LICENSE,
 * DO NOT USE THIS SOFTWARE.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITH NO WARRANTY WHATSOEVER,
 * EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION,
 * ANY WARRANTIES OF ACCURACY, ACCESSIBILITY, COMPLETENESS,
 * FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT, 
 * TITLE AND USEFULNESS.
 * 
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS A RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.numericalmethod.suanshu.parallel;

import com.numericalmethod.suanshu.parallel.SynchronizedIterator.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class provides a framework for executing an algorithm in parallel. A
 * thread pool is created when executing a list of tasks.
 *
 * <p>
 * Caution: Avoid using another executor within parallelized calls, this would
 * create numerous threads, leading to much memory consumption and huge overhead
 * for thread switching. It is recommended to parallelize the outermost-scoped
 * tasks.
 *
 * @author Ken Yiu
 */
public class ParallelExecutor {

    private static final AtomicLong executorCount = new AtomicLong(0);
    private final int concurrency;
    private final ThreadPoolExecutor executor;
    private final AtomicLong threadCount = new AtomicLong(0);
    private final long executorId = executorCount.incrementAndGet();
    private final String namePrefix = String.format("parallel-executor-%d-thread-", executorId);

    /**
     * Creates an instance using default concurrency number, which is the
     * number of available processors returned by
     * <pre><code>
     * Runtime.getRuntime().availableProcessors()
     * </code></pre>
     */
    public ParallelExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Creates an instance with a specified concurrency number.
     *
     * @param concurrency the maximum number of threads can be used when executing a list of tasks
     */
    public ParallelExecutor(int concurrency) {
        this.concurrency = concurrency;
        this.executor = new ThreadPoolExecutor(
                concurrency,
                concurrency,
                500, // keep threads in the pool alive for a short time period
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {

                    @Override
                    public Thread newThread(Runnable r) {
                        String threadLabel = namePrefix + threadCount.incrementAndGet();
                        Thread t = new Thread(r, threadLabel);
                        if (t.isDaemon()) {
                            t.setDaemon(false);
                        }
                        if (t.getPriority() != Thread.NORM_PRIORITY) {
                            t.setPriority(Thread.NORM_PRIORITY);
                        }
                        return t;
                    }
                });
        /*
         * Note: core threads have to be allowed to timeout in order to let the
         * ThreadPoolExecutor shutdown itself automatically when there is no
         * more reference to the instance.
         *
         * Ref.: see javadoc for ThreadPoolExecutor
         */
        this.executor.allowCoreThreadTimeOut(true);
    }

    /**
     * Executes a list of {@link Callable} tasks, and returns a list of results
     * in the same sequential order as {@code tasks}.
     *
     * @param <T>   the type of results
     * @param tasks the list of tasks
     * @return a list of results
     * @throws MultipleExecutionException if one or more tasks throws an exception
     */
    public <T> List<T> executeAll(List<? extends Callable<T>> tasks)
            throws MultipleExecutionException {
        List<T> results = new ArrayList<T>(tasks.size());
        try {
            List<Future<T>> futures = executor.invokeAll(tasks);

            List<ExecutionException> exceptions = new ArrayList<ExecutionException>(futures.size());
            boolean exceptionCaught = false;
            for (Future<T> future : futures) {
                try {
                    results.add(future.get());
                    exceptions.add(null);
                } catch (ExecutionException ex) {
                    results.add(null);
                    exceptions.add(ex);
                    exceptionCaught = true;
                }
            }
            if (exceptionCaught) {
                throw new MultipleExecutionException(results, exceptions);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        return results;
    }

    /**
     * Executes an arbitrary number of {@link Callable} tasks, and returns a
     * list of results in the same order. This is a convenient method and is the
     * same as calling:
     * <pre><code>
     * executeAll(Arrays.&lt;Callable&lt;T&gt;&gt;asList(tasks));
     * </code></pre>
     *
     * @param <T>   the type of results
     * @param tasks the list of tasks
     * @return a list of results
     * @throws MultipleExecutionException if one or more tasks throws an exception
     */
    public <T> List<T> executeAll(Callable<T>... tasks) throws MultipleExecutionException {
        return executeAll(Arrays.<Callable<T>>asList(tasks));
    }

    /**
     * Executes a list of tasks in parallel, and returns the result from the
     * earliest successfully completed tasks (without throwing an exception).
     *
     * @param <T>   the type of results
     * @param tasks the list of tasks
     * @return the earliest returned results
     * @throws ExecutionException if no task successfully completes
     */
    public <T> T executeAny(List<? extends Callable<T>> tasks) throws ExecutionException {
        T result = null;
        try {
            result = executor.invokeAny(tasks);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    /**
     * Executes a list of tasks in parallel, and returns the result from the
     * earliest successfully completed tasks (without throwing an exception).
     * This is a convenient method and is the same as calling:
     * <pre><code>
     * executeAny(Arrays.&lt;Callable&lt;T&gt;&gt;asList(tasks));
     * </code></pre>
     *
     * @param <T>   the type of results
     * @param tasks the list of tasks
     * @return the earliest returned results
     * @throws ExecutionException if no task successfully completes
     */
    public <T> T executeAny(Callable<T>... tasks) throws ExecutionException {
        return executeAny(Arrays.<Callable<T>>asList(tasks));
    }

    /**
     * Runs a for-loop in parallel. A huge for-loop is partitioned into roughly
     * equal size, and each partition is then run by a thread. This is similar
     * to running a normal for-loop construct:
     * <pre><code>
     * for (int i = start; i &lt; end; i += increment) {
     *     body.run(i);
     * }
     * </code></pre>
     *
     * @param start     the first loop index (inclusive)
     * @param end       the last loop index (exclusive)
     * @param increment the increment of the loop index in each iteration
     * @param body      the loop body
     * @throws MultipleExecutionException if one or more partitioned for-loop throws an exception
     */
    public void forLoop(int start, int end, final int increment, final LoopBody body) throws MultipleExecutionException {
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>(concurrency);

        // divide the loop into equal portions among threads
        int blockSize = (int) Math.ceil((end - start) / increment / (double) concurrency) * increment;
        blockSize = Math.max(1, blockSize); // block size must be at least 1

        for (int i = 0; i < concurrency; ++i) {
            final int THREAD_START_INDEX = start + i * blockSize;
            if (THREAD_START_INDEX >= end) {
                break; // do not need so many threads
            }
            final int THREAD_END_INDEX = Math.min(THREAD_START_INDEX + blockSize, end);
            tasks.add(
                    new Callable<Void>() {

                        @Override
                        public Void call() throws Exception {
                            for (int j = THREAD_START_INDEX; j < THREAD_END_INDEX; j += increment) {
                                body.run(j);
                            }
                            return null;
                        }
                    });
        }

        executeAll(tasks);
    }

    /**
     * Calls {@link #forLoop(int, int, int, com.numericalmethod.suanshu.parallel.LoopBody) forLoop}
     * with {@code increment} of 1.
     *
     * @param start the first loop index (inclusive)
     * @param end   the last loop index (exclusive)
     * @param body  the loop body
     * @throws MultipleExecutionException if one or more partitioned for-loop throws an exception
     */
    public void forLoop(int start, int end, LoopBody body) throws MultipleExecutionException {
        forLoop(start, end, 1, body);
    }

    /**
     * Runs a parallel for-loop only if {@code conditionToParallelize} is {@code true}.
     * Otherwise, the loop body will be executed in a single thread.
     *
     * @param conditionToParallelize the condition to parallelize the for-loop execution
     * @param start                  the first loop index (inclusive)
     * @param end                    the last loop index (exclusive)
     * @param increment              the increment of the loop index in each iteration
     * @param body                   the loop body
     * @throws MultipleExecutionException if one or more partitioned for-loop throws an exception
     */
    public void conditionalForLoop(boolean conditionToParallelize, int start, int end, int increment, LoopBody body) throws MultipleExecutionException {
        if (conditionToParallelize) {
            forLoop(start, end, increment, body);
        } else {
            // single threaded for-loop
            try {
                for (int i = start; i < end; i += increment) {
                    body.run(i);
                }
            } catch (Exception ex) {
                throw new MultipleExecutionException(
                        Arrays.<Void>asList((Void) null),
                        Arrays.<ExecutionException>asList(new ExecutionException(ex)));
            }
        }
    }

    /**
     * Calls {@link #conditionalForLoop(boolean, int, int, int, com.numericalmethod.suanshu.parallel.LoopBody) conditionalForLoop}
     * with {@code increment} of 1.
     *
     * @param conditionToParallelize the condition to parallelize the for-loop execution
     * @param start                  the first loop index (inclusive)
     * @param end                    the last loop index (exclusive)
     * @param body                   the loop body
     * @throws MultipleExecutionException if one or more partitioned for-loop throws an exception
     */
    public void conditionalForLoop(boolean conditionToParallelize, int start, int end, LoopBody body) throws MultipleExecutionException {
        conditionalForLoop(conditionToParallelize, start, end, 1, body);
    }

    /**
     * Runs a "foreach" loop in parallel. Multiple threads take elements from
     * the iterable collection and run the loop body in parallel. Threads are
     * coordinated such that two different threads will not run the loop body
     * for the same element in the collection. This is similar to running a
     * normal "foreach" construct:
     * <pre><code>
     * for (T item : collection) {
     *     body.run(item);
     * }
     * </code></pre>
     *
     * @param <T>      data type of elements in {@code iterable}
     * @param iterable the {@link Iterable} collection
     * @param body     the loop body
     * @throws MultipleExecutionException if one or more threads throws an exception
     */
    public <T> void forEach(Iterable<T> iterable, final IterationBody<T> body) throws MultipleExecutionException {
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>(concurrency);

        final SynchronizedIterator<T> iterator = new SynchronizedIterator<T>(iterable.iterator());

        for (int i = 0; i < concurrency; ++i) {
            tasks.add(
                    new Callable<Void>() {

                        @Override
                        public Void call() throws Exception {
                            for (Element<T> element; (element = iterator.next()).exists();) {
                                body.run(element.get());
                            }
                            return null;
                        }
                    });
        }

        executeAll(tasks);
    }

    /**
     * Calls {@link #forEach(java.lang.Iterable, com.numericalmethod.suanshu.parallel.IterationBody) forEach}
     * only if {@code conditionToParallelize} is {@code true}.
     * Otherwise, the loop body will be executed in a single thread.
     *
     * @param <T>                    data type of elements in {@code iterable}
     * @param conditionToParallelize the condition to parallelize the "foreach" execution
     * @param iterable               the {@link Iterable} collection
     * @param body                   the loop body
     * @throws MultipleExecutionException if one or more threads throws an exception
     */
    public <T> void conditionalForEach(boolean conditionToParallelize, Iterable<T> iterable, IterationBody<T> body) throws MultipleExecutionException {
        if (conditionToParallelize) {
            forEach(iterable, body);
        } else {
            for (T item : iterable) {
                body.run(item);
            }
        }
    }
}
