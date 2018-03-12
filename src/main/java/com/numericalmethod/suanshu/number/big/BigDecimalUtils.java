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
package com.numericalmethod.suanshu.number.big;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * These are the utility functions to manipulate {@link BigDecimal}.
 *
 * @author Haksun Li
 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/math/BigDecimalUtils.html">Class BigDecimal</a>
 */
public class BigDecimalUtils {

    private BigDecimalUtils() {
        // private constructor for utility class
    }
    /**
     * the value of PI
     *
     * @see <a href="http://www.joyofpi.com/pi.html">Joy of PI</a>
     */
    public static final BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");

    //<editor-fold defaultstate="collapsed" desc="comparisons">
    /**
     * Compare two {@code BigDecimal}s up to a precision.
     * In other words, if the absolute difference between the two numbers falls below a threshold, they are considered equal.
     *
     * @param n1 a {@code BigDecimal}
     * @param n2 a {@code BigDecimal}
     * @param p  the threshold is <i>1e-p</i>
     * @return -1, 0, or 1 when {@code n1} is numerically less than, equal to, or greater than {@code n2}, respectively
     */
    public static int compare(BigDecimal n1, BigDecimal n2, int p) {
        BigDecimal eplison = BigDecimal.ONE.movePointLeft(p);
        BigDecimal absDelta = n1.subtract(n2).abs();
        int result = absDelta.compareTo(eplison);
        return result <= 0 ? 0 : n1.compareTo(n2);
    }

    /**
     * Check if two {@code BigDecimal}s are equal up to a precision.
     *
     * @param n1        a {@code BigDecimal}
     * @param n2        a {@code BigDecimal}
     * @param precision the threshold is <i>1e-p</i>
     * @return {@code true} if the numbers are equal up to a precision
     */
    public static boolean equals(BigDecimal n1, BigDecimal n2, int precision) {
        return compare(n1, n2, precision) == 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="sum">
    /**
     * Sum up the {@code BigDecimal} numbers.
     *
     * @param big {@code BigDecimal} numbers
     * @return the sum
     */
    public static BigDecimal sum(BigDecimal... big) {
        BigDecimal result = BigDecimal.ZERO;

        for (BigDecimal bb : big) {
            result = result.add(bb);
        }

        return result;
    }

    /**
     * Sum up big numbers.
     *
     * @param big numbers
     * @return the sum
     */
    public static BigDecimal sum(double... big) {
        BigDecimal result = BigDecimal.ZERO;

        for (double bb : big) {
            result = result.add(BigDecimal.valueOf(bb));
        }

        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="get parts">
    /**
     * Get the integral part of a number (discarding the fractional part).
     *
     * @param num a {@code BigDecimal}
     * @return the integral part of the number
     */
    public static BigDecimal getWhole(BigDecimal num) {
        BigDecimal result = num.setScale(0, BigDecimal.ROUND_DOWN);
        return result;
    }

    /**
     * Get the fractional part of a number.
     * This is the same as the number subtracting the whole part.
     * For a -ve. number, the fractional part is also -ve.
     * For example, for <i>-3.1415</i>, the whole is <i>-3</i> and the fractional part is <i>-0.1415</i>.
     *
     * @param num a {@code BigDecimal}
     * @return the fractional part of the number
     */
    public static BigDecimal getFractional(BigDecimal num) {
        BigDecimal result = num.subtract(getWhole(num));
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="pow">
    /**
     * Compute <i>a</i> to the power of <i>b</i>.
     *
     * @param a a base
     * @param b an exponent
     * @return <i>a<sup>b</sup></i>
     */
    public static BigDecimal pow(BigDecimal a, BigDecimal b) {
        return pow(a, b, Constant.MACH_SCALE);
    }

    /**
     * Compute <i>a</i> to the power of <i>b</i>.
     *
     * @param a     a base
     * @param b     an exponent
     * @param scale a precision parameter as in {@link BigDecimal}
     * @return <i>a<sup>b</sup></i>
     */
    public static BigDecimal pow(BigDecimal a, BigDecimal b, int scale) {
        BigDecimal result = b;
        result = result.multiply(log(a, scale));
        result = exp(result, scale);

        return result;
    }

    /**
     * Compute <i>a</i> to the power of <i>n</i>, where <i>n</i> is an integer.
     *
     * @param a a base
     * @param n an integer exponent
     * @return <i>a<sup>n</sup></i>
     */
    public static BigDecimal pow(BigDecimal a, int n) {
        return pow(a, n, Constant.MACH_SCALE);
    }

    /**
     * Compute <i>a</i> to the power of <i>n</i>, where <i>n</i> is an integer.
     * This is simply a wrapper around {@link BigDecimal#pow(int)} but handles also negative exponents.
     * Use {@link BigDecimal#pow(int)} for arbitrary precision if the exponent is positive.
     *
     * @param a     a base
     * @param n     an exponent
     * @param scale a precision parameter as in {@link BigDecimal}
     * @return <i>a<sup>n</sup></i>
     */
    public static BigDecimal pow(BigDecimal a, int n, int scale) {
        if (n < 0) {
            return BigDecimal.ONE.divide(pow(a, -n, scale), scale, BigDecimal.ROUND_HALF_EVEN);
        }

        return a.pow(n).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="log">
    /**
     * Compute <i>log(x)</i>.
     * The base is <i>e</i>, hence the natural log.
     *
     * @param x a number
     * @return <i>log(x)</i>
     * @see <a href="http://en.wikipedia.org/wiki/Natural_logarithm">Wikipedia: Natural logarithm</a>
     */
    public static BigDecimal log(BigDecimal x) {
        return log(x, Constant.MACH_SCALE);
    }

    /**
     * Compute <i>log(x)</i> up to a scale.
     * The base is <i>e</i>, hence the natural log.
     *
     * @param x     a number
     * @param scale a precision parameter as in {@link BigDecimal}
     * @return <i>log(x)</i>
     * @see <a href="http://en.wikipedia.org/wiki/Natural_logarithm">Wikipedia: Natural logarithm</a>
     */
    public static BigDecimal log(BigDecimal x, int scale) {
        BigDecimal logx;

        if (x.compareTo(BigDecimal.TEN) < 0) {
            logx = logByNewton(x, scale);
        } else {
            int magnitude = x.precision() - x.scale() - 1;
            BigDecimal m = BigDecimal.valueOf(10).pow(magnitude);//x = 10^m * a
            BigDecimal a = x.divide(m);

            BigDecimal log10 = logByNewton(BigDecimal.valueOf(10), scale);
            BigDecimal loga = logByNewton(a, scale);
            logx = BigDecimal.valueOf(magnitude).multiply(log10).add(loga);
        }

        return logx.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Compute <i>log(x)</i> up to a scale.
     * The base is <i>e</i>, hence the natural log.
     * <p/>
     * The algorithm solves for {@code y}, by Newton's method,
     * <i>e<sup>y</sup> - x = 0</i>.
     * It works best for "small" <i>x</i>.
     *
     * @param x     <i>x</i>
     * @param scale the scale for the BigDecimal result; a precision parameter
     * @return <i>log(x)</i>
     * @see
     * <ul>
     * <li><a href="http://en.wikipedia.org/wiki/Natural_logarithm">Wikipedia: Natural logarithm</a>
     * <li><a href="http://en.wikipedia.org/wiki/Newton%27s_method">Wikipedia: Newton's method</a>
     * </ul>
     */
    private static BigDecimal logByNewton(BigDecimal x, int scale) {
        SuanShuUtils.assertArgument(x.signum() > 0, "x must be > 0");

        int precision = 2 * scale;//TODO: what is a correct scale for log?
        BigDecimal y = x;
        //a list of initial guesses for known values to speed up the convergence
        if (y.compareTo(BigDecimal.valueOf(10)) == 0) {
            y = BigDecimal.valueOf(2.302585092994);
        } else if (y.compareTo(BigDecimal.valueOf(1)) == 0) {
            y = BigDecimal.ZERO;
        }

        for (BigDecimal delta = BigDecimal.ONE; !equals(delta, BigDecimal.ZERO, scale);) {
            BigDecimal ey = exp(y, precision);
            delta = BigDecimal.ONE.subtract(x.divide(ey, precision, BigDecimal.ROUND_HALF_EVEN));
            y = y.subtract(delta);//y' = y - (1 - x / e(y))
        }

        return y.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="exp">
    /**
     * Compute <i>e<sup>x</sup></i>.
     *
     * @param x the exponent
     * @return <i>e<sup>x</sup></i>
     * @see <a href="http://en.wikipedia.org/wiki/Exponential_function">Wikipedia: Exponential function</a>
     */
    public static BigDecimal exp(double x) {
        return exp(x, Constant.MACH_SCALE);
    }

    /**
     * Compute <i>e<sup>x</sup></i>.
     *
     * @param x     the exponent
     * @param scale a precision parameter as in {@link BigDecimal}
     * @return <i>e<sup>x</sup></i>
     * @see <a href="http://en.wikipedia.org/wiki/Exponential_function">Wikipedia: Exponential function</a>
     */
    public static BigDecimal exp(double x, int scale) {
        return exp(BigDecimal.valueOf(x), scale);
    }

    /**
     * Compute <i>e<sup>x</sup></i>.
     *
     * @param x the exponent
     * @return <i>e<sup>x</sup></i>
     * @see <a href="http://en.wikipedia.org/wiki/Exponential_function">Wikipedia: Exponential function</a>
     */
    public static BigDecimal exp(BigDecimal x) {
        return exp(x, Constant.MACH_SCALE);
    }

    /**
     * Compute <i>e<sup>x</sup></i>.
     *
     * @param x     the exponent
     * @param scale a precision parameter as in {@link BigDecimal}
     * @return <i>e<sup>x</sup></i>
     * @see <a href="http://en.wikipedia.org/wiki/Exponential_function">Wikipedia: Exponential function</a>
     */
    public static BigDecimal exp(BigDecimal x, int scale) {
        int whole = getWhole(x).intValue();
        BigDecimal fraction = getFractional(x);//x = whole + fraction

        BigDecimal e = expByTaylor(BigDecimal.ONE, scale);
        BigDecimal exp1 = pow(e, whole, scale);//e ^ whole
        BigDecimal exp2 = expByTaylor(fraction, scale);//e ^ fraction
        BigDecimal result = exp1.multiply(exp2);

        return result.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Compute <i>e<sup>x</sup></i> using Taylor series expansion.
     * This works best for small <i>x</i> as the convergence is quick.
     *
     * @param x     the exponent
     * @param scale a precision parameter as in {@link BigDecimal}
     * @return <i>e<sup>x</sup></i>
     * @see <a href="http://en.wikipedia.org/wiki/Exponential_function">Wikipedia: Exponential function</a>
     */
    private static BigDecimal expByTaylor(BigDecimal x, int scale) {
        final MathContext mc = new MathContext(scale);
        BigDecimal result = BigDecimal.ZERO, newResult = BigDecimal.ONE;

        BigDecimal term = new BigDecimal(1, mc);
        for (int i = 1; !equals(result, newResult, scale); ++i) {
            result = newResult;

            term = term.multiply(x, mc);//x ^ i
            term = term.divide(BigDecimal.valueOf(i), scale + 1, BigDecimal.ROUND_HALF_EVEN);//(x ^ i) / i!
            newResult = result.add(term);//∑((x ^ i) / i!)
        }

        return result.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }
    //</editor-fold>
}
