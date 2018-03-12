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
package com.numericalmethod.suanshu.stats.factoranalysis;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin Sun
 */
public class FactorAnalysisMLETest {

    @Test
    public void test_0010() throws Exception {
        Matrix corr = new DenseMatrix(new double[][]{
                    {1.0000000, 0.9393083, 0.5128866, 0.4320310, 0.4664948, 0.4086076},
                    {0.9393083, 1.0000000, 0.4124441, 0.4084281, 0.4363925, 0.4326113},
                    {0.5128866, 0.4124441, 1.0000000, 0.8770750, 0.5128866, 0.4320310},
                    {0.4320310, 0.4084281, 0.8770750, 1.0000000, 0.4320310, 0.4323259},
                    {0.4664948, 0.4363925, 0.5128866, 0.4320310, 1.0000000, 0.9473451},
                    {0.4086076, 0.4326113, 0.4320310, 0.4323259, 0.9473451, 1.0000000}
                });

        FactorAnalysisMLE instance = new FactorAnalysisMLE(corr, 3, FactorAnalysisMLE.GRADIENT.NUMERICAL, 1e-8, 500);

        Vector estimate = instance.estimate(new DenseVector(new double[]{
                    0.05682148, 0.06241503, 0.10299240, 0.12598095, 0.04840647, 0.05190937
                }));
//        Vector estimate = instance.estimate(new DenseVector(
//                0.005000, 0.099435, 0.005000, 0.219751, 0.084893, 0.005000));
//        Vector estimate = instance.estimate(new DenseVector(new double[]{
//                    0.056821425134940505,
//                    0.062414967488358314,
//                    0.10299228052258123,
//                    0.12598081838004124,
//                    0.048406415052776,
//                    0.05190930971473107
//                }));

//        System.out.println(new DenseVector(estimate));
//        System.out.println(instance.nL.evaluate(estimate));
//        System.out.println(new DenseVector(0.005000, 0.099435, 0.005000, 0.219751, 0.084893, 0.005000));
//        System.out.println(instance.nL.evaluate(new DenseVector(0.005000, 0.099435, 0.005000, 0.219751, 0.084893, 0.005000)));

        Vector expected = new DenseVector(0.005000, 0.099435, 0.005000, 0.219751, 0.084893, 0.005000);//from R
        Vector ratio = expected.divide(estimate);

        assertEquals(estimate.size(), 6);
        assertArrayEquals(R.rep(1., 6), ratio.toArray(), 3e-2);
        assertTrue(instance.nL.evaluate(estimate) < instance.nL.evaluate(expected));//compare likelihood
    }

    //TODO: use L-BFGS-B; otherwise could compute the gradient in an "invalid" region
//    @Test
    public void test_0020() throws Exception {
        Matrix corr = new DenseMatrix(new double[][]{
                    {1.0000000, 0.9393083, 0.5128866, 0.4320310, 0.4664948, 0.4086076},
                    {0.9393083, 1.0000000, 0.4124441, 0.4084281, 0.4363925, 0.4326113},
                    {0.5128866, 0.4124441, 1.0000000, 0.8770750, 0.5128866, 0.4320310},
                    {0.4320310, 0.4084281, 0.8770750, 1.0000000, 0.4320310, 0.4323259},
                    {0.4664948, 0.4363925, 0.5128866, 0.4320310, 1.0000000, 0.9473451},
                    {0.4086076, 0.4326113, 0.4320310, 0.4323259, 0.9473451, 1.0000000}
                });

        FactorAnalysisMLE instance = new FactorAnalysisMLE(corr, 3, FactorAnalysisMLE.GRADIENT.ANALYTICAL, 1e-8, 100);

        Vector estimate = instance.estimate(new DenseVector(
                0.05682148, 0.06241503, 0.10299240, 0.12598095, 0.04840647, 0.05190937));

        System.out.println(new DenseVector(estimate));
        System.out.println(instance.nL.evaluate(estimate));

        assertEquals(estimate.size(), 6);
        assertArrayEquals(
                new double[]{0.005000, 0.099435, 0.005000, 0.219751, 0.084893, 0.005000},
                estimate.toArray(),
                1e-6);
    }
}
