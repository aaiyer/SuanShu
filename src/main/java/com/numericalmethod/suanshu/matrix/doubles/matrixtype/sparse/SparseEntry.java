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

import java.util.Comparator;

/**
 * This is a (non-zero) entry in a sparse matrix.
 * <p/>
 * This class is immutable.
 *
 * @author Ken Yiu
 */
public final class SparseEntry {

    /**
     * This {@code Comparator} sorts the matrix coordinates first from top to
     * bottom (rows), and then from left to right (columns).
     */
    public static enum TopLeftFirstComparator implements Comparator<SparseEntry> {

        INSTANCE; // singleton

        @Override
        public int compare(SparseEntry o1, SparseEntry o2) {
            // compare row
            if (o1.coordinates.i < o2.coordinates.i) {
                return -1;
            }

            if (o1.coordinates.i > o2.coordinates.i) {
                return 1;
            }

            // same row
            if (o1.coordinates.j < o2.coordinates.j) {
                return -1;
            }

            if (o1.coordinates.j > o2.coordinates.j) {
                return 1;
            }

            // same coordinates
            return 0;
        }
    }

    /** the coordinates of this entry */
    public final Coordinates coordinates;
    /** the entry value */
    public final double value;

    /**
     * Construct a sparse entry in a sparse matrix.
     *
     * @param coordinates the entry coordinates
     * @param value       the entry value
     */
    public SparseEntry(Coordinates coordinates, double value) {
        this.coordinates = coordinates;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SparseEntry other = (SparseEntry) obj;
        if (this.coordinates != other.coordinates && (this.coordinates == null || !this.coordinates.equals(other.coordinates))) {
            return false;
        }
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.coordinates != null ? this.coordinates.hashCode() : 0);
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
        return hash;
    }
}
