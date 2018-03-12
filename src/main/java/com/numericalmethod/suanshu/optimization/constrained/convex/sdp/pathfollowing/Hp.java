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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.pathfollowing;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;

/**
 * This is the symmetrization operator as defined in eq. 6 in the reference.
 * \[
 * H_p: C^{n \times n} \rightarrow \textit{H}^n \\
 * H_p(U) = \frac{1}{2}[PUP^{-1}]+P^{-*}U^*P^*
 * \]
 *
 * @author Haksun Li
 * @see "K. C. Toh, M. J. Todd, R. H. Tütüncü, "SDPT3 -- a MATLAB software package for semidefinite programming, version 2.1," OPTIMIZATION METHODS AND SOFTWARE. 1999."
 */
public class Hp {

    private final ImmutableMatrix P;
    private final ImmutableMatrix Pinv;
    private final boolean isIdentity;

    /**
     * Construct a symmetrization operator.
     *
     * @param P a parameter matrix
     */
    public Hp(Matrix P) {
        this.P = new ImmutableMatrix(P);
        this.Pinv = new ImmutableMatrix(new Inverse(P));
        this.isIdentity = false;
    }

    /**
     * Construct the symmetrization operator using an identity matrix.
     */
    public Hp() {
        this.P = null;
        this.Pinv = null;
        this.isIdentity = true;
    }

    /**
     * Compute \(H_p(U) = \frac{1}{2}[PUP^{-1}]+P^{-*}U^*P^*\).
     *
     * @param U a matrix
     * @return \(H_p(U)\)
     */
    public Matrix evaluate(Matrix U) {
        if (isIdentity) {
            return evaluate4I(U);
        }

        Matrix HpU = P.multiply(U).multiply(Pinv);
        HpU = HpU.add(HpU.t());
        HpU = HpU.scaled(0.5);

        return HpU;
    }

    private Matrix evaluate4I(Matrix U) {
        Matrix HpU = U.add(U.t()).scaled(0.5);
        return HpU;
    }
}
