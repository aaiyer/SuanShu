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
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface allows customization of certain operations in the Active Set algorithm to solve a general constrained minimization problem
 * using Sequential Quadratic Programming.
 *
 * @author Haksun Li
 */
public interface SQPASVariation {

    /**
     * Get the initial Hessian matrix.
     *
     * @param x0 the initial minimizer
     * @param v0 the initial Lagrange multipliers for equality constraints (lambda)
     * @param u0 the initial Lagrange multipliers for inequality constraints (mu)
     * @return the initial Hessian matrix, e.g., identity
     */
    public Matrix getInitialHessian(Vector x0, Vector v0, Vector u0);

    /**
     * Update the Hessian matrix using the latest iterates.
     *
     * @param x1  the next minimizer
     * @param v1  the next Lagrange multipliers for equality constraints (lambda)
     * @param u1  the next Lagrange multipliers for inequality constraints (mu)
     * @param d   the minimizer increment
     * @param g0  the gradient
     * @param Ae0 the set of active equality constraints
     * @param Ai0 the set of active inequality constraints
     * @param W0  the current Hessian matrix
     * @return the next Hessian matrix
     */
    public Matrix updateHessian(Vector x1, Vector v1, Vector u1, Vector d, Vector g0, Matrix Ae0, Matrix Ai0, Matrix W0);

    /**
     * Get the percentage increment along the minimizer increment direction.
     *
     * @param x the current minimizer
     * @param d the minimizer increment
     * @param v the Lagrange multipliers for equality constraints (lambda)
     * @param u the Lagrange multipliers for inequality constraints (mu)
     * @return the percentage increment
     */
    public double alpha(final Vector x, final Vector d, final Vector v, final Vector u);
}
