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

import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;
import com.numericalmethod.suanshu.stats.random.concurrent.ConcurrentCachedGenerator.Generator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Ken Yiu
 */
public class ConcurrentCachedGeneratorTest {

    /**
     * Mocks a non-thread-safe RNG by generating sequential numbers.
     */
    private static class SequentialGenerator implements Generator<Integer> {

        private int nextNumber = 1;

        @Override
        public Integer next() {
            return nextNumber++;
        }

        public int nGenerated() {
            return nextNumber - 1;
        }
    }

    // Use sequential gen with multiple threads and check that there is no duplicates
    @Test
    public void test_noDuplicates_0010() throws MultipleExecutionException {
        SequentialGenerator generator = new SequentialGenerator();
        final ConcurrentCachedGenerator<Integer> concurrentGenerator = new ConcurrentCachedGenerator<Integer>(generator, 1000);
        int nNum = 1000000;
        final List<Integer> numbers = Collections.synchronizedList(new ArrayList<Integer>(nNum));

        // get 1 million numbers in parallel
        int nLoops = 100;
        final int nGenPerLoop = nNum / nLoops;
        ParallelExecutor parallelExecutor = new ParallelExecutor();
        parallelExecutor.forLoop(0, nLoops, new LoopBody() {

            @Override
            public void run(int i) throws Exception {
                for (int j = 0; j < nGenPerLoop; j++) {
                    numbers.add(concurrentGenerator.next());
                }
            }
        });

        int[] sortedNumbers = collection2IntArray(numbers);
        Arrays.sort(sortedNumbers);

        // check no duplicates AND no skipped numbers
        for (int i = 0; i < sortedNumbers.length; i++) {
            assertEquals(i + 1, sortedNumbers[i]);
        }

        int nGenerated = generator.nGenerated();
        int nStored = numbers.size();
        int maxStored = sortedNumbers[sortedNumbers.length - 1];
        int nSkipped = maxStored - nStored;
        System.out.printf("generated: %d, stored: %d, max: %d, skipped: %d%n",
                nGenerated, nStored, maxStored, nSkipped);

        assertTrue("generated must be more than or equal to stored", nGenerated >= nStored);
        assertEquals("no skipped number", 0, nSkipped);
    }
}
