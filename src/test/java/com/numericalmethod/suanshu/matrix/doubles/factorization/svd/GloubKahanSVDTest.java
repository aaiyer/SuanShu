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
public class GloubKahanSVDTest {

    @Test
    public void test_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    5.464985704219043, 0.365966190626257//from R
                }),
                                     1e-14));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-14));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-14));
    }

    @Test
    public void test_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.5, 2, 4.96},
                    {3, 4, 99},
                    {73, 63.4, 0.19}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    100.815539315694764, 95.086544860320885, 0.472989441518184//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero row.
     */
    @Test
    public void test_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {0, 1, 2, 0, 0},
                    {0, 0, 1, 2, 0},
                    {0, 0, 0, 1, 2},
                    {0, 0, 0, 0, 1}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    2.8852716787894273, 2.5523972304756901, 2.0371819688524240, 1.4170931135297458, 0.0470366963635844//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero out last B22 column.
     */
    @Test
    public void test_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    1.68481033526142e+01, 1.06836951455471e+00, 3.83879416046065e-16//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero out a B22 row.
     */
    @Test
    public void test_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0},
                    {0, 0, 6},
                    {0, 0, 9}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    10.81665382639197, 2.23606797749979, 0//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero out a B22 row.
     */
    @Test
    public void test_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0},
                    {0, 0, 6, 0},
                    {0, 0, 0, 11},
                    {0, 0, 0, 10}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    14.86606874731851, 6.00000000000000, 2.23606797749979, 0//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero out a B22 row.
     */
    @Test
    public void test_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {0, 0, 6, 0, 0},
                    {0, 0, 21, 11, 0},
                    {0, 0, 0, 0, 17},
                    {0, 0, 0, 0, 29}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    33.61547262794322, 24.30277265856775, 2.71573951364484, 2.23606797749979, 0//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero row.
     */
    @Test
    public void test_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 21, 11, 0},
                    {0, 0, 0, 0, 17},
                    {0, 0, 0, 0, 29}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    33.61547262794322, 23.70653918225939, 2.23606797749979, 0, 0//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero row.
     */
    @Test
    public void test_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 17},
                    {0, 0, 0, 0, 29}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    33.61547262794322, 2.23606797749979, 0, 0, 0//from R
                }), 1e-13));

        Matrix Ut = instance.Ut();
        Matrix U = instance.U();
        Matrix V = instance.V();

        Matrix Aexpected = U.multiply(D).multiply(V.t());
        assertTrue(AreMatrices.equal(Aexpected, A, 1e-13));

        Matrix Dexpected = Ut.multiply(A).multiply(V);
        assertTrue(AreMatrices.equal(Dexpected, D, 1e-13));
    }

    /**
     * Zero row.
     */
    @Test
    public void test_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertEquals(D, new DiagonalMatrix(new double[]{
                    1, 0, 0, 0, 0
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
     * Zero.
     */
    @Test
    public void test_0110() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertEquals(D, new DiagonalMatrix(new double[]{
                    0, 0, 0, 0, 0
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
     * Identity.
     */
    @Test
    public void test_0120() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 1}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertEquals(D, new DiagonalMatrix(new double[]{
                    1, 1, 1, 1, 1
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
     * Tall matrix.
     */
    @Test
    public void test_0130() {
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

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

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
     * Zero columns.
     */
    @Test
    public void test_0140() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 2, 4.96, 6.37, 0},
                    {0, 4, 99, 12.24, 0},
                    {0, 63.4, 0.19, 24, 0},
                    {0, 9.31, 66.78, 88.12, 0},
                    {0, 63.25, 36.1, 45.68, 0},
                    {0, 66, 12.3, 79, 0},
                    {0, 66.9, 12, 79.98, 0},
                    {0, 66, 12, 79, 0}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D,
                                     new DiagonalMatrix(new double[]{
                    226.7419863081803, 111.5454880659761, 54.6331596847936, 0, 0//from R
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
     * Fat matrix.
     */
    @Test
    public void test_0150() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 0},
                    {0, 3},
                    {0, 0},
                    {2, 0},
                    {0, 0},
                    {0, 4},
                    {0, 0},
                    {0, 0},
                    {0, 0}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D, new DiagonalMatrix(new double[]{
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
     * Columns matrix.
     */
    @Test
    public void test_0160() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1},
                    {2},
                    {3},
                    {4}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

        DiagonalMatrix D = instance.D();
        assertTrue(AreMatrices.equal(D, new DiagonalMatrix(new double[]{
                    5.47722557505166//from R
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
     * 1x1 matrix.
     */
    @Test
    public void test_0170() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1.123}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

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
     * 1x1 matrix.
     */
    @Test
    public void test_0180() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);

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
     * Fat matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_0300() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 2},
                    {0, 0, 3, 0, 0,},
                    {0, 0, 0, 0, 0,},
                    {0, 4, 0, 0, 0,}
                });

        GloubKahanSVD instance = new GloubKahanSVD(A, true, true, 1e-15);
        assertEquals(true, true);
    }
}
