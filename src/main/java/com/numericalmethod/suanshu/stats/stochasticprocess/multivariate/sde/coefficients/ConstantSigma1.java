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
import com.numericalmethod.suanshu.matrix.doubles.Matrix;

/**
 * The class represents a constant diffusion coefficient function.
 *
 * @author Haksun Li
 */
public class ConstantSigma1 implements Diffusion {

    /**
     * the constant diffusion matrix
     */
    private final Matrix sigma;

    /**
     * Construct a constant diffusion coefficient function.
     *
     * @param sigma the constant diffusion matrix
     */
    public ConstantSigma1(Matrix sigma) {
        this.sigma = sigma;
    }

    public Matrix evaluate(Ft ft) {
        return sigma;
    }

    public int nRows() {
        return sigma.nRows();
    }

    public int nCols() {
        return sigma.nCols();
    }
}
