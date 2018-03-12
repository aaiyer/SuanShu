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
package com.numericalmethod.suanshu.analysis.function.polynomial;

/**
 * A quadratic monomial has this form: <i>x<sup>2</sup> + ux + v</i>.
 *
 * @author Haksun Li
 */
public class QuadraticMonomial extends Polynomial {

    /**
     * Construct a quadratic monomial.
     *
     * @param u <i>u</i> as in <i>(x<sup>2</sup> + ux + v)</i>
     * @param v <i>v</i> as in <i>(x<sup>2</sup> + ux + v)</i>
     */
    public QuadraticMonomial(double u, double v) {
        super(1., u, v);
    }

    /**
     * Get <i>u</i> as in <i>(x<sup>2</sup> + ux + v)</i>.
     *
     * @return <i>u</i>
     */
    public double u() {
        return getCoefficient(1);
    }

    /**
     * Get <i>v</i> as in <i>(x<sup>2</sup> + ux + v)</i>.
     *
     * @return <i>v</i>
     */
    public double v() {
        return getCoefficient(2);
    }
}
