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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.random.multivariate.IID;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Ken Yiu
 */
public class ParallelMatrixMathOperationTest extends MatrixMathOperationTest {

    @Override
    public MatrixMathOperation newInstance() {
        return new ParallelMatrixMathOperation();
    }

    @Test
    public void test_speed_0010() {
        int matrixSize = 1000;
        IID iid = new IID(new UniformRng(), matrixSize * matrixSize);
        Matrix A1 = new DenseMatrix(iid.nextVector(), matrixSize, matrixSize);
        Matrix A2 = new DenseMatrix(iid.nextVector(), matrixSize, matrixSize);

        long start = System.currentTimeMillis();
        Matrix result1 = new SimpleMatrixMathOperation().multiply(A1, A2);
        long timeSimple = System.currentTimeMillis() - start;
        System.out.println("time taken (Simple): " + timeSimple);

        start = System.currentTimeMillis();
        Matrix result2 = new ParallelMatrixMathOperation().multiply(A1, A2);
        long timeParallel = System.currentTimeMillis() - start;
        System.out.println("time taken (Parallel): " + timeParallel);

        start = System.currentTimeMillis();
        Matrix result3 = A1.multiply(A2);
        long timeDenseParallel = System.currentTimeMillis() - start;
        System.out.println("time taken (Dense Parallel): " + timeDenseParallel);

        assertTrue("results computed by simple and parallel algorithms should be the same",
                AreMatrices.equal(result1, result2, 1e-5));
        assertTrue("results computed by simple and parallel algorithms should be the same",
                AreMatrices.equal(result1, result3, 1e-5));

        assertTrue("Dense Parallel should be the fastest",
                timeDenseParallel < timeParallel && timeParallel < timeSimple);
    }

    @Test
    public void test_speedForManyMatrixMultiplications() {
        int matrixSize = 400;
        IID iid = new IID(new UniformRng(), matrixSize * matrixSize);
        Matrix A1 = new DenseMatrix(iid.nextVector(), matrixSize, matrixSize);
        int nLoops = 100;

        // compute A1 to the power 100
        Matrix M1 = A1;
        SimpleMatrixMathOperation simple = new SimpleMatrixMathOperation();
        long start = System.currentTimeMillis();
        for (int i = 0; i < nLoops; ++i) {
            M1 = simple.multiply(M1, A1);
        }
        long timeSimple = System.currentTimeMillis() - start;
        System.out.println("time taken (Simple): " + timeSimple);

        Matrix M2 = A1;
        ParallelMatrixMathOperation parallel = new ParallelMatrixMathOperation();
        start = System.currentTimeMillis();
        for (int i = 0; i < nLoops; ++i) {
            M2 = parallel.multiply(M2, A1);
        }
        long timeParallel = System.currentTimeMillis() - start;
        System.out.println("time taken (Parallel): " + timeParallel);

        Matrix M3 = A1;
        start = System.currentTimeMillis();
        for (int i = 0; i < nLoops; ++i) {
            M3 = M3.multiply(A1);
        }
        long timeDenseParallel = System.currentTimeMillis() - start;
        System.out.println("time taken (Dense Parallel): " + timeDenseParallel);

        assertTrue(AreMatrices.equal(M1, M2, 1e-5));
        assertTrue(AreMatrices.equal(M1, M3, 1e-5));

        assertTrue("Dense Parallel should be the fastest",
                timeDenseParallel < timeParallel && timeParallel < timeSimple);
    }
}
