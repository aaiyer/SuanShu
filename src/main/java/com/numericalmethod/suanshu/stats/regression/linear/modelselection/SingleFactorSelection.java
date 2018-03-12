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
import static com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix.columns;
import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GLMProblem;
import com.numericalmethod.suanshu.stats.regression.linear.glm.GeneralizedLinearModel;
import java.util.Arrays;

/**
 * Given a set of observations {y, X}, we would like to construct a GLM for it.
 * One way to do it is to examine each factor (a column in X) one by one,
 * and include in the getModel the significant ones,
 * i.e., those with z-value bigger than some critical value.
 *
 * @author Chun Yip Yau
 */
abstract class SingleFactorSelection {//TODO: R package leaps

    /**
     * We throw a <tt>ModelNotFound</tt> exception when we fail to construct a getModel to explain the data.
     */
    public static class ModelNotFound extends RuntimeException {

        private static final long serialVersionUID = 1L;

        /**
         * Construct a <tt>ModelNotFound</tt> exception with an error message.
         *
         * @param msg the error message
         */
        public ModelNotFound(String msg) {
            super(msg);
        }
    }

    /**
     * This is an auxiliary class to keep track of which getFactors/variables/regressors are selected in the getModel.
     */
    class Indices {

        /**
         * the GLM problem to be solved
         */
        protected final GLMProblem problem;
        /**
         * indicate whether a factor/variable/regressor is selected in the getModel
         */
        protected int[] flags;//+ve: selected; 0: dropped/not selected
        /**
         * the subset of getFactors/variables/regressors selected
         */
        protected int[] subset;

        /**
         * Construct an instance of <tt>Indices</tt> for a GLM problem.
         *
         * @param problem a GLM problem
         */
        Indices(GLMProblem problem) {
            this.problem = problem;
        }

        /**
         * Check whether a particular index is selected in the getModel.
         *
         * <p>
         * The indices count from 1.
         *
         * @param factorIndex the index to a factor
         * @return {@code true} if the particular index is selected in the getModel
         */
        boolean isSelected(int factorIndex) {//count from 1 (because 0 in flats means dropped)
            return flags[factorIndex - 1] > 0;
        }

        /**
         * Construct a covariates subset.
         *
         * @return the columns corresponding to the selected variables
         */
        Matrix subA() {
            //the selected variable indices
            subset = R.select(flags,
                              new R.which() {

                @Override
                public boolean isTrue(double x, int index) {
                    return x > 0;
                }
            });

            Matrix subA = subset.length > 0 ? columns(problem.A, subset) : null;
            return subA;
        }
    }

    /**
     * the GLM problem to be solved
     */
    public final GLMProblem problem;
    /**
     * a critical value
     *
     * <p>
     * A factor is considered significant if its z-value is bigger than some critical value.
     * We add the significant getFactors to the getModel.
     */
    public final double criticalValue;
    Indices indices;

    /**
     * Construct automatically a GLM getModel to explain the observations.
     *
     * @param problem      a GLM problem
     * @param significance a critical value to determine whether a factor is significant (to be included in the getModel)
     */
    public SingleFactorSelection(GLMProblem problem, double significance) {
        assertArgument(significance < 0.5, "significant level should be less than 0.5");

        this.problem = problem;

        //the criticle value for the z-test
        criticalValue = new NormalDistribution(0, 1).quantile(1 - significance / 2);
    }

    /**
     * Get the constructed getModel.
     *
     * @return a GLM getModel
     */
    public GeneralizedLinearModel getModel() {
        Matrix subA = indices.subA();

        if (subA == null) {
            throw new ModelNotFound("no factor selected");
        }

        GLMProblem p = new GLMProblem(problem.y, subA, problem.addIntercept, problem.family);
        GeneralizedLinearModel model = new GeneralizedLinearModel(p);

        return model;
    }

    /**
     * Get a copy of the factor flags.
     *
     * <p>
     * A positive value indicates that the factor is selected;
     * a 0 value indicates that the factor is dropped or not selected.
     *
     * @return a copy of the getFactors/indices
     */
    int[] getFactors() {
        return Arrays.copyOf(indices.flags, indices.flags.length);
    }
}
