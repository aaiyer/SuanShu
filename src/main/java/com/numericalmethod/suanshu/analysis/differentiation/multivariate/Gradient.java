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

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * The gradient of a scalar field is a vector field which points in the direction of the greatest rate of increase of the scalar field,
 * and of which the magnitude is the greatest rate of change.
 * Mathematically, the gradient of a scalar function is a vector of size <i>n</i>,
 * where <i>n</i> is the domain dimension of <i>f</i>.
 * \[
 * \nabla{f} = {\partial f \over \partial x_1}, \dots, {\partial f \over \partial x_n}
 * \]
 * The gradient vector is computed numerically using the finite difference method, e.g., {@link FiniteDifference}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Gradient">Wikipedia: Gradient</a>
 */
public class Gradient extends DenseVector {

    /**
     * Construct the gradient vector for a multivariate function <i>f</i> at point <i>x</i>.
     *
     * @param f a multivariate function
     * @param x the point to evaluate the gradient of <i>f</i> at
     */
    public Gradient(RealScalarFunction f, Vector x) {
        super(f.dimensionOfDomain());

        for (int i = 1; i <= size(); ++i) {
            FiniteDifference df = new FiniteDifference(f, new int[]{i});
            double dfx = df.evaluate(x);
            super.set(i, dfx);
        }
    }
}
