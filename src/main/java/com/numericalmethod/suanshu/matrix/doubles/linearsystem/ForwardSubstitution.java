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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * Forward substitution solves a matrix equation in the form <i>Lx = b</i>
 * by an iterative process for a lower triangular matrix <i>L</i>.
 * The process is so called because for a lower triangular matrix, one first computes <i>x<sub>1</sub></i>,
 * then substitutes that forward into the next equation to solve for <i>x<sub>2</sub></i>,
 * and repeats until <i>x<sub>n</sub></i>.
 * Note that some diagonal entries in <i>L</i> can be 0s, provided that the system of equations is consistent.
 * For example,
 * \[
 * \begin{bmatrix}
 * 0 & 0 & 0\\
 * 2 & 0 & 0\\
 * 4 & 5 & 6
 * \end{bmatrix} \times
 * \begin{bmatrix}
 * 0\\
 * 0\\
 * 30
 * \end{bmatrix} =
 * \begin{bmatrix}
 * 0\\
 * 0\\
 * 30
 * \end{bmatrix}
 * \]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Triangular_matrix#Forward_substitution">Wikipedia: Forward substitution</a>
 */
public class ForwardSubstitution {

    /**
     * Solve <i>Lx = b</i>.
     *
     * @param L a lower triangular matrix, representing the system of linear equations (the homogeneous part)
     * @param b a vector
     * @return a solution <i>x</i> such that <i>Lx = b</i>
     * @throws LinearSystemSolver.NoSolution if there is no solution to the system
     */
    public Vector solve(LowerTriangularMatrix L, Vector b) {
        SuanShuUtils.assertArgument(L.nRows() == b.size(), "b must have the same length as L's dimension");

        int dim = b.size();
        DenseVector x = new DenseVector(dim);
        for (int i = 1; i <= dim; ++i) {
            x.set(i, b.get(i));
            for (int j = 1; j < i; ++j) {
                x.set(i, x.get(i) - L.get(i, j) * x.get(j)); //x[i] -= L[i,j] * x[j];
            }

            if (!isZero(L.get(i, i), 0)) {
                x.set(i, x.get(i) / L.get(i, i));
            } else if (!isZero(x.get(i), 0)) {//L.get(i, i) == 0
                throw new LinearSystemSolver.NoSolution("no solution to this system of linear equations");
            }
        }

        return x;
    }
}
