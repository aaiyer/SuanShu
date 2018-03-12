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

import com.numericalmethod.suanshu.DeepCopyable;
import com.numericalmethod.suanshu.datastructure.Table;
import com.numericalmethod.suanshu.mathstructure.Ring;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface defines a {@code Matrix} as a {@link Ring}, a {@link Table}, and a few more methods not already defined in its mathematical definition.
 * The interface is made minimal to avoid listing all possible matrix operations.
 * Instead, matrix operations are grouped into packages and classes by their properties.
 * This is to avoid interface "pollution", lengthy and cumbersome design.
 *
 * @author Ken Yiu
 */
public interface Matrix extends MatrixTable, MatrixRing, DeepCopyable {

    /**
     * Right multiply this matrix, <i>A</i>, by a vector.
     *
     * @param v a vector
     * @return <i>Av</i>, a vector
     */
    public Vector multiply(Vector v);

    /**
     * Scale this matrix, <i>A</i>, by a constant.
     *
     * @param c a double
     * @return <i>cA</i>
     */
    public Matrix scaled(double c);

    /**
     * Get the specified row in the matrix as a vector.
     *
     * @param i the row index
     * @return the vector <i>A[i, ]</i>
     * @throws MatrixAccessException when <i>i</i> &lt; 1, or when <i>i</i> &gt; the number of rows
     */
    public Vector getRow(int i) throws MatrixAccessException;//TODO: extend to get a sub row efficiently

    /**
     * Get the specified column in the matrix as a vector.
     *
     * @param j the column index
     * @return a vector <i>A[, j]</i>
     * @throws MatrixAccessException when <i>j</i> &lt; 1, or when <i>j</i> &gt; the number of columns
     */
    public Vector getColumn(int j) throws MatrixAccessException;//TODO: extend to get a sub column efficiently

    @Override
    public Matrix deepCopy();//override the return type
}
