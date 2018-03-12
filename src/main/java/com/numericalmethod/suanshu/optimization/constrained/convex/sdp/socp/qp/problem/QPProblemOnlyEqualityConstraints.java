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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.problem;

import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.svd.SVD;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.matrix.doubles.operation.PseudoInverse;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A quadratic programming problem with only equality constraints can be converted into
 * a equivalent quadratic programming problem without constraints, hence a mere quadratic function.
 *
 * @author Haksun Li
 */
public class QPProblemOnlyEqualityConstraints extends QuadraticFunction {

    private final LinearEqualityConstraints equal;
    private final ImmutableMatrix Aplus;
    private final ImmutableMatrix Vr;

    private static class ModifiedQPProblem {

        private final QuadraticFunction f;
        private final Matrix Aplus;
        private final Matrix Vr;

        private ModifiedQPProblem(QuadraticFunction f, Matrix Aplus, Matrix Vr) {
            this.f = f;
            this.Aplus = new ImmutableMatrix(Aplus);
            this.Vr = new ImmutableMatrix(Vr);
        }
    }

    /**
     * Construct a quadratic programming problem with only equality constraints.
     *
     * @param f     the quadratic objective function to be minimized
     * @param equal the linear equality constraints
     */
    public QPProblemOnlyEqualityConstraints(QuadraticFunction f, LinearEqualityConstraints equal) {
        this(getQuadraticFunction(f, equal), equal);
    }

    private QPProblemOnlyEqualityConstraints(ModifiedQPProblem problem, LinearEqualityConstraints equal) {
        super(problem.f);
        this.equal = equal;
        this.Aplus = new ImmutableMatrix(problem.Aplus);
        this.Vr = new ImmutableMatrix(problem.Vr);
    }

    private static ModifiedQPProblem getQuadraticFunction(QuadraticFunction f, LinearEqualityConstraints equal) {
        Matrix A = equal.A();
        assertArgument(DimensionCheck.isFat(A), "A in the equality constraints must be a fat matrix");
        int rA = MatrixMeasure.rank(A);
        assertArgument(rA == A.nRows(), "A must has full row rank");

        Matrix Aplus = new PseudoInverse(A);

        /*
         * We need to obtain a square Vr (hence V).
         * To do so, we need to pad 0s to A.
         * This does not change the U and D of the SVD operation.
         */
        Matrix zeroPad = new DenseMatrix(A.nCols() - A.nRows(), A.nCols());
        Matrix Apadded = CreateMatrix.rbind(A, zeroPad);
        SVD svd = new SVD(Apadded, true);
        Matrix Vr = CreateMatrix.columns(svd.V(), A.nRows() + 1, A.nCols());

        Matrix H = f.Hessian();
        Matrix Hhat = new CongruentMatrix(Vr, H);//eq. 13.3b

        Vector p = f.p();
        Vector b = equal.b();
        Matrix p1 = H.multiply(Aplus);
        Vector p2 = p1.multiply(b);
        Vector p3 = p2.add(p);
        Vector phat = Vr.t().multiply(p3);

        QuadraticFunction qf = new QuadraticFunction(Hhat, phat);

        return new ModifiedQPProblem(qf, Aplus, Vr);
    }

    /**
     * Back out the solution for the original (constrained) problem, if the modified (unconstrained) problem can be solved.
     *
     * @param phi the solution to the modified (unconstrained) problem
     * @return the solution to the original (constrained) problem, if the modified (unconstrained) problem can be solved
     * @see "eq. 13.4a"
     */
    public ImmutableVector getSolutionToOriginalProblem(Vector phi) {
        Vector x1 = Vr.multiply(phi);
        Vector x2 = Aplus.multiply(equal.b());
        Vector x = x1.add(x2);// eq. 13.4a
        return new ImmutableVector(x);
    }
}
