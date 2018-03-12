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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate;

import com.numericalmethod.suanshu.stats.random.univariate.normal.StandardNormalRng;
import static java.lang.Math.sqrt;

/**
 * This interface defines the <tt>Iterator</tt> for generating (reading) a realization of a univariate random process.
 *
 * <p>
 * According to the Lévy–Khintchine representation, for a stochastic process, we have
 * <ul>
 * the absolutely continuous part such that the increment dB is proportional to the square root of time increment dt
 * </ul>
 *
 * @author Haksun Li
 */
public interface Realization extends com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.Realization {

    /**
     * This <tt>Iterator</tt> support lazy evaluation/generation of a realization from a stochastic process.
     * For a given filtration, a stochastic process gives arise to a particular realization,
     * which is a (deterministic) time series.
     *
     * A realization is therefore only created on demand.
     * For example, we create a realization when an <tt>Iterator</tt> is constructed.
     */
    public abstract class Iterator implements java.util.Iterator<Realization.Entry> {

        private int index = 0;// count from 1
        private final int size;
        private final StandardNormalRng rnorm;

        /**
         * Get the current timestamp of the realization.
         *
         * @param index the index to the current <tt>Entry</tt>
         * @return the current timestamp
         */
        public abstract double t(int index);

        /**
         * Get the current value of the realization.
         *
         * @param index the index to the current <tt>Entry</tt>
         * @return the current value
         */
        public abstract double xt(int index);

        /**
         * Construct a realization of a univariate stochastic process.
         *
         * @param size length of the time series
         * @param seed seeding the same {@code seed} gives arise to the same realization
         */
        public Iterator(int size, long seed) {
            this.size = size;
            this.rnorm = new StandardNormalRng();
            this.rnorm.seed(seed);
        }

        public boolean hasNext() {
            return index < size;
        }

        public Realization.Entry next() {
            ++index;

            Realization.Entry entry = new Realization.Entry(t(index), xt(index));
            return entry;
        }

        public double nextValue() {
            return xt(++index);
        }

        /**
         * Get a Gaussian innovation.
         * 
         * @return a Gaussian innovation
         */
        protected double Zt() {
            double zt = rnorm.nextDouble();
            return zt;
        }

        //TODO: extend to the Lévy triplet
        //http://en.wikipedia.org/wiki/L%C3%A9vy_process#L.C3.A9vy.E2.80.93It.C5.8D_decomposition
        protected double dB(double dt) {
            double dB = sqrt(dt) * Zt();
            return dB;
        }
    }

    public Realization.Iterator iterator();

    /**
     * Get the ending value of a realization,
     * i.e., the value at the end of the time interval, e.g., ω(T).
     *
     * @return the ending value of a realization
     */
    public abstract double lastValue();
}
