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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype;

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.*;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.MatrixMathOperation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation.SimpleMatrixMathOperation;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * Givens rotation is a rotation in the plane spanned by two coordinates axes.
 * That is, left multiplying a vector <i>x</i> by <i>G(i, j, θ)</i>, i.e., <i>G(i, j, θ)x</i>,
 * amounts to rotating <i>x</i> counter-clockwise by radians in the <i>(i ,j)</i> coordinate plane.
 * Its main use is to zero out entries in matrices and vectors.
 * Compared to Householder transformation, Givens rotation can zero out entries more selectively.
 * For example, given a matrix <i>A</i>, we can construct a Givens matrix, <i>G</i>, such that
 * <i>GA</i> has a 0 at an entry <i>GA[i,j]</i> of our choice.
 * Givens matrices are orthogonal.
 * <p/>
 * This implementation of a Givens matrix is <em>immutable</em>.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Givens_rotation">Wikipedia: Givens rotation</a>
 */
public class GivensMatrix implements Matrix {

    private final int dim;
    private final int i;
    private final int j;
    private final double c;
    private final double s;
    private final MatrixMathOperation math = new SimpleMatrixMathOperation();

    /**
     * Construct a Givens matrix in the form
     * \[
     * G(i,j,c,s) = \begin{bmatrix}
     * 1 & ... & 0 & ... & 0 & ... & 0\\
     * ... & & & & & & \\
     * 0 & ... & c & ... & s & ... & 0\\
     * ... & & & & & & \\
     * 0 & ... & -s & ... & c & ... & 0\\
     * ... & & & & & & \\
     * 0 & ... & 0 & ... & 0 & ... & 1
     * \end{bmatrix}
     * \]
     * We have,
     * <blockquote><pre>
     * <i>G[i,i] = c</i> (diagonal entry)
     * <i>G[j,j] = c</i> (diagonal entry)
     * <i>G[i,j] = s</i>
     * <i>G[j,i] = -s</i>
     * </pre></blockquote>
     *
     * @param dim the dimension of <i>G</i>
     * @param i   <i>i</i>
     * @param j   <i>j</i>
     * @param c   <i>c</i>
     * @param s   <i>s</i>
     */
    public GivensMatrix(int dim, int i, int j, double c, double s) {
        SuanShuUtils.assertArgument(0 < i, "incorrect dimension");
        SuanShuUtils.assertArgument(0 < j, "incorrect dimension");
        SuanShuUtils.assertArgument(i <= dim, "incorrect dimension");
        SuanShuUtils.assertArgument(j <= dim, "incorrect dimension");
        SuanShuUtils.assertArgument(i != j, "invalid specification");

        this.dim = dim;
        this.i = i;
        this.j = j;
        this.c = c;
        this.s = s;
    }

    /**
     * Copy constructor.
     *
     * @param that a Givens matrix
     */
    public GivensMatrix(GivensMatrix that) {
        this(that.dim, that.i, that.j, that.c, that.s);
    }

    /**
     * Construct a Givens matrix from ρ.
     * This construction is discussed in the reference.
     *
     * @param dim the dimension of <i>G</i>
     * @param i   <i>i</i>
     * @param j   <i>j</i>
     * @param rho <i>ρ</i>
     * @return a Givens matrix
     * @see "G. H. Golub, C. F. van Loan, "Section 5.1.11," Matrix Computations, 3rd edition."
     */
    public static GivensMatrix CtorFromRho(int dim, int i, int j, double rho) {
        SuanShuUtils.assertArgument(0 < i, "incorrect dimension");
        SuanShuUtils.assertArgument(0 < j, "incorrect dimension");
        SuanShuUtils.assertArgument(i <= dim, "incorrect dimension");
        SuanShuUtils.assertArgument(j <= dim, "incorrect dimension");
        SuanShuUtils.assertArgument(i != j, "invalid specification");

        double cc, s;
        if (equal(rho, 1, 0)) {
            s = 1;
            cc = 0;
        } else if (compare(Math.abs(rho), 1d, 0) < 0) {
            s = 2d * rho;
            cc = Math.sqrt(1d - s * s);
        } else {
            cc = 2d / rho;
            s = Math.sqrt(1d - cc * cc);
        }

        return new GivensMatrix(dim, i, j, cc, s);
    }

    /**
     * Same as {@code new GivensMatrix(2, 1, 2, c, s)}.
     *
     * @param c <i>c</i>
     * @param s <i>s</i>
     * @return a 2x2 Givens matrix
     */
    public static GivensMatrix Ctor2x2(double c, double s) {
        return new GivensMatrix(2, 1, 2, c, s);
    }

    /**
     * Construct a Givens matrix such that <i>G * [a b]<sup>t</sup> = [* 0]<sup>t</sup></i>.
     * This operation rotates rows <i>i<sub>1</sub></i> and <i>i<sub>2</sub></i> to make the entry in row <i>i<sub>1</sub></i> 0.
     * This implementation is a variant of the numerically stable version in the reference.
     *
     * @param dim the dimension of <i>G</i>
     * @param i1  <i>i<sub>1</sub></i> as in <i>A[i<sub>1</sub>,i<sub>1</sub>] = c</i>
     * @param i2  <i>i<sub>2</sub></i> as in <i>A[i<sub>1</sub>,i<sub>2</sub>] = s</i>
     * @param a   <i>a</i> as in <i>[a b]<sup>t</sup></i>
     * @param b   <i>b</i> as in <i>[a b]<sup>t</sup></i>
     * @return <i>G</i>
     * @see "G. W. Stewart, "Algorithm 1.6," Matrix Algorithms Vol. 1"
     */
    public static GivensMatrix CtorToRotateRows(int dim, int i1, int i2, double a, double b) {
        double t = Math.abs(a) + Math.abs(b);

        double cc, s;
        if (isZero(t, 0)) {//TODO: use epsilon?
            cc = 1;
            s = 0;
        } else {
            double v = t * Math.sqrt(a / t * a / t + b / t * b / t);
            cc = a / v;
            s = b / v;
        }

        return new GivensMatrix(dim, i1, i2, cc, s);
    }

    /**
     * Construct a Givens matrix such that <i>G * A</i> has 0 in the <i>[i,j]</i> entry.
     *
     * @param A a matrix
     * @param i <i>i</i> as in <i>A[i,j]</i>
     * @param j <i>j</i> as in <i>A[i,j]</i>
     * @return a Givens matrix
     */
    public static GivensMatrix CtorToZeroOutEntry(Matrix A, int i, int j) {
        double a = A.get(i == 1 ? A.nRows() : i - 1, j);//cycle to the last row
        double b = A.get(i, j);
        return CtorToRotateRows(A.nRows(), i == 1 ? A.nRows() : i - 1, i, a, b);
    }

    /**
     * Construct a Givens matrix such that <i>[a b] * G = [* 0]</i>.
     * This operation rotates columns <i>j<sub>1</sub></i> and <i>j<sub>2</sub></i> to make the entry in column <i>j<sub>2</sub></i> 0.
     *
     * @param dim the dimension of <i>G</i>
     * @param j1  <i>j<sub>1</sub></i> as in <i>A[j<sub>1</sub>,j<sub>1</sub>] = c</i>
     * @param j2  <i>j<sub>2</sub></i> as in <i>A[j<sub>1</sub>,j<sub>2</sub>] = s</i>
     * @param a   <i>a</i> as in <i>[a b]<sup>t</sup></i>
     * @param b   <i>b</i> as in <i>[a b]<sup>t</sup></i>
     * @return a Givens matrix
     */
    public static GivensMatrix CtorToRotateColumns(int dim, int j1, int j2, double a, double b) {
        double t = Math.abs(a) + Math.abs(b);

        double cc, s;
        if (isZero(t, 0)) {//TODO: use epsilon?
            cc = 1;
            s = 0;
        } else {
            double v = t * Math.sqrt(a / t * a / t + b / t * b / t);
            cc = a / v;
            s = b / v;
        }

        return new GivensMatrix(dim, j1, j2, cc, -s);
    }

    /**
     * Construct a Givens matrix such that <i>G<sup>t</sup> * A</i> has 0 in the <i>[i,j]</i> entry.
     *
     * @param A a matrix
     * @param i <i>i</i> as in <i>A[i,j]</i>
     * @param j <i>j</i> as in <i>A[i,j]</i>
     * @return <i>G</i>
     */
    public static GivensMatrix CtorToZeroOutEntryByTranspose(Matrix A, int i, int j) {
        double a = A.get(i == 1 ? A.nRows() : i - 1, j);//cycle to the last row
        double b = A.get(i, j);
        return CtorToRotateColumns(A.nRows(), i == 1 ? A.nRows() : i - 1, i, a, b);
    }

    /**
     * Get ρ as discussed in the reference.
     *
     * @return ρ
     * @see "G. H. Golub, C. F. van Loan, "Section 5.1.11," Matrix Computations, 3rd edition."
     */
    public double rho() {
        double result;

        if (isZero(this.c, 0)) {
            result = 1d;
        } else if (compare(Math.abs(this.s), Math.abs(this.c), 0) < 0) {
            result = Math.signum(this.c) * this.s / 2d;
        } else {
            result = 2d * Math.signum(this.s) / this.c;
        }

        return result;
    }

    /**
     * Rotate <i>x</i> in the <i>[i,j]</i> coordinate plane.
     *
     * @param x a vector
     * @return <i>G(i, j, θ)x</i>
     *
     * @deprecated Not supported yet.
     */
    @Deprecated
    public Vector rotate(Vector x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double get(int i, int j) {
        if (i == this.i && j == this.i) {
            return c;
        } else if (i == this.j && j == this.j) {
            return c;
        } else if (i == this.i && j == this.j) {
            return s;
        } else if (i == this.j && j == this.i) {
            return -s;
        } else if (i == j) {
            return 1;
        }

        return 0;
    }

    @Override
    public Vector getRow(int i) throws MatrixAccessException {
        throwIfInvalidRow(this, i);

        Vector result = new DenseVector(dim, 0);
        result.set(i, this.get(i, i)); // diagonal entry: c or 1
        result.set(this.i, this.get(i, this.i)); // s or -s or 0
        result.set(this.j, this.get(i, this.j)); // s or -s or 0
        return result;
    }

    @Override
    public Vector getColumn(int j) throws MatrixAccessException {
        throwIfInvalidColumn(this, j);

        Vector result = new DenseVector(dim, 0);
        result.set(j, this.get(j, j)); // diagonal entry: c or 1
        result.set(this.i, this.get(this.i, j)); // s or -s or 0
        result.set(this.j, this.get(this.j, j)); // s or -s or 0
        return result;
    }

    @Override
    public GivensMatrix t() {
        return new GivensMatrix(dim, i, j, c, -s);
    }

    /**
     * Left multiplication by <i>G</i>, namely, <i>G * A</i>.
     * This operation affects only the <i>i</i>-th and the <i>j</i>-th rows.
     * This implementation uses this fact for performance.
     *
     * @param A a left multiply matrix
     * @return <i>G * A</i>
     */
    @Override
    public Matrix multiply(Matrix A) {
        throwIfIncompatible4Multiplication(this, A);

        DenseMatrix result = new DenseMatrix(A);
        for (int k = 1; k <= A.nCols(); ++k) {
            double t1 = result.get(i, k);
            double t2 = result.get(j, k);
            result.set(i, k, c * t1 + s * t2);
            result.set(j, k, -s * t1 + c * t2);
        }
        return result;
    }

    @Override
    public Vector multiply(Vector v) {
        throwIfIncompatible4Multiplication(this, v);

        Vector result = v.deepCopy();
        result.set(i, v.get(i) * c + v.get(j) * s);
        result.set(j, v.get(j) * c - v.get(i) * s);
        return result;
    }

    /**
     * Right multiplication by <i>G</i>, namely, <i>A * G</i>.
     * This operation affects only the <i>i</i>-th and the <i>j</i>-th columns.
     * This implementation uses this fact for performance.
     *
     * @param A a right multiply matrix
     * @return <i>A * G</i>
     */
    public Matrix rightMultiply(Matrix A) {
        throwIfIncompatible4Multiplication(A, this);

        DenseMatrix result = new DenseMatrix(A); // copy
        for (int k = 1; k <= A.nRows(); ++k) {
            double t1 = result.get(k, i);
            double t2 = result.get(k, j);
            result.set(k, i, c * t1 - s * t2);
            result.set(k, j, s * t1 + c * t2);
        }
        return result;
    }

    /**
     * Given an array of Givens matrices <i>{G<sub>i</sub>}</i>,
     * compute <i>G</i>, where
     * <blockquote><i>
     * G = G<sub>1</sub> * G<sub>2</sub> * ... * G<sub>n</sub>
     * </i></blockquote>
     *
     * @param Gs an array of Givens matrices
     * @return <i>G</i>
     */
    public static Matrix product(GivensMatrix[] Gs) {
        Matrix result = null;

        for (int i = Gs.length - 1; i >= 0; --i) {
            if (Gs[i] != null) {
                if (result == null) {
                    result = new DenseMatrix(Gs[i].dim, Gs[i].dim).ONE();
                }

                result = Gs[i].multiply(result);
            }
        }

        return result;
    }

    @Override
    public GivensMatrix ONE() {
        return new GivensMatrix(dim, 1, 2, 1, 0);
    }

    @Override
    public String toString() {//TODO: improve the performance
        Matrix A = new DenseMatrix(dim, dim).ONE();

        A.set(i, i, c);
        A.set(j, j, c);
        A.set(i, j, s);
        A.set(j, i, -s);

        return A.toString();
    }

    @Override
    public GivensMatrix deepCopy() {
        return new GivensMatrix(this);
    }

    /**
     * @deprecated GivensMatrix is immutable
     */
    @Deprecated
    @Override
    public void set(int row, int col, double value) {
        throw new MatrixAccessException("GivensMatrix is immutable");
    }

    /**
     * @deprecated no zero matrix for GivensMatrix
     */
    @Deprecated
    public Matrix ZERO() {
        throw new UnsupportedOperationException("no zero matrix for GivensMatrix");
    }

    @Override
    public int nRows() {
        return dim;
    }

    @Override
    public int nCols() {
        return dim;
    }

    @Override
    public Matrix add(Matrix that) {
        return math.add(this, that);
    }

    @Override
    public Matrix minus(Matrix that) {
        return math.minus(this, that);
    }

    @Override
    public Matrix scaled(double c) {
        return math.scaled(this, c);
    }

    @Override
    public Matrix opposite() {
        return scaled(-1);
    }
}
