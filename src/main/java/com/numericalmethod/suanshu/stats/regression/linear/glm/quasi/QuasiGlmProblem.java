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
package com.numericalmethod.suanshu.stats.regression.linear.glm.quasi;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.quasi.family.QuasiFamily;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This class represents a quasi Generalized Linear regression problem.
 *
 * @author Haksun Li
 */
public class QuasiGlmProblem extends com.numericalmethod.suanshu.stats.regression.linear.glm.GLMProblem {

    /**
     * the quasi-quasiFamily distribution
     */
    public final QuasiFamily quasiFamily;

    /**
     * Construct a quasi GLM problem.
     *
     * @param y the dependent variables
     * @param X the factors
     * @param addIntercept {@code true} if to add an intercept term to the linear regression
     * @param quasiFamily the exponential family distribution for the mean with a quasi-likelihood function
     */
    public QuasiGlmProblem(DenseVector y, Matrix X, boolean addIntercept, QuasiFamily quasiFamily) {
        super(y, X, addIntercept, quasiFamily.toFamily());
        this.quasiFamily = quasiFamily;
    }

    /**
     * Construct a quasi GLM problem from a linear regression problem.
     *
     * @param problem a linear regression problem
     * @param quasiFamily the exponential family distribution for the mean with a quasi-likelihood function
     */
    public QuasiGlmProblem(com.numericalmethod.suanshu.stats.regression.linear.LMProblem problem, QuasiFamily quasiFamily) {
        super(problem, quasiFamily.toFamily());
        this.quasiFamily = quasiFamily;
    }
}
