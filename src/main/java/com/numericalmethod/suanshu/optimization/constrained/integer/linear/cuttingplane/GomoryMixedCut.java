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
import java.util.Arrays;

/**
 * This cutting-plane implementation uses Gomory's mixed cut method.
 *
 * @author Haksun Li
 * @see "K. C. Toh, "4.3 Gomory's mixed integer cutting plane method," MA4252: Discrete Optimization, NUS, 2011."
 */
public class GomoryMixedCut extends SimplexCuttingPlane {

    /**
     * This is Gomory's mixed cut.
     */
    public static class MyCutter implements Cutter {

        private final ILPProblem problem;
        private final int[] indices;

        /**
         * Construct a Gomory mixed cutter.
         *
         * @param problem an MILP problem
         */
        public MyCutter(ILPProblem problem) {
            this.problem = problem;
            this.indices = problem.getIntegerIndices();
        }

        @Override
        public SimplexTable cut(SimplexTable table) {
            //step 1: search for a source row
            int k = -1;//row index
            for (int i = 1; i <= table.nRows(); ++i) {
                if (!table.getRowLabel(i).type.equals(LabelType.NON_BASIC)) {
                    continue;
                }

                double xk = table.getBCol(i);
                if (Math.abs(xk - Math.round(xk)) < problem.epsilon()) {
                    continue;//already an integer
                }

                if (isIntegralConstraint(table.getRowLabel(i).index)) {
                    k = i;
                    break;
                }
            }
            assert k != -1;//this method should not be called when there is no non-satisfying integral variable

            //step 2: add a mixed cut-constraint, Section 4.3, Gomory's mixed integer cutting plane method
            ++k;//will shift all rows down by 1 after adding the new constraint
            table.addRowAt(1, new SimplexTable.Label(LabelType.BASIC, table.nRows()));//counting the new constraint from 'nRow'

            double b = table.get(k, table.nCols());
            double beta = b - Math.floor(b);
            table.set(1, table.nCols(), -beta);//set the B column

            for (int j = 1; j < table.nCols(); j++) {
                if (!(table.getColLabel(j).type.equals(LabelType.NON_BASIC) && isIntegralConstraint(table.getColLabel(j).index))) {//the column corresponds to fractional variable
                    if (table.get(k, j) <= 0) {//J+: SuanShu has a different convention than the reference (by -1)
                        table.set(1, j, -table.get(k, j));
                    } else {//J-: SuanShu has a different convention than the reference (by -1)
                        double coefficient = -beta / (beta - 1) * table.get(k, j);
                        table.set(1, j, coefficient);
                    }
                }
            }

            return table;
        }

        private boolean isIntegralConstraint(int index) {
            return Arrays.binarySearch(indices, index) >= 0;
        }
    }

    /**
     * Construct a Gomory mixed cutting-plane minimizer to solve an MILP problem.
     */
    public GomoryMixedCut() {
        super(
                new LPTwoPhaseSolver(),
                new CutterFactory() {

                    @Override
                    public Cutter getCutter(final ILPProblem problem) {
                        return new MyCutter(problem);
                    }
                });
    }
}
