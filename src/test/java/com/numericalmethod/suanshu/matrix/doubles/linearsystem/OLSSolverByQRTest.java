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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class OLSSolverByQRTest {

    @Test
    public void test_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 1},
                    {1, 2},
                    {1, 3},
                    {1, 4}
                });

        Vector y = new DenseVector(new double[]{6, 5, 7, 10});

        OLSSolverByQR instance = new OLSSolverByQR(0);
        Vector b1 = instance.solve(new LSProblem(A, y));
        assertArrayEquals(new double[]{3.5, 1.4}, b1.toArray(), 1e-14);
    }

    @Test
    public void test_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 1},
                    {1, 2},
                    {1, 3},
                    {1, 4}
                });

        Vector y = new DenseVector(new double[]{6, 5, 7, 10});

        OLSSolverByQR instance = new OLSSolverByQR(0);
        Vector b1 = instance.solve(new LSProblem(A, y));
        assertArrayEquals(new double[]{3.5, 1.4}, b1.toArray(), 1e-14);
    }

    @Test
    public void test_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 1},
                    {1, 2},
                    {1, 3},
                    {1, 4}
                });
        A = A.scaled(1e-100);

        Vector y = new DenseVector(new double[]{6e-100, 5e-100, 7e-100, 10e-100});

        OLSSolverByQR instance = new OLSSolverByQR(0);
        Vector b1 = instance.solve(new LSProblem(A, y));
        assertArrayEquals(new double[]{3.5, 1.4}, b1.toArray(), 1e-14);
    }
}
