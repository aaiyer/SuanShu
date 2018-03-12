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
package com.numericalmethod.suanshu.analysis.interpolation;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.StepFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.tuple.OrderedPairs;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;
import java.util.Arrays;

/**
 * Neville's algorithm is a polynomial interpolation algorithm.
 * Given <i>n + 1</i> points, there is a unique polynomial of degree &le; <i>n</i> which associates with them.
 * Neville's algorithm evaluates this polynomial for a given input <i>x</i>.
 * <p/>
 * This implementation is based on filling out the Neville table. This table can be used in two ways.
 * First, it can be used to compute the interpolated function <i>f(x)</i>.
 * Second, it can be used in an incremental manner by adding a few, say one, data points each time to allow for examination of the table values.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Neville_algorithm">Wikipedia: Neville's algorithm</a>
 */
public class NevilleTable extends UnivariateRealFunction implements OnlineInterpolator {

    private double[] x;//the abscissae
    private double[][] table;//TODO: write a version that does not keep the O(n^2) table
    private double lastX;//last x evaluated
    private int N;//number of data points
    private int N0;//last N before adding new data
    private static final double allowance = 1.2;

    /**
     * Construct a Neville table of size <i>n</i>, initialized with data <i>{(x, y)}</i>.
     * The size <i>n</i> is only indicative. If there are more data points than <i>n</i>, the table size grows accordingly.
     *
     * @param n indicative size; make this big for reservation to add new data points
     * @param f the points to be interpolated
     */
    public NevilleTable(int n, OrderedPairs f) {
        int size = n;
        if (size < f.size()) {
            size = (int) (f.size() * allowance);
        }

        this.x = new double[size];
        this.table = new double[size][size];

        addData(f);
    }

    /**
     * Construct a Neville table of size <i>n</i>, initialized with data <i>{(x, y)}</i>.
     *
     * @param f the points to be interpolated
     */
    public NevilleTable(OrderedPairs f) {
        this(0, f);
    }

    /**
     * Construct an empty Neville table.
     */
    public NevilleTable() {
        this(10, new StepFunction(0));
    }

    @Override
    public void addData(OrderedPairs f) {
        final double[] fx = f.x();
        final double[] fy = f.y();

        int dsize = fx.length;
        if (N + dsize > table.length) {//expand the table
            int newLength = (int) ((N + dsize) * allowance);

            double[] newX = new double[newLength];
            for (int i = 0; i < N; ++i) {//copy the existing data points
                newX[i] = x[i];
            }
            x = newX;

            double[][] newTable = new double[newLength][newLength];
            for (int i = 0; i < N; ++i) {//copy the existing data points
                for (int j = i; j < N; ++j) {
                    newTable[i][j] = table[i][j];
                }
            }
            table = newTable;
        }

        for (int i = N, j = 0; j < dsize; ++i, ++j) {//add the new points
            x[i] = fx[j];
            table[i][i] = fy[j];
        }

        N += dsize;
    }

    /**
     * Get the number of data points.
     *
     * @return the number of data points
     */
    public int N() {
        return N;
    }

    /**
     * Get a copy of the Neville table.
     *
     * @return a copy of the table
     */
    public double[][] getTable() {
        double[][] copy = new double[N][N];

        for (int i = 0; i < N; ++i) {
            for (int j = i; j < N; ++j) {
                copy[i][j] = table[i][j];
            }
        }

        return copy;
    }

    /**
     * Get a copy of the <i>x</i>'s.
     *
     * @return a copy of <i>x</i>'s.
     */
    public double[] x() {
        double[] copy = Arrays.copyOf(x, N);
        return copy;
    }

    /**
     * Get the value of a table entry.
     *
     * @param i row index, counting from 0
     * @param j column index, counting from 0
     * @return the value of the table entry
     */
    public double get(int i, int j) {
        SuanShuUtils.assertArgument(j >= i, "only the upper triangular part of the table is used");
        return table[i][j];
    }

    @Override
    public double evaluate(double x) {
        SuanShuUtils.assertArgument(N >= 2, "not enough data points for interpolation");

        if (N0 == (N - 1) && lastX == x) {
            return table[0][N0];//return last saved value
        }

        /*
         * starting from the first column after the last computed column if evaluate for the same x;
         * otherwise, redo the computation from scratch
         */
        for (int j = (lastX == x ? N0 : 1); j < N; ++j) {//columns
            for (int i = j - 1; i >= 0; --i) {//rows
                double pij = (x - this.x[j]) * table[i][j - 1];
                pij += (this.x[i] - x) * table[i + 1][j];

                double divisor = this.x[i] - this.x[j];
                if (isZero(divisor, 0)) {
                    throw new DuplicatedAbscissae(String.format("x[%d] and x[%d] are identical", i, j));
                }

                pij /= divisor;
                table[i][j] = pij;
            }
        }

        //save the states
        lastX = x;
        N0 = N - 1;

        return table[0][N0];
    }
}
