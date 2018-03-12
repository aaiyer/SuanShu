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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SVDTest {

    /**
     * Test fat matrix.
     */
    @Test
    public void test_SVD_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 2},
                    {0, 0, 3, 0, 0,},
                    {0, 0, 0, 0, 0,},
                    {0, 4, 0, 0, 0,}
                });

        SVD instance = new SVD(A, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    4, 3, 2.23606797749979, 0//from R
                }), 1e-14));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test fat matrix.
     */
    @Test
    public void test_SVD_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 2},
                    {0, 0, 3, 0, 0,},
                    {0, 0, 0, 0, 0,},
                    {0, 4, 0, 0, 0,}
                });

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    4, 3, 2.23606797749979, 0//from R
                }), 1e-14));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_SVD_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 2, 0, 0, 0, 0, 0},
                    {0, 0, 3, 0, 0, 0, 4, 0, 0, 0}
                });

        SVD instance = new SVD(A, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    5, 2.23606797749979//from R
                }), 1e-14));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test of fat matrix.
     */
    @Test
    public void test_SVD_0025() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 2, 0, 0, 0, 0, 0},
                    {0, 0, 3, 0, 0, 0, 4, 0, 0, 0}
                });

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    5, 2.23606797749979//from R
                }), 1e-14));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_SVD_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.5, 2, 4.96, 6.37, 36.01},
                    {3, 4, 99, 12.24, 369.6},
                    {73, 63.4, 0.19, 24, 99.39},
                    {25.36, 9.31, 66.78, 88.12, 36.03},
                    {15.51, 63.25, 36.1, 45.68, 111},
                    {55, 66, 12.3, 79, 121},
                    {55.5, 66.9, 12, 79.98, 97.001},
                    {55, 66, 12, 79, 97}
                });

        SVD instance = new SVD(A, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    479.1298232378101, 193.0232889504696, 91.2574218051938, 34.9528538829285, 26.0366391601524//from R
                }), 1e-12));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-12));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-12));
    }

    /**
     * Test of tall matrix.
     */
    @Test
    public void test_SVD_0035() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.5, 2, 4.96, 6.37, 36.01},
                    {3, 4, 99, 12.24, 369.6},
                    {73, 63.4, 0.19, 24, 99.39},
                    {25.36, 9.31, 66.78, 88.12, 36.03},
                    {15.51, 63.25, 36.1, 45.68, 111},
                    {55, 66, 12.3, 79, 121},
                    {55.5, 66.9, 12, 79.98, 97.001},
                    {55, 66, 12, 79, 97}
                });

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    479.1298232378101, 193.0232889504696, 91.2574218051938, 34.9528538829285, 26.0366391601524//from R
                }), 1e-12));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-10));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-10));
    }

    /**
     * Test of small numbers.
     *
     * TODO:
     * The current implementation of SVD may not work for all small number cases, WHEN maxInterations = Integer.MAX_VALUE
     * possibly because
     * 1. Polyroot may not work well for small numbers.
     * 2. Bidiagonalization may need a precision parameter.
     *
     */
    @Test
    public void test_SVD_0037() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.5, 2, 4.96, 6.37, 36.01},
                    {3, 4, 99, 12.24, 369.6},
                    {73, 63.4, 0.19, 24, 99.39},
                    {25.36, 9.31, 66.78, 88.12, 36.03},
                    {15.51, 63.25, 36.1, 45.68, 111},
                    {55, 66, 12.3, 79, 121},
                    {55.5, 66.9, 12, 79.98, 97.001},
                    {55, 66, 12, 79, 97}
                });
        A = A.scaled(1e-100);

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    479.12982323781011e-100, 193.02328895046961e-100, 91.25742180519381e-100, 34.95285388292851e-100, 26.03663916015241e-100//from R
                }), 1e-100));//TODO: much worse accuracy; should be around 1e-112

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-100));//TODO: much worse accuracy; should be around 1e-112

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-99));//TODO: much worse accuracy; should be around 1e-112
    }

    /**
     * Test of column matrix.
     */
    @Test
    public void test_SVD_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1},
                    {2},
                    {3},
                    {4}
                });

        SVD instance = new SVD(A, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    5.47722557505166//from R
                }), 1e-12));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test of column matrix.
     */
    @Test
    public void test_SVD_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1},
                    {2},
                    {3},
                    {4}
                });

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    5.47722557505166//from R
                }), 1e-12));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test of row matrix.
     */
    @Test
    public void test_SVD_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4}
                });

        SVD instance = new SVD(A, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    5.47722557505166//from R
                }), 1e-12));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test of row matrix.
     */
    @Test
    public void test_SVD_0055() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4}
                });

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    5.47722557505166//from R
                }), 1e-12));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    /**
     * Test of 1x1 matrix.
     */
    @Test
    public void test_SVD_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1.123}
                });

        SVD instance = new SVD(A, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertEquals(D, new DiagonalMatrix(new double[]{
                    1.123
                }));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertEquals(Aexpected, A);

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 0));
    }

    /**
     * Test of 1x1 matrix.
     */
    @Test
    public void test_SVD_0065() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1.123}
                });

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertEquals(D, new DiagonalMatrix(new double[]{
                    1.123
                }));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertEquals(Aexpected, A);

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 0));
    }

    /**
     * Test of 1x1 matrix.
     */
    @Test
    public void test_SVD_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0}
                });

        SVD instance = new SVD(A, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertEquals(D, new DiagonalMatrix(new double[]{
                    0
                }));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertEquals(Aexpected, A);

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 0));
    }

    /**
     * Test of 1x1 matrix.
     */
    @Test
    public void test_SVD_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0}
                });

        SVD instance = new SVD(A, true);

        DiagonalMatrix D = instance.D();
        assertEquals(D, new DiagonalMatrix(new double[]{
                    0
                }));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertEquals(Aexpected, A);

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 0));
    }
}
