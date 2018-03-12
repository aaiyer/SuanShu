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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;

/**
 * Given a matrix <i>A</i> and an invertible matrix <i>P</i>, we construct the similar matrix <i>B</i> s.t.,
 * <blockquote><i>
 * B = P<sup>-1</sup>AP
 * </i></blockquote>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Similar_matrix">Wikipedia: Similar matrix</a>
 */
public class SimilarMatrix extends DenseMatrix {

    /**
     * Construct the similar matrix <i>B = P<sup>-1</sup>AP</i>.
     *
     * @param P an invertible matrix
     * @param A a matrix
     */
    public SimilarMatrix(Matrix P, Matrix A) {
        super(new Inverse(P).multiply(A).multiply(P));
    }
}
