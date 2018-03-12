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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaRegularizedP;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GammaRegularizedPTest {

    /**
     * Test of class GammaRegularizedP.
     * Special cases/
     */
    @Test
    public void testEvaluate_0010() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(1, instance.evaluate(0, 0), 1e-15);
        assertEquals(1, instance.evaluate(0, 999), 1e-15);
        assertEquals(0, instance.evaluate(999, 0), 1e-15);
    }

    /**
     * Test of class GammaRegularizedP.
     * Invalid s.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluate_0020() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0, instance.evaluate(-1, 999), 1e-15);
    }

    /**
     * Test of class GammaRegularizedP.
     * Invalid z.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluate_0030() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0, instance.evaluate(999, -1), 1e-15);
    }

    /**
     * Test of class GammaRegularizedP.
     */
    @Test
    public void testEvaluate_0040() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0.63212055882856, instance.evaluate(1, 1), 1e-14);//from Matlab
        assertEquals(0.60837482372891, instance.evaluate(1.5, 1.5), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(2.987654321, 999.99), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(5, 99), 1e-14);//from Matlab
        assertEquals(0.94503635850489, instance.evaluate(5, 9), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedP.
     * P(100, x) = ...
     */
    @Test
    public void testEvaluate_0050() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0, instance.evaluate(100, 0), 1e-14);//from Matlab
        assertEquals(0, instance.evaluate(100, 10), 1e-14);//from Matlab
        assertEquals(0, instance.evaluate(100, 20), 1e-14);//from Matlab
        assertEquals(0, instance.evaluate(100, 30), 1e-14);//from Matlab
        assertEquals(0, instance.evaluate(100, 40), 1e-14);//from Matlab
        assertEquals(3.200065324585199e-010, instance.evaluate(100, 50), 1e-16);//from Matlab
        assertEquals(1.481527632646008e-006, instance.evaluate(100, 60), 1e-16);//from Matlab
        assertEquals(4.303725949799227e-004, instance.evaluate(100, 70), 1e-16);//from Matlab
        assertEquals(0.01710831303513, instance.evaluate(100, 80), 1e-14);//from Matlab
        assertEquals(0.15822098918643, instance.evaluate(100, 90), 1e-14);//from Matlab
        assertEquals(0.51329879827916, instance.evaluate(100, 100), 1e-14);//from Matlab
        assertEquals(0.84172132993990, instance.evaluate(100, 110), 1e-13);//from Matlab
        assertEquals(0.97213626010948, instance.evaluate(100, 120), 1e-13);//from Matlab
        assertEquals(0.99724959163269, instance.evaluate(100, 130), 1e-13);//from Matlab
        assertEquals(0.99983894282529, instance.evaluate(100, 140), 1e-13);//from Matlab
        assertEquals(0.99999407545966, instance.evaluate(100, 150), 1e-14);//from Matlab
        assertEquals(0.99999985579775, instance.evaluate(100, 160), 1e-14);//from Matlab
        assertEquals(0.99999999756992, instance.evaluate(100, 170), 1e-14);//from Matlab
        assertEquals(0.99999999997052, instance.evaluate(100, 180), 1e-14);//from Matlab
        assertEquals(0.99999999999973, instance.evaluate(100, 190), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(100, 200), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(100, 270), 1e-30);//from Matlab
        assertEquals(1, instance.evaluate(100, 999.99), 1e-60);//from Matlab
        assertEquals(1, instance.evaluate(100, 999999999), 1e-60);//from Matlab
    }

    /**
     * Test of class GammaRegularizedP.
     * Small s.
     */
    @Test
    public void testEvaluate_0060() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0, instance.evaluate(0.05, 0), 1e-14);//from Matlab
        assertEquals(0.51482795452518, instance.evaluate(0.05, 0.000001), 1e-14);//from Matlab
        assertEquals(0.57764621820570, instance.evaluate(0.05, 0.00001), 1e-14);//from Matlab
        assertEquals(0.64812693926267, instance.evaluate(0.05, 0.0001), 1e-14);//from Matlab
        assertEquals(0.72717922905292, instance.evaluate(0.05, 0.001), 1e-14);//from Matlab
        assertEquals(0.81555980574129, instance.evaluate(0.05, 0.01), 1e-14);//from Matlab
        assertEquals(0.91125762523752, instance.evaluate(0.05, 0.1), 1e-14);//from Matlab
        assertEquals(0.93920908869922, instance.evaluate(0.05, 0.2), 1e-14);//from Matlab
        assertEquals(0.95438112187435, instance.evaluate(0.05, 0.3), 1e-14);//from Matlab
        assertEquals(0.96428245014799, instance.evaluate(0.05, 0.4), 1e-14);//from Matlab
        assertEquals(0.97131737124416, instance.evaluate(0.05, 0.5), 1e-14);//from Matlab
        assertEquals(0.97656961647752, instance.evaluate(0.05, 0.6), 1e-14);//from Matlab
        assertEquals(0.98062077338083, instance.evaluate(0.05, 0.7), 1e-14);//from Matlab
        assertEquals(0.98381848987535, instance.evaluate(0.05, 0.8), 1e-14);//from Matlab
        assertEquals(0.98638642603456, instance.evaluate(0.05, 0.9), 1e-14);//from Matlab
        assertEquals(0.98847634705146, instance.evaluate(0.05, 1), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedP.
     * Small x.
     */
    @Test
    public void testEvaluate_0070() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0, instance.evaluate(0.005, 0), 1e-14);//from Matlab
        assertEquals(0.93593243382074, instance.evaluate(0.005, 0.000001), 1e-14);//from Matlab
        assertEquals(0.94676997823596, instance.evaluate(0.005, 0.00001), 1e-14);//from Matlab
        assertEquals(0.95773262906969, instance.evaluate(0.005, 0.0001), 1e-14);//from Matlab
        assertEquals(0.96881831342285, instance.evaluate(0.005, 0.001), 1e-14);//from Matlab
        assertEquals(0.97999293901738, instance.evaluate(0.005, 0.01), 1e-14);//from Matlab
        assertEquals(0.99090878477237, instance.evaluate(0.005, 0.1), 1e-14);//from Matlab
        assertEquals(0.99388938679546, instance.evaluate(0.005, 0.2), 1e-14);//from Matlab
        assertEquals(0.99546763805108, instance.evaluate(0.005, 0.3), 1e-14);//from Matlab
        assertEquals(0.99648163646034, instance.evaluate(0.005, 0.4), 1e-14);//from Matlab
        assertEquals(0.99719383727339, instance.evaluate(0.005, 0.5), 1e-14);//from Matlab
        assertEquals(0.99772072768136, instance.evaluate(0.005, 0.6), 1e-14);//from Matlab
        assertEquals(0.99812405935483, instance.evaluate(0.005, 0.7), 1e-14);//from Matlab
        assertEquals(0.99844036650874, instance.evaluate(0.005, 0.8), 1e-14);//from Matlab
        assertEquals(0.99869294429137, instance.evaluate(0.005, 0.9), 1e-14);//from Matlab
        assertEquals(0.99889747495417, instance.evaluate(0.005, 1), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedP.
     * P(45.123456789, x) = ...
     */
    @Test
    public void testEvaluate_0080() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0.51247066829275, instance.evaluate(45.123456789, 45), 1e-13);//from Matlab
        assertEquals(0.51979878059266, instance.evaluate(45.123456789, 45.123456789), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedP.
     * Big s.
     */
    @Test
    public void testEvaluate_0090() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(0, instance.evaluate(999, 2.987654321), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedP.
     * Very big s.
     */
    @Test
    public void testEvaluate_0100() {
        GammaRegularizedP instance = new GammaRegularizedP();
        assertEquals(1.845495835866142e-005, instance.evaluate(15000, 14500), 1e-15);//from Matlab
        assertEquals(0, instance.evaluate(15000, 14000), 1e-14);//from Matlab
        assertEquals(0.4616042640242, instance.evaluate(15000.123456, 14987.987654), 1e-12);//from Matlab
    }
}
