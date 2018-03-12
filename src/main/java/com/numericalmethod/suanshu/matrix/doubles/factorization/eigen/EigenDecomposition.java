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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.List;

/**
 * Let <i>A</i> be a square, <em>diagonalizable</em> <i>N × N</i> matrix with <i>N</i> linearly independent eigenvectors.
 * Then <i>A</i> can be factorized as <i>Q * D * Q' = A</i>.
 * <i>Q</i> is the square <i>N × N</i> matrix whose <i>i</i>-th column is the eigenvector of <i>A</i>,
 * and <i>D</i> is the diagonal matrix whose diagonal elements are the corresponding eigenvalues.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Eigendecomposition_of_a_matrix#Eigendecomposition_of_a_matrix">Wikipedia: Eigendecomposition of a matrix</a>
 */
public class EigenDecomposition {

    private final int dim;
    private final Eigen eigen;

    /**
     * Run the eigen decomposition on a <em>square</em> matrix.
     *
     * @param A       a square, <em>diagonalizable</em> matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public EigenDecomposition(Matrix A, double epsilon) {
        SuanShuUtils.assertArgument(DimensionCheck.isSquare(A), "eigen decomposition applies only to square matrices");

        this.dim = A.nRows();
        this.eigen = new Eigen(A, Eigen.Method.QR, epsilon);
    }

    /**
     * Run the eigen decomposition on a <em>square</em> matrix.
     *
     * @param A a square, <em>diagonalizable</em> matrix
     */
    public EigenDecomposition(Matrix A) {
        this(A, 1000. * SuanShuUtils.autoEpsilon(A));
    }

    /**
     * Get the diagonal matrix <i>D</i> as in <i>Q * D * Q' = A</i>.
     * <p/>
     * Note that for the moment we support only real eigenvalues.
     *
     * @return <i>D</i>
     */
    public DiagonalMatrix D() {
        double[] diag = new double[dim];

        int k = 0;
        for (int i = 0; i < eigen.size(); ++i) {
            EigenProperty property = eigen.getProperty(i);
            for (int j = 0; j < property.algebraicMultiplicity(); ++j) {
                diag[k++] = property.eigenvalue().doubleValue();
            }
        }

        return new DiagonalMatrix(diag);
    }

    /**
     * Get <i>Q</i> as in <i>Q * D * Q' = A</i>.
     * <p/>
     * Note that for the moment we support only real eigenvalues.
     *
     * @return <i>Q</i>
     */
    public Matrix Q() {
        List<Vector> list = new ArrayList<Vector>();

        for (int i = 0; i < eigen.size(); ++i) {
            EigenProperty property = eigen.getProperty(i);
            list.addAll(property.eigenbasis());//TODO: does this work for multiple eigenvectors?
        }

        return CreateMatrix.cbind(list);
    }

    /**
     * Get <i>Q'</i> as in <i>Q * D * Q' = A</i>.
     * <p/>
     * Note that for the moment we support only real eigenvalues.
     *
     * @return {@code Q.t()}
     */
    public Matrix Qt() {
        return Q().t();
    }
}
