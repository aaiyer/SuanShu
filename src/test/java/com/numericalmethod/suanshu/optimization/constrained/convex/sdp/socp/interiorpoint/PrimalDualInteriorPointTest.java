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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.interiorpoint;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.problem.SOCPGeneralProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class PrimalDualInteriorPointTest {

    @Test
    public void test_0010() throws Exception {
        Vector f = new DenseVector(1., 0., 0., 0., 0.);

        Matrix A1t = new DenseMatrix(new double[][]{
                    {0, -1, 0, 1, 0},
                    {0, 0, 1, 0, -1}
                });
        Matrix A2t = new DenseMatrix(new double[][]{
                    {0, 0.5, 0, 0, 0},
                    {0, 0, 1, 0, 0}
                });
        Matrix A3t = new DenseMatrix(new double[][]{
                    {0, 0, 0, -0.7071, -0.7071},
                    {0, 0, 0, -0.3536, 0.3536}
                });

        Vector b1 = f;
        Vector b2 = f.ZERO();
        Vector b3 = f.ZERO();

        Vector c1 = new DenseVector(2);//zero
        Vector c2 = new DenseVector(-0.5, 0.);
        Vector c3 = new DenseVector(4.2426, -0.7071);

        double[] d = new double[]{0., 1, 1};

        SOCPGeneralProblem problem = new SOCPGeneralProblem(
                f,
                new Matrix[]{A1t.t(), A2t.t(), A3t.t()},
                new Vector[]{c1, c2, c3},
                new Vector[]{b1, b2, b3},
                d);

        Vector x0 = new DenseVector(1, 0, 0, 0.1, 0, 0, 0.1, 0, 0);
        Vector s0 = new DenseVector(3.7, 1, -3.5, 1, 0.25, 0.5, 1, -0.35355, -0.1767);
        Vector y0 = new DenseVector(-3.7, -1.5, -0.5, -2.5, -4);
        PrimalDualSolution soln0 = new PrimalDualSolution(x0, s0, y0);

        PrimalDualInteriorPoint socp = new PrimalDualInteriorPoint(0.00001, 20);
        IterativeMinimizer<PrimalDualSolution> soln = socp.solve(problem);
        soln.search(soln0);

        assertArrayEquals(new double[]{-1.707791, -2.044705, -0.852730, -2.544839, -2.485646}, soln.minimizer().y.toArray(), 1e-6);
    }

    /**
     * Using the initials as in SDPT3.
     *
     * @throws Exception
     */
    @Test
    public void test_0020() throws Exception {
        Vector f = new DenseVector(1., 0., 0., 0., 0.);

        Matrix A1t = new DenseMatrix(new double[][]{
                    {0, -1, 0, 1, 0},
                    {0, 0, 1, 0, -1}
                });
        Matrix A2t = new DenseMatrix(new double[][]{
                    {0, 0.5, 0, 0, 0},
                    {0, 0, 1, 0, 0}
                });
        Matrix A3t = new DenseMatrix(new double[][]{
                    {0, 0, 0, -0.7071, -0.7071},
                    {0, 0, 0, -0.3536, 0.3536}
                });

        Vector b1 = f;
        Vector b2 = f.ZERO();
        Vector b3 = f.ZERO();

        Vector c1 = new DenseVector(2);//zero
        Vector c2 = new DenseVector(-0.5, 0.);
        Vector c3 = new DenseVector(4.2426, -0.7071);

        double[] d = new double[]{0., 1, 1};

        SOCPGeneralProblem problem = new SOCPGeneralProblem(
                f,
                new Matrix[]{A1t.t(), A2t.t(), A3t.t()},
                new Vector[]{c1, c2, c3},
                new Vector[]{b1, b2, b3},
                d);

        PrimalDualInteriorPoint socp = new PrimalDualInteriorPoint(0.00001, 20);
        IterativeMinimizer<PrimalDualSolution> soln = socp.solve(problem);
        soln.search();

        assertArrayEquals(new double[]{-1.707786, -2.044708, -0.852731, -2.544840, -2.485640}, soln.minimizer().y.toArray(), 1e-6);
    }
}
