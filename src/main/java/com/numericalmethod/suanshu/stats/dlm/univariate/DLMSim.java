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

import com.numericalmethod.suanshu.stats.random.univariate.normal.NormalRng;

/**
 * This is a simulator for a univariate controlled dynamic linear model process.
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
 *
 * @author Kevin Sun
 * @see "G. Petris et al., "Dynamic Linear Models with R," New York, Springer, 2009, ch. 2, pp. 31-84."
 */
public class DLMSim {

    /** a simulated innovation */
    public static class Innovation {

        /** the simulated state */
        public final double state;
        /** the simulated observation */
        public final double observation;

        Innovation(double state, double observation) {
            this.state = state;
            this.observation = observation;
        }
    }

    private int t = 0;
    private double x_lag;
    private final DLM model;
    private final NormalRng rnorm;

    /**
     * Simulate a univariate controlled dynamic linear model process.
     *
     * @param model a univariate controlled dynamic linear model
     * @param rnorm a standard Gaussian random number generator (for seeding)
     */
    public DLMSim(DLM model, NormalRng rnorm) {
        this.model = model;
        this.rnorm = rnorm;
    }

    /**
     * Get the next innovation.
     *
     * @param u the control as of time <i>t - 1</i>; used not for the first innovation (initial state)
     * @return a simulated innovation
     */
    public Innovation next(double u) {
        final StateEquation state = model.getStateModel();
        final ObservationEquation observation = model.getObservationModel();

        double x = 0;
        if (t == 0) {
            x = model.m0() + Math.sqrt(model.C0()) * rnorm.nextDouble();
        } else {
            x = state.xt(t, x_lag, u);
        }

        double y = observation.yt(t, x);

        //updates
        x_lag = x;
        ++t;

        return new Innovation(x, y);
    }
}
