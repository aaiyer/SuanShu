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
package com.numericalmethod.suanshu.stats.regression.linear;

import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The residual of a sample is the difference between the sample and the estimated function (fitted) value.
 * 
 * @author Haksun Li
 *
 * @see <a href="http://en.wikipedia.org/wiki/Errors_and_residuals_in_statistics">Wikipedia: Errors and residuals in statistics</a>
 */
public abstract class Residuals {

    /**
     * the linear regression problem to be solved
     */
    public final LMProblem problem;
    /**
     * the fitted values, y^
     */
    public final ImmutableVector fitted;
    /**
     * the residuals, ε
     */
    public final ImmutableVector residuals;

    /**
     * Create an instance of <tt>Residuals</tt> for a linear regression problem.
     * 
     * @param problem the linear regression problem to be solved
     * @param fitted the fitted values, y^
     */
    public Residuals(LMProblem problem, Vector fitted) {
        this.problem = problem;
        this.fitted = new ImmutableVector(fitted);
        this.residuals = new ImmutableVector(problem.y.minus(fitted));
    }
}
