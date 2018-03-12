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
package com.numericalmethod.suanshu.stats.random.concurrent;

import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import com.numericalmethod.suanshu.stats.random.RngUtils;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Johannes Lehmann
 */
public class ConcurrentCachedRNGTest {

    @Test
    public void benchmarkAgainstSynchronized() throws MultipleExecutionException {
        int nLoops = 10;
        final int nGenPerLoop = 1000000;
        ParallelExecutor parallelExecutor = new ParallelExecutor(nLoops); // use same number of threads to minimize the effect for recycling threads

        // Run with synchronized random generator
        long startTime = System.currentTimeMillis();
        final RandomNumberGenerator rngSynch =
                RngUtils.synchronizedRNG(new UniformRng());
        parallelExecutor.forLoop(0, nLoops, new LoopBody() {

            @Override
            public void run(int i) throws Exception {
                for (int j = 0; j < nGenPerLoop; j++) {
                    rngSynch.nextDouble();
                }
            }
        });
        long synchTotal = System.currentTimeMillis() - startTime;

        // Run with ConcurrentCachedRNG
        startTime = System.currentTimeMillis();
        final RandomNumberGenerator rngCached = new ConcurrentCachedRNG(new UniformRng());
        parallelExecutor.forLoop(0, nLoops, new LoopBody() {

            @Override
            public void run(int i) throws Exception {
                for (int j = 0; j < nGenPerLoop; j++) {
                    rngCached.nextDouble();
                }
            }
        });
        long cachedTotal = System.currentTimeMillis() - startTime;

        System.out.printf("Benchmark results: Synchronized took %dms, ConcurrentCachedRNG took %dms%n", synchTotal, cachedTotal);
        assertTrue("ConcurrentCachedRNG is faster", cachedTotal < synchTotal);
    }
}
