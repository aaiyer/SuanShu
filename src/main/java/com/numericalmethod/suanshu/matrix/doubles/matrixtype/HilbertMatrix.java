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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import com.numericalmethod.suanshu.number.ScientificNotation;
import com.numericalmethod.suanshu.number.big.BigIntegerUtils;
import static java.lang.Math.abs;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * A Hilbert matrix, <i>H</i>, is a symmetric matrix with entries being the unit fractions
 * <blockquote><i>H[i][j] = 1 / (i + j -1)</i></blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Hilbert_matrix"> Wikipedia: Hilbert matrix</a>
 */
public class HilbertMatrix extends SymmetricMatrix {

    /**
     * Construct a Hilbert matrix.
     *
     * @param n the Hilbert matrix dimension
     */
    public HilbertMatrix(int n) {
        super(n);

        for (int i = 1; i <= n; ++i) {
            for (int j = i; j <= n; ++j) {
                this.set(i, j, 1d / (i + j - 1));
            }
        }
    }

    /**
     * One over the determinant of <i>H</i>: <i>1/|H|</i>, which is an integer.
     *
     * @return one over the determinant of <i>H</i>, which is an integer
     */
    public BigInteger invdet() {
        final int n = nRows();
        
        BigInteger result = BigIntegerUtils.factorial(n);//n!
        for (int i = 1; i <= 2 * n - 1; ++i) {
            BigInteger term = BigIntegerUtils.combination(i, i / 2);
            result = result.multiply(term);
        }
        return result;
    }

    /**
     * The determinant of a Hilbert matrix is the reciprocal of an integer.
     *
     * @return the determinant
     * @see <a href="http://www.research.att.com/~njas/sequences/A005249"> OEIS: sequence A005249</a>
     */
    public double det() {
        ScientificNotation denominator = new ScientificNotation(invdet());
        BigDecimal result = BigDecimal.ONE.divide(denominator.bigDecimalValue(), abs(denominator.exponent()) + Constant.MACH_SCALE, RoundingMode.HALF_EVEN);//TODO: is the scale correct?
        return result.doubleValue();
    }
}
