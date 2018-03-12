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
package com.numericalmethod.suanshu.stats.sampling.discrete;

import com.numericalmethod.suanshu.number.Counter;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.stats.distribution.ProbabilityMassFunction;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DiscreteSamplingTest {

    @Test
    public void test_0010() {
        final double pm = 0.2;
        DiscreteSampling<Integer> instance = new DiscreteSampling<Integer>(
                DoubleUtils.intArray2List(new int[]{1, 2, 3, 4, 5}),
                new ProbabilityMassFunction<Integer>() {

                    public double evaluate(Integer x) {
                        return pm;
                    }
                });

        UniformRng rng = new UniformRng();
        Counter counter = new Counter();

        int N = 10000;
        for (int i = 0; i < N; ++i) {
            counter.add(instance.getSample(rng.nextDouble()));
        }

        assertEquals((double) counter.count(1) / N, pm, 1e-1);
        assertEquals((double) counter.count(2) / N, pm, 1e-1);
        assertEquals((double) counter.count(3) / N, pm, 1e-1);
        assertEquals((double) counter.count(4) / N, pm, 1e-1);
        assertEquals((double) counter.count(5) / N, pm, 1e-1);
    }

    @Test
    public void test_0020() {
        final double pm = 0.2;
        DiscreteSampling<Integer> instance = new DiscreteSampling<Integer>(
                new Iterable<Integer>() {

                    public Iterator<Integer> iterator() {
                        return new Iterator<Integer>() {

                            private final int[] collection = new int[]{3, 4, 2, 5, 1};
                            private int index = 0;

                            public boolean hasNext() {
                                return index < collection.length;
                            }

                            public Integer next() {
                                return collection[index++];
                            }

                            public void remove() {
                                throw new UnsupportedOperationException("Not supported yet.");
                            }
                        };
                    }
                },
                new ProbabilityMassFunction<Integer>() {

                    public double evaluate(Integer x) {
                        return pm;
                    }
                });

        UniformRng rng = new UniformRng();
        Counter counter = new Counter();

        int N = 10000;
        for (int i = 0; i < N; ++i) {
            counter.add(instance.getSample(rng.nextDouble()));
        }

        assertEquals((double) counter.count(1) / N, pm, 1e-1);
        assertEquals((double) counter.count(2) / N, pm, 1e-1);
        assertEquals((double) counter.count(3) / N, pm, 1e-1);
        assertEquals((double) counter.count(4) / N, pm, 1e-1);
        assertEquals((double) counter.count(5) / N, pm, 1e-1);
    }
}
