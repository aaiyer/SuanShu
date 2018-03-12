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

import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaRegularizedQ;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GammaRegularizedQTest {

    /**
     * Test of class GammaRegularizedQ.
     * Special cases/
     */
    @Test
    public void testEvaluate_0010() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(0, instance.evaluate(0, 0), 1e-15);
        assertEquals(0, instance.evaluate(0, 999), 1e-15);
        assertEquals(1, instance.evaluate(999, 0), 1e-15);
    }

    /**
     * Test of class GammaRegularizedQ.
     * Invalid s.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluate_0020() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(0, instance.evaluate(-1, 999), 1e-15);
    }

    /**
     * Test of class GammaRegularizedQ.
     * Invalid z.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluate_0030() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(0, instance.evaluate(999, -1), 1e-15);
    }

    /**
     * Test of class GammaRegularizedQ.
     */
    @Test
    public void testEvaluate_0040() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(0.36787944117144, instance.evaluate(1, 1), 1e-14);//from Matlab
        assertEquals(0.39162517627109, instance.evaluate(1.5, 1.5), 1e-14);//from Matlab
        assertEquals(0, instance.evaluate(2.987654321, 999.99), 1e-14);//from Matlab
        assertEquals(0, instance.evaluate(5, 99), 1e-14);//from Matlab
        assertEquals(0.05496364149511, instance.evaluate(5, 9), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedQ.
     * Q(100, x) = ...
     */
    @Test
    public void testEvaluate_0050() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(1, instance.evaluate(100, 0), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(100, 10), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(100, 20), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(100, 30), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(100, 40), 1e-14);//from Matlab
        assertEquals(0.99999999967999, instance.evaluate(100, 50), 1e-14);//from Matlab
        assertEquals(0.99999851847237, instance.evaluate(100, 60), 1e-14);//from Matlab
        assertEquals(0.99956962740502, instance.evaluate(100, 70), 1e-14);//from Matlab
        assertEquals(0.98289168696487, instance.evaluate(100, 80), 1e-14);//from Matlab
        assertEquals(0.84177901081357, instance.evaluate(100, 90), 1e-14);//from Matlab
        assertEquals(0.48670120172084, instance.evaluate(100, 100), 1e-14);//from Matlab
        assertEquals(0.15827867006010, instance.evaluate(100, 110), 1e-13);//from Matlab
        assertEquals(0.02786373989052, instance.evaluate(100, 120), 1e-13);//from Matlab
        assertEquals(0.00275040836731, instance.evaluate(100, 130), 1e-13);//from Matlab
        assertEquals(1.610571747125977e-004, instance.evaluate(100, 140), 1e-16);//from Matlab
        assertEquals(5.924540335433548e-006, instance.evaluate(100, 150), 1e-16);//from Matlab
        assertEquals(1.442022450337532e-007, instance.evaluate(100, 160), 1e-16);//from Matlab
        assertEquals(2.430077250537011e-009, instance.evaluate(100, 170), 1e-16);//from Matlab
        assertEquals(2.948297161964320e-011, instance.evaluate(100, 180), 1e-16);//from Matlab
        assertEquals(2.663425036075751e-013, instance.evaluate(100, 190), 1e-16);//from Matlab
        assertEquals(1.887379141862766e-015, instance.evaluate(100, 200), 1e-16);//from Matlab
        assertEquals(0, instance.evaluate(100, 270), 1e-30);//from Matlab
        assertEquals(0, instance.evaluate(100, 999.99), 1e-60);//from Matlab
        assertEquals(0, instance.evaluate(100, 999999999), 1e-60);//from Matlab
    }

    /**
     * Test of class GammaRegularizedQ.
     * Small s.
     */
    @Test
    public void testEvaluate_0060() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(1, instance.evaluate(0.05, 0), 1e-14);//from Matlab
        assertEquals(0.48517204547482, instance.evaluate(0.05, 0.000001), 1e-14);//from Matlab
        assertEquals(0.42235378179430, instance.evaluate(0.05, 0.00001), 1e-14);//from Matlab
        assertEquals(0.35187306073733, instance.evaluate(0.05, 0.0001), 1e-14);//from Matlab
        assertEquals(0.27282077094708, instance.evaluate(0.05, 0.001), 1e-14);//from Matlab
        assertEquals(0.18444019425871, instance.evaluate(0.05, 0.01), 1e-14);//from Matlab
        assertEquals(0.08874237476248, instance.evaluate(0.05, 0.1), 1e-14);//from Matlab
        assertEquals(0.06079091130078, instance.evaluate(0.05, 0.2), 1e-14);//from Matlab
        assertEquals(0.04561887812565, instance.evaluate(0.05, 0.3), 1e-14);//from Matlab
        assertEquals(0.03571754985201, instance.evaluate(0.05, 0.4), 1e-14);//from Matlab
        assertEquals(0.02868262875584, instance.evaluate(0.05, 0.5), 1e-14);//from Matlab
        assertEquals(0.02343038352248, instance.evaluate(0.05, 0.6), 1e-14);//from Matlab
        assertEquals(0.01937922661917, instance.evaluate(0.05, 0.7), 1e-14);//from Matlab
        assertEquals(0.01618151012465, instance.evaluate(0.05, 0.8), 1e-14);//from Matlab
        assertEquals(0.01361357396544, instance.evaluate(0.05, 0.9), 1e-14);//from Matlab
        assertEquals(0.01152365294854, instance.evaluate(0.05, 1), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedQ.
     * Small x.
     */
    @Test
    public void testEvaluate_0070() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(1, instance.evaluate(0.005, 0), 1e-14);//from Matlab
        assertEquals(0.06406756617926, instance.evaluate(0.005, 0.000001), 1e-14);//from Matlab
        assertEquals(0.05323002176404, instance.evaluate(0.005, 0.00001), 1e-14);//from Matlab
        assertEquals(0.04226737093031, instance.evaluate(0.005, 0.0001), 1e-14);//from Matlab
        assertEquals(0.03118168657715, instance.evaluate(0.005, 0.001), 1e-14);//from Matlab
        assertEquals(0.02000706098262, instance.evaluate(0.005, 0.01), 1e-14);//from Matlab
        assertEquals(0.00909121522763, instance.evaluate(0.005, 0.1), 1e-14);//from Matlab
        assertEquals(0.00611061320454, instance.evaluate(0.005, 0.2), 1e-14);//from Matlab
        assertEquals(0.00453236194892, instance.evaluate(0.005, 0.3), 1e-14);//from Matlab
        assertEquals(0.00351836353966, instance.evaluate(0.005, 0.4), 1e-14);//from Matlab
        assertEquals(0.00280616272661, instance.evaluate(0.005, 0.5), 1e-14);//from Matlab
        assertEquals(0.00227927231864, instance.evaluate(0.005, 0.6), 1e-14);//from Matlab
        assertEquals(0.00187594064517, instance.evaluate(0.005, 0.7), 1e-14);//from Matlab
        assertEquals(0.00155963349126, instance.evaluate(0.005, 0.8), 1e-14);//from Matlab
        assertEquals(0.00130705570863, instance.evaluate(0.005, 0.9), 1e-14);//from Matlab
        assertEquals(0.00110252504583, instance.evaluate(0.005, 1), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedQ.
     * Q(45.123456789, x) = ...
     */
    @Test
    public void testEvaluate_0080() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(0.48752933170725, instance.evaluate(45.123456789, 45), 1e-13);//from Matlab
        assertEquals(0.48020121940734, instance.evaluate(45.123456789, 45.123456789), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedQ.
     * Big s.
     */
    @Test
    public void testEvaluate_0090() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(1, instance.evaluate(999, 2.987654321), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaRegularizedQ.
     * Very big s.
     */
    @Test
    public void testEvaluate_0100() {
        GammaRegularizedQ instance = new GammaRegularizedQ();
        assertEquals(0.99998154504164, instance.evaluate(15000, 14500), 1e-14);//from Matlab
        assertEquals(1, instance.evaluate(15000, 14000), 1e-14);//from Matlab
        assertEquals(0.5383957359758, instance.evaluate(15000.123456, 14987.987654), 1e-12);//from Matlab
    }
}
