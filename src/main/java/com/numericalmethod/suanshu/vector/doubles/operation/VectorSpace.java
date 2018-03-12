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
package com.numericalmethod.suanshu.vector.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.qr.HouseholderReflection;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.OLSSolver;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A vector space is a set of vectors that are closed under some operations.
 * The basis of a vector space is a set of vectors that, in a linear combination, can represent
 * every vector in the space, and
 * that no element of the set can be represented as a linear combination of the others.
 * In other words, a basis is a linearly independent spanning set.
 * The orthogonal complement <i>A<sup>⊥</sup></i> of a subspace
 * <i>A</i> of an inner product space <i>V</i> is the set of all vectors in <i>V</i> that are orthogonal to every vector in <i>A</i>.
 * Informally, it is called the perp, short for perpendicular complement.
 * For an <i>m x n</i> matrix <i>A</i>, where <i>m &ge; n</i>,
 * the orthogonal basis are the orthogonalization of the columns.
 * The orthogonal complement is <i>A<sup>⊥</sup> = Null(A.t())</i>.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Basis_(linear_algebra)">Wikipedia: Basis (linear algebra)</a>
 * <li><a href="http://en.wikipedia.org/wiki/Orthogonal_complement">Wikipedia: Orthogonal complement</a>
 * </ul>
 */
public class VectorSpace {//TODO: extend this so that nRows < nCols

    private List<Vector> basis = new ArrayList<Vector>();
    private List<Vector> complement = new ArrayList<Vector>();
    private int rank;
    private final double epsilon;

    /**
     * Construct a vector space from a matrix (a set of column vectors).
     * This implementation computes the orthogonal basis and the orthogonal complement.
     *
     * @param A       a matrix, i.e., (a set of column vectors)
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0.
     * The ε is used to determine the numerical rank of the linear space.
     */
    public VectorSpace(Matrix A, double epsilon) {
        this.epsilon = epsilon;
        run(A);
    }

    /**
     * Construct a vector space from a matrix (a set of column vectors).
     * This implementation computes the orthogonal basis and the orthogonal complement.
     *
     * @param A a matrix, i.e., (a set of column vectors)
     */
    public VectorSpace(Matrix A) {
        this(A, SuanShuUtils.autoEpsilon(MatrixUtils.to1DArray(A)));
    }

    /**
     * Construct a vector space from a list of vectors.
     * This implementation computes the orthogonal basis and the orthogonal complement.
     *
     * @param elements a list of vectors
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0.
     * The ε is used to determine the numerical rank of the linear space.
     */
    public VectorSpace(List<Vector> elements, double epsilon) {
        this(CreateMatrix.cbind(elements), epsilon);
    }

    /**
     * Construct a vector space from a list of vectors.
     * This implementation computes the orthogonal basis and the orthogonal complement.
     *
     * @param elements a list of vectors
     */
    public VectorSpace(List<Vector> elements) {
        this(CreateMatrix.cbind(elements));
    }

    /**
     * Construct a vector space from an array of vectors.
     * This implementation computes the orthogonal basis and the orthogonal complement.
     *
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0.
     * The ε is used to determine the numerical rank of the linear space.
     * @param elements a list of vectors
     */
    public VectorSpace(double epsilon, Vector... elements) {
        this(Arrays.asList(elements), epsilon);
    }

    /**
     * Construct a vector space from an array of vectors.
     * This implementation computes the orthogonal basis and the orthogonal complement.
     *
     * @param elements a list of vectors
     */
    public VectorSpace(Vector... elements) {
        this(Arrays.asList(elements));
    }

    /** Computes the orthogonal basis and the orthogonal complement. */
    private void run(Matrix A) {
        HouseholderReflection hr = new HouseholderReflection(A, epsilon);
        Matrix Q = hr.squareQ();
        rank = hr.rank();

        for (int i = 1; i <= rank; ++i) {
            Vector bb = Q.getColumn(i);
            bb = bb.scaled(1d / bb.norm());//normalization
            basis.add(bb);
        }

        for (int i = rank + 1; i <= Q.nCols(); ++i) {
            Vector bb = Q.getColumn(i);
            bb = bb.scaled(1d / bb.norm());//normalization
            complement.add(bb);
        }
    }

    /**
     * Get the rank of this vector space.
     *
     * @return the rank
     */
    public int rank() {
        return rank;
    }

    /**
     * Get the orthogonal basis.
     *
     * @return the orthogonal basis
     */
    public List<Vector> getBasis() {
        return Collections.unmodifiableList(basis);
    }

    /**
     * Get the basis of the orthogonal complement.
     *
     * @return the orthogonal basis of the nullspace
     */
    public List<Vector> getComplement() {
        return Collections.unmodifiableList(complement);
    }

    /**
     * Get the linear span of the orthogonal basis from a set of coefficients.
     *
     * @param d coefficients
     * @return a span of the basis
     * @deprecated Not supported yet.
     */
    @Deprecated
    public Vector getLinearSpan(double... d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Find a linear combination of the basis that best approximates a vector in the least square sense.
     * This implementation solves an OLS problem.
     *
     * @param b a vector
     * @return the coefficients for a linear combination of basis; {@code null} if {@code b} is not in the vector space
     * @see OLSSolver
     */
    public Vector getSpanningCoefficients(Vector b) {
        Vector result = null;

        try {
            Matrix B = CreateMatrix.cbind(basis);//TODO: cache B

            OLSSolver ols = new OLSSolver(epsilon);
            result = ols.solve(new LSProblem(B, b));

            //check if the coefficients are valid
            Vector b_hat = B.multiply(result);
            if (!AreMatrices.equal(b_hat, b, epsilon)) {
                result = null;
            }
        } catch (Exception ex) {
            result = null;
        }

        return result;
    }

    /**
     * Check whether a vector is in the span of the basis.
     * That is, whether there exists a linear combination of the basis that equals the vector.
     *
     * @param b a vector
     * @return {@code true} if {@code b} is in this vector space
     */
    public boolean isSpanned(Vector b) {
        Vector x = getSpanningCoefficients(b);
        return (x != null);
    }

    /**
     * Check whether a vector is in the span of the the kernel/nullspace.
     * That is, whether there exists a linear combination of the basis of the kernel that equals the vector.
     *
     * @param b a vector
     * @return {@code true} if {@code b} is in the kernel/nullspace of this vector space
     * @deprecated Not supported yet.
     */
    @Deprecated
    public boolean isInKernel(Vector b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
