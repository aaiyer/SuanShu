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
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.List;

/**
 * This interface defines methods for solving panel regression.
 *
 * @author Chung Lee
 * @see <a href="http://en.wikipedia.org/wiki/Panel_analysis">Wikipedia: Panel analysis</a>
 */
public interface PanelRegression {

    /**
     * Solve panel regression of the following form:
     * \[
     * y_{t} = A_{t} x + \epsilon_{t}
     * \]
     *
     * @param yt           list of column vectors \( y_{t} \)
     * @param At           list of matrices \( A_{t} \)
     * @param addIntercept {@code true} iff to add an intercept term to the regression
     * @return instance of {@link PanelRegressionResult}
     */
    public PanelRegressionResult solve(List<Vector> yt, List<Matrix> At, boolean addIntercept);
}
