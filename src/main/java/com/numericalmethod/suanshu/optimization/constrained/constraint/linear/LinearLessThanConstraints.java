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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a collection of linear less-than-or-equal-to constraints.
 * <blockquote><pre><i>
 * A * x &le; b
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 */
public class LinearLessThanConstraints extends LinearConstraints implements LessThanConstraints {

    /**
     * Construct a collection of linear less-than or equal-to constraints.
     *
     * @param A the less-than inequality constraints
     * @param b the less-than inequality values
     */
    public LinearLessThanConstraints(Matrix A, Vector b) {
        super(A, b);
    }

    @Override
    public LinearGreaterThanConstraints toGreaterThanConstraints() {
        return new LinearGreaterThanConstraints(A().scaled(-1), b().scaled(-1));
    }

    /**
     * Given a collection of linear less-than-or-equal-to constraints as well as a collection of equality constraints,
     * find a feasible initial point that satisfy the constraints.
     * This implementation solves eq. 11.25 in the reference.
     * The first (n-1) entries consist of a feasible initial point.
     * The last entry is the single point perturbation.
     *
     * @param equal a collection of linear equality constraints
     * @return a feasible initial point, and the single point perturbation (in one vector)
     * @see "Andreas Antoniou, Wu-Sheng Lu, "Eq 11.25, Quadratic and Convex Programming," Practical Optimization: Algorithms and Engineering Applications."
     */
    public Vector getFeasibleInitialPoint(LinearEqualityConstraints equal) {
        return this.toGreaterThanConstraints().getFeasibleInitialPoint(equal);
    }

    /**
     * Given a collection of linear less-than-or-equal-to constraints,
     * find a feasible initial point that satisfy the constraints.
     * This implementation solves eq. 11.25 in the reference.
     * The first (n-1) entries consist of a feasible initial point.
     * The last entry is the single point perturbation.
     *
     * @return a feasible initial point, and the single point perturbation (in one vector)
     * @see "Andreas Antoniou, Wu-Sheng Lu, "Eq 11.25, Quadratic and Convex Programming," Practical Optimization: Algorithms and Engineering Applications."
     */
    public Vector getFeasibleInitialPoint() {
        return getFeasibleInitialPoint(null);
    }
}
