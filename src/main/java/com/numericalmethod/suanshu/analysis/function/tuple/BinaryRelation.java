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
package com.numericalmethod.suanshu.analysis.function.tuple;

import com.numericalmethod.suanshu.analysis.interpolation.DuplicatedAbscissae;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * A binary relation on a set A is a collection of ordered pairs of elements in A.
 * In other words, it is a subset of the Cartesian product A<sup>2</sup> = A × A.
 * <p/>
 * This class is immutable.
 *
 * @author Haksun Li
 */
public class BinaryRelation extends TreeSet<Pair> implements OrderedPairs {

    private static final long serialVersionUID = 1L;

    /**
     * Construct a binary relation from {(x,y)}.
     *
     * @param x elements in A
     * @param y elements in A
     */
    public BinaryRelation(double[] x, double[] y) {
        super(
                new Comparator<Pair>() {

                    @Override
                    public int compare(Pair o1, Pair o2) {
                        return DoubleUtils.compare(o1.x, o2.x, 0);
                    }
                });

        SuanShuUtils.assertArgument(x.length == y.length, "x and y must have the same length");

        for (int i = 0; i < x.length; ++i) {
            Pair pair = new Pair(x[i], y[i]);
            if (!add(pair)) {
                throw new DuplicatedAbscissae(String.format("x[%d] are duplicated", i));
            }
        }

        //check for non-consecutive duplications in the inputs,
        //which are sorted in the 'TreeSet' after 'add'
        double[] z = x();
        for (int i = 1; i < z.length; ++i) {
            if (z[i - 1] == z[i]) {
                throw new DuplicatedAbscissae(String.format("x[%d] and x[%d] are duplicated", i, i + 1));
            }
        }
    }

    @Override
    public double[] x() {
        double[] x = new double[this.size()];

        int i = 0;
        for (Pair pair : this) {
            x[i] = pair.x;
            ++i;
        }

        return x;
    }

    @Override
    public double[] y() {
        double[] y = new double[this.size()];

        int i = 0;
        for (Pair pair : this) {
            y[i] = pair.y;
            ++i;
        }

        return y;
    }
}
