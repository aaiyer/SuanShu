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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GeneralizedLinearModel;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GLMProblem;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 * To construct a GLM getModel for a set of observations using the backward selection method,
 * we first assume that all getFactors are included in the getModel.
 * Iteratively, we drop the least significant factor until all remaining getFactors are significant.
 *
 * @author Chun Yip Yau
 */
public class Backward extends SingleFactorSelection {

    private class Indices extends SingleFactorSelection.Indices {

        private Indices(GLMProblem problem) {
            super(problem);
            this.flags = R.seq(1, problem.nExogenousFactors());
        }

        /**
         * Drop the least significant factor.
         *
         * @param index
         */
        private void drop(int index) {
            flags[subset[index] - 1] = 0;
        }
    }

    /**
     * Construct automatically a GLM getModel using the backward selection method.
     * 
     * @param problem a GLM problem
     * @param significance a critical value to determine whether a factor is significant (to be included in the getModel)
     */
    public Backward(GLMProblem problem, double significance) {
        super(problem, significance);
        indices = new Indices(problem);

        run();
    }

    /**
     * an implementation of the backward selection method
     */
    private void run() {
        for (Matrix subA = indices.subA(); subA != null; subA = indices.subA()) {
            GLMProblem trial = new GLMProblem(problem.y, subA, problem.addIntercept, problem.family);
            GeneralizedLinearModel glm = new GeneralizedLinearModel(trial);
            double[] z = glm.beta.z.toArray();

            //select the most insignificant factor
            int insignificantFactor = minIndex(true, 0, subA.nCols(), abs(z));//we ignore the last intercept column

            /*
             * For backward selection, we want to drop the insignificant getFactors (those with big p-value).
             * A factor is insignificant, e.g., for 0.05 significant level, if either 1) p-value > 0.05 or 2) z-value < critical value.
             */
            double zInsignificant = Math.abs(z[insignificantFactor]);
            if (zInsignificant < criticalValue) {
                ((Indices) indices).drop(insignificantFactor);//drop the least significant factor and continue
            } else {
                break;//stop
            }
        }
    }
}
