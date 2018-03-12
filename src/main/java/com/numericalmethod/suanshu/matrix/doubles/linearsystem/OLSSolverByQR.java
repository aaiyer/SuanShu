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

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.qr.HouseholderReflection;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;

/**
 * This class solves an over-determined system of linear equations in the
 * ordinary least square sense.
 * An over-determined system, represented by
 * <blockquote><i>Ax = y</i></blockquote>
 * has more rows than columns. That is, there are more equations than unknowns.
 * One important application is linear regression, where <i>A</i> is the
 * independent factors, <i>y</i> the dependent observations.
 * The solution <i>x^</i> minimizes:
 * <blockquote><i>|Ax - y|<sub>2</sub></i></blockquote>
 * That is, <i>x^</i> is the best approximation that
 * minimizes the sum of squared differences between the data values and their
 * corresponding modeled values.
 * The approach is called "linear" least squares since the solution depends
 * linearly on the data.
 * <blockquote><i>
 * <i>x^</i> = (A<sup>t</sup>A)<sup>-1</sup>A<sup>t</sup>y,
 * </i></blockquote>
 * This implementation does not use the above formula to solve for <i>x^</i>
 * because of the numerical stability problem in computing
 * <i>A<sup>t</sup>A</i>.
 * Instead, we use QR decomposition, an orthogonal decomposition method that is
 * numerically stable.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Linear_least_squares">Wikipedia:
 * Linear least squares</a>
 * <li><a href="http://en.wikipedia.org/wiki/Least_squares">Wikipedia: Least
 * squares</a>
 * <li><a
 * href="http://en.wikipedia.org/wiki/Linear_least_squares#Orthogonal_decomposition_methods">Wikipedia:
 * Orthogonal decomposition methods</a>
 * </ul>
 */
public class OLSSolverByQR {//TODO: finish up this class; add javadoc, etc.

    private final double epsilon;

    /**
     * Construct an OLS solver for an over-determined system of linear
     * equations.
     *
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is
     * considered 0
     */
    public OLSSolverByQR(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * In the ordinary least square sense, solve
     * <blockquote><i>Ax = y</i></blockquote>
     *
     * @param problem a system of linear equations
     * @return a solution <i>x^</i> that minimizes
     * <blockquote><i>|Ax - y|<sub>2</sub></i></blockquote>
     */
    public Vector solve(LSProblem problem) {
        final ImmutableMatrix A = problem.A();
        final ImmutableVector b = problem.b();

        HouseholderReflection hs = new HouseholderReflection(A, epsilon);
        Matrix Qt = hs.squareQ().t();
        UpperTriangularMatrix R = hs.R();

        Vector b1 = CreateVector.subVector(Qt.multiply(b), 1, R.nRows());//truncate b
        BackwardSubstitution solver = new BackwardSubstitution();
        Vector x = solver.solve(R, b1);

        return x;
    }
}
