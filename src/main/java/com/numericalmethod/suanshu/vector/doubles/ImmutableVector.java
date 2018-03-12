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
package com.numericalmethod.suanshu.vector.doubles;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.number.Real;
import com.numericalmethod.suanshu.vector.doubles.IsVector.VectorAccessException;

/**
 * This is a read-only view of a {@link Vector} instance. It keeps the reference to the
 * instance and delegates all operations to the instance except for
 * {@linkplain #set(int, double)} which will always result in an
 * {@link IsVector.VectorAccessException}.
 * It may, however, still be modified indirectly via the reference of the
 * original vector. To ensure complete immutability, an independent copy is needed.
 * That is,
 * <blockquote><code>
 * ImmutableVector immutable = new ImmutableVector(v.deepCopy());
 * </code></blockquote>
 * Note that the returned values of all operations (e.g., {@code add}) has
 * the same types as the original ones.
 *
 * @author Haksun Li
 */
public class ImmutableVector implements Vector {

    private final Vector v;

    /**
     * Construct a read-only version of a vector.
     * <em>Note that, however, changing the original vector changes the "immutable" version as well.</em>
     *
     * @param v a vector
     */
    public ImmutableVector(Vector v) {
        this.v = v;
    }

    /**
     * This method is overridden to always throw {@code VectorAccessException}.
     *
     * @throws VectorAccessException always
     */
    @Override
    public final void set(int i, double value) {//cannot override this again to ensure immutability
        throw new VectorAccessException("this vector is immutable");
    }

    @Override
    public int size() {
        return v.size();
    }

    @Override
    public double get(int index) {
        return v.get(index);
    }

    @Override
    public Vector add(Vector that) {
        return v.add(that);
    }

    @Override
    public Vector minus(Vector that) {
        return v.minus(that);
    }

    @Override
    public Vector multiply(Vector that) {
        return v.multiply(that);
    }

    @Override
    public Vector divide(Vector that) {
        return v.divide(that);
    }

    @Override
    public Vector add(double scalar) {
        return v.add(scalar);
    }

    @Override
    public Vector minus(double scalar) {
        return v.minus(scalar);
    }

    @Override
    public double innerProduct(Vector that) {
        return v.innerProduct(that);
    }

    @Override
    public Vector pow(double scalar) {
        return v.pow(scalar);
    }

    @Override
    public Vector scaled(double scalar) {
        return v.scaled(scalar);
    }

    @Override
    public Vector scaled(Real scalar) {
        return v.scaled(scalar);
    }

    @Override
    public double norm() {
        return v.norm();
    }

    @Override
    public double norm(int p) {
        return v.norm(p);
    }

    @Override
    public double angle(Vector that) {
        return v.angle(that);
    }

    @Override
    public Vector opposite() {
        return v.opposite();
    }

    @Override
    public Vector ZERO() {
        return v.ZERO();
    }

    @Override
    public double[] toArray() {
        return v.toArray();
    }

    @Override
    public ImmutableVector deepCopy() {
        return this;
    }

    @Override
    public String toString() {
        return v != null ? v.toString() : "null";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Vector)) {
            return false;
        }
        final Vector that = (Vector) obj;
        if (!AreMatrices.equal(v, that, 0.)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }
}
