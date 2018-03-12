/*
 * Copyright (constraints) Numerical Method Inc.
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
package com.numericalmethod.suanshu.optimization.constrained.general.penaltymethod;

import com.numericalmethod.suanshu.optimization.constrained.constraint.ConstraintsUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.max;

/**
 * This penalty function sums up the squared costs penalties.
 * It applies to inequality constrained problems.
 *
 * @author Haksun Li
 * @see "R. Fletcher, "An ideal penalty function for constrained optimization," J. Inst. Maths Applns, 15, 319 - 342. 1975."
 */
public class Fletcher extends MultiplierPenalty {

    /**
     * Construct a Fletcher penalty function from a collection of inequality constraints.
     *
     * @param constraints a collection of inequality constraints
     * @param weights     the weights assigned to the constraints
     */
    public Fletcher(LessThanConstraints constraints, double[] weights) {
        super(constraints, weights);
    }

    /**
     * Construct a Fletcher penalty function from a collection of inequality constraints.
     * We assign the same weight to all constraints.
     *
     * @param constraints a collection of inequality constraints
     * @param weight      the same weight assigned to the constraints
     */
    public Fletcher(LessThanConstraints constraints, double weight) {
        super(constraints, weight);
    }

    /**
     * Construct a Fletcher penalty function from a collection of inequality constraints.
     * We assign the same default weight to all constraints.
     *
     * @param constraints a collection of inequality constraints
     */
    public Fletcher(LessThanConstraints constraints) {
        this(constraints, 1.0);
    }

    @Override
    public Double evaluate(Vector x) {
        Vector hx = ConstraintsUtils.evaluate(constraints, x);

        double penalty = 0;
        for (int i = 1; i <= hx.size(); ++i) {
            double hx_i = hx.get(i);
            penalty += weights[i - 1] * max(hx_i, 0) * max(hx_i, 0);//hx <= 0 is OK; otherwise a penalty
        }

        penalty /= 2.;
        return penalty;
    }
}
