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

import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Projection creates a real-valued function {@link RealScalarFunction} from a vector-valued function {@link RealVectorFunction} by
 * taking only one of its coordinate components in the vector output.
 * For example, suppose we have a \(R^n \rightarrow R^m\) function, \(f_1 = [y_1, y_2, y_3]'\).
 * We construct a \(R^n \rightarrow R\) function by taking its second coordinate.
 * That is, \(f_2 = y_2\).
 *
 * @author Haksun Li
 */
public class Projection implements RealScalarFunction {

    /** the original \(R^n \rightarrow R^m\) function */
    private final RealVectorFunction f;
    /** the dimension/coordinate to take from the output of <i>f</i>; counts from 1 */
    private final int dimension;

    /**
     * Construct a \(R^n \rightarrow R\) projection from a \(R^n \rightarrow R^m\) function <i>f</i>.
     *
     * @param f         a vector-valued function
     * @param dimension the dimension/coordinate of the <i>f</i> value to output
     */
    public Projection(RealVectorFunction f, int dimension) {
        this.f = f;
        this.dimension = dimension;
    }

    @Override
    public Double evaluate(Vector x) {
        Vector fx = f.evaluate(x);
        return fx.get(dimension);
    }

    @Override
    public int dimensionOfDomain() {
        return f.dimensionOfDomain();
    }

    @Override
    public int dimensionOfRange() {
        return 1;
    }
}
