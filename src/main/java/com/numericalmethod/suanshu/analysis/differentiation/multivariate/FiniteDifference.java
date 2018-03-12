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
package com.numericalmethod.suanshu.analysis.differentiation.multivariate;

import static com.numericalmethod.suanshu.Constant.MACH_EPS;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import java.util.Arrays;

/**
 * A partial derivative of a multivariate function is the derivative with respect to one of the variables with the others held constant.
 * This implementation applies recursively the (first order) finite difference on the function.
 * For example,
 * \[
 * {\partial^2 \over \partial x_1 \partial x_2} = {\partial \over \partial x_2}({\partial \over \partial x_1})
 * \]
 *
 * Each of the two univariate derivatives is computed using the central difference method.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Partial_derivative">Wikipedia: Partial derivative</a>
 * <li><a href="http://en.wikipedia.org/wiki/Finite_difference">Wikipedia: Finite difference</a>
 * </ul>
 */
public class FiniteDifference implements RealScalarFunction {

    private final RealScalarFunction f;
    private final int[] varidx;

    /**
     * Construct the partial derivative of a multi-variable function.
     * <p/>
     * For example,
     * <code>varidx = new int[]{1, 2}</code> means
     * \[
     * f_{x_1,x_2} = {\partial^2 \over \partial x_1 \partial x_2} = {\partial \over \partial x_2}({\partial \over \partial x_1})
     * \]
     *
     * @param f      the real multivariate function to take derivative of
     * @param varidx the variable indices of the derivative, counting from 1 up to the domain dimension of <i>f</i>
     */
    public FiniteDifference(RealScalarFunction f, int[] varidx) {
        this.f = f;

        /*
         * Check the validty of {@code varidx}.
         * Each index must be between 1 and the domain dimension.
         */
        for (int i = 0; i < varidx.length; ++i) {
            if (varidx[i] < 1 || varidx[i] > f.dimensionOfDomain()) {
                throw new IllegalArgumentException("invalid variable specification; order specifiies variable indices");
            }
        }

        this.varidx = Arrays.copyOf(varidx, varidx.length);
    }

    /**
     * Evaluate numerically the partial derivative of <i>f</i> at point <i>x</i>.
     * <p/>
     * Make sure that <i>h</i> and <i>x+h</i> are representable in floating point precision
     * so that the difference between <i>x+h</i> and <i>x</i> is exactly <i>h</i>.
     *
     * @param x the point to evaluate the derivative at
     * @return the numerical partial derivative of <i>f</i> at point <i>x</i>
     * @see <a href="http://en.wikipedia.org/wiki/Numerical_differentiation#Practical_considerations">Wikipedia: Practical considerations</a>
     */
    @Override
    public Double evaluate(Vector x) {
        /*
         * TODO: take care when |x| be 0.
         * <i>h</i> cannot be too big to avoid inaccuracy;
         * <i>h</i> cannot be too small to avoid rounding error (compute <i>f</i> for the "same" values)
         */
        double h = pow(MACH_EPS, 1d / (varidx.length + 1)) * max(1e-1, x.norm());
        return evaluate(x, h);
    }

    /**
     * Evaluate numerically the partial derivative of <i>f</i> at point <i>x</i> with step size <i>h</i>.
     * It could be challenging to automatically determine the step size <i>h</i>, esp. when <i>|x|</i> is near 0.
     * It may, for example, require an analysis that involves <i>f'</i> and <i>f''</i>.
     * The user may want to experiment with different <i>h</i>s by calling this function.
     *
     * @param x the point to evaluate the derivative of <i>f</i> at
     * @param h the step size
     * @return the numerical partial derivative of <i>f</i> at point <i>x</i> with step size <i>h</i>
     */
    public double evaluate(Vector x, double h) {
        double dfdx = evaluateByRecursion(varidx.length, h, x);
        return dfdx;
    }

    private double evaluateByRecursion(int n, double h, Vector z) {
        Vector x = new DenseVector(z);

        //the base case; order == 1
        if (n == 1) {
            int i = varidx[0];
            x.set(i, z.get(i) + h);
            double fx_p = f.evaluate(x);//f(x+h)
            x.set(i, x.get(i) - 2 * h);//TODO: this seems more accurate than x.set(i, z.get(i) - h); why?
            double fx_n = f.evaluate(x);//f(x-h)
            double dfdx = (fx_p - fx_n) / 2 / h;//central difference
            return dfdx;
        }

        //order >= 2
        int i = varidx[n - 1];
        x.set(i, z.get(i) + h);
        double fx_p = evaluateByRecursion(n - 1, h, x);//d(n-1){f(x+h)}
        x.set(i, x.get(i) - 2 * h);//TODO: this seems more accurate than x.set(i, z.get(i) - h); why?
        double fx_n = evaluateByRecursion(n - 1, h, x);//d(n-1){f(x-h)}
        double dfdx = (fx_p - fx_n) / 2 / h;//central difference

        return dfdx;
    }

    @Override
    public int dimensionOfDomain() {
        return f.dimensionOfDomain();
    }

    @Override
    public int dimensionOfRange() {
        return 1;
    }
}
