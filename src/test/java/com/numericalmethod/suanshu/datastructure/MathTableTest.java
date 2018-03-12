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

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MathTableTest {

    @Test
    public void test_0010() {
        String[] headers = {"col1", "col2"};
        MathTable instance = new MathTable(headers);
        assertArrayEquals(headers, instance.getHeaders());
    }

    @Test
    public void test_0020() {
        MathTable instance = new MathTable(3);
        assertArrayEquals(new String[]{"Column1", "Column2", "Column3"}, instance.getHeaders());
    }

    @Test(expected = RuntimeException.class)
    public void test_0030() {
        String[] headers = {"col1", "col1"};
        MathTable instance = new MathTable(headers);
    }

    @Test
    public void test_0100() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        double[] keys = instance.getIndices();
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0}, keys, 0.0);

        MathTable.Row row1 = instance.getRowOnOrBefore(1.0);
        assertArrayEquals(data1, row1.toArray(), 0.0);

        MathTable.Row row2 = instance.getRowOnOrBefore(2.0);
        assertArrayEquals(data2, row2.toArray(), 0.0);

        MathTable.Row row3 = instance.getRowOnOrBefore(3.0);
        assertArrayEquals(data3, row3.toArray(), 0.0);

        MathTable.Row row4 = instance.getRowOnOrBefore(4.0);
        assertArrayEquals(data4, row4.toArray(), 0.0);
    }

    @Test
    public void test_0110() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        double[] keys = instance.getIndices();
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0}, keys, 0.0);

        MathTable.Row row1 = instance.getRowOnOrBefore(1.5);
        assertArrayEquals(data1, row1.toArray(), 0.0);

        MathTable.Row row2 = instance.getRowOnOrBefore(2.5);
        assertArrayEquals(data2, row2.toArray(), 0.0);

        MathTable.Row row3 = instance.getRowOnOrBefore(3.5);
        assertArrayEquals(data3, row3.toArray(), 0.0);

        MathTable.Row row4 = instance.getRowOnOrBefore(10.5);
        assertArrayEquals(data4, row4.toArray(), 0.0);
    }

    @Test(expected = NoSuchElementException.class)
    public void test_0120() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        MathTable.Row beforeBeginning = instance.getRowOnOrBefore(0.5);
    }

    @Test(expected = NoSuchElementException.class)
    public void test_0130() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        MathTable.Row afterEnding = instance.getRowOnOrAfter(10.5);
    }

    @Test
    public void test_0200() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        Iterator<MathTable.Row> it = instance.getRowsOnOrBefore(3.5);
        assertArrayEquals(data3, it.next().toArray(), 0.0);
        assertArrayEquals(data2, it.next().toArray(), 0.0);
        assertArrayEquals(data1, it.next().toArray(), 0.0);
    }

    @Test
    public void test_0210() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        Iterator<MathTable.Row> it = instance.getRowsOnOrBefore(3.0);
        assertArrayEquals(data3, it.next().toArray(), 0.0);
        assertArrayEquals(data2, it.next().toArray(), 0.0);
        assertArrayEquals(data1, it.next().toArray(), 0.0);
    }

    @Test
    public void test_0220() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        Iterator<MathTable.Row> it = instance.getRowsOnOrBefore(0.5);
        assertFalse(it.hasNext());
    }

    @Test
    public void test_0300() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        Iterator<MathTable.Row> it = instance.getRowsOnOrAfter(1.5);
        assertArrayEquals(data2, it.next().toArray(), 0.0);
        assertArrayEquals(data3, it.next().toArray(), 0.0);
        assertArrayEquals(data4, it.next().toArray(), 0.0);
    }

    @Test
    public void test_0310() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        Iterator<MathTable.Row> it = instance.getRowsOnOrAfter(2);
        assertArrayEquals(data2, it.next().toArray(), 0.0);
        assertArrayEquals(data3, it.next().toArray(), 0.0);
        assertArrayEquals(data4, it.next().toArray(), 0.0);
    }

    @Test
    public void test_0320() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        Iterator<MathTable.Row> it = instance.getRowsOnOrAfter(10);
        assertFalse(it.hasNext());
    }

    @Test
    public void test_0400() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        assertEquals(1.1, instance.get(1.0, 1), 0);
        assertEquals(2.2, instance.get(2.0, 2), 0);
        assertEquals(3.3, instance.get(3.0, 3), 0);
        assertEquals(4.3, instance.get(4.0, 3), 0);
    }

    @Test
    public void test_0410() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        assertEquals(1.6, instance.get(1.5, 1), 0);
        assertEquals(2.8, instance.get(2.7, 1), 1e-15);
        assertEquals(4.1, instance.get(4.0, 1), 1e-15);
    }

    @Test(expected = NoSuchElementException.class)
    public void test_0420() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        instance.get(0.5, 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void test_0430() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[] data1 = new double[]{1.1, 1.2, 1.3};
        instance.addRow(1.0, data1);
        double[] data2 = new double[]{2.1, 2.2, 2.3};
        instance.addRow(2.0, data2);
        double[] data3 = new double[]{3.1, 3.2, 3.3};
        instance.addRow(3.0, data3);
        double[] data4 = new double[]{4.1, 4.2, 4.3};
        instance.addRow(4.0, data4);

        instance.get(4.5, 1);
    }

    @Test
    public void test_0440() {
        MathTable instance = new MathTable(10);
        instance.addRows(new double[][]{
                    {25.0, -1.6085553059029958, -1.2041684359772609, -0.9358086240599484, -0.6963731262176656, -0.4552043238647444, -0.18702905759763477, 0.09212619882018075, 0.4397780182938855, 0.946221175326937, 5.833554220692953},
                    {50.0, -1.6067989033419026, -1.2184514083774547, -0.9418808157932785, -0.7033993148305102, -0.46515901772142054, -0.20576270974381217, 0.10113778390170848, 0.4409138852158214, 0.9387746974091211, 3.9658158665177545},
                    {75.0, -1.6205414744019282, -1.2270357084731836, -0.9555181981477564, -0.7067822324570432, -0.49417146686996183, -0.2242615705771781, 0.051680616371951475, 0.4030619861547302, 0.8965199952887083, 3.840721830140284},
                    {100.0, -1.6060899560102633, -1.2229001952750271, -0.967333152924523, -0.7357581746377152, -0.4880625765069918, -0.21841040384307425, 0.07783299298164598, 0.4292467439773328, 0.927704383387695, 4.259722703402378}
                });

        assertEquals(-1.6, instance.get(25, 1), 1e-1);
    }

    @Test
    public void test_0500() {
        String[] headers = {"col1", "col2", "col3"};
        MathTable instance = new MathTable(headers);

        double[][] data = new double[][]{
            {1.0, 1.1, 1.2, 1.3},
            {2.0, 2.1, 2.2, 2.3},
            {3.0, 3.1, 3.2, 3.3},
            {4.0, 4.1, 4.2, 4.3}
        };

        instance.addRows(data);

        double[] keys = instance.getIndices();
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0}, keys, 0.0);

        MathTable.Row row1 = instance.getRowOnOrBefore(1.0);
        assertArrayEquals(new double[]{1.1, 1.2, 1.3}, row1.toArray(), 0.0);

        MathTable.Row row2 = instance.getRowOnOrBefore(2.0);
        assertArrayEquals(new double[]{2.1, 2.2, 2.3}, row2.toArray(), 0.0);

        MathTable.Row row3 = instance.getRowOnOrBefore(3.0);
        assertArrayEquals(new double[]{3.1, 3.2, 3.3}, row3.toArray(), 0.0);

        MathTable.Row row4 = instance.getRowOnOrBefore(4.0);
        assertArrayEquals(new double[]{4.1, 4.2, 4.3}, row4.toArray(), 0.0);
    }
}
