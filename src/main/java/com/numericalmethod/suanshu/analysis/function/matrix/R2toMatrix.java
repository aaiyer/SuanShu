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
 * This is a function that maps from <i>R<sup>2</sup></i> to a Matrix space.
 * It takes two real arguments and outputs one matrix value. That is,
 * /[
 * f(x_1, x_2) = A
 * /]
 *
 * @author Haksun Li
 */
public abstract class R2toMatrix implements RntoMatrix {

    @Override
    public int dimensionOfDomain() {
        return 2;
    }

    @Override
    public int dimensionOfRange() {
        return 1;
    }

    @Override
    public Matrix evaluate(Vector x) {
        if (x.size() != 2) {
            throw new EvaluationException("this is a bivariate function");
        }

        return evaluate(x.get(1), x.get(2));
    }

    /**
     * Evaluate <i>f(x<sub>1</sub>, x<sub>2</sub>) = A</i>.
     *
     * @param x1 {@code x1}
     * @param x2 {@code x2}
     * @return {@code f(x1, x2)}
     */
    public abstract Matrix evaluate(double x1, double x2);
}
