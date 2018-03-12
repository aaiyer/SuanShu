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
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.optimization.constrained.constraint.Constraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * The real-valued constraints define the domain (feasible regions) for a real-valued objective function in a
 * constrained optimization problem. The type of constraints can be equality (=), greater-than-or-equal-to (&ge;),
 * and less-than or equal-to (&le;).
 *
 * @author Haksun Li
 */
public abstract class GeneralConstraints implements Constraints {

    /** the constraints */
    private final ArrayList<RealScalarFunction> constraints = new ArrayList<RealScalarFunction>();
    private final int dim;

    /**
     * Construct an instance of constraints from a collection of real-valued functions.
     *
     * @param constraints the constraints
     */
    public GeneralConstraints(Collection<RealScalarFunction> constraints) {
        assertArgument(!constraints.isEmpty(), "constraints cannot be empty");

        final RealScalarFunction f0 = constraints.iterator().next();
        assertArgument(f0 != null, "found a <code>null</code> constraint");

        this.dim = f0.dimensionOfDomain();
        for (RealScalarFunction constraint : constraints) {
            assertArgument(constraint != null, "found a <code>null</code> constraint");
            assertArgument(this.dim == constraint.dimensionOfDomain(), "all constraints must have the same domain dimension");
        }

        this.constraints.addAll(constraints);
    }

    /**
     * Construct an instance of constraints from an array of real-valued functions.
     *
     * @param constraints the constraints
     */
    public GeneralConstraints(RealScalarFunction... constraints) {
        this(Arrays.asList(constraints));
    }

    /**
     * Get the constraints.
     *
     * @return the constraints
     */
    @Override
    public ArrayList<RealScalarFunction> getConstraints() {
        ArrayList<RealScalarFunction> copy = new ArrayList<RealScalarFunction>();
        copy.addAll(constraints);
        return copy;
    }

    @Override
    public int dimension() {
        return dim;
    }

    @Override
    public int size() {
        return constraints.get(0).dimensionOfRange();
    }
}
