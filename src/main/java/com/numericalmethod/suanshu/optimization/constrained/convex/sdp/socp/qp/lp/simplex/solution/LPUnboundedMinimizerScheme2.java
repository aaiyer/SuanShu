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

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is the solution to an unbounded linear programming problem found in scheme 2.
 *
 * @author Haksun Li
 */
public class LPUnboundedMinimizerScheme2 extends LPUnboundedMinimizer {

    /**
     * Construct the solution for an unbounded linear programming problem as a result of applying scheme 2.
     *
     * @param table     the resultant simplex table
     * @param lambdaCol the column indicating an unbounded solution
     */
    public LPUnboundedMinimizerScheme2(SimplexTable table, int lambdaCol) {
        super(table, lambdaCol);
    }

    @Override
    public ImmutableVector minimizer() {
        return v();
    }

    @Override
    public ImmutableVector v() {
        Vector v = new DenseVector(table.getProblemSize(), 0);
        v.set(table.getColLabel(lambdaCol).index, -1 * Math.signum(table.getCostRow(lambdaCol)));//c.f., p.85

        return new ImmutableVector(v);
    }
}
