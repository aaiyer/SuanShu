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
package com.numericalmethod.suanshu.matrix.doubles.linearsystem;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BackwardSubstitutionTest {

    /**
     * Test of class BackwardSubstitution.
     */
    @Test
    public void testBackwardSubstitution_0010() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3},
                    {4, 5},
                    {6}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        BackwardSubstitution instance = new BackwardSubstitution();
        Vector x = instance.solve(U, b);
        assertEquals(b, U.multiply(x));
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test
    public void testBackwardSubstitution_0020() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {47.1, 53.23, 65.14159},
                    {22.123, 33.33},
                    {10.5}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        BackwardSubstitution instance = new BackwardSubstitution();
        Vector x = instance.solve(U, b);
        assertEquals(b, U.multiply(x));
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test
    public void testBackwardSubstitution_0030() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {71.1, 5.23, 9.719, 123.02},
                    {417.1, 553.23, 6.519},
                    {224.123, 33.0133},
                    {21310.5}
                });
        Vector b = new DenseVector(new double[]{1.2304, 20.20, 333.30, 456.213});

        BackwardSubstitution instance = new BackwardSubstitution();
        Vector x = instance.solve(U, b);
        assertArrayEquals(b.toArray(), U.multiply(x).toArray(), 1e-13);
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test
    public void testBackwardSubstitution_0040() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {621.1, 52.3, 97.19, 12.02, 56.19},
                    {71.1, 5.23, 9.719, 123.02},
                    {417.1, 553.23, 6.519},
                    {224.123, 33.0133},
                    {21310.5}
                });
        Vector b = new DenseVector(new double[]{20, 1.2304, 20.20, 333.30, 456.213});

        BackwardSubstitution instance = new BackwardSubstitution();
        Vector x = instance.solve(U, b);
        assertArrayEquals(b.toArray(), U.multiply(x).toArray(), 1e-13);
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test
    public void testBackwardSubstitution_0050() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {1}
                });
        Vector b = new DenseVector(new double[]{10});

        BackwardSubstitution instance = new BackwardSubstitution();
        Vector x = instance.solve(U, b);
        assertEquals(b, U.multiply(x));
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testBackwardSubstitution_0060() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {0}
                });
        Vector b = new DenseVector(new double[]{10});

        BackwardSubstitution instance = new BackwardSubstitution();
        instance.solve(U, b);
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testBackwardSubstitution_0070() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {0, 0},
                    {0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        BackwardSubstitution instance = new BackwardSubstitution();
        instance.solve(U, b);
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testBackwardSubstitution_0080() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {10, 20},
                    {0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        BackwardSubstitution instance = new BackwardSubstitution();
        instance.solve(U, b);
    }

    /**
     * Test of class BackwardSubstitution.
     */
    @Test
    public void testBackwardSubstitution_0090() {
        UpperTriangularMatrix U = new UpperTriangularMatrix(new double[][]{
                    {1, 2, 3},
                    {0, 5},
                    {0}
                });
        Vector b = new DenseVector(new double[]{10, 0, 0});

        BackwardSubstitution instance = new BackwardSubstitution();
        Vector x = instance.solve(U, b);
        assertEquals(new DenseVector(new double[]{10, 0, 0}), x);
        assertEquals(b, U.multiply(x));
    }
}
