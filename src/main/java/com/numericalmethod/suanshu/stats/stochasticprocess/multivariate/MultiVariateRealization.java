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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate;

import com.numericalmethod.suanshu.stats.random.univariate.normal.StandardNormalRng;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;

/**
 * This interface defines the realization for a multivariate stochastic process, as well as
 * the <tt>Iterator</tt> for generating (reading) the realization.
 *
 * <p>
 * According to the Lévy–Khintchine representation, for a stochastic process, we have
 * <ul>
 * the absolutely continuous part such that the increment dB is proportional to the square root of time increment dt
 * </ul>
 *
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/L%C3%A9vy_process#L.C3.A9vy.E2.80.93It.C5.8D_decomposition">Lévy–Itō decomposition</a>
 */
public interface MultiVariateRealization extends com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.MultiVariateRealization {

    /**
     * This <tt>Iterator</tt> support lazy evaluation/generation of a realization from a stochastic process.
     * For a given filtration, a stochastic process gives arise to a particular realization,
     * which is a (deterministic) time series.
     *
     * A realization is therefore only created on demand.
     * For example, we create a realization when an <tt>Iterator</tt> is constructed.
     */
    public abstract class Iterator implements java.util.Iterator<MultiVariateRealization.Entry> {

        private int index = 0;// count from 1
        /**
         * the dimension of the Brownian motion
         */
        public final int d;
        private final int size;
        private final StandardNormalRng rnorm;// TOOD: use a vectored rnrom?

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
        public abstract Vector xt(int index);

        /**
         * Construct a realization of a multivariate stochastic process.
         *
         * @param d the dimension of the Brownian motion
         * @param size the length of the time series
         * @param seed seeding the same {@code seed} gives arise to the same realization
         */
        public Iterator(int d, int size, long seed) {
            this.d = d;
            this.size = size;
            this.rnorm = new StandardNormalRng();
            this.rnorm.seed(seed);
        }

        public boolean hasNext() {
            return index < size;
        }

        @Override
        public MultiVariateRealization.Entry next() {
            ++index;
            MultiVariateRealization.Entry entry = new MultiVariateRealization.Entry(t(index), xt(index));
            return entry;
        }

        public Vector nextValue() {
            return xt(++index);
        }

        /**
         * Get a d-dimension Gaussian innovation.
         * 
         * @return a Gaussian innovation
         */
        protected Vector Zt() {
            Vector Zt = new DenseVector(d);

            for (int i = 1; i <= d; ++i) {
                double zt = rnorm.nextDouble();
                Zt.set(i, zt);
            }

            return Zt;
        }

        //TODO: extend to the Lévy triplet
        //http://en.wikipedia.org/wiki/L%C3%A9vy_process#L.C3.A9vy.E2.80.93It.C5.8D_decomposition
        protected Vector dB(double dt) {
            Vector dB = Zt().scaled(sqrt(dt));
            return dB;
        }
    }

    public MultiVariateRealization.Iterator iterator();

    /**
     * Get the ending value of a realization,
     * i.e., the value at the end of the time interval, e.g., ω(T).
     * 
     * @return the ending value of a realization
     */
    public abstract Vector lastValue();
}
