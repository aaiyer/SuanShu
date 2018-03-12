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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex;

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblemImpl1;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class FerrisMangasarianWrightPhase1Test {

    /**
     * Example 3-4-1.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0010() throws LPInfeasible, Exception {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(4.0, 5.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {1.0, 1.0},
                    {1.0, 2.0},
                    {4.0, 2.0},
                    {-1.0, -1.0},
                    {-1.0, 1.0}
                }),
                new DenseVector(-1.0, 1.0, 8.0, -3.0, 1.0)),
                null,
                null,
                null);

        SimplexTable table0 = new SimplexTable(problem);

        FerrisMangasarianWrightPhase1 phase1 = new FerrisMangasarianWrightPhase1(table0);
        SimplexTable table1 = phase1.process();

        assertEquals(14., table1.minimum(), 0.);
        assertArrayEquals(new double[]{1., 2.}, table1.minimizer().toArray(), 1e-15);
    }

    /**
     * Exercise 3-4-2. (1).
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test(expected = LPInfeasible.class)
    public void test_0020() throws LPInfeasible, Exception {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(-3.0, 1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {-1.0, -1.0},
                    {2.0, 2.0}
                }),
                new DenseVector(-2.0, 10.0)),
                null,
                null,
                null);

        SimplexTable table0 = new SimplexTable(problem);

        FerrisMangasarianWrightPhase1 phase1 = new FerrisMangasarianWrightPhase1(table0);
        SimplexTable table1 = phase1.process();
    }
}
