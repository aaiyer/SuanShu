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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.*;
import static com.numericalmethod.suanshu.matrix.doubles.operation.MatrixUtils.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class HouseholderReflectionTest {

    @Test
    public void test_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, 2},
                    {1, 2}
                });
        HouseholderReflection instance = new HouseholderReflection(A1, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.948683298050514, -0.31622776601683},
                    {-0.316227766016838, 0.948683298050514}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-3.16227766016838, -2.52982212813470},
                    {1.26491106406735}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));
    }

    @Test
    public void test_0015() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3, 2},
                    {1, 2}
                });
        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.948683298050514, -0.31622776601683},
                    {-0.316227766016838, 0.948683298050514}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-3.16227766016838, -2.52982212813470},
                    {1.26491106406735}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));
    }

    @Test
    public void test_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, -51, 4},
                    {6, 167, -68},
                    {-4, 24, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1, 0);
        assertEquals(3, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.857142857142857, 0.394285714285714, 0.3314285714285714},
                    {-0.428571428571429, -0.902857142857143, -0.0342857142857143},
                    {0.285714285714286, -0.171428571428571, 0.9428571428571428}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-15));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-15));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-14, -21, 14},
                    {-175, 70},
                    {-35}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));
    }

    @Test
    public void test_0025() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, -51, 4},
                    {6, 167, -68},
                    {-4, 24, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(3, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.857142857142857, 0.394285714285714, 0.3314285714285714},
                    {-0.428571428571429, -0.902857142857143, -0.0342857142857143},
                    {0.285714285714286, -0.171428571428571, 0.9428571428571428}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-15));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-15));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-14, -21, 14},
                    {-175, 70},
                    {-35}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));
    }

    @Test
    public void test_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        HouseholderReflection instance = new HouseholderReflection(A1, 1e-14);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.0341992784028384, -0.7738413334506800, -0.3595391142537895, -0.365119938649447, -0.370700763045105},
                    {-0.2051956704170308, -0.5078333750770072, -0.0316753013691522, 0.361173699874667, 0.754022701118487},
                    {-0.3761920624312232, -0.2418254167033372, 0.8680554446241514, -0.145511712308901, -0.159078869241954},
                    {-0.5471884544454155, 0.0241825416703336, -0.2029285281256860, 0.667982079591594, -0.461107312691127},
                    {-0.7181848464596079, 0.2901905000440044, -0.2739125008755232, -0.518524128507912, 0.236864243859700}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-29.240383034426891, -31.1213433465830072, -33.0023036587391, -34.8832639708952, -36.7642242830514},
                    {-1.2091270835166883, -2.41825416703338, -3.62738125055006, -4.83650833406675},
                    {0, 0, 0},
                    {0, 0},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix D = A2.minus(A1);
        double maxnorm = max(abs(to1DArray(D)));
        assertEquals(0, maxnorm, 1e-13);
    }

    @Test
    public void test_0032() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.0341992784028384, -0.7738413334506800, -0.3595391142537895, -0.365119938649447, -0.370700763045105},
                    {-0.2051956704170308, -0.5078333750770072, -0.0316753013691522, 0.361173699874667, 0.754022701118487},
                    {-0.3761920624312232, -0.2418254167033372, 0.8680554446241514, -0.145511712308901, -0.159078869241954},
                    {-0.5471884544454155, 0.0241825416703336, -0.2029285281256860, 0.667982079591594, -0.461107312691127},
                    {-0.7181848464596079, 0.2901905000440044, -0.2739125008755232, -0.518524128507912, 0.236864243859700}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));
        assertTrue(AreMatrices.equal(Qexpected, instance.squareQ(), 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-29.240383034426891, -31.1213433465830072, -33.0023036587391, -34.8832639708952, -36.7642242830514},
                    {-1.2091270835166883, -2.41825416703338, -3.62738125055006, -4.83650833406675},
                    {0, 0, 0},
                    {0, 0},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));
        assertTrue(AreMatrices.equal(Rexpected, instance.tallR(), 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix D = A2.minus(A1);
        double maxnorm = max(abs(to1DArray(D)));
        assertEquals(0, maxnorm, 1e-13);
    }

    @Test
    public void test_0350() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });
        A1 = A1.scaled(1e-20);

        HouseholderReflection instance = new HouseholderReflection(A1, 1e-28);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));
    }

    @Test
    public void test_0040() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}
                });

        HouseholderReflection instance = new HouseholderReflection(A1, 1e-14);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.0341992784028384, -0.7738413334506800, -0.3595391142537895},
                    {-0.2051956704170308, -0.5078333750770072, -0.0316753013691522},
                    {-0.3761920624312232, -0.2418254167033372, 0.8680554446241514},
                    {-0.5471884544454155, 0.0241825416703336, -0.2029285281256860},
                    {-0.7181848464596079, 0.2901905000440044, -0.2739125008755232}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Matrix Qcomplete = instance.squareQ();
        Qexpected = new DenseMatrix(new double[][]{
                    {-0.0341992784028384, -0.7738413334506800, -0.3595391142537895, -0.365119938649447, -0.370700763045105},
                    {-0.2051956704170308, -0.5078333750770072, -0.0316753013691522, 0.361173699874667, 0.754022701118487},
                    {-0.3761920624312232, -0.2418254167033372, 0.8680554446241514, -0.145511712308901, -0.159078869241954},
                    {-0.5471884544454155, 0.0241825416703336, -0.2029285281256860, 0.667982079591594, -0.461107312691127},
                    {-0.7181848464596079, 0.2901905000440044, -0.2739125008755232, -0.518524128507912, 0.236864243859700}
                });
        assertTrue(AreMatrices.equal(Qexpected, Qcomplete, 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-29.240383034426891, -31.1213433465830072, -33.0023036587391},
                    {-1.2091270835166883, -2.41825416703338},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));

        Matrix R2 = instance.tallR();
        Matrix Rexpected2 = new DenseMatrix(new double[][]{
                    {-29.2403830344269, -31.12134334658301, -33.0023036587391},
                    {0, -1.20912708351669, -2.41825416703338},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });
        assertTrue(AreMatrices.equal(Rexpected2, R2, 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));
    }

    @Test
    public void test_0045() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}
                });

        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.0341992784028384, -0.7738413334506800, -0.3595391142537895},
                    {-0.2051956704170308, -0.5078333750770072, -0.0316753013691522},
                    {-0.3761920624312232, -0.2418254167033372, 0.8680554446241514},
                    {-0.5471884544454155, 0.0241825416703336, -0.2029285281256860},
                    {-0.7181848464596079, 0.2901905000440044, -0.2739125008755232}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-14));

        Matrix Qcomplete = instance.squareQ();
        Qexpected = new DenseMatrix(new double[][]{
                    {-0.0341992784028384, -0.7738413334506800, -0.3595391142537895, -0.365119938649447, -0.370700763045105},
                    {-0.2051956704170308, -0.5078333750770072, -0.0316753013691522, 0.361173699874667, 0.754022701118487},
                    {-0.3761920624312232, -0.2418254167033372, 0.8680554446241514, -0.145511712308901, -0.159078869241954},
                    {-0.5471884544454155, 0.0241825416703336, -0.2029285281256860, 0.667982079591594, -0.461107312691127},
                    {-0.7181848464596079, 0.2901905000440044, -0.2739125008755232, -0.518524128507912, 0.236864243859700}
                });
        assertTrue(AreMatrices.equal(Qexpected, Qcomplete, 1e-14));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{
                    {-29.240383034426891, -31.1213433465830072, -33.0023036587391},
                    {-1.2091270835166883, -2.41825416703338},
                    {0}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-13));

        Matrix R2 = instance.tallR();
        Matrix Rexpected2 = new DenseMatrix(new double[][]{
                    {-29.2403830344269, -31.12134334658301, -33.0023036587391},
                    {0, -1.20912708351669, -2.41825416703338},
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                });
        assertTrue(AreMatrices.equal(Rexpected2, R2, 1e-13));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));
    }

    /**
     * This shows that Householder Reflection is more numerically stable than Gram-Schmidt.
     *
     * Using Gram-Schmidt procedure,
     * there is a loss of orthogonal,
     * when the number of iterations or dimension of the matrix increase.
     */
//    @Test
    public void test_0050() {
        int dim = 500;
        Matrix A1 = new DenseMatrix(dim, dim);
        int count = 0;
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= dim; ++j) {
                A1.set(i, j, ++count);
            }
        }

        HouseholderReflection instance = new HouseholderReflection(A1, 1e-7);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        assertEquals(-11.1970473508047, R.get(2, 2), 1e-8);//The product still loses out a bit of precision but not as much as in the Gram-Schmidt case.
        assertEquals(-548.655320191059, R.get(2, 50), 1e-8);
        assertEquals(0, R.get(4, 4), 1e-8);
        assertEquals(0, R.get(dim, dim), 1e-7);

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-6));

        Matrix A3 = instance.squareQ().multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-6));
    }

    /**
     * This shows that Householder Reflection is more numerically stable than Gram-Schmidt.
     *
     * Using Gram-Schmidt procedure,
     * there is a loss of orthogonal,
     * when the number of iterations or dimension of the matrix increase.
     */
//    @Test
    public void test_0060() {
        int dim = 3500;
        Matrix A1 = new DenseMatrix(dim, dim);

        int count = 0;
        for (int i = 1; i <= dim; ++i) {
            for (int j = 1; j <= dim; ++j) {
                A1.set(i, j, ++count);
            }
        }

        HouseholderReflection instance = new HouseholderReflection(A1, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        assertEquals(-418327545.62095827, R.get(1, 1), 1e-6);//set this according to the magnitude of R[1,1];
        /*
         * TODO: R seems to confuse the values for R[2,2] and R[2,3], or am I wrong?
         */
//        assertEquals(418327648.083148, R.get(1, 2), 1e-6);//TODO: what is the true value?
        assertEquals(-29.5867341769226, R.get(2, 2), 1e-6);
        assertEquals(-59.1734683535534, R.get(2, 3), 1e-6);
        assertEquals(-88.7602025281696, R.get(2, 4), 1e-6);
        assertEquals(-118.346936702786, R.get(2, 5), 1e-6);
        assertEquals(-562.147949336131, R.get(2, 20), 1e-6);
        assertEquals(-5887.76010092419, R.get(2, 200), 1e-6);
        assertEquals(-59143.8816168007, R.get(2, 2000), 1e-6);
        assertEquals(0, R.get(4, 4), 1e-6);
        assertEquals(0, R.get(dim, dim), 1e-6);

        Matrix A2 = Q.multiply(R);
        assertEquals(A1, A2);
    }

    @Test
    public void test_0070() {
        double[][] data = {{1}};
        Matrix A1 = new DenseMatrix(data);

        HouseholderReflection instance = new HouseholderReflection(A1, 0);
        assertEquals(1, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{1}});
        assertEquals(Rexpected, R);

        Matrix A2 = Q.multiply(R);
        assertEquals(A1, A2);
    }

    @Test
    public void test_0075() {
        double[][] data = {{1}};
        Matrix A1 = new DenseMatrix(data);

        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(1, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{1}});
        assertEquals(Rexpected, R);

        Matrix A2 = Q.multiply(R);
        assertEquals(A1, A2);
    }

    @Test
    public void test_0080() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {2, 3},
                    {1, 3},
                    {2, 3}
                });
        HouseholderReflection instance = new HouseholderReflection(A1, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();//this Q is not orthogonal
        UpperTriangularMatrix R = instance.R();
        Matrix Rexpected = new DenseMatrix(new double[][]{
                    {-3, -5},
                    {0, -1.4142135623731}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));
    }

    @Test
    public void test_0085() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {2, 3},
                    {1, 3},
                    {2, 3}
                });
        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();//this Q is not orthogonal
        UpperTriangularMatrix R = instance.R();
        Matrix Rexpected = new DenseMatrix(new double[][]{
                    {-3, -5},
                    {0, -1.4142135623731}});
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));
    }

    /**
     * Test of class HouseholderReflection.
     * [0] %*% [1] = [1] %*% [0] = [0]
     * The results of Gram-Schmidt and Householder are different.
     */
    @Test
    public void test_0090() {
        double[][] data = {{0}};
        Matrix A1 = new DenseMatrix(data);

        HouseholderReflection instance = new HouseholderReflection(A1, 0);
        assertEquals(0, instance.rank());

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);//different from Gram-Schmidt

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{0}});
        assertEquals(Rexpected, R);
    }

    /**
     * [0] %*% [1] = [1] %*% [0] = [0]
     * The results of Gram-Schmidt and Householder are different.
     */
    @Test
    public void test_0095() {
        double[][] data = {{0}};
        Matrix A1 = new DenseMatrix(data);

        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(0, instance.rank());

        Matrix Q = instance.Q();
        assertEquals(Q.ONE(), Q);//different from Gram-Schmidt

        UpperTriangularMatrix R = instance.R();
        UpperTriangularMatrix Rexpected = new UpperTriangularMatrix(new double[][]{{0}});
        assertEquals(Rexpected, R);
    }

    /**
     * nRows > nCols
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_0100() {
        Matrix A1 = new DenseMatrix(R.seq(1, 15, 1d), 3, 5);

        HouseholderReflection hr = new HouseholderReflection(A1, 0);
    }

    /**
     * Test for squareQ and tallR.
     */
    @Test
    public void test_0120() {
        Matrix A1 = new DenseMatrix(R.seq(1d, 10d, 1d), 2, 5);
        A1 = A1.t();
        HouseholderReflection instance = new HouseholderReflection(A1, 0);

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.134839972492648, -0.7627700713964742},
                    {-0.269679944985297, -0.4767312946227957},
                    {-0.404519917477945, -0.1906925178491184},
                    {-0.539359889970594, 0.0953462589245592},
                    {-0.674199862463242, 0.3813850356982367}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-15));

        Matrix Qcomplete = instance.squareQ();
        Qexpected = new DenseMatrix(new double[][]{
                    {-0.134839972492648, -0.7627700713964742, -0.37361262647279736, -0.365081933523425, -0.356551240574052},
                    {-0.269679944985297, -0.4767312946227957, -0.00758312901914571, 0.371114044356147, 0.749811217731439},
                    {-0.404519917477945, -0.1906925178491184, 0.86173398722565653, -0.158835181015250, -0.179404349256156},
                    {-0.539359889970594, 0.0953462589245592, -0.20626808150268561, 0.664655963055759, -0.464419992385796},
                    {-0.674199862463242, 0.3813850356982367, -0.27427015023102769, -0.511852892873231, 0.250564364484565}
                });
        assertTrue(AreMatrices.equal(Qexpected, Qcomplete, 1e-15));

        Matrix R = instance.tallR();
        Matrix Rexpected = new DenseMatrix(new double[][]{
                    {-7.41619848709566, -17.52919642404430},
                    {0, -4.76731294622797},
                    {0, 0},
                    {0, 0},
                    {0, 0}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));

        Matrix A2 = Qcomplete.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));
    }

    /**
     * Test for squareQ and tallR.
     */
    @Test
    public void test_0125() {
        Matrix A1 = new DenseMatrix(R.seq(1d, 10d, 1d), 2, 5);
        A1 = A1.t();
        HouseholderReflection instance = new HouseholderReflection(A1);

        Matrix Q = instance.Q();
        Matrix Qexpected = new DenseMatrix(new double[][]{
                    {-0.134839972492648, -0.7627700713964742},
                    {-0.269679944985297, -0.4767312946227957},
                    {-0.404519917477945, -0.1906925178491184},
                    {-0.539359889970594, 0.0953462589245592},
                    {-0.674199862463242, 0.3813850356982367}
                });
        assertTrue(AreMatrices.equal(Qexpected, Q, 1e-15));

        Matrix Qcomplete = instance.squareQ();
        Qexpected = new DenseMatrix(new double[][]{
                    {-0.134839972492648, -0.7627700713964742, -0.37361262647279736, -0.365081933523425, -0.356551240574052},
                    {-0.269679944985297, -0.4767312946227957, -0.00758312901914571, 0.371114044356147, 0.749811217731439},
                    {-0.404519917477945, -0.1906925178491184, 0.86173398722565653, -0.158835181015250, -0.179404349256156},
                    {-0.539359889970594, 0.0953462589245592, -0.20626808150268561, 0.664655963055759, -0.464419992385796},
                    {-0.674199862463242, 0.3813850356982367, -0.27427015023102769, -0.511852892873231, 0.250564364484565}
                });
        assertTrue(AreMatrices.equal(Qexpected, Qcomplete, 1e-15));

        Matrix R = instance.tallR();
        Matrix Rexpected = new DenseMatrix(new double[][]{
                    {-7.41619848709566, -17.52919642404430},
                    {0, -4.76731294622797},
                    {0, 0},
                    {0, 0},
                    {0, 0}
                });
        assertTrue(AreMatrices.equal(Rexpected, R, 1e-14));

        Matrix A2 = Qcomplete.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-14));
    }

    @Test
    public void test_0130() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1, 0);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    @Test
    public void test_0135() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    /**
     * Same as in test_0130,
     * but with a different precision parameter.
     */
    @Test
    public void test_0140() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1, 1e-15);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    /**
     * Same as in test_0130,
     * but with a different precision parameter.
     */
    @Test
    public void test_0145() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    /**
     * Same as in test_0130,
     * but with a different precision parameter.
     */
    @Test
    public void test_0150() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1, 1e-10);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    /**
     * Same as in test_0130,
     * but with a different precision parameter.
     */
    @Test
    public void test_0155() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    /**
     * Same as in test_0130,
     * but with a different precision parameter.
     */
    @Test
    public void test_0160() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1, 1e-5);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    /**
     * Same as in test_0130,
     * but with a different precision parameter.
     */
    @Test
    public void test_0165() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {12, 0, 4},
                    {6, 0, -68},
                    {-4, 0, -41}
                });
        HouseholderReflection instance = new HouseholderReflection(A1);
        assertEquals(2, instance.rank());

        Matrix Q = instance.Q();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        UpperTriangularMatrix R = instance.R();
        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix sqQ = instance.squareQ();
        assertTrue(IsMatrix.orthogonal(Q, 1e-15));
        Matrix A3 = sqQ.multiply(instance.tallR());
        assertTrue(AreMatrices.equal(A1, A3, 1e-13));
    }

    @Test
    public void test_0170() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        HouseholderReflection instance = new HouseholderReflection(A1, 0);

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        Matrix QQt = Q.multiply(Q.t());
        assertTrue(AreMatrices.equal(QQt, QQt.ONE(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix D = A2.minus(A1);
        double maxnorm = max(abs(to1DArray(D)));
        assertEquals(0, maxnorm, 1e-13);
    }

    @Test
    public void test_0180() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5},
                    {6, 7, 8, 9, 10},
                    {11, 12, 13, 14, 15},
                    {16, 17, 18, 19, 20},
                    {21, 22, 23, 24, 25}
                });

        HouseholderReflection instance = new HouseholderReflection(A1);

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        Matrix QQt = Q.multiply(Q.t());
        assertTrue(AreMatrices.equal(QQt, QQt.ONE(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        Matrix D = A2.minus(A1);
        double maxnorm = max(abs(to1DArray(D)));
        assertEquals(0, maxnorm, 1e-13);
    }

    @Test
    public void test_0190() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 0.5},
                    {0, 1}
                });

        HouseholderReflection instance = new HouseholderReflection(A1);

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        Matrix QQt = Q.multiply(Q.t());
        assertTrue(AreMatrices.equal(QQt, QQt.ONE(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        int rank = instance.rank();
        assertEquals(2, rank);
    }

    @Test
    public void test_0200() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {-0.22112894416615017, -0.008495122726671599, -0.013904044256496344, 0.018195978004875962},
                    {0.02170482221352272, -0.24729700934839935, 0.08073606184864421, 0.007533998463136099},
                    {0.11330772498851954, 0.1514674566076728, -0.03895149472753445, -0.017122678011985634},
                    {0.2981288581469038, 0.04812388712259168, 0.04369939759017251, -0.11078281629177006}
                });

        HouseholderReflection instance = new HouseholderReflection(A1, 10 * SuanShuUtils.autoEpsilon(A1));

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        Matrix QQt = Q.multiply(Q.t());
        assertTrue(AreMatrices.equal(QQt, QQt.ONE(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1, A2, 1e-13));

        int rank = instance.rank();
        assertEquals(3, rank);
    }

    /**
     * It is hard to detect the rank for this matrix.
     * Even R gets it wrong.
     *
     * b = c(-0.22112894682217057,0.02170482221352272,0.11330772498851954,0.2981288581469038,
     * -0.008495122726671599,-0.24729701200441975,0.1514674566076728,0.04812388712259168,
     * -0.013904044256496344,0.08073606184864421,-0.038951497383554856,0.04369939759017251,
     * 0.018195978004875962,0.007533998463136099,-0.017122678011985634,-0.11078281894779046
     * )
     *
     * B = matrix(b, 4, 4)
     * Bt = t(B)
     *
     * qrbt = qr(Bt)
     * qrbt$rank #R gives 4 but should be 3; R[4,4] is too big for R's default tolerance of 1e-7
     *
     */
    @Test
    public void test_0210() {
        DenseMatrix A1 = new DenseMatrix(new double[][]{
                    {-0.22112894682217057, -0.0084951227266716, -0.01390404425649634, 0.01819597800487596},
                    {0.02170482221352272, -0.2472970120044198, 0.08073606184864421, 0.00753399846313610},
                    {0.11330772498851954, 0.1514674566076728, -0.03895149738355486, -0.01712267801198563},
                    {0.29812885814690382, 0.0481238871225917, 0.04369939759017251, -0.11078281894779046}
                });

        HouseholderReflection instance = new HouseholderReflection(A1.t(), 1e-6);//anything smaller than 1e-6 will fail to find the correct rank

        Matrix Q = instance.Q();
        UpperTriangularMatrix R = instance.R();

        Matrix QQt = Q.multiply(Q.t());
        assertTrue(AreMatrices.equal(QQt, QQt.ONE(), 1e-14));

        Matrix A2 = Q.multiply(R);
        assertTrue(AreMatrices.equal(A1.t(), A2, 1e-8));

        int rank = instance.rank();
        assertEquals(3, rank);
    }
}
