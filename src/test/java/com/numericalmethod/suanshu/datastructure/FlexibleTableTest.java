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
package com.numericalmethod.suanshu.datastructure;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class FlexibleTableTest {

    public FlexibleTableTest() {
    }

    @Test
    public void test_0010() {
        FlexibleTable instance = new FlexibleTable(
                new String[]{"row1", "row2"},
                new String[]{"col1", "col2", "col3"});

        assertEquals(1, instance.getIndexFromRowLabel("row1"));
        assertEquals(2, instance.getIndexFromRowLabel("row2"));

        assertEquals(1, instance.getIndexFromColLabel("col1"));
        assertEquals(2, instance.getIndexFromColLabel("col2"));
        assertEquals(3, instance.getIndexFromColLabel("col3"));

        assertTrue(AreMatrices.equal(new DenseMatrix(2, 3), instance.toMatrix(), 0));
    }

    @Test
    public void test_0020() {
        FlexibleTable instance = new FlexibleTable(
                new String[]{"row1", "row2"},
                new String[]{"col1", "col2", "col3"},
                new double[][]{
                    {1, 2, 3},
                    {4, 5, 6}
                });

        assertEquals(1.0, instance.get(1, 1), 0);
        assertEquals(2.0, instance.get(1, 2), 0);
        assertEquals(3.0, instance.get(1, 3), 0);
        assertEquals(4.0, instance.get(2, 1), 0);
        assertEquals(5.0, instance.get(2, 2), 0);
        assertEquals(6.0, instance.get(2, 3), 0);
    }

    @Test
    public void test_0030() {
        FlexibleTable instance = new FlexibleTable(
                new String[]{"row1", "row2"},
                new String[]{"col1", "col2", "col3"},
                new double[][]{
                    {1, 2, 3},
                    {4, 5, 6}
                });

        instance.addRowAt(3, "row3");

        assertEquals(3, instance.getIndexFromRowLabel("row3"));

        assertEquals(1.0, instance.get(1, 1), 0);
        assertEquals(2.0, instance.get(1, 2), 0);
        assertEquals(3.0, instance.get(1, 3), 0);
        assertEquals(4.0, instance.get(2, 1), 0);
        assertEquals(5.0, instance.get(2, 2), 0);
        assertEquals(6.0, instance.get(2, 3), 0);
        assertEquals(0.0, instance.get(3, 1), 0);
        assertEquals(0.0, instance.get(3, 2), 0);
        assertEquals(0.0, instance.get(3, 3), 0);

        instance.deleteCol(2);

        assertEquals(1, instance.getIndexFromRowLabel("row1"));
        assertEquals(2, instance.getIndexFromRowLabel("row2"));
        assertEquals(3, instance.getIndexFromRowLabel("row3"));

        assertEquals(1, instance.getIndexFromColLabel("col1"));
        assertEquals(2, instance.getIndexFromColLabel("col3"));

        assertEquals(1.0, instance.get(1, 1), 0);
        assertEquals(3.0, instance.get(1, 2), 0);
        assertEquals(4.0, instance.get(2, 1), 0);
        assertEquals(6.0, instance.get(2, 2), 0);
        assertEquals(0.0, instance.get(3, 1), 0);
        assertEquals(0.0, instance.get(3, 2), 0);

        instance.deleteRow(2);

        assertEquals(1, instance.getIndexFromRowLabel("row1"));
        assertEquals(2, instance.getIndexFromRowLabel("row3"));

        assertEquals(1, instance.getIndexFromColLabel("col1"));
        assertEquals(2, instance.getIndexFromColLabel("col3"));

        assertEquals(1.0, instance.get(1, 1), 0);
        assertEquals(3.0, instance.get(1, 2), 0);
        assertEquals(0.0, instance.get(2, 1), 0);
        assertEquals(0.0, instance.get(2, 2), 0);

        instance.addColAt(3);

        assertEquals(1, instance.getIndexFromColLabel("col1"));
        assertEquals(2, instance.getIndexFromColLabel("col3"));
        assertEquals(3, instance.getIndexFromColLabel("col#1"));

        assertEquals(1.0, instance.get(1, 1), 0);
        assertEquals(3.0, instance.get(1, 2), 0);
        assertEquals(0.0, instance.get(1, 3), 0);
        assertEquals(0.0, instance.get(2, 1), 0);
        assertEquals(0.0, instance.get(2, 2), 0);
        assertEquals(0.0, instance.get(2, 3), 0);

        instance.deleteRow(1);
        instance.deleteCol(1);
        instance.deleteCol(2);

        assertEquals(0.0, instance.get(instance.getIndexFromRowLabel("row3"), instance.getIndexFromColLabel("col3")), 0);
    }

    @Test
    public void test_0040() {
        FlexibleTable instance = new FlexibleTable(
                new String[]{"row1", "row2"},
                new String[]{"col1", "col2", "col3"});

        instance.renameRow(1, "row11");
        instance.renameCol(2, "col22");

        assertEquals(1, instance.getIndexFromRowLabel("row11"));
        assertEquals(2, instance.getIndexFromRowLabel("row2"));

        assertEquals(1, instance.getIndexFromColLabel("col1"));
        assertEquals(2, instance.getIndexFromColLabel("col22"));
        assertEquals(3, instance.getIndexFromColLabel("col3"));

        assertTrue(AreMatrices.equal(new DenseMatrix(2, 3), instance.toMatrix(), 0));
    }

    @Test
    public void test_0050() {
        FlexibleTable instance1 = new FlexibleTable(
                new String[]{"row1"},
                new String[]{"col1"});

        instance1.set(1, 1, 99);

        FlexibleTable instance2 = new FlexibleTable(
                new String[]{"row1"},
                new String[]{"col1"},
                new double[][]{{99}});

        assertEquals(instance2, instance1);
    }
}
