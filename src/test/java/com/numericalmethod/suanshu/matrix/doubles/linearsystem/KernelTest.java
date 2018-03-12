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
package com.numericalmethod.suanshu.matrix.doubles.linearsystem;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.operation.VectorSpace;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class KernelTest {

    //<editor-fold defaultstate="collapsed" desc="tests for GAUSSIAN_JORDAN_ELIMINATION">
    /**
     * Test of class Kernel.
     */
    @Test
    public void test_GJE_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, -3, 0, 2, -8},
                    {0, 1, 5, 0, -1, 4},
                    {0, 0, 0, 1, 7, -9},
                    {0, 0, 0, 0, 0, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
        assertEquals(3, instance.nullity());
        assertEquals(3, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v3 = basis.get(new Integer(3));
        assertEquals(new DenseVector(new double[]{3, -5, 1, 0, 0, 0}), v3);
        Vector v5 = basis.get(new Integer(5));
        assertEquals(new DenseVector(new double[]{-2, 1, 0, -7, 1, 0}), v5);
        Vector v6 = basis.get(new Integer(6));
        assertEquals(new DenseVector(new double[]{8, -4, 0, 9, 0, 1}), v6);

        assertFalse(instance.isZero());

        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_GJE_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-3, -3, 3},
                    {3, -9, 3},
                    {6, -6, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
        assertEquals(1, instance.nullity());
        assertEquals(2, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v3 = basis.get(new Integer(3));
        assertEquals(new DenseVector(new double[]{0.5, 0.5, 1}), v3);

        assertFalse(instance.isZero());

        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_GJE_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, -3, 3},
                    {3, -3, 3},
                    {6, -6, 6}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
        assertEquals(2, instance.nullity());
        assertEquals(1, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v2 = basis.get(new Integer(2));
        assertEquals(new DenseVector(new double[]{1, 1, 0}), v2);
        Vector v3 = basis.get(new Integer(3));
        assertEquals(new DenseVector(new double[]{-1, 0, 1}), v3);

        assertFalse(instance.isZero());

        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_GJE_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 1, -3, 0, 4},
                    {0, 0, 0, 0, 1, 6}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
        assertEquals(4, instance.nullity());
        assertEquals(2, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v1 = basis.get(new Integer(1));
        assertEquals(new DenseVector(new double[]{1, 0, 0, 0, 0, 0}), v1);
        Vector v2 = basis.get(new Integer(2));
        assertEquals(new DenseVector(new double[]{0, 1, 0, 0, 0, 0}), v2);
        Vector v4 = basis.get(new Integer(4));
        assertEquals(new DenseVector(new double[]{0, 0, 3, 1, 0, 0}), v4);
        Vector v6 = basis.get(new Integer(6));
        assertEquals(new DenseVector(new double[]{0, 0, -4, 0, -6, 1}), v6);

        assertFalse(instance.isZero());

        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_GJE_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 2},
                    {0, 1, 0, 1},
                    {0, 0, 1, -1},
                    {0, 0, 0, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
        assertEquals(1, instance.nullity());
        assertEquals(3, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v4 = basis.get(new Integer(4));
        assertEquals(new DenseVector(new double[]{-2, -1, 1, 1}), v4);

        assertFalse(instance.isZero());

        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_GJE_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3, 7},
                    {23, 27, 31},
                    {2, 5, 11}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
        assertEquals(0, instance.nullity());
        assertEquals(3, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();
        assertTrue(basis.isEmpty());
        assertTrue(instance.isZero());

        assertTrue(AreMatrices.equal(instance.U(), instance.T().multiply(A), 1e-13));//less precision
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_GJE_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
        assertEquals(3, instance.nullity());
        assertEquals(0, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v1 = basis.get(new Integer(1));
        Vector v2 = basis.get(new Integer(2));
        Vector v3 = basis.get(new Integer(3));

        Matrix I = CreateMatrix.cbind(new Vector[]{v1, v2, v3});
        assertEquals(new DenseMatrix(3, 3).ONE(), I);
    }

    /**
     * Test of class Kernel.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_GJE_0200() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 2},
                    {0, 1, 0, 1},
                    {0, 0, 1, -1},
                    {1, 2, 3, 4},
                    {5, 6, 7, 8}
                });

        Kernel instance = new Kernel(A, Kernel.Method.GAUSSIAN_JORDAN_ELIMINATION, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for QR">
    /**
     * Test of class Kernel.
     */
    @Test
    public void test_QR_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, -3, 0, 2, -8},
                    {0, 1, 5, 0, -1, 4},
                    {0, 0, 0, 1, 7, -9},
                    {0, 0, 0, 0, 0, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 1e-14);
        assertEquals(3, instance.nullity());
        assertEquals(3, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.02052947587973641,
                    0.19443198282901408,
                    -0.058484777362537786,
                    0.9661736821992011,
                    -0.15698776804967007,
                    -0.01474896601649879
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    -0.13040955536942964,
                    0.921541079527966,
                    -0.2446675148123575,
                    -0.16672685379641722,
                    0.17805662452579235,
                    0.1199632797649034
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    0.9116982631927105,
                    0.008043355639402446,
                    -0.1325407106387882,
                    0.024207376970489816,
                    0.30500590895309926,
                    0.23991652662690943
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{3, -5, 1, 0, 0, 0}),
                new DenseVector(new double[]{-2, 1, 0, -7, 1, 0}),
                new DenseVector(new double[]{8, -4, 0, 9, 0, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));
        assertTrue(vs.isSpanned(instance.basis().get(1)));
        assertTrue(vs.isSpanned(instance.basis().get(2)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_QR_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-3, -3, 3},
                    {3, -9, 3},
                    {6, -6, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 1e-14);
        assertEquals(1, instance.nullity());
        assertEquals(2, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.40824829046386296, 0.408248290463863, 0.816496580927726
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{0.5, 0.5, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_QR_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, -3, 3},
                    {3, -3, 3},
                    {6, -6, 6}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 1e-14);
        assertEquals(2, instance.nullity());
        assertEquals(1, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.5773502691896256, 0.788675134594813, 0.21132486540518705
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    -0.5773502691896256, 0.21132486540518705, 0.788675134594813
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{-1, 0, 1}),
                new DenseVector(new double[]{1, 1, 0}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));
        assertTrue(vs.isSpanned(instance.basis().get(1)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_QR_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 1, -3, 0, 4},
                    {0, 0, 0, 0, 1, 6}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 1e-14);
        assertEquals(4, instance.nullity());
        assertEquals(2, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.19611613513818402, -0.2395691629117869, 0.9041450777202072, 0.28756476683937826, 0.06217616580310881, -0.010362694300518172
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    -0.588348405414552, 0.7187074887353607, 0.28756476683937826, 0.13730569948186522, -0.18652849740932645, 0.031088082901554404
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    0, 0.2595332598211025, 0.06217616580310881, -0.18652849740932645, 0.9326424870466321, -0.15544041450777202
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    0.7844645405527361, 0.5989229072794672, -0.010362694300518172, 0.03108808290155446, -0.15544041450777202, 0.02590673575129543
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{1, 0, 0, 0, 0, 0}),
                new DenseVector(new double[]{0, 1, 0, 0, 0, 0}),
                new DenseVector(new double[]{0, 0, 3, 1, 0, 0}),
                new DenseVector(new double[]{0, 0, -4, 0, -6, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));
        assertTrue(vs.isSpanned(instance.basis().get(1)));
        assertTrue(vs.isSpanned(instance.basis().get(2)));
        assertTrue(vs.isSpanned(instance.basis().get(3)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_QR_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 2},
                    {0, 1, 0, 1},
                    {0, 0, 1, -1},
                    {0, 0, 0, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 1e-14);
        assertEquals(1, instance.nullity());
        assertEquals(3, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    -0.7559289460184543, -0.37796447300922725, 0.3779644730092272, 0.37796447300922725
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{-2, -1, 1, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_QR_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3, 7},
                    {23, 27, 31},
                    {2, 5, 11}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 0);
        assertEquals(0, instance.nullity());
        assertEquals(3, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();
        assertTrue(basis.isEmpty());
        assertTrue(instance.isZero());

        assertTrue(AreMatrices.equal(instance.U(), instance.T().multiply(A), 1e-13));//less precision
    }

    /**
     * Test of class Kernel.
     */
    @Test
    public void test_QR_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 0);
        assertEquals(3, instance.nullity());
        assertEquals(0, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v1 = basis.get(new Integer(1));
        Vector v2 = basis.get(new Integer(2));
        Vector v3 = basis.get(new Integer(3));

        Matrix I = CreateMatrix.cbind(new Vector[]{v1, v2, v3});
        assertEquals(new DenseMatrix(3, 3).ONE(), I);
    }

    /**
     * Test of class Kernel.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_QR_0200() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 2},
                    {0, 1, 0, 1},
                    {0, 0, 1, -1},
                    {1, 2, 3, 4},
                    {5, 6, 7, 8}
                });

        Kernel instance = new Kernel(A, Kernel.Method.QR, 0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for Kernel">
    @Test
    public void test_Kernel_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, -3, 0, 2, -8},
                    {0, 1, 5, 0, -1, 4},
                    {0, 0, 0, 1, 7, -9},
                    {0, 0, 0, 0, 0, 0}
                });

        Kernel instance = new Kernel(A);
        assertEquals(3, instance.nullity());
        assertEquals(3, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.02052947587973641,
                    0.19443198282901408,
                    -0.058484777362537786,
                    0.9661736821992011,
                    -0.15698776804967007,
                    -0.01474896601649879
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    -0.13040955536942964,
                    0.921541079527966,
                    -0.2446675148123575,
                    -0.16672685379641722,
                    0.17805662452579235,
                    0.1199632797649034
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    0.9116982631927105,
                    0.008043355639402446,
                    -0.1325407106387882,
                    0.024207376970489816,
                    0.30500590895309926,
                    0.23991652662690943
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{3, -5, 1, 0, 0, 0}),
                new DenseVector(new double[]{-2, 1, 0, -7, 1, 0}),
                new DenseVector(new double[]{8, -4, 0, 9, 0, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));
        assertTrue(vs.isSpanned(instance.basis().get(1)));
        assertTrue(vs.isSpanned(instance.basis().get(2)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    @Test
    public void test_Kernel_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-3, -3, 3},
                    {3, -9, 3},
                    {6, -6, 0}
                });

        Kernel instance = new Kernel(A);
        assertEquals(1, instance.nullity());
        assertEquals(2, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.40824829046386296, 0.408248290463863, 0.816496580927726
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{0.5, 0.5, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    @Test
    public void test_Kernel_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3, -3, 3},
                    {3, -3, 3},
                    {6, -6, 6}
                });

        Kernel instance = new Kernel(A);
        assertEquals(2, instance.nullity());
        assertEquals(1, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.5773502691896256, 0.788675134594813, 0.21132486540518705
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    -0.5773502691896256, 0.21132486540518705, 0.788675134594813
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{-1, 0, 1}),
                new DenseVector(new double[]{1, 1, 0}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));
        assertTrue(vs.isSpanned(instance.basis().get(1)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    @Test
    public void test_Kernel_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 1, -3, 0, 4},
                    {0, 0, 0, 0, 1, 6}
                });

        Kernel instance = new Kernel(A);
        assertEquals(4, instance.nullity());
        assertEquals(2, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.19611613513818402, -0.2395691629117869, 0.9041450777202072, 0.28756476683937826, 0.06217616580310881, -0.010362694300518172
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    -0.588348405414552, 0.7187074887353607, 0.28756476683937826, 0.13730569948186522, -0.18652849740932645, 0.031088082901554404
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    0, 0.2595332598211025, 0.06217616580310881, -0.18652849740932645, 0.9326424870466321, -0.15544041450777202
                }));

        instance.basis().contains(new DenseVector(new double[]{
                    0.7844645405527361, 0.5989229072794672, -0.010362694300518172, 0.03108808290155446, -0.15544041450777202, 0.02590673575129543
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{1, 0, 0, 0, 0, 0}),
                new DenseVector(new double[]{0, 1, 0, 0, 0, 0}),
                new DenseVector(new double[]{0, 0, 3, 1, 0, 0}),
                new DenseVector(new double[]{0, 0, -4, 0, -6, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));
        assertTrue(vs.isSpanned(instance.basis().get(1)));
        assertTrue(vs.isSpanned(instance.basis().get(2)));
        assertTrue(vs.isSpanned(instance.basis().get(3)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    @Test
    public void test_Kernel_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 2},
                    {0, 1, 0, 1},
                    {0, 0, 1, -1},
                    {0, 0, 0, 0}
                });

        Kernel instance = new Kernel(A);
        assertEquals(1, instance.nullity());
        assertEquals(3, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    -0.7559289460184543, -0.37796447300922725, 0.3779644730092272, 0.37796447300922725
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{-2, -1, 1, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }

    @Test
    public void test_Kernel_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3, 7},
                    {23, 27, 31},
                    {2, 5, 11}
                });

        Kernel instance = new Kernel(A);
        assertEquals(0, instance.nullity());
        assertEquals(3, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();
        assertTrue(basis.isEmpty());
        assertTrue(instance.isZero());

        assertTrue(AreMatrices.equal(instance.U(), instance.T().multiply(A), 1e-13));//less precision
    }

    @Test
    public void test_Kernel_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });

        Kernel instance = new Kernel(A);
        assertEquals(3, instance.nullity());
        assertEquals(0, instance.rank());
        Map<Integer, Vector> basis = instance.basisAndFreeVars();

        Vector v1 = basis.get(new Integer(1));
        Vector v2 = basis.get(new Integer(2));
        Vector v3 = basis.get(new Integer(3));

        Matrix I = CreateMatrix.cbind(new Vector[]{v1, v2, v3});
        assertEquals(new DenseMatrix(3, 3).ONE(), I);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_Kernel_0200() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 2},
                    {0, 1, 0, 1},
                    {0, 0, 1, -1},
                    {1, 2, 3, 4},
                    {5, 6, 7, 8}
                });

        Kernel instance = new Kernel(A);
    }

    /**
     * Test of small numbers.
     */
    @Test
    public void test_Kernel_0210() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-3, -3, 3},
                    {3, -9, 3},
                    {6, -6, 0}
                });
        A = A.scaled(1e-100);

        Kernel instance = new Kernel(A);
        assertEquals(1, instance.nullity());
        assertEquals(2, instance.rank());

        instance.basis().contains(new DenseVector(new double[]{
                    0.40824829046386296, 0.408248290463863, 0.816496580927726
                }));

        VectorSpace vs = new VectorSpace(1e-14,
                new DenseVector(new double[]{0.5, 0.5, 1}));
        assertTrue(vs.isSpanned(instance.basis().get(0)));

        assertFalse(instance.isZero());
        assertEquals(instance.U(), instance.T().multiply(A));
    }
    //</editor-fold>
}
