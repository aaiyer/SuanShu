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

import static com.numericalmethod.suanshu.datastructure.DimensionCheck.throwIfDifferentDimension;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;

/**
 * The Frobenius inner product is the component-wise inner product of two matrices as though they are vectors.
 * In other words, it is the sum of the entries of the Hadamard product, that is,
 * <blockquote><pre><i>
 * A : B = trace(A'B) = trace(AB');
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Matrix_multiplication#Frobenius_inner_product">Wikipedia: Frobenius inner product</a>
 */
public class InnerProduct {

    private final double value;

    /**
     * Compute the inner product of two matrices.
     *
     * @param A a matrix
     * @param B a matrix
     */
    public InnerProduct(Matrix A, Matrix B) {
        throwIfDifferentDimension(A, B);

        double v = 0;
        for (int i = 1; i <= A.nRows(); ++i) {
            for (int j = 1; j <= A.nCols(); ++j) {
                v += A.get(i, j) * B.get(i, j);
            }
        }

        value = v;
    }

    /**
     * Get the value of the inner product.
     *
     * @return the value of the inner product
     */
    public double value() {
        return value;
    }
}
