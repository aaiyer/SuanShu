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

import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularized;
import com.numericalmethod.suanshu.analysis.function.special.gamma.LogGamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Gamma;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.StandardCumulativeNormal;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.Erf;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.Gaussian;
import com.numericalmethod.suanshu.analysis.differentiation.Ridders;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * The first derivative is a measure of how a function changes as its input changes.
 * In other words, a derivative is how much a quantity changes if the input changes (by a very small amount).
 * The derivative of a function at a particular point is the best linear approximation of the function at that point.
 * For a univariate real function, the derivative at a point is the slope of the tangent line to the function at that point.
 * <p/>
 * This implementation is a simple wrapper class around the built-in derivative functions, e.g., {@link DBeta}, {@link DErf},
 * and the numerical derivative computation classes, e.g., {@link FiniteDifference} and {@link Ridders}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Derivative">Wikipedia: Derivative</a>
 * @see FiniteDifference
 */
public class Dfdx extends UnivariateRealFunction {

    /** the methods available to compute the numerical derivative */
    public static enum Method {

        /**
         * Finite difference: approximate a derivative using grid points.
         *
         * @see FiniteDifference
         */
        FINITE_DIFFERENCE,
        /**
         * Ridders' method: try a sequence of decreasing increments to the compute derivative, and then extrapolate to zero using Neville's algorithm.
         * It in general gives a better approximation.
         *
         * @see Ridders
         */
        RIDDERS
    };

    private final UnivariateRealFunction dfdx;//the derivative function

    /**
     * Construct the first order derivative function of a univariate function <i>f</i>.
     *
     * @param f      a univariate function
     * @param method the numerical method to use, c.f., {@link Method}
     */
    public Dfdx(final UnivariateRealFunction f, Method method) {
        if (f instanceof Polynomial) {
            dfdx = new DPolynomial((Polynomial) f);
        } else if (f instanceof BetaRegularized) {
            dfdx = new DBetaRegularized(((BetaRegularized) f).p(), ((BetaRegularized) f).q());
        } else if (f instanceof LogGamma) {
            dfdx = new Digamma();
        } else if (f instanceof Gamma) {
            dfdx = new DGamma();
        } else if (f instanceof StandardCumulativeNormal) {
            dfdx = new Gaussian();
        } else if (f instanceof Gaussian) {
            dfdx = new DGaussian((Gaussian) f);
        } else if (f instanceof Erf) {
            dfdx = new DErf();
        } else {
            /*
             * No "closed-form" formula is implemented or available.
             * We resort to numerical derivative.
             */
            switch (method) {
                case RIDDERS:
                    dfdx = new UnivariateRealFunction() {//essentially cast a RealScalarFunction to a UnivariateRealFunction

                        private final RealScalarFunction df = new Ridders(f, 1);

                        @Override
                        public double evaluate(double x) {
                            return df.evaluate(new DenseVector(x));
                        }
                    };
                    break;
                case FINITE_DIFFERENCE:
                default:
                    dfdx = new FiniteDifference(f, 1, FiniteDifference.Type.CENTRAL);
                    break;
            }
        }
    }

    /**
     * Construct, using the central finite difference, the first order derivative function of a univariate function <i>f</i>.
     *
     * @param f a univariate function
     */
    public Dfdx(UnivariateRealFunction f) {
        this(f, Method.FINITE_DIFFERENCE);
    }

    @Override
    public double evaluate(double x) {
        return dfdx.evaluate(x);
    }
}
