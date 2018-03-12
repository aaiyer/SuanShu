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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate;

import com.numericalmethod.suanshu.analysis.function.matrix.R2toMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;

/**
 * This class represents an auto-correlation function for a multi-dimensional time series <i>{Xt}</i>.
 * <blockquote><code><pre>
 * γ<sub>ij</sub> = E((Xi - μi) * (Xj - μj)')
 * ρ(i, j) = γ<sub>ij</sub> / sqrt(γ<sub>ii</sub>γ<sub>jj</sub>)
 * </pre></code></blockquote>
 *
 * <p>
 * For stationary process, the auto-correlation depends only on the lag, |i - j|.
 * 
 * @author Haksun Li
 */
public abstract class AutoCorrelationFunction extends R2toMatrix {//TODO: make this R1toMatrix?

    /**
     * Get the auto-correlation matrix for Xi and Xj.
     *
     * @param i i > 0
     * @param j j > 0
     * @return an auto-correlation Matrix indexed by <i>[i, j]</i>
     */
    public Matrix get(int i, int j) {
        assertArgument(i > 0 && j > 0, "i > 0 and j > 0");

        return evaluate((double) i, (double) j);
    }
}
