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

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.random.multivariate.IID;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Ken Yiu
 */
public class ParallelExecutorTest {

    @Test
    public void test_executeAllList_0010() throws MultipleExecutionException {
        final int taskSize = 10;
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>(taskSize);
        for (int i = 0; i < taskSize; ++i) {
            final int I = i;
            tasks.add(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    int sum = 0;
                    for (int i = 1; i <= 100; ++i) {
                        sum += i;
                    }
                    return sum + I;
                }
            });
        }

        Integer[] sums = new Integer[taskSize];
        Arrays.fill(sums, 5050);
        for (int i = 0; i < taskSize; ++i) {
            sums[i] += i; // check if the results are in sequence
        }
        List<Integer> expResult = Arrays.<Integer>asList(sums);

        List<Integer> result = new ParallelExecutor().executeAll(tasks);
        assertEquals(expResult, result);
    }

    @Test
    public void test_executeAllVarArg_0010() throws MultipleExecutionException {
        @SuppressWarnings("unchecked")
        List<Integer> results = new ParallelExecutor().executeAll(
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        return 1;
                    }
                },
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        return 2;
                    }
                });

        assertEquals(
                "results should be in the same sequential order as input tasks",
                Arrays.asList(1, 2), results);
    }

    @Test(timeout = 1000)
    public void test_executeAnyVarArg_0010() throws ExecutionException {
        ParallelExecutor parallel = new ParallelExecutor(2);
        @SuppressWarnings("unchecked")
        Integer result = parallel.executeAny(
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        throw new Exception("test");
                    }
                },
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        throw new RuntimeException("runtime");
                    }
                },
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        Thread.sleep(100000); // longer execution time
                        return 100;
                    }
                },
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        return 1;
                    }
                });

        assertEquals(1, result.intValue());
    }

    @Test
    public void test_forLoop_accessAtomicObject() throws MultipleExecutionException {
        final AtomicInteger sum = new AtomicInteger(0);
        new ParallelExecutor().forLoop(1, 101, new LoopBody() {

            @Override
            public void run(int i) throws Exception {
                sum.addAndGet(i);
            }
        });

        assertEquals("sum of indices", 5050, sum.get());
    }

    @Test
    public void test_forLoop_assignArrayEntries() throws MultipleExecutionException {
        final double[] array = new double[1000];
        new ParallelExecutor().forLoop(0, array.length, new LoopBody() {

            @Override
            public void run(int i) throws Exception {
                array[i] = i; // assign all elements in parallel
            }
        });

        double[] expResult = R.seq(0., array.length - 1, 1.);

        assertArrayEquals(expResult, array, 1e-15);
    }

    @Test
    public void test_forLoop_nestedLoops() throws MultipleExecutionException {
        final double[][] array2d = new double[100][100];

        new ParallelExecutor().forLoop(0, array2d.length, new LoopBody() {

            @Override
            public void run(final int i) throws Exception {
                for (int j = 0; j < array2d[i].length; ++j) {
                    array2d[i][j]++; // nested loop for incrementing 2D array
                }
            }
        });

        for (int i = 0; i < array2d.length; ++i) {
            for (int j = 0; j < array2d[i].length; ++j) {
                assertEquals("all 1's", 1., array2d[i][j], 1e-15);
            }
        }
    }

    @Test
    public void test_forLoop_assignMatrixEntries() throws MultipleExecutionException {
        final int matrixSize = 10;
        IID iid = new IID(new UniformRng(), matrixSize * matrixSize);
        final Matrix A1 = new DenseMatrix(iid.nextVector(), matrixSize, matrixSize);
        final Matrix A2 = new DenseMatrix(matrixSize, matrixSize);
        A2.set(1, 1, 0.); // trigger space allocation in the main thread
        new ParallelExecutor().forLoop(1, matrixSize + 1, new LoopBody() {

            @Override
            public void run(int i) throws Exception {
                for (int j = 1; j <= matrixSize; ++j) {
                    A2.set(i, j, A1.get(i, j));
                }
            }
        });

        assertTrue(A1 + " does not equal to " + A2, AreMatrices.equal(A1, A2, 1e-15));
    }

    @Test
    public void test_forLoop_blockIncrement() throws MultipleExecutionException {
        final int increment = 10;
        final double[] array = new double[1000];
        new ParallelExecutor().forLoop(0, array.length, increment, new LoopBody() {

            @Override
            public void run(int i) throws Exception {
                array[i] = i; // increment all elements in parallel
            }
        });

        double[] expResult = new double[1000];
        for (int i = 0; i < expResult.length; i += increment) {
            expResult[i] = i;
        }

        assertArrayEquals(expResult, array, 1e-15);
    }

    @Test
    public void test_forEach_0010() throws MultipleExecutionException {
        int size = 100;
        List<Integer> list = new ArrayList<Integer>(size);
        for (int i = 0; i < size; ++i) {
            list.add(i);
        }

        final AtomicInteger sum = new AtomicInteger(0);
        new ParallelExecutor().forEach(list, new IterationBody<Integer>() {

            @Override
            public void run(Integer item) {
                sum.addAndGet(item);
            }
        });

        assertEquals(4950, sum.get());
    }

    @Test
    public void test_forEach_0020() throws MultipleExecutionException {
        List<Integer> list = Collections.<Integer>emptyList();
        final AtomicInteger sum = new AtomicInteger(0);
        new ParallelExecutor().forEach(list, new IterationBody<Integer>() {

            @Override
            public void run(Integer item) {
                sum.addAndGet(item);
            }
        });

        assertEquals(0, sum.get());
    }

    @Test
    public void test_reusability_0010() throws MultipleExecutionException {
        ParallelExecutor executor = new ParallelExecutor();
        @SuppressWarnings("unchecked")
        List<Integer> results1 = executor.executeAll(
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        return 1;
                    }
                });

        assertEquals(Arrays.asList(1), results1);
        // reuse the executor
        @SuppressWarnings("unchecked")
        List<Integer> results2 = executor.executeAll(
                new Callable<Integer>() {

                    @Override
                    public Integer call() throws Exception {
                        return 2;
                    }
                });

        assertEquals(Arrays.asList(2), results2);
    }
}
