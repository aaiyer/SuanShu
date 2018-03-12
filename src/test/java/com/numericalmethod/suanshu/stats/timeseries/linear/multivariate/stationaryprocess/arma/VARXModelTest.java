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
public class VARXModelTest {

    /**
     * from http://support.sas.com/rnd/app/da/new/801ce/ets/chap4/sect5.htm
     */
    @Test
    public void test_0010() {
        VECMTransitory vecm = new VECMTransitory(
                new DenseMatrix(new double[][]{
                    {-0.4, 0.8},
                    {0.1, -0.2}
                }),//pi
                new Matrix[]{
                    new DenseMatrix(new double[][]{
                        {-0.8, -0.7},
                        {0.4, -0.6}
                    })},//gamma_1
                null,
                new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                }));//sigma

        VARXModel var = new VARXModel(vecm);

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {-0.2, 0.1},
                    {0.5, 0.2}
                }), var.AR(1), 1e-3));

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.8, 0.7},
                    {-0.4, 0.6}
                }), var.AR(2), 1e-3));

        assertEquals(2, var.p());
    }

    /**
     * from http://support.sas.com/rnd/app/da/new/801ce/ets/chap4/sect5.htm
     */
    @Test
    public void test_0020() {
        VECMTransitory vecm = new VECMTransitory(
                new DenseMatrix(new double[][]{
                    {-0.467, 0.913},
                    {0.107, -0.209}
                }),//pi
                new Matrix[]{
                    new DenseMatrix(new double[][]{
                        {-0.743, -0.746},
                        {0.405, -0.572}
                    })},//gamma_1
                null,
                new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                }));//sigma

        VARXModel var = new VARXModel(vecm);

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {-0.210, 0.167},
                    {0.512, 0.220}
                }), var.AR(1), 1e-3));

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.743, 0.746},
                    {-0.405, 0.572}
                }), var.AR(2), 1e-3));

        assertEquals(2, var.p());
    }

    @Test
    public void test_0030() {
        VECMLongrun vecm = new VECMLongrun(
                new DenseMatrix(new double[][]{
                    {1, 0.5},
                    {-0.5, 1}
                }),//pi
                new Matrix[]{
                    new DenseMatrix(new double[][]{
                        {0.5, 0},
                        {0, -0.5}
                    })},//gamma_1
                null,
                new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 1}
                }));//sigma

        VARXModel var = new VARXModel(vecm);

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {1.5, 0},
                    {0, 0.5}
                }), var.AR(1), 1e-3));

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {-0.5, 1.5}
                }), var.AR(2), 1e-3));

        assertEquals(2, var.p());
    }
}
