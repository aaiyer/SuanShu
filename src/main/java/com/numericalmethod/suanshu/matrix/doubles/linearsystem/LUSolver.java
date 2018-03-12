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
package com.numericalmethod.suanshu.matrix.doubles.linearsystem;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.MatrixSingularityException;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Use LU decomposition to solve <i>Ax = b</i> where <i>A</i> is square and
 * <i>det(A) != 0</i>.
 * The dimensions of <i>A</i> and <i>b</i> must match. The algorithm goes as
 * follows.
 * <blockquote><pre><i>
 * Ax = b;
 * LUx = PAx = Pb
 * </i></pre></blockquote>
 * We first solve
 * <i>Ly = b</i> by forward substitution and then <i>Ux = y</i> by backward
 * substitution.
 *
 * @author Haksun Li
 * @see <a
 * href="http://en.wikipedia.org/wiki/LU_decomposition#Solving_linear_equations">Wikipedia:
 * Solving linear equations</a>
 */
public class LUSolver {

    /**
     * Solve <i>Ax = b</i>.
     *
     * @param problem a system of linear equations
     * @return x a solution such that <i>Ax = b</i>
     * @throws LinearSystemSolver.NoSolution if there is no solution to the system
     */
    public Vector solve(LSProblem problem) {
        final ImmutableMatrix A = problem.A();
        final ImmutableVector b = problem.b();
        
        SuanShuUtils.assertArgument(DimensionCheck.isSquare(A), "A must be a square matrix");

        try {
            com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.LU luDecomp =
                    new com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.LU(A,
                                                                                             0);
            LowerTriangularMatrix L = luDecomp.L();
            UpperTriangularMatrix U = luDecomp.U();
            PermutationMatrix P = luDecomp.P();

            Vector bb = P.multiply(b);
            ForwardSubstitution forward = new ForwardSubstitution();
            Vector y = forward.solve(L, bb);

            BackwardSubstitution backward = new BackwardSubstitution();
            Vector x = backward.solve(U, y);

            return x;
        } catch (MatrixSingularityException ex) {
            throw new LinearSystemSolver.NoSolution("no solution to this system of linear equations");
        }
    }
}
