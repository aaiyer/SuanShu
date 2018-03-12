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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.problem;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import java.util.ArrayList;

/**
 * A dual SDP problem, as in eq. 14.4 in the reference, takes the following form.
 * \[
 * \max_y \mathbf{b'y} \textrm{, s.t., } \\
 * \sum_{i=1}^{p}y_i\mathbf{A_i}+\textbf{S} = \textbf{C}, \textbf{S} \succeq \textbf{0}
 * \]
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 14.2, Primal and Dual SDP Problems," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SDPDualProblem extends ConstrainedOptimProblemImpl1 {
    //<editor-fold defaultstate="collapsed" desc="EqualityConstraints">

    /**
     * This is the collection of equality constraints:
     * \[
     * \sum_{i=1}^{p}y_i\mathbf{A_i}+\textbf{S} = \textbf{C}, \textbf{S} \succeq \textbf{0}
     * \]
     */
    //TODO: S being positive definite
    public static class EqualityConstraints implements com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints {

        private final ImmutableMatrix C;
        private final ImmutableMatrix[] A;
        private final int p;
        private final int n;

        /**
         * Construct the equality constraints for a dual SDP problem, \(\sum_{i=1}^{p}y_i\mathbf{A_i}+\textbf{S} = \textbf{C}, \textbf{S} \succeq \textbf{0}\).
         *
         * @param b \(b\)
         * @param C \(C\)
         * @param A \(A\)
         */
        public EqualityConstraints(final Vector b, final SymmetricMatrix C, final SymmetricMatrix[] A) {
            this.C = new ImmutableMatrix(C);
            this.A = new ImmutableMatrix[A.length];
            for (int i = 0; i < A.length; ++i) {
                this.A[i] = new ImmutableMatrix(A[i]);
            }

            this.p = b.size();
            this.n = C.nRows();
        }

        @Override
        public ArrayList<RealScalarFunction> getConstraints() {
            ArrayList<RealScalarFunction> h = new ArrayList<RealScalarFunction>();
            h.add(
                    new RealScalarFunction() {

                        @Override
                        public Double evaluate(Vector x) {//eq.14.4b
                            Vector y = CreateVector.subVector(x, 1, p);
                            Matrix S = new DenseMatrix(CreateVector.subVector(x, p + 1, n * n).toArray(), n, n);

                            Matrix sum = S;
                            for (int i = 0; i < p; ++i) {
                                sum = sum.add(A[i].scaled(y.get(i + 1)));
                            }
                            sum = sum.minus(C);

                            return MatrixMeasure.Frobenius(sum);
                        }

                        @Override
                        public int dimensionOfDomain() {
                            return p + n * n;
                        }

                        @Override
                        public int dimensionOfRange() {
                            return 1;
                        }
                    });

            return h;
        }

        @Override
        public int dimension() {
            return p + n * n;
        }

        @Override
        public int size() {
            return 1;
        }
    }
    //</editor-fold>

    private final ImmutableVector b;
    private final SymmetricMatrix C;
    private final SymmetricMatrix[] A;

    /**
     * Construct a dual SDP problem.
     * \[
     * \max_y \mathbf{b'y} \textrm{, s.t., } \\
     * \sum_{i=1}^{p}y_i\mathbf{A_i}+\textbf{S} = \textbf{C}, \textbf{S} \succeq \textbf{0}
     * \]
     *
     * @param b \(b\)
     * @param C \(C\)
     * @param A \(A\)
     */
    public SDPDualProblem(final Vector b, final SymmetricMatrix C, final SymmetricMatrix[] A) {
        super(
                new RealScalarFunction() {

                    @Override
                    public Double evaluate(Vector x) {
                        final int p = b.size();
                        Vector y = CreateVector.subVector(x, 1, p);
                        double by = b.innerProduct(y);
                        return by;
                    }

                    @Override
                    public int dimensionOfDomain() {
                        final int p = b.size();
                        final int n = C.nRows();
                        return p + n * n;
                    }

                    @Override
                    public int dimensionOfRange() {
                        return 1;
                    }
                },
                new EqualityConstraints(b, C, A),//TODO: S being positive definite
                null);

        this.b = new ImmutableVector(b);

        this.C = new SymmetricMatrix(C);

        this.A = new SymmetricMatrix[A.length];
        for (int i = 0; i < A.length; ++i) {
            SuanShuUtils.assertArgument(C.nRows() == A[i].nRows(), "all matrices must have the same dimension");
            this.A[i] = A[i];
        }
    }

    /**
     * Get the dimension of the square matrices <i>C</i> and <i>A</i>s.
     *
     * @return the dimension of the matrices
     */
    public int n() {
        return C.nRows();//dimension for C and As;
    }

    /**
     * Get the dimension of the system, i.e., <i>p</i> = the dimension of <i>y</i>, the number of variables.
     *
     * @return the dimension of the system
     */
    public int p() {
        return A.length;
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
     * Get <i>C</i>.
     *
     * @return <i>C</i>
     */
    public SymmetricMatrix C() {
        return new SymmetricMatrix(C);
    }

    /**
     * Get <i>A<sub>i</sub></i>.
     *
     * @param i an index to the A's, counting from 1
     * @return <i>A<sub>i</sub></i>
     */
    public SymmetricMatrix A(int i) {
        return new SymmetricMatrix(A[i - 1]);
    }
}
