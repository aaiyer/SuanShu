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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.activeset;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.number.DoubleUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * These are the utility functions to handle active (equality or inequality) constraints.
 *
 * @author Haksun Li
 */
class WorkingActiveSet {

    /** the row indices of active constraints, Aa */
    private TreeSet<Integer> J = new TreeSet<Integer>();
    /** the active constraints */
    private ImmutableMatrix Aa;
    /** all constraints */
    private final ImmutableMatrix A;

    /**
     * Construct a working set of active constraints.
     *
     * @param A the constraints
     */
    WorkingActiveSet(Matrix A) {
        this.A = new ImmutableMatrix(A);
    }

    /**
     * Add active constraints by indices.
     *
     * @param indices a collection of active constraint indices
     */
    void addAll(Collection<Integer> indices) {
        J.addAll(indices);
        constructActiveConstraints();
    }

    /**
     * Add an active constraint by index.
     *
     * @param j an index of an active constraint
     */
    void add(int j) {
        J.add(j);
        constructActiveConstraints();
    }

    /**
     * Remove an active constraint by index.
     *
     * @param j an index of an active constraint
     */
    void removeByIndex(int j) {
        // there is a one-to-one correspondence between J and mu
        Iterator<Integer> it = J.iterator();
        for (int s = 1; s <= J.size(); ++s) {
            Integer integer = it.next();

            if (s == j) {
                J.remove(integer);
                break;
            }
        }

        constructActiveConstraints();
    }

    /**
     * Get the number of active constraints.
     *
     * @return the number of active constraints
     */
    int size() {
        return J.size();
    }

    /**
     * Check if the set of active constraints contains a certain index.
     *
     * @param j an index of an active constraint
     * @return {@code true} if the set contains {@code j}
     */
    boolean contains(int j) {
        return J.contains(j);
    }

    /**
     * Get a <em>reference</em> to the active constraints.
     *
     * @return the active constraints
     */
    ImmutableMatrix Aa() {
        return Aa;
    }

    private void constructActiveConstraints() {
        int[] rows = DoubleUtils.collection2IntArray(J);
        int[] cols = R.seq(1, A.nCols());
        this.Aa = new ImmutableMatrix(CreateMatrix.subMatrix(A, rows, cols));
    }

    @Override
    public String toString() {
        return this.J.toString();
    }
}
