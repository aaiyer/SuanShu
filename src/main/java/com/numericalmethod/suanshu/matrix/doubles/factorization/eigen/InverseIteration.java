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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * Inverse iteration is an iterative eigenvalue algorithm.
 * It finds an approximate eigenvector when an approximation to an eigenvalue is already known.
 * Inverse iteration does not apply when λ is the <em>exact</em> eigenvalue because (A - λI) is singular.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Inverse_iteration">Wikipedia: Inverse iteration</a>
 * <li>"G. H. Golub, C. F. van Loan, "Section 2.4.1," Matrix Computations, 3rd edition."
 * <li>COMPUTING AN EIGENVECTOR WITH INVERSE ITERATION, ILSE C. F. IPSEN.
 * </ul>
 */
public class InverseIteration {

    /**
     * This interface defines the convergence criterion.
     */
    public static interface StoppingCriterion {

        /**
         * Check whether we stop with the current eigenvector.
         *
         * @param v an eigenvector
         * @return {@code true} if we are satisfied with the eigenvector
         */
        public boolean toStop(Vector v);
    }

    private final ImmutableMatrix A;
    private final ImmutableMatrix A1inv;
    private final StoppingCriterion criterion;

    /**
     * Construct an instance of <tt>InverseIteration</tt> to find the corresponding eigenvector.
     *
     * @param A         a matrix
     * @param lambda    an eigenvalue
     * @param criterion a convergence criterion
     */
    public InverseIteration(Matrix A, double lambda, StoppingCriterion criterion) {
        this.A = new ImmutableMatrix(A);
        this.criterion = criterion;

        Matrix A1 = A.minus(A.ONE().scaled(lambda));//(A - λI)
        this.A1inv = new ImmutableMatrix(new Inverse(A1, 0));
    }

    /**
     * Construct an instance of <tt>InverseIteration</tt> to find the corresponding eigenvector.
     *
     * @param A      a matrix
     * @param lambda an eigenvalue
     */
    public InverseIteration(final Matrix A, final double lambda) {
        this(
                A,
                lambda,
                new StoppingCriterion() {//as suggested in "G. H. Golub, C. F. van Loan, "eq. 7.6.2," Matrix Computations, 3rd edition."

                    private final Matrix A1 = A.minus(A.ONE().scaled(lambda));//(A - λI)
                    private final double u = Constant.unitRoundOff();

                    @Override
                    public boolean toStop(Vector v) {
                        Vector r = A1.multiply(v);
                        double rNorm = r.norm(Integer.MAX_VALUE);
                        double aNorm = MatrixMeasure.max(A);

                        return rNorm <= u * aNorm;
                    }
                });
    }

    /**
     * Get an eigenvector from an initial guess.
     *
     * @param v0            an initial guess of eigenvector
     * @param maxIterations the maximum number of iterations, e.g., 10
     * @return an approximate eigenvector
     */
    public Vector getEigenVector(Vector v0, int maxIterations) {
        Vector v = v0;

        for (int i = 0; i < maxIterations; ++i) {
            Vector q = A1inv.multiply(v);
            q = q.scaled(1. / q.norm());

            v = q;
            if (criterion.toStop(v)) {
                break;
            }
        }

        return new ImmutableVector(v);
    }

    /**
     * Get an eigenvector.
     *
     * @return an approximate eigenvector
     */
    public Vector getEigenVector() {
        return getEigenVector(new DenseVector(A.nRows(), 1.0), 10);
    }
}
