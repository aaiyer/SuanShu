/*
 * Copyright (c)
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
package com.numericalmethod.suanshu.matrix.doubles.factorization.svd;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.BidiagonalMatrix;
import com.numericalmethod.suanshu.misc.R;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GloubKahanSVDStepTest {

    @Test
    public void test_GloubKahanSVDStep_0010() {
        BidiagonalMatrix B = new BidiagonalMatrix(new double[][]{
                    {2, 3, 4, 5},
                    {10, 11, 12, 13, 14}
                });

        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
        Matrix U = instance.U();
        Matrix V = instance.V();

        BidiagonalMatrix D = instance.UtBV();
        assertTrue(AreMatrices.equal(D, U.t().multiply(B).multiply(V), 1e-14));
    }

    @Test
    public void test_GloubKahanSVDStep_0020() {
        BidiagonalMatrix B = new BidiagonalMatrix(new double[][]{
                    {2, 3, 4, 5, 5329.32, 12.56, 39.2, 13.023, 245},
                    {10, 11, 12, 13, 14, 0.01, 0.0001, 0.00000001, 10, 999.999}
                });

        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
        Matrix U = instance.U();
        Matrix V = instance.V();

        BidiagonalMatrix D = instance.UtBV();
        assertTrue(AreMatrices.equal(D, U.t().multiply(B).multiply(V), 1e-12));
    }

    @Test
    public void test_GloubKahanSVDStep_0030() {
        BidiagonalMatrix B = new BidiagonalMatrix(new double[][]{
                    R.seq(1d, 20d, 0.5),
                    R.seq(1d, 20.5, 0.5)
                });

        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
        Matrix U = instance.U();
        Matrix V = instance.V();

        BidiagonalMatrix D = instance.UtBV();
        assertTrue(AreMatrices.equal(D, U.t().multiply(B).multiply(V), 1e-14));
    }

    @Test
    public void test_GloubKahanSVDStep_0040() {
        BidiagonalMatrix B = new BidiagonalMatrix(new double[][]{
                    R.seq(1d, 50d, 0.5),
                    R.seq(1d, 50.5, 0.5)
                });

        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
        Matrix U = instance.U();
        Matrix V = instance.V();

        BidiagonalMatrix D = instance.UtBV();
        assertTrue(AreMatrices.equal(D, U.t().multiply(B).multiply(V), 1e-13));
    }
//    /**
//     * Test of class GloubKahanSVDStep.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void test_GloubKahanSVDStep_0200() {
////
//        Matrix B = new BidiagonalMatrix(new double[][]{
//                    {0},
//                    {1, 2}
//                });
//
//        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
//        Matrix U = instance.U();
//        Matrix V = instance.V();
//
//        BidiagonalMatrix D = instance.UtBV();
//        assertTrue(AreMatrices.equal(D, U.t().multiply(B).multiply(V), 1e-14));
//    }
//
//    /**
//     * Test of class GloubKahanSVDStep.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void test_GloubKahanSVDStep_0210() {
////
//        Matrix B = new BidiagonalMatrix(new double[][]{
//                    {3},
//                    {1, 0}
//                });
//
//        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
//    }
//
//    /**
//     * Test of class GloubKahanSVDStep.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void test_GloubKahanSVDStep_0220() {
////
//        Matrix B = new BidiagonalMatrix(new double[][]{
//                    {3},
//                    {0, 1}
//                });
//
//        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
//    }
//
//    /**
//     * Test of class GloubKahanSVDStep.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void test_GloubKahanSVDStep_0230() {
////
//        Matrix B = new BidiagonalMatrix(new double[][]{
//                    {0},
//                    {0, 0}
//                });
//
//        GloubKahanSVDStep instance = new GloubKahanSVDStep(B);
//    }
}
