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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen;

import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen.Method;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.number.NumberUtils;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.test.NumberAssert;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.operation.VectorSpace;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class EigenTest {

    //<editor-fold defaultstate="collapsed" desc="tests for CHARACTERISTIC_POLYNOMIAL">
    @Test
    public void test_CHARACTERISTIC_POLYNOMIAL_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });
        NumberAssert.assertSameList(
                Arrays.asList(6.464101615137754, -0.464101615137755),
                new Eigen(A, Eigen.Method.CHARACTERISTIC_POLYNOMIAL, 0).getEigenvalues(),
                1e-15);
    }

    @Test
    public void test_CHARACTERISTIC_POLYNOMIAL_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.234, 61.972},
                    {3665, 12.361}
                });
        NumberAssert.assertSameList(
                Arrays.asList(-469.813801305634, 483.408801305634),
                new Eigen(A, Eigen.Method.CHARACTERISTIC_POLYNOMIAL, 0).getEigenvalues(),
                1e-12);
    }

    @Test
    public void test_CHARACTERISTIC_POLYNOMIAL_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.234, -61.972},
                    {3665, 12.361}
                });
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"6.7975-476.546353954943i", "6.7975+476.546353954943i"})),
                new Eigen(A, Eigen.Method.CHARACTERISTIC_POLYNOMIAL, 0).getEigenvalues(),
                1e-12);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for getEigenvalue">
    @Test
    public void test_eigenvalue_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });
        Eigen eigen = new Eigen(A, Eigen.Method.QR, 0);
        NumberAssert.assertSameList(
                Arrays.asList(6.464101615137754, -0.464101615137755),
                eigen.getEigenvalues(),
                1e-12);

        assertEquals(2, eigen.size());
        assertEquals(2, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            double eigenvalue = eigen.getRealEigenvalues()[i];
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
            assertEquals(1, property.algebraicMultiplicity());
        }
    }

    @Test
    public void test_eigenvalue_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });
        Eigen eigen = new Eigen(A);
        NumberAssert.assertSameList(
                Arrays.asList(6.464101615137754, -0.464101615137755),
                eigen.getEigenvalues(),
                1e-12);

        assertEquals(2, eigen.size());
        assertEquals(2, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            double eigenvalue = eigen.getRealEigenvalues()[i];
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
            assertEquals(1, property.algebraicMultiplicity());
        }
    }

    @Test
    public void test_eigenvalue_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.234, -61.972},
                    {3665, 12.361}
                });
        Eigen eigen = new Eigen(A, Eigen.Method.QR, 0);
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"6.7975-476.546353954943i", "6.7975+476.546353954943i"})),
                eigen.getEigenvalues(),
                1e-12);

        assertEquals(2, eigen.size());
        assertEquals(0, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            Number eigenvalue = eigen.getEigenvalue(i);
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
            assertEquals(1, property.algebraicMultiplicity());
        }
    }

    @Test
    public void test_eigenvalue_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.234, -61.972},
                    {3665, 12.361}
                });
        Eigen eigen = new Eigen(A);
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"6.7975-476.546353954943i", "6.7975+476.546353954943i"})),
                eigen.getEigenvalues(),
                1e-12);

        assertEquals(2, eigen.size());
        assertEquals(0, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            Number eigenvalue = eigen.getEigenvalue(i);
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
            assertEquals(1, property.algebraicMultiplicity());
        }
    }

    @Test
    public void test_eigenvalue_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1.234, -61.972, 0},
                    {0, 3665, 12.361, 0},
                    {0, 0, 0, 99}
                });
        Eigen eigen = new Eigen(A, Eigen.Method.QR, 0);
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"1",
                                                                  "99",
                                                                  "6.7975-476.546353954943i",
                                                                  "6.7975+476.546353954943i"})),
                eigen.getEigenvalues(),
                1e-12);

        assertEquals(4, eigen.size());
        assertEquals(2, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            Number eigenvalue = eigen.getEigenvalue(i);
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
            assertEquals(1, property.algebraicMultiplicity());
        }
    }

    @Test
    public void test_eigenvalue_0035() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0},
                    {0, 1.234, -61.972, 0},
                    {0, 3665, 12.361, 0},
                    {0, 0, 0, 99}
                });
        Eigen eigen = new Eigen(A);
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"1",
                                                                  "99",
                                                                  "6.7975-476.546353954943i",
                                                                  "6.7975+476.546353954943i"})),
                eigen.getEigenvalues(),
                1e-12);
        assertEquals(4, eigen.size());
        assertEquals(2, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            Number eigenvalue = eigen.getEigenvalue(i);
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
            assertEquals(1, property.algebraicMultiplicity());
        }
    }

    @Test
    public void test_eigenvalue_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0, 0},
                    {0, 0, 0, 1.234, -61.972, 0},
                    {0, 0, 0, 3665, 12.361, 0},
                    {0, 0, 0, 0, 0, 99}
                });
        Eigen eigen = new Eigen(A, Eigen.Method.QR, 0);//0 precision
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"1",
                                                                  "99",
                                                                  "6.7975-476.546353954943i",
                                                                  "6.7975+476.546353954943i"})),
                eigen.getEigenvalues(),
                1e-12);
        assertEquals(4, eigen.size());
        assertEquals(2, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            Number eigenvalue = eigen.getEigenvalue(i);
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
        }

        assertEquals(3, eigen.getProperty(1d).algebraicMultiplicity());
    }

    @Test
    public void test_eigenvalue_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0, 0},
                    {0, 0, 0, 1.234, -61.972, 0},
                    {0, 0, 0, 3665, 12.361, 0},
                    {0, 0, 0, 0, 0, 99}
                });
        Eigen eigen = new Eigen(A);//default precision
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"1",
                                                                  "99",
                                                                  "6.7975-476.546353954943i",
                                                                  "6.7975+476.546353954943i"})),
                eigen.getEigenvalues(),
                1e-12);
        assertEquals(4, eigen.size());
        assertEquals(2, eigen.getRealEigenvalues().length);

        for (int i = 0; i < eigen.size(); ++i) {
            Number eigenvalue = eigen.getEigenvalue(i);
            EigenProperty property = eigen.getProperty(eigenvalue);
            assertEquals(eigenvalue, property.eigenvalue());
            assertEquals(eigen.getEigenvalue(i), property.eigenvalue());
            assertEquals(eigen.getProperty(eigen.getEigenvalue(i)).algebraicMultiplicity(), property.algebraicMultiplicity());
        }

        assertEquals(3, eigen.getProperty(1d).algebraicMultiplicity());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for eigenbasis">
    @Test
    public void test_eigenbasis_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });
        Eigen instance = new Eigen(A, Eigen.Method.QR, 0);
        NumberAssert.assertSameList(
                Arrays.asList(6.464101615137754, -0.464101615137755),
                instance.getEigenvalues(),
                1e-15);

        EigenProperty prop = instance.getProperty(0);
        assertEquals(NumberUtils.parse("6.464101615137754"), instance.getEigenvalue(0));
        assertEquals(1, prop.geometricMultiplicity());
        assertTrue(instance.getProperty(0).eigenbasis().contains(
                new DenseVector(new double[]{0.5906904945688721, 0.8068982213550735})));//from debugger

        prop = instance.getProperty(1);
        assertEquals(NumberUtils.parse("-0.4641016151377546"), instance.getEigenvalue(1));
        assertEquals(1, prop.geometricMultiplicity());
        assertTrue(prop.eigenbasis().contains(
                new DenseVector(new double[]{-0.9390708015880442, 0.3437237693334403})));//from debugger
    }

    @Test
    public void test_eigenbasis_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });
        Eigen instance = new Eigen(A);
        NumberAssert.assertSameList(
                Arrays.asList(6.464101615137754, -0.464101615137755),
                instance.getEigenvalues(),
                1e-15);

        EigenProperty prop = instance.getProperty(0);
        assertEquals(NumberUtils.parse("6.464101615137754"), prop.eigenvalue());
        assertEquals(1, prop.geometricMultiplicity());
        assertTrue(prop.eigenbasis().contains(
                new DenseVector(new double[]{0.5906904945688721, 0.8068982213550735})));//from debugger

        prop = instance.getProperty(1);
        assertEquals(NumberUtils.parse("-0.4641016151377546"), prop.eigenvalue());
        assertEquals(1, prop.geometricMultiplicity());
        assertTrue(prop.eigenbasis().contains(
                new DenseVector(new double[]{-0.9390708015880442, 0.3437237693334403})));//from debugger
    }

    @Test
    public void test_eigenbasis_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, -3, 3},
                    {3, -5, 3},
                    {6, -6, 4}
                });
        Eigen instance = new Eigen(A, Eigen.Method.QR, 1e-14);
        NumberAssert.assertSameList(
                Arrays.asList(-2, 4),
                instance.getEigenvalues(),
                1e-12);

        Number eigenvalue = instance.getEigenvalue(0);
        assertEquals(new Double(3.9999999999999987), eigenvalue);
        EigenProperty prop = instance.getProperty(eigenvalue);
        assertEquals(1, prop.geometricMultiplicity());
        List<Vector> basis = prop.eigenbasis();
        VectorSpace vs = new VectorSpace(basis, 1e-15);
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.408248290463863, -0.408248290463863, -0.816496580927726})));

        eigenvalue = instance.getEigenvalue(1);
        assertEquals(new Double(-1.9999999999999993), eigenvalue);
        prop = instance.getProperty(eigenvalue);
        assertEquals(2, prop.geometricMultiplicity());
        basis = prop.eigenbasis();
        vs = new VectorSpace(basis, 1e-15);
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.408248290463863, 0.408248290463863, 0.816496580927726})));
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.140500278407039, -0.766809364126755, -0.626309085719716})));
    }

    @Test
    public void test_eigenbasis_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, -3, 3},
                    {3, -5, 3},
                    {6, -6, 4}
                });
        Eigen instance = new Eigen(A);
        NumberAssert.assertSameList(
                Arrays.asList(-2, 4),
                instance.getEigenvalues(),
                1e-12);

        Number eigenvalue = instance.getEigenvalue(0);
        assertEquals(new Double(3.9999999999999987), eigenvalue);
        EigenProperty prop = instance.getProperty(eigenvalue);
        assertEquals(1, prop.geometricMultiplicity());
        List<Vector> basis = prop.eigenbasis();
        VectorSpace vs = new VectorSpace(basis, 1e-15);
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.408248290463863, -0.408248290463863, -0.816496580927726})));

        eigenvalue = instance.getEigenvalue(1);
        assertEquals(new Double(-1.9999999999999993), eigenvalue);
        prop = instance.getProperty(eigenvalue);
        assertEquals(2, prop.geometricMultiplicity());
        basis = prop.eigenbasis();
        vs = new VectorSpace(basis, 1e-15);
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.408248290463863, 0.408248290463863, 0.816496580927726})));
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.140500278407039, -0.766809364126755, -0.626309085719716})));
    }

    @Test
    public void test_eigenbasis_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, -3, 3},
                    {3, -5, 3},
                    {6, -6, 4}
                });
        A = A.scaled(1e-100);
        Eigen instance = new Eigen(A);
        NumberAssert.assertSameList(
                Arrays.asList(-2e-100, 4e-100),
                instance.getEigenvalues(),
                1e-112);

        Number eigenvalue = instance.getEigenvalue(0);
        assertEquals(new Double(3.9999999999999986E-100), eigenvalue);
        EigenProperty prop = instance.getProperty(eigenvalue);
        assertEquals(1, prop.geometricMultiplicity());
        List<Vector> basis = prop.eigenbasis();
        VectorSpace vs = new VectorSpace(basis, 1e-15);
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.408248290463863, -0.408248290463863, -0.816496580927726})));

        eigenvalue = instance.getEigenvalue(1);
        assertEquals(new Double(-2.0000000000000008E-100), eigenvalue);
        prop = instance.getProperty(eigenvalue);
        assertEquals(2, prop.geometricMultiplicity());
        basis = prop.eigenbasis();
        vs = new VectorSpace(basis, 1e-15);
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.408248290463863, 0.408248290463863, 0.816496580927726})));
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.140500278407039, -0.766809364126755, -0.626309085719716})));
    }

    /**
     * Using a very small precision will make the rank computation wrong.
     * This in turn causes other subsequent errors.
     */
    @Test
    public void test_eigenbasis_0040() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, -3, 3},
                    {3, -5, 3},
                    {6, -6, 4}
                });
        Eigen instance = new Eigen(A, Eigen.Method.QR, SuanShuUtils.autoEpsilon(A));//use a smaller precision will compute the rank wrong
        NumberAssert.assertSameList(
                Arrays.asList(-2, 4),
                instance.getEigenvalues(),
                1e-12);

        Number eigenvalue = instance.getEigenvalue(0);
        assertEquals(new Double(3.9999999999999987), eigenvalue);
        EigenProperty prop = instance.getProperty(eigenvalue);
        assertEquals(1, prop.geometricMultiplicity());
        List<Vector> basis = prop.eigenbasis();
        VectorSpace vs = new VectorSpace(basis, 1e-15);
        assertTrue(vs.isSpanned(new DenseVector(new double[]{-0.408248290463863, -0.408248290463863, -0.816496580927726})));

        eigenvalue = instance.getEigenvalue(1);
        assertEquals(-1.9999999999999958, eigenvalue.doubleValue(), 1e-14);

        /*
         * Using 1e-15 as the precision will make the computer thinks that A is a full rank matrix.
         * Hence the getBasis is null.
         * The following assertions therefore fail.
         */
//        getBasis = instance.getProperty(getEigenvalue).eigenbasis();
//        vs = new VectorSpace(getBasis, 1e-15);
//        assertTrue(vs.isSpanned(new Vector(new double[]{-0.408248290463863, 0.408248290463863, 0.816496580927726})));
//        assertTrue(vs.isSpanned(new Vector(new double[]{-0.140500278407039, -0.766809364126755, -0.626309085719716})));
    }

    /**
     * test for multiplicity = 1
     * asymmetric matrix
     *
     * a = c(0.09777770144973694, -0.008495122726671599, -0.013904044256496344, 0.018195978004875962,
     * 0.02170482221352272, 0.07160963626748776, 0.08073606184864421, 0.007533998463136099,
     * 0.11330772498851954, 0.1514674566076728, 0.27995515088835266, -0.017122678011985634,
     * 0.2981288581469038, 0.04812388712259168, 0.04369939759017251, 0.20812382932411705)
     * A = t(matrix(a, 4, 4))
     *
     * eA = eigen(A)
     *
     * D = diag(eA$values)
     * Q = as.matrix(eA$vectors)
     *
     *
     */
    @Test
    public void test_eigenbasis_0050() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {0.09777770144973694, -0.008495122726671599, -0.013904044256496344, 0.018195978004875962},
                    {0.02170482221352272, 0.07160963626748776, 0.08073606184864421, 0.007533998463136099},
                    {0.11330772498851954, 0.1514674566076728, 0.27995515088835266, -0.017122678011985634},
                    {0.2981288581469038, 0.04812388712259168, 0.04369939759017251, 0.20812382932411705}
                });
        Eigen instance = new Eigen(A, Eigen.Method.QR, 1e-12);//100 * SuanShuUtils.autoEpsilon(A.to1DArray()));

        EigenProperty prop = instance.getProperty(0);
        assertEquals(1, prop.geometricMultiplicity());
        DenseVector expectedVector = new DenseVector(new double[]{-0.03525983390325232, 0.29463035758969425, 0.8765854536142578, 0.3788768124490728});//from R
        double diff = instance.getProperty(0).eigenbasis().get(0).minus(expectedVector).norm();
        assertEquals(0, diff, 1e-11);

        prop = instance.getProperty(1);
        assertEquals(1, prop.geometricMultiplicity());
        expectedVector = new DenseVector(new double[]{0.12611070493059301, 0.03121139002062104, -0.0592283766323340, 0.9897544840193390});//from R
        diff = instance.getProperty(1).eigenbasis().get(0).minus(expectedVector).norm();
        assertEquals(0, diff, 1e-11);
    }

    @Test
    public void test_eigenbasis_0060() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {0.19745476386608624, -0.0012134912602413395},
                    {-0.001213491260241405, 0.3777474748094125}
                });
        Eigen instance = new Eigen(A);

        EigenProperty prop = instance.getProperty(0);
        assertEquals(1, prop.geometricMultiplicity());
        assertNotNull(instance.getProperty(0).eigenbasis().get(0));

        prop = instance.getProperty(1);
        assertEquals(1, prop.geometricMultiplicity());
        assertNotNull(instance.getProperty(1).eigenbasis().get(0));
    }

    @Test
    public void test_eigenbasis_0070() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {0.08022536816079928, -1.3196665060569157E-5},
                    {-1.31966650605459E-5, 0.09014518443166611}
                });
        Eigen instance = new Eigen(A, Method.QR, 1e-10);

        EigenProperty prop = instance.getProperty(0);
        assertEquals(1, prop.geometricMultiplicity());
        assertNotNull(instance.getProperty(0).eigenbasis().get(0));

        prop = instance.getProperty(1);
        assertEquals(1, prop.geometricMultiplicity());
        assertNotNull(instance.getProperty(1).eigenbasis().get(0));
    }

    /**
     * a = matrix(c(4432119745.094424, 4322961.768192, -292966206.911152,
     * 4322961.768192, 4224.378379, -53529.033621,
     * -292966206.911152, -53529.033621, 6857546699.940956)
     * , 3,3)
     *
     * eigen(a)
     *
     */
    @Test
    public void test_eigenbasis_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {4432119745.094424, 4322961.768192, -292966206.911152},
                    {4322961.768192, 4224.378379, -53529.033621},
                    {-292966206.911152, -53529.033621, 6857546699.940956}
                });
        Eigen instance = new Eigen(A, Eigen.Method.QR, 1e-15);
        NumberAssert.assertSameList(
                Arrays.asList(6.892432233417672e+09, 4.397238435996086e+09, 1.144409179687500e-05),
                instance.getEigenvalues(),
                1e2);//from R

        EigenProperty prop = instance.getProperty(0);
//        assertEquals(NumberUtils.parse("6.892432233417672e+09"), instance.getEigenvalue(0));
        assertEquals(1, prop.geometricMultiplicity());
        assertTrue(instance.getProperty(0).eigenbasis().contains(
                new DenseVector(new double[]{-0.1182416298889091, -8.187355225812148E-5, 0.9929848489568891})));//from debugger

        prop = instance.getProperty(1);
//        assertEquals(NumberUtils.parse("4.397238435996086e+09"), instance.getEigenvalue(1));
        assertEquals(1, prop.geometricMultiplicity());
        assertTrue(prop.eigenbasis().contains(
                new DenseVector(new double[]{0.9929843710901278, 9.747727559910673E-4, 0.11824165335793371})));//from debugger
    }

    @Test
    public void test_eigenbasis_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-0.706638408025463, -1.0363375959642547E-15, -1.4599026779328453E-14},
                    {-1.036337595964258E-15, -0.706638408025463, 1.7472677320941955E-14},
                    {-9.057555810954446E-16, 8.686693713011859E-16, 0.2688843528273975}
                });
        Eigen instance = new Eigen(A, Eigen.Method.QR, 8.066530584821925E-15);
        NumberAssert.assertSameList(
                Arrays.asList(0.2688843528273958, -0.7066384080254620),//from R
                instance.getEigenvalues(),
                1e-14);

        EigenProperty prop = instance.getProperty(0);
        assertEquals(1, prop.geometricMultiplicity());
        assertTrue(instance.getProperty(0).eigenbasis().contains(
                new DenseVector(new double[]{-1.4965336909786837E-14, 1.7911091388238143E-14, 1.0})));//from debugger

        prop = instance.getProperty(1);
        assertEquals(2, prop.geometricMultiplicity());
        assertTrue(prop.eigenbasis().contains(
                new DenseVector(new double[]{0, 1, 0})));//from debugger
        assertTrue(prop.eigenbasis().contains(
                new DenseVector(new double[]{0, 0, 1})));//from debugger
    }
    //</editor-fold>
}
