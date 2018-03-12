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
 * Divide a polynomial <i>P(x)</i> by a quadratic monomial <i>(x<sup>2</sup> + ux + v)</i>
 * to give the quotient <i>Q(x)</i> and the remainder <i>(b * (x + u) + a)</i>.
 * The polynomial remainder theorem says:
 * <pre><i>
 * P(x) = (x<sup>2</sup> + ux + v) Q(x) + (b * (x + u) + a)
 * </i></pre>
 *
 * @author Ken Yiu
 */
public class QuadraticSyntheticDivision {

    private Polynomial quotient = null;
    private double a;
    private double b;
    private final QuadraticMonomial quadratic;
    private final Polynomial polynomial;

    /**
     * Divide a polynomial by a quadratic monomial.
     *
     * @param polynomial a polynomial
     * @param quadratic  a quadratic monomial
     */
    public QuadraticSyntheticDivision(Polynomial polynomial, QuadraticMonomial quadratic) {
        if (polynomial.degree() < 2) {
            throw new IllegalArgumentException("degree must be at least 2");
        }
        this.polynomial = polynomial;
        this.quadratic = quadratic;
        divide();
    }

    /**
     * Get the quotient <i>Q(x)</i>.
     *
     * @return <i>Q(x)</i>
     */
    public Polynomial quotient() {
        return quotient;
    }

    /**
     * Get <i>a</i> as in the remainder <i>(b * (x + u) + a)</i>.
     *
     * @return <i>a</i>
     */
    public double a() {
        return a;
    }

    /**
     * Get <i>b</i> as in the remainder <i>(b * (x + u) + a)</i>.
     *
     * @return <i>b</i>
     */
    public double b() {
        return b;
    }

    private void divide() {
        double[] p = polynomial.getCoefficients();
        double[] q = new double[p.length - 2]; // the quotient has 2 degree less

        double b_ = p[0];
        double a_ = p[1] - (b_ * quadratic.u());
        p[1] = a_;

        for (int j = 2; j < p.length; ++j) {
            p[j] = p[j] - b_ * quadratic.v() - a_ * quadratic.u();
            b_ = a_;
            a_ = p[j];
        }

        System.arraycopy(p, 0, q, 0, q.length);
        quotient = new Polynomial(q);
        this.b = b_;
        this.a = a_;
    }
}
