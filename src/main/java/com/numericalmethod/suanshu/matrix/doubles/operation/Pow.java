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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * This is a square matrix <i>A</i> to the power of an integer <i>n</i>, <i>A<sup>n</sup></i>.
 * To avoid overflow of {@code double} precision, we represent the result as a product of
 * <blockquote><i>
 * A<sup>n</sup> = base<sup>scale</sup> * B;
 * </i></blockquote>
 * E.g.,
 * <blockquote><i>
 * A<sup>n</sup> = 1e100<sup>scale</sup> * B = 10<sup>100 * scale</sup> * B
 * </i></blockquote>
 * All entries in <i>B</i> can be represented in {@code double} precision.
 *
 * @author Haksun Li
 */
public class Pow extends DenseMatrix {

    private final double base;
    private final int scale;
    private final ImmutableMatrix B;

    /**
     * Construct the power matrix <i>A<sup>n</sup></i> so that
     * <blockquote><i>
     * A<sup>n</sup> = base<sup>scale</sup> * B
     * </i></blockquote>
     *
     * @param A    a matrix
     * @param n    a positive integer exponent
     * @param base the base to scale down the product to avoid overflow
     */
    public Pow(Matrix A, int n, double base) {
        super(A.nCols(), A.nRows());

        SuanShuUtils.assertArgument(n > 0, "the exponent must be positive");

        this.base = base;

        Matrix An = A.ONE();
        Matrix SQ = A;

        //we use the repeated square trick
        int scale1 = 0, scale2 = 0, scale3 = 0;
        while (n > 0) {
            if ((n & 1) == 1) {
                An = An.multiply(SQ);
                scale3 += scale2;
            }

            //scale down the matrices to avoid overflow
            while (MatrixMeasure.max(An) > base) {
                An = An.scaled(1d / base);
                ++scale1;
            }

            while (MatrixMeasure.max(SQ) > base) {
                SQ = SQ.scaled(1. / base);
                ++scale2;
            }

            if ((n >>>= 1) != 0) {
                SQ = SQ.multiply(SQ);
                scale2 *= 2;
            }
        }

        this.scale = scale1 + scale3;
        this.B = new ImmutableMatrix(An.deepCopy());
        An = this.B.scaled(Math.pow(this.base, this.scale));

        //overwrite {@code this}
        for (int i = 1; i <= nRows(); ++i) {
            for (int j = 1; j <= nCols(); ++j) {
                super.set(i, j, An.get(i, j));
            }
        }
    }

    /**
     * Construct the power matrix <i>A<sup>n</sup></i> so that
     * <blockquote><i>
     * A<sup>n</sup> = (1e100)<sup>scale</sup> * B
     * </i></blockquote>
     *
     * @param A a matrix
     * @param n a positive integer exponent
     */
    public Pow(Matrix A, int n) {
        this(A, n, 1e100);
    }

    /**
     * Get the radix or base of the coefficient.
     *
     * @return the base
     * @see <a href="http://en.wikipedia.org/wiki/Radix">Wikipedia: Radix</a>
     */
    public double base() {
        return base;
    }

    /**
     * Get the exponential of the coefficient.
     *
     * @return the exponential of the coefficient
     */
    public int scale() {
        return scale;
    }

    /**
     * Get the double precision matrix.
     *
     * @return the double precision matrix
     */
    public ImmutableMatrix B() {
        return B;
    }
}
