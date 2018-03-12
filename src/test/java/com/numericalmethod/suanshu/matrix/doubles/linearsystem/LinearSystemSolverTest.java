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
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LinearSystemSolver.NoSolution;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearSystemSolverTest {

    //<editor-fold defaultstate="collapsed" desc="full rank case">    
    /**
     * det(A) != 0
     */
    @Test
    public void test_Solve_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {2, 3, 8},
                    {4, 5, 6}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LinearSystemSolver instance = new LinearSystemSolver(0);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);

        assertEquals(b, A.multiply(x));
        assertEquals(0, soln.getHomogeneousSoln().size());
    }

    /**
     * det(A) != 0
     */
    @Test
    public void test_Solve_0015() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {2, 3, 8},
                    {4, 5, 6}
                });
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LinearSystemSolver instance = new LinearSystemSolver(SuanShuUtils.autoEpsilon(A));
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);

        assertEquals(b, A.multiply(x));
        assertEquals(0, soln.getHomogeneousSoln().size());
    }

    /**
     * det(A) != 0
     */
    @Test
    public void test_Solve_0020() {
        Matrix A = new DenseMatrix(new double[][]{
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

        LinearSystemSolver instance = new LinearSystemSolver(0);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);

        assertArrayEquals(b.toArray(), A.multiply(x).toArray(), 1e-11);
        assertEquals(0, soln.getHomogeneousSoln().size());
    }

    /**
     * det(A) != 0
     */
    @Test
    public void test_Solve_0025() {
        Matrix A = new DenseMatrix(new double[][]{
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

        LinearSystemSolver instance = new LinearSystemSolver(0);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);

        assertArrayEquals(b.toArray(), A.multiply(x).toArray(), 1e-11);
        assertEquals(0, soln.getHomogeneousSoln().size());
    }

    /**
     * Test of small numbers.
     */
    @Test
    public void test_Solve_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {2, 3, 8},
                    {4, 5, 6}
                });
        A = A.scaled(1e-100);
        Vector b = new DenseVector(new double[]{10, 20, 30});

        LinearSystemSolver instance = new LinearSystemSolver(SuanShuUtils.autoEpsilon(A));
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);

        assertEquals(b, A.multiply(x));
        assertEquals(0, soln.getHomogeneousSoln().size());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="homogeneous solution">    
    @Test
    public void test_Solve_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4, 7},
                    {2, 5, 8},
                    {3, 6, 9}
                });
        Vector b = new DenseVector(new double[]{0, 0, 0});

        LinearSystemSolver instance = new LinearSystemSolver(1e-15);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);

        assertEquals(b, A.multiply(x));
        assertEquals(1, soln.getHomogeneousSoln().size());
    }

    @Test
    public void test_Solve_0045() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4, 7},
                    {2, 5, 8},
                    {3, 6, 9}
                });
        Vector b = new DenseVector(new double[]{0, 0, 0});

        LinearSystemSolver instance = new LinearSystemSolver(SuanShuUtils.autoEpsilon(A));
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);

        assertEquals(x.ZERO(), x);
        assertEquals(b, A.multiply(x));
        assertEquals(1, soln.getHomogeneousSoln().size());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="no solution">    
    @Test(expected = NoSolution.class)
    public void test_Solve_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4, 7},
                    {2, 5, 8},
                    {3, 6, 9}
                });
        Vector b = new DenseVector(new double[]{7, 23, 31});

        LinearSystemSolver instance = new LinearSystemSolver(0);
        Vector x = instance.solve(A).getParticularSolution(b);
    }

    @Test(expected = NoSolution.class)
    public void test_Solve_0055() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 4, 7},
                    {2, 5, 8},
                    {3, 6, 9}
                });
        Vector b = new DenseVector(new double[]{7, 23, 31});

        LinearSystemSolver instance = new LinearSystemSolver(SuanShuUtils.autoEpsilon(A));
        Vector x = instance.solve(A).getParticularSolution(b);
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="under-determined system">    
    @Test
    public void test_Solve_0200() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 2, -1},
                    {1, 0, 1, 1},
                    {-1, 1, 0, -1},
                    {0, 2, 3, -1}
                });
        Vector b = new DenseVector(new double[]{1, 4, 2, 7});

        LinearSystemSolver instance = new LinearSystemSolver(1e-15);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);
        assertEquals(b, A.multiply(x));

        Vector v = new DenseVector(new double[]{-0.7559289460184543, -0.37796447300922753, 0.3779644730092275, 0.37796447300922675});//copied from debugger
        List<Vector> soln0 = soln.getHomogeneousSoln();
        assertTrue(soln0.contains(v));
    }

    @Test
    public void test_Solve_0210() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 2, -1},
                    {1, 0, 1, 1},
                    {-1, 1, 0, -1},
                    {0, 2, 3, -1}
                });
        Vector b = new DenseVector(new double[]{1, 4, 2, 7});

        LinearSystemSolver instance = new LinearSystemSolver(SuanShuUtils.autoEpsilon(A));
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);
        assertEquals(b, A.multiply(x));

        Vector v = new DenseVector(new double[]{-0.7559289460184543, -0.37796447300922753, 0.3779644730092275, 0.37796447300922675});//copied from debugger
        List<Vector> soln0 = soln.getHomogeneousSoln();
        assertTrue(soln0.contains(v));
    }

    @Test
    public void test_Solve_0220() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 3, 5},
                    {-4, 2, 3}
                });
        Vector b = new DenseVector(new double[]{23, 9});

        LinearSystemSolver instance = new LinearSystemSolver(1e-15);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);
        assertEquals(b, A.multiply(x));
    }

    @Test
    public void test_Solve_0230() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 3, 5},
                    {-4, 2, 3}
                });
        Vector b = new DenseVector(new double[]{21.5, 18.5});

        LinearSystemSolver instance = new LinearSystemSolver(1e-15);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);
        assertEquals(b, A.multiply(x));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for more equations than variables">
    @Test
    public void test_overdetermined_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, -1, 0},
                    {0, -2, 0},
                    {0, 0, -1},
                    {0, 0, -2}
                });
        Vector b = new DenseVector(new double[]{-0.8, -1.6, 0.8, 1.6});

        LinearSystemSolver instance = new LinearSystemSolver(1e-15);
        LinearSystemSolver.Solution soln = instance.solve(A);
        Vector x = soln.getParticularSolution(b);
        assertArrayEquals(b.toArray(), A.multiply(x).toArray(), 1e-14);
    }
    //</editor-fold>
}
