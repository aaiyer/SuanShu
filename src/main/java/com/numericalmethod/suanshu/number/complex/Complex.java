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
package com.numericalmethod.suanshu.number.complex;

import com.numericalmethod.suanshu.mathstructure.Field;
import com.numericalmethod.suanshu.mathstructure.Field.InverseNonExistent;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.NumberUtils;

/**
 * A complex number is a number consisting of a real number part and an imaginary number part.
 * It is normally written in the form <i>a + bi</i>, where <i>a</i> and <i>b</i> are real numbers,
 * and <i>i</i> is the square root of minus one.
 * <p/>
 * This class is immutable.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Complex_number">Wikipedia: Complex number</a>
 */
public class Complex extends Number implements Field<Complex>, NumberUtils.Comparable<Complex> {

    /**
     * a number representing <i>0.0 + 1.0i</i>, the square root of <i>-1</i>
     */
    public static final Complex I = new Complex(0.0, 1.0);
    /**
     * a number representing <i>0.0 + 0.0i</i>
     */
    public static final Complex ZERO = new Complex(0.0, 0.0);
    /**
     * a number representing <i>1.0 + 0.0i</i>
     */
    public static final Complex ONE = new Complex(1.0, 0.0);
    /**
     * a number representing <i>+∞ + ∞i</i>
     */
    public static final Complex POSITIVE_INFINITY = new Complex(java.lang.Double.POSITIVE_INFINITY, java.lang.Double.POSITIVE_INFINITY);
    /**
     * a number representing <i>-∞ + -∞i</i>
     */
    public static final Complex NEGATIVE_INFINITY = new Complex(java.lang.Double.NEGATIVE_INFINITY, java.lang.Double.NEGATIVE_INFINITY);
    /**
     * a number representing the complex Not-a-Number ({@code NaN})
     */
    public static final Complex NaN = new Complex(java.lang.Double.NaN, java.lang.Double.NaN);
    /**
     * the real part of this complex number
     */
    private final double real;
    /**
     * the imaginary part of this complex number
     */
    private final double imaginary;
    private static final long serialVersionUID = 1L;

    /**
     * Construct a complex number from the real and imaginary parts.
     *
     * @param a the real part
     * @param b the imaginary part
     */
    public Complex(double a, double b) {
        if (java.lang.Double.isNaN(a) || java.lang.Double.isNaN(b)) {
            this.real = java.lang.Double.NaN;
            this.imaginary = java.lang.Double.NaN;
        } else {
            this.real = a;
            this.imaginary = b;
        }
    }

    /**
     * Construct a complex number from a real number.
     *
     * @param a a real number
     */
    public Complex(double a) {
        this(a, 0);
    }

    /**
     * Factory method to construct a complex number from the polar form: <i>(r, θ)</i>.
     *
     * @param r     a radius
     * @param theta an angle
     * @return a complex number equivalent to the polar form <i>(r, θ)</i>
     */
    public static Complex fromPolar(double r, double theta) {
        return new Complex(r * Math.cos(theta), r * Math.sin(theta));
    }

    /**
     * Get the real part of this complex number.
     *
     * @return the real part
     */
    public double real() {
        return real;
    }

    /**
     * Get the imaginary part of this complex number.
     *
     * @return the imaginary part
     */
    public double imaginary() {
        return imaginary;
    }

    /**
     * Check if this complex number is a real number;
     * i.e., the imaginary part is 0.
     *
     * @param z a complex number
     * @return {@code true} if the imaginary part is 0
     */
    public static boolean isReal(Complex z) {
        return DoubleUtils.isZero(z.imaginary, 0);
    }

    /**
     * Check if a complex number is an {@code NaN};
     * i.e., either the real or the imaginary part is an {@code NaN}.
     *
     * @param z a complex number
     * @return {@code true} if either the real or the imaginary part is a {@code NaN}
     */
    public static boolean isNaN(Complex z) {
        return java.lang.Double.isNaN(z.real) || java.lang.Double.isNaN(z.imaginary);
    }

    /**
     * Check if a complex number is an infinity;
     * i.e., either the real or the imaginary part is infinite, c.f., {@link Double#isInfinite()},
     * <em>and</em> the number is not a {@code NaN}.
     *
     * @param z a complex number
     * @return {@code true} if either the real or the imaginary part is infinite
     */
    public static boolean isInfinite(Complex z) {
        return !isNaN(z)
               && (java.lang.Double.isInfinite(z.real) || java.lang.Double.isInfinite(z.imaginary));
    }

    /**
     * Get the modulus.
     * The modulus is the square root of itself multiplied by its conjugate, namely
     * <blockquote><i>
     * this.modulus() * this.modulus() = this.multiply(this.conjugate())
     * </i></blockquote>
     *
     * @return the modulus
     * @see java.lang.Math#hypot(double, double)
     */
    public double modulus() {
        if (DoubleUtils.isZero(imaginary, 0)) {
            return Math.abs(real);
        }

        if (DoubleUtils.isZero(real, 0)) {
            return Math.abs(imaginary);
        }

        return Math.hypot(real, imaginary);
    }

    /**
     * Get the <i>θ</i> of the complex number in polar representation.
     *
     * @return θ as in the polar form <i>(r, θ)</i>
     */
    public double arg() {
        return Math.atan2(imaginary, real);
    }

    /**
     * Cast the complex number to a {@link Double} if it is a real number.
     *
     * @return the real part if this complex number is a real number
     * @throws IllegalArgumentException if this complex number is not a real number
     */
    public Double toDouble() {
        if (!isReal(this)) {
            throw new IllegalArgumentException(String.format("this complex number %s is not real", toString()));
        }

        return new Double(real);
    }

    /**
     * Get the conjugate of the complex number, namely, <i>(a - bi)</i>.
     *
     * @return the conjugate
     */
    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }

    /**
     * @deprecated Invalid operation.
     */
    @Override
    @Deprecated
    public int intValue() {
        throw new UnsupportedOperationException("Complex number cannot be converted to int.");
    }

    /**
     * @deprecated Invalid operation.
     */
    @Override
    @Deprecated
    public long longValue() {
        throw new UnsupportedOperationException("Complex number cannot be converted to long.");
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        if (!isReal(this)) {
            throw new IllegalArgumentException(String.format("this complex number %s is not real", toString()));
        }

        return real;
    }

    @Override
    public Complex add(Complex that) {
        return new Complex(this.real + that.real, this.imaginary + that.imaginary);
    }

    @Override
    public Complex minus(Complex that) {
        return new Complex(this.real - that.real, this.imaginary - that.imaginary);
    }

    @Override
    public Complex opposite() {
        return new Complex(-real, -imaginary);
    }

    @Override
    public Complex inverse() throws InverseNonExistent {
        if (DoubleUtils.isZero(real, 0) && DoubleUtils.isZero(imaginary, 0)) {
            throw new InverseNonExistent();
        }

        double mod = this.modulus();
        return new Complex(real / mod, -imaginary / mod);
    }

    /**
     * Compute the quotient of this complex number divided by another complex number.
     * \[
     * \frac{a + bi}{c + di} = \frac{ac + bd + (bc - ad)i}{c^2 + d^2}
     * \]
     * <p/>
     * ABSTRACT
     * <blockquote>
     * We develop a simple method for scaling to avoid overflow and harmful underflow in complex division.
     * The method guarantees that no overflow will occur unless at least one component of the quotient must overflow,
     * otherwise the normwise error in the computed result is at most a few units in the last place.
     * Moreover, the scaling requires only four floating point multiplications
     * and a small amount of integer arithmetic to compute the scale factor.
     * Thus, on many modern CPUs, our method is both safer and faster than Smith's widely used algorithm.
     * </blockquote>
     *
     * @return <i>this / that</i>
     * @throws ArithmeticException if division by zero happens
     * @see "Douglas M. Priest Sun Microsystems, Menlo Park, CA, "Efficient scaling for complex division," ACM Transactions on Mathematical Software (TOMS) archive, Volume 30, Issue 4 (December 2004) Pages: 389 - 401."
     */
    @Override
    public Complex divide(Complex that) {
        if (Complex.isNaN(this) || Complex.isNaN(that)) {
            return NaN;//quick return
        }

        if (Complex.isInfinite(that) && !Complex.isInfinite(this)) {
            return that.ZERO();//quick return
        }

        Complex quotient;

        final double a = this.real;
        final double b = this.imaginary;
        final double c = that.real;
        final double d = that.imaginary;

        if (Math.abs(c) < Math.abs(d)) {
            if (DoubleUtils.isZero(d, 0)) {
                throw new ArithmeticException("division by zero");
            }

            double q = c / d;//d != 0
            double denominator = c * q + d;
            quotient = new Complex(
                    (a * q + b) / denominator,
                    (b * q - a) / denominator);
        } else {//Math.abs(c) > Math.abs(d)
            if (DoubleUtils.isZero(c, 0)) {
                throw new ArithmeticException("division by zero");
            }

            double q = d / c;//c != 0
            double denominator = d * q + c;
            quotient = new Complex(
                    (b * q + a) / denominator,
                    (b - a * q) / denominator);
        }

        return quotient;
    }

    /**
     * Compute the product of this complex number and that complex number.
     * <blockquote><i>
     * (a + bi)(c + di) = (ac - bd) + (ad + bc)i
     * </i></blockquote>
     * This implementation is more efficient by doing 1 less multiplication:
     * <blockquote><i>
     * (a + bi)(c + di) = (ac - bd) + ((a + b)(c + d) - ac - bd)i
     * </i></blockquote>
     *
     * @return <i>this * that</i>
     */
    @Override
    public Complex multiply(Complex that) {
        double ac = this.real * that.real;
        double bd = this.imaginary * that.imaginary;
        return new Complex(
                ac - bd,
                (this.real + this.imaginary) * (that.real + that.imaginary) - ac - bd);
    }

    /**
     * Get zero - the number representing <i>0.0 + 0.0i</i>.
     *
     * @return ZERO
     */
    @Override
    public Complex ZERO() {
        return Complex.ZERO;
    }

    /**
     * Get one - the number representing <i>1.0 + 0.0i</i>.
     *
     * @return ONE
     */
    @Override
    public Complex ONE() {
        return Complex.ONE;
    }

    @Override
    public int compare(Number that, double epsilon) {
        Complex diff = this.minus((Complex) that);
        return DoubleUtils.compare(diff.modulus(), 0d, epsilon);
    }

    @Override
    public String toString() {
        String result = String.format("%f%s%fi",
                                      real,
                                      imaginary >= 0 ? "+" : "",//use the "-" sign from imaginary
                                      imaginary);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Complex that = (Complex) obj;
        if (Complex.isNaN(that)) {
            return Complex.isNaN(this);
        }
        if (DoubleUtils.compare(this.real, that.real, 0) != 0) {
            return false;
        }
        if (DoubleUtils.compare(this.imaginary, that.imaginary, 0) != 0) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.real) ^ (Double.doubleToLongBits(this.real) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.imaginary) ^ (Double.doubleToLongBits(this.imaginary) >>> 32));
        return hash;
    }
}
