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

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LUSolverTest {

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0010() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(L.toDense(), b));
        assertEquals(b, L.toDense().multiply(x));
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0020() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {10.5},
                    {22.123, 33.33},
                    {47.1, 53.23, 65.14159}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(L.toDense(), b));
        assertArrayEquals(b.toArray(), L.toDense().multiply(x).toArray(), 1e-13);
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0030() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02}
                });
        Vector b = new DenseVector(new double[]{1.2304, 20.20, 333.30, 456.213});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(L.toDense(), b));
        assertEquals(b, L.toDense().multiply(x));
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0040() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02},
                    {621.1, 52.3, 97.19, 12.02, 56.19}
                });
        Vector b = new DenseVector(new double[]{20, 1.2304, 20.20, 333.30, 456.213});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(L.toDense(), b));
        assertArrayEquals(b.toArray(), L.toDense().multiply(x).toArray(), 1e-13);
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0050() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {1}
                });
        Vector b = new DenseVector(new double[]{10});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(L.toDense(), b));
        assertEquals(b, L.toDense().multiply(x));
    }

    /**
     * Test of class LUSolver.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testLU_0060() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {0}
                });
        Vector b = new DenseVector(new double[]{10});

        LUSolver instance = new LUSolver();
        instance.solve(new LSProblem(L.toDense(), b));
    }

    /**
     * Test of class LUSolver.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testLU_0070() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {0},
                    {0, 0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        LUSolver instance = new LUSolver();
        instance.solve(new LSProblem(L.toDense(), b));
    }

    /**
     * Test of class LUSolver.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testLU_0080() {
        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {0},
                    {20, 0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        LUSolver instance = new LUSolver();
        instance.solve(new LSProblem(L.toDense(), b));
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0090() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5},
                    {6}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(U.toDense(), b));
        assertEquals(b, U.toDense().multiply(x));
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0100() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {47.1, 53.23, 65.14159},
                    {22.123, 33.33},
                    {10.5}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(U.toDense(), b));
        assertEquals(b, U.toDense().multiply(x));
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0110() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {71.1, 5.23, 9.719, 123.02},
                    {417.1, 553.23, 6.519},
                    {224.123, 33.0133},
                    {21310.5}
                });
        Vector b = new DenseVector(new double[]{1.2304, 20.20, 333.30, 456.213});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(U.toDense(), b));
        assertArrayEquals(b.toArray(), U.toDense().multiply(x).toArray(), 1e-13);
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0120() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {621.1, 52.3, 97.19, 12.02, 56.19},
                    {71.1, 5.23, 9.719, 123.02},
                    {417.1, 553.23, 6.519},
                    {224.123, 33.0133},
                    {21310.5}
                });
        Vector b = new DenseVector(new double[]{20, 1.2304, 20.20, 333.30, 456.213});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(U.toDense(), b));
        assertArrayEquals(b.toArray(), U.toDense().multiply(x).toArray(), 1e-13);
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0130() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {1}
                });
        Vector b = new DenseVector(new double[]{10});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(U.toDense(), b));
        assertEquals(b, U.toDense().multiply(x));
    }

    /**
     * Test of class LUSolver.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testLU_0140() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {0}
                });
        Vector b = new DenseVector(new double[]{10});

        LUSolver instance = new LUSolver();
        instance.solve(new LSProblem(U.toDense(), b));
    }

    /**
     * Test of class LUSolver.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testLU_0150() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {0, 0},
                    {0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        LUSolver instance = new LUSolver();
        instance.solve(new LSProblem(U.toDense(), b));
    }

    /**
     * Test of class LUSolver.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testLU_0160() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {10, 20},
                    {0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        LUSolver instance = new LUSolver();
        instance.solve(new LSProblem(U.toDense(), b));
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0170() {
        Matrix M = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {2, 3, 8},
                    {4, 5, 6}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(M, b));
        assertEquals(b, M.multiply(x));
    }

    /**
     * Test of class LUSolver.
     */
    @Test
    public void testLU_0180() {
        Matrix M = new DenseMatrix(new double[][]{
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {11, 25, 34, 41, 55, 66, 74, 28, 19},
                    {-61, 12, -33, 4.4, 5.5, 78.6, 17.7, 81.6, 19},
                    {-0.31, -12, -3, -34, 35, 66, 17, 8.21, 91.3},
                    {0.51, 20.69, 33.6, 45.1, 95, 36, 7, 8, 9},
                    {-6.11, -32.3, -93, -614, 55, 26, 37, 98, 19},
                    {100, 20.6, -93.36, 46.9, 55.2, 66.4, 63.7, 88, 39},
                    {101, 222, 33.6, 40.9, 50.6, 56.3, 67, 58, 92},
                    {102.3, 236, 31.9, 413.3, 51, 62.9, 715.5, 818.8, 99.9}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30, 40, 50, 60, 70, 80, 99});

        LUSolver instance = new LUSolver();
        Vector x = instance.solve(new LSProblem(M, b));
        assertArrayEquals(b.toArray(), M.multiply(x).toArray(), 1e-11);
    }
}
