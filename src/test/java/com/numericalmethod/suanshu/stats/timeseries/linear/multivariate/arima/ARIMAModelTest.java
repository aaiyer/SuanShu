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
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin Sun
 */
public class ARIMAModelTest {

    @Test
    public void test_0010() {
        double[] mu = new double[]{1., -1.};
        Matrix[] phi = new Matrix[2];
        phi[0] = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0., 0.5}});
        phi[1] = phi[0].t();
        Matrix[] theta = new Matrix[1];
        theta[0] = phi[1];
        int d = 0;
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1, 0.2},
                    {0.2, 1}});
        ARIMAModel arimaModel = new ARIMAModel(new DenseVector(mu), phi, d, theta, sigma);

        assertEquals(mu[0], arimaModel.mu().toArray()[0], 1e-15);
        assertEquals(mu[1], arimaModel.mu().toArray()[1], 1e-15);
        assertEquals(d, arimaModel.d(), 1e-15);
        assertTrue(AreMatrices.equal(sigma, arimaModel.sigma(), 1e-15));
        assertEquals(phi.length, arimaModel.p(), 1e-15);
        assertEquals(theta.length, arimaModel.q(), 1e-15);
        assertEquals(mu.length, arimaModel.dimension(), 1e-15);
        assertTrue(AreMatrices.equal(sigma, arimaModel.sigma(), 1e-15));
    }

    @Test
    public void test_0020() {
        Matrix[] phi = new Matrix[2];
        phi[0] = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0., 0.5}});
        phi[1] = phi[0].t();
        Matrix[] theta = new Matrix[1];
        theta[0] = phi[1];
        int d = 0;
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1, 0.2},
                    {0.2, 1}});
        ARIMAModel arimaModel = new ARIMAModel(phi, d, theta, sigma);

        assertEquals(0., arimaModel.mu().toArray()[0], 1e-15);
        assertEquals(0., arimaModel.mu().toArray()[1], 1e-15);
        assertEquals(d, arimaModel.d(), 1e-15);
        assertTrue(AreMatrices.equal(sigma, arimaModel.sigma(), 1e-15));
        assertEquals(phi.length, arimaModel.p(), 1e-15);
        assertEquals(theta.length, arimaModel.q(), 1e-15);
        assertEquals(2, arimaModel.dimension(), 1e-15);
        assertTrue(AreMatrices.equal(sigma, arimaModel.sigma(), 1e-15));
    }
}
