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
 * Horner scheme is an algorithm for the efficient evaluation of polynomials in monomial form.
 * It can also be seen as a fast algorithm for dividing a polynomial by a linear polynomial with Ruffini's rule.
 * <p/>
 * The polynomial remainder theorem says:
 * <pre><i>
 * P(x) = Q(x)(x - x<sub>0</sub>) + P(x<sub>0</sub>)
 * </i></pre>
 * We thus can compute the value of a polynomial <i>P(x)</i> at <i>x<sub>0</sub></i>,
 * i.e., <i>P(x<sub>0</sub>)</i>, and at the same time the quotient <i>Q(x)</i>.
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Horner_scheme">Wikipedia: Horner scheme</a>
 */
public class HornerScheme {

    private double remainder;
    private Polynomial quotient;
    private final Polynomial polynomial;
    private final double x;

    /**
     * Evaluate a polynomial at <i>x</i>.
     *
     * @param polynomial a polynomial
     * @param x          a point to evaluate the polynomial at
     */
    public HornerScheme(Polynomial polynomial, double x) {
        if (polynomial.degree() < 1) {
            throw new IllegalArgumentException("degree must be at least 1");
        }
        this.polynomial = polynomial;
        this.x = x;
        compute();
    }

    /**
     * Get the remainder, <i>P(x<sub>0</sub>)</i>.
     *
     * @return the remainder
     */
    public double remainder() {
        return remainder;
    }

    /**
     * Get the quotient, <i>Q(x)</i>.
     *
     * @return the quotient
     */
    public Polynomial quotient() {
        return quotient;
    }

    private void compute() {
        double[] p = polynomial.getCoefficients();
        double[] q = new double[p.length - 1]; // quotient has 1 degree less
        for (int j = 1; j < p.length; ++j) {
            p[j] = p[j - 1] * x + p[j];
        }
        remainder = p[polynomial.degree()];
        System.arraycopy(p, 0, q, 0, q.length);
        quotient = new Polynomial(q);
    }
}
