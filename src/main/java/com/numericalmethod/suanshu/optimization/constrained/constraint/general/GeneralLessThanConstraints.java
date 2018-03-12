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
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the collection of less-than or equal-to constraints for an optimization problem.
 *
 * @author Haksun Li
 */
public class GeneralLessThanConstraints extends GeneralConstraints implements LessThanConstraints {

    /**
     * Construct an instance of less-than or equal-to inequality constraints from a collection of real-valued functions.
     *
     * @param constraints the less-than or equal-to inequality constraints
     */
    public GeneralLessThanConstraints(Collection<RealScalarFunction> constraints) {
        super(constraints);
    }

    /**
     * Construct an instance of less-than or equal-to inequality constraints from an array of real-valued functions.
     *
     * @param constraints the less-than or equal-to inequality constraints
     */
    public GeneralLessThanConstraints(RealScalarFunction... constraints) {
        super(constraints);
    }

    @Override
    public GeneralGreaterThanConstraints toGreaterThanConstraints() {
        ArrayList<RealScalarFunction> lessThan = getConstraints();

        ArrayList<RealScalarFunction> greaterThan = new ArrayList<RealScalarFunction>(lessThan.size());
        for (RealScalarFunction l : lessThan) {
            final RealScalarFunction f = l;// make the complier happy
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
            greaterThan.add(nf);
        }

        return new GeneralGreaterThanConstraints(greaterThan);
    }
}
