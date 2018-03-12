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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.integration.sde;

import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.Ft;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.SDE;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.descriptive.Covariance;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.MultiVariateRealization;
import com.numericalmethod.suanshu.stats.test.distribution.normality.ShapiroWilk;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.TimeGrid;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.descriptive.moment.Variance;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.coefficients.ConstantSigma1;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde.coefficients.Drift;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.EvenlySpacedGrid;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class EulerTest {

    /**
     * Same as generating a correlated multi-normal using Cholesky decomposition.
     */
    @Test
    public void test_0010() {
        final double sd11 = 1.5;
        final double sd22 = 3.7;
        final double rho = 0.5;

        final Matrix A = new DenseMatrix(new double[][]{//Cholesky decomposition
                    {sd11, 0},
                    {rho * sd22, sqrt(1 - rho * rho) * sd22}
                });

        SDE sde = new SDE(
                new Drift() {

                    public Vector evaluate(Ft ft) {
                        return new DenseVector(2, 0);
                    }
                },
                new ConstantSigma1(A),
                A.nCols()) {
        };

        int nT = 5000;
        double dt = 1. / nT;
        TimeGrid t = new EvenlySpacedGrid(0, 1, nT);

        Construction Xt = new Euler(sde, t);
        Xt.seed(12345678912L);

        MultiVariateRealization xt = Xt.nextRealization(new DenseVector(2, 0));//[0, 0]
        MultiVariateRealization.Iterator it = xt.iterator();
        Vector x0 = it.nextValue();
        assertEquals(x0.ZERO(), x0);

        double[] x1 = new double[xt.size()];
        x1[0] = 0;
        double[] x2 = new double[xt.size()];
        x2[0] = 0;

        for (int i = 1; it.hasNext(); ++i) {
            Vector x = it.nextValue();
            x1[i] = x.get(1);
            x2[i] = x.get(2);
        }

        double[] dx1 = R.diff(x1);
        ShapiroWilk test = new ShapiroWilk(dx1);
        assertTrue(test.pValue() > 0.10);

        Variance var1 = new Variance(dx1);
        assertEquals(dt * sd11 * sd11, var1.value(), 2e-5);

        double[] dx2 = R.diff(x2);
        test = new ShapiroWilk(dx2);
        assertTrue(test.pValue() > 0.10);

        Variance var2 = new Variance(dx2);
        assertEquals(dt * sd22 * sd22, var2.value(), 1e-4);

        Covariance cov = new Covariance(new double[][]{dx1, dx2});
        assertEquals(dt * rho * sd11 * sd22, abs(cov.value()), 1e-4);
    }
}
