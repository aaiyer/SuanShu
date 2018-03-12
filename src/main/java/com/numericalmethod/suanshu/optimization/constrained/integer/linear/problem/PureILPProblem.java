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
package com.numericalmethod.suanshu.optimization.constrained.integer.linear.problem;

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.BoxConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a pure integer linear programming problem, in which all variables are integral.
 *
 * @author Haksun Li
 */
public class PureILPProblem extends ILPProblemImpl1 {

    /**
     * Construct a pure ILP problem.
     *
     * @param cost    the linear objective function
     * @param greater the greater-than-or-equal-to constraints
     * @param less    the less-than-or-equal-to constraints
     * @param equal   the equality constraints
     * @param bounds  the box constraints
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public PureILPProblem(
            Vector cost,
            LinearGreaterThanConstraints greater,
            LinearLessThanConstraints less,
            LinearEqualityConstraints equal,
            BoxConstraints bounds,
            double epsilon) {
        super(cost, greater, less, equal, bounds,
              R.seq(1, cost.size()),
              epsilon);
    }
}
