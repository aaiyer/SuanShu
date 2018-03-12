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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.mathstructure.Ring;
import com.numericalmethod.suanshu.mathstructure.VectorSpace;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;
import com.numericalmethod.suanshu.number.Real;
import com.numericalmethod.suanshu.number.complex.Complex;
import com.numericalmethod.suanshu.number.complex.ElementaryFunction;
import java.util.Arrays;

/**
 * A polynomial is a {@link UnivariateRealFunction} that represents a finite length expression constructed from variables and constants,
 * using the operations of addition, subtraction, multiplication, and constant non-negative whole number exponents.
 * Specifically, it has the form
 * \[
 * p(x) = a_0x^n + a_1x^{n-1} + ... + a_{n-1}x + a_n
 * \]
 * This implementation is immutable.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Polynomial">Wikipedia: Polynomial</a>
 */
public class Polynomial extends UnivariateRealFunction implements Ring<Polynomial>, VectorSpace<Polynomial, Real> {

    /** a polynomial representing <i>0</i> */
    public static final Polynomial ZERO = new Polynomial(0);
    /** a polynomial representing <i>1</i> */
    public static final Polynomial ONE = new Polynomial(1);
    private int degree;
    private final double coefficients[];

    /**
     * Construct a polynomial from an array of coefficients.
     * The first/0-th entry corresponds to the <i>x<sup>n</sup></i> term.
     * The last/n-th entry corresponds to the constant term.
     * The degree of the polynomial is <i>n</i>, the array length minus 1.
     * <p/>
     * For example,
     * <blockquote><code>
     * new Polynomial(1, -2, 3, 2)
     * </code></blockquote>
     * creates an instance of {@code Polynomial} representing <i>x<sup>3</sup> - 2x<sup>2</sup> + 3x + 2</i>.
     *
     * @param coefficients the polynomial coefficients
     */
    public Polynomial(double... coefficients) {
        degree = coefficients.length - 1;
        for (int i = 0; i < coefficients.length - 1; ++i) {
            if (coefficients[i] != 0) {
                break;
            }
            --degree;//search for a non-zero leading coefficient
        }

        this.coefficients = Arrays.copyOfRange(coefficients, coefficients.length - degree - 1, coefficients.length);
    }

    /**
     * Copy constructor.
     *
     * @param that a polynomial
     */
    public Polynomial(Polynomial that) {
        this(that.getCoefficients());
    }

    /**
     * Get the degree of this polynomial.
     * It is equal to the largest exponent of the variable.
     * For example, <i>x<sup>4</sup> + 1</i> has a degree of 4.
     *
     * @return the polynomial degree
     */
    public int degree() {
        return degree;
    }

    /**
     * Get a copy of the polynomial coefficients.
     * In general, {@code coefficients[i]} is the coefficient of <i>x<sup>n-i</sup></i>, where <i>n</i> is the polynomial degree.
     * Specifically, {@code coefficients[0]} is the leading coefficient and {@code coefficients[n]} the constant term.
     *
     * @return a copy of the polynomial coefficients
     */
    public double[] getCoefficients() {
        return Arrays.copyOf(coefficients, coefficients.length);
    }

    /**
     * Get <i>a<sub>n-i</sub></i>, the coefficient of <i>x<sup>n-i</sup></i>.
     *
     * @param i the <i>i-th</i> coefficient in this polynomial, counting from 0
     * @return <i>a<sub>n-i</sub></i>
     * @see #getCoefficients()
     */
    public double getCoefficient(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("coefficient index counts from 0, the highest order term");
        }
        return (i < coefficients.length) ? coefficients[i] : 0;
    }

    /**
     * Get the normalized version of this polynomial so the leading coefficient is 1.
     *
     * @return a scaled version of the polynomial that has a leading coefficient 1
     */
    public Polynomial getNormalization() {
        double[] coeff = Arrays.copyOf(this.coefficients, this.coefficients.length);
        double an = coeff[0];
        for (int i = 1; i < coeff.length; ++i) {
            coeff[i] /= an;
        }
        coeff[0] = 1; // avoid numerical rounding of division

        return new Polynomial(coeff);
    }

    /**
     * Evaluate this polynomial at <i>x</i>.
     *
     * @param x the argument
     * @return <i>p(x)</i>
     */
    public Complex evaluate(Number x) {
        if (x instanceof Complex) {
            return evaluate((Complex) x);
        }

        double px = evaluate(x.doubleValue());
        return new Complex(px, 0);
    }

    /**
     * Evaluate this polynomial at <i>x</i>.
     *
     * @param x the argument
     * @return <i>p(x)</i>
     */
    @Override
    public double evaluate(double x) {
        if (degree == 0) { // a constant term only
            return coefficients[0];
        }

        HornerScheme horner = new HornerScheme(this, x);
        return horner.remainder();
    }

    /**
     * Evaluate this polynomial at <i>x</i>.
     *
     * @param z the argument
     * @return <i>p(x)</i>
     */
    public Complex evaluate(Complex z) {
        Complex result = z.ZERO();
        for (int i = 0; i <= this.degree; ++i) {
            result = result.add(ElementaryFunction.pow(z, new Complex(this.degree - i, 0)).
                    multiply(new Complex(coefficients[i], 0)));
        }
        return result;
    }

    @Override
    public Polynomial add(Polynomial that) {
        double[] longer, shorter;
        if (this.degree > that.degree) {
            longer = this.coefficients;
            shorter = that.coefficients;
        } else {
            longer = that.coefficients;
            shorter = this.coefficients;
        }
        int dLength = longer.length - shorter.length;

        double[] coeff1 = new double[longer.length];
        System.arraycopy(longer, 0, coeff1, 0, dLength);
        for (int i = 0; i < shorter.length; ++i) {
            coeff1[dLength + i] = longer[dLength + i] + shorter[i];
        }
        return new Polynomial(coeff1);
    }

    @Override
    public Polynomial minus(Polynomial that) {
        return this.add(that.opposite());
    }

    @Override
    public Polynomial multiply(Polynomial that) {
        int degree1 = this.degree + that.degree;
        double[] coeff1 = new double[degree1 + 1];

        for (int i = 0; i < coeff1.length; ++i) {
            coeff1[i] = 0.0;
            for (int k = 0; k <= i; ++k) {
                coeff1[i] += that.getCoefficient(k) * this.getCoefficient(i - k);
            }
        }
        return new Polynomial(coeff1);
    }

    @Override
    public Polynomial scaled(Real c) {
        return scaled(c.doubleValue());
    }

    public Polynomial scaled(double c) {
        double[] coeff1 = new double[coefficients.length];
        for (int i = 0; i < coeff1.length; ++i) {
            coeff1[i] = c * coefficients[i];
        }
        return new Polynomial(coeff1);
    }

    @Override
    public Polynomial opposite() {
        double[] coeff1 = new double[coefficients.length];
        for (int i = 0; i <= degree; ++i) {
            coeff1[i] = -coefficients[i];
        }

        return new Polynomial(coeff1);
    }

    @Override
    public Polynomial ZERO() {
        return Polynomial.ZERO;
    }

    @Override
    public Polynomial ONE() {
        return Polynomial.ONE;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i <= degree; ++i) {
            if (coefficients[i] != 0) {
                if (i > 0 && coefficients[i] >= 0) {
                    buffer.append("+");//no need to do this for -ve. coefficients
                }

                buffer.append(String.format("%.2f", coefficients[i]));

                if (i != degree) {
                    buffer.append(String.format("(x^%d)", (this.degree - i)));
                }
            }
        }

        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }

        if (!(obj instanceof Polynomial)) {
            return false;
        }

        final Polynomial that = (Polynomial) obj;
        if (this.degree != that.degree) {
            return false;
        }

        if (this.coefficients != that.coefficients
                && (this.coefficients == null || !equal(this.coefficients, that.coefficients, 0))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.degree;
        hash = 71 * hash + (this.coefficients != null ? this.coefficients.hashCode() : 0);
        return hash;
    }
}
