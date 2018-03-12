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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * These are the utility functions for {@link SparseMatrix}.
 *
 * @author Ken Yiu
 */
class SparseMatrixUtils {

    /**
     * Convert a {@link SparseEntry} to a {@link String}.
     *
     * @param A a {@link SparseEntry}
     * @return a {@link String} representation
     */
    public static String toString(SparseMatrix A) {
        List<SparseEntry> elements = A.getEntrytList();
        StringBuilder buffer = new StringBuilder();
        buffer.append(A.nRows()).append("x").append(A.nCols()).
                append(" nnz = ").append(A.nNonZeros()).append("\n");
        for (SparseEntry element : elements) {
            buffer.append("(").append(element.coordinates.i).append(", ").
                    append(element.coordinates.j).append("): ").
                    append(element.value).append("\n");
        }
        return buffer.toString();
    }

    /**
     * Check if two {@link SparseEntry}s are equal.
     *
     * @param A a {@link SparseEntry}
     * @param B a {@link SparseEntry}
     * @return {@code true} if {@code A} and {@code B} are the equal
     */
    public static boolean equals(SparseMatrix A, Object B) {
        if (B == null) {
            return false;
        }
        if (A.getClass() != B.getClass()) {
            return false;
        }
        final SparseMatrix other = (SparseMatrix) B;
        if (A.nRows() != other.nRows()) {
            return false;
        }
        if (A.nCols() != other.nCols()) {
            return false;
        }
        if (A.nNonZeros() != other.nNonZeros()) {
            return false;
        }

        List<SparseEntry> thisElements = A.getEntrytList();
        List<SparseEntry> thatElements = other.getEntrytList();
        Collections.sort(thisElements, SparseEntry.TopLeftFirstComparator.INSTANCE);
        Collections.sort(thatElements, SparseEntry.TopLeftFirstComparator.INSTANCE);
        Iterator<SparseEntry> thisIterator = thisElements.listIterator();
        Iterator<SparseEntry> thatIterator = thatElements.listIterator();
        boolean thisHasNext = thisIterator.hasNext();
        boolean thatHasNext = thatIterator.hasNext();
        while (thisHasNext && thatHasNext) {
            if (!thisIterator.next().equals(thatIterator.next())) {
                return false;
            }
            thisHasNext = thisIterator.hasNext();
            thatHasNext = thatIterator.hasNext();
        }

        if (thisHasNext != thatHasNext) {
            return false;
        }

        return true;
    }
}
