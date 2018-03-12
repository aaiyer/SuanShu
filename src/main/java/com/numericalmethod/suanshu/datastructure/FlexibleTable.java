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

import com.numericalmethod.suanshu.matrix.MatrixAccessException;
import com.numericalmethod.suanshu.matrix.doubles.MatrixTable;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;

/**
 * This is a 2D table that can shrink or grow by row or by column.
 * The labels to the rows and columns are for printing and debugging.
 * The access to the table is by indices. All indices count from 1.
 * Translation between labels and indices are provided.
 *
 * @author Haksun Li
 */
public class FlexibleTable implements MatrixTable {

    private int count = 0;
    private ArrayList<Object> rowLabels = new ArrayList<Object>();
    private ArrayList<Object> colLabels;
    private ArrayList<ArrayList<Double>> table = new ArrayList<ArrayList<Double>>();//row view

    /**
     * Construct a flexible table that can shrink or grow.
     *
     * @param rowLabels the row labels
     * @param colLabels the column labels
     * @param cells     the table content
     */
    public FlexibleTable(Object[] rowLabels, Object[] colLabels, double[][] cells) {
        this.colLabels = new ArrayList<Object>(colLabels.length);
        for (int i = 0; i < colLabels.length; ++i) {
            this.colLabels.add(colLabels[i]);
        }

        for (int i = 0; i < rowLabels.length; ++i) {
            addRowAt(i + 1, rowLabels[i]);
            for (int j = 0; j < colLabels.length; ++j) {
                set(i + 1, j + 1, cells[i][j]);
            }
        }
    }

    /**
     * Construct a table by row and column labels, initializing the content to 0.
     *
     * @param rowLabels the row labels
     * @param colLabels the column labels
     */
    public FlexibleTable(Object[] rowLabels, Object[] colLabels) {
        this(rowLabels, colLabels, new double[rowLabels.length][colLabels.length]);
    }

    /**
     * Construct a table using default labeling.
     *
     * @param nRows number of rows
     * @param nCols number of columns
     */
    public FlexibleTable(int nRows, int nCols) {
        this(new Object[nRows], new Object[nCols]);

        for (int i = 0; i < nRows; ++i) {
            renameRow(i + 1, getAutoRowLabel());
        }

        for (int i = 0; i < nCols; ++i) {
            renameCol(i + 1, getAutoColLabel());
        }
    }

    /**
     * Copy constructor.
     *
     * @param that another {@code FlexibleTable}
     */
    public FlexibleTable(FlexibleTable that) {
        this(that.rowLabels.toArray(new Object[0]),
             that.colLabels.toArray(new Object[0]),
             MatrixUtils.to2DArray(that.toMatrix()));
    }

    @Override
    public int nRows() {
        return rowLabels.size();
    }

    @Override
    public int nCols() {
        return colLabels.size();
    }

    @Override
    public void set(int i, int j, double value) {
        table.get(i - 1).set(j - 1, value);
    }

    @Override
    public double get(int i, int j) {
        return table.get(i - 1).get(j - 1);
    }

    public Vector getRow(int row) throws MatrixAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector getColumn(int col) throws MatrixAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Translate a row label to a row index. The index counts from 1.
     *
     * @param label a row label
     * @return the row index
     */
    public int getIndexFromRowLabel(Object label) {
        for (int i = 0; i < rowLabels.size(); ++i) {
            if (rowLabels.get(i).equals(label)) {
                return i + 1;
            }
        }

        throw new RuntimeException(String.format("unrecognized name: %s", label.toString()));
    }

    /**
     * Translate a column label to a column index. The index counts from 1.
     *
     * @param label a column label
     * @return the column index
     */
    public int getIndexFromColLabel(Object label) {
        for (int i = 0; i < colLabels.size(); ++i) {
            if (colLabels.get(i).equals(label)) {
                return i + 1;
            }
        }

        throw new RuntimeException(String.format("unrecognized name: %s", label.toString()));
    }

    /**
     * Get the label for row i.
     *
     * @param i a row index, counting from 1
     * @return the label for row i
     */
    public Object getRowLabel(int i) {
        return rowLabels.get(i - 1);
    }

    /**
     * Get the label for column i.
     *
     * @param i a column index, counting from 1
     * @return the label for column i
     */
    public Object getColLabel(int i) {
        return colLabels.get(i - 1);
    }

    /**
     * Rename row i.
     *
     * @param i     a row index, counting from 1
     * @param label the new label
     */
    public void renameRow(int i, Object label) {
        rowLabels.set(i - 1, label);
    }

    /**
     * Rename column i.
     *
     * @param i     a column index, counting from 1
     * @param label the new label
     */
    public void renameCol(int i, Object label) {
        colLabels.set(i - 1, label);
    }

    /**
     * Delete row i.
     *
     * @param i a row index, counting from 1
     */
    public void deleteRow(int i) {
        table.remove(i - 1);
        rowLabels.remove(i - 1);
    }

    /**
     * Delete column i.
     *
     * @param i a column index, counting from 1
     */
    public void deleteCol(int i) {
        int nRows = nRows();
        for (int j = 0; j < nRows; ++j) {
            Double deleted = table.get(j).remove(i - 1);
        }

        colLabels.remove(i - 1);
    }

    /**
     * Add a row at i.
     *
     * @param i     the row index, counting from 1
     * @param label the row label
     */
    public void addRowAt(int i, Object label) {
        rowLabels.add(i - 1, label);

        int nCols = nCols();
        table.add(i - 1, new ArrayList<Double>(nCols));
        ArrayList<Double> row = table.get(i - 1);
        for (int j = 0; j < nCols; ++j) {
            row.add(new Double(0));
        }
    }

    /**
     * Add a row at i.
     *
     * @param i the row index, counting from 1
     */
    public void addRowAt(int i) {
        addRowAt(i, getAutoRowLabel());
    }

    /**
     * Add a column at i.
     *
     * @param i     the column index, counting from 1
     * @param label the column label
     */
    public void addColAt(int i, Object label) {
        colLabels.add(i - 1, label);

        int nRows = nRows();
        for (int j = 0; j < nRows; ++j) {
            table.get(j).add(i - 1, 0.);
        }
    }

    /**
     * Add a column at i.
     *
     * @param i the column index, counting from 1
     */
    public void addColAt(int i) {
        addColAt(i, getAutoColLabel());
    }

    /**
     * Get a copy of the flexible table in the form of a matrix.
     *
     * @return a matrix representation
     */
    public DenseMatrix toMatrix() {
        int nRows = nRows();
        int nCols = nCols();

        DenseMatrix A = new DenseMatrix(nRows, nCols);

        for (int i = 1; i <= nRows; ++i) {
            for (int j = 1; j <= nCols; ++j) {
                A.set(i, j, get(i, j));
            }
        }

        return A;
    }

    private Object getAutoRowLabel() {
        return String.format("row#%d", ++count);
    }

    private Object getAutoColLabel() {
        return String.format("col#%d", ++count);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        int nRows = nRows();
        int nCols = nCols();

        str.append(String.format("%dx%d\n", nRows, nCols));

        str.append(String.format("%-32s", ""));
        for (int i = 0; i < nCols; ++i) {
            str.append(String.format("%-32s", colLabels.get(i)));
//            str.append("\t");
        }
        str.append("\n");

        for (int i = 0; i < nRows; ++i) {
            str.append(String.format("%-32s", rowLabels.get(i)));
            for (int j = 0; j < nCols; ++j) {
                str.append(String.format("%-32f", get(i + 1, j + 1)));
//                str.append("\t");
            }
            str.append("\n");
        }

        return str.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlexibleTable other = (FlexibleTable) obj;
        if (this.table != other.table && (this.table == null || !this.table.equals(other.table))) {
            return false;
        }
        if (this.colLabels != other.colLabels && (this.colLabels == null || !this.colLabels.equals(other.colLabels))) {
            return false;
        }
        if (this.rowLabels != other.rowLabels && (this.rowLabels == null || !this.rowLabels.equals(other.rowLabels))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.table != null ? this.table.hashCode() : 0);
        hash = 41 * hash + (this.colLabels != null ? this.colLabels.hashCode() : 0);
        hash = 41 * hash + (this.rowLabels != null ? this.rowLabels.hashCode() : 0);
        return hash;
    }
}
