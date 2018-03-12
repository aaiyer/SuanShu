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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class PowTest {

    double relativeDiff(Matrix A, Matrix B) {
        Matrix D = A.minus(B);

        double maxA = MatrixMeasure.max(A);
        double maxB = MatrixMeasure.max(B);
        double maxD = MatrixMeasure.max(D);

        return maxD / Math.max(maxA, maxB);
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3},
                    {2, 4}
                });

        Pow A2 = new Pow(A, 2, 10000);

        Matrix A2expected = new DenseMatrix(new double[][]{
                    {7, 15},
                    {10, 22}
                });
        assertEquals(A2expected, A2);
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3},
                    {2, 4}
                });

        Pow A3 = new Pow(A, 3, 10000);

        Matrix A3expected = new DenseMatrix(new double[][]{
                    {37, 81},
                    {54, 118}
                });
        assertEquals(A3expected, A3);
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 3},
                    {2, 4}
                });

        Pow A4 = new Pow(A, 4, 100);

        Matrix A4expected = new DenseMatrix(new double[][]{
                    {199, 435},
                    {290, 634}
                });
        assertTrue(AreMatrices.equal(A4expected, A4, 1e-13));

        assertEquals(1d, A4.scale(), 1e-15);
        assertTrue(AreMatrices.equal(A4.B(),
                new DenseMatrix(new double[][]{
                    {1.99, 4.35},
                    {2.90, 6.34}
                }),
                1e-15));
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        int base = 10;
        Pow A10 = new Pow(A, 10, 10);

        Matrix A10expected = new DenseMatrix(new double[][]{
                    {132476037840d, 162775103256d, 193074168672d},
                    {300005963406d, 368621393481d, 43723682355d},
                    {467535888972d, 574467683706d, 681399478440d}
                });
        assertTrue(relativeDiff(A10expected, A10) < 1e-15);
        assertEquals(11, A10.scale(), 1e-15);
        assertTrue(relativeDiff(A10expected, A10.B().scaled(Math.pow(base, A10.scale()))) < 1e-15);
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        int base = 10;
        Pow A16 = new Pow(A, 16, base);

        Matrix A16expected = new DenseMatrix(new double[][]{
                    {2321760077211226624d, 2852778075722657792d, 3383796074234089984d},
                    {5257870631770368000d, 6460416909794222080d, 7662963187818077184d},
                    {8193981186329508864d, 10068055743865786368d, 11942130301402064896d}
                });

        assertTrue(relativeDiff(A16expected, A16) < 1e-15);
        assertEquals(19, A16.scale(), 1e-15);
        assertTrue(relativeDiff(A16expected, A16.B().scaled(Math.pow(base, A16.scale()))) < 1e-15);
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        int base = 10;
        Pow A20 = new Pow(A, 20, base);

        Matrix A20expected = new DenseMatrix(new double[][]{
                    {1.566525053602059e+23, 1.924810565850575e+23, 2.283096078099092e+23},
                    {3.547561246362766e+23, 4.358936586618860e+23, 5.170311926874955e+23},
                    {5.528597439123471e+23, 6.793062607387145e+23, 8.057527775650818e+23}
                });

        assertTrue(relativeDiff(A20expected, A20) < 1e-15);
        assertEquals(23, A20.scale(), 1e-15);
        assertTrue(relativeDiff(A20expected, A20.B().scaled(Math.pow(base, A20.scale()))) < 1e-15);
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        int base = 10;

        for (int n = 1; n <= 200; n++) {
            Pow An = new Pow(A, n, base);
            double rdiff = relativeDiff(An, An.B().scaled(Math.pow(base, An.scale())));
            assertTrue(rdiff < 1e-15);
        }
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0080() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        int base = 100;

        for (int n = 1; n <= 200; n++) {
            Pow An = new Pow(A, n, base);
            double rdiff = relativeDiff(An, An.B().scaled(Math.pow(base, An.scale())));
            assertTrue(rdiff < 1e-15);
        }
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0090() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                });

        for (int n = 1; n <= 200; n++) {
            Pow An = new Pow(A, n);
            double rdiff = relativeDiff(An, An.B().scaled(Math.pow(1e100, An.scale())));
            assertTrue(rdiff < 1e-15);
        }
    }

    /**
     * Test of class Pow.
     */
    @Test
    public void test_Pow_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1.12, 2.65951, 3.213},
                    {4.4, 0.595, 6.55},
                    {7, 0.25, 9.101}
                });

        int base = 100;

        for (int n = 1; n <= 200; n++) {
            Pow An = new Pow(A, n, base);
            double rdiff = relativeDiff(An, An.B().scaled(Math.pow(base, An.scale())));
            assertTrue(rdiff < 1e-15);
        }
    }
}
