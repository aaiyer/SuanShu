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
package com.numericalmethod.suanshu.analysis.uniroot;

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Brent's root-finding algorithm combines super-linear convergence with reliability of bisection.
 * It uses the secant method or inverse quadratic interpolation whenever possible because they converge faster,
 * but falls back to the more robust bisection method if necessary.
 * Unlike {@link Newton} and {@link Halley}, it does not need the derivatives of the function.
 * Brent's algorithm is the preferred method of choice for root-finding.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Brent's_method"> Wikipedia: Brent's method</a>
 */
public class Brent implements Uniroot {

    private final double tol;
    private final int maxIterations;

    /**
     * Construct an instance of Brent's root finding algorithm.
     *
     * @param tol           the convergence tolerance
     * @param maxIterations the maximum number of iterations
     */
    public Brent(double tol, int maxIterations) {
        this.tol = tol;
        this.maxIterations = maxIterations;
    }

    @Override
    public double solve(UnivariateRealFunction f, double lower, double upper, double... guess) {
        return solve(f, lower, upper);
    }

    public double solve(UnivariateRealFunction f, double lower, double upper) {
        //initialization
        double a = lower;//old root approximation
        double b = upper;//last root approximation
        double c = a;//bracket point

        double fa = f.evaluate(a);
        double fb = f.evaluate(b);
        double fc = fa;

        //check bracketing
        if ((fa > 0 && fb > 0) || (fa < 0 && fb < 0)) {
            throw new RuntimeException("[a, b] must bracket the root");
        }

        double delta = b - a;
        double delta0 = delta;//old delta

        //brent's iteration
        for (int count = 0; count < maxIterations; ++count) {
            if ((fb > 0 && fc > 0) || (fb < 0 && fc < 0)) {//reset the bracket point
                c = a;
                fc = fa;
                delta = b - a;
                delta0 = delta;
            }

            if (abs(fc) < abs(fb)) {//use the bracket point if is better than the last approximation
                a = b;//old root
                b = c;//bracket as the last approximate root
                c = a;
                fa = fb;
                fb = fc;
                fc = fa;
            }

            double m = 0.5 * (c - b);
            if ((abs(fb) < tol) || abs(m) < EPSILON) {
                break;//root found
            }

            delta = m;//use bisection by default, unless...
            delta0 = delta;

            if ((abs(delta0) >= tol) &&//bound decreasing not too slowly
                    (abs(fa) > abs(fb))) {
                //attempt inverse quadratic interpolation
                double s = fb / fa;
                double p, q;

                if (a == c) {//as in the original Brent's method, the equality test is intentional
                    p = 2 * m * s;
                    q = 1 - s;
                } else {
                    double r = fb / fc;
                    q = fa / fc;
                    p = s * (2 * m * q * (q - r) - (b - a) * (r - 1));
                    q = (q - 1) * (r - 1) * (s - 1);
                }

                if (p > 0) {//in bounds?
                    q = -q;
                } else {
                    p = -p;
                }

                /**
                 * TODO:
                 * Do we implement the Brent's additional conditions in the wiki?
                 * They are not found in GSL, Numerical Recipes, or Apache...
                 */
                if (2 * p < min(3.0 * m * q - abs(tol * q), abs(delta0 * q))) {
                    //use inverse quadratic interpolation
                    delta0 = delta;
                    delta = p / q;
                }
            }

            //save old b, fb
            a = b;
            fa = fb;

            //compute new b, fb
            if (abs(delta) > tol) {
                b += delta;
            } else {
                b += (m > 0 ? tol : -tol);
            }

            fb = f.evaluate(b);
        }

        return b;
    }
}
