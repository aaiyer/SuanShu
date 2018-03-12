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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.garch;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.NelderMead;
import com.numericalmethod.suanshu.optimization.unconstrained.quasinewton.BFGS;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.log;
import static java.lang.Math.max;
import java.util.Arrays;

/**
 * This class does fitting for the Generalized Autoregressive Conditional Heteroscedasticity (GARCH) getModel.
 *
 * <p>
 * This implementation does the fitting by maximizing the likelihood function, using the gradient information.
 *
 * <p>
 * The R equivalent functions are {@code garch} in {@code tseries} and {@code garchFit} in {@code fGarch}.
 *
 * @author Haksun Li
 *
 * @see "Bollerslev, Tim. <i>Generalized autoregressive conditional heteroskedasticity,</i> Journal of Econometrics, Issue 3, Vol. 31. 1986."
 */
public class GARCH {//TODO: need to use a constrained version of the BFGS algorithm

    /**
     * the gradient information used to guild the optimization search
     */
    public static enum GRADIENT {

        /**
         * use the analytical gradient in the references, eqs. 19, 21
         */
        ANALYTICAL,
        /**
         * use finite differencing to compute the gradient
         */
        NUMERICAL
    }

    private final int nparams;
    /**
     * the unconditional variance
     */
    private final double var;
    /**
     * the fitted GARCH getModel
     */
    private GARCHModel fit;

    /**
     * Fit the GARCH(p, q) getModel to the time series.
     *
     * @param xt            a time series of the observations
     * @param q             the ARCH order
     * @param p             the GARCH part order
     * @param maxIterations the maximum number of iterations in the numerical optimization algorithm
     */
    public GARCH(final TimeSeries xt, final int p, final int q, int maxIterations, GRADIENT grad) {
        nparams = 1 + q + p;
        final double[] e_t = xt.toArray();

        var = new Variance(e_t).value();
        final double[] e_t2 = foreach(
                e_t,
                new UnivariateRealFunction() {//e_t2 = e_t^2

                    @Override
                    public double evaluate(double x) {
                        return x * x;
                    }
                });

        RealScalarFunction mL = new RealScalarFunction() {//minimize the negative of the log-likelihood

            final RealScalarFunction L = logLikelihood(e_t2, p, q);

            @Override
            public Double evaluate(Vector x) {
                return -L.evaluate(x);
            }

            @Override
            public int dimensionOfDomain() {
                return L.dimensionOfDomain();
            }

            @Override
            public int dimensionOfRange() {
                return L.dimensionOfRange();
            }
        };

        RealVectorFunction g = dLogLikelihood(e_t2, p, q);//to use with analytical gradient

        final double small = 0.05;
        Vector xmin = new DenseVector(nparams, small);
        xmin.set(1, var * (0.8 - small * (q + p)));//TODO: why 0.8? it is 1.0 from garch in R's tseries
//        xmin.set(1, var);

        C2OptimProblemImpl problem = grad == GRADIENT.NUMERICAL ? new C2OptimProblemImpl(mL) : new C2OptimProblemImpl(mL, g);

        NelderMead optim1 = new NelderMead(0, maxIterations);
        NelderMead.Solution soln1 = optim1.solve(problem);
        xmin = soln1.search(xmin);

        try {
            BFGS optim2 = new BFGS(false, 0, maxIterations);
            IterativeMinimizer<Vector> soln2 = optim2.solve(problem);
            for (int i = 0; i < maxIterations; ++i) {
                Vector xmin1 = (Vector) soln2.step();
                double[] theta = xmin1.toArray();
                if (getGarchModel(theta, p, q) == null) {//check if the new estimation is valid
                    break;//use the last good estimation
                }
                xmin = xmin1;
            }
        } catch (Exception ex) {//TODO: better way of writing this?
            //it can throw exception if during optimization, an invalid parameter set is tested, e.g., in BFGS
            //ignore
        }

        double[] coeff = xmin.toArray();

        fit = getGarchModel(coeff, p, q);
    }

    /**
     * Fit the GARCH(p, q) getModel to the time series.
     *
     * @param xt a time series of the observations
     * @param q  the ARCH order
     * @param p  the GARCH part order
     */
    public GARCH(final TimeSeries xt, final int p, final int q) {
        this(xt, p, q, 300, GRADIENT.ANALYTICAL);
    }

    /**
     * Get the fitted GARCH getModel.
     *
     * @return the fitted GARCH getModel, the estimators.
     */
    public GARCHModel getModel() {
        return fit;
    }

    /**
     * the log-likelihood function for a set of observations
     *
     * <p>
     * The log-likelihood takes θ as the inputs.
     *
     * @param e_t2 squared observations
     * @param q    the ARCH order
     * @param p    the GARCH part order
     * @return log-likelihood function for a set of observations
     *
     * @see "Bollerslev, Tim. "Eq. 18. Generalized autoregressive conditional heteroskedasticity," Journal of Econometrics, Issue 3, Vol. 31. 1986."
     */
    private RealScalarFunction logLikelihood(final double[] e_t2, final int p, final int q) {
        return new RealScalarFunction() {

            final int maxPQ = max(p, q);

            @Override
            public Double evaluate(Vector theta) {//θ = {α0, {α_i}, {β_i}}
                GARCHModel model = getGarchModel(theta.toArray(), p, q);

                if (model == null) {
                    return -Double.MAX_VALUE;
                }

                double[] h = new double[e_t2.length];
                for (int t = 0; t < maxPQ; ++t) {
                    h[t] = var;//initialization
                }

                double L = 0;
                for (int t = maxPQ; t < e_t2.length; ++t) {
                    double[] e_t2_lag = Arrays.copyOfRange(e_t2, t - q, t);
                    reverse(e_t2_lag);//reverse the time order so that it goes from e2_{t-1} to e2_{t-q}

                    double[] h_lag = Arrays.copyOfRange(h, t - p, t);
                    reverse(h_lag);//reverse the time order so that it goes from h_{t-1} to h_{t-p}

                    h[t] = model.sigma2(e_t2_lag, h_lag);

                    double l = l_t(h[t], e_t2[t]);//the log-likelihood for one sample

                    L += l;
                }

                L /= (e_t2.length - maxPQ);
                return L;
            }

            @Override
            public int dimensionOfDomain() {
                return nparams;
            }

            @Override
            public int dimensionOfRange() {
                return 1;
            }
        };
    }

    /**
     * the gradient of the log-likelihood function for a set of observations
     *
     * <p>
     * The gradient log-likelihood takes θ as the inputs.
     *
     * @param e_t2 the squared observations
     * @param p    the number of AR terms
     * @param q    the number of MA terms
     * @return the gradient of the log-likelihood function
     *
     * @see "Bollerslev, Tim. "Eqs. 19, 21. Generalized autoregressive conditional heteroskedasticity," Journal of Econometrics, Issue 3, Vol. 31. 1986."
     */
    private RealVectorFunction dLogLikelihood(final double[] e_t2, final int p, final int q) {
        return new RealVectorFunction() {

            final int maxPQ = max(p, q);

            @Override
            public Vector evaluate(Vector theta) {//θ = {α0, {α_i}, {β_i}}
                double[] b = Arrays.copyOfRange(theta.toArray(), q + 1, nparams);//theta.length = nparams

                GARCHModel model = getGarchModel(theta.toArray(), p, q);

                Vector[] dh = new Vector[e_t2.length];
                for (int t = 0; t < maxPQ; ++t) {
                    dh[t] = new DenseVector(nparams, 0);//initialization
                    dh[t].set(1, 1);//dh/da0 = 1
                    //TODO: shall we set the rest to e_t2 and h_t instead of 0s?
                }

                double[] h = new double[e_t2.length];
                for (int t = 0; t < maxPQ; ++t) {
                    h[t] = var;//initialization
                }

                Vector dL = new DenseVector(nparams, 0);
                for (int t = maxPQ; t < e_t2.length; ++t) {
                    double[] e_t2_lag = Arrays.copyOfRange(e_t2, t - q, t);
                    reverse(e_t2_lag);//reverse the time order so that it goes from e2_{t-1} to e2_{t-q+1}

                    double[] h_lag = Arrays.copyOfRange(h, t - p, t);
                    reverse(h_lag);//reverse the time order so that it goes from h_{t-1} to h_{t-p+1}

                    h[t] = model.sigma2(e_t2_lag, h_lag);

                    dh[t] = new DenseVector(concat(
                            new double[]{1},
                            e_t2_lag,
                            h_lag));//z_t in eq. 21; text just above eq. 17

                    for (int i = 1; i <= p; ++i) {//eq. 21
                        Vector bdh = dh[t - i].scaled(b[i - 1]);
                        dh[t] = dh[t].add(bdh);
                    }

                    Vector dl = dh[t].scaled(0.5 / h[t]);//eq. 19
                    dl = dl.scaled(e_t2[t] / h[t] - 1);

                    dL = dL.add(dl);
                }

                dL = dL.scaled(1.0 / (e_t2.length - maxPQ));

                return dL;
            }

            @Override
            public int dimensionOfDomain() {
                return nparams;
            }

            @Override
            public int dimensionOfRange() {
                return 1;
            }
        };
    }

    //@see Bollerslev, Tim. "Eq. 18. Generalized autoregressive conditional heteroskedasticity," Journal of Econometrics, Issue 3, Vol. 31. 1986.
    private static double l_t(double ht, double e_t2) {
        double l_t = log(ht);
        l_t += e_t2 / ht;
        l_t *= -0.5;

        return l_t;
    }

    private GARCHModel getGarchModel(double[] theta, int p, int q) {
        try {
            return new GARCHModel(theta[0],
                    Arrays.copyOfRange(theta, 1, q + 1),
                    Arrays.copyOfRange(theta, q + 1, nparams));
        } catch (Exception ex) {
            return null;
        }
    }
}
