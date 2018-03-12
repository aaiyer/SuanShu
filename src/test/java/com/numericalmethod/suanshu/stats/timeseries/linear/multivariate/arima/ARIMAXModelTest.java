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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.ARMAXModel;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ARIMAXModelTest {

    @Test
    public void test_0010() {
        Vector mu = new DenseVector(new double[]{1., 1.});
        Matrix[] phi = new Matrix[]{
            new DenseMatrix(new double[][]{
                {0.5, 0.5},
                {0., 0.5}
            }),
            new DenseMatrix(new double[][]{
                {0.1, 0.2},
                {0.3, 0.4}
            })};
        Matrix[] theta = new Matrix[]{
            new DenseMatrix(new double[][]{
                {-0.5, 0.5},
                {0.1, 0.1}
            }),
            new DenseMatrix(new double[][]{
                {-0.1, 0.2},
                {-0.3, 0.4}
            })};
        Matrix psi = new DenseMatrix(new double[][]{
                    {1, 0.2, 0.3},
                    {0.2, 1, -0.3}
                });
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1, 0.2},
                    {0.2, 1}
                });

        ARMAXModel model = new ARMAXModel(mu, phi, theta, psi, sigma);

        assertEquals(2, model.dimension());
        assertEquals(2, model.p());
        assertEquals(2, model.q());
        assertEquals(0, model.d());

        ARMAXModel copy = new ARMAXModel(model);
        assertArrayEquals(mu.toArray(), copy.mu().toArray(), 0);
        assertTrue(AreMatrices.equal(phi[0], copy.AR(1), 0));
        assertTrue(AreMatrices.equal(phi[1], copy.AR(2), 0));
        assertTrue(AreMatrices.equal(theta[0], copy.MA(1), 0));
        assertTrue(AreMatrices.equal(theta[1], copy.MA(2), 0));
        assertTrue(AreMatrices.equal(psi, copy.psi(), 0));
        assertTrue(AreMatrices.equal(sigma, copy.sigma(), 0));
    }
}
