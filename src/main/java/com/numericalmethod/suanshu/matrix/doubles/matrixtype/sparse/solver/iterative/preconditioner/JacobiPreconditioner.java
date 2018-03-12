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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.preconditioner;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.diagonal;

/**
 * The Jacobi (or diagonal) preconditioner is one of the simplest forms of
 * preconditioning, such that the preconditioner is the diagonal of
 * the coefficient matrix, i.e., <i>P = diag(A)</i>.
 *
 * @author Ken Yiu
 */
public class JacobiPreconditioner implements Preconditioner {

    private final Vector Dinv;

    /**
     * Construct a Jacobi preconditioner.
     *
     * @param A a coefficient matrix
     */
    public JacobiPreconditioner(Matrix A) {
        SuanShuUtils.assertArgument(DimensionCheck.isSquare(A), "A must be a square matrix");

        Vector D = diagonal(A);
        Dinv = com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.foreach(
                D,
                new UnivariateRealFunction() {

                    @Override
                    public double evaluate(double x) {
                        return 1. / x;
                    }
                });
    }

    /**
     * Return <i>P<sup>-1</sup>x</i>, where <i>P</i> is the diagonal matrix
     * of <i>A</i>. The output vector <i>v</i>
     * has entries:
     * <blockquote><i>
     * v<sub>i</sub> = x<sub>i</sub> / A<sub>i,i</sub>
     * </i></blockquote>
     *
     * @param x a vector
     * @return <i>P<sup>-1</sup>x</i>
     */
    @Override
    public Vector solve(Vector x) {
        return x.multiply(Dinv);
    }

    /**
     * <i>P<sup>t</sup> = P<sup>-1</sup></i> for Jacobi preconditioner.
     *
     * @param x a vector
     * @return <i>P<sup>-1</sup>x</i>
     */
    @Override
    public Vector transposeSolve(Vector x) {
        return solve(x);
    }
}
