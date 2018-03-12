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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.interval.RealInterval;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.BoxConstraints.Bound;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This represents the lower and upper bounds for a variable.
 *
 * <blockquote><code><pre>
 * lb ≤ x ≤ ub
 * </blockquote></code></pre>
 *
 * @author Haksun Li
 */
public class BoxConstraints {

    /**
     * a bound constraint for a variable
     */
    public static class Bound {

        /**
         * the index to the variable, counting from 1
         */
        public final int index;
        private RealInterval interval;

        /**
         * Construct a bound constraint for a variable.
         *
         * @param index the index to the variable, counting from 1
         * @param lower the lower bound for the variable
         * @param upper the upper bound for the variable
         */
        public Bound(int index, double lower, double upper) {
            this.index = index;
            this.interval = new RealInterval(lower, upper);
        }

        public double lower() {
            return interval.begin();
        }

        public double upper() {
            return interval.end();
        }

        @Override
        public String toString() {
            return String.format("x[%d]: %s", index, interval.toString());
        }
    }
    private final int dim;
    private final ArrayList<Bound> bounds;

    /**
     * Construct a set of bound constraints.
     * 
     * @param bounds the bounds
     * @param dim the number of variables
     */
    public BoxConstraints(int dim, Bound... bounds) {
        this.dim = dim;
        this.bounds = new ArrayList<Bound>(bounds.length);
        this.bounds.addAll(Arrays.asList(bounds));
    }

    /**
     * Get a deep copy of the bounds.
     *
     * @return a deep copy of the bounds
     */
    public ArrayList<Bound> getBounds() {
        ArrayList<Bound> copy = new ArrayList<Bound>(bounds.size());
        copy.addAll(bounds);

        return copy;
    }

    /**
     * Split the equality constraints and get the less-than-the-upper-bounds part.
     * 
     * @return the upper bound constraints
     */
    public LinearLessThanConstraints getUpperBounds() {
        ArrayList<Vector> rows = new ArrayList<Vector>();
        ArrayList<Double> bs = new ArrayList<Double>();

        for (Bound bound : bounds) {
            Double upper = bound.upper();
            if (upper == Double.POSITIVE_INFINITY) {//not really a constraint
                continue;
            }
            bs.add(upper);

            Vector v = new DenseVector(dim, 0.0);
            v.set(bound.index, 1);
            rows.add(v);
        }

        if (rows.isEmpty()) {
            return null;
        }

        Matrix A = CreateMatrix.rbind(rows.toArray(new Vector[0]));
        Vector b = new DenseVector(DoubleUtils.collection2DoubleArray(bs));

        return new LinearLessThanConstraints(A, b);
    }

    /**
     * Split the equality constraints and get the greater-than-the-lower-bounds part.
     *
     * @return the lower bound constraints
     */
    public LinearGreaterThanConstraints getLowerBounds() {
        ArrayList<Vector> rows = new ArrayList<Vector>();
        ArrayList<Double> bs = new ArrayList<Double>();

        for (Bound bound : bounds) {
            Double lower = bound.lower();
            if (lower == Double.NEGATIVE_INFINITY//not really a constraint
                    || lower == 0) {//not a 'free' variable
                continue;
            }
            bs.add(lower);

            Vector v = new DenseVector(dim, 0.0);
            v.set(bound.index, 1);
            rows.add(v);
        }

        if (rows.isEmpty()) {
            return null;
        }

        Matrix A = CreateMatrix.rbind(rows.toArray(new Vector[0]));
        Vector b = new DenseVector(DoubleUtils.collection2DoubleArray(bs));

        return new LinearGreaterThanConstraints(A, b);
    }

    public LinearLessThanConstraints toLessThanConstraints() {
        LinearGreaterThanConstraints greater = getLowerBounds();

        LinearLessThanConstraints less1 = getUpperBounds();
        LinearLessThanConstraints less2 = greater.toLessThanConstraints();

        LinearLessThanConstraints less3 = (LinearLessThanConstraints) LinearConstraints.concat(less1, less2);
        return less3;
    }

    public LinearGreaterThanConstraints toGreaterThanConstraints() {
        LinearLessThanConstraints less = toLessThanConstraints();
        LinearGreaterThanConstraints greater = less.toGreaterThanConstraints();
        return greater;
    }
}
