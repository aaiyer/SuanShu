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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearRepresentationTest {

    @Test
    public void testPsi() {
        double[] ar = new double[]{0.2};
        double[] ma = new double[]{0.3};

        Matrix PHI = new DenseMatrix(new double[][]{
                    ar
                });

        Matrix THETA = new DenseMatrix(new double[][]{
                    ma
                });

        com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.LinearRepresentation expected =
                new com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.LinearRepresentation(
                new com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma.ARMAModel(
                new Matrix[]{PHI},
                new Matrix[]{THETA}));

        ARMAModel arma = new ARMAModel(ar, ma);
        LinearRepresentation instance = new LinearRepresentation(arma);

        double[] psi = instance.psi();
        for (int j = 0; j < LinearRepresentation.DEFAULT_NUMBER_OF_LAGS; ++j) {
            assertEquals(expected.PSI[j].get(1, 1), psi[j], 0);
        }
    }
}
