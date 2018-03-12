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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp;

import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class QPSimpleSolverTest {

    /**
     * example 13.1
     * "Andreas Antoniou, Wu-Sheng Lu. "Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
     */
    @Test
    public void test_0010() throws Exception {
        QuadraticFunction f = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {0.75, 0.25},
                    {0.25, 0.75}
                }),
                new DenseVector(0.1642, 2.6642));

        QPSolution solution = QPSimpleSolver.solve(f);
        assertArrayEquals(new double[]{1.0858, -3.9142}, solution.minimizer().toArray(), 1e-4);
        assertTrue(solution.isUnique());
    }

    /**
     * example 13.1
     * "Andreas Antoniou, Wu-Sheng Lu. "Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
     */
    @Test(expected = QPInfeasible.class)
    public void test_0020() throws Exception {
        QuadraticFunction f = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {1, 0},
                    {0, 0}
                }),
                new DenseVector(1, -1));

        QPSolution solution = QPSimpleSolver.solve(f);
    }

    /**
     * example 13.1
     * "Andreas Antoniou, Wu-Sheng Lu. "Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
     */
    @Test
    public void test_0110() throws Exception {
        QuadraticFunction f = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
                }),
                new DenseVector(2., 1., -1.));

        LinearEqualityConstraints equal = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {0, 1, 1}
                }),
                new DenseVector(new double[]{1}));

        QPSolution solution = QPSimpleSolver.solve(f, equal);
        assertArrayEquals(new double[]{-2, -2, 3}, solution.minimizer().toArray(), 1e-15);
        assertTrue(solution.isUnique());
    }

    /**
     * example 13.1
     * "Andreas Antoniou, Wu-Sheng Lu. "Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
     */
    @Test(expected = QPInfeasible.class)
    public void test_0120() throws Exception {
        QuadraticFunction f = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
                }),
                new DenseVector(2., 1., -1.));

        LinearEqualityConstraints equal = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {1, 0, 0}
                }),
                new DenseVector(new double[]{1}));

        QPSolution solution = QPSimpleSolver.solve(f, equal);
        assertArrayEquals(new double[]{-2, -2, 3}, solution.minimizer().toArray(), 1e-15);
        assertTrue(solution.isUnique());
    }

    /**
     * example 13.1
     * "Andreas Antoniou, Wu-Sheng Lu. "Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
     */
    @Test
    public void test_0130() throws Exception {
        QuadraticFunction f = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
                }),
                new DenseVector(2., 1., 0.));

        LinearEqualityConstraints equal = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {1, 0, 0}
                }),
                new DenseVector(new double[]{1}));

        QPSolution solution = QPSimpleSolver.solve(f, equal);
        assertArrayEquals(new double[]{1, -1, 0}, solution.minimizer().toArray(), 1e-15);// the 3rd entry can be anything
        assertFalse(solution.isUnique());
    }

    /**
     * example 16.2
     * "Andreas Antoniou, Wu-Sheng Lu. "Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
     */
    @Test
    public void test_0140() throws Exception {
        QuadraticFunction f = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {6, 2, 1},
                    {2, 5, 2},
                    {1, 2, 4}
                }),
                new DenseVector(-8., -3., -3.));

        LinearEqualityConstraints equal = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {1, 0, 1},
                    {0, 1, 1}
                }),
                new DenseVector(new double[]{3., 0.}));

        QPSolution solution = QPSimpleSolver.solve(f, equal);
        assertArrayEquals(new double[]{2, -1, 1}, solution.minimizer().toArray(), 1e-15);
        assertTrue(solution.isUnique());
    }

    /**
     * require 0 epsilon
     * 
     * @throws Exception 
     */
    @Test
    public void test_0200() throws Exception {
        QuadraticFunction f = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {1.611188746256185E-7, 7.623277601720256E-5, -7.572772914313463E-5},
                    {7.623277601720256E-5, 0.09308214716323629, -0.09270618047937493},
                    {-7.572772914313463E-5, -0.09270618047937491, 0.0923321261604602}
                }),
                new DenseVector(1.1328010873819337E-5, 1.6727948615508445E-5, 6.075374929193815E-6));

        LinearEqualityConstraints equal = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {1, 1, 1}
                }),
                new DenseVector(new double[]{0.}));

        QPSolution solution = QPSimpleSolver.solve(f, equal, 0);
//        System.out.println(solution.minimizer());
        assertArrayEquals(new double[]{28296355.314822, -14131157.569445, -14165197.745377}, solution.minimizer().toArray(), 1e-6);// from debugger
        assertTrue(solution.isUnique());
    }
}
