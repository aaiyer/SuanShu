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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class InverseIterationTest {

    /**
     * Example 7.6.1 in "Golub G. H., van Loan C. F. Matrix Computations, 3rd edition."
     */
    @Test
    public void test_0010() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, 1},
                    {1e-10, 1}
                });

        InverseIteration instance = new InverseIteration(A, 0.99999);
        Vector x1 = instance.getEigenVector(new DenseVector(1.0, 1.0), 10);
        assertArrayEquals(new double[]{-1, 1e-5}, x1.toArray(), 1e-6);//change the sign as in the book
    }

    /**
     * Example 7.6.1 in "Golub G. H., van Loan C. F. Matrix Computations, 3rd edition."
     */
    @Test
    public void test_0020() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, 1},
                    {1e-10, 1}
                });

        InverseIteration instance = new InverseIteration(A, 1.00001);
        Vector x1 = instance.getEigenVector(new DenseVector(1.0, 1.0), 10);
        assertArrayEquals(new double[]{1, 1e-5}, x1.toArray(), 1e-6);
    }

    /**
     * http://en.wikipedia.org/wiki/Rayleigh_quotient_iteration
     * 
     * expected answers from R
     */
    @Test
    public void test_0030() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {2, 2, 1},
                    {3, 1, 1}
                });

        InverseIteration instance = new InverseIteration(A, 5.3648808);//the wiki seems to have the wrong eigenvalue
        Vector x1 = instance.getEigenVector(new DenseVector(1.0, 1.0, 1.0), 10);
        assertArrayEquals(new double[]{0.6305371, 0.5403585, 0.5571675}, x1.toArray(), 1e-7);
    }

    /**
     * Inverse iteration may not apply when lambda is the <em>exact</em> eigenvalue because (A - λI) may be singular.
     */
//    @Test
    public void test_0040() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });

        InverseIteration instance = new InverseIteration(A, -0.4641016151377546);
        Vector x1 = instance.getEigenVector(new DenseVector(1.0, 1.0), 10);
        assertArrayEquals(new double[]{0.9390708015880442, -0.3437237693334403}, x1.toArray(), 1e-7);
    }

    @Test
    public void test_0050() {
        DenseMatrix A = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });

        InverseIteration instance = new InverseIteration(A, -0.464101615137754);//artificial round off the eigenvalue
        Vector x1 = instance.getEigenVector(new DenseVector(1.0, 1.0), 10);
        assertArrayEquals(new double[]{0.9390708015880442, -0.3437237693334403}, x1.toArray(), 1e-7);
    }
}
