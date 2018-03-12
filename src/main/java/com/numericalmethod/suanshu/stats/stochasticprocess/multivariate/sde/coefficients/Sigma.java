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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.coefficients;

import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.Ft;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.FtAdaptedRealFunction;

/**
 * This class provides an implementation of the diffusion coefficients in the form of a diffusion matrix.
 * Each matrix element is an F<sub>t</sub> adapted function.
 *
 * @author Haksun Li
 */
public abstract class Sigma implements Diffusion {

    public Matrix evaluate(Ft ft) {
        int n = ft.dim();
        int d = ft.nB();

        DenseMatrix sigma = new DenseMatrix(n, d);

        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= d; ++j) {
                FtAdaptedRealFunction f = sigma_i_j(i, j);
                double s_ij = f.evaluate(ft);
                sigma.set(i, j, s_ij);
            }
        }

        return sigma;
    }

    /**
     * Get the F<sub>t</sub> adapted function <i>D[i,j]</i> element in the diffusion matrix.
     * 
     * @param i the row index
     * @param j the column index
     * @return <i>D[i,j]</i>
     */
    public abstract FtAdaptedRealFunction sigma_i_j(int i, int j);
}
