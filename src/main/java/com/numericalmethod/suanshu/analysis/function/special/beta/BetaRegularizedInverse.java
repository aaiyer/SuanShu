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
package com.numericalmethod.suanshu.analysis.function.special.beta;

import static com.numericalmethod.suanshu.Constant.ROOT_2;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.ErfInverse;
import com.numericalmethod.suanshu.analysis.uniroot.Halley;
import com.numericalmethod.suanshu.analysis.uniroot.NoRootFoundException;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;
import static java.lang.Math.*;

/**
 * The inverse of the Regularized Incomplete Beta function is defined at:
 * \[
 * x = I^{-1}_{(p,q)}(u), 0 \le u \le 1
 * \]
 * <p/>
 * The R equivalent function is {@code qbeta}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Amparo Gil, Javier Segura, and Nico M. Temme, "Section 10.5," Numerical Methods for Special Functions."
 * <li>"John Maddock, Paul A. Bristow, Hubert Holin, and Xiaogang Zhang. "Notes for The Incomplete Beta Function Inverses," Boost Library."
 * <li>"K. L. Majumder, and G. P. Bhattacharjee, Algorithm AS 63: The Incomplete Beta Integral, 1973."
 * <li>"Cran, G. W., K. J. Martin, and G. E. Thomas, "Remark AS R19 and Algorithm AS 109," Applied Statistics, 26, 111–114, 1977, and subsequent remarks (AS83 and correction)."
 * </ul>
 */
public class BetaRegularizedInverse extends UnivariateRealFunction {

    private final double p;//the shape parameter
    private final double q;//the shape parameter
    private final LogBeta logBeta = new LogBeta();
    private final ErfInverse erfInv = new ErfInverse();

    /**
     * Construct an instance of \(I^{-1}_{(p,q)}(u)\) with parameters <i>p</i> and <i>p</i>.
     *
     * @param p <i>p > 0</i>
     * @param q <i>q > 0</i>
     */
    public BetaRegularizedInverse(double p, double q) {
        this.p = p;
        this.q = q;
    }

    /**
     * Evaluate \(I^{-1}_{(p,q)}(u)\).
     *
     * @param u \(0 \le u \le 1\)
     * @return \(I^{-1}_{(p,q)}(u)\)
     */
    @Override
    public double evaluate(final double u) {
        if (equal(u, 0., 0)) {
            return 0;
        }

        if (equal(u, 1., 0)) {
            return 1;
        }

        double guess = getInitials(p, q, u);
        if (guess < 1e-300) {//TODO: what should this number be? it should be as small as possible, but not 0
            guess = 1e-300;
        } else if (guess > 0.999999999999999) {
            guess = 0.999999999999999;
        }

        // Starting with an initial guess, solve <i>x</i> where \(I_{(p,q)}(x) = u\).
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                BetaRegularized Ix = new BetaRegularized(p, q);
                return Ix.evaluate(x) - u;
            }
        };

        UnivariateRealFunction df = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                double y = (p - 1) * log(x) + (q - 1) * log(1 - x) - logBeta.evaluate(p, q);
                return exp(y);
            }
        };

        UnivariateRealFunction d2f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                double y = (p - 2) * log(x) + (q - 2) * log(1 - x) - logBeta.evaluate(p, q);
                y = exp(y);
                y *= (p - 1) * (1 - x) + (q - 1) * x;
                return y;
            }
        };

        Halley halley = new Halley(1e-16, 50);

        double result;
        try {
            result = halley.solve(f, df, d2f, guess);
        } catch (NoRootFoundException ex) {
            result = ex.x();
        }
        return result;
    }

    /**
     * Guess where the solution lies,
     * using which we will refine the precision using a root finding method.
     *
     * @param p the shape parameter
     * @param q the shape parameter
     * @param u quantile
     * @return an approximation of where \(x = I^{-1}_{(p,q)}(u)\) is
     */
    private double getInitials(double p, double q, double u) {
        if (u > 0.5) {
            return 1 - getInitials(q, p, 1 - u);
        }

        //TODO: do we use Temme's method as in boost? Is it really better?
//        if (p + q >= 5) {
//            if (sqrt(p) > q - p) {
//                return nearlySymmetricCase(p, q, u);
//            }
//        }

        //default
        return algorithmAS109(p, q, u);
    }

    /*
     * TODO: Is this really better than algorithmAS109? Not sure...
     *
     * <pre>
     * Section 10.5
     * Numerical Methods for Special Functions
     * by
     * Amparo Gil, Javier Segura, and Nico M. Temme.
     * </pre>
     */
    private double nearlySymmetricCase(double a, double b, double u) {
        final double beta = b - a;
        double eta0 = -erfInv.evaluate(1 - 2 * u) / sqrt(a / 2);

        //eqs. 10.77, 10.81
        final double beta2 = beta * beta;
        Polynomial p1 = new Polynomial(new double[]{-beta * ROOT_2 / 3840d, -1 / 192d, -beta * ROOT_2 / 48d, (1 - 2 * beta) / 8d, -beta * ROOT_2 / 2d});
        Polynomial p2 = new Polynomial(new double[]{beta * ROOT_2 * (20 * beta - 1) / 960d, (20 * beta2 - 12 * beta + 1) / 128d, beta * ROOT_2 * (3 * beta - 2) / 12d});
        Polynomial p3 = new Polynomial(new double[]{beta * ROOT_2 * (-75 * beta2 + 80 * beta - 16) / 480d});

        double e1 = p1.evaluate(eta0);
        double e2 = p2.evaluate(eta0);
        double e3 = p3.evaluate(eta0);

        double en = (e1 + (e2 + e3 / a) / a) / a;//eq. 10.71
        double eta = eta0 + en;//eq. 10.70

        double eta2 = eta * eta;
        double x = 0.5 * (1 + eta * sqrt((1 - exp(-0.5 * eta2)) / eta2));//eq. 10.56
        return x;
    }

    /**
     * Algorithm AS 109.
     *
     * @param p the shape parameter
     * @param q the shape parameter
     * @param u quantile
     * @return an approximation of where \(x = I^{-1}_{(p,q)}(u)\) is
     */
    private double algorithmAS109(double p, double q, double u) {//R uses a similiar algorithm
        double x = 0;

        double z = sqrt(-log(u * u));
        double y = z - (2.30753 + 0.27061 * z) / (1 + (0.99229 + 0.04481 * z) * z);

        if (p > 1 && q > 1) {
            double r = (y * y - 3) / 6;
            double s = 1 / (2 * p - 1);
            double t = 1 / (2 * q - 1);
            double v = 2 / (s + t);
            double w = y * sqrt(v + r) / v - (t - s) * (r + 5 / 6 - 2 / (3 * v));
            x = p / (p + q * exp(w + w));
        } else {
            double r = 2 * q;
            double t = 1 / (9 * q);
            t = r * pow(1 - t + y * sqrt(t), 3);

            if (t <= 0) {
                x = 1 - exp((log((1 - u) * q) + logBeta.evaluate(p, q)) / q);
            } else {
                double v = (4 * p + r - 2) / t;

                if (v <= 1) {
                    x = exp((log(u * p) + logBeta.evaluate(p, q)) / p);
                } else {
                    x = 1 - 2 / (v + 1);
                }
            }
        }

        return x;
    }
}
