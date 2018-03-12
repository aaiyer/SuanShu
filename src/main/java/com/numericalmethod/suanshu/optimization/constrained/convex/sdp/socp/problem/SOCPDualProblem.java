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

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblem;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import java.util.ArrayList;

/**
 * This is the Dual Second Order Conic Programming problem.
 * \[
 * \max_y \mathbf{b'y} \textrm{ s.t.,} \\
 * \mathbf{\hat{A}_i'y + s_i = \hat{c}_i} \\
 * s_i \in K_i, i = 1, 2, ..., q
 * \]
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "eq 14.102, Section 14.7, Second-Order Cone Programming," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SOCPDualProblem implements ConstrainedOptimProblem {

    //<editor-fold defaultstate="collapsed" desc="EqualityConstraints">
    public static class EqualityConstraints implements com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints {

        private final ImmutableVector b;
        private final ImmutableMatrix[] A;
        private final ImmutableVector[] c;
        private final int m;
        private final int q;

        /**
         * Construct the equality constraints for a dual SOCP problem, \(\max_y \mathbf{b'y} \textrm{ s.t.,} \\ \mathbf{\hat{A}_i'y + s_i = \hat{c}_i} \\ s_i \in K_i, i = 1, 2, ..., q\).
         *
         * @param b \(b\)
         * @param A \(A_i\)'s
         * @param c \(c_i\)'s
         */
        public EqualityConstraints(final Vector b, final Matrix[] A, final Vector[] c) {
            this.m = b.size();
            this.b = new ImmutableVector(b);

            this.q = A.length;
            this.A = new ImmutableMatrix[q];
            for (int i = 0; i < A.length; ++i) {
                this.A[i] = new ImmutableMatrix(A[i]);
            }

            this.c = new ImmutableVector[q];
            for (int i = 0; i < c.length; ++i) {
                this.c[i] = new ImmutableVector(c[i]);
            }
        }

        @Override
        public ArrayList<RealScalarFunction> getConstraints() {
            ArrayList<RealScalarFunction> h = new ArrayList<RealScalarFunction>();

            for (int j = 0; j < q; ++j) {
                final int i = j;
                h.add(
                        new RealScalarFunction() {

                            @Override
                            public Double evaluate(Vector x) {
                                Vector y = CreateVector.subVector(x, 1, m);

                                int begin = m + 1;
                                int end = m + A[0].nCols();
                                for (int k = 1; k <= i; ++k) {//TODO: check logic; not sure if this is correct
                                    begin += A[k - 1].nCols();
                                    end += A[k].nCols();
                                }
                                Vector si = CreateVector.subVector(x, begin, end);

                                Vector sum = A[i].t().multiply(y).add(si).minus(c[i]);
                                return sum.norm();
                            }

                            @Override
                            public int dimensionOfDomain() {
                                int dim = m;

                                for (int r = 0; r < q; ++r) {
                                    dim += A[r].nCols();
                                }

                                return dim;
                            }

                            @Override
                            public int dimensionOfRange() {
                                return 1;
                            }
                        });
            }

            return h;
        }

        @Override
        public int dimension() {
            int dim = m;

            for (int r = 0; r < q; ++r) {
                dim += A[r].nCols();
            }

            return dim;
        }

        @Override
        public int size() {
            return q;
        }
    }
    //</editor-fold>

    private final ConstrainedOptimProblemImpl1 problem;
    private final ImmutableVector b;
    private final ImmutableMatrix[] A;//A^
    private final ImmutableVector[] c;//c^
    private final int m;
    private final int q;

    /**
     * Construct a dual SODP problem.
     * \[
     * \max_y \mathbf{b'y} \textrm{ s.t.,} \\
     * \mathbf{\hat{A}_i'y + s_i = \hat{c}_i} \\
     * s_i \in K_i, i = 1, 2, ..., q
     * \]
     *
     * @param b \(b\)
     * @param A \(A_i\)'s
     * @param c \(c_i\)'s
     */
    public SOCPDualProblem(final Vector b, final Matrix[] A, final Vector[] c) {
        this.problem = new ConstrainedOptimProblemImpl1(
                new RealScalarFunction() {

                    @Override
                    public Double evaluate(Vector x) {
                        final int m = b.size();
                        Vector y = CreateVector.subVector(x, 1, m);
                        double by = b.innerProduct(y);
                        return by;
                    }

                    @Override
                    public int dimensionOfDomain() {
                        int dim = b.size();
                        int q = A.length;

                        for (int i = 0; i < q; ++i) {
                            dim += A[i].nCols();
                        }

                        return dim;
                    }

                    @Override
                    public int dimensionOfRange() {
                        return 1;
                    }
                },
                new EqualityConstraints(b, A, c),
                null);

        this.m = b.size();
        this.b = new ImmutableVector(b);

        this.q = A.length;
        this.A = new ImmutableMatrix[q];
        this.c = new ImmutableVector[q];
        for (int i = 0; i < q; ++i) {
            SuanShuUtils.assertArgument(A[i].nCols() == c[i].size(), "A[i] #columns must = c[i] size");
            this.A[i] = new ImmutableMatrix(A[i]);
            this.c[i] = new ImmutableVector(c[i]);
        }
    }

    @Override
    public int dimension() {
        return problem.dimension();
    }

    @Override
    public RealScalarFunction f() {
        return problem.f();
    }

    @Override
    public LessThanConstraints getLessThanConstraints() {
        return problem.getLessThanConstraints();
    }

    @Override
    public EqualityConstraints getEqualityConstraints() {
        return (EqualityConstraints) problem.getEqualityConstraints();
    }

    /**
     * Get the dimension of the system, i.e., <i>m</i> = the dimension of <i>y</i>.
     *
     * @return the dimension of the system
     */
    public int m() {
        return m;
    }

    /**
     * Get the number of A matrices.
     *
     * @return the number of A matrices
     */
    public int q() {
        return q;
    }

    /**
     * Get <i>b</i>.
     *
     * @return <i>b</i>
     */
    public ImmutableVector b() {
        return b;
    }

    /**
     * Get <i>c<sub>i</sub></i>.
     *
     * @param i an index to the c's, counting from 1
     * @return <i>c<sub>i</sub></i>
     */
    public ImmutableVector c(int i) {
        return c[i - 1];
    }

    /**
     * Get <i>A<sub>i</sub></i>.
     *
     * @param i an index to the A's, counting from 1
     * @return <i>A<sub>i</sub></i>
     */
    public ImmutableMatrix A(int i) {
        return A[i - 1];
    }

    /**
     * Get the number of columns of <i>A<sub>i</sub></i>.
     *
     * @param i an index to the A's, counting from 1
     * @return the number of columns of <i>A<sub>i</sub></i>
     */
    public int n(int i) {
        return A(i).nCols();
    }

    /**
     * \[
     * A = [A_1, A_2, ... A_q]
     * \]
     *
     * @return <i>A</i>
     * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 14.8.1, Assumptions and KKT conditions," Practical Optimization: Algorithms and Engineering Applications."
     */
    public ImmutableMatrix A() {
        Matrix A = CreateMatrix.cbind(this.A);
        return new ImmutableMatrix(A);
    }

    public ImmutableVector c() {
        Vector c = CreateVector.concat(this.c);
        return new ImmutableVector(c);
    }
}
