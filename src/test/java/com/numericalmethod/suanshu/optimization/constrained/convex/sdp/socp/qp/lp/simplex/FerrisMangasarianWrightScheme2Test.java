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

import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPUnbounded;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.exception.LPInfeasible;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.problem.LPProblemImpl1;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.BoxConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearGreaterThanConstraints;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class FerrisMangasarianWrightScheme2Test {

    /**
     * Example 3-6-5.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0010() throws LPInfeasible, LPUnbounded {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(2.0, -1.0, 1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {1.0, -1.0, 4.0},
                    {1.0, -1.0, -1.0}
                }),
                new DenseVector(-1.0, 2.0)),
                null,
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{
                    {1.0, 3.0, 2.0}
                }),
                new DenseVector(3.0)),
                new BoxConstraints(3, new BoxConstraints.Bound(3, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));//x3 is free

        SimplexTable table0 = new SimplexTable(problem);

        FerrisMangasarianWrightScheme2 instance = new FerrisMangasarianWrightScheme2(table0);
        SimplexTable table1 = instance.process();

        assertEquals(-1, table1.get(1, 1), 0);
        assertEquals(-7, table1.get(1, 2), 0);
        assertEquals(7, table1.get(1, 4), 0);

        assertEquals(1.5, table1.get(2, 1), 0);
        assertEquals(0.5, table1.get(2, 2), 0);
        assertEquals(-3.5, table1.get(2, 4), 0);

        assertEquals(1.5, table1.get(4, 1), 0);
        assertEquals(-2.5, table1.get(4, 2), 0);
        assertEquals(1.5, table1.get(4, 4), 0);
    }

    /**
     * Example 3-6-13 (a).
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     * 
     * The number of equality constraints must be less than the number of variables.
     */
//    @Test(expected = LPInfeasible.class)
    public void test_0020() throws LPInfeasible, LPUnbounded {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(2.0, -1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {1.0, -2.0}
                }),
                new DenseVector(-2.0)),
                null,
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{
                    {1.0, 0.0},
                    {2.0, 0.0}
                }),
                new DenseVector(4.0, 6.0)),//inconsistent equality constraints
                new BoxConstraints(2, new BoxConstraints.Bound(2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));//x2 is free

        SimplexTable table0 = new SimplexTable(problem);

        FerrisMangasarianWrightScheme2 instance = new FerrisMangasarianWrightScheme2(table0);
        SimplexTable table1 = instance.process();
    }

    /**
     * Example 3-6-13 (b).
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     * 
     * The number of equality constraints must be less than the number of variables.
     */
//    @Test
    public void test_0030() throws LPInfeasible, LPUnbounded {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(2.0, -1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {1.0, -2.0}
                }),
                new DenseVector(-2.0)),
                null,
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{
                    {1.0, 0.0},
                    {2.0, 0.0}
                }),
                new DenseVector(4.0, 8.0)),//made consistent
                new BoxConstraints(2, new BoxConstraints.Bound(2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));//x2 is free

        SimplexTable table0 = new SimplexTable(problem);

        FerrisMangasarianWrightScheme2 instance = new FerrisMangasarianWrightScheme2(table0);
        SimplexTable table1 = instance.process();
    }

    /**
     * pp.84.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test(expected = LPUnbounded.class)
    public void test_0040() throws LPInfeasible, LPUnbounded {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(2.0, -1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {1.0, 0.0}
                }),
                new DenseVector(-6.0)),
                null,
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{
                    {-1.0, 0.0}
                }),
                new DenseVector(-4.0)),
                new BoxConstraints(2, new BoxConstraints.Bound(2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));//x2 is free

        SimplexTable table0 = new SimplexTable(problem);

        FerrisMangasarianWrightScheme2 instance = new FerrisMangasarianWrightScheme2(table0);
        SimplexTable table1 = instance.process();
    }

    /**
     * pp.85.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     *
     * Check if this test case throw no exception. It should throw nothing.
     * We cannot determine whether the problem is unbound only after Scheme 2.
     * We need also Phase 1 to determine the unboundness.
     */
    @Test
    public void test_0050() throws LPInfeasible, LPUnbounded {
        LPProblemImpl1 problem = new LPProblemImpl1(
                new DenseVector(2.0, -1.0),
                new LinearGreaterThanConstraints(new DenseMatrix(new double[][]{
                    {1.0, 0.0}
                }),
                new DenseVector(6.0)),
                null,
                new LinearEqualityConstraints(new DenseMatrix(new double[][]{
                    {-1.0, 0.0}
                }),
                new DenseVector(-4.0)),
                new BoxConstraints(2, new BoxConstraints.Bound(2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));//x2 is free

        SimplexTable table0 = new SimplexTable(problem);

        FerrisMangasarianWrightScheme2 instance = new FerrisMangasarianWrightScheme2(table0);
        SimplexTable table1 = instance.process();
    }
}
