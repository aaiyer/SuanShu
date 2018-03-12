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
package com.numericalmethod.suanshu.analysis.sequence;

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * Summation is the operation of adding a sequence of numbers; the result is their sum or total.
 * If numbers are added sequentially from left to right, any intermediate result is a partial sum, prefix sum, or running total of the summation.
 * For a finite sequence of such elements, summation always produces a well-defined sum (possibly by virtue of the convention for empty sums).
 * Given the {@link Summation.Term}s, <i>x<sub>i</sub></i>, we have
 * \[
 * S = \sum (x_i)
 * \]
 * If a {@code threshold} is specified, the summation is treated as a convergent series.
 * The summing process stops after \(x_i < \texttt{threshold}\).
 * <p/>
 * Sample usages:
 * <pre><code>
 *      Summation series = new Summation(new Summation.Term() {
 *
 *         public double evaluate(double i) {
 *              return i;
 *          }
 *      });
 *      double sum = series.sum(1, 100);
 * </code></pre>
 * <pre><code>
 *      Summation series = new Summation(new Summation.Term() {
 *
 *          public double evaluate(double i) {
 *              return 1d / i;
 *          }
 *      }, 0.0001);
 *      double sum = series.sumToInfinity(1);
 * </code></pre>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Summation">Wikipedia: Summation</a>
 */
public class Summation {

    /**
     * Define the terms in a summation series.
     */
    public static interface Term {

        /**
         * Evaluate the term for an index.
         *
         * @param i the index
         * @return <i>x<sub>i</sub></i>
         */
        public double evaluate(double i);
    }

    private final Term term;
    private final double threshold;

    /**
     * Construct a summation series.
     * It is assumed that the terms are smaller than a {@code threshold} for all terms after a certain index.
     *
     * @param term      the terms to sum up
     * @param threshold the convergence threshold. When a term falls below it, the summing process stops. For a finite summation, it should be set to 0.
     */
    public Summation(Term term, double threshold) {
        this.term = term;
        this.threshold = threshold;
    }

    /**
     * Construct a <em>finite</em> summation series.
     *
     * @param term the terms to sum up
     */
    public Summation(Term term) {
        this.term = term;
        this.threshold = 0;
    }

    /**
     * Sum up the terms from {@code from} to {@code to} with the increment 1.
     *
     * @param from the starting index
     * @param to   the ending index (inclusive)
     * @return the sum
     */
    public double sum(int from, int to) {
        return sum(from, to, 1);
    }

    /**
     * Sum up the terms from {@code from} to {@code to} with the increment {@code inc}.
     *
     * @param from the starting index
     * @param to   the ending index (inclusive)
     * @param inc  the increment
     * @return the sum
     */
    public double sum(int from, int to, int inc) {
        return sum((double) from, to, inc);
    }

    /**
     * Sum up the terms from {@code from} to {@code to} with the increment {@code inc}.
     *
     * @param from the starting index
     * @param to   the ending index (inclusive)
     * @param inc  the increment
     * @return the sum
     */
    public double sum(double from, double to, double inc) {
        double[] indices = R.seq(from, to, inc);
        return sum(indices);
    }

    /**
     * Partial summation of the selected terms.
     *
     * @param indices the indices to the selected terms
     * @return the sum
     */
    public double sum(double[] indices) {
        double sum = 0;

        for (double i : indices) {//TODO: parallelize this
            double value = term.evaluate(i);
            sum += value;
        }

        return sum;
    }

    /**
     * Sum up the terms from {@code from} to infinity with increment 1 until the series converges.
     *
     * @param from the starting index
     * @return the sum
     */
    public double sumToInfinity(int from) {
        return sumToInfinity(from, 1);
    }

    /**
     * Sum up the terms from {@code from} to infinity with increment {@code inc} until the series converges.
     *
     * @param from the starting index
     * @param inc  the increment
     * @return the sum
     */
    public double sumToInfinity(double from, double inc) {
        SuanShuUtils.assertOrThrow(threshold > 0
                                   ? null
                                   : new RuntimeException("the convergence threshold is 0; summing will not stop"));

        double sum = 0;
        for (double i = from;; i += inc) {//TODO: parallelize this
            double value = term.evaluate(i);
            if (Math.abs(value) < threshold) {//convergence criterion
                break;
            }
            sum += value;
        }

        return sum;
    }
}
