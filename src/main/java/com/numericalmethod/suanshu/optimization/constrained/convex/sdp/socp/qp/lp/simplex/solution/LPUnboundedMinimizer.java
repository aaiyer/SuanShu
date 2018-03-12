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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution;

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is the solution to an unbounded linear programming problem.
 *
 * @author Haksun Li
 */
public class LPUnboundedMinimizer implements LPSimplexMinimizer {

    protected final SimplexTable table;
    protected final int lambdaCol;

    /**
     * Construct the solution for an unbounded linear programming problem.
     *
     * @param table     the table of the current (intermediate) solution of a linear programming problem
     * @param lambdaCol the column index for which there is no row that passes the ratio test (hence the problem is unbounded)
     * When the problem is bounded, {@code lambdaCol = 0}.
     */
    public LPUnboundedMinimizer(SimplexTable table, int lambdaCol) {
        SuanShuUtils.assertArgument(lambdaCol > 0,
                                    "this class reads only an unbounded solution, lambdaCol = %d", lambdaCol);

        this.lambdaCol = lambdaCol;
        this.table = new SimplexTable(table);
    }

    @Override
    public SimplexTable getResultantTableau() {
        return new SimplexTable(table);
    }

    @Override
    public double minimum() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * This is the same as the <i>u</i> vector, such that the direction of arbitrarily negative can be computed by adjusting <i>λ</i>.
     * <blockquote><i>
     * u + λv
     * </i></blockquote>
     *
     * @return the <i>u</i> vector
     */
    @Override
    public ImmutableVector minimizer() {
        return table.minimizer();
    }

    /**
     * When the problem is unbounded, the direction of arbitrarily negative can be computed by adjusting <i>λ</i>.
     * <blockquote><i>
     * u + λv
     * </i></blockquote>
     * where {@code u = minimizer()}.
     * <p/>
     * This is only meaningful in the case where the problem is unbounded.
     *
     * @return the <i>v</i> vector
     */
    public ImmutableVector v() {
        Vector v = new DenseVector(table.getProblemSize(), 0);
        v.set(table.getColLabel(lambdaCol).index, 1);//c.f., p.54

        for (int i = 1; i < table.nRows(); i++) {//ignore the last COST row
            if (table.getRowLabel(i).type == SimplexTable.LabelType.NON_BASIC) {
                v.set(table.getRowLabel(i).index, table.get(i, lambdaCol));
            }
        }

        return new ImmutableVector(v);
    }
}
