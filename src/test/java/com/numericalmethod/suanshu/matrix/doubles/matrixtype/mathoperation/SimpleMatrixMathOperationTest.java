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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class SimpleMatrixMathOperationTest extends MatrixMathOperationTest {

    @Override
    public MatrixMathOperation newInstance() {
        return new SimpleMatrixMathOperation();
    }

    @Test
    public void test_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {11662.0, 0, 0, 0},
                    {-46208.0, 1749.399073915265, 0, 0},
                    {0, -60, 98901.94215050545, 0},
                    {-220, -871.6995369576401, -98333.89710752526, 536.3802141432097}
                });

        Matrix A2 = new UpperTriangularMatrix(new double[][]{
                    {1, -3.9622706225347284, 0, -0.01886468873263591},
                    {1, -0.034297491575616436, -0.49828512542122355},
                    {1, -0.9942564824246246},
                    {1}
                });

        Matrix A1A2 = A1.multiply(A2);
        assertEquals(1.002111, MatrixMeasure.det(A1A2) / 1.08e15, 1e-5);
    }
}
