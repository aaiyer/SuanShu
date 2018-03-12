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
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils.to1DArray;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class InnerProductTest {

    @Test
    public void test_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1., 2., 3.,},
                    {4., 5., 6.,},
                    {7., 8., 9.}
                });

        Matrix B = new DenseMatrix(new double[][]{
                    {0.5, 0.3, 0.1,},
                    {3.5, 4.5, 5.5,},
                    {2.5, 1.5, 0.5}
                });

        Vector a = new DenseVector(to1DArray(A));
        Vector b = new DenseVector(to1DArray(B));

        InnerProduct instance = new InnerProduct(A, B);
        assertEquals(a.innerProduct(b), instance.value(), 0.0);
    }
}
