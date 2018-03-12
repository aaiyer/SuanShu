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

import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A bivariate real function takes two real arguments and outputs one real value.
 * That is, <i>y = f(x<sub>1</sub>,x<sub>2</sub>)</i>.
 *
 * @author Haksun Li
 */
public abstract class BivariateRealFunction implements RealScalarFunction {

    @Override
    public int dimensionOfDomain() {
        return 2;
    }

    @Override
    public int dimensionOfRange() {
        return 1;
    }

    @Override
    public Double evaluate(Vector x) {
        if (x.size() != 2) {
            throw new EvaluationException("this is a bivariate function");
        }
        return evaluate(x.get(1), x.get(2));
    }

    /**
     * Evaluate <i>y = f(x<sub>1</sub>,x<sub>2</sub>)</i>.
     *
     * @param x1 <i>x<sub>1</sub></i>
     * @param x2 <i>x<sub>2</sub></i>
     * @return <i>f(x<sub>1</sub>, x<sub>2</sub>)</i>
     */
    public abstract double evaluate(double x1, double x2);
}
