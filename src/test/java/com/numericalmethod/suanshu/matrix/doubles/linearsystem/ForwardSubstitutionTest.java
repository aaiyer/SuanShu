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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ForwardSubstitutionTest {

    /**
     * Test of class ForwardSubstitution.
     */
    @Test
    public void testForwardSubstitution_0010() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {1},
                    {2, 3},
                    {4, 5, 6}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        ForwardSubstitution instance = new ForwardSubstitution();
        Vector x = instance.solve(L, b);
        assertEquals(b, L.toDense().multiply(x));
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test
    public void testForwardSubstitution_0020() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {10.5},
                    {22.123, 33.33},
                    {47.1, 53.23, 65.14159}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        ForwardSubstitution instance = new ForwardSubstitution();
        Vector x = instance.solve(L, b);
        assertEquals(b, L.multiply(x));
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test
    public void testForwardSubstitution_0030() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02}
                });
        Vector b = new DenseVector(new double[]{1.2304, 20.20, 333.30, 456.213});

        ForwardSubstitution instance = new ForwardSubstitution();
        Vector x = instance.solve(L, b);
        assertArrayEquals(b.toArray(), L.multiply(x).toArray(), 1e-13);
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test
    public void testForwardSubstitution_0040() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {21310.5},
                    {224.123, 33.0133},
                    {417.1, 553.23, 6.519},
                    {71.1, 5.23, 9.719, 123.02},
                    {621.1, 52.3, 97.19, 12.02, 56.19}
                });
        Vector b = new DenseVector(new double[]{20, 1.2304, 20.20, 333.30, 456.213});

        ForwardSubstitution instance = new ForwardSubstitution();
        Vector x = instance.solve(L, b);
        assertEquals(b, L.multiply(x));
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test
    public void testForwardSubstitution_0050() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {1}
                });
        Vector b = new DenseVector(new double[]{10});

        ForwardSubstitution instance = new ForwardSubstitution();
        Vector x = instance.solve(L, b);
        assertEquals(b, L.multiply(x));
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testForwardSubstitution_0060() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {0}
                });
        Vector b = new DenseVector(new double[]{10});

        ForwardSubstitution instance = new ForwardSubstitution();
        instance.solve(L, b);
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testForwardSubstitution_0070() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {0},
                    {0, 0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        ForwardSubstitution instance = new ForwardSubstitution();
        instance.solve(L, b);
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test(expected = LinearSystemSolver.NoSolution.class)
    public void testForwardSubstitution_0080() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {0},
                    {20, 0}
                });
        Vector b = new DenseVector(new double[]{10, 20});

        ForwardSubstitution instance = new ForwardSubstitution();
        instance.solve(L, b);
    }

    /**
     * Test of class ForwardSubstitution.
     */
    @Test
    public void testForwardSubstitution_0090() {

        LowerTriangularMatrix L = new LowerTriangularMatrix(new double[][]{
                    {0},
                    {2, 0},
                    {4, 5, 6}
                });
        Vector b = new DenseVector(new double[]{0, 0, 30});

        ForwardSubstitution instance = new ForwardSubstitution();
        Vector x = instance.solve(L, b);
        assertEquals(new DenseVector(new double[]{0, 0, 5}), x);
        assertEquals(b, L.multiply(x));
    }
}
