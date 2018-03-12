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

import com.numericalmethod.suanshu.analysis.function.matrix.R1toMatrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearRepresentationTest {

    /**
     * Example 11.3.2.
     */
    @Test
    public void test_0010() {
        Matrix PHI1 = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0, 0.5}
                });

        Matrix THETA1 = PHI1.t();

        LinearRepresentation instance = new LinearRepresentation(new ARMAModel(
                new Matrix[]{PHI1},
                new Matrix[]{THETA1}));

        R1toMatrix PSI = new R1toMatrix() {

            @Override
            public Matrix evaluate(double x) {
                int j = (int) x;

                Matrix M = new DenseMatrix(2, 2);
                M.set(1, 1, j + 1);
                M.set(1, 2, 2 * j - 1);
                M.set(2, 1, 1);
                M.set(2, 2, 2);
                M = M.scaled(Math.pow(2, -j));

                return M;
            }
        };

        assertTrue(AreMatrices.equal(instance.PSI[0].ONE(), instance.PSI[0], 0));
        for (int j = 1; j < LinearRepresentation.DEFAULT_NUMBER_OF_LAGS; ++j) {
            assertTrue(AreMatrices.equal(PSI.evaluate(j), instance.PSI[j], 0));
        }
    }

    /**
     * MA(1)
     */
    @Test
    public void test_0020() {
        Matrix THETA1 = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0, 0.5}
                });

        LinearRepresentation instance = new LinearRepresentation(new ARMAModel(
                null,
                new Matrix[]{THETA1}));//a simple MA(1) model

        assertTrue(AreMatrices.equal(instance.PSI[0].ONE(), instance.PSI[0], 0));
        assertTrue(AreMatrices.equal(THETA1, instance.PSI[1], 0));
        for (int j = 2; j < LinearRepresentation.DEFAULT_NUMBER_OF_LAGS; ++j) {
            assertTrue(AreMatrices.equal(instance.PSI[j].ZERO(), instance.PSI[j], 0));
        }
    }

    /**
     * Make sure there is null pointer exception.
     * TODO: check the correctness of the numbers.
     */
    @Test
    public void test_0030() {
        Matrix PHI1 = new DenseMatrix(new double[][]{
                    {0.5, 0.5},
                    {0, 0.5}
                });

        Matrix THETA1 = PHI1.t();

        LinearRepresentation instance = new LinearRepresentation(new ARMAModel(
                new Matrix[]{PHI1},
                null));
    }
}
