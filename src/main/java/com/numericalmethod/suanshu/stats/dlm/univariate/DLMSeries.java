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
package com.numericalmethod.suanshu.stats.dlm.univariate;

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.dlm.univariate.DLMSim.Innovation;
import com.numericalmethod.suanshu.stats.random.univariate.normal.NormalRng;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This is a simulator for a multivariate controlled dynamic linear model process.
 * For <i>(t &ge; 1)</i>, a controlled DLM takes the following form:
 * <p/>
 * Observation Equation:
 * <blockquote><i>
 * y<sub>t</sub> = F<sub>t</sub> * x<sub>t</sub> + v<sub>t</sub>,
 * </i></blockquote>
 * State Equation:
 * <blockquote><i>
 * x<sub>t</sub> = G<sub>t</sub> * x<sub>t-1</sub> + H<sub>t</sub> * u<sub>t</sub> + w<sub>t</sub>,
 * </i></blockquote>
 * Given the model parameters, the time series of control variables <i>{<i>u<sub>t</sub></i>}</i> and an integer (length) <i>T</i>,
 * This simulator generates both the states <i>{<i>x<sub>t</sub></i>}</i> and observations <i>{<i>y<sub>t</sub></i>}</i>.
 * <p/>
 * This implementation is appropriate for a short time series when the size is known;
 * otherwise use instead {@link DLMSim}.
 *
 * @author Kevin Sun
 * @see "G. Petris et al., "Dynamic Linear Models with R," New York, Springer, 2009, ch. 2, pp. 31-84."
 */
public class DLMSeries implements com.numericalmethod.suanshu.stats.timeseries.TimeSeries<Integer, Innovation, DLMSeries.Entry> {

    /**
     * This is the {@code TimeSeries.Entry} for a univariate DLM time series.
     */
    public static class Entry implements com.numericalmethod.suanshu.stats.timeseries.TimeSeries.Entry<Integer, Innovation> {

        private final int time;
        private final Innovation value;

        private Entry(int time, Innovation value) {
            this.time = time;
            this.value = value;
        }

        @Override
        public Integer getTime() {
            return time;
        }

        @Override
        public Innovation getValue() {
            return value;
        }
    }

    /** the simulated states */
    private final double[] state;
    /** the simulated observations */
    private final double[] observation;

    /**
     * Simulate a univariate controlled dynamic linear model process.
     *
     * @param T     the length of the univariate time series (states and observations) to generate
     * @param model a univariate controlled dynamic linear model
     * @param u     a time series of control variables (length = <i>T</i>)
     * @param rnorm a standard Gaussian random number generator (for seeding)
     */
    public DLMSeries(int T, DLM model, double[] u, NormalRng rnorm) {
        this.state = new double[T];
        this.observation = new double[T];

        //simulation
        DLMSim sim = new DLMSim(model, rnorm);
        for (int t = 0; t < T; ++t) {
            Innovation value = sim.next(u[t]);
            state[t] = value.state;
            observation[t] = value.observation;
        }
    }

    /**
     * Simulate a univariate controlled dynamic linear model process.
     *
     * @param T     the length of the univariate time series (states and observations) to generate
     * @param model a univariate controlled dynamic linear model
     * @param u     a time series of control variables (length = <i>T</i>)
     */
    public DLMSeries(int T, DLM model, double[] u) {
        this(T, model, u, new NormalRng(0, 1));
    }

    /**
     * Simulate a univariate controlled dynamic linear model process.
     *
     * @param T     the length of the univariate time series (states and observations) to generate
     * @param model a univariate controlled dynamic linear model
     */
    public DLMSeries(int T, DLM model) {
        this(T, model, R.rep(0., T));
    }

    @Override
    public int size() {
        return state.length;
    }

    @Override
    public Iterator<Entry> iterator() {
        return new Iterator<DLMSeries.Entry>() {

            private int t = 0;

            @Override
            public boolean hasNext() {
                return t < state.length;
            }

            @Override
            public Entry next() {
                return new DLMSeries.Entry(t, new Innovation(state[t], observation[t]));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    /**
     * Get the states.
     *
     * @return the simulated states
     */
    public double[] getStates() {
        return Arrays.copyOf(state, state.length);
    }

    /**
     * Get the observations.
     *
     * @return the simulated observations
     */
    public double[] getObservations() {
        return Arrays.copyOf(observation, observation.length);
    }
}
