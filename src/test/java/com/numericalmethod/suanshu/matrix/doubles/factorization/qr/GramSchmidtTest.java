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
package com.numericalmethod.suanshu.matrix.doubles.factorization.qr;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.misc.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * The expected values in assertEquals come from R.
 * For example, a piece of R code looks like
 * <code>
 * size = 500
 * a = matrix(seq(1,size*size,1),size,size)
 * a = t(a)
 * b = qr(a)
 * b$rank
 * X = qr.Q(b)
 * q = qr.Q(b)
 * r = qr.R(b)
 * </code>
 *
 * @author Haksun Li
 */
public class GramSchmidtTest {

    private long timer = 0;

    @Before
    public void setUp() {
        timer = System.currentTimeMillis();
    }

    @After
    public void tearDown() {
        timer = System.currentTimeMillis() - timer;
//        System.out.println(String.format("time elapsed: %d msec", timer));
    }

    /**
     * The results of GramSchmidt and HouseholderReflection may differ by a sign.
     */
    @Test
    public void test_GramSchmidt_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, 2},
                    {1, 2}});
        GramSchmidt instance = new GramSchmidt(A1, true, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.948683298050514, -0.31622776601683},
                    {0.316227766016838, 0.948683298050514}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {3.16227766016838, 2.52982212813470},
                    {1.26491106406735}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertEquals(A1, A2);

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(A1.ONE(), P, 0));//no pivoting needed
    }

    /**
     * The results of GramSchmidt and HouseholderReflection may differ by a sign.
     */
    @Test
    public void test_GramSchmidt_0015() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, 2},
                    {1, 2}});
        GramSchmidt instance = new GramSchmidt(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.948683298050514, -0.31622776601683},
                    {0.316227766016838, 0.948683298050514}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {3.16227766016838, 2.52982212813470},
                    {1.26491106406735}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertEquals(A1, A2);

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(A1.ONE(), P, 0));//no pivoting needed
    }

    @Test
    public void test_GramSchmidt_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, -51, 4},
                    {6, 167, -68},
                    {-4, 24, -41}});

        GramSchmidt instance = new GramSchmidt(A1, true, 0);
        assertEquals(3, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {6d / 7d, -69d / 175d, -58d / 175d},
                    {3d / 7d, 158d / 175d, 6d / 175d},
                    {-2d / 7d, 6d / 35d, -33d / 35d}});
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {14, 21, -14},
                    {175, -70},
                    {35}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(A1.ONE(), P, 0));//no pivoting needed
    }

    @Test
    public void test_GramSchmidt_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}});

        GramSchmidt instance = new GramSchmidt(A1, true, 1e-14);//the precision affects the rank
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.0341992784028384, 0.7738413334506800, 0, 0, 0},//the signs are different than those in HouseholderReflection
                    {0.2051956704170308, 0.5078333750770072, 0, 0, 0},
                    {0.3761920624312232, 0.2418254167033372, 0, 0, 0},
                    {0.5471884544454155, -0.0241825416703336, 0, 0, 0},
                    {0.7181848464596079, -0.2901905000440044, 0, 0, 0}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Q = instance.squareQ();
        Qexpected = new DenseMatrix(new double[][]{//last 3 columns is a set of orthogonal complements, which are found using a different method from that in HouseholderReflection
                    {0.0341992784028384, 0.7738413334506800, 0.6324555320336759, 0, 0},
                    {0.2051956704170308, 0.5078333750770072, -0.6324555320336755, 0.5477225575051666, 0},
                    {0.3761920624312232, 0.2418254167033372, -0.316227766016839, -0.7302967433402223, 0.40824829046386013},
                    {0.5471884544454155, -0.0241825416703336, 0, -0.18257418583505305, -0.8164965809277265},
                    {0.7181848464596079, -0.2901905000440044, 0.3162277660168375, 0.3651483716701092, 0.4082482904638649}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {29.240383034426891, 31.1213433465830072, 33.0023036587391, 34.8832639708952, 36.7642242830514},
                    {1.2091270835166883, 2.41825416703338, 3.62738125055006, 4.83650833406675},
                    {0, 0, 0},
                    {0, 0},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertEquals(new PermutationMatrix(new int[]{1, 2, 4, 3, 5}), P);
    }

    @Test
    public void test_GramSchmidt_0035() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}});

        GramSchmidt instance = new GramSchmidt(A1);//the precision affects the rank
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.0341992784028384, 0.7738413334506800, 0, 0, 0},//the signs are different than those in HouseholderReflection
                    {0.2051956704170308, 0.5078333750770072, 0, 0, 0},
                    {0.3761920624312232, 0.2418254167033372, 0, 0, 0},
                    {0.5471884544454155, -0.0241825416703336, 0, 0, 0},
                    {0.7181848464596079, -0.2901905000440044, 0, 0, 0}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Q = instance.squareQ();
        Qexpected = new DenseMatrix(new double[][]{//last 3 columns is a set of orthogonal complements, which are found using a different method from that in HouseholderReflection
                    {0.0341992784028384, 0.7738413334506800, 0.6324555320336759, 0, 0},
                    {0.2051956704170308, 0.5078333750770072, -0.6324555320336755, 0.5477225575051666, 0},
                    {0.3761920624312232, 0.2418254167033372, -0.316227766016839, -0.7302967433402223, 0.40824829046386013},
                    {0.5471884544454155, -0.0241825416703336, 0, -0.18257418583505305, -0.8164965809277265},
                    {0.7181848464596079, -0.2901905000440044, 0.3162277660168375, 0.3651483716701092, 0.4082482904638649}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {29.240383034426891, 31.1213433465830072, 33.0023036587391, 34.8832639708952, 36.7642242830514},
                    {1.2091270835166883, 2.41825416703338, 3.62738125055006, 4.83650833406675},
                    {0, 0, 0},
                    {0, 0},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertEquals(new PermutationMatrix(new int[]{1, 2, 4, 3, 5}), P);
    }

    @Test
    public void test_GramSchmidt_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        GramSchmidt instance = new GramSchmidt(A1, true, 1e-14);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.0341992784028384, 0.7738413334506800, 0},
                    {0.2051956704170308, 0.5078333750770072, 0},
                    {0.3761920624312232, 0.2418254167033372, 0},
                    {0.5471884544454155, -0.0241825416703336, 0},
                    {0.7181848464596079, -0.2901905000440044, 0}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Qexpected = new DenseMatrix(new double[][]{
                    {0.0341992784028384, 0.7738413334506800, 0.6324555320336759, 0, 0},
                    {0.2051956704170308, 0.5078333750770072, -0.6324555320336755, 0.5477225575051666, 0},
                    {0.3761920624312232, 0.2418254167033372, -0.316227766016839, -0.7302967433402223, 0.40824829046386013},
                    {0.5471884544454155, -0.0241825416703336, 0, -0.18257418583505305, -0.8164965809277265},
                    {0.7181848464596079, -0.2901905000440044, 0.3162277660168375, 0.3651483716701092, 0.4082482904638649}
                });
        assertTrue(AreMatrices.equal(Qexpected, sqQ, 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {29.240383034426891, 31.1213433465830072, 33.0023036587391},
                    {1.2091270835166883, 2.41825416703338},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));

        Matrix tallR = instance.tallR();
        Matrix tallRExpected = new DenseMatrix(new double[][]{
                    {29.240383034426891, 31.1213433465830072, 33.0023036587391},
                    {0, 1.2091270835166883, 2.41825416703338},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });
        assertTrue(AreMatrices.equal(tallRExpected, tallR, 1e-12));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(R.ONE(), P, 0));
    }

    @Test
    public void test_GramSchmidt_0045() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        GramSchmidt instance = new GramSchmidt(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.0341992784028384, 0.7738413334506800, 0},
                    {0.2051956704170308, 0.5078333750770072, 0},
                    {0.3761920624312232, 0.2418254167033372, 0},
                    {0.5471884544454155, -0.0241825416703336, 0},
                    {0.7181848464596079, -0.2901905000440044, 0}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Qexpected = new DenseMatrix(new double[][]{
                    {0.0341992784028384, 0.7738413334506800, 0.6324555320336759, 0, 0},
                    {0.2051956704170308, 0.5078333750770072, -0.6324555320336755, 0.5477225575051666, 0},
                    {0.3761920624312232, 0.2418254167033372, -0.316227766016839, -0.7302967433402223, 0.40824829046386013},
                    {0.5471884544454155, -0.0241825416703336, 0, -0.18257418583505305, -0.8164965809277265},
                    {0.7181848464596079, -0.2901905000440044, 0.3162277660168375, 0.3651483716701092, 0.4082482904638649}
                });
        assertTrue(AreMatrices.equal(Qexpected, sqQ, 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {29.240383034426891, 31.1213433465830072, 33.0023036587391},
                    {1.2091270835166883, 2.41825416703338},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));

        Matrix tallR = instance.tallR();
        Matrix tallRExpected = new DenseMatrix(new double[][]{
                    {29.240383034426891, 31.1213433465830072, 33.0023036587391},
                    {0, 1.2091270835166883, 2.41825416703338},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });
        assertTrue(AreMatrices.equal(tallRExpected, tallR, 1e-12));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(R.ONE(), P, 0));
    }

    /**
     * There is a loss of orthogonal,
     * when the number of iterations or dimension of the matrix increase.
     *
     * We will need to decrease the precision to compute the rank correctly.
     */
    @Test
    public void test_GramSchmidt_0050() {
        int dim = 500;
        Matrix A1 = new DenseMatrix(dim, dim);
        int count = 0;
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= dim; ++j) {
                A1.set(i, j, ++count);
            }
        }

        GramSchmidt instance = new GramSchmidt(A1, true, 1e-7);//TODO: cannot really determine the precision before you know the rank
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        assertEquals(11.1970473508047, R.get(2, 2), 1e-7);//set this according to the magnitude of R[1,1]
        assertEquals(548.655320191059, R.get(2, 50), 1e-7);
        assertEquals(0, R.get(4, 4), 1e-7);
        assertEquals(0, R.get(dim, dim), 1e-7);

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-7));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-14));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-7));

    }

    /**
     * There is a loss of orthogonal,
     * when the number of iterations or dimension of the matrix increase.
     *
     * We will need to decrease the precision to compute the rank correctly.
     */
//    @Test
    public void test_GramSchmidt_0060() {
        int dim = 3500;
        Matrix A1 = new DenseMatrix(dim, dim);

        int count = 0;
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= dim; ++j) {
                A1.set(i, j, ++count);
            }
        }

        GramSchmidt instance = new GramSchmidt(A1, true, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        assertEquals(418327545.62095827, R.get(1, 1), 1e-5);//set this according to the magnitude of R[1,1]
        /*
         * TODO: R seems to confuse the values for R[2,2] and R[2,3], or am I wrong?
         */
//        assertEquals(418327648.083148, R.get(1, 2), 1e-5);//TODO: what is the true value?
        assertEquals(29.5867341769226, R.get(2, 2), 1e-5);
        assertEquals(59.1734683535534, R.get(2, 3), 1e-5);
        assertEquals(88.7602025281696, R.get(2, 4), 1e-5);
        assertEquals(118.346936702786, R.get(2, 5), 1e-5);
        assertEquals(562.147949336131, R.get(2, 20), 1e-5);
        assertEquals(5887.76010092419, R.get(2, 200), 1e-5);
        assertEquals(59143.8816168007, R.get(2, 2000), 1e-5);
        assertEquals(0, R.get(4, 4), 1e-5);
        assertEquals(0, R.get(dim, dim), 1e-5);

//        Matrix A2 = Q.multiply(R);
//        assertEquals(A1, A2);
    }

    @Test
    public void test_GramSchmidt_0070() {
        double[][] data = {{1}};
        Matrix A1 = new DenseMatrix(data);

        GramSchmidt instance = new GramSchmidt(A1, true, 0);
        assertEquals(1, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{1}});
        assertEquals(Rexpected, R);

        Matrix A2 = Q.multiply(R);
        assertEquals(A1, A2);

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(A1.ONE(), P, 0));
    }

    @Test
    public void test_GramSchmidt_0075() {
        double[][] data = {{1}};
        Matrix A1 = new DenseMatrix(data);

        GramSchmidt instance = new GramSchmidt(A1);
        assertEquals(1, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 0));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{1}});
        assertEquals(Rexpected, R);

        Matrix A2 = Q.multiply(R);
        assertEquals(A1, A2);

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(A1.ONE(), P, 0));
    }

    /**
     * TODO: shall we throw a runtime exception if one of the norm is 0?
     */
    @Test
    public void test_GramSchmidt_0090() {
        double[][] data = {{0}};
        Matrix A1 = new DenseMatrix(data);

        GramSchmidt instance = new GramSchmidt(A1, true, 0);
        assertEquals(0, instance.rank());

        Matrix Q = instance.Q();
        assertEquals(Q.ZERO(), Q);//different from Householder reflection

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{0}});
        assertEquals(Rexpected, R);

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(A1.ONE(), P, 0));
    }

    /**
     * TODO: shall we throw a runtime exception if one of the norm is 0?
     */
    @Test
    public void test_GramSchmidt_0095() {
        double[][] data = {{0}};
        Matrix A1 = new DenseMatrix(data);

        GramSchmidt instance = new GramSchmidt(A1);
        assertEquals(0, instance.rank());

        Matrix Q = instance.Q();
        assertEquals(Q.ZERO(), Q);//different from Householder reflection

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{0}});
        assertEquals(Rexpected, R);

        PermutationMatrix P = instance.P();
        assertTrue(AreMatrices.equal(A1.ONE(), P, 0));
    }

    /**
     * nRows < nCols, a fat matrix
     */
    @Test
    public void test_GramSchmidt_0100() {
        Matrix A1 = new DenseMatrix(R.seq(1, 15, 1d), 3, 5);

        GramSchmidt instance = new GramSchmidt(A1, true, 0);

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));
    }

    @Test
    public void test_GramSchmidt_0120() {
        Matrix A1 = new DenseMatrix(R.seq(1d, 10d, 1d), 2, 5);
        A1 = A1.t();
        GramSchmidt instance = new GramSchmidt(A1, true, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.squareQ();//this Q is not orthogonal
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.134839972492648, 0.7627700713964742, 0.6324555320336759, 0, 0},
                    {0.269679944985297, 0.4767312946227957, -0.6324555320336759, 0.5477225575051662, 0},
                    {0.404519917477945, 0.1906925178491184, -0.316227766016838, -0.7302967433402215, 0.40824829046386285},
                    {0.539359889970594, -0.0953462589245592, 0, -0.18257418583505522, -0.816496580927726},
                    {0.674199862463242, -0.3813850356982367, 0.3162277660168379, 0.3651483716701106, 0.4082482904638632}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Matrix R = instance.tallR();
        Matrix Rexpected = new DenseMatrix(new double[][]{
                    {7.41619848709566, 17.52919642404430},
                    {0, 4.76731294622797},
                    {0, 0},
                    {0, 0},
                    {0, 0}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));
    }

    @Test
    public void test_GramSchmidt_0125() {
        Matrix A1 = new DenseMatrix(R.seq(1d, 10d, 1d), 2, 5);
        A1 = A1.t();
        GramSchmidt instance = new GramSchmidt(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.squareQ();//this Q is not orthogonal
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.134839972492648, 0.7627700713964742, 0.6324555320336759, 0, 0},
                    {0.269679944985297, 0.4767312946227957, -0.6324555320336759, 0.5477225575051662, 0},
                    {0.404519917477945, 0.1906925178491184, -0.316227766016838, -0.7302967433402215, 0.40824829046386285},
                    {0.539359889970594, -0.0953462589245592, 0, -0.18257418583505522, -0.816496580927726},
                    {0.674199862463242, -0.3813850356982367, 0.3162277660168379, 0.3651483716701106, 0.4082482904638632}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Matrix R = instance.tallR();
        Matrix Rexpected = new DenseMatrix(new double[][]{
                    {7.41619848709566, 17.52919642404430},
                    {0, 4.76731294622797},
                    {0, 0},
                    {0, 0},
                    {0, 0}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));
    }

    /**
     * For a fat matrix A, the permutation matrix P is not square.
     * The last columns are 0.
     */
    @Test
    public void test_GramSchmidt_0130() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, -51, 4, 1},
                    {6, 167, -68, 10},
                    {-4, 24, -41, -22}
                });

        GramSchmidt instance = new GramSchmidt(A1, true, 1e-15);
        assertEquals(3, instance.rank());

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));
    }

    /**
     * For a fat matrix A, the permutation matrix P is not square.
     * The last columns are 0.
     */
    @Test
    public void test_GramSchmidt_0135() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, -51, 4, 1},
                    {6, 167, -68, 10},
                    {-4, 24, -41, -22}
                });

        GramSchmidt instance = new GramSchmidt(A1);
        assertEquals(3, instance.rank());

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(sqQ, 1e-15));

        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-14));
    }

    /**
     * The results of GramSchmidt and HouseholderReflection may differ by a sign.
     */
    @Test
    public void test_GramSchmidt_0140() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3e-100, 2e-100},
                    {1e-100, 2e-100}
                });
        GramSchmidt instance = new GramSchmidt(A1, true, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.948683298050514, -0.31622776601683},
                    {0.316227766016838, 0.948683298050514}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {3.16227766016838e-100, 2.52982212813470e-100},
                    {1.26491106406735e-100}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-114));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-115));
    }

    /**
     * The results of GramSchmidt and HouseholderReflection may differ by a sign.
     */
    @Test
    public void test_GramSchmidt_0150() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3e-100, 2e-100},
                    {1e-100, 2e-100}
                });
        GramSchmidt instance = new GramSchmidt(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {0.948683298050514, -0.31622776601683},
                    {0.316227766016838, 0.948683298050514}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {3.16227766016838e-100, 2.52982212813470e-100},
                    {1.26491106406735e-100}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-114));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-115));
    }
}
