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

import com.numericalmethod.suanshu.datastructure.Table;
import com.numericalmethod.suanshu.matrix.MatrixAccessException;

/**
 * This interface defines the methods for accessing entries in a matrix.
 * Indices count from 1, e.g., {@code get(1,1)}.
 * This is what mathematicians (not programmers) are accustomed to.
 * Invalid access such as using out-of-range indices or altering immutable matrix
 * will lead to {@link MatrixAccessException}.
 * The <em>only</em> way to change a matrix is by calling {@link #set(int, int, double)}.
 * Other operations that "change" the matrix actually creates a new and independent copy.
 *
 * @author Haksun Li
 */
public interface MatrixAccess extends Table {//TODO: extends Cloneable, java.io.Serializable {

    /**
     * Set the matrix entry at <i>[i,j]</i> to a value.
     * This is the only method that may change a matrix.
     *
     * @param i     the row index
     * @param j     the column index
     * @param value the value to set <i>A[i,j]</i> to
     * @throws MatrixAccessException if <i>i</i> or <i>j</i> is out of range
     */
    public void set(int i, int j, double value) throws MatrixAccessException;

    /**
     * Get the matrix entry at <i>[i,j]</i>.
     *
     * @param i the row index
     * @param j the column index
     * @return <i>A[i,j]</i>
     * @throws MatrixAccessException if <i>i</i> or <i>j</i> is out of range
     */
    public double get(int i, int j) throws MatrixAccessException;
}
