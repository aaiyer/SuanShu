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
package com.numericalmethod.suanshu.analysis.differentiation.univariate;

import static com.numericalmethod.suanshu.Constant.MACH_EPS;
import static com.numericalmethod.suanshu.analysis.function.FunctionOps.combination;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static java.lang.Math.*;

/**
 * A finite difference (divided by a small increment) is an approximation of the derivative of a function.
 * The accuracy depends on the function to take the derivative of.
 * In general, the accuracy of central difference is the best while those of forward and backward differences are more or less the same.
 * For finite difference, the higher an order of a derivative, the less accurate it gets.
 * For example, approximating the 5-th derivative is much less accurate than approximating the 1st derivative.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Finite_difference">Wikipedia: Finite difference</a>
 * <li><a href="http://en.wikipedia.org/wiki/Difference_operator">Wikipedia: Difference operator</a>
 * <li><a href="http://en.wikipedia.org/wiki/Numerical_differentiation#Practical_considerations">Wikipedia: Practical considerations</a>
 * </ul>
 */
public class FiniteDifference extends UnivariateRealFunction {

    /** the types of finite difference available */
    public static enum Type {

        /** forward difference */
        FORWARD,
        /** backward difference */
        BACKWARD,
        /** central difference */
        CENTRAL
    };

    private final UnivariateRealFunction f;//the univariate function to take the derivative of
    private final int order;//the order of the derivative
    private final Type type;//the type of finite difference chosen
    private volatile double temp;

    /**
     * Construct an approximate derivative function for <i>f</i> using finite difference.
     *
     * @param f     a univariate function
     * @param order the order of the derivative
     * @param type  the type of finite difference to use, c.f., {@link Type}
     */
    public FiniteDifference(UnivariateRealFunction f, int order, Type type) {
        SuanShuUtils.assertArgument(order > 0, "the order of derivative must be >= 1");

        this.order = order;
        this.type = type;
        this.f = f;
    }

    @Override
    public double evaluate(double x) {
        /*
         * Make sure that <i>h</i> and <i>x+h</i> are representable in floating point precision
         * so that the difference between <i>x+h</i> and <i>x</i> is exactly <i>h</i>, the step size.
         * <p/>
         * TODO: take care when |x| be 0.
         * <i>h</i> cannot be too big to avoid inaccuracy;
         * <i>h</i> cannot be too small to avoid rounding error (compute <i>f</i> for the "same" values)
         */
        double h = pow(MACH_EPS, 1d / (order + 1)) * (max(1e-1, abs(x)));
        return evaluate(x, h);
    }

    /**
     * Evaluate numerically the derivative of <i>f</i> at point <i>x</i>, <i>f'(x)</i>, with step size <i>h</i>.
     * It could be challenging to automatically determine the step size <i>h</i>, esp. when <i>|x|</i> is near 0.
     * It may, for example, require an analysis that involves <i>f'</i> and <i>f''</i>.
     * The user may want to experiment with different <i>h</i>s by calling this function.
     *
     * @param x the point to evaluate the derivative of <i>f</i> at
     * @param h step size
     * @return <i>f'(x)</i>, the numerical derivative of <i>f</i> at point <i>x</i> with step size <i>h</i>
     */
    public double evaluate(double x, double h) {
        temp = x + h;
        h = temp - x;

        double df = df(x, h);

        //correct the problem of changing the interval of discretization
        if (order % 2 == 1 && type == Type.CENTRAL) {
            df = 0.5 * (df(x - h / 2, h) + df(x + h / 2, h));
        }

        double dfdx = df / pow(h, order);
        return dfdx;
    }

    /**
     * Compute the finite difference for <i>f</i> at <i>x</i> with an increment <i>h</i> for the <i>n</i>-th order using either forward, backward, or central difference.
     *
     * @param x the point to evaluate the function at
     * @param h the increment
     * @return \(\Delta^n_hf(x)\)
     * @see <a href="http://en.wikipedia.org/wiki/Difference_operator#Higher-order_differences">Wikipedia: Higher-order differences</a>
     */
    public double df(double x, double h) {
        double df = 0;

        int sign = 1;
        for (int i = 0; i <= order; ++i) {
            double x_i = x;
            switch (type) {
                case FORWARD:
                    x_i += (order - i) * h;
                    break;
                case BACKWARD:
                    x_i += -i * h;
                    break;
                case CENTRAL:
                    x_i += (order / 2d - i) * h;
                    break;
            }

            df += sign * combination(order, i) * f.evaluate(x_i);
            sign *= -1;
        }

        return df;
    }
}
