/*
 * Copyright (c) Numerical Method Inc.
 * http://www.numericalmethod.com/ * Copyright (c)
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

import com.numericalmethod.suanshu.mathstructure.Ring;

/**
 * A matrix ring is the set of all <i>n × n</i> matrices over an arbitrary {@link Ring} <i>R</i>.
 * This matrix set becomes a ring under matrix addition and multiplication.
 * Moreover, it has a structure of a *-algebra over <i>R</i>, where the involution * on the matrix ring is the matrix transposition.
 *
 * @author Haksun Li
 */
public interface MatrixRing extends Ring<Matrix> {

    /**
     * Get the transpose of this matrix.
     * This is the involution on the matrix ring.
     *
     * @return the transpose of this matrix
     */
    public Matrix t();

    /**
     * <i>this + that</i>
     *
     * @param that a matrix
     * @return the sum of {@code this} and {@code that}
     */
    @Override
    public Matrix add(Matrix that);

    /**
     * <i>this - that</i>
     *
     * @param that a matrix
     * @return the difference between {@code this} and {@code that}
     */
    @Override
    public Matrix minus(Matrix that);

    /**
     * <i>this * that</i>
     *
     * @param that a matrix
     * @return the product of{@code this} and {@code that}
     */
    @Override
    public Matrix multiply(Matrix that);

    /**
     * Get the opposite of this matrix.
     *
     * @return <i>-this</i>
     */
    @Override
    public Matrix opposite();

    /**
     * Get a zero matrix that has the same dimension as this matrix.
     *
     * @return the 0 matrix
     */
    @Override
    public Matrix ZERO();

    /**
     * Get an identity matrix that has the same dimension as this matrix.
     * For a non-square matrix, it zeros out the rows (columns) with index &gt; nCols (nRows).
     *
     * @return an identity matrix
     */
    @Override
    public Matrix ONE();
}
