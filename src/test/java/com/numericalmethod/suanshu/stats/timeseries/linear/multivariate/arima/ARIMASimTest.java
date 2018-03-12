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
import com.numericalmethod.suanshu.stats.descriptive.CovarianceMatrix;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.ARMAModel;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin Sun
 */
public class ARIMASimTest {

    @Test
    public void test_0010() {
        int size = 100000;
        double[] mu = new double[]{1., 1.};
        Matrix[] phi = new Matrix[]{
            new DenseMatrix(new double[][]{
                {0.5, 0.5},
                {0., 0.5}
            })};
        Matrix[] theta = new Matrix[]{phi[0]};
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1, 0.2},
                    {0.2, 1}
                });
        ARMAModel model = new ARMAModel(new DenseVector(mu), phi, theta, sigma);

        ARIMASim instance = new ARIMASim(size, model, 1234567890L);

        assertEquals(2, instance.dimension(), 1e-15);
        assertEquals(size, instance.size(), 1e-15);

        double[] mean = new Inverse(new DenseMatrix(2, 2).ONE().minus(model.AR(1))).multiply(new DenseVector(mu)).toArray();
        Matrix instanceM = instance.toMatrix();

        assertEquals(mean[0], new Mean(instanceM.getColumn(1).toArray()).value(), 5e-2);
        assertEquals(mean[1], new Mean(instanceM.getColumn(2).toArray()).value(), 5e-2);

        CovarianceMatrix cov = new CovarianceMatrix(instanceM);
        Matrix covExpected = new DenseMatrix(new double[][]{
                    {811. / 135., 101. / 45.},
                    {101. / 45., 7. / 3.}
                });
        assertTrue(AreMatrices.equal(covExpected, cov, 1e-1));
    }
}
