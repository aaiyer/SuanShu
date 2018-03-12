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
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.relativeError;

/**
 * The Newton–Cotes formulae, also called the Newton–Cotes quadrature rules or simply Newton–Cotes rules,
 * are a group of formulae for numerical integration (also called quadrature) based on evaluating the integrand at equally-spaced points.
 * A number of standard numerical quadrature methods are special cases of the more general Newton–Cotes formula,
 * e.g., the Trapezoidal rule ({@code rate = 2}), the Midpoint method ({@code rate = 2} and specifying {@code OPEN} formula).
 * <p/>
 * This implementation is based on the Euler-Maclaurin formula.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Newton%E2%80%93Cotes_formulas">Wikipedia: Newton–Cotes formulas</a>
 * <li><a href="http://en.wikipedia.org/wiki/Euler_Maclaurin">Wikipedia: Euler–Maclaurin formula</a>
 * <li><a href="http://en.wikipedia.org/wiki/Trapezoidal_rule">Wikipedia: Trapezoidal rule</a>
 * <li><a href="http://en.wikipedia.org/wiki/Rectangle_rule">Wikipedia: Rectangle method</a>
 * </ul>
 */
public class NewtonCotes implements IterativeIntegrator {

    /** There are two types of the Newton-Cotes method: OPEN and CLOSED. */
    public static enum Type {

        /**
         * The first and the last terms in the Euler-Maclaurin formula are included in the sum.
         * They are:
         * <i>1/2 * f(x<sub>0</sub>)</i>, and
         * <i>1/2 * f(x<sub>n</sub>)</i>.
         */
        CLOSED,
        /**
         * The first and the last terms in the Euler-Maclaurin formula are not included in the sum.
         * This is to avoid computing for end points where <i>f(x)</i> cannot be evaluated.
         * Instead, we use the mid-point to make the first rough estimate of the integral.
         */
        OPEN
    };

    /**
     * This is the rate of sub-dividing an interval.
     * Starting with the whole interval <i>(b-a)</i>, we divide each sub-interval into {@code rate} many intervals.
     * For example, when {@code rate = 2}, we double the number of intervals, hence abscissas, in each iteration.
     * Note: the number of abscissas grows exponentially.
     */
    private final int rate;
    /**
     * This indicates whether the two end points are included for computation.
     */
    private final Type type;
    /**
     * the convergence threshold.
     * The iterative procedure converges when the <em>relative</em> difference between two successive sums is less than {@code precision}.
     */
    private final double precision;
    /**
     * This is the maximum number of iterations for this iterative procedure.
     * <p/>
     * For those integrals that do not converge, we need to put a bound on the number of iterations.
     * Note that the number of abscissas grows exponentially with each iteration, i.e., <i>rate<sup>iterations</sup></i>.
     * Thus, be careful when choosing {@code maxIterations}, as it may severely affect the performance. It should not be too big.
     */
    private final int maxIterations;
    /**
     * the discretization size for each iteration
     */
    private double h;
    private int nAbscissas = 1;

    /**
     * Construct an instance of the Newton–Cotes quadrature.
     *
     * @param rate          the rate of further sub-dividing an integral interval. For example, when {@code rate = 2}, we divide <i>[x<sub>i</sub>, x<sub>i+1</sub>]</i> into two equal length intervals. This is equivalent to the Trapezoidal rule.
     * @param type          specifying whether to use CLOSED or OPEN formula
     * @param precision     the precision required, e.g., {@code 1e-8}
     * @param maxIterations the maximum number of iterations
     */
    public NewtonCotes(int rate, Type type, double precision, int maxIterations) {
        this.rate = rate;
        this.type = type;
        this.precision = precision;
        this.maxIterations = maxIterations;
    }

    @Override
    public double integrate(UnivariateRealFunction f, double a, double b) {
        double sum0, sum1 = Double.NaN;
        for (int iter = 1; iter <= maxIterations; ++iter) {
            sum0 = sum1;
            sum1 = next(iter, f, a, b, sum0);

            if ((iter > 3) && relativeError(sum1, sum0) < precision) {//avoid spurious convergence
                break;//converged
            }
        }

        return sum1;
    }

    @Override
    public double next(int iter, UnivariateRealFunction f, double a, double b, double sum0) {
        SuanShuUtils.assertArgument(iter > 0, "iteration count must be +ve");

        double sum = sum0;

        if (iter == 1) {
            h = (b - a);
            nAbscissas = 1;

            switch (type) {
                case CLOSED://use the two end points
                    sum = 0.5 * h * (f.evaluate(a) + f.evaluate(b));
                    break;
                case OPEN://use the mid point
                    sum = h * (f.evaluate((a + b) / 2));
                    break;
            }
            return sum;
        } else {//summing up the interior abscissas
            h /= rate;
            nAbscissas *= rate;

            double addition = 0;
            double x = a;
            for (int i = 1; i < nAbscissas; ++i) {//i <= nAbscissas - 1;
                x += h;
                if ((i % rate) == 0) {
                    continue;//this abscissa is already computed in a previous iteration
                }

                addition += f.evaluate(x);
            }

            sum = sum0 / rate + h * addition;
        }

        return sum;
    }

    @Override
    public double h() {
        return h;
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
