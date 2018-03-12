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
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A bordered Hessian matrix consists of the Hessian of a multivariate function <i>f</i>,
 * and the gradient of a multivariate function <i>g</i>.
 * We assume that the function <i>f</i> is continuous so that the bordered Hessian matrix is square and symmetric.
 * For scalar functions <i>f</i> and <i>g</i>, we have
 * \[
 * H(f,g) = \begin{bmatrix}
 * 0 & \frac{\partial g}{\partial x_1} & \frac{\partial g}{\partial x_2} & \cdots & \frac{\partial g}{\partial x_n} \\ \\
 * \frac{\partial g}{\partial x_1} & \frac{\partial^2 f}{\partial x_1^2} & \frac{\partial^2 f}{\partial x_1\,\partial x_2} & \cdots & \frac{\partial^2 f}{\partial x_1\,\partial x_n} \\ \\
 * \frac{\partial g}{\partial x_2} & \frac{\partial^2 f}{\partial x_2\,\partial x_1} & \frac{\partial^2 f}{\partial x_2^2} & \cdots & \frac{\partial^2 f}{\partial x_2\,\partial x_n} \\ \\
 * \vdots & \vdots & \vdots & \ddots & \vdots \\ \\
 * \frac{\partial g}{\partial x_n} & \frac{\partial^2 f}{\partial x_n\,\partial x_1} & \frac{\partial^2 f}{\partial x_n\,\partial x_2} & \cdots & \frac{\partial^2 f}{\partial x_n^2}
 * \end{bmatrix}
 * \]
 * <p/>
 * This implementation computes the bordered Hessian matrix numerically using the finite difference method.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Hessian_matrix#Bordered_Hessian">Wikipedia: Bordered Hessian</a>
 */
public class BorderedHessian extends SymmetricMatrix {

    /**
     * Construct the bordered Hessian matrix for multivariate functions <i>f</i> and <i>g</i> at point <i>x</i>.
     * The dimension is \((n+1) \times (n+1)\),
     * where <i>n</i> is the domain dimension of both <i>f</i> and <i>g</i>.
     *
     * @param f a multivariate function, usually an objective function
     * @param g a multivariate function, usually a constraint function
     * @param x the point to evaluate the bordered Hessian at
     */
    public BorderedHessian(RealScalarFunction f, RealScalarFunction g, Vector x) {
        super(f.dimensionOfDomain() + 1);//+1 for the border (g)

        SuanShuUtils.assertArgument(f.dimensionOfDomain() == g.dimensionOfDomain(), "f and g must have the same dimension for the domains");

        //the gradient part
        for (int i = 2; i <= nRows(); ++i) {
            FiniteDifference dg = new FiniteDifference(g, new int[]{i - 1});//dg/dxi
            double dgdx = dg.evaluate(x);
            super.set(1, i, dgdx);
        }

        //the Hessian part
        for (int i = 2; i <= nRows(); ++i) {
            for (int j = i; j <= nCols(); ++j) {
                FiniteDifference df = new FiniteDifference(f, new int[]{i - 1, j - 1});//df/dxidxj
                double dfdx = df.evaluate(x);
                super.set(i, j, dfdx);
            }
        }
    }
}
