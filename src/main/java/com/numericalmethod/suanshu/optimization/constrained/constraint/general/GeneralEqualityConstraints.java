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
package com.numericalmethod.suanshu.optimization.constrained.constraint.general;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import java.util.Collection;

/**
 * This is the collection of equality constraints for an optimization problem.
 *
 * @author Haksun Li
 */
public class GeneralEqualityConstraints extends GeneralConstraints implements EqualityConstraints {

    /**
     * Construct an instance of equality constraints from a collection of real-valued functions.
     *
     * @param constraints the equality constraints
     */
    public GeneralEqualityConstraints(Collection<RealScalarFunction> constraints) {
        super(constraints);

        final RealScalarFunction f0 = getConstraints().get(0);
        SuanShuUtils.assertArgument(constraints.size() < f0.dimensionOfDomain(),
                                    "the number of equality constraints must be less than the number of variables");
    }

    /**
     * Construct an instance of equality constraints from an array of real-valued functions.
     *
     * @param constraints the equality constraints
     */
    public GeneralEqualityConstraints(RealScalarFunction... constraints) {
        super(constraints);
    }
}
