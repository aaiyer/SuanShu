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

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.datastructure.FlexibleTable;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable.Label;
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
public class SimplexTableTest {

    /**
     * Example 3-6-5.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_0010() {
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

        SimplexTable instance = new SimplexTable(problem);

        assertEquals(4, instance.nCols());
        assertEquals(4, instance.nRows());

        assertEquals(SimplexTable.LabelType.NON_BASIC, instance.getColLabel(1).type);
        assertEquals(SimplexTable.LabelType.NON_BASIC, instance.getColLabel(2).type);
        assertEquals(SimplexTable.LabelType.FREE, instance.getColLabel(3).type);
        assertEquals(SimplexTable.LabelType.B, instance.getColLabel(4).type);

        assertEquals(SimplexTable.LabelType.BASIC, instance.getRowLabel(1).type);
        assertEquals(SimplexTable.LabelType.BASIC, instance.getRowLabel(2).type);
        assertEquals(SimplexTable.LabelType.EQUALITY, instance.getRowLabel(3).type);
        assertEquals(SimplexTable.LabelType.COST, instance.getRowLabel(4).type);

        assertEquals(1, instance.get(1, 1), 0);
        assertEquals(-1, instance.get(1, 2), 0);
        assertEquals(4, instance.get(1, 3), 0);
        assertEquals(1, instance.get(1, 4), 0);

        assertEquals(1, instance.get(2, 1), 0);
        assertEquals(-1, instance.get(2, 2), 0);
        assertEquals(-1, instance.get(2, 3), 0);
        assertEquals(-2, instance.get(2, 4), 0);

        assertEquals(1, instance.get(3, 1), 0);
        assertEquals(3, instance.get(3, 2), 0);
        assertEquals(2, instance.get(3, 3), 0);
        assertEquals(-3, instance.get(3, 4), 0);

        assertEquals(2, instance.get(4, 1), 0);
        assertEquals(-1, instance.get(4, 2), 0);
        assertEquals(1, instance.get(4, 3), 0);
        assertEquals(0, instance.get(4, 4), 0);
    }

    /**
     * test swap
     */
    @Test
    public void test_0020() {
        FlexibleTable table = new FlexibleTable(
                new Label[]{SimplexTable.COST, SimplexTable.ARTIFICIAL_COST},
                new Label[]{SimplexTable.B, SimplexTable.ARTIFICIAL},
                new double[][]{
                    {2, 1},
                    {3, 1}
                });

        SimplexTable table0 = new SimplexTable(table, Constant.EPSILON);
        SimplexTable table1 = table0.swap(1, 1);

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {0.5, -0.5},
                    {1.5, -0.5}
                }),
                table1.toMatrix(), 0));
        assertEquals(table1.getRowLabel(1), SimplexTable.B);
        assertEquals(table1.getRowLabel(2), SimplexTable.ARTIFICIAL_COST);
        assertEquals(table1.getColLabel(1), SimplexTable.COST);
        assertEquals(table1.getColLabel(2), SimplexTable.ARTIFICIAL);
    }
}
