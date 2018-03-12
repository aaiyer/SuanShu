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

import com.numericalmethod.suanshu.DeepCopyable;
import com.numericalmethod.suanshu.mathstructure.HilbertSpace;
import com.numericalmethod.suanshu.number.Real;

/**
 * An Euclidean vector is a geometric object that has both a magnitude/length and a direction.
 * The mathematical structure of a collection of vectors is a {@linkplain HilbertSpace Hilbert space}.
 * This interface is made minimal to avoid listing all possible vector operations.
 * Instead, vector operations are grouped into packages and classes by their properties.
 * This is to avoid interface "pollution", lengthy and cumbersome design.
 * <p/>
 * The <em>only</em> way to change a vector is by {@link #set(int, double)}.
 * Other operations that "change" the vector actually creates a new and independent copy.
 *
 * @author Ken Yiu
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Euclidean_vector">Wikipedia: Euclidean vector</a>
 * <li><a href="http://en.wikipedia.org/wiki/Vector_space">Wikipedia: Vector space</a>
 * </ul>
 */
public interface Vector extends HilbertSpace<Vector, Real>, DeepCopyable {

    /**
     * Get the length of this vector.
     *
     * @return the vector length
     */
    public int size();

    /**
     * Get the value at position <i>i</i>.
     *
     * @param i the position of a vector entry
     * @return <i>v[i]</i>
     */
    public double get(int i);

    /**
     * Change the value of an entry in this vector.
     * This is the only method that may change the entries of a vector.
     *
     * @param i     the index of the entry to change. The indices are counting from 1, NOT 0.
     * @param value the value to change to
     */
    public void set(int i, double value);

    /**
     * \(this + that\)
     *
     * @param that a vector
     * @return \(this + that\)
     */
    @Override
    public Vector add(Vector that);

    /**
     * \(this - that\)
     *
     * @param that a vector
     * @return \(this - that\)
     */
    @Override
    public Vector minus(Vector that);

    /**
     * Multiply {@code this} by {@code that}, entry-by-entry.
     *
     * @param that a vector
     * @return \(this \cdot that\)
     */
    public Vector multiply(Vector that);

    /**
     * Divide {@code this} by {@code that}, entry-by-entry.
     *
     * @param that a vector
     * @return \(this / that\)
     */
    public Vector divide(Vector that);

    /**
     * Add a constant to all entries in this vector.
     *
     * @param c a constant
     * @return \(v + c\)
     */
    public Vector add(double c);

    /**
     * Subtract a constant from all entries in this vector.
     *
     * @param c a constant
     * @return \(v - c\)
     */
    public Vector minus(double c);

    /**
     * Inner product in the Euclidean space is the dot product.
     *
     * @param that a vector
     * @return \(this \cdot that\)
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product"> Wikipedia: Dot product</a>
     */
    @Override
    public double innerProduct(Vector that);

    /**
     * Take the exponentiation of all entries in this vector, entry-by-entry.
     *
     * @param c a constant
     * @return \(v ^ s\)
     */
    public Vector pow(double c);

    /**
     * Scale this vector by a constant, entry-by-entry.
     * Here is a way to get a unit version of the vector:
     * <blockquote><code>
     * vector.scaled(1. / vector.norm())
     * </code></blockquote>
     *
     * @param c a constant
     * @return \(c \times this\)
     */
    public Vector scaled(double c);

    /**
     * Scale this vector by a constant, entry-by-entry.
     * Here is a way to get a unit version of the vector:
     * <blockquote><code>
     * vector.scaled(1. / vector.norm())
     * </code></blockquote>
     *
     * @param c a constant
     * @return \(c \times this\)
     */
    @Override
    public Vector scaled(Real c);

    /**
     * Compute the length or magnitude or Euclidean norm of a vector, namely, \(\|v\|\).
     *
     * @return the Euclidean norm
     */
    @Override
    public double norm();

    /**
     * Get the norm of a vector.
     * <ul>
     * <li>When <i>p</i> is finite, \(\|v\|_p = \sum_{i}|v_i^p|^\frac{1}{p}\).
     * <li>When <i>p</i> is \(+\infty\) (Integer.MAX_VALUE), \|v\|_p = \max|v_i|.
     * <li>When <i>p</i> is \(-\infty\) (Integer.MIN_VALUE),\|v\|_p = \min|v_i|.
     * </ul>
     *
     * @param p an integer, Integer.MAX_VALUE, or Integer.MIN_VALUE
     * @return \(\|v\|_p\)
     */
    public double norm(int p);

    /**
     * Measure the angle, \(\theta\), between {@code this} and {@code that}.
     * That is,
     * \[
     * this \cdot that = \|this\| \times \|that\| \times \cos \theta
     * \]
     *
     * @param that a vector
     * @return the angle, \(\theta\), between {@code this} and {@code that}
     */
    @Override
    public double angle(Vector that);

    /**
     * Get the opposite of this vector.
     *
     * @return <i>-v</i>
     */
    @Override
    public Vector opposite();

    /**
     * Get a 0-vector that has the same length as this vector.
     *
     * @return the 0-vector
     */
    @Override
    public Vector ZERO();

    /**
     * Cast this vector into a 1D {@code double[]}.
     *
     * @return a copy of all vector entries as a {@code double[]}
     */
    public double[] toArray();

    @Override
    public Vector deepCopy();//TODO: do we really need this? use ctor?
}
