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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Hessian matrix is the square matrix of the second-order partial derivatives of a multivariate function.
 * Mathematically, the Hessian of a scalar function is an \(n \times n\) matrix,
 * where <i>n</i> is the domain dimension of <i>f</i>.
 * For a scalar function <i>f</i>, we have
 * \[
 * H(f) = \begin{bmatrix}
 * \frac{\partial^2 f}{\partial x_1^2} & \frac{\partial^2 f}{\partial x_1\,\partial x_2} & \cdots & \frac{\partial^2 f}{\partial x_1\,\partial x_n} \\ \\
 * \frac{\partial^2 f}{\partial x_2\,\partial x_1} & \frac{\partial^2 f}{\partial x_2^2} & \cdots & \frac{\partial^2 f}{\partial x_2\,\partial x_n} \\ \\
 * \vdots & \vdots & \ddots & \vdots \\ \\
 * \frac{\partial^2 f}{\partial x_n\,\partial x_1} & \frac{\partial^2 f}{\partial x_n\,\partial x_2} & \cdots & \frac{\partial^2 f}{\partial x_n^2}
 * \end{bmatrix}
 * \]
 * <p/>
 * This implementation computes the Hessian matrix numerically using the finite difference method.
 * We assume that the function <i>f</i> is continuous so the Hessian matrix is square and symmetric.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Hessian_matrix">Wikipedia: Hessian matrix</a>
 */
public class Hessian extends SymmetricMatrix {

    /**
     * Construct the Hessian matrix for a multivariate function <i>f</i> at point <i>x</i>.
     *
     * @param f a multivariate function
     * @param x the point to evaluate the Hessian of <i>f</i> at
     */
    public Hessian(RealScalarFunction f, Vector x) {
        super(f.dimensionOfDomain());//square and symmetric

        for (int i = 1; i <= nRows(); ++i) {
            for (int j = i; j <= nCols(); ++j) {
                FiniteDifference df = new FiniteDifference(f, new int[]{i, j});
                super.set(i, j, df.evaluate(x));
            }
        }
    }
}
