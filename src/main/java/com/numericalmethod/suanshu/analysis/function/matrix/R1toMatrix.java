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
package com.numericalmethod.suanshu.analysis.function.matrix;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a function that maps from <i>R<sup>1</sup></i> to a Matrix space.
 * It takes one real argument and outputs one matrix value. That is,
 * /[
 * f(x) = A
 * /]
 *
 * @author Haksun Li
 */
public abstract class R1toMatrix implements RntoMatrix {

    @Override
    public int dimensionOfDomain() {
        return 1;
    }

    @Override
    public int dimensionOfRange() {
        return 1;
    }

    @Override
    public Matrix evaluate(Vector x) {//TODO: make ImmutableMatrix?
        if (x.size() != 1) {
            throw new EvaluationException("this is a univariate function");
        }

        return evaluate(x.get(1));
    }

    /**
     * Evaluate <i>f(x) = A</i>.
     *
     * @param x <i>x</i>
     * @return <i>f(x)</i>
     */
    public abstract Matrix evaluate(double x);//TODO: make ImmutableMatrix?
}
