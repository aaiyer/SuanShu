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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.ErfInverse;
import com.numericalmethod.suanshu.analysis.uniroot.Halley;
import com.numericalmethod.suanshu.analysis.uniroot.NoRootFoundException;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import static java.lang.Math.*;

/**
 * The inverse of the Regularized Incomplete Gamma P function is defined as:
 * \[
 * x = P^{-1}(s,u), 0 \geq u \geq 1
 * \]
 * <ul>
 * <li>When {@code s > 1}, we use the asymptotic inversion method.
 * <li>When {@code s <= 1}, we use an approximation of <i>P(s,x)</i> together with a higher-order Newton like method.
 * </ul>
 * In both cases, the estimated value is then improved using Halley's method, c.f., {@link Halley}
 * <p/>
 * The R equivalent function is {@code qgamma}. E.g., {@code qgamma(u, s, lower=TRUE)}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Amparo Gil, Javier Segura, and Nico M. Temme. "Sections 8.3, 10.3.1, 10.6," Numerical Methods for Special Functions."
 * <li><a href="http://en.wikipedia.org/wiki/Regularized_Gamma_function#Regularized_Gamma_functions_and_Poisson_random_variables">Wikipedia: Regularized Gamma functions and Poisson random variables</a>
 * </ul>
 */
public class GammaRegularizedPInverse extends BivariateRealFunction {

    private static final Gamma gamma = new GammaLanczosQuick();
    private static final LogGamma lgamma = new LogGamma();
    private static final GammaRegularizedP pgamma = new GammaRegularizedP();
    //eq. 10.39
    private static final Polynomial p1 = new Polynomial(new double[]{-11 / 382725d, 5 / 18144d, -7 / 6480d, 1 / 1620d, 1 / 36d, -1 / 3d});
    private static final Polynomial p2 = new Polynomial(new double[]{109 / 1749600d, -1579 / 2099520d, 533 / 204120d, -7 / 2592d, -7 / 405d});
    private static final Polynomial p3 = new Polynomial(new double[]{346793 / 5290790400d, 29233 / 36741600d, -63149 / 20995200d, 449 / 102060d});
//        private static final Polynomial p4 = new Polynomial(new double[]{636178018081d / 48260539847520000d, -16004851139d / 26398927779840000d, -16968489929d / 194992080192000d, 1981235233 / 6666395904000d, -449882243 / 982102968000d, -269383 / 4232632320d, 319 / 183708d});
    private static final Polynomial p4 = new Polynomial(new double[]{1981235233 / 6666395904000d, -449882243 / 982102968000d, -269383 / 4232632320d, 319 / 183708d});
    //a rough approximation from eq. 10.42; might not even give +ve lambda for some eta
    private static final Polynomial pLambda = new Polynomial(new double[]{1 / 4320d, -1 / 270d, 1 / 36d, 1 / 3d, 1, 1});

    /**
     * Evaluate <i>x = P<sup>-1</sup>(s,u)</i>.
     *
     * @param s <i>s &gt; 0</i>
     * @param u <i>0 &le; u &le; 1</i>
     * @return <i>P<sup>-1</sup>(s,u)</i>
     */
    @Override
    public double evaluate(final double s, final double u) {
        SuanShuUtils.assertArgument(s > 0, "s must be > 0");

        try {
            double guess = 0;

            if (isZero(u, 0)) {
                return 0;
            } else if (compare(u, 1, 0) == 0) {// u == 1
                return Double.POSITIVE_INFINITY;
            } else if (compare(s, 1, 0) <= 0) {//s <= 1
                guess = evaluateByApproximation(s, u);
            } else {//s >  1
                guess = evaluateByAsymptoticInversion(s, u);
            }

            /* improve the initial guess (estimate) by Halley's method */

            //<i>u</i> is the root for <i>f</i>
            UnivariateRealFunction f = new UnivariateRealFunction() {

                @Override
                public double evaluate(double x) {
                    return pgamma.evaluate(s, x) - u;
                }
            };

            //df = dP/dx = (x<sup>s-1</sup> * e<sup>-x</sup>) / Γ(s)
            UnivariateRealFunction df = new UnivariateRealFunction() {

                @Override
                public double evaluate(double x) {
                    double df = -x;
                    df += (s - 1) * log(x);
                    df -= lgamma.evaluate(s);
                    df = exp(df);
                    return df;
                }
            };

            /*
             * d2f = d2P/dx2 = ((s-1) * x<sup>s-2</sup> * e<sup>-x</sup> - x<sup>s-1</sup> * e<sup>-x</sup>) / Γ(s)
             * = x<sup>s-2</sup> * e<sup>-x</sup> * (-x + s - 1) / Γ(s)
             */
            UnivariateRealFunction d2f = new UnivariateRealFunction() {

                @Override
                public double evaluate(double x) {
                    double d2f = -x;
                    d2f += (s - 2d) * log(x);
                    d2f -= lgamma.evaluate(s);
                    d2f = exp(d2f);
                    d2f *= (-x + s - 1d);
                    return d2f;
                }
            };

            Halley halley = new Halley(1e-16, 50);
            double result = halley.solve(f, df, d2f, guess);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Evaluate the inverse of the regularized incomplete Gamma P function by the asymptotic inversion method.
     *
     * @param s <i>s &gt; 0</i>
     * @param u <i>0 &le; u &le; 1</i>
     * @return <i>P<sup>-1</sup>(s,u)</i>
     * @see "Amparo Gil, Javier Segura, and Nico M. Temme, "Sections 8.3, 10.3.1, 10.6," Numerical Methods for Special Functions."
     */
    double evaluateByAsymptoticInversion(double s, double u) throws NoRootFoundException {
        ErfInverse erfInv = new ErfInverse();
        double eta0 = erfInv.evaluate(1 - 2 * u) / sqrt(s / 2);//eqs. 8.17, 10.14
        eta0 *= -1;

        double e1 = p1.evaluate(eta0);
        double e2 = p2.evaluate(eta0);
        double e3 = p3.evaluate(eta0);
        double e4 = p4.evaluate(eta0);

        double en = (e1 + (e2 + (e3 + e4 / s) / s) / s) / s;//eq. 10.17
        double eta = eta0 + en;//eq. 10.16
        double lambda = pLambda.evaluate(eta);

        //solve for lambda, if lambda is -ve
        if (lambda < 0) {
            final double half_nu_sq = 0.5 * eta0 * eta0;
            Halley halley = new Halley(EPSILON, 50);
            lambda = halley.solve(
                    new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double x) {
                            double f = x - 1 - log(x) - half_nu_sq;//solve for f(λ) = λ - 1 - log(λ) - 0.5(ηη) = 0
                            return f;
                        }
                    },
                    new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double x) {
                            double f = 1d - 1d / x;//f'(λ) = 1 - 1/λ
                            return f;
                        }
                    },
                    new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double x) {
                            double f = 1d - 1d / x / x;//f'(λ) = 1 - 1/(λλ)
                            return f;
                        }
                    },
                    half_nu_sq);
        }

        double x = s * lambda;//lambda must be > 0
        return x;
    }

    /**
     * We use the approximation:
     * \[
     * u \approx P(s,x) \approx \frac{x^s}{\Gamma(s+1)}
     * \]
     *
     * @param s <i>s &gt; 0</i>
     * @param u <i>0 &le; u &le; 1</i>
     * @return <i>P<sup>-1</sup>(s,u)</i>
     */
    double evaluateByApproximation(final double s, final double u) {
        double ug = u * gamma.evaluate(s + 1);
        double x0 = pow(ug, 1 / s);

        //compute the adjustment <i>h</i>
        double fx0 = pgamma.evaluate(s, x0) - u;

        /*
         * <i>c1</i>, <i>c2</i>, <i>c3</i>, <i>c4</i> are for computing the adjustment <i>h</i>.
         * @see "Amparo Gil, Javier Segura, and Nico M. Temme, "Equation 10.128," Numerical Methods for Special Functions."
         */
        double c1 = -pow(x0, 1 - s);
        c1 *= exp(x0);
        c1 *= gamma.evaluate(s);

        double c2 = c1 * c1;
        c2 *= x0 + 1 - s;
        c2 /= 2 * x0;

        double ss = s * s;//s square
        double c3 = c1 * c1 * c1;
        c3 *= 2 * x0 * x0 + 4 * x0 * (1 - s) + 2 * ss - 3 * s + 1;
        c3 /= 6 * x0 * x0;

        double x03 = x0 * x0 * x0;//x0 cube
        double c4 = c1 * c1 * c1 * c1;
        c4 *= 6 * x03 + 18 * x0 * x0 * (1 - s) + x0 * (18 * ss - 29 * s + 11) - 6 * s * ss + 11 * ss - 6 * s + 1;
        c4 /= 24 * x03;

        double h = (c1 + (c2 + (c3 + c4 * fx0) * fx0) * fx0) * fx0;

        return x0 + h;
    }
}
