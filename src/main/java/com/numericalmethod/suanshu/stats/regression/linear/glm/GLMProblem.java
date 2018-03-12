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
package com.numericalmethod.suanshu.stats.regression.linear.glm;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Family;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This class represents a Generalized Linear regression problem.
 *
 * @author Haksun Li
 */
public class GLMProblem extends com.numericalmethod.suanshu.stats.regression.linear.LMProblem {

    /**
     * the exponential family distribution for the mean
     */
    public final Family family;

    /**
     * Construct a GLM problem.
     * 
     * @param y the dependent variables
     * @param X the factors
     * @param addIntercept {@code true} if to add an intercept term to the linear regression
     * @param family the exponential family distribution for the mean
     */
    public GLMProblem(Vector y, Matrix X, boolean addIntercept, Family family) {
        super(y, X, addIntercept);
        this.family = family;
    }

    /**
     * Construct a GLM problem from a linear regression problem.
     * 
     * @param problem a linear regression problem
     * @param family the exponential family distribution for the mean
     */
    public GLMProblem(com.numericalmethod.suanshu.stats.regression.linear.LMProblem problem, Family family) {
        super(problem);
        this.family = family;
    }
}
