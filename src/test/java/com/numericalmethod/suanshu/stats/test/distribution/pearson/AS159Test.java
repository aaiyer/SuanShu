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
package com.numericalmethod.suanshu.stats.test.distribution.pearson;

import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.stats.test.distribution.pearson.AS159.RandomMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class AS159Test {

    /**
     * the probability of 2x2 matrix can be verified by the R program
     *
    pr <- function(a,b,c,d)
    {
    e = gamma(a+b+1)
    e = e * gamma(c+d+1)
    e = e * gamma(a+c+1)
    e = e * gamma(b+d+1)
    e = e /gamma(a+b+c+d+1)
    e = e / gamma(a+1)
    e = e / gamma(b+1)
    e = e / gamma(c+1)
    e = e / gamma(d+1)

    return (e)
    }
     *
     * @see http://faculty.vassar.edu/lowry/fisher.html
     *
     */
    @Test
    public void test_0010() {
        RandomLongGenerator rng = new UniformRng();
        rng.seed(1);

        AS159 instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9},//column sums
                rng);

        RandomMatrix A = instance.nextSample();
        assertTrue(instance.isValidated(A.A));
        assertEquals(0.1909545562796337, A.prob, 1e-14);

        rng.seed(22);
        instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9},//column sums
                rng);

        A = instance.nextSample();
        assertTrue(instance.isValidated(A.A));
        assertEquals(0.3437182013033407, A.prob, 1e-14);

        rng.seed(100);
        instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9},//column sums
                rng);

        A = instance.nextSample();
        assertTrue(instance.isValidated(A.A));
        assertEquals(0.2864318344194505, A.prob, 1e-14);

        rng.seed(10000000000L);
        instance = new AS159(
                new int[]{22, 23},//row sums
                new int[]{23, 22},//column sums
                rng);

        A = instance.nextSample();
        assertTrue(instance.isValidated(A.A));
        assertEquals(0.2316893453657629, A.prob, 1e-13);
    }

    @Test
    public void test_0015() {
        RandomLongGenerator rng = new UniformRng();

        AS159 instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9});//column sums

        Matrix B = new DenseMatrix(new double[][]{
                    {6, 3},
                    {4, 6}
                });

        int N = 50000;
        int count = 0;
        for (int i = 0; i < N; ++i) {
            RandomMatrix A = instance.nextSample();
            if (MatrixMeasure.Frobenius(B.minus(A.A)) < 1e-10) {
                count++;
            }
        }

        assertEquals(0.1909545562796337, (double) count / N, 1e-2);
    }

    @Test
    public void test_0020() {
        AS159 instance = new AS159(
                new int[]{6, 5},//row sums
                new int[]{3, 4, 4});//column sums

        for (int i = 0; i < 1000; ++i) {
            RandomMatrix A = instance.nextSample();
            assertTrue(instance.isValidated(A.A));
        }
    }

    /**
     * 1   2  3
     * 6   7  8
     * 11 12 13
     */
    @Test
    public void test_0030() {
        AS159 instance = new AS159(
                new int[]{6, 21, 36},//row sums
                new int[]{18, 21, 24});//column sums

        for (int i = 0; i < 1000; ++i) {
            RandomMatrix A = instance.nextSample();
            assertTrue(instance.isValidated(A.A));
        }
    }

    /**
     * 1   2  3  4
     * 6   7  8  9
     * 11 12 13 14
     * 16 17 18 19
     */
    @Test
    public void test_0040() {
        AS159 instance = new AS159(
                new int[]{10, 30, 50, 70},//row sums
                new int[]{34, 38, 42, 46});//column sums

        for (int i = 0; i < 1000; ++i) {
            RandomMatrix A = instance.nextSample();
            assertTrue(instance.isValidated(A.A));
        }
    }

    /**
     * 1   2  3  4  5
     * 6   7  8  9 10
     * 11 12 13 14 15
     * 16 17 18 19 20
     * 21 22 23 24 25
     */
    @Test
    public void test_0050() {
        AS159 instance = new AS159(
                new int[]{15, 40, 65, 90, 115},//row sums
                new int[]{55, 60, 65, 70, 75});//column sums

        for (int i = 0; i < 1000; ++i) {
            RandomMatrix A = instance.nextSample();
            assertTrue(instance.isValidated(A.A));
        }
    }

    /**
     * http://www.physics.csbsju.edu/stats/contingency.html
     * http://www.physics.csbsju.edu/stats/exact.html
     * http://www.physics.csbsju.edu/stats/3x3_count_tables.w.p.txt
     */
    @Test
    public void test_0060() {
        RandomLongGenerator rng = new UniformRng();
        rng.seed(3);

        AS159 instance = new AS159(
                new int[]{10, 9, 5},//row sums
                new int[]{7, 8, 9},//column sums
                rng);

        RandomMatrix A = instance.nextSample();
        assertTrue(instance.isValidated(A.A));
        assertEquals(0.006290, A.prob, 1e-6);//from http://www.physics.csbsju.edu/stats/3x3_count_tables.w.p.txt
    }

    @Test
    public void test_0065() {
        RandomLongGenerator rng = new UniformRng();

        AS159 instance = new AS159(
                new int[]{10, 9, 5},//row sums
                new int[]{7, 8, 9});//column sums

        Matrix B = new DenseMatrix(new double[][]{
                    {4, 3, 3},
                    {3, 2, 4},
                    {0, 3, 2}
                });

        int N = 1000000;
        int count = 0;
        for (int i = 0; i < N; ++i) {
            RandomMatrix A = instance.nextSample();
            if (MatrixMeasure.Frobenius(B.minus(A.A)) < 1e-10) {
                count++;
            }
        }

        assertEquals(0.006290, (double) count / N, 1e-3);
    }

    /**
     * http://www.physics.csbsju.edu/stats/contingency.html
     * http://www.physics.csbsju.edu/stats/exact.html
     * http://www.physics.csbsju.edu/stats/3x3_count_tables.w.p.txt
     */
    @Test
    public void test_0070() {
        RandomLongGenerator rng = new UniformRng();
        rng.seed(306962);

        AS159 instance = new AS159(
                new int[]{10, 9, 5},//row sums
                new int[]{7, 8, 9},//column sums
                rng);

        RandomMatrix A = instance.nextSample();
        assertTrue(instance.isValidated(A.A));
        assertEquals(1.0E-06, A.prob, 1e-6);//from http://www.physics.csbsju.edu/stats/3x3_count_tables.w.p.txt
    }

////    @Test
//    public void test_0075() {
//        RandomLongGenerator rng = new UniformRng();
//
//        AS159 instance = new AS159(
//                new int[]{10, 9, 5},//row sums
//                new int[]{7, 8, 9});//column sums
//
//        Matrix B = new DenseMatrix(new double[][]{
//                    {1, 0, 9},
//                    {3, 6, 0},
//                    {3, 2, 0}
//                });
//
//        int N = 2000000;
//        int count = 0;
//        for (int i = 0; i < N; ++i) {
//            RandomMatrix A = instance.nextSample();
//            if (MatrixMeasure.Frobenius(B.minus(A.A)) < 1e-10) {
//                count++;
//            }
//        }
//
//        assertTrue(count >= 1 && count <= 3);
//    }
    /**
     * @see http://faculty.vassar.edu/lowry/fisher.html
     */
    @Test
    public void test_0080() {
        RandomLongGenerator rng = new UniformRng();

        AS159 instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9});//column sums

        Matrix B = new DenseMatrix(new double[][]{
                    {2, 7},
                    {8, 2}
                });

        int N = 2000000;
        int count = 0;
        for (int i = 0; i < N; ++i) {
            RandomMatrix A = instance.nextSample();
            if (MatrixMeasure.Frobenius(B.minus(A.A)) < 1e-10) {
                count++;
            }
        }

        assertEquals(0.01754, (double) count / N, 1e-3);
    }

    /**
     * @see http://faculty.vassar.edu/lowry/fisher.html
     */
    @Test
    public void test_0082() {
        RandomLongGenerator rng = new UniformRng();

        AS159 instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9});//column sums

        Matrix B = new DenseMatrix(new double[][]{
                    {1, 8},
                    {9, 1}
                });

        int N = 2000000;
        int count = 0;
        for (int i = 0; i < N; ++i) {
            RandomMatrix A = instance.nextSample();
            if (MatrixMeasure.Frobenius(B.minus(A.A)) < 1e-10) {
                count++;
            }
        }

        assertEquals(0.00097, (double) count / N, 1e-4);
    }

    /**
     * @see http://faculty.vassar.edu/lowry/fisher.html
     */
    @Test
    public void test_0084() {
        RandomLongGenerator rng = new UniformRng();

        AS159 instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9});//column sums

        Matrix B = new DenseMatrix(new double[][]{
                    {8, 1},
                    {2, 8}
                });

        int N = 2000000;
        int count = 0;
        for (int i = 0; i < N; ++i) {
            RandomMatrix A = instance.nextSample();
            if (MatrixMeasure.Frobenius(B.minus(A.A)) < 1e-10) {
                count++;
            }
        }

        assertEquals(0.00438, (double) count / N, 1e-3);
    }

    /**
     * @see http://faculty.vassar.edu/lowry/fisher.html
     */
    @Test
    public void test_0086() {
        RandomLongGenerator rng = new UniformRng();

        AS159 instance = new AS159(
                new int[]{9, 10},//row sums
                new int[]{10, 9});//column sums

        Matrix B = new DenseMatrix(new double[][]{
                    {9, 0},
                    {1, 9}
                });

        int N = 2000000;
        int count = 0;
        for (int i = 0; i < N; ++i) {
            RandomMatrix A = instance.nextSample();
            if (MatrixMeasure.Frobenius(B.minus(A.A)) < 1e-10) {
                count++;
            }
        }

        assertEquals(0.00011, (double) count / N, 1e-4);
    }
}
