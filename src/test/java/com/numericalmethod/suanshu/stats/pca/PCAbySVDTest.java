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
package com.numericalmethod.suanshu.stats.pca;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class PCAbySVDTest {

    static DenseMatrix USArrests = new DenseMatrix(new double[][]{
                {13.2, 236, 58, 21.2},
                {10.0, 263, 48, 44.5},
                {8.1, 294, 80, 31.0},
                {8.8, 190, 50, 19.5},
                {9.0, 276, 91, 40.6},
                {7.9, 204, 78, 38.7},
                {3.3, 110, 77, 11.1},
                {5.9, 238, 72, 15.8},
                {15.4, 335, 80, 31.9},
                {17.4, 211, 60, 25.8},
                {5.3, 46, 83, 20.2},
                {2.6, 120, 54, 14.2},
                {10.4, 249, 83, 24.0},
                {7.2, 113, 65, 21.0},
                {2.2, 56, 57, 11.3},
                {6.0, 115, 66, 18.0},
                {9.7, 109, 52, 16.3},
                {15.4, 249, 66, 22.2},
                {2.1, 83, 51, 7.8},
                {11.3, 300, 67, 27.8},
                {4.4, 149, 85, 16.3},
                {12.1, 255, 74, 35.1},
                {2.7, 72, 66, 14.9},
                {16.1, 259, 44, 17.1},
                {9.0, 178, 70, 28.2},
                {6.0, 109, 53, 16.4},
                {4.3, 102, 62, 16.5},
                {12.2, 252, 81, 46.0},
                {2.1, 57, 56, 9.5},
                {7.4, 159, 89, 18.8},
                {11.4, 285, 70, 32.1},
                {11.1, 254, 86, 26.1},
                {13.0, 337, 45, 16.1},
                {0.8, 45, 44, 7.3},
                {7.3, 120, 75, 21.4},
                {6.6, 151, 68, 20.0},
                {4.9, 159, 67, 29.3},
                {6.3, 106, 72, 14.9},
                {3.4, 174, 87, 8.3},
                {14.4, 279, 48, 22.5},
                {3.8, 86, 45, 12.8},
                {13.2, 188, 59, 26.9},
                {12.7, 201, 80, 25.5},
                {3.2, 120, 80, 22.9},
                {2.2, 48, 32, 11.2},
                {8.5, 156, 63, 20.7},
                {4.0, 145, 73, 26.2},
                {5.7, 81, 39, 9.3},
                {2.6, 53, 66, 10.8},
                {6.8, 161, 60, 15.6}});

    /**
     * The variances of the variables in the USArrests data vary by orders of magnitude,
     * so scaling is appropriate. The first test uses the unscaled matrix, which is
     * not advisable. We use it only to compare with the R outputs.
     *
     * R code:
     *
    pc.1 <- prcomp(USArrests)
    (nFactors <- ncol(USArrests))
    (nObs <- nrow(USArrests))
    (mean.1 <- pc.1$center)
    (scale.1 <- if (!pc.1$scale) {rep(1, nFactors)} else pc.1$scale)
    (sdev.1 <- pc.1$sdev)
    (loadings.1 <- pc.1$rotation)
    (proportion.1 <- pc.1$sdev ^ 2 / sum(pc.1$sdev ^ 2))
    (cumprop.1 <- cumsum(proportion.1))
    (scores.1 <- pc.1$x)
     *
     */
    @Test
    public void test_0010() {
        PCAbySVD pca_1 = new PCAbySVD(USArrests, true, false);

        int p = pca_1.nFactors();
        int n = pca_1.nObs();
        Vector mean_1 = pca_1.mean();
        Vector scale_1 = pca_1.scale();
        Vector sdev_1 = pca_1.sdPrincipalComponent();
        Matrix loadings_1 = pca_1.loadings();
        Vector proportion_1 = pca_1.proportionVar();
        Vector cumprop_1 = pca_1.cumulativeProportionVar();
        Matrix scores_1 = pca_1.scores();

        assertEquals(p, 4, 1e-15);
        assertEquals(n, 50, 1e-15);
        assertArrayEquals(
                new double[]{7.788, 170.760, 65.540, 21.232},
                mean_1.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{1., 1., 1., 1.},
                scale_1.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{83.732400, 14.212402, 6.489426, 2.482790},
                sdev_1.toArray(),
                1e-5);
        assertTrue(AreMatrices.equal( //The signs of the columns of the loading are arbitrary.
                loadings_1,
                new DenseMatrix(new double[][]{
                    {0.04170432, -0.04482166, -0.07989066, -0.99492173},
                    {0.99522128, -0.05876003, 0.06756974, 0.03893830},
                    {0.04633575, 0.97685748, 0.20054629, -0.05816914},
                    {0.07515550, 0.20071807, -0.97408059, 0.07232502}}),
                1e-5));
        assertArrayEquals(
                new double[]{0.9655342206, 0.0278173366, 0.0057995349, 0.0008489079},
                proportion_1.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{0.9655342, 0.9933516, 0.9991511, 1.0000000},
                cumprop_1.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{64.802164, -11.4480074, 2.49493284, -2.4079009},
                scores_1.getRow(1).toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{92.827450, -17.9829427, -20.12657487, 4.0940470},
                scores_1.getRow(2).toArray(),
                1e-5);
    }

    /**
     * R code:
     *
    pc.2 <- prcomp(USArrests, scale. = TRUE)
    (nFactors <- ncol(USArrests))
    (nObs <- nrow(USArrests))
    (mean.2 <- pc.2$center)
    (scale.2 <- if (!pc.2$scale) {rep(1, nFactors)} else pc.2$scale)
    (sdev.2 <- pc.2$sdev)
    (loadings.2 <- pc.2$rotation)
    (proportion.2 <- pc.2$sdev ^ 2 / sum(pc.2$sdev ^ 2))
    (cumprop.2 <- cumsum(proportion.2))
    (scores.2 <- pc.2$x)
     *
     */
    @Test
    public void test_0020() {
        PCAbySVD pca_2 = new PCAbySVD(USArrests, true, true);

        int p = pca_2.nFactors();
        int n = pca_2.nObs();
        Vector mean_2 = pca_2.mean();
        Vector scale_2 = pca_2.scale();
        Vector sdev_2 = pca_2.sdPrincipalComponent();
        Matrix loadings_2 = pca_2.loadings();
        Vector proportion_2 = pca_2.proportionVar();
        Vector cumprop_2 = pca_2.cumulativeProportionVar();
        Matrix scores_2 = pca_2.scores();

        assertEquals(p, 4, 1e-15);
        assertEquals(n, 50, 1e-15);
        assertArrayEquals(
                new double[]{7.788, 170.760, 65.540, 21.232},
                mean_2.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{4.355510, 83.337661, 14.474763, 9.366385},
                scale_2.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{1.5748783, 0.9948694, 0.5971291, 0.4164494},
                sdev_2.toArray(),
                1e-5);
        assertTrue(AreMatrices.equal( //The signs of the columns of the loading are arbitrary.
                loadings_2,
                new DenseMatrix(new double[][]{
                    {0.5358995, -0.4181809, -0.3412327, 0.64922780},
                    {0.5831836, -0.1879856, -0.2681484, -0.74340748},
                    {0.2781909, 0.8728062, -0.3780158, 0.13387773},
                    {0.5434321, 0.1673186, 0.8177779, 0.08902432}}),
                1e-3));
        assertArrayEquals(
                new double[]{0.62006039, 0.24744129, 0.08914080, 0.04335752},
                proportion_2.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{0.6200604, 0.8675017, 0.9566425, 1.0000000},
                cumprop_2.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{0.97566045, -1.12200121, -0.43980366, 0.154696581},
                scores_2.getRow(1).toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{1.93053788, -1.06242692, 2.01950027, -0.434175454},
                scores_2.getRow(2).toArray(),
                1e-5);
    }
}
