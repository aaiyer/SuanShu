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
package com.numericalmethod.suanshu.optimization.constrained.integer.linear.cuttingplane;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable.LabelType;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solver.LPTwoPhaseSolver;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.cuttingplane.SimplexCuttingPlane.CutterFactory.Cutter;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem.ILPProblem;
import com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem.PureILPProblem;

/**
 * This cutting-plane implementation uses Gomory's pure cut method for pure integer programming,
 * in which all variables are integral.
 *
 * @author Haksun Li
 * @see "K. C. Toh, "4.3 Gomory's mixed integer cutting plane method," MA4252: Discrete Optimization, NUS, 2011"
 */
public class GomoryPureCut extends SimplexCuttingPlane {

    /**
     * This is Gomory's pure cut.
     */
    public static class MyCutter implements Cutter {

        private final PureILPProblem problem;

        /**
         * Construct a Gomory pure cutter.
         *
         * @param problem a pure ILP problem
         */
        public MyCutter(PureILPProblem problem) {
            this.problem = problem;
        }

        @Override
        public SimplexTable cut(SimplexTable table) {
            //step 1: search for a source row
            int i = 1;//row index
            for (; i <= table.nRows(); ++i) {
                if (!table.getRowLabel(i).type.equals(LabelType.NON_BASIC)) {
                    continue;
                }

                double xk = table.getBCol(i);
                if (table.getRowLabel(i).type.equals(LabelType.NON_BASIC)
                    && Math.abs(xk - Math.round(xk)) > problem.epsilon()) {
                    break;
                }
            }
            assert i <= table.nRows();//this method should not be called when there is no non-satisfying integral variable

            //step 2: add a pure cut-constraint, Section 4.2.1, Gomory's cutting plane algorithm for ILPs
            ++i;//will shift all rows down by 1 after adding the new constraint
            table.addRowAt(1, new SimplexTable.Label(LabelType.BASIC, table.nRows()));//counting the new constraint from 'nRow'

            for (int j = 1; j < table.nCols(); j++) {//eq. 4.7
                double aij = table.get(i, j);
                double value = -Math.floor(-aij) - aij;
                table.set(1, j, value); //floor(a_i) - a_i
            }
            double bi = table.get(i, table.nCols());
            double value2 = Math.floor(bi) - bi;
            table.set(1, table.nCols(), value2); //floor(b_i) - b_i

            return table;
        }
    }

    /**
     * Construct a Gomory pure cutting-plane minimizer to solve pure ILP problems,
     * in which all variables are integral.
     */
    public GomoryPureCut() {
        super(
                new LPTwoPhaseSolver(),
                new CutterFactory() {

                    @Override
                    public Cutter getCutter(final ILPProblem problem) {
                        return new MyCutter((PureILPProblem) problem);
                    }
                });
    }
}
