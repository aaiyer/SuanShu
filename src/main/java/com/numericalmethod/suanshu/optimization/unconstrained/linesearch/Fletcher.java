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
package com.numericalmethod.suanshu.optimization.unconstrained.linesearch;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * This is Fletcher's inexact line search method.
 * An inexact line search is usually used in conjunction with a multi-dimensional optimization algorithm.
 * It trades off the accuracy of the line search result with the amount of computation.
 * A lot of optimization algorithms are shown to be quite tolerant to line search imprecision.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 2.6, Algorithms 4.6, 7.3," Practical Optimization: Algorithms and Engineering Applications."
 */
public class Fletcher implements LineSearch {

    private final double rho;
    private final double sigma;
    private final double tau;
    private final double chi;
    private final double epsilon;
    private final int maxIterations;

    /**
     * Construct a line search minimizer using the Fletcher method.
     *
     * @param rho           a precision parameter; smaller ρ, e.g., 0.1, gives better accuracy
     * @param sigma         a precision parameter; smaller σ, e.g., 0.1, gives better accuracy
     * @param tau           a control parameter to prevent the result from being too close to boundary
     * @param chi           a control parameter to prevent the result from being too close to boundary
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0. This affects the precision of the result.
     * @param maxIterations the maximum number of iterations. This affects the precision of the result.
     */
    public Fletcher(double rho, double sigma, double tau, double chi, double epsilon, int maxIterations) {
        this.rho = rho;
        this.sigma = sigma;
        this.tau = tau;
        this.chi = chi;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    /**
     * Construct a line search minimizer using the Fletcher method with the default control parameters.
     */
    public Fletcher() {
        this(0.1, 0.1, 0.1, 0.75, 1e-10, 400);
    }

    @Override
    public Solution solve(final C2OptimProblem problem) {
        return new Solution() {

            @Override
            public double linesearch(final Vector x, final Vector d) {
                //f as a function of {@code a}
                UnivariateRealFunction fa = new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double a) {
                        Vector newx = x.add(d.scaled(a));//x + a * d
                        double fnewx = problem.f().evaluate(newx);//f(x + a * d)
                        return fnewx;
                    }
                };

                //f' as a function of {@code a}
                UnivariateRealFunction dfa = new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double a) {
                        Vector newx = x.add(d.scaled(a));//x + a * d
                        Vector gnewx = problem.g().evaluate(newx);//gradient(x + a * d)
                        double gd = gnewx.innerProduct(d);//gradient(x + a * d)' %*% d
                        return gd;
                    }
                };

                //[aL, aU] is the bracketing interval
                double aL = 0;
                double aU = 1e99;//Double.MAX_VALUE;

                //initializaton
                double fL = fa.evaluate(aL);
                double dfL = dfa.evaluate(aL);

                //estimate alpha0
                double a0 = 1;
                if (abs(dfL) > 0) {
                    a0 = -2 * fL / dfL;
                }

                if (a0 <= epsilon || a0 > 1) {
                    a0 = 1;
                }

                for (int i = 1; i <= maxIterations; ++i) {
                    //does the Goldstein conditions hold?
                    double f0 = fa.evaluate(a0);
                    if ((f0 > fL + rho * (a0 - aL) * dfL) && (abs(fL - f0) > epsilon)) {
                        if (a0 < aU) {
                            aU = a0;
                        }

                        double a0hat = aL + pow(a0 - aL, 2) * dfL / 2 / (fL - f0 + (a0 - aL) * dfL);//eq. 4.57

                        double tmp = aL + tau * (aU - aL);
                        if (a0hat < tmp) {//should not be too close to aL
                            a0hat = tmp;
                        }

                        tmp = aU - tau * (aU - aL);
                        if (a0hat > tmp) {//should not be too close to aU
                            a0hat = tmp;
                        }

                        a0 = a0hat;
                        continue;
                    }

                    //the slope at the acceptable point must be greater than a fraction, sigma, of the initial slope
                    double df0 = dfa.evaluate(a0);
                    if ((df0 < sigma * dfL) && (abs(fL - f0) > epsilon) && (!equal(df0, dfL, epsilon))) {
                        double da0 = (a0 - aL) * df0 / (dfL - df0);

                        if (da0 <= 0) {
                            da0 = a0;
                        }

                        double tmp = tau * (a0 - aL);
                        if (da0 < tmp) {//should not be too close to aL
                            da0 = tmp;
                        }

                        tmp = chi * (aU - a0);
                        if (da0 > tmp) {//should not be too close to aU
                            da0 = tmp;
                        }

                        double a0hat = a0 + da0;
                        aL = a0;
                        a0 = a0hat;
                        fL = f0;
                        dfL = df0;

                        continue;
                    }

                    break;//both conditions are NOT satisified
                }

                //TODO: make this a parameter?
                if (a0 < 1e-5) {//TODO: remove? why 1e-5? to ensure some minimum increment?
                    a0 = 1e-5;//1e-5 seems OK; 1e-6 or smaller doesn't work
                }

                return a0;
            }
        };
    }
}
