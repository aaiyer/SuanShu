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
package com.numericalmethod.suanshu.analysis.function.rn2r1.univariate;

import com.numericalmethod.suanshu.analysis.function.tuple.OrderedPairs;
import com.numericalmethod.suanshu.analysis.function.tuple.Pair;
import com.numericalmethod.suanshu.analysis.interpolation.DuplicatedAbscissae;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * A step function (or staircase function) is a finite linear combination of indicator functions of intervals.
 * Informally speaking, a step function is a piecewise constant function having only finitely many pieces.
 *
 * <p/>
 * Note: this function is mutable for convenience. It allows dynamically adding more points to the function.
 * It is handy when, e.g., merging two sets of points.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Step_function">Wikipedia: Step function</a>
 */
public class StepFunction extends UnivariateRealFunction implements OrderedPairs {

    private final TreeSet<Pair> pairs;

    /**
     * Construct an empty step function.
     *
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public StepFunction(final double epsilon) {
        this.pairs = new TreeSet<Pair>(
                new Comparator<Pair>() {

                    @Override
                    public int compare(Pair o1, Pair o2) {
                        return DoubleUtils.compare(o1.x, o2.x, epsilon);
                    }
                });
    }

    /**
     * Construct a step function from a collection ordered pairs.
     *
     * @param f       a collection of ordered pairs
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public StepFunction(OrderedPairs f, double epsilon) {
        this(epsilon);
        add(f);
    }

    /**
     * Construct a step function from a collection ordered pairs.
     *
     * @param f a collection of ordered pairs
     */
    public StepFunction(OrderedPairs f) {
        this(f, 0);
    }

    /**
     * Dynamically add points to the step function.
     *
     * @param f a collection of ordered pairs
     */
    public void add(OrderedPairs f) {
        double[] x = f.x();
        double[] y = f.y();

        SuanShuUtils.assertArgument(x.length == y.length, "x and y must have the same length");

        for (int i = 0; i < x.length; ++i) {
            Pair pair = new Pair(x[i], y[i]);
            if (!pairs.add(pair)) {
                throw new DuplicatedAbscissae(String.format("x[%d] are duplicated", i));
            }
        }
    }

    @Override
    public double[] x() {
        double[] x = new double[pairs.size()];

        int i = 0;
        for (Pair pair : pairs) {
            x[i] = pair.x;
            ++i;
        }

        return x;
    }

    @Override
    public double[] y() {
        double[] y = new double[pairs.size()];

        int i = 0;
        for (Pair pair : pairs) {
            y[i] = pair.y;
            ++i;
        }

        return y;
    }

    /**
     * Get the smallest abscissae.
     *
     * @return the smallest abscissae
     */
    public double minDomain() {
        return pairs.first().x;
    }

    /**
     * Get the biggest abscissae.
     *
     * @return the biggest abscissae
     */
    public double maxDomain() {
        return pairs.last().x;
    }

    @Override
    public int size() {
        return pairs.size();
    }

    @Override
    public double evaluate(double x) {
        Pair pair = pairs.floor(new Pair(x, 0));
        if (pair != null) {
            return pair.y;
        }

        throw new RuntimeException(String.format("f(%f) is not defined", x));
    }
}
