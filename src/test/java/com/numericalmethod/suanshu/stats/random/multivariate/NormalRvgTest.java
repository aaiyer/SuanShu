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
package com.numericalmethod.suanshu.stats.random.multivariate;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.stats.descriptive.CovarianceMatrix;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Kevin Sun
 */
public class NormalRvgTest {

    @Test
    public void test_0010() {
        DenseVector mu = new DenseVector(new double[]{0., 0.});
        Matrix sigma = new DenseMatrix(2, 2).ONE();
        NormalRvg instance = new NormalRvg(mu, sigma);
        instance.seed(123456789L);

        final int size = 10000;
        double[][] sample = new double[size][];

        Mean mean1 = new Mean();
        Mean mean2 = new Mean();
        for (int i = 0; i < size; ++i) {
            double[] x = instance.nextVector();
            mean1.addData(x[0]);
            mean2.addData(x[1]);

            sample[i] = x;
        }

        assertEquals(mu.get(1), mean1.value(), 0.05);
        assertEquals(mu.get(2), mean2.value(), 0.05);

        DenseMatrix SAMPLE = new DenseMatrix(sample);
        CovarianceMatrix cov = new CovarianceMatrix(SAMPLE);
        assertEquals(0, MatrixMeasure.Frobenius(sigma.minus(cov)), 0.02);
    }

    @Test
    public void test_0020() {
        DenseVector mu = new DenseVector(new double[]{-2., 2.});
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1., 0.5},
                    {0.5, 1.}
                });
        NormalRvg instance = new NormalRvg(mu, sigma);
        instance.seed(123456789L);

        final int size = 10000;
        double[][] sample = new double[size][];

        Mean mean1 = new Mean();
        Mean mean2 = new Mean();
        for (int i = 0; i < size; ++i) {
            double[] x = instance.nextVector();
            mean1.addData(x[0]);
            mean2.addData(x[1]);

            sample[i] = x;
        }

        assertEquals(mu.get(1), mean1.value(), 0.05);
        assertEquals(mu.get(2), mean2.value(), 0.05);

        DenseMatrix SAMPLE = new DenseMatrix(sample);
        CovarianceMatrix cov = new CovarianceMatrix(SAMPLE);
        assertEquals(0, MatrixMeasure.Frobenius(sigma.minus(cov)), 0.02);
    }

    @Test
    public void test_0030() {
        DenseVector mu = new DenseVector(new double[]{-2., 2.});
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1., 0.1},
                    {0.2, 1.}
                });
        NormalRvg instance = new NormalRvg(mu, sigma);
        instance.seed(123456789L);

        final int size = 10000;
        double[][] sample = new double[size][];

        Mean mean1 = new Mean();
        Mean mean2 = new Mean();
        for (int i = 0; i < size; ++i) {
            double[] x = instance.nextVector();
            mean1.addData(x[0]);
            mean2.addData(x[1]);

            sample[i] = x;
        }

        assertEquals(mu.get(1), mean1.value(), 0.05);
        assertEquals(mu.get(2), mean2.value(), 0.05);

        DenseMatrix SAMPLE = new DenseMatrix(sample);
        CovarianceMatrix cov = new CovarianceMatrix(SAMPLE);
//        assertEquals(0, MatrixMeasure.Frobenius(sigma.minus(cov)), 0.005);
    }

    /**
     * sigma is non-diagonalizable
     */
    @Test
    public void test_0040() {
        DenseVector mu = new DenseVector(new double[]{-2., 2.});
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1., 0.5},
                    {0., 1.}
                });
        NormalRvg instance = new NormalRvg(mu, sigma);
        instance.seed(123456789L);

        final int size = 10000;
        double[][] sample = new double[size][];

        Mean mean1 = new Mean();
        Mean mean2 = new Mean();
        for (int i = 0; i < size; ++i) {
            double[] x = instance.nextVector();
            mean1.addData(x[0]);
            mean2.addData(x[1]);

            sample[i] = x;
        }

        assertEquals(mu.get(1), mean1.value(), 0.06);
        assertEquals(mu.get(2), mean2.value(), 0.05);

        DenseMatrix SAMPLE = new DenseMatrix(sample);
        CovarianceMatrix cov = new CovarianceMatrix(SAMPLE);
//        assertEquals(0, MatrixMeasure.Frobenius(sigma.minus(cov)), 0.005);
    }

    @Test
    public void test_0050() {
        DenseVector mu = new DenseVector(new double[]{-2., 0, 2.});
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1, 0.4, 0.2},
                    {0.4, 1, 0.5},
                    {0.2, 0.5, 1}
                });
        NormalRvg instance = new NormalRvg(mu, sigma);
        instance.seed(123456789L);

        final int size = 10000;
        double[][] sample = new double[size][];

        Mean mean1 = new Mean();
        Mean mean2 = new Mean();
        for (int i = 0; i < size; ++i) {
            double[] x = instance.nextVector();
            mean1.addData(x[0]);
            mean2.addData(x[1]);

            sample[i] = x;
        }

        assertEquals(mu.get(1), mean1.value(), 0.05);
        assertEquals(mu.get(2), mean2.value(), 0.05);

        DenseMatrix SAMPLE = new DenseMatrix(sample);
        CovarianceMatrix cov = new CovarianceMatrix(SAMPLE);
        assertEquals(0, MatrixMeasure.Frobenius(sigma.minus(cov)), 0.04);
    }

    /**
     * asymmetric sigma
     */
    @Test
    public void test_0060() {
        DenseVector mu = new DenseVector(new double[]{-2., 0, 2.});
        Matrix sigma = new DenseMatrix(new double[][]{
                    {1, 0.4, 0.3},
                    {0.4, 1, 0.5},
                    {0.1, 0.5, 1}
                });
        NormalRvg instance = new NormalRvg(mu, sigma);
        instance.seed(123456789L);

        final int size = 10000;
        double[][] sample = new double[size][];

        Mean mean1 = new Mean();
        Mean mean2 = new Mean();
        for (int i = 0; i < size; ++i) {
            double[] x = instance.nextVector();
            mean1.addData(x[0]);
            mean2.addData(x[1]);

            sample[i] = x;
        }

        assertEquals(mu.get(1), mean1.value(), 0.05);
        assertEquals(mu.get(2), mean2.value(), 0.05);

        DenseMatrix SAMPLE = new DenseMatrix(sample);
        CovarianceMatrix cov = new CovarianceMatrix(SAMPLE);
//        assertEquals(0, MatrixMeasure.Frobenius(sigma.minus(cov)), 0.005);
    }
}
