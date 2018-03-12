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

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import com.numericalmethod.suanshu.number.Real;
import com.numericalmethod.suanshu.vector.doubles.IsVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A sparse vector stores only non-zero values.
 *
 * @author Ken Yiu
 */
public class SparseVector implements
        Vector,
        SparseStructure,
        Iterable<SparseVector.Entry> {

    /**
     * This is an entry in a {@link SparseVector}.
     */
    public static class Entry {

        private int index;
        private double value;

        Entry(int index, double value) {
            this.index = index;
            this.value = value;
        }

        /**
         * Get the index of this entry in the sparse vector, counting from 1.
         *
         * @return the index
         */
        public int index() {
            return index;
        }

        /**
         * Get the value of this entry.
         *
         * @return the value
         */
        public double value() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("[%d] %.4f", index, value);
        }
    }

    /**
     * This wrapper class overrides the {@link java.util.Iterator#remove()}
     * method to throw an exception when called.
     */
    public static class Iterator implements java.util.Iterator<SparseVector.Entry> {

        private final ListIterator<SparseVector.Entry> iterator;

        private Iterator(ListIterator<SparseVector.Entry> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public SparseVector.Entry next() {
            return iterator.next();
        }

        /**
         * Overridden to avoid the vector being altered.
         *
         * @throws UnsupportedOperationException always; set the element to zero
         * instead
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("cannot remove an element in a vector; set it to zero instead");
        }
    }

    private final int size;
    /** Store non-zero entries in sorted order of indices */
    private final LinkedList<Entry> entries;

    /**
     * Construct a sparse vector.
     *
     * @param size the size of the vector
     */
    public SparseVector(int size) {
        this.size = size;
        this.entries = new LinkedList<SparseVector.Entry>();
    }

    /**
     * Construct a sparse vector.
     *
     * @param size    the size of the vector
     * @param indices the indices of the non-zero values
     * @param values  the non-zero values
     */
    public SparseVector(int size, int[] indices, double[] values) {
        // check array sizes
        SuanShuUtils.assertArgument(indices.length == values.length, "sizes of input arrays mismatch");

        this.size = size;

        Entry[] tempEntries = new Entry[values.length];
        for (int i = 0; i < values.length; ++i) {
            // check indices range
            if (indices[i] < 1 || indices[i] > size) {
                throw new IndexOutOfBoundsException("out-of-range index: " + indices[i]);
            }
            tempEntries[i] = new Entry(indices[i], values[i]);
        }
        Arrays.sort(tempEntries, new Comparator<Entry>() {

            @Override
            public int compare(Entry o1, Entry o2) {
                return o1.index < o2.index ? -1 : 1;
            }
        });

        entries = new LinkedList<Entry>();
        int lastIndex = 0;
        for (int i = 0; i < tempEntries.length; ++i) {
            if (lastIndex >= tempEntries[i].index) {
                throw new IllegalArgumentException("duplicated indices: " + tempEntries[i].index);
            }
            lastIndex = tempEntries[i].index;
            entries.add(tempEntries[i]);
        }
    }

    /**
     * Copy constructor.
     *
     * @param that the vector to be copied
     */
    public SparseVector(SparseVector that) {
        this(that.size);

        for (Entry entry : that.entries) { // copy all entries
            this.entries.add(new Entry(entry.index, entry.value));
        }
    }

    private SparseVector(int size, LinkedList<Entry> entries) {
        this.size = size;
        this.entries = entries;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public double get(int index) {
        SuanShuUtils.assertArgument(index >= 1 && index <= size, "out-of-range [1:%d] index: %d", size, index);

        for (Entry entry : entries) {
            if (entry.index == index) {
                return entry.value;
            }
            if (entry.index > index) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public void set(int index, double value) {
        SuanShuUtils.assertArgument(index >= 1 && index <= size, "out-of-range [1:%d] index: %d", size, index);

        for (ListIterator<Entry> it = entries.listIterator(); it.hasNext();) {
            Entry entry = it.next();
            if (entry.index == index) {
                if (Double.compare(0., value) != 0) {
                    entry.value = value;
                } else {
                    it.remove();
                }
                return;
            }
            if (entry.index > index) {
                if (Double.compare(0., value) != 0) {
                    it.previous(); // insert before this element
                    it.add(new Entry(index, value));
                }
                return;
            }
        }
        entries.addLast(new Entry(index, value));
    }

    @Override
    public java.util.Iterator<SparseVector.Entry> iterator() {
        return new Iterator(entries.listIterator());
    }

    @Override
    public Vector add(Vector that) {
        if (that instanceof SparseVector) {
            return this.add((SparseVector) that, +1);
        }

        IsVector.throwIfNotEqualSize(this, that);
        Vector result = new DenseVector(that);
        for (Entry entry : entries) {
            result.set(entry.index, entry.value + result.get(entry.index));
        }
        return result;
    }

    private SparseVector add(SparseVector that, int sign) {
        IsVector.throwIfNotEqualSize(this, that);

        LinkedList<Entry> resultEntries = new LinkedList<Entry>();
        java.util.Iterator<Entry> thisIterator = this.entries.iterator();
        java.util.Iterator<Entry> thatIterator = that.entries.iterator();

        Entry thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
        Entry thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
        while (thisEntry != null || thatEntry != null) {
            if (thisEntry != null && (thatEntry == null || thisEntry.index < thatEntry.index)) {
                resultEntries.add(new Entry(thisEntry.index, thisEntry.value));
                thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
            } else if (thatEntry != null && (thisEntry == null || thisEntry.index > thatEntry.index)) {
                resultEntries.add(new Entry(thatEntry.index, sign * thatEntry.value));
                thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
            } else {
                double sum = thisEntry.value + sign * thatEntry.value;
                if (Double.compare(0., sum) != 0) {
                    resultEntries.add(new Entry(thisEntry.index, sum));
                }
                thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
                thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
            }
        }

        return new SparseVector(size, resultEntries);
    }

    @Override
    public Vector minus(Vector that) {
        if (that instanceof SparseVector) {
            return this.add((SparseVector) that, -1);
        }

        IsVector.throwIfNotEqualSize(this, that);
        Vector result = that.opposite();
        for (Entry entry : entries) {
            result.set(entry.index, entry.value + result.get(entry.index));
        }
        return result;
    }

    @Override
    public SparseVector multiply(Vector v) {
        if (v instanceof SparseVector) {
            return this.multiply((SparseVector) v);
        }

        IsVector.throwIfNotEqualSize(this, v);
        SparseVector result = new SparseVector(this);
        for (Entry entry : result.entries) {
            result.set(entry.index, entry.value * v.get(entry.index));
        }
        return result;
    }

    SparseVector multiply(SparseVector that) {
        IsVector.throwIfNotEqualSize(this, that);

        LinkedList<Entry> resultEntries = new LinkedList<Entry>();
        java.util.Iterator<Entry> thisIterator = this.entries.iterator();
        java.util.Iterator<Entry> thatIterator = that.entries.iterator();

        Entry thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
        Entry thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
        while (thisEntry != null && thatEntry != null) {
            if (thisEntry.index < thatEntry.index) {
                thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
            } else if (thisEntry.index > thatEntry.index) {
                thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
            } else {
                double product = thisEntry.value * thatEntry.value;
                if (Double.compare(0., product) != 0) {
                    resultEntries.add(new Entry(thisEntry.index, product));
                }
                thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
                thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
            }
        }

        return new SparseVector(size, resultEntries);
    }

    @Override
    public Vector divide(Vector that) {
        IsVector.throwIfNotEqualSize(this, that);
        SparseVector result = new SparseVector(this);
        for (Entry entry : result.entries) {
            result.set(entry.index, entry.value / that.get(entry.index));
        }
        return result;
    }

    @Override
    public Vector add(double c) {
        double[] data = R.rep(c, size);
        for (Entry entry : entries) {
            data[entry.index - 1] += entry.value;
        }
        return new DenseVector(data);
    }

    @Override
    public Vector minus(double c) {
        return this.add(-c);
    }

    @Override
    public double innerProduct(Vector that) {
        if (that instanceof SparseVector) {
            return this.innerProduct((SparseVector) that);
        }

        IsVector.throwIfNotEqualSize(this, that);
        double result = 0.;
        for (Entry entry : entries) {
            result += entry.value * that.get(entry.index);
        }
        return result;
    }

    double innerProduct(SparseVector that) {
        IsVector.throwIfNotEqualSize(this, that);

        double sum = 0.;
        java.util.Iterator<Entry> thisIterator = this.entries.iterator();
        java.util.Iterator<Entry> thatIterator = that.entries.iterator();

        Entry thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
        Entry thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
        while (thisEntry != null && thatEntry != null) {
            if (thisEntry.index < thatEntry.index) {
                thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
            } else if (thisEntry.index > thatEntry.index) {
                thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
            } else {
                sum += thisEntry.value * thatEntry.value;
                thisEntry = thisIterator.hasNext() ? thisIterator.next() : null;
                thatEntry = thatIterator.hasNext() ? thatIterator.next() : null;
            }
        }

        return sum;
    }

    @Override
    public Vector pow(double c) {
        double zeroPow = Math.pow(0, c);
        if (Double.compare(0., zeroPow) != 0) { // if the power of zero is non-zero
            double[] data = R.rep(zeroPow, size);
            for (Entry entry : entries) {
                data[entry.index - 1] = Math.pow(entry.value, c);
            }
            return new DenseVector(data);
        }

        // otherwise, all zeros remain zeros
        LinkedList<Entry> resultEntries = new LinkedList<Entry>();
        for (Entry entry : entries) {
            resultEntries.add(new Entry(entry.index, Math.pow(entry.value, c)));
        }
        return new SparseVector(size, resultEntries);
    }

    @Override
    public SparseVector scaled(double c) {
        if (Double.compare(0., c) == 0) {
            return new SparseVector(size);
        }

        LinkedList<Entry> cA = new LinkedList<Entry>();
        for (Entry entry : entries) {
            cA.add(new Entry(entry.index, c * entry.value));
        }

        return new SparseVector(size, cA);
    }

    @Override
    public SparseVector scaled(Real c) {
        return this.scaled(c.doubleValue());
    }

    @Override
    public SparseVector opposite() {
        return this.scaled(-1);
    }

    @Override
    public double norm() {
        return this.norm(2);
    }

    @Override
    public double norm(int p) {
        if (p == Integer.MAX_VALUE) {
            return max(abs(toArray()));
        }

        if (p == Integer.MIN_VALUE) {
            return min(abs(toArray()));
        }

        double result = 0.;
        for (Entry entry : entries) {
            result += Math.pow(entry.value, p);
        }

        result = Math.pow(result, 1. / p);
        return result;
    }

    @Override
    public double angle(Vector that) {
        double result = innerProduct(that);
        result /= this.norm();
        result /= that.norm();
        result = Math.acos(result);
        return result;
    }

    @Override
    public SparseVector ZERO() {
        return new SparseVector(size);
    }

    @Override
    public double[] toArray() {
        double[] result = R.rep(0., size);
        for (Entry entry : entries) {
            result[entry.index - 1] = entry.value;
        }
        return result;
    }

    @Override
    public SparseVector deepCopy() {
        return new SparseVector(this);
    }

    @Override
    public int nNonZeros() {
        return entries.size();
    }

//    public int dropTolerance(double tol) {
//        int nDrops = 0;
//        for (java.util.Iterator<Entry> iterator = entries.iterator(); iterator.hasNext();) {
//            Entry entry = iterator.next();
//            if (Double.compare(Math.abs(entry.value), tol) <= 0) {
//                iterator.remove();
//                nDrops++;
//            }
//        }
//
//        return nDrops;
//    }
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Entry entry : entries) {
            buffer.append(entry.toString()).append("\n");
        }
        return buffer.toString();
    }
}
