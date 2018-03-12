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
package com.numericalmethod.suanshu.analysis.function.rn2r1;

import com.numericalmethod.suanshu.analysis.function.SubFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Map;

/**
 * This constructs a {@link RealScalarFunction} from another {@link RealScalarFunction} by
 * restricting/fixing the values of a subset of variables.
 *
 * @author Haksun Li
 */
public class RealScalarSubFunction extends SubFunction<Double> implements RealScalarFunction {

    /**
     * Construct a scalar sub-function.
     *
     * @param f      the unrestricted/original function
     * @param fixing the values held fixed for a subset of variables
     */
    public RealScalarSubFunction(RealScalarFunction f, Map<Integer, Double> fixing) {
        super(f, fixing);
    }

    @Override
    public Double evaluate(Vector x) {
        Vector z = getAllParts(x, fixing);
        if (dimensionOfDomain() == 0) {
            z = getAllParts(new DenseVector(0), fixing);
        }

        return ((RealScalarFunction) f).evaluate(z);
    }

    /**
     * Evaluate the function <i>f</i> at <i>x</i>.
     *
     * @param x <i>x</i>
     * @return <i>f(x)</i>
     */
    public double evaluate(double x) {
        return evaluate(new DenseVector(x));
    }
}
