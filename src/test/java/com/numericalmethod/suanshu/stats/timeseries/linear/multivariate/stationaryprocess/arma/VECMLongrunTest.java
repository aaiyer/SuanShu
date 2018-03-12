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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class VECMLongrunTest {

    @Test
    public void test_0010() {
        VARXModel var = new VARXModel(new Matrix[]{
                    new DenseMatrix(new double[][]{
                        {1.5, 0},
                        {0, 0.5}
                    }),
                    new DenseMatrix(new double[][]{
                        {0.5, 0.5},
                        {-0.5, 1.5}
                    })
                },
                null);

        VECMLongrun vecm = new VECMLongrun(var);//sigma

        assertEquals(2, vecm.dimension());

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1, 0.5},
                    {-0.5, 1}
                }), vecm.pi(), 1e-3));

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.5, 0},
                    {0, -0.5}
                }), vecm.gamma(1), 1e-3));
    }
}
