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
package com.numericalmethod.suanshu.number;

import com.numericalmethod.suanshu.number.big.BigDecimalUtils;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Scientific notation expresses a number in this form
 * <blockquote><i>
 * x = a * 10<sup>b</sup>
 * </i></blockquote>
 * <i>a</i> is called the significand or mantissa, and <i>1 &le; |a| &lt; 10</i>.
 * <i>b</i> is called the exponent and is an integer.
 * <p/>
 * Strictly speaking, <i>0</i> cannot be represented in this notation.
 * This implementation, however, expresses it as <i>0 = 0 * 10<sup>0</sup></i>.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Scientific_notation"> Wikipedia: Scientific notation</a>
 */
public class ScientificNotation extends Number {

    /** the significand or mantissa */
    private final BigDecimal significand;
    /** the exponent */
    private final int exponent;
    private final BigDecimal TEN = BigDecimal.valueOf(10);
    private static final long serialVersionUID = 1L;

    /**
     * Construct the scientific notation of a number in this form: <i>x = a * 10<sup>b</sup></i>.
     *
     * @param significand the significand
     * @param exponent    the exponent
     */
    public ScientificNotation(double significand, int exponent) {
        this.significand = new BigDecimal(significand);
        this.exponent = exponent;
    }

    /**
     * Construct the scientific notation of a number in this form: <i>x = a * 10<sup>b</sup></i>.
     *
     * @param significand the significand
     * @param exponent    the exponent
     */
    public ScientificNotation(BigDecimal significand, int exponent) {
        this.significand = significand;
        this.exponent = exponent;
    }

    /**
     * Get the significand.
     *
     * @return the significand
     */
    public BigDecimal significand() {
        return significand;
    }

    /**
     * Get the exponent.
     *
     * @return the exponent
     */
    public int exponent() {
        return exponent;
    }

    /**
     * Construct the scientific notation of a number.
     *
     * @param x a number
     */
    public ScientificNotation(BigDecimal x) {
        if (x.equals(BigDecimal.ZERO)) {
            significand = BigDecimal.ZERO;
            exponent = 0;
            return;
        }

        int b = 0;
        BigDecimal absx = x.abs();
        for (; absx.compareTo(TEN) >= 0;) {//|x| >= 10
            ++b;
            absx = absx.divide(TEN);
        }

        for (; absx.compareTo(BigDecimal.ONE) < 0;) {//|x| < 1
            --b;
            absx = absx.multiply(TEN);
        }

        significand = x.signum() > 0 ? absx : absx.negate();
        exponent = b;
    }

    /**
     * Construct the scientific notation of an integer.
     *
     * @param x an integer
     */
    public ScientificNotation(BigInteger x) {
        this(new BigDecimal(x));
    }

    /**
     * Construct the scientific notation of a {@code long}.
     *
     * @param x a {@code long} integer
     */
    public ScientificNotation(long x) {
        this(BigInteger.valueOf(x));
    }

    /**
     * Construct the scientific notation of a {@code double}.
     *
     * @param x a {@code double} number
     */
    public ScientificNotation(double x) {
        this(new BigDecimal(x));
    }

    /**
     * Convert the number to {@link BigDecimal}.
     * There is no rounding.
     *
     * @return the {@code BigDecimal} representation of the number
     */
    public BigDecimal bigDecimalValue() {
        BigDecimal b10 = BigDecimalUtils.pow(TEN, exponent, Math.abs(exponent));//10^b
        BigDecimal result = significand.multiply(b10);
        result.stripTrailingZeros();
        return result;
    }

    @Override
    public int intValue() {
        return bigDecimalValue().intValue();
    }

    @Override
    public long longValue() {
        return bigDecimalValue().longValue();
    }

    @Override
    public float floatValue() {
        return bigDecimalValue().floatValue();
    }

    @Override
    public double doubleValue() {
        return bigDecimalValue().doubleValue();
    }
}
