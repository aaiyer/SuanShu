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
import com.numericalmethod.suanshu.optimization.constrained.constraint.GreaterThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the collection of greater-than-or-equal-to constraints for an optimization problem.
 *
 * @author Haksun Li
 */
public class GeneralGreaterThanConstraints extends GeneralConstraints implements GreaterThanConstraints {

    /**
     * Construct an instance of greater-than-or-equal-to inequality constraints from a collection of real-valued functions.
     *
     * @param constraints the greater-than-or-equal-to inequality constraints
     */
    public GeneralGreaterThanConstraints(Collection<RealScalarFunction> constraints) {
        super(constraints);
    }

    /**
     * Construct an instance of greater-than-or-equal-to inequality constraints from an array of real-valued functions.
     *
     * @param constraints the greater-than-or-equal-to inequality constraints
     */
    public GeneralGreaterThanConstraints(RealScalarFunction... constraints) {
        super(constraints);
    }

    @Override
    public GeneralLessThanConstraints toLessThanConstraints() {
        ArrayList<RealScalarFunction> greaterThan = getConstraints();

        ArrayList<RealScalarFunction> lessThan = new ArrayList<RealScalarFunction>(greaterThan.size());
        for (RealScalarFunction g : greaterThan) {
            final RealScalarFunction f = g;// make the complier happy
            RealScalarFunction nf = new RealScalarFunction() {

                @Override
                public Double evaluate(Vector x) {
                    return -f.evaluate(x);
                }

                @Override
                public int dimensionOfDomain() {
                    return f.dimensionOfDomain();
                }

                @Override
                public int dimensionOfRange() {
                    return f.dimensionOfRange();
                }
            };
            lessThan.add(nf);
        }

        return new GeneralLessThanConstraints(lessThan);
    }
}
