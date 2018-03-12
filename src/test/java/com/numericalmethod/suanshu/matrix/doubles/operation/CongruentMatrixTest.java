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

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CongruentMatrixTest {

    @Test
    public void test_0010() {
        Matrix Vr = new DenseMatrix(new double[][]{
                    {0.700000, 0.700000},
                    {0.500000, -0.500000},
                    {-0.500000, 0.500000}
                });

        Matrix H = new DenseMatrix(new double[][]{
                    {1.000000, 0.000000, 0.000000},
                    {0.000000, 1.000000, 0.000000},
                    {0.000000, 0.000000, 0.000000}
                });

        Matrix B = new CongruentMatrix(Vr, H);
        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.740000, 0.240000},
                    {0.240000, 0.740000}
                }),
                B,
                1e-15));
    }
}
