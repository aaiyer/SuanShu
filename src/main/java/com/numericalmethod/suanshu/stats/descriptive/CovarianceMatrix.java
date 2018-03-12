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
package com.numericalmethod.suanshu.stats.descriptive;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;

/**
 * This class computes the Covariance matrix of a matrix,
 * where the <i>(i, j)</i> entry is the covariance of the <i>i</i>-th column and <i>j</i>-th column of the matrix.
 * <p/>
 * The R equivalent function is {@code cov}.
 *
 * @author Haksun Li
 */
public class CovarianceMatrix extends DenseMatrix {

    /**
     * Construct the covariance matrix of a matrix.
     *
     * @param A a matrix
     */
    public CovarianceMatrix(Matrix A) {
        super(A.nCols(), A.nCols());

        for (int i = 1; i <= A.nCols(); ++i) {
            for (int j = i; j <= A.nCols(); ++j) {
                double[] data1 = new double[A.nRows()];
                for (int k = 1; k <= A.nRows(); ++k) {
                    data1[k - 1] = A.get(k, i);
                }

                double[] data2 = new double[A.nRows()];
                for (int k = 1; k <= A.nRows(); ++k) {
                    data2[k - 1] = A.get(k, j);
                }

                Covariance cov = new Covariance(new double[][]{data1, data2});
                super.set(i, j, cov.value());
                super.set(j, i, cov.value());
            }
        }
    }
}
