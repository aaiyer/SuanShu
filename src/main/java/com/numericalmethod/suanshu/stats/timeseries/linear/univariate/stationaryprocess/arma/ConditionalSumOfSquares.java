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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma;

import static com.numericalmethod.suanshu.analysis.function.FunctionOps.dotProduct;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.Riemann;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.unconstrained.NelderMead;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;
import java.util.Arrays;

/**
 * This class does fitting for an ARIMA model by minimizing the conditional sum of squares (CSS).
 * The CSS estimates are conditional on the assumption that the past unobserved errors are 0s.
 *
 * <p>
 * Note that the order of integration is taken as an input, not estimated.
 *
 * <p>
 * The values produced here can be used as a starting point for the maximum likelihood algorithm.
 *
 * <p>
 * The R equivalent functions is {@code arima}.
 *
 * @author Chun Yip Yau
 *
 * @see "P. J. Brockwell and R. A. Davis, "Chapter 8.7. Model Building and Forecasting with ARIMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 */
public class ConditionalSumOfSquares implements ARMAFitting {

    /**
     * the estimated mean of the model
     */
    private final double mu;
    /**
     * the estimated ARMA model and coefficients and variance for the white noise
     */
    private final Estimators estimators;
    /**
     * the length of the observations
     */
    private final int n;
    /**
     * the maximum likelihood
     */
    private final double maxLikelihood;

    /**
     * the parameters are, in order, the φ, θ, and σ^2 in Eq. 8.7.1;
     * Brockwell and Davis fits a zero-mean ARMA model.
     * θ0 = 1 is not included
     */
    private static class Estimators {

        final double[] phi;
        final double[] theta;
        final double var;

        private Estimators(double[] coefficients, int p, int q) {
            phi = Arrays.copyOfRange(coefficients, 0, p);
            theta = Arrays.copyOfRange(coefficients, p, p + q);
            var = coefficients[p + q];
        }

        int p() {
            return phi.length;
        }

        int q() {
            return theta.length;
        }
    }

    /**
     * Fit an ARIMA model for the observations.
     *
     * <p>
     * If the differenced input time series is not zero-mean, we first de-mean it before running the algorithm as in Brockwell and Davis.
     * When we output the model, we will compute the intercept to match the mean.
     *
     * <p>
     * We only fit an ARMA model. {@code d} is supplied as an argument.
     *
     * @param xt the time series of observations
     * @param p  the number of AR terms
     * @param d  the order of integration
     * @param q  the number of MA terms
     */
    public ConditionalSumOfSquares(TimeSeries xt, final int p, final int d, final int q) {
        this.n = xt.size();

        //make stationary (ARMA) by differencing when d > 0
        double[] dxt0 = xt.toArray();
        if (d > 0) {
            dxt0 = R.diff(dxt0, 1, d);
        }

        //demean
        this.mu = new Mean(dxt0).value();//the mean/intercept of the stationary time series
        double[] dxt1 = new double[dxt0.length];
        for (int i = 0; i < dxt0.length; ++i) {
            dxt1[i] = dxt0[i] - this.mu;
        }

        RealScalarFunction nL = nLogLikelihood(p, q, dxt1);
        NelderMead optim = new NelderMead(0, 500);//TODO: make this a parameter
        NelderMead.Solution soln = optim.solve(new C2OptimProblemImpl(nL));
        Vector xmin = new DenseVector(p + q + 1);//initial ar, ma are 0s
        xmin.set(p + q + 1, 0.01);//initial var, very small
        xmin = soln.search(xmin);

        this.maxLikelihood = -1 * nL.evaluate(xmin);
        this.estimators = new Estimators(xmin.toArray(), p, q);
    }

    @Override
    public ARMAModel getFittedARMA() {
        double intercept = 1;
        for (int i = 0; i < estimators.phi.length; ++i) {
            intercept -= estimators.phi[i];
        }
        intercept *= this.mu;

        ARMAModel fitted = new ARMAModel(intercept, estimators.phi, estimators.theta, estimators.var);
        return fitted;
    }

    @Override
    public double var() {
        return estimators.var;
    }

    /**
     * Compute the asymptotic covariance matrix for the estimated parameters, φ and θ.
     *
     * <p>
     * The estimators are asymptotically normal.
     *
     * @return the asymptotic covariance matrix
     *
     * @see "P. J. Brockwell and R. A. Davis, "Eq. 10.8.27. Thm. 10.8.2. Chapter 10.8. Model Building and Forecasting with ARIMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
     */
    @Override
    public Matrix covariance() {
        int p = this.estimators.p();
        int q = this.estimators.q();
        int pq = p + q;

        Riemann I = new Riemann();
        Matrix cov = new DenseMatrix(pq, pq);

        for (int j = 1; j <= pq; ++j) {
            for (int k = j; k <= pq; ++k) {
                final UnivariateRealFunction dgdj = dlogg(j);
                final UnivariateRealFunction dgdk = dlogg(k);

                UnivariateRealFunction integrand = new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        double term1 = dgdj.evaluate(x);
                        double term2 = dgdk.evaluate(x);
                        double result = term1 * term2;
                        return result;
                    }
                };

                double Wjk = I.integrate(integrand, -PI, PI);
                Wjk /= 4. * PI;

                cov.set(j, k, Wjk);
                cov.set(k, j, Wjk);
            }
        }

        Matrix result = new Inverse(cov).scaled(1. / n);
        return result;
    }

    /**
     * Compute the asymptotic standard errors for the estimated parameters, φ and θ.
     *
     * <p>
     * The estimators are asymptotically normal.
     *
     * @return the asymptotic errors
     *
     * @see "P. J. Brockwell and R. A. Davis, "Eq. 10.8.27. Thm. 10.8.2. Chapter 10.8. Model Building and Forecasting with ARIMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
     */
    @Override
    public ImmutableVector stderr() {
        Matrix cov = covariance();

        int pq = this.estimators.p() + this.estimators.q();
        Vector stderr = new DenseVector(pq);

        for (int i = 1; i <= pq; ++i) {
            stderr.set(i, sqrt(cov.get(i, i)));
        }

        return new ImmutableVector(stderr);
    }

    /**
     * Compute the number of parameters for the estimation/fitting.
     *
     * <p>
     * the AR terms, MA terms, and variance (sigma^2)
     *
     * @return the number of parameters
     */
    public int nParams() {
        return estimators.p() + estimators.q() + 1;
    }

    /**
     * Compute the AIC, a model selection criterion.
     *
     * @return the AIC
     *
     * @see <a href="http://en.wikipedia.org/wiki/Akaike_information_criterion">Wikipedia: Akaike information criterion</a>
     */
    @Override
    public double AIC() {
        double logL = log(maxLikelihood);
        double AIC = -2.0 * logL + 2.0 * nParams();
        return AIC;
    }

    /**
     * Compute the AICC, a model selection criterion.
     *
     * @return the AICC
     *
     * @see "P. J. Brockwell and R. A. Davis, "Eq. 9.2.1. Chapter 9.2. Model Building and Forecasting with ARIMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
     */
    @Override
    public double AICC() {
        double logL = log(maxLikelihood);
        double AICC = -2.0 * logL + 2.0 * nParams() * n / (n - nParams() - 1);
        return AICC;
    }

    @Override
    public String toString() {
        return getFittedARMA().toString();
    }

    /**
     * Compute the negative of the log-likelihood function from observations.
     *
     * @param p    the number of AR terms
     * @param q    the number of MA terms
     * @param arma a stationary ARMA series of observations
     * @return the negative of the log-likelihood function
     *
     * @see "P. J. Brockwell and R. A. Davis, "Eq. 8.7.4. Chapter 8.7. Model Building and Forecasting with ARIMA Processes," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
     */
    private static RealScalarFunction nLogLikelihood(final int p, final int q, final double[] arma) {
        final int maxPQ = max(p, q);
        final int length = arma.length;

        //CSS assumes that the unobserved past observations are 0 with size = maxPQ
        final double[] xtExtended = DoubleUtils.concat(R.rep(0.0, maxPQ), arma);//the augmented time series of observations
        final int lengthExtended = xtExtended.length;

        RealScalarFunction f = new RealScalarFunction() {

            @Override
            public Double evaluate(Vector params) {
                Estimators estimators = new Estimators(params.toArray(), p, q);
                double var = estimators.var;
//                var = var > 0 ? var : 0.01;//R uses this to make variance always +ve.
                if (DoubleUtils.isNegative(var, 0)) {
                    return Double.MAX_VALUE;
                }

                //compute the random error terms/innovations/white noise
                double[] Z = new double[lengthExtended];//the Z's (white noise) in eq. 8.7.1
                for (int i = 0; i < length; ++i) {
                    int t = maxPQ + i;

                    double[] x_lag = new double[p];
                    double[] z_lag = new double[q];

                    if (p > 0) {
                        for (int j = 0; j < p; ++j) {
                            x_lag[j] = xtExtended[t - j - 1];
                        }
                    }

                    if (q > 0) {
                        for (int j = 0; j < q; ++j) {
                            z_lag[j] = Z[t - j - 1];
                        }
                    }

                    double Xt_hat = dotProduct(estimators.phi, x_lag) + dotProduct(estimators.theta, z_lag);//as in Eq. 8.7.8
                    Z[t] = xtExtended[t] - Xt_hat;//θ0 = 1
                }

                /*
                 * This is the log-likelihood function in Eq. 8.7.4 except that
                 * it discards the constant term, and
                 * it has a different sign b/c we do minimization instead of maximization.
                 * Also, we assume r_i be asymptotically 1.
                 */
                double CSS = dotProduct(Z, Z);//the conditional sum of squares
                double nL = length * log(var) + CSS / var;
                return nL;
            }

            @Override
            public int dimensionOfDomain() {
                return (q + p + 1);//the AR terms, MA terms, and variance (sigma^2)
            }

            @Override
            public int dimensionOfRange() {
                return 1;
            }
        };

        return f;
    }

    /**
     * This implements dlogg/dφ or dlogg/dθ.
     *
     * To understand this implementation, we first make a dlogg/dφ into a fraction form.
     * We expand both the numerator and denominator.
     * Then we find that both the numerator and denominator are a real function in λ.
     * All computation can be done in the Real number domain, though the equations appear to be Complex.
     *
     * To get rid of the complex i, we use the Euler formula:
     * e<sup>ix</sup> + e<sup>-ix</sup> = 2cos(x)
     *
     * @param j an index between 1 and p + q.
     * @return an integrand that is a function of λ
     */
    private UnivariateRealFunction dlogg(final int j) {
        final double[] coeff = j <= estimators.p() ? this.estimators.phi : this.estimators.theta;

        UnivariateRealFunction result = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {//x = lambda
                //2cos(jλ) - 2φ<sub>1</sub>cos(jλ-λ) - 2φ<sub>2</sub>cos(jλ-2λ) - 2φ<sub>3</sub>cos(jλ-3λ) - ... - 2φ<sub>p</sub>cos(jλ-pλ)
                double numerator = 2. * cos(j * x);
                for (int r = 1; r <= coeff.length; ++r) {
                    numerator -= 2 * coeff[r - 1] * cos(j * x - x);
                }

                //1 + Σ
                double denominator = 1;
                for (int r = 0; r < coeff.length; ++r) {
                    denominator += coeff[r] * coeff[r];
                    denominator -= 2. * coeff[r] * cos(x);
                }

                for (int r = 1; r <= coeff.length; ++r) {
                    for (int s = r + 1; s <= coeff.length; ++s) {
                        denominator += 2. * coeff[r - 1] * coeff[s - 1] * cos((r - s) * x);
                    }
                }

                return numerator / denominator;
            }
        };

        return result;
    }
}
