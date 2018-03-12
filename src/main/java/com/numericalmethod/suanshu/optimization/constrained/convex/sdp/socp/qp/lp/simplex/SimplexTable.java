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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.datastructure.FlexibleTable;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import static com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix.cbind;
import static com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix.rbind;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.CanonicalLPProblem1;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblem;
import com.numericalmethod.suanshu.optimization.problem.MinimizationSolution;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;

/**
 * This is a simplex table used to solve a linear programming problem using a simplex method.
 *
 * @author Haksun Li
 */
public class SimplexTable extends FlexibleTable implements MinimizationSolution<ImmutableVector> {

    //<editor-fold defaultstate="collapsed" desc="labeling of rows and columns">
    public static enum LabelType {

        /**
         * the non-basic variables, i.e., original variables in the cost function
         */
        NON_BASIC,
        /**
         * the constraint values (the last column)
         */
        B,
        /**
         * the basic variables, i.e., constraints
         */
        BASIC,
        /**
         * the cost function (the last row)
         */
        COST,
        /**
         * the equality constraints
         */
        EQUALITY,
        /**
         * the free variables
         */
        FREE,
        /**
         * the artificial variable, x0, pp. 61
         */
        ARTIFICIAL,
        /**
         * the artificial objective, z0, pp. 61
         */
        ARTIFICIAL_COST,
        /**
         * the deleted rows and columns
         */
        DELETED,
        /**
         * an undefined label
         */
        UNDEFINED
    };

    public static class Label {//a read-only structure

        /**
         * the label type
         */
        public final LabelType type;
        /**
         * the index of a variable, an inequality, an equality, etc., counting from 1
         */
        public final int index;

        /**
         * Construct a label for a row or column in the simplex table.
         *
         * @param type  the label type
         * @param index the index of a variable, an inequality, an equality, etc., counting from 1
         */
        public Label(LabelType type, int index) {
            this.type = type;
            this.index = index;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append(String.format("%s,%d", type.toString(), index));
            return result.toString();
        }
    }

    public static final Label COST = new Label(LabelType.COST, Integer.MAX_VALUE);
    public static final Label B = new Label(LabelType.B, Integer.MAX_VALUE);
    public static final Label ARTIFICIAL_COST = new Label(LabelType.ARTIFICIAL_COST, Integer.MAX_VALUE);
    public static final Label ARTIFICIAL = new Label(LabelType.ARTIFICIAL, 0);
    public static final Label UNDEFINED = new Label(LabelType.UNDEFINED, Integer.MAX_VALUE);
    //</editor-fold>
    private final double epsilon;

    //<editor-fold defaultstate="collapsed" desc="Ctors">
    SimplexTable(FlexibleTable table, double epsilon) {// TODO: for now made "package" for test case
        super(table);
        this.epsilon = epsilon;
    }

    /**
     * Construct a simplex table from a canonical linear programming problem.
     *
     * @param problem a canonical linear programming problem
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public SimplexTable(CanonicalLPProblem1 problem, double epsilon) {
        this(ctorFromStandardProblem(problem), epsilon);
    }

    /**
     * Construct a simplex table from a canonical linear programming problem.
     *
     * @param problem a canonical linear programming problem
     */
    public SimplexTable(CanonicalLPProblem1 problem) {
        this(ctorFromStandardProblem(problem), 10. * Constant.EPSILON);//TODO: how to determine 0?;
    }

    private static FlexibleTable ctorFromStandardProblem(CanonicalLPProblem1 problem) {
        Matrix b = new DenseMatrix(problem.b());//we assume b ≤ 0
        Matrix mb = b.scaled(-1);

        //xB = [A -b]
        Matrix xB = cbind(problem.A(), mb);

        //z = [c' 0]
        Matrix z = cbind(
                new DenseMatrix(problem.c()).t(),
                new DenseMatrix(1, 1).ZERO());

        /*
         * | A -b |
         * T = | c' 0 |
         */
        Matrix T = rbind(xB, z);

        //label the rows
        Label[] rowLabel = new Label[T.nRows()];
        for (int i = 0; i < T.nRows() - 1; i++) {
            rowLabel[i] = new Label(LabelType.BASIC, i + 1);
        }
        rowLabel[T.nRows() - 1] = COST;

        //label the columns
        Label[] colLabel = new Label[T.nCols()];
        for (int j = 0; j < T.nCols() - 1; j++) {
            colLabel[j] = new Label(LabelType.NON_BASIC, j + 1);
        }
        colLabel[T.nCols() - 1] = B;

        double[][] cells = MatrixUtils.to2DArray(T);
        return new FlexibleTable(rowLabel, colLabel, cells);
    }

    /**
     * Construct a simplex table from a general linear programming problem.
     *
     * @param problem a general linear programming problem
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public SimplexTable(LPProblem problem, double epsilon) {
        this(ctorFromLPProblem(problem), epsilon);
    }

    /**
     * Construct a simplex table from a general linear programming problem.
     *
     * @param problem a general linear programming problem
     */
    public SimplexTable(LPProblem problem) {
        this(ctorFromLPProblem(problem), 10. * Constant.EPSILON);//TODO: how to determine 0?;
    }

    private static FlexibleTable ctorFromLPProblem(LPProblem problem) {
        Vector b = problem.b();
        Vector beq = problem.beq();

        Matrix bbeq = new DenseMatrix(CreateVector.concat(b, beq));
        Matrix mbbeq = bbeq.scaled(-1);

        Matrix A = problem.A();
        Matrix Aeq = problem.Aeq();
        Matrix AAeq = CreateMatrix.rbind(A, Aeq);

        //xB = [A     -b]
        //     [Aeq -beq]
        Matrix xB = cbind(AAeq, mbbeq);

        //z = [c' 0]
        Matrix z = cbind(
                new DenseMatrix(problem.c()).t(),
                new DenseMatrix(1, 1).ZERO());

        /*
         * | A -b |
         * T = | Aeq -beq |
         * | c' 0 |
         */
        Matrix T = rbind(xB, z);

        //label the rows
        Label[] rowLabel = new Label[T.nRows()];
        int nInequalities = A.nRows();//problem.nGreaterThanInequalities();
        for (int i = 0; i < nInequalities; i++) {
            rowLabel[i] = new Label(LabelType.BASIC, i + 1);
        }
        for (int i = nInequalities; i < T.nRows(); i++) {
            rowLabel[i] = new Label(LabelType.EQUALITY, i - nInequalities + 1);
        }
        rowLabel[T.nRows() - 1] = COST;

        //label the columns
        Label[] colLabel = new Label[T.nCols()];
        for (int j = 0; j < T.nCols() - 1; j++) {
            colLabel[j] = new Label(LabelType.NON_BASIC, j + 1);
        }
        colLabel[T.nCols() - 1] = B;

        //identify the free variables
        for (int i = 1; i <= T.nCols(); ++i) {
            if (problem.isFree(i)) {
                colLabel[i - 1] = new Label(LabelType.FREE, i);
            }
        }

        double[][] cells = MatrixUtils.to2DArray(T);
        return new FlexibleTable(rowLabel, colLabel, cells);
    }

    /**
     * Copy constructor.
     *
     * @param table a simplex table
     */
    public SimplexTable(SimplexTable table) {
        super(table);
        this.epsilon = table.epsilon;
    }
    //</editor-fold>

    @Override
    public Label getColLabel(int i) {
        return (Label) super.getColLabel(i);
    }

    @Override
    public Label getRowLabel(int i) {
        return (Label) super.getRowLabel(i);
    }

    /**
     * Get the table entry at <i>[COST, j]</i>.
     *
     * @param j a column index, counting from 1
     * @return <i>T[COST, j]</i>
     */
    public double getCostRow(int j) {
        //we always use the last row
        //because we cannot distinguishes between COST and ARTIFICIAL_COST
        return get(nRows(), j);
    }

    /**
     * Get the table entry at <i>[i, B]</i>.
     *
     * @param i a row index, counting from 1
     * @return <i>T[i, B]</i>
     */
    public double getBCol(int i) {
        return get(i, getIndexFromColLabel(B));
    }

    /**
     * Get the number of variables in the problem or the cost/objective function.
     *
     * @return the number of variables
     */
    public int getProblemSize() {
        int count = 0;

        //check columns
        for (int i = 1; i <= nCols(); ++i) {
            Label label = getColLabel(i);
            if (label.type == LabelType.NON_BASIC || label.type == LabelType.FREE) {
                ++count;
            }
        }

        //check rows
        for (int i = 1; i <= nRows(); ++i) {
            Label label = getRowLabel(i);
            if (label.type == LabelType.NON_BASIC || label.type == LabelType.FREE) {
                ++count;
            }
        }

        return count;
    }

    /**
     * Perform a Jordan Exchange to swap row {@code r} with column {@code s}.
     *
     * @param r the index to a entering variable (row)
     * @param s the index to a leaving variable (column)
     * @return a swapped simplex table
     */
    public SimplexTable swap(int r, int s) {
        FlexibleTable table1 = new JordanExchange(this, r, s);
        return new SimplexTable(table1, epsilon);
    }

    /**
     * Check if this table is feasible.
     *
     * @return {@code true} if the table is feasible
     */
    public boolean isFeasible() {
        for (int i = 1; i <= nRows(); i++) {
            // we don'care about free variables, p. 80
            if (getRowLabel(i).type == SimplexTable.LabelType.BASIC) {//  do we check rows corresponding to variables (NON_BASIC)?
                if (getBCol(i) < -epsilon) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public double minimum() {
        return getCostRow(getIndexFromColLabel(B));
    }

    @Override
    public ImmutableVector minimizer() {
        Vector minimizer = new DenseVector(getProblemSize(), 0);

        for (int i = 1; i <= nRows(); i++) {
            if (getRowLabel(i).type == SimplexTable.LabelType.NON_BASIC || getRowLabel(i).type == SimplexTable.LabelType.FREE) {
                minimizer.set(getRowLabel(i).index, getBCol(i));//c.f., p.54
            }
        }

        return new ImmutableVector(minimizer);
    }
}
