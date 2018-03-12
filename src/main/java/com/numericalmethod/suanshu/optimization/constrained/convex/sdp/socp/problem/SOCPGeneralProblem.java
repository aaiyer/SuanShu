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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.problem;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;

/**
 * Many convex programming problems can be represented in the following form.
 * Minimize
 * \[
 * f^' x
 * \]
 * subject to
 * \[
 * \lVert A_i x + b_i \rVert_2 \leq c_i^T x + d_i,\quad i = 1,\dots,m \\
 * Fx = g
 * \]
 * where the problem parameters are
 * \[
 * x\in\mathbb{R}^n, f \in \mathbb{R}^n, \ A_i \in \mathbb{R}^{{n_i}\times n}, \ b_i \in \mathbb{R}^{n_i}, \ c_i \in \mathbb{R}^n, \ d_i \in \mathbb{R}, \ F \in \mathbb{R}^{p\times n}, g \in \mathbb{R}^p
 * \]
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Second-order_cone_programming">Wikipedia: Second-order cone programming</a>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "eq 14.104, Section 14.7, Second-Order Cone Programming," Practical Optimization: Algorithms and Engineering Applications."
 * </ul>
 */
public class SOCPGeneralProblem extends SOCPDualProblem {

    /**
     * Construct a general Second Order Conic Programming problem.
     * Minimize
     * \[
     * f^' x
     * \]
     * subject to
     * \[
     * \lVert A_i x + b_i \rVert_2 \leq c_i^T x + d_i,\quad i = 1,\dots,m
     * \]
     *
     * @param f <i>f</i>
     * @param A <i>A</i>
     * @param c <i>c</i>
     * @param b <i>b</i>
     * @param d <i>d</i>
     */
    public SOCPGeneralProblem(Vector f, Matrix[] A, Vector[] c, Vector[] b, double[] d) {
        super(f, getAhat(A, b), getchat(c, d));

        final int m = f.size();

        for (Matrix Ai : A) {
            assertArgument(m == Ai.nRows(), "all A row sizes must match f's size");
        }

        for (Vector bi : b) {
            assertArgument(m == bi.size(), "all b sizes must match f's size");
        }
    }

    private static Matrix[] getAhat(Matrix[] A, Vector[] b) {
        Matrix[] Ahat = new Matrix[A.length];

        for (int i = 0; i < Ahat.length; ++i) {
            Ahat[i] = CreateMatrix.cbind(new DenseMatrix(b[i]), A[i]);
        }

        return Ahat;
    }

    private static Vector[] getchat(Vector[] c, double[] d) {
        Vector[] chat = new Vector[c.length];

        for (int i = 0; i < chat.length; ++i) {
            chat[i] = CreateVector.concat(new DenseVector(d[i]), c[i]);
        }

        return chat;
    }
}
