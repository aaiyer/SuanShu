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
package com.numericalmethod.suanshu.stats.descriptive.rank;

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.shellsort;
import static java.lang.Math.abs;
import java.util.Arrays;

/**
 * Rank is a relationship between a set of items such that,
 * for any two items, the first is either "ranked higher than", "ranked lower than" or "ranked equal to" the second.
 * This is known as a weak order or total preorder of objects.
 * It is not necessarily a total order of objects because two different objects can have the same ranking.
 * The rankings themselves are totally ordered.
 * In statistics, "ranking" refers to the data transformation in which numerical or ordinal values are replaced by their rank when the data are sorted.
 * It is important to note that ranks can sometimes have non-integer values for tied data values.
 * Thus, in one way of treating tied data values, when there is an even number of copies of the same data value,
 * the statistical rank (being the median rank of the tied data) can end in ½ or another fraction.
 * <p/>
 * The R equivalent function is {@code rank}.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Ranking">Wikipedia: Ranking</a>
 * <li><a href="http://en.wikipedia.org/wiki/Ranking#Ranking_in_statistics">Wikipedia: Ranking in statistics</a>
 * <li>"Algorithm AS 26: Ranking an Array of Numbers. P. R. Freeman. Journal of the Royal Statistical Society. Series C (Applied Statistics) Vol. 19, No. 1 (1970), p. 111-113."
 * <li>"Remark AS R7: A Remark on Algorithm AS 26: Ranking an Array of Numbers. P. R. Freeman. Journal of the Royal Statistical Society. Series C (Applied Statistics), Vol. 22, No. 1 (1973), p. 133."
 * </ul>
 */
public class Rank {

    /**
     * t = Σ(t<sub>i</sub><sup>3</sup> - t<sub>i</sub>)
     */
    private final double t;
    /**
     * s = Σ(t<sub>i</sub><sup>2</sup> - t<sub>i</sub>)
     */
    private final double s;
    /**
     * the result
     */
    private final double[] rank;

    /**
     * Compute the sample ranks of the values.
     *
     * @param values    the values
     * @param threshold the tie threshold.
     * If successive elements of the sorted array differ by less than the threshold, they are treated as equal.
     * We count the number of ties in each group.
     */
    public Rank(double[] values, double threshold) {
        double tt = 0;
        double ss = 0;

        final int n = values.length;
        double[] copy = Arrays.copyOf(values, n);
        int[] order = shellsort(copy);

        double[] sa = new double[n];
        sa[n - 1] = n - 1;
        for (int i = 0; i < n - 1; ++i) {
            if (abs(copy[i] - copy[i + 1]) > threshold) {//arr is sorted at this point
                sa[i] = i;
            } else {//ties found
                int nties = 2;
                /*
                 * If successive elements of the sorted array differ by less than the threshold, they are treated as equal.
                 * We count the number of ties in each group.
                 */
                for (int j = i + 1; (j < n - 1) && (abs(copy[j] - copy[j + 1]) < threshold); ++j) {
                    nties++;
                }

                double rank4group = i + 0.5 * (nties - 1);
                for (int k = 0; k < nties; ++k) {
                    sa[i + k] = rank4group;
                }

                double ka = (nties - 1) * nties;
                ss += ka;
                tt += ka * (nties + 1);
                i += nties - 1;//the for loop will advance j by another 1
            }
        }

        s = ss;
        t = tt;

        rank = new double[n];
        for (int i = 0; i < n; ++i) {
            rank[order[i] - 1] = sa[i] + 1;//'rank' counts from 1
        }
    }

    /**
     * Compute the sample ranks of the values.
     *
     * @param values the values
     */
    public Rank(double[] values) {
        this(values, SuanShuUtils.autoEpsilon(values));
    }

    /**
     * Get the rank of the <i>i</i>-th element.
     *
     * @param i the index to a value
     * @return the rank of the <i>i</i>-th element
     */
    public double rank(int i) {
        return rank[i];
    }

    /**
     * Get the ranks of the values.
     *
     * @return the ranks
     */
    public double[] ranks() {
        return Arrays.copyOf(rank, rank.length);
    }

    /**
     * /[
     * t = \sum(t_i^3 - t_i)
     * \]
     *
     * @return <i>t</i>
     */
    public double t() {
        return t;
    }

    /**
     * \[
     * s = \sum(t_i^2 - t_i)
     * \]
     *
     * @return <i>s</i>
     */
    public double s() {
        return s;
    }
}
