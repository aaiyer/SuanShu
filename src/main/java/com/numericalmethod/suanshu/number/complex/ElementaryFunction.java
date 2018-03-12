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

import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import static com.numericalmethod.suanshu.number.DoubleUtils.isZero;

/**
 * This class contains some elementary functions for complex number, {@link Complex}.
 * The references for the formulae can be found in the following.
 *
 * @author Ken Yiu
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Square_root#Square_roots_of_negative_and_complex_numbers">Wikipedia: Square roots of negative and complex numbers</a>
 * <li><a href="http://en.wikipedia.org/wiki/Logarithm">Wikipedia: Logarithm</a>
 * <li><a href="http://en.wikipedia.org/wiki/Exponentiation">Wikipedia: Exponentiation</a>
 * <li><a href="http://en.wikipedia.org/wiki/Trigonometric_function">Wikipedia: Trigonometric functions</a>
 * <li><a href="http://en.wikipedia.org/wiki/Hyperbolic_function">Wikipedia: Hyperbolic functions</a>
 * </ul>
 */
public class ElementaryFunction {

    /**
     * Square root of a complex number.
     *
     * @param z a complex number
     * @return the square root of the number
     * @see <a href="http://en.wikipedia.org/wiki/Square_root#Square_roots_of_negative_and_complex_numbers">Wikipedia: Square roots of negative and complex numbers</a>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex sqrt(Complex z) {
        isValid(z);
        Complex sqrt;
        double absRe = Math.abs(z.real());
        double absIm = Math.abs(z.imaginary());

        double w;
        if (isZero(z.real(), 0) && isZero(z.imaginary(), 0)) {
            return new Complex(0, 0);
        }

        if (absRe >= absIm) {
            double imOverRe = absIm / absRe;
            w = Math.sqrt(absRe) * Math.sqrt((1 + Math.sqrt(1 + imOverRe * imOverRe)) / 2d);
        } else {
            double reOverIm = absRe / absIm;
            w = Math.sqrt(absIm) * Math.sqrt((reOverIm + Math.sqrt(1 + reOverIm * reOverIm)) / 2d);
        }

        if (compare(z.real(), 0, 0) >= 0) {
            sqrt = new Complex(w, z.imaginary() / w / 2d);
        } else if (compare(z.imaginary(), 0, 0) >= 0) {
            sqrt = new Complex(absIm / w / 2d, w);
        } else {
            sqrt = new Complex(absIm / w / 2d, -w);
        }

        return sqrt;
    }

    /**
     * Natural logarithm of a complex number.
     * <blockquote><i>
     * ln(a + bi) = ln(r exp(i θ)) = ln(r) + i θ
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>ln(z)</i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex log(Complex z) {
        isValid(z);
        if (z.equals(z.ZERO())) {
            return Complex.NEGATIVE_INFINITY;
        }

        return new Complex(
                Math.log(z.modulus()),
                Math.atan2(z.imaginary(), z.real()));
    }

    /**
     * Exponential of a complex number.
     * <blockquote><pre><i>
     * exp(a + bi) = exp(a) * [cos(b) + i sin(b)]
     *             = exp(b)cos(b) + i exp(a)sin(b)
     * </i></pre></blockquote>
     *
     * @param z a complex number
     * @return <i>e<sup>z</sup></i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex exp(Complex z) {
        isValid(z);
        double expRe = Math.exp(z.real());
        return Complex.fromPolar(expRe, z.imaginary());
    }

    /**
     * z<sub>1</sub> to the power z<sub>2</sub>.
     * <blockquote><i>
     * (r exp(i θ)) ^ (a + bi)
     * = r^a exp(-b θ) (cos(b ln(r) + a θ) + i sin(b ln(r) + a θ))
     * </i></blockquote>
     *
     * @param z1 a complex number
     * @param z2 a complex number
     * @return <i>z1<sup>z2</sup></i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex pow(Complex z1, Complex z2) {
        isValid(z1);
        isValid(z2);
        double mod1 = z1.modulus();
        if (isZero(mod1, 0)) {
            return z1.ZERO();
        }
        double arg1 = Math.atan2(z1.imaginary(), z1.real());
        return Complex.fromPolar(
                Math.pow(mod1, z2.real()) / Math.exp(z2.imaginary() * arg1),
                z2.imaginary() * Math.log(mod1) + z2.real() * arg1);
    }

    /**
     * Sine of a complex number.
     * <blockquote><i>
     * sin(a + bi) = sin(a)cosh(b) + i cos(a)sinh(b)
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>sin(z)</i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex sin(Complex z) {
        isValid(z);
        return new Complex(
                Math.sin(z.real()) * Math.cosh(z.imaginary()),
                Math.cos(z.real()) * Math.sinh(z.imaginary()));

    }

    /**
     * Cosine of a complex number.
     * <blockquote><i>
     * cos(a + bi) = cos(a)cosh(b) - i sin(a)sinh(b)
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>cos(z)</i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex cos(Complex z) {
        isValid(z);
        return new Complex(
                Math.cos(z.real()) * Math.cosh(z.imaginary()),
                -Math.sin(z.real()) * Math.sinh(z.imaginary()));
    }

    /**
     * Tangent of a complex number.
     * <blockquote><pre><i>
     * tan(a + bi) = [sin(2a) + i sinh(2a)] / [cos(2a) + cosh(2b)]
     * </i></pre></blockquote>
     *
     * @param z a complex number
     * @return <i>tan(z)</i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex tan(Complex z) {
        isValid(z);
        double twoRe = z.real() * 2d;
        double twoIm = z.imaginary() * 2d;
        double denominator = Math.cos(twoRe) + Math.cosh(twoIm);
        return new Complex(
                Math.sin(twoRe) / denominator,
                Math.sinh(twoIm) / denominator);
    }

    /**
     * Inverse of sine.
     * <blockquote><i>
     * arcsin(z) = -i ln(iz + sqrt(1 - z<sup>2</sup>))
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>sin<sup>-1</sup>(z)</i>
     * @see <a href="http://mathworld.wolfram.com/InverseSine.html">Inverse Sine from Wolfram MathWorld</a>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex asin(Complex z) {
        isValid(z);
        return Complex.I.opposite().multiply(
                log(Complex.I.multiply(z).
                add(sqrt(z.ONE().minus(z.multiply(z))))));
    }

    /**
     * Inverse of cosine.
     * <blockquote><i>
     * arccos(z) = pi / 2 + i ln(iz + sqrt(1 - z<sup>2</sup>))
     * = pi / 2 - arcsin(z)
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>cos<sup>-1</sup>(z)</i>
     * @see <a href="http://mathworld.wolfram.com/InverseCosine.html">Inverse Cosine from Wolfram MathWorld</a>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex acos(Complex z) {
        isValid(z);
        return new Complex(Math.PI / 2, 0).minus(asin(z));
    }

    /**
     * Inverse of tangent.
     * <blockquote><i>
     * arctan(z) = i * ( ln ( 1 - iz ) - ln ( 1 + iz) ) / 2
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>tan<sup>-1</sup>(z)</i>
     * @see <a href="http://mathworld.wolfram.com/InverseTangent.html">Inverse Tangent from Wolfram MathWorld</a>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex atan(Complex z) {
        isValid(z);
        return new Complex(0d, 0.5d).multiply(
                log(Complex.ONE.minus(Complex.I.multiply(z))).minus(log(Complex.ONE.add(Complex.I.multiply(z)))));
    }

    /**
     * Hyperbolic sine of a complex number.
     * <blockquote><i>
     * sinh(a + bi) = sinh(a)cos(b) + i cosh(a)sin(b)
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>sinh(z)</i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex sinh(Complex z) {
        isValid(z);
        return new Complex(
                Math.sinh(z.real()) * Math.cos(z.imaginary()),
                Math.cosh(z.real()) * Math.sin(z.imaginary()));
    }

    /**
     * Hyperbolic cosine of a complex number.
     * <blockquote><i>
     * cosh(a + bi) = cosh(a)cos(b) + i sinh(a)sin(b)
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>cosh(z)</i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex cosh(Complex z) {
        isValid(z);
        return new Complex(
                Math.cosh(z.real()) * Math.cos(z.imaginary()),
                Math.sinh(z.real()) * Math.sin(z.imaginary()));
    }

    /**
     * Hyperbolic tangent of a complex number.
     * <blockquote><i>
     * tanh(a + bi) = [sinh(2a) + i sin(2b)] / [cosh(2a) + cos(2b)]
     * </i></blockquote>
     *
     * @param z a complex number
     * @return <i>tanh(z)</i>
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    public static Complex tanh(Complex z) {
        isValid(z);
        double twoRe = z.real() * 2d;
        double twoIm = z.imaginary() * 2d;
        double denominator = Math.cosh(twoRe) + Math.cos(twoIm);
        return new Complex(
                Math.sinh(twoRe) / denominator,
                Math.sin(twoIm) / denominator);
    }

    /**
     * Check if a complex number is valid,
     * i.e., it is not a {@code NaN} or <i>∞</i>.
     *
     * @param z a complex number
     * @return {@code true} if {@code z} is valid
     * @throws IllegalArgumentException if {@code z} is a {@code NaN} or <i>∞</i>
     */
    private static boolean isValid(Complex z) {
        if (Complex.isNaN(z) || Complex.isInfinite(z)) {
            throw new IllegalArgumentException("this method is not defined for NaN or INFINITY");
        }
        return true;
    }
}
