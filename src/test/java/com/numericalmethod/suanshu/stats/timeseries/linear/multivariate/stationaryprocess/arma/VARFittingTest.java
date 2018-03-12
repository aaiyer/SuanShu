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

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMASim;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin Sun
 */
public class VARFittingTest {

    @Test
    public void test_0010() {
        int size = 2000;

        Vector mu = new DenseVector(new double[]{1., 2.});
        Matrix[] phi = new Matrix[]{
            new DenseMatrix(new double[][]{
                {0.5, 0.5},
                {0., 0.5}})
        };
        Matrix sigma = new DenseMatrix(new double[][]{
                    {0.2, 0.1},
                    {0.1, 0.2}});
        ARModel model = new ARModel(mu, phi, sigma);

        ARIMASim instance = new ARIMASim(size, model, 123456);

        ARModel fitted = new VARFitting(instance, 1).getFittedArModel();
        assertArrayEquals(mu.toArray(), fitted.mu().toArray(), 2e-1);
        assertTrue(AreMatrices.equal(phi[0], fitted.AR(1), 1e-1));

//        double[] mean = new Inverse(new DenseMatrix(2, 2).ONE().minus(model.AR(1))).multiply(new DenseVector(mu)).toArray();
//        Matrix instanceM = instance.toMatrix();
//        assertEquals(mean[0], new Mean(instanceM.column(1).toArray()).value(), 1e-1);
//        assertEquals(mean[1], new Mean(instanceM.column(2).toArray()).value(), 1e-1);
    }

//    @Test
    public void test_0020() {
        int size = 5000;

        Vector mu = new DenseVector(new double[]{1., 2.});
        Matrix[] phi = new Matrix[]{
            new DenseMatrix(new double[][]{
                {0.5, 0.5},
                {0., 0.5}})
        };
        Matrix sigma = new DenseMatrix(new double[][]{
                    {0.2, 0.1},
                    {0.1, 0.2}});
        ARModel model = new ARModel(mu, phi, sigma);

        ARIMASim instance = new ARIMASim(size, model);

        ARModel fitted = new VARFitting(instance, 1).getFittedArModel();
        assertArrayEquals(mu.toArray(), fitted.mu().toArray(), 1e-1);
        assertTrue(AreMatrices.equal(phi[0], fitted.AR(1), 5e-2));
//
//        double[] mean = new Inverse(new DenseMatrix(2, 2).ONE().minus(model.AR(1))).multiply(new DenseVector(mu)).toArray();
//        Matrix instanceM = instance.toMatrix();
//        assertEquals(mean[0], new Mean(instanceM.column(1).toArray()).value(), 1e-1);
//        assertEquals(mean[1], new Mean(instanceM.column(2).toArray()).value(), 1e-1);
    }
}
