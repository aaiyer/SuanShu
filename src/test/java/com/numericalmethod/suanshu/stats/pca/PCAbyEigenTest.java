package com.numericalmethod.suanshu.stats.pca;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin Sun
 */
public class PCAbyEigenTest {

    /**
     * The variances of the variables in the USArrests data vary by orders of magnitude,
     * so scaling is appropriate. The first test uses the covariance matrix, which is
     * not advisable. We use it only to compare with the R outputs.
     *
     * R code:
     *
    pc.cov <- princomp(USArrests)
    (nFactors <- ncol(USArrests))
    (nObs <- pc.cov$n.obs)
    (mean.cov <- pc.cov$center)
    (scale.cov <- pc.cov$scale)
    (sdev.cov <- pc.cov$sdev)
    (sdevadj.cov <- pc.cov$sdev * sqrt(nObs / (nObs - 1))) # adjusted standard deviations)
    (loadings.cov <- pc.cov$loadings) # note that blank entries are small but not zero
    (proportion.cov <- pc.cov$sdev ^ 2 / sum(pc.cov$sdev ^ 2))
    (cumprop.cov <- cumsum(proportion.cov))
    (scores.cov <- pc.cov$scores)
     */
    @Test
    public void test_0010() {
        PCAbyEigen pca_cov = new PCAbyEigen(PCAbySVDTest.USArrests, false);

        int p = pca_cov.nFactors();
        int n = pca_cov.nObs();
        Vector mean_cov = pca_cov.mean();
        Vector scale_cov = pca_cov.scale();
        Vector sdev_cov = pca_cov.sdPrincipalComponent();
        Matrix loadings_cov = pca_cov.loadings();
        Vector proportion_cov = pca_cov.proportionVar();
        Vector cumprop_cov = pca_cov.cumulativeProportionVar();
        Matrix scores_cov = pca_cov.scores();

        assertEquals(p, 4, 1e-15);
        assertEquals(n, 50, 1e-15);
        assertArrayEquals(
                new double[]{7.788, 170.760, 65.540, 21.232},
                mean_cov.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{1., 1., 1., 1.},
                scale_cov.toArray(),
                1e-5);
        assertTrue(AreMatrices.equal(//The standard deviations differ by a factor of sqrt(50 / 49), since we used divisor (nObs - 1) for the sample covariance matrix
                new DenseVector(new double[]{82.890847, 14.069560, 6.424204, 2.457837}).scaled(Math.sqrt((double) n / (n - 1))),
                sdev_cov,
                1e-5));
        assertTrue(AreMatrices.equal( //The signs of the columns of the loading are arbitrary.
                loadings_cov,
                new DenseMatrix(new double[][]{
                    {0.042, -0.045, 0.080, -0.995},
                    {0.995, -0.059, -0.068, 0.039},
                    {0.046, 0.977, -0.201, -0.058},
                    {0.075, 0.201, 0.974, 0.072}}),
                1e-3));
        assertArrayEquals(
                new double[]{0.9655342206, 0.0278173366, 0.0057995349, 0.0008489079},
                proportion_cov.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{0.9655342, 0.9933516, 0.9991511, 1.0000000},
                cumprop_cov.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{64.802164, -11.4480074, -2.49493284, -2.4079009},
                scores_cov.getRow(1).toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{92.827450, -17.9829427, 20.12657487, 4.0940470},
                scores_cov.getRow(2).toArray(),
                1e-5);
    }

    /**
     * R code:
     *
    pc.cor <- princomp(USArrests, cor = TRUE)
    (nFactors <- ncol(USArrests))
    (nObs <- pc.cor$n.obs)
    (mean.cor <- pc.cor$center)
    (scale.cor <- pc.cor$scale)
    (scaleadj.cor <- pc.cor$scale * sqrt(nObs / (nObs - 1))) # adjusted scaling vector
    (sdev.cor <- pc.cor$sdev)
    (loadings.cor <- pc.cor$loadings) # note that blank entries are small but not zero
    (proportion.cor <- pc.cor$sdev ^ 2 / sum(pc.cor$sdev ^ 2))
    (cumprop.cor <- cumsum(proportion.cor))
    (scores.cor <- pc.cor$scores / sqrt(nObs / (nObs - 1)))
     */
    @Test
    public void test_0020() {
        PCAbyEigen pca_cor = new PCAbyEigen(PCAbySVDTest.USArrests, true);

        int p = pca_cor.nFactors();
        int n = pca_cor.nObs();
        Vector mean_cor = pca_cor.mean();
        Vector scale_cor = pca_cor.scale();
        Vector sdev_cor = pca_cor.sdPrincipalComponent();
        Matrix loadings_cor = pca_cor.loadings();
        Vector proportion_cor = pca_cor.proportionVar();
        Vector cumprop_cor = pca_cor.cumulativeProportionVar();
        Matrix scores_cor = pca_cor.scores();

        assertEquals(p, 4, 1e-15);
        assertEquals(n, 50, 1e-15);
        assertArrayEquals(
                new double[]{7.788, 170.760, 65.540, 21.232},
                mean_cor.toArray(),
                1e-5);
        assertTrue(AreMatrices.equal( //The scales differ by a factor of sqrt(50 / 49), since we used divisor (nObs - 1) for the sample covariance matrix
                scale_cor,
                new DenseVector(new double[]{4.311735, 82.500075, 14.329285, 9.272248}).scaled(Math.sqrt((double) n / (n - 1))),
                1e-5));
        assertArrayEquals(
                new double[]{1.5748783, 0.9948694, 0.5971291, 0.4164494},
                sdev_cor.toArray(),
                1e-5);
        assertTrue(AreMatrices.equal( //The signs of the columns of the loading are arbitrary.
                loadings_cor,
                new DenseMatrix(new double[][]{
                    {0.536, -0.418, -0.341, 0.649},
                    {0.583, -0.188, -0.268, -0.743},
                    {0.278, 0.873, -0.378, 0.134},
                    {0.543, 0.167, 0.818, 0.089}}),
                1e-3));
        assertArrayEquals(
                new double[]{0.62006039, 0.24744129, 0.08914080, 0.04335752},
                proportion_cor.toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{0.6200604, 0.8675017, 0.9566425, 1.0000000},
                cumprop_cor.toArray(),
                1e-5);
        //The signs of the columns of the scores are arbitrary. The scores should also differ by a factor of sqrt(50 / 49)
        assertArrayEquals(
                new double[]{0.97566045, -1.12200121, -0.43980366, 0.154696581},
                scores_cor.getRow(1).toArray(),
                1e-5);
        assertArrayEquals(
                new double[]{1.93053788, -1.06242692, 2.01950027, -0.434175454},
                scores_cor.getRow(2).toArray(),
                1e-5);
    }
}
