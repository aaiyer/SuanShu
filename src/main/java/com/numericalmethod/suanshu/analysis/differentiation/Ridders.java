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
package com.numericalmethod.suanshu.analysis.differentiation;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.differentiation.univariate.FiniteDifference.Type;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.tuple.BinaryRelation;
import com.numericalmethod.suanshu.analysis.interpolation.NevilleTable;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;

/**
 * Ridders' method computes the numerical derivative of a function.
 * In general it gives a higher precision than the simple finite differencing method,
 * c.f., {@link com.numericalmethod.suanshu.analysis.differentiation.univariate.FiniteDifference}.
 * Ridders' method tries a sequence of decreasing <i>h</i>'s to compute the derivatives,
 * and then extrapolate to zero using Neville's algorithm.
 * The choice of the initial <i>h</i> is critical.
 * If <i>h</i> is too big, the value computed could be inaccurate.
 * If <i>h</i> is too small, due to rounding error, we might be computing the "same" value over and over again for different <i>h</i>'s.
 *
 * @author Haksun Li
 * @see "C. J. F. Ridders, "Accurate computation of F'(x) and F''(x)," in Adv. Eng. Sftw., 4, 75-76. 1982."
 */
public class Ridders implements RealScalarFunction {

    private static interface MyFunction {

        double evaluate(Vector x, double h);
    }

    /** the function to take the limit of when {@code h → 0} */
    private MyFunction dfh;
    /** the interpolation method */
    private NevilleTable neville;
    /** the function to take the derivative of */
    private final RealScalarFunction f;
    /** the order of the derivative */
    private final int order;
    /** the rate at which <i>h</i> decreases */
    private final double rate;
    /** the number of points for extrapolation */
    private final int discretization;
    /** the default number of points for extrapolation */
    private static final int DISCRETIZATION = 15;

    /**
     * Construct the derivative function of a univariate function using Ridder's method.
     *
     * @param f              the {@link UnivariateRealFunction} to take derivative of
     * @param order          the order of differentiation
     * @param rate           the rate at which the increment <i>h</i> decreases; {@code rate} should be a simple number such as 0.75, not like 0.66666666666...
     * @param discretization the number of points for extrapolation
     */
    public Ridders(final UnivariateRealFunction f, final int order, double rate, int discretization) {
        SuanShuUtils.assertArgument(order > 0, "the order of derivative must be >= 1");

        this.f = f;
        this.order = order;
        this.rate = rate;
        this.discretization = discretization;
        this.dfh = new MyFunction() {

            private final com.numericalmethod.suanshu.analysis.differentiation.univariate.FiniteDifference df =
                    new com.numericalmethod.suanshu.analysis.differentiation.univariate.FiniteDifference(f, order, Type.CENTRAL);

            @Override
            public double evaluate(Vector x, double h) {
                return df.evaluate(x.get(1), h);
            }
        };
    }

    /**
     * Construct the derivative function of a univariate function using Ridder's method.
     *
     * @param f     the {@link UnivariateRealFunction} to take derivative of
     * @param order the order of the derivative
     */
    public Ridders(UnivariateRealFunction f, int order) {
        this(f, order, 0.75, DISCRETIZATION);
    }

    /**
     * Construct the derivative function of a vector-valued function using Ridder's method.
     * <p/>
     * By convention,
     * <code>varidx = new int[]{1, 2}</code> means
     * \[
     * f_{x_1,x_2} = {\partial^2 f \over \partial x_1 \partial x_2} = {\partial \over \partial x_2}{\partial \over \partial x_1}
     * \]
     * <p/>
     * The indices count from 1 up to the number of variables of <i>f</i>,
     * i.e., the domain dimension of <i>f</i>.
     *
     * @param f              the multivariate function to take derivative of
     * @param varidx         specify the variable indices, numbering from 1 up to the domain dimension of <i>f</i>
     * @param rate           {@code rate} should be a simple number, not like 0.66666666666...
     * @param discretization the number of points used for extrapolation
     */
    public Ridders(final RealScalarFunction f, final int[] varidx, double rate, int discretization) {
        this.f = f;
        this.order = varidx.length;
        this.rate = rate;
        this.discretization = discretization;
        this.dfh = new MyFunction() {

            private final com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifference df =
                    new com.numericalmethod.suanshu.analysis.differentiation.multivariate.FiniteDifference(f, varidx);

            @Override
            public double evaluate(Vector x, double h) {
                return df.evaluate(x, h);
            }
        };
    }

    /**
     * Construct the derivative function of a vector-valued function using Ridder's method.
     * <p/>
     * By convention,
     * <code>varidx = new int[]{1, 2}</code> means
     * \[
     * f_{x_1,x_2} = {\partial^2 f \over \partial x_1 \partial x_2} = {\partial \over \partial x_2}{\partial \over \partial x_1}
     * \]
     * <p/>
     * The indices count from 1 up to the number of variables of <i>f</i>,
     * i.e., the domain dimension of <i>f</i>.
     *
     * @param f      the real multivariate function to take derivative of
     * @param varidx specify the variable indices, numbering from 1 up to the domain dimension of <i>f</i>
     */
    public Ridders(RealScalarFunction f, int[] varidx) {
        this(f, varidx, 0.75, DISCRETIZATION);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Make sure that <i>h</i> and <i>x+h</i> are representable in floating point precision
     * so that the difference between <i>x+h</i> and <i>x</i> is exactly <i>h</i>, the step size.
     *
     * @param x the point to evaluate the derivative of <i>f</i> at
     * @return <i>f'(x)</i>, the numerical derivative of <i>f</i> at point <i>x</i> using Ridders' method
     * @see <a href="http://en.wikipedia.org/wiki/Numerical_differentiation#Practical_considerations">Wikipedia: Practical considerations</a>
     */
    @Override
    public Double evaluate(Vector x) {
        double norm = x.norm();
        /*
         * TODO: take care when |x| be 0.
         * <i>h</i> cannot be too big to avoid inaccuracy;
         * <i>h</i> cannot be too small to avoid rounding error (compute <i>f</i> for the "same" values)
         */
        double h = DISCRETIZATION * pow(Constant.EPSILON, 1d / (order + 1)) * max(1e-1, norm);//multiple times the "smallest" h
        return evaluate(x, h);
    }

    /**
     * Evaluate <i>f'(x)</i>, where <i>f</i> is a {@link UnivariateRealFunction}.
     *
     * @param x the point to evaluate the derivative of <i>f</i> at
     * @return <i>f'(x)</i>, the numerical derivative of <i>f</i> at point <i>x</i> using Ridders' method
     * @see <a href="http://en.wikipedia.org/wiki/Numerical_differentiation#Practical_considerations">Wikipedia: Practical considerations</a>
     */
    public double evaluate(double x) {
        if (f.dimensionOfDomain() != 1) {
            throw new IllegalArgumentException("f is not a univariate function");
        }
        return evaluate(new DenseVector(x));
    }

    /**
     * Evaluate numerically the derivative of <i>f</i> at point <i>x</i>, <i>f'(x)</i>, with step size <i>h</i>.
     * It could be challenging to automatically determine the step size <i>h</i>, esp. when <i>|x|</i> is near 0.
     * It may, for example, require an analysis that involves <i>f'</i> and <i>f''</i>.
     * The user may want to experiment with different <i>h</i>s by calling this function.
     *
     * @param x the point to evaluate <i>f</i> at
     * @param h the step size
     * @return <i>f'(x)</i>, the numerical derivative of <i>f</i> at point <i>x</i> with step size <i>h</i>
     */
    public double evaluate(Vector x, double h) {
        double h_1 = h;
        double df_1 = df_i(x, h_1);
        neville = new NevilleTable();
        neville.addData(new BinaryRelation(new double[]{h_1}, new double[]{df_1}));//TODO: h1 or pow(h1, order); pow could be meaninglessly small for higher order for h is small

        double result = df_1;
        double minErr = Double.MAX_VALUE;
        double h_i = h_1;
        for (int i = 2; i <= discretization; ++i) {
            h_i *= rate;
            double df_i = df_i(x, h_i);
            neville.addData(new BinaryRelation(new double[]{h_i}, new double[]{df_i}));//TODO: hi or pow(hi, order); pow could be meaninglessly small for higher order for h is small
            neville.evaluate(0);//compute the Neville table

            for (int j = i - 2; j >= 0; --j) {
                double dfdx_jj = neville.get(j, i - 1);

                double err1 = abs(dfdx_jj - neville.get(j, i - 2));//one to the left
                double err2 = abs(dfdx_jj - neville.get(j + 1, i - 1));//one below
                double err = max(err1, err2);
                if (err < minErr) {
                    result = dfdx_jj;
                    minErr = err;
                }
            }

            if (abs(df_i - neville.get(i - 2, i - 2)) > 2d * minErr) {//the new derivative is way off
                break;
            }
        }

//        double df = neville.evaluate(0);//this does not always give better result
        return result;
    }

    private double df_i(Vector x, double h) {
        double df_i = dfh.evaluate(x, h);
        return df_i;
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
