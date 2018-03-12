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
package com.numericalmethod.suanshu.stats.dlm.multivariate;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.CholeskyWang2006;
import com.numericalmethod.suanshu.stats.random.multivariate.NormalRvg;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

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
 *
 * @author Kevin Sun
 * @see "G. Petris et al., "Dynamic Linear Models with R," New York, Springer, 2009, ch. 2, pp. 31-84."
 */
public class DLMSim {

    /** a simulated innovation */
    public static class Innovation {

        /** the simulated state */
        public final ImmutableVector state;
        /** the simulated observation */
        public final ImmutableVector observation;

        Innovation(Vector state, Vector observation) {
            this.state = new ImmutableVector(state);
            this.observation = new ImmutableVector(observation);
        }
    }

    private int t = 0;
    private Vector x_lag;
    private final DLM model;
    private final NormalRvg rmvnorm;

    /**
     * Simulate a multivariate controlled dynamic linear model process.
     *
     * @param model   a multivariate controlled dynamic linear model
     * @param rmvnorm a standard multivariate Gaussian random vector generator (for seeding)
     */
    public DLMSim(DLM model, NormalRvg rmvnorm) {
        this.model = model;
        this.rmvnorm = rmvnorm;
    }

    /**
     * Get the next innovation.
     *
     * @param u the control vector as of time <i>t - 1</i>; used not for the first innovation (initial state)
     * @return a simulated innovation
     */
    public Innovation next(Vector u) {
        final StateEquation state = model.getStateModel();
        final ObservationEquation observation = model.getObservationModel();
        final int p = model.getStateDimension();
        final int d = model.getObsDimension();

        Vector x;
        if (t == 0) {
            Matrix A = new CholeskyWang2006(model.C0(), p);
            x = model.m0().add(A.multiply(new DenseVector(rmvnorm.nextVector())));
        } else {
            x = state.xt(t, x_lag, u != null ? u : null);
        }

        Vector y = observation.yt(t, x);

        //updates
        x_lag = x;
        ++t;

        return new Innovation(x, y);
    }
}
