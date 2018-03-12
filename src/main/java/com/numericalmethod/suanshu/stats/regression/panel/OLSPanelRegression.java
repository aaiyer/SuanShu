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
package com.numericalmethod.suanshu.stats.regression.panel;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.stats.regression.linear.LMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.ols.OLSRegression;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import java.util.List;

/**
 * Implementation of {@link PanelRegression} using OLS (ordinary least square).
 *
 * @author Chung Lee
 */
public class OLSPanelRegression implements PanelRegression {

    @Override
    public PanelRegressionResult solve(List<Vector> yt, List<Matrix> At, boolean addIntercept) {
        SuanShuUtils.assertArgument(!yt.isEmpty(), "empty vector list");
        SuanShuUtils.assertArgument(yt.size() == At.size(),
                "number of vectors and number of matrices do not match");

        checkDimensions(yt, At);

        Matrix M = CreateMatrix.rbind(At.toArray(new Matrix[0]));

        Vector vec = CreateVector.concat(yt.toArray(new Vector[0]));

        LMProblem panel = new LMProblem(vec, M, addIntercept);
        OLSRegression ols = new OLSRegression(panel);

        return new PanelRegressionResult(ols.beta, ols.residuals);
    }

    private static void checkDimensions(List<Vector> yt, List<Matrix> At) {
        int ySize = yt.get(0).size();

        int nRows = At.get(0).nRows();
        int nCols = At.get(0).nCols();

        SuanShuUtils.assertArgument(ySize == nRows,
                "size of y0 (%d) and number of rows of A0 (%d) do not match", ySize, nRows);

        int i = 0;
        for (Vector y : yt) {
            int size = y.size();
            SuanShuUtils.assertArgument(ySize == size,
                    "size of y%d (%d) is not consistent", i, size);
            i++;
        }

        i = 0;
        for (Matrix A : At) {
            int nRowsA = A.nRows();
            int nColsA = A.nCols();
            SuanShuUtils.assertArgument(nRows == nRowsA && nCols == nColsA,
                    "dimension of A%d (%d by %d) is not consistent", i, nRowsA, nColsA);
            i++;
        }
    }
}
