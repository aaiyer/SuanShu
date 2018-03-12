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
package com.numericalmethod.suanshu.matrix.doubles;

import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a read-only view of a {@link Matrix} instance.
 * It keeps a reference to the matrix instance,
 * delegates all operations to the instance except for {@linkplain #set(int, int, double)} which will always result in an {@link MatrixAccessException}.
 * Note that the returned values of all operations (e.g., {@code add()}) have the same types as the original matrix.
 * Popular usages of this class include, e.g., the {@code final} member of a class, the return value of a method.
 * <p/>
 * Note that the 'immutability' can be broken if the original matrix is modified.
 * To avoid this, make a copy of the original matrix before passing it to the constructor. For example,
 * <blockquote<code>
 * ImmutableMatrix immutable = new ImmutableMatrix(m.deepCopy());
 * </code></blockquote>
 *
 * @author Haksun Li
 */
public class ImmutableMatrix implements Matrix {

    private final Matrix A;//the referenced matrix

    /**
     * Construct a read-only version of a matrix.
     * <em>Note that changing the original matrix changes the "immutable" version as well</em>.
     *
     * @param A a matrix
     */
    public ImmutableMatrix(Matrix A) {
        this.A = A;
    }

    @Override
    public void set(int row, int col, double value) throws MatrixAccessException {
        throw new MatrixAccessException("this matrix is immutable");
    }

    @Override
    public int nRows() {
        return A.nRows();
    }

    @Override
    public int nCols() {
        return A.nCols();
    }

    @Override
    public double get(int i, int j) throws MatrixAccessException {
        return A.get(i, j);
    }

    @Override
    public Vector getRow(int i) throws MatrixAccessException {
        return A.getRow(i);
    }

    @Override
    public Vector getColumn(int j) throws MatrixAccessException {
        return A.getColumn(j);
    }

    @Override
    public Matrix add(Matrix that) {
        return A.add(that);
    }

    @Override
    public Matrix minus(Matrix that) {
        return A.minus(that);
    }

    @Override
    public Matrix multiply(Matrix that) {
        return A.multiply(that);
    }

    @Override
    public Vector multiply(Vector v) {
        return A.multiply(v);
    }

    @Override
    public Matrix scaled(double c) {
        return A.scaled(c);
    }

    @Override
    public Matrix opposite() {
        return A.opposite();
    }

    @Override
    public Matrix ZERO() {
        return A.ZERO();
    }

    @Override
    public Matrix ONE() {
        return A.ONE();
    }

    @Override
    public Matrix t() {
        return A.t();
    }

    /**
     * Make a deep copy of the underlying matrix.
     * <em>The copy is no longer an {@code ImmutableMatrix} and may be mutable.</em>
     *
     * @return a deep copy of the underlying matrix
     */
    @Override
    public Matrix deepCopy() {
        if (A instanceof ImmutableMatrix) {
            return ((ImmutableMatrix) A).A.deepCopy();
        }

        return A.deepCopy();
    }

    @Override
    public String toString() {
        return A.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Matrix.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Matrix other = (Matrix) obj;
        if (!AreMatrices.equal(A, other, 0)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.A != null ? this.A.hashCode() : 0);
        return hash;
    }
}
