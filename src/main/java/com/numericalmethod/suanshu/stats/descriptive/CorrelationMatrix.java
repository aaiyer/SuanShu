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

import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.diagonal;
import static java.lang.Math.sqrt;

/**
 * The correlation matrix of <i>n</i> random variables <i>X<sub>1</sub>, ..., X<sub>n</sub></i> is the <i>n × n</i> matrix whose <i>i,j</i> entry is <i>corr(X<sub>i</sub>, X<sub>j</sub></i>),
 * the correlation between <i>X<sub>1</sub></i> and <i>X<sub>n</sub></i>.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Correlation_and_dependence#Correlation_matrices">Wikipedia: Correlation matrices</a>
 */
public class CorrelationMatrix extends DenseMatrix {

    /**
     * Construct a correlation matrix from a covariance matrix.
     *
     * @param cov a covariance matrix
     */
    public CorrelationMatrix(Matrix cov) {
        super(cor(cov));
    }

    private static DenseMatrix cor(Matrix cov) {
        SuanShuUtils.assertArgument(IsMatrix.symmetric(cov, 0), "the covariance matrix must be symmetric");

        final int nRows = cov.nRows();
        final int nCols = cov.nCols();
        final Vector diag = diagonal(cov);

        DenseMatrix cor = new DenseMatrix(nRows, nCols);
        for (int i = 1; i <= cov.nCols(); ++i) {
            for (int j = i; j <= cov.nRows(); ++j) {
                cor.set(i, j, cov.get(i, j) / sqrt(diag.get(i)) / sqrt(diag.get(j)));
                cor.set(j, i, cov.get(i, j) / sqrt(diag.get(i)) / sqrt(diag.get(j)));
            }
        }

        return cor;
    }
}
