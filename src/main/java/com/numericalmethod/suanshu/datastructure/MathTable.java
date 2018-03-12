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
package com.numericalmethod.suanshu.datastructure;

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import java.util.*;

/**
 * A mathematical table consists of numbers showing the results of calculation with varying arguments.
 * It can be used to simplify and drastically speed up computation.
 * We use them to archive results for, for example, quantiles of difficult distribution functions, often computed by slow Monte Carlo simulation.
 * <p/>
 * This implementation provides various ways of looking up a table, esp. when an index is not exactly found in the table.
 *
 * @author Haksun Li
 */
public class MathTable {//TODO: make synchronized; read/write from/to file

    private final double EPSILON = 10. * com.numericalmethod.suanshu.Constant.EPSILON;

    //<editor-fold defaultstate="collapsed" desc="MathTable.Row">
    /**
     * A row is indexed by a number and contains multiple values.
     * <p/>
     * This class is immutable.
     */
    public class Row {

        /** the row index */
        private final double index;
        /** the column values in the row */
        private final double[] values;

        /**
         * Construct a mathematical table row.
         *
         * @param index  the row index
         * @param values the column values
         */
        private Row(double index, double[] values) {//read-only
            this.index = index;
            this.values = Arrays.copyOf(values, values.length);
        }

        /**
         * Get the value in the row by column index.
         *
         * @param j a column index, counting from 1
         * @return the corresponding column value
         */
        public double get(int j) {
            return values[j - 1];
        }

        /**
         * Get the value in the row by column name.
         *
         * @param header the column name
         * @return the corresponding column value
         */
        public double get(String header) {
            return get(headers.get(header));
        }

        /**
         * Convert the row to a {@code double[]}, excluding the index.
         *
         * @return the {@code double[]} representation
         */
        public double[] toArray() {
            return Arrays.copyOf(values, values.length);
        }
    }
    //</editor-fold>

    /** a translation table to convert header strings to header indices */
    private final Map<String, Integer> headers = new LinkedHashMap<String, Integer>();//perserve insertion order
    /** store the rows, sorted according the row indices in ascending order */
    private final TreeMap<Double, Row> table = new TreeMap<Double, Row>();

    /**
     * Construct an empty table by headers.
     *
     * @param headers the column names; they must be unique
     */
    private void ctor(String[] headers) {
        int j = 0;
        for (String header : headers) {
            this.headers.put(header, j++);//counting from 0
        }

        //check uniqueness
        SuanShuUtils.assertArgument(this.headers.size() == headers.length, "duplicated headers");
    }

    /**
     * Construct an empty table by headers.
     *
     * @param headers the column names; they must be unique
     */
    public MathTable(String... headers) {
        ctor(headers);
    }

    /**
     * Construct an empty table.
     * The headers are given default names.
     *
     * @param nColumns the number of columns
     */
    public MathTable(int nColumns) {
        String[] headers = new String[nColumns];
        for (int i = 1; i <= nColumns; ++i) {
            headers[i - 1] = String.format("Column%d", i);
        }

        ctor(headers);
    }

    /**
     * Get the column names.
     *
     * @return the headers
     */
    public String[] getHeaders() {
        return headers.keySet().toArray(new String[0]);
    }

    /**
     * Get the number of columns in the table.
     *
     * @return the number of columns
     */
    public int nColumns() {
        return headers.size();
    }

    /**
     * Add a row to the table.
     *
     * @param index  the row index
     * @param values the row values
     */
    public void addRow(double index, double[] values) {
        Row row = new Row(index, values);
        table.put(row.index, row);
    }

    /**
     * Add rows by a {@code double[][]}.
     * The first number in each row/{@code double[]} is the row index.
     * For example,
     * <pre>
     * new double[][]{
     * {1.0, 1.1, 1.2, 1.3},
     * {2.0, 2.1, 2.2, 2.3},
     * {3.0, 3.1, 3.2, 3.3},
     * {4.0, 4.1, 4.2, 4.3}
     * };
     * </pre>
     * represents
     * <pre>
     * 1.0: {1.1, 1.2, 1.3}//row 1
     * 2.0: {2.1, 2.2, 2.3}//row 2
     * 3.0: {3.1, 3.2, 3.3}//row 3
     * 4.0: {4.1, 4.2, 4.3}//row 4
     * </pre>
     *
     * @param data a {@code double[][]} of table entries.
     */
    public void addRows(double[][] data) {
        for (int i = 0; i < data.length; ++i) {
            double[] row = data[i];
            double index = row[0];
            double[] column = Arrays.copyOfRange(row, 1, row.length);//TODO: how to avoid this uncessary copying

            addRow(index, column);
        }
    }

    /**
     * Get a copy of the row indices.
     *
     * @return a copy of the row indices
     */
    public double[] getIndices() {
        NavigableSet<Double> keySet = table.navigableKeySet();
        return DoubleUtils.collection2DoubleArray(keySet);
    }

    /**
     * Get the row corresponding to a row index.
     * If there is no row matching the exact index, the row with the biggest index but smaller than the specified index is returned.
     *
     * @param i a row index
     * @return the corresponding row
     * @throws NoSuchElementException if {@code i} is smaller than the first row index
     */
    public Row getRowOnOrBefore(double i) {
        double lastKey = table.headMap(i + EPSILON).lastKey();
        return table.get(lastKey);
    }

    /**
     * Get the row corresponding to a row index.
     * If there is no row matching the exact value, the row with the smallest index value but bigger than the specified value is returned.
     *
     * @param i a row index
     * @return the corresponding row
     * @throws NoSuchElementException if {@code i} is bigger than the last row index value
     */
    public Row getRowOnOrAfter(double i) {
        double firstKey = table.tailMap(i - EPSILON).firstKey();
        return table.get(firstKey);
    }

    /**
     * Get the rows having the row index value equal to or just smaller than {@code i}.
     * The returned {@code Iterator} allows iterating the rows in reversed order starting from the matching row.
     *
     * @param i the row index
     * @return an {@code Iterator} of {@code Row}s at or above the matching row
     */
    public Iterator<Row> getRowsOnOrBefore(double i) {
        NavigableMap<Double, Row> view1 = table.headMap(i + EPSILON, true);
        NavigableMap<Double, Row> view2 = view1.descendingMap();//review map
        Collection<Row> rows = view2.values();

        return rows.iterator();
    }

    /**
     * Get the rows having the row index value equal to or just bigger than {@code i}.
     * The returned {@code Iterator} allows iterating the rows starting from the matching row.
     *
     * @param i the row index
     * @return an {@code Iterator} of {@code Row}s at or below the matching row
     */
    public Iterator<Row> getRowsOnOrAfter(double i) {
        NavigableMap<Double, Row> view1 = table.tailMap(i - EPSILON, true);
        Collection<Row> rows = view1.values();

        return rows.iterator();
    }

    /**
     * Get a particular table entry at [i,j].
     * If there is no matching row to {@code i}, by default, we use linear interpolation between the row above and below.
     * If <i>i</i> is smaller than the first row index, we return the value at [1,j].
     * A subclass may override this behavior to customize interpolation.
     *
     * @param i a row index
     * @param j a column index, counting from 1
     * @return the value at [i,j]
     * @throws NoSuchElementException if {@code rowValue} is outside the table range
     */
    public double get(double i, int j) {
        Row row1 = getRowOnOrBefore(i);
        double index1 = row1.index;
        double value1 = row1.get(j);
        if (index1 == i) {
            return value1;
        }

        //no matching row; do an linear interpolation
        Row row2 = getRowOnOrAfter(i);
        double index2 = row2.index;
        double value2 = row2.get(j);

        double result = value1 * (index2 - i);
        result += value2 * (i - index1);
        result /= index2 - index1;

        return result;
    }

    /**
     * Get a particular table entry at [i, "header"].
     * If there is no matching row to {@code i}, by default, we use linear interpolation between the row above and below.
     * If <i>i</i> is smaller than the first row index, we return the value at [1,"header"].
     * A subclass may override this behavior to customize interpolation.
     *
     * @param i      a row value index
     * @param header the column name
     * @return the value at [i, "header"]
     * @throws NoSuchElementException if {@code i} is outside the table range
     */
    public double get(double i, String header) {
        return get(i, this.headers.get(header));
    }
}
