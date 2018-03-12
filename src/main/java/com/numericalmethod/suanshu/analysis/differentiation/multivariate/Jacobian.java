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

import com.numericalmethod.suanshu.analysis.function.rn2r1.Projection;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.List;

/**
 * The Jacobian matrix is the matrix of all first-order partial derivatives of a vector-valued function.
 * For a <i>R<sup>n</sup>->R<sup>m</sup></i> function, we have a \(m \times n\) matrix.
 * \[
 * J=\begin{bmatrix} \dfrac{\partial y_1}{\partial x_1} & \cdots & \dfrac{\partial y_1}{\partial x_n} \\ \vdots & \ddots & \vdots \\ \dfrac{\partial y_m}{\partial x_1} & \cdots & \dfrac{\partial y_m}{\partial x_n} \end{bmatrix}
 * \]
 * <p/>
 * This implementation computes the Jacobian matrix numerically using the finite difference method.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Jacobian_matrix_and_determinant">Wikipedia: Jacobian matrix and determinant</a>
 */
public class Jacobian extends DenseMatrix {

    /**
     * Construct the Jacobian matrix for a multivariate function <i>f</i> at point <i>x</i>.
     *
     * @param f a multivariate function
     * @param x the point to evaluate the Jacobian matrix at
     */
    public Jacobian(RealVectorFunction f, Vector x) {
        super(f.dimensionOfRange(), f.dimensionOfDomain());

        for (int i = 1; i <= nRows(); ++i) {//fill by rows; same f projection
            Projection fi = new Projection(f, i);
            for (int j = 1; j <= nCols(); ++j) {//index for variable, x
                FiniteDifference df = new FiniteDifference(fi, new int[]{j});//∂fi/∂xj
                super.set(i, j, df.evaluate(x));
            }
        }
    }

    /**
     * Construct the Jacobian matrix for a multivariate function <i>f</i> at point <i>x</i>.
     *
     * @param f a multivariate function in the form of an array of univariate functions
     * @param x the point to evaluate the Jacobian matrix at
     */
    public Jacobian(final RealScalarFunction[] f, Vector x) {
        this(
                new RealVectorFunction() {

                    @Override
                    public Vector evaluate(Vector x) {
                        double[] v = new double[f.length];

                        for (int i = 0; i < f.length; ++i) {
                            double fx = f[i].evaluate(x);
                            v[i] = fx;
                        }

                        return new DenseVector(v);
                    }

                    @Override
                    public int dimensionOfDomain() {
                        return f[0].dimensionOfDomain();
                    }

                    @Override
                    public int dimensionOfRange() {
                        return f.length;
                    }
                },
                new DenseVector(x));
    }

    /**
     * Construct the Jacobian matrix for a multivariate function <i>f</i> at point <i>x</i>.
     *
     * @param f a multivariate function in the form of a list of univariate functions
     * @param x the point to evaluate the Jacobian matrix at
     */
    public Jacobian(final List<RealScalarFunction> f, Vector x) {
        this(f.toArray(new RealScalarFunction[0]), x);
    }
}
