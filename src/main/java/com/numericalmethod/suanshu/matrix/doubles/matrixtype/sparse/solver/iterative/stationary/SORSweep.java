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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is a building block for
 * {@linkplain SuccessiveOverrelaxationSolver SOR} and
 * {@linkplain SymmetricSuccessiveOverrelaxationSolver SSOR}
 * to perform the forward or backward sweep. That is, solving the <i>n</i> equations in
 * <i>Ax = b</i> sequentially (forward or backward), while using the updated
 * components of <i>x</i> as soon as they are available.
 *
 * @author Ken Yiu
 */
public class SORSweep {

    private final Matrix A;
    private final Vector b;
    private final int n;
    private final double omega;

    /**
     * Construct an instance to perform forward or backward sweep for a linear
     * system <i>Ax = b</i>.
     *
     * @param A     the coefficient matrix
     * @param b     a vector
     * @param omega the extrapolation factor
     */
    public SORSweep(Matrix A, Vector b, double omega) {
        SuanShuUtils.assertArgument(DimensionCheck.isSquare(A), "A must be a square matrix");
        checkMatrix(A);

        this.A = A;
        this.b = b;
        this.n = A.nCols();
        this.omega = omega;
    }

    /**
     * Perform a forward sweep.
     *
     * @param x the original iterate
     * @return the next iterate
     */
    public Vector forward(Vector x) {
        Vector xNext = new DenseVector(n).ZERO();

        // solve each equation independently
        for (int i = 1; i <= n; ++i) {
            double xi = 0.;
            for (int j = 1; j < i; ++j) {
                xi += A.get(i, j) * xNext.get(j); // from the new iterate
            }
            for (int j = i + 1; j <= n; ++j) {
                xi += A.get(i, j) * x.get(j); // from the old iterate
            }

            xi = (b.get(i) - xi) / A.get(i, i);
            xi = x.get(i) + omega * (xi - x.get(i)); // weighted average
            xNext.set(i, xi);
        }

        return xNext;
    }

    /**
     * Perform a backward sweep.
     *
     * @param x the original iterate
     * @return the next iterate
     */
    public Vector backward(Vector x) {
        Vector xNext = new DenseVector(n).ZERO();

        // solve each equation independently
        for (int i = n; i >= 1; --i) {
            double xi = 0.;
            for (int j = 1; j < i; ++j) {
                xi += A.get(i, j) * x.get(j); // from the old iterate
            }
            for (int j = i + 1; j <= n; ++j) {
                xi += A.get(i, j) * xNext.get(j); // from the new iterate
            }

            xi = (b.get(i) - xi) / A.get(i, i);
            xi = x.get(i) + omega * (xi - x.get(i)); // weighted average
            xNext.set(i, xi);
        }

        return xNext;
    }

    private static void checkMatrix(Matrix A) {
        for (int i = 1; i <= A.nCols(); ++i) {
            if (Double.compare(A.get(i, i), 0.) == 0) {
                throw new IllegalArgumentException("diagonal entries must be non-zero");
            }
        }
    }
}
