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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.solver.iterative.stationary;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class SORSweepTest {

    @Test
    public void test_forward_0010() {
        int n = 5;
        Matrix A = new DenseMatrix(R.rep(1., n * n), n, n); // n-by-n matrix with all entries equal to 1
        Vector b = new DenseVector(R.seq(1., n, 1.));

        SORSweep sweep = new SORSweep(A, b, 1);

        Vector x = new DenseVector(n).ZERO();

        x = sweep.forward(x);
        assertArrayEquals(new double[]{1, 1, 1, 1, 1}, x.toArray(), 1e-15);

        x = sweep.forward(x);
        assertArrayEquals(new double[]{-3, 2, 2, 2, 2}, x.toArray(), 1e-15);

        x = sweep.forward(x);
        assertArrayEquals(new double[]{-7, 3, 3, 3, 3}, x.toArray(), 1e-15);
    }

    @Test
    public void test_backward_0010() {
        int n = 5;
        Matrix A = new DenseMatrix(R.rep(1., n * n), n, n); // n-by-n matrix with all entries equal to 1
        Vector b = new DenseVector(R.seq(1., n, 1.));

        SORSweep sweep = new SORSweep(A, b, 1);

        Vector x = new DenseVector(n).ZERO();

        x = sweep.backward(x);
        assertArrayEquals(new double[]{-1, -1, -1, -1, 5}, x.toArray(), 1e-15);

        x = sweep.backward(x);
        assertArrayEquals(new double[]{-2, -2, -2, -2, 9}, x.toArray(), 1e-15);

        x = sweep.backward(x);
        assertArrayEquals(new double[]{-3, -3, -3, -3, 13}, x.toArray(), 1e-15);
    }
}
