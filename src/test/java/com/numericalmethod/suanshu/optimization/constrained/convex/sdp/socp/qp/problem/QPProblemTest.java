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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.problem;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPInfeasible;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.QPSolution;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.activeset.QPPrimalActiveSetSolver;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.QuadraticFunction;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class QPProblemTest {

    /**
     * http://support.sas.com/documentation/cdl/en/ormpug/59679/HTML/default/viewer.htm#qpsolver_sect13.htm
     */
    @Test
    public void test_0010() throws QPInfeasible, Exception {
        Matrix H = new DenseMatrix(new double[][]{
                    {0.08, -0.05, -0.05, -0.05},
                    {-0.05, 0.16, -0.02, -0.02},
                    {-0.05, -0.02, 0.35, 0.06},
                    {-0.05, -0.02, 0.06, 0.35}
                });
        Vector p = new DenseVector(new double[]{0, 0, 0, 0});
        QuadraticFunction f = new QuadraticFunction(H, p);

        Matrix Aless = new DenseMatrix(new double[][]{
                    {1, 1, 1, 1}
                });
        Vector bless = new DenseVector(new double[]{10000});
        LinearLessThanConstraints less = new LinearLessThanConstraints(Aless, bless);

        Matrix Agreater = new DenseMatrix(new double[][]{
                    {0.05, -0.2, 0.15, 0.3},
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
                });
        Vector bgreater = new DenseVector(new double[]{1000, 0, 0, 0, 0});
        LinearGreaterThanConstraints greater = new LinearGreaterThanConstraints(Agreater, bgreater);

        QPProblem problem = new QPProblem(f, greater, less);

        QPPrimalActiveSetSolver instance = new QPPrimalActiveSetSolver(Math.sqrt(SuanShuUtils.autoEpsilon(problem.f().Hessian())), Integer.MAX_VALUE);
        IterativeMinimizer<QPSolution> soln = instance.solve(new QPProblem(problem));
        QPSolution minimizer = soln.search();
        assertArrayEquals(new double[]{3.4529E+03, 7.0389E-04, 1.0688E+03, 2.2235E+03}, minimizer.minimizer().toArray(), 1e-1);
    }
}
