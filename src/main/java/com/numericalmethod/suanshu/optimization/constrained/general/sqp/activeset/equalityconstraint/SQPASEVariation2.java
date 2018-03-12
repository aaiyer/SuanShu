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
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset.equalityconstraint;

import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This implementation tries to find an exact positive definite Hessian whenever possible.
 *
 * @author Haksun Li
 */
public class SQPASEVariation2 extends SQPASEVariation1 {

    /**
     * Construct a variation.
     *
     * @param r              Han's exact penalty function coefficient, the bigger the better, e.g., eq. 15.20
     * @param lower          the lower bound of alpha; the smaller the better but cannot be zero
     * @param discretization the number of points between <i>[lower, 1]</i> to search for alpha; the bigger the better
     */
    public SQPASEVariation2(double r, double lower, int discretization) {
        super(r, lower, discretization);
    }

    /**
     * Construct a variation.
     */
    public SQPASEVariation2() {
        this(100., 0.01, 50);
    }

    @Override
    public Matrix updateHessian(Vector x1, Vector v1, Vector d, Vector g0, Matrix A0, Matrix W0) {
        Matrix W1;
        if (!foundPositiveDefiniteHessian) {
            W1 = W(x1, v1);// try to find an exact positive definite Hessian; we just need one
            if (IsMatrix.positiveDefinite(W1)) {
                foundPositiveDefiniteHessian = true;
                return W1;
            }
        }

        return super.updateHessian(x1, v1, d, g0, A0, W0);
    }
}
