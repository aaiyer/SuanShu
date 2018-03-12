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

import com.numericalmethod.suanshu.optimization.constrained.constraint.Constraints;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * A multiplier penalty function allows different weights to be assigned to the constraints.
 *
 * @author Haksun Li
 * @see "R. Fletcher, "Section 12.2. Multiplier PenaltyFunction Function," Practical Method of Optimization Vol. 2. Constrained Optimization. Wiley, 1981."
 */
public abstract class MultiplierPenalty extends PenaltyFunction {

    /**
     * the weights for the constraints
     */
    protected final double[] weights;
    /**
     * the constraint/cost functions
     */
    protected final Constraints constraints;

    /**
     * Construct a multiplier penalty function from a collection of constraints.
     *
     * @param constraints a collection of constraints
     * @param weights     the weights assigned to the constraints
     */
    public MultiplierPenalty(Constraints constraints, double[] weights) {
        this.constraints = constraints;
        this.weights = Arrays.copyOf(weights, constraints.getConstraints().size());
    }

    /**
     * Construct a multiplier penalty function from a collection of constraints.
     * We assign the same weight to all constraints.
     *
     * @param constraints a collection of equality constraints
     * @param weight      the same weight assigned to all constraints
     */
    public MultiplierPenalty(Constraints constraints, double weight) {
        this(constraints, new DenseVector(constraints.getConstraints().size(), weight).toArray());
    }

    /**
     * Construct a multiplier penalty function from a collection of constraints.
     * We assign the same default weight, 1.0, to all constraints.
     *
     * @param constraints a collection of constraints
     */
    public MultiplierPenalty(Constraints constraints) {
        this(constraints, 1.0);
    }

    @Override
    public int dimensionOfDomain() {
        return constraints.getConstraints().get(0).dimensionOfDomain();
    }
}
