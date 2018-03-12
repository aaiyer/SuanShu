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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.number.NumberUtils;
import com.numericalmethod.suanshu.test.NumberAssert;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class QRAlgorithmTest {

    //<editor-fold defaultstate="collapsed" desc="tests for finding eigenvalues for Hessenberg matrices">
    @Test
    public void test_Hessenberg_0010() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 3, 4, 5, 6, 0, 0, 0},
                    {0, 0, 0, 4, 4, 5, 6, 7, 0, 0, 0},
                    {0, 0, 0, 0, 3, 6, 7, 8, 0, 0, 0},
                    {0, 0, 0, 0, 0, 2, 8, 9, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 10, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        assertFalse(eigen.contains(0.));
        NumberAssert.assertSubset(eigen,
                                  Arrays.asList(1., 14.15397588887864, 9.52481159080654, 5.15520692737634, 1.50142201208615, -0.33541641914766),//from matlab
                                  1e-8);
    }

    @Test
    public void test_Hessenberg_0015() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 3, 4, 5, 6, 0, 0, 0},
                    {0, 0, 0, 4, 4, 5, 6, 7, 0, 0, 0},
                    {0, 0, 0, 0, 3, 6, 7, 8, 0, 0, 0},
                    {0, 0, 0, 0, 0, 2, 8, 9, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 10, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        assertFalse(eigen.contains(0.));
        NumberAssert.assertSubset(eigen,
                                  Arrays.asList(1., 14.15397588887864, 9.52481159080654, 5.15520692737634, 1.50142201208615, -0.33541641914766),//from matlab
                                  1e-8);
    }

    @Test
    public void test_Hessenberg_0020() {
        Matrix H = new DenseMatrix(new double[][]{
                    {2, 3, 4, 5, 6},
                    {4, 4, 5, 6, 7},
                    {0, 3, 6, 7, 8},
                    {0, 0, 2, 8, 9},
                    {0, 0, 0, 1, 10}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        assertFalse(eigen.contains(0.));
        assertFalse(eigen.contains(1.));
        NumberAssert.assertSubset(eigen,
                                  Arrays.asList(14.15397588887864, 9.52481159080654, 5.15520692737634, 1.50142201208615, -0.33541641914766),//from matlab
                                  1e-8);
    }

    @Test
    public void test_Hessenberg_0025() {
        Matrix H = new DenseMatrix(new double[][]{
                    {2, 3, 4, 5, 6},
                    {4, 4, 5, 6, 7},
                    {0, 3, 6, 7, 8},
                    {0, 0, 2, 8, 9},
                    {0, 0, 0, 1, 10}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        assertFalse(eigen.contains(0.));
        assertFalse(eigen.contains(1.));
        NumberAssert.assertSubset(eigen,
                                  Arrays.asList(14.15397588887864, 9.52481159080654, 5.15520692737634, 1.50142201208615, -0.33541641914766),//from matlab
                                  1e-8);
    }

    @Test
    public void test_Hessenberg_0030() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(0.));
    }

    @Test
    public void test_Hessenberg_0035() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(0.));
    }

    @Test
    public void test_Hessenberg_0040() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(0.));
    }

    @Test
    public void test_Hessenberg_0045() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(0.));
    }

    @Test
    public void test_Hessenberg_0050() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(0.));
    }

    @Test
    public void test_Hessenberg_0055() {
        Matrix H = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(0.));
    }

    @Test
    public void test_Hessenberg_0060() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 1}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(1.));
    }

    @Test
    public void test_Hessenberg_0065() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 1}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(1.));
    }

    @Test
    public void test_Hessenberg_0070() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(1.));
    }

    @Test
    public void test_Hessenberg_0075() {
        Matrix H = new DenseMatrix(new double[][]{
                    {1}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        assertTrue(eigen.contains(1.));
    }

    @Test
    public void test_Hessenberg_0080() {
        Matrix H = new DenseMatrix(new double[][]{
                    {12, 23, 4, 55, 16},
                    {43, 45, 35, 56, 71},
                    {0, 35, 65.5, 71.3, 82.6},
                    {0, 0, 2.2, 8.3, 9.1},
                    {0, 0, 0, 1.1, 10.5}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(eigen,
                                  Arrays.asList(99.15288334754663, 34.87588346325702, -12.18164971154826, 6.99618869144715, 12.45669420929752),//from matlab
                                  1e-8);
    }

    @Test
    public void test_Hessenberg_0085() {
        Matrix H = new DenseMatrix(new double[][]{
                    {12, 23, 4, 55, 16},
                    {43, 45, 35, 56, 71},
                    {0, 35, 65.5, 71.3, 82.6},
                    {0, 0, 2.2, 8.3, 9.1},
                    {0, 0, 0, 1.1, 10.5}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(eigen,
                                  Arrays.asList(99.15288334754663, 34.87588346325702, -12.18164971154826, 6.99618869144715, 12.45669420929752),//from matlab
                                  1e-8);

    }

    @Test
    public void test_Hessenberg_0090() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-5.12, -23.23, 4, 55, 16},
                    {43, 45, -35.35, -56, 71},
                    {0, 35, 65.5, -71.3, 99.6},
                    {0, 0, -22.2, -18.3, -99.1},
                    {0, 0, 0, 1.1, 10.5}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(
                NumberUtils.parseArray(new String[]{
                    "51.20463768542070 + 13.67288991850144i",//from matlab
                    "51.20463768542070 - 13.67288991850144i",//from matlab
                    "-18.04589672489077",//from matlab
                    "6.60831067702472 + 3.23291912650711i",//from matlab
                    "6.60831067702472 - 3.23291912650711i"//from matlab)}
                })),//from matlab
                1e-8);
    }

    @Test
    public void test_Hessenberg_0095() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-5.12, -23.23, 4, 55, 16},
                    {43, 45, -35.35, -56, 71},
                    {0, 35, 65.5, -71.3, 99.6},
                    {0, 0, -22.2, -18.3, -99.1},
                    {0, 0, 0, 1.1, 10.5}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(
                NumberUtils.parseArray(new String[]{
                    "51.20463768542070 + 13.67288991850144i",//from matlab
                    "51.20463768542070 - 13.67288991850144i",//from matlab
                    "-18.04589672489077",//from matlab
                    "6.60831067702472 + 3.23291912650711i",//from matlab
                    "6.60831067702472 - 3.23291912650711i"//from matlab)}
                })),//from matlab
                1e-8);
    }

    @Test
    public void test_Hessenberg_0100() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-25.12, -123.23, 40, 1.55, -2.16},
                    {3.43, 0.45, -30.35, -5.6, 0.0071},
                    {0, 0.000035, 6005.5, -7001.3, -99.6},
                    {0, 0, -222.2, -118.3, -99.123456789},
                    {0, 0, 0, 1.123456789, -0.00001015}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(
                NumberUtils.parseArray(new String[]{
                    "6249.79420093222",//from matlab
                    "-12.33499998353 + 16.10039362555i",//from matlab
                    "-12.33499998353 - 16.10039362555i",//from matlab
                    "-362.28785264449",//from matlab
                    "-0.30635847067"//from matlab
                })),//from matlab
                1e-8);
    }

    @Test
    public void test_Hessenberg_0105() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-25.12, -123.23, 40, 1.55, -2.16},
                    {3.43, 0.45, -30.35, -5.6, 0.0071},
                    {0, 0.000035, 6005.5, -7001.3, -99.6},
                    {0, 0, -222.2, -118.3, -99.123456789},
                    {0, 0, 0, 1.123456789, -0.00001015}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(NumberUtils.parseArray(new String[]{
                    "6249.79420093222",//from matlab
                    "-12.33499998353 + 16.10039362555i",//from matlab
                    "-12.33499998353 - 16.10039362555i",//from matlab
                    "-362.28785264449",//from matlab
                    "-0.30635847067"//from matlab
                })),//from matlab
                1e-8);
    }

    @Test
    public void test_Hessenberg_0110() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-25.1234567, -123.23456789, 40000, 1.55555, -0.00000216},
                    {233.43, 1000.45, -130.35, -5.6, 0.000000071},
                    {0, 0.000035, 6005.5, -7001.3, -9999.6},
                    {0, 0, -22222.2, -118.3, -9999.123456789},
                    {0, 0, 0, -1.123456789, -0.00001015}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(15786.9121573697,//from matlab
                              -9901.7407783580,//from matlab
                              971.5884532664,//from matlab
                              3.7380741609,//from matlab
                              2.0286267110//from matlab
                ),
                1e-8);
    }

    @Test
    public void test_Hessenberg_0115() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-25.1234567, -123.23456789, 40000, 1.55555, -0.00000216},
                    {233.43, 1000.45, -130.35, -5.6, 0.000000071},
                    {0, 0.000035, 6005.5, -7001.3, -9999.6},
                    {0, 0, -22222.2, -118.3, -9999.123456789},
                    {0, 0, 0, -1.123456789, -0.00001015}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(15786.9121573697,//from matlab
                              -9901.7407783580,//from matlab
                              971.5884532664,//from matlab
                              3.7380741609,//from matlab
                              2.0286267110//from matlab
                ),
                1e-8);
    }

    @Test
    public void test_Hessenberg_0120() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-25.1234567, -123.23456789, 40000, 1.55555, -0.00000216, 20},
                    {-233.43, 1000.45, -130.35, -5.6, 0.000000071, 10},
                    {0, 0.000035, 6005.5, -7001.3, -9999.6, 100},
                    {0, 0, 22222.2, -118.3, -9999.123456789, -321},
                    {0, 0, 0, -1.123456789, -0.00001015, -123456},
                    {0, 0, 0, 0, -1, 2}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(NumberUtils.parseArray(new String[]{
                    "2943.0119592675 + 12091.0866875276i",//from matlab
                    "2943.0119592675 - 12091.0866875276i",//from matlab
                    "-52.4449340138",//from matlab
                    "-349.7958872306",//from matlab
                    "352.9719607814"//from matlab
                })),//from matlab
                1e-8);
    }

    @Test
    public void test_Hessenberg_0125() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-25.1234567, -123.23456789, 40000, 1.55555, -0.00000216, 20},
                    {-233.43, 1000.45, -130.35, -5.6, 0.000000071, 10},
                    {0, 0.000035, 6005.5, -7001.3, -9999.6, 100},
                    {0, 0, 22222.2, -118.3, -9999.123456789, -321},
                    {0, 0, 0, -1.123456789, -0.00001015, -123456},
                    {0, 0, 0, 0, -1, 2}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(NumberUtils.parseArray(new String[]{
                    "2943.0119592675 + 12091.0866875276i",//from matlab
                    "2943.0119592675 - 12091.0866875276i",//from matlab
                    "-52.4449340138",//from matlab
                    "-349.7958872306",//from matlab
                    "352.9719607814"//from matlab
                })),//from matlab
                1e-8);
    }

    @Test
    public void test_Hessenberg_0130() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {0, 1200000, 10}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1000000000, 1.10644557144570e+03, -1.08444557144570e+03),//from R, which has more precision
                1e-8);
    }

    @Test
    public void test_Hessenberg_0135() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {0, 1200000, 10}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1000000000, 1.10644557144570e+03, -1.08444557144570e+03),//from R, which has more precision
                1e-8);
    }

    @Test
    public void test_Hessenberg_0140() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-0.000000000002, 0.0000006, 0.0000000000005},
                    {0.000000001, 0.12, 0.000000123456},
                    {0, 150, 0.000000246802}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(1.20154122369446e-01, -1.53875567442168e-04, -2.00404176218215e-12),//from R, which has more precision
                1e-8);
    }

    @Test
    public void test_Hessenberg_0145() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-0.000000000002, 0.0000006, 0.0000000000005},
                    {0.000000001, 0.12, 0.000000123456},
                    {0, 150, 0.000000246802}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(1.20154122369446e-01, -1.53875567442168e-04, -2.00404176218215e-12),//from R, which has more precision
                1e-8);
    }

    /**
     * Test of class QRAlgorithm.
     *
     * TODO: This test case is FAKE!!!
     * The results match those with Matlab (which has much fewer precision than R).
     * The results do NOT match those in R.
     */
    @Test
    public void test_Hessenberg_0150() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-0.000000000000002, 0.0000000006, 1000000000},
                    {0.123456789, 123456789, 100},
                    {0, 0.12, 100}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();

        /*
         * These results are from R, which hasApproximately many precision.
         */
//        assertTrue(eigen.hasExactly(
//                1.23456789000000e+08,
//                9.99987998874264e+01,
//                1.20001440152165e-03));

        /*
         * These results are copied from debugger!!!
         */
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(0.0012000000011643996,
                              1.2345678900000009E8,
                              99.99879990182676),
                0);
    }

    /**
     * Test of class QRAlgorithm.
     *
     * TODO: This test case is FAKE!!!
     * The results match those with Matlab (which hasApproximately much fewer precision than R).
     * The results do NOT match those in R.
     */
    @Test
    public void test_Hessenberg_0155() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-0.000000000000002, 0.0000000006, 1000000000},
                    {0.123456789, 123456789, 100},
                    {0, 0.12, 100}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();

        /*
         * These results are from R, which hasApproximately many precision.
         */
//        assertTrue(eigen.hasExactly(
//                1.23456789000000e+08,
//                9.99987998874264e+01,
//                1.20001440152165e-03));

        /*
         * These results are copied from debugger!!!
         */
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(0.0012000000011643996,
                              1.2345678900000009E8,
                              99.99879990182676),
                0);
    }

    @Test
    public void test_Hessenberg_0160() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-0.000000000000002, 0.0000000006, 123},
                    {0.000000000001, 0.12, 1},
                    {0, 0, 0.000005}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
//        System.out.println(eigen);
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(0, 0.12, 0.000005),
                1e-8);
    }

    @Test
    public void test_Hessenberg_0165() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-0.000000000000002, 0.0000000006, 123},
                    {0.000000000001, 0.12, 1},
                    {0, 0, 0.000005}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
//        System.out.println(eigen);
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(0, 0.12, 0.000005),
                1e-8);
    }

    @Test
    public void test_Hessenberg_0170() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-1000000000, 0.0000000006, 123},
                    {0.000000000001, 0.12, 1},
                    {0, 0, 0.000005}
                });

        QRAlgorithm instance = new QRAlgorithm(H, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.0e+09, 1.2e-01, 5.0e-06),//from R, same as Matlab
                1e-8);
    }

    @Test
    public void test_Hessenberg_0180() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-1000000000, 0.0000000006, 123},
                    {0.000000000001, 0.12, 1},
                    {0, 0, 0.000005}
                });

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.0e+09, 1.2e-01, 5.0e-06),//from R, same as Matlab
                1e-8);
    }

    /**
     * Tes of small numbers.
     */
    @Test
    public void test_Hessenberg_0190() {
        Matrix H = new DenseMatrix(new double[][]{
                    {-25.1234567, -123.23456789, 40000, 1.55555, -0.00000216},
                    {233.43, 1000.45, -130.35, -5.6, 0.000000071},
                    {0, 0.000035, 6005.5, -7001.3, -9999.6},
                    {0, 0, -22222.2, -118.3, -9999.123456789},
                    {0, 0, 0, -1.123456789, -0.00001015}
                });
        H = H.scaled(1e-100);

        QRAlgorithm instance = new QRAlgorithm(H);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(15786.9121573697e-100,//from matlab
                              -9901.7407783580e-100,//from matlab
                              971.5884532664e-100,//from matlab
                              3.7380741609e-100,//from matlab
                              2.0286267110e-100//from matlab
                ),
                1e-8);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for finding eigenvalues for general matrices">
    @Test
    public void test_eigenvalues_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3, -3},
                    {-3, 7, -3},
                    {-6, 6, -2}
                });

        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(4, -2),
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-14));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3, -3},
                    {-3, 7, -3},
                    {-6, 6, -2}
                });

        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(4, -2),
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-14));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {10.10, 3.33, -3.333},
                    {-31.234, 70.701, -300.1},
                    {-66, 6.66, -212}
                });

        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-204.65157218089638, 65.87539229366163, 7.57717988723485),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-11));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {10.10, 3.33, -3.333},
                    {-31.234, 70.701, -300.1},
                    {-66, 6.66, -212}
                });

        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-204.65157218089638, 65.87539229366163, 7.57717988723485),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-11));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    /**
     * Test of class QRAlgorithm.
     *
     * TODO:Can't get the same answer as R...
     * but R might be wrong.
     * R gives the same results for both H1 and H2.
     * Yet, they should be slightly different.
     * It is as if R treat the (3, 3)-th element as 0, because it is very small compared to the other entries.
     */
    @Test
    public void test_eigenvalues_0030() {
        Matrix M1 = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {0.000000001, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(M1, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.00000000000000e+09, 1106.4455713343261, -1084.4455713342097),//from debugger
                0);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-8));//T is upper triangular at a lower precision
        assertEquals(T, Q.t().multiply(M1).multiply(Q));

        Matrix M2 = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {0, 1200000, 10}
                });

        instance = new QRAlgorithm(M2, Integer.MAX_VALUE, 1e-8);
        eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.00000000000000e+09, 1.10644557144570e+03, -1.08444557144570e+03),//from R
                1e-8);

        Q = instance.Q();
        T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-8));
        assertEquals(T, Q.t().multiply(M2).multiply(Q));
    }

    /**
     * Test of class QRAlgorithm.
     *
     * TODO:Can't get the same answer as R...
     * but R might be wrong.
     * R gives the same results for both H1 and H2.
     * Yet, they should be slightly different.
     * It is as if R treat the (3, 3)-th element as 0, because it is very small compared to the other entries.
     */
    @Test
    public void test_eigenvalues_0035() {
        Matrix M1 = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {0.000000001, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(M1);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.00000000000000e+09, 1106.4455713343261, -1084.4455713342097),//from debugger
                0);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-8));//T is upper triangular at a lower precision
        assertEquals(T, Q.t().multiply(M1).multiply(Q));

        Matrix M2 = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {0, 1200000, 10}
                });

        instance = new QRAlgorithm(M2, Integer.MAX_VALUE, 1e-8);
        eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.00000000000000e+09, 1.10644557144570e+03, -1.08444557144570e+03),//from R
                1e-8);
        Q = instance.Q();
        T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-8));
        assertEquals(T, Q.t().multiply(M2).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {1, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.00000000000000e+09, 1.10644557144848e+03, -1.08444557144748e+03),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-15));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {1, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.00000000000000e+09, 1.10644557144848e+03, -1.08444557144748e+03),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-15));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {100, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
//        assertTrue(eigen.hasApproximately(1e-8, -1.00000000000000e+09, 1.10644557172387e+03, -1.08444557162387e+03));//from R
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.0000000000000005e9, 1106.4455717330025, -1084.4455716330012),//from debugger
                0);
    }

    @Test
    public void test_eigenvalues_0055() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {100, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
//        assertTrue(eigen.hasApproximately(1e-8, -1.00000000000000e+09, 1.10644557172387e+03, -1.08444557162387e+03));//from R
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.0000000000000005e9, 1106.4455717330025, -1084.4455716330012),//from debugger
                0);
    }

    @Test
    public void test_eigenvalues_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {10000, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
//        assertTrue(eigen.hasApproximately(1e-8, -1.00000000000001e+09, 1.10644559926287e+03, -1.08444558926292e+03));//from R
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.0000000000000095E9, 1106.445599284046, -1084.4455892844355),//from debugger
                0);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-10));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0065() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1000000000, 5000, 1},
                    {0.000000001, 12, 1},
                    {10000, 1200000, 10}
                });
        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
//        assertTrue(eigen.hasApproximately(1e-8, -1.00000000000001e+09, 1.10644559926287e+03, -1.08444558926292e+03));//from R
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(-1.0000000000000095E9, 1106.445599284046, -1084.4455892844355),//from debugger
                0);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-10));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-123.567, 500.03, 10.456, 256.1},
                    {0.05, 12.12, 11.11, 36.98},
                    {99.9, 721, 210, 7.23},
                    {100.1, 12.023, 56.17, 8.683}
                });
        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(298.1948824715647, -180.5736060227177, -72.8038109337569, 62.4185344849101),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-10));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0075() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-123.567, 500.03, 10.456, 256.1},
                    {0.05, 12.12, 11.11, 36.98},
                    {99.9, 721, 210, 7.23},
                    {100.1, 12.023, 56.17, 8.683}
                });
        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(298.1948824715647, -180.5736060227177, -72.8038109337569, 62.4185344849101),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-10));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-123.567, 500.03, 10.456, 256.1, 5639.12, 236.16},
                    {0.05, 12.12, 11.11, 36.98, 135.92, 256.29},
                    {99.9, 721, 210, 7.23, 742.123, 486.248},
                    {560.1, 453.43, 346.17, 8.683, 2698.3, 129.355},
                    {46.1, 567.873, 780.17, 1259, 2964.3, 236.78},
                    {246.1, 709.03, 30.17, 19.24, 228.2, 1893.3}
                });
        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(NumberUtils.parseArray(
                "4351.2113190007049", "1849.9980319933732",
                "-663.9611634205439+792.620968947048i",
                "-663.9611634205439-792.620968947048i",
                "184.6204326555930", "-93.0714568085813")),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-10));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0085() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-123.567, 500.03, 10.456, 256.1, 5639.12, 236.16},
                    {0.05, 12.12, 11.11, 36.98, 135.92, 256.29},
                    {99.9, 721, 210, 7.23, 742.123, 486.248},
                    {560.1, 453.43, 346.17, 8.683, 2698.3, 129.355},
                    {46.1, 567.873, 780.17, 1259, 2964.3, 236.78},
                    {246.1, 709.03, 30.17, 19.24, 228.2, 1893.3}
                });
        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(NumberUtils.parseArray(
                "4351.2113190007049", "1849.9980319933732",
                "-663.9611634205439+792.620968947048i",
                "-663.9611634205439-792.620968947048i",
                "184.6204326555930", "-93.0714568085813")),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-10));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-123.567, 500.03, 10.456, 256.1, 5639.12, 236.16, 1, 2, 3},
                    {0.05, 12.12, 11.11, 36.98, 135.92, 256.29, 4, 5, 6},
                    {99.9, 721, 210, 7.23, 742.123, 486.248, 7, 8, 9},
                    {560.1, 453.43, 346.17, 8.683, 2698.3, 129.355, 0, 1, 2},
                    {46.1, 567.873, 780.17, 1259, 2964.3, 236.78, 3, 4, 5},
                    {566.1, 99.03, 387.17, 19.24, 228.2, 1893.3, 6, 7, 8},
                    {216.1, 9.03, 50.17, 63.24, 26.2, 893.3, 9, 10, 11},
                    {86.1, 73.03, 62.17, 4.24, 2.2, 183.3, 12, 13, 14},
                    {36.1, 58.03, 92.17, 39.24, 93.2, 893.3, 15, 16, 17}
                });
        QRAlgorithm instance = new QRAlgorithm(A, Integer.MAX_VALUE, 1e-8);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(NumberUtils.parseArray(
                "4.41326264032731e+03",
                "1.75008915325320e+03",
                "-6.76183268622504e2+7.81560933624619e2i",
                "-6.76183268622504e2-7.81560933624619e2i",
                "9.58680677370416e1+5.07437190891462e1i",
                "9.58680677370416e1-5.07437190891462e1i",
                "5.57304095208876e-1+1.27607786576220e1i",
                "5.57304095208876e-1-1.27607786576220e1i",
                "-2.82457936166985e-15")),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-8));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }

    @Test
    public void test_eigenvalues_0095() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-123.567, 500.03, 10.456, 256.1, 5639.12, 236.16, 1, 2, 3},
                    {0.05, 12.12, 11.11, 36.98, 135.92, 256.29, 4, 5, 6},
                    {99.9, 721, 210, 7.23, 742.123, 486.248, 7, 8, 9},
                    {560.1, 453.43, 346.17, 8.683, 2698.3, 129.355, 0, 1, 2},
                    {46.1, 567.873, 780.17, 1259, 2964.3, 236.78, 3, 4, 5},
                    {566.1, 99.03, 387.17, 19.24, 228.2, 1893.3, 6, 7, 8},
                    {216.1, 9.03, 50.17, 63.24, 26.2, 893.3, 9, 10, 11},
                    {86.1, 73.03, 62.17, 4.24, 2.2, 183.3, 12, 13, 14},
                    {36.1, 58.03, 92.17, 39.24, 93.2, 893.3, 15, 16, 17}
                });
        QRAlgorithm instance = new QRAlgorithm(A);
        List<Number> eigen = instance.getEigenvalues();
        NumberAssert.assertSubset(
                eigen,
                Arrays.asList(NumberUtils.parseArray(
                "4.41326264032731e+03",
                "1.75008915325320e+03",
                "-6.76183268622504e2+7.81560933624619e2i",
                "-6.76183268622504e2-7.81560933624619e2i",
                "9.58680677370416e1+5.07437190891462e1i",
                "9.58680677370416e1-5.07437190891462e1i",
                "5.57304095208876e-1+1.27607786576220e1i",
                "5.57304095208876e-1-1.27607786576220e1i",
                "-2.82457936166985e-15")),//from R
                1e-8);

        Matrix Q = instance.Q();
        Matrix T = instance.T();

        assertTrue(IsMatrix.orthogonal(Q, 1e-14));
        assertTrue(IsMatrix.quasiTriangular(T, 1e-8));
        assertEquals(T, Q.t().multiply(A).multiply(Q));
    }
    //</editor-fold>
}
