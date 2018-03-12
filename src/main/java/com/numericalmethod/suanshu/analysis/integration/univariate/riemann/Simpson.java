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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import static com.numericalmethod.suanshu.number.DoubleUtils.relativeError;

/**
 * Simpson's rule can be thought of as a special case of Romberg's method.
 * It is the weighted average (or extrapolation) of two successive iterations of the {@link Trapezoidal} rule.
 * Simpson's rule is often an accurate integration rule.
 * Simpson's is expected to improve on the trapezoidal rule for functions which are twice continuously differentiable.
 * However for rougher functions the trapezoidal rule is likely to be more preferable.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Simpson%27s_rule">Wikipedia: Simpson's rule</a>
 * <li><a href="http://en.wikipedia.org/wiki/Rectangle_rule">Wikipedia: Rectangle method</a>
 * </ul>
 */
public class Simpson implements IterativeIntegrator {

    private double t0, t1;//two successive steps of the trapezoidal rule
    private final Trapezoidal trapezoidal;
    private final int maxIterations;
    private final double precision;

    /**
     * Construct an integrator that implements Simpson's rule.
     *
     * @param precision     the convergence threshold
     * @param maxIterations the maximum number of iterations
     */
    public Simpson(double precision, int maxIterations) {
        this.trapezoidal = new Trapezoidal(precision, maxIterations);
        this.maxIterations = maxIterations;
        this.precision = precision;
    }

    @Override
    public double integrate(UnivariateRealFunction f, double a, double b) {
        double sum0 = Double.NaN, sum1 = Double.NaN;
        for (int iter = 1; iter <= maxIterations; ++iter) {
            sum0 = sum1;
            sum1 = next(iter, f, a, b, sum0);//TODO: separate the sum for M and T?

            if ((iter > 3) && relativeError(sum1, sum0) < precision) {//avoid spurious convergence
                break;//converged
            }
        }

        return sum1;
    }

    @Override
    public double next(int iteration, UnivariateRealFunction f, double a, double b, double sum) {
        if (iteration == 1) {
            t0 = trapezoidal.next(1, f, a, b, sum);
            t1 = trapezoidal.next(2, f, a, b, t0);
        } else {
            t0 = t1;
            t1 = trapezoidal.next(iteration + 1, f, a, b, t0);
        }

        double result = (4 * t1 - t0) / 3;//TODO: reference?
        return result;
    }

    @Override
    public double h() {
        return trapezoidal.h();
    }

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }

    @Override
    public double getPrecision() {
        return precision;
    }
}
