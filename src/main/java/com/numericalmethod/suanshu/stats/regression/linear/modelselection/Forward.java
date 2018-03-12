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
 * FITNESS FOR subA PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT,
 * TITLE AND USEFULNESS.
 *
 * IN NO EVENT AND UNDER NO LEGAL THEORY,
 * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
 * ARISING AS subA RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.numericalmethod.suanshu.stats.regression.linear.modelselection;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GeneralizedLinearModel;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GLMProblem;
import static com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix.cbind;
import static java.lang.Math.abs;

/**
 * To construct a GLM getModel for a set of observations using the forward selection method,
 * we iteratively add a significant factor to the getModel, one at a time.
 * This repeats until all remaining getFactors are insignificant.
 *
 * @author Chun Yip Yau
 */
public class Forward extends SingleFactorSelection {

    private class Indices extends SingleFactorSelection.Indices {

        private Indices(GLMProblem problem) {
            super(problem);
            this.flags = R.rep(0, problem.nExogenousFactors());
        }

        /**
         * Add a factor.
         *
         * @param index
         */
        private void add(int index) {
            flags[index - 1] = index;
        }
    }

    /**
     * Construct automatically a GLM getModel using the forward selection method.
     * 
     * @param problem a GLM problem
     * @param significance a critical value to determine whether a factor is significant (to be included in the getModel)
     */
    public Forward(GLMProblem problem, double significance) {
        super(problem, significance);
        indices = new Indices(problem);

        run();
    }

    /**
     * an implementation of the forward selection method
     */
    private void run() {
        while (true) {
            Matrix subA = indices.subA();

            double zSignificant = Double.NEGATIVE_INFINITY;
            int significantFactor = 0;
            for (int i = 1; i <= problem.nExogenousFactors(); ++i) {
                if (indices.isSelected(i)) {
                    continue;
                }

                //add in one factor for trial
                Matrix A = cbind(new Matrix[]{
                            new DenseMatrix(problem.A.getColumn(i)),
                            subA
                        });

                GLMProblem trial = new GLMProblem(problem.y, A, problem.addIntercept, problem.family);
                GeneralizedLinearModel glm = new GeneralizedLinearModel(trial);
                double[] z = glm.beta.z.toArray();

                //select the most significant factor
                if (abs(z[0]) > zSignificant) {
                    zSignificant = abs(z[0]);
                    significantFactor = i;
                }
            }

            /*
             * For forward selection, we want to add the significant getFactors (those with small p-value).
             * A factor is significant, e.g., for 0.05 significant level, if either 1) p-value < 0.05 or 2) z-value > critical value.
             */
            if (zSignificant >= criticalValue) {
                ((Indices) indices).add(significantFactor);//add the most significant factor and continue
            } else {
                break;//stop
            }
        }
    }
}
