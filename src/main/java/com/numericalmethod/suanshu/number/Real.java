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

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.mathstructure.Field;
import com.numericalmethod.suanshu.mathstructure.Field.InverseNonExistent;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A real number is an arbitrary precision number.
 * This implementation is simply a wrapper around {@link java.math.BigDecimal} and implements the {@link Field} interface.
 * <p/>
 * This class is immutable.
 *
 * @author Haksun Li
 */
public class Real extends Number implements Field<Real>, Comparable<Real> {

    /** a number representing <i>0</i> */
    public static final Real ZERO = new Real(BigDecimal.ZERO);
    /** a number representing <i>1</i> */
    public static final Real ONE = new Real(BigDecimal.ONE);
    private final BigDecimal value;
    private static final long serialVersionUID = 1L;

    /**
     * Construct a {@code Real} from a {@code double}.
     *
     * @param value a {@code double}
     */
    public Real(double value) {
        this.value = BigDecimal.valueOf(value);
    }

    /**
     * Construct a {@code Real} from an integer.
     *
     * @param value an integer
     */
    public Real(long value) {
        this.value = BigDecimal.valueOf(value);
    }

    /**
     * Construct a {@code Real} from a {@code BigDecimal}.
     *
     * @param value a {@code BigDecimal}
     */
    public Real(BigDecimal value) {
        this.value = value;
    }

    /**
     * Construct a {@code Real} from a {@code BigInteger}.
     *
     * @param value a {@code BigInteger}
     */
    public Real(BigInteger value) {
        this.value = new BigDecimal(value);
    }

    /**
     * Construct a {@code Real} from a {@code String}.
     *
     * @param value a {@code String} representation of a number
     */
    public Real(String value) {
        this.value = new BigDecimal(value);
    }

    /**
     * Convert this number to a {@code BigDecimal}.
     *
     * @return the value in {@code BigDecimal}
     */
    public BigDecimal toBigDecimal() {
        return value;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public Real add(Real that) {
        return new Real(this.value.add(that.value));
    }

    @Override
    public Real minus(Real that) {
        return add(that.opposite());
    }

    @Override
    public Real opposite() {
        return new Real(this.value.negate());
    }

    @Override
    public Real multiply(Real that) {
        return new Real(this.value.multiply(that.value));
    }

    @Override
    public Real divide(Real that) {
        return divide(that, Constant.MACH_SCALE);
    }

    /**
     * <i>/ : R × R → R</i>
     * <p/>
     * Divide this number by another one.
     * Rounding is performed with the specified {@code scale}.
     *
     * @param that  another non-zero real number
     * @param scale rounding scale as in {@link BigDecimal}
     * @return <i>this/that</i>
     */
    public Real divide(Real that, int scale) {
        Real result = new Real(this.value.divide(that.value, scale, BigDecimal.ROUND_HALF_EVEN));
        return result;
    }

    @Override
    public Real inverse() throws InverseNonExistent {
        return ONE().divide(this);
    }

    @Override
    public Real ZERO() {
        return Real.ZERO;
    }

    @Override
    public Real ONE() {
        return Real.ONE;
    }

    @Override
    public int compareTo(Real that) {
        return value.compareTo(that.value);
    }

    @Override
    public String toString() {
        return value.stripTrailingZeros().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Real other = (Real) obj;
        if (this.value != other.value && (this.value == null || this.value.compareTo(other.value) != 0)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
