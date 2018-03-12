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
package com.numericalmethod.suanshu.analysis.function;

import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;

/**
 * The mathematical concept of a function expresses the idea that one quantity (the argument of the function, also known as the input) completely determines another quantity (the value, or output).
 * The argument (domain) and value (range) may be real numbers (as in {@link RealScalarFunction}), but they can also be elements from any given sets.
 *
 * @param <D> the domain of a function
 * @param <R> the range of a function
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Function_(mathematics)">Wikipedia: Function (mathematics)</a>
 */
public interface Function<D, R> {

    /**
     * This is the {@link RuntimeException} thrown when it fails to evaluate an expression.
     * E.g., when there are not two arguments passed in a {@link BivariateRealFunction}.
     */
    public static class EvaluationException extends RuntimeException {//TODO: make Exception (non-runtime)?

        private static final long serialVersionUID = 1L;

        /**
         * Constructs an {@code EvaluationException} with the specified detail message.
         *
         * @param msg the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
         */
        public EvaluationException(String msg) {
            super(msg);
        }
    }

    /**
     * Evaluate the function <i>f</i> at <i>x</i>, where <i>x</i> is from the domain.
     *
     * @param x <i>x</i>
     * @return <i>f(x)</i>
     */
    public R evaluate(D x);

    /**
     * Get the number of variables the function has.
     * For example, for a univariate function, the domain dimension is 1; for a bivariate function, the domain dimension is 2.
     *
     * @return the number of variables
     */
    public int dimensionOfDomain();

    /**
     * Get the dimension of the range space of the function.
     * For example, for a <i>R<sup>n</sup>->R<sup>m</sup></i> function, the dimension of the range is <i>m</i>.
     *
     * @return the dimension of the range
     */
    public int dimensionOfRange();
}
