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
package com.numericalmethod.suanshu.optimization.constrained.constraint;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.List;

/**
 * These are the utility functions for manipulating {@link Constraints}.
 *
 * @author Haksun Li
 */
public class ConstraintsUtils {

    /**
     * Check if the constraints are satisfied.
     *
     * @param constraints the constraints
     * @param x           the value to evaluate the constraints at
     * @param epsilon     a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if the constraints are satisfied
     */
    public static boolean isSatisfied(Constraints constraints, Vector x, double epsilon) {
        final Vector fx = evaluate(constraints, x);

        //equality constraints
        if (EqualityConstraints.class.isAssignableFrom(constraints.getClass())) {
            return DoubleUtils.compare(fx.norm(), 0, epsilon) == 0;//TODO: make precision a parameter
        }

        //greater-than or equal constraints
        if (GreaterThanConstraints.class.isAssignableFrom(constraints.getClass())) {
            for (int i = 1; i <= fx.size(); ++i) {
                if (fx.get(i) < -epsilon) {// {@code true} if fx >= 0
                    return false;
                }
            }

            return true;
        }

        //less-than or equal constraints
        if (LessThanConstraints.class.isAssignableFrom(constraints.getClass())) {
            for (int i = 1; i <= fx.size(); ++i) {
                if (fx.get(i) > epsilon) {// {@code true} if fx <= 0
                    return false;
                }
            }

            return true;
        }

        //TODO: box constraints

        throw new RuntimeException("unrecognized constraint type");
    }

    /**
     * Check if the constraints are satisfied.
     *
     * @param constraints the constraints
     * @param x           the value to evaluate the constraints at
     * @return {@code true} if the constraints are satisfied
     */
    public static boolean isSatisfied(Constraints constraints, Vector x) {
        final double epsilon = SuanShuUtils.autoEpsilon(x.toArray());
        return isSatisfied(constraints, x, epsilon);
    }

    /**
     * Evaluate the constraints.
     *
     * @param constraints the constraints
     * @param x           the value to evaluate the constraints at
     * @return the constraint values
     */
    public static Vector evaluate(Constraints constraints, Vector x) {
        List<RealScalarFunction> f = constraints.getConstraints();
        Vector v = new DenseVector(f.size());
        for (int i = 0; i < f.size(); ++i) {
            v.set(i + 1, f.get(i).evaluate(x));
        }
        return v;
    }
}
