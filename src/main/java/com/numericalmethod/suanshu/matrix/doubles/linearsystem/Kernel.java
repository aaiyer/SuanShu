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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.gaussianelimination.GaussJordanElimination;
import com.numericalmethod.suanshu.matrix.doubles.factorization.qr.HouseholderReflection;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.*;

/**
 * The kernel or null space (also nullspace) of a matrix <i>A</i> is the set of all vectors <i>x</i> for which <i>Ax = 0</i>.
 * The kernel of a matrix with <i>n</i> columns is a linear subspace of n-dimensional Euclidean space.
 * Also, the kernel of a matrix <i>A</i> is exactly the same as the kernel of the linear mapping defined by the matrix-vector multiplication, <i>x → Ax</i>.
 * That is, the set of vectors that map to the zero vector.
 * The rank-nullity theorem says that the rank of <i>A</i> + the dimension of the kernel of <i>A</i> = the number of columns in <i>A</i>.
 * <p/>
 * The kernel is the solution of a system of homogeneous linear equations.
 * With the transformation matrix <i>T</i>, which turns <i>A</i> into the reduced row echelon form,
 * it can solve also a system of non-homogeneous linear equations.
 * Specifically, to find a particular solution for a non-homogeneous system of linear equations with a matrix <i>A</i>, i.e.,
 * <blockquote><i>Ax = b</blockquote></i>
 * we solve
 * <blockquote><i>T * A = U</i></blockquote>
 * and then
 * <blockquote><i>x = T * b</i></blockquote>
 * where
 * <i>x</i> is a particular solution.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Kernel_(matrix)#Nonhomogeneous_equations">Wikipedia: Kernel (matrix)</a>
 * <li><a href="http://en.wikipedia.org/wiki/Rank-nullity_theorem">Wikipedia: Rank–nullity theorem</a>
 * </ul>
 */
public class Kernel {

    private Matrix U;
    private Matrix T;
    private Map<Integer, Vector> basis;//the basis for A's kernel
    private final int nRows;//the number of rows
    private final int nCols;//the number of columns
    private final Method method;//the method to perform the LU decomposition
    private final double epsilon;

    /**
     * the methods available to compute kernel basis.
     * Note that the matrices <i>T</i> and <i>U</i> are always computed using Gauss-Jordan elimination.
     */
    public static enum Method {

        /**
         * use the Singular Value Decomposition method; very expensive
         * TODO:
         */
//        SVD,
        /**
         * use QR decomposition; moderately expensive (recommended)
         */
        QR,
        /**
         * use Gauss-Jordan elimination; cheap but subject to numerical stability (rounding errors)
         */
        GAUSSIAN_JORDAN_ELIMINATION
    }

    /**
     * Construct the kernel of a matrix.
     *
     * @param A       a matrix
     * @param method  the kernel computation method
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0;
     * the ε is used to determine the numerical rank of the linear space
     */
    public Kernel(Matrix A, Method method, double epsilon) {
        SuanShuUtils.assertArgument(A.nCols() >= A.nRows(), "cannot compute for an over-determined system");

        this.nRows = A.nRows();
        this.nCols = A.nCols();
        this.method = method;
        this.epsilon = epsilon;

        //Use elementary row operations to put A in reduced row echelon form.
        GaussJordanElimination ge = new GaussJordanElimination(A, true, epsilon);
        this.U = ge.U();
        this.T = ge.T();

        //find the kernel basis
        switch (this.method) {
            case GAUSSIAN_JORDAN_ELIMINATION:
                this.basis = elimination();
                break;
//            case SVD:
//                throw new UnsupportedOperationException("Not supported yet.");
//                break;
            case QR:
            default:
                this.basis = getOrthogonalComplement(A);
                break;
        }
    }

    /**
     * Construct the kernel of a matrix.
     *
     * @param A a matrix
     */
    public Kernel(Matrix A) {
        this(A, Method.QR, SuanShuUtils.autoEpsilon(A));
    }

    /**
     * Get the nullity of <i>A</i>.
     * That is, the dimension of the kernel.
     *
     * @return the nullity
     */
    public int nullity() {
        return basis.size();
    }

    /**
     * Get the rank of <i>A</i>.
     * That is, the number of linearly independent columns or rows in <i>A</i>.
     *
     * @return the rank
     */
    public int rank() {
        return nCols - basis.size();
    }

    /**
     * Get the kernel basis and the associated free variables for each basis/column.
     * This method is only meaningful when the kernel is computed using {@link Method#GAUSSIAN_JORDAN_ELIMINATION}.
     * Using this process, a free variable corresponds to an entry '1' in a column.
     * For the example in Wikipedia,
     * <i>x<sub>3</sub></i> (the 3rd variable) is a free variable, and it corresponds to <i>[3,-5,1,0,0,0]<sup>t</sup></i>.
     * <p/>
     * If the kernel basis is computed using a different method, call {@link #basis()} instead.
     *
     * @return a map of indices and kernel basis vectors
     * @see <a href="http://en.wikipedia.org/wiki/Kernel_(matrix)#Basis">Wikipedia: Basis</a>
     */
    public Map<Integer, Vector> basisAndFreeVars() {
        Map<Integer, Vector> copy = new HashMap<Integer, Vector>();
        Set<Integer> freeVars = basis.keySet();
        for (Integer idx : freeVars) {
            Vector v = basis.get(idx).deepCopy();
            copy.put(idx, v);
        }

        return copy;
    }

    /**
     * Get the kernel basis.
     *
     * @return the kernel basis
     */
    public List<Vector> basis() {
        List<Vector> copy = new ArrayList<Vector>();
        for (Map.Entry<Integer, Vector> entry : basis.entrySet()) {
            Vector v = entry.getValue().deepCopy();
            copy.add(v);
        }

        return copy;
    }

    /**
     * Check if the kernel has zero dimension, that is, if <i>A</i> has full rank.
     *
     * @return {@code true} if the kernel is null
     */
    public boolean isZero() {
        return (basis.isEmpty());
    }

    /**
     * Get the transformation matrix, <i>T</i>, such that <i>T * A = U</i>.
     * To find a particular solution for a non-homogeneous system of linear equations <i>Ax = b</i>, we do
     * <blockquote><i>x = T * b</i></blockquote>
     * where <i>x</i> is a particular solution.
     *
     * @return the transformation matrix <i>T</i>
     */
    public Matrix T() {
        return T.deepCopy();
    }

    /**
     * Get the upper triangular matrix <i>U</i>, such that <i>T * A = U</i>.
     *
     * @return the <i>U</i> matrix
     */
    public Matrix U() {
        return U.deepCopy();
    }

    /**
     * @see <a href="http://en.wikipedia.org/wiki/Kernel_(matrix)#Basis">Wikipedia: Basis</a>
     */
    private Map<Integer, Vector> elimination() {
        Map<Integer, Vector> basis = new HashMap<Integer, Vector>();
        /*
         * Interpret the reduced row echelon form as a homogeneous linear system;
         * determine which of the variables x<sub>1</sub>, x<sub>2</sub>, ..., x<sub>n</sub> are free.
         */
        for (int i = 1; i <= nCols; ++i) {//each column corresponds to a variable/factor
            basis.put(new Integer(i), null);
        }

        Map<Integer, Integer> location = new HashMap<Integer, Integer>();
        for (int i = 1; i <= nRows; ++i) {//each row corresponds to a linear equation
            for (int j = i; j <= nCols; ++j) {//search for the leading 1
                if (U.get(i, j) == 1) {//this assumes that the leading 1s in <i>U</i> are exactly 1, as guaranteed by the implementation
                    basis.remove(new Integer(j));//j is not a free variable
                    //i is its location in the matrix
                    location.put(new Integer(j), new Integer(i));
                    break;
                }
            }
        }

        /*
         * For each free variable x<sub>i</sub>,
         * choose the vector in the kernel for which <i>x<sub>i</sub> = 1</i>
         * and the remaining free variables are zero.
         */
        Set<Integer> freeVars = basis.keySet();
        for (Integer idx : freeVars) {
            Vector v = new DenseVector(nCols);//length = number of variables/factors
            v.set(idx, 1);//xi = 1

            //the other bounded variables
            Set<Integer> boundedVars = location.keySet();
            for (Integer var : boundedVars) {
                int varLocation = location.get(var).intValue();
                double value = U.get(varLocation, idx);
                v.set(var, -value);//* -1
            }

            basis.put(idx, v);
        }

        return basis;
    }

    /**
     * <pre><i>
     * A.t() = QR = [Q1 Q2] * [ R1 ]
     *                        [ 0  ]
     * </i></pre>
     * <i>Q2</i> is the orthogonal complement of <i>A</i>, which is also the kernel.
     *
     * @param A the matrix to find the orthogonal complement for
     */
    private Map<Integer, Vector> getOrthogonalComplement(Matrix A) {
        Map<Integer, Vector> basis = new HashMap<Integer, Vector>();

        Matrix At = A.t();
        HouseholderReflection hr = new HouseholderReflection(At, epsilon);
        Matrix Q = hr.squareQ();
        int rank = hr.rank();

        for (int i = rank + 1; i <= Q.nCols(); ++i) {
            Vector bb = Q.getColumn(i);
            bb = bb.scaled(1d / bb.norm());//normalization
            basis.put(new Integer(i - rank), bb);
        }

        return basis;
    }
}
