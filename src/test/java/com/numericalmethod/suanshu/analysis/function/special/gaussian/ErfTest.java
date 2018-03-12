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
package com.numericalmethod.suanshu.analysis.function.special.gaussian;

import com.numericalmethod.suanshu.analysis.function.special.gaussian.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ErfTest {

    /**
     * Test of evaluate method, of class Erf.
     */
    @Test
    public void testEvaluate() {
        Erf erf = new Erf();

        assertEquals(-9.999779095030015e-001, erf.evaluate(-3), 1e-15);//from Matlab
        assertEquals(-9.953222650189527e-001, erf.evaluate(-2), 1e-15);//from Matlab
        assertEquals(-8.427007929497148e-001, erf.evaluate(-1), 1e-15);//from Matlab
        assertEquals(-7.969082124228322e-001, erf.evaluate(-0.9), 1e-15);//from Matlab
        assertEquals(-7.421009647076605e-001, erf.evaluate(-0.8), 1e-15);//from Matlab
        assertEquals(-6.778011938374184e-001, erf.evaluate(-0.7), 1e-15);//from Matlab
        assertEquals(-6.038560908479259e-001, erf.evaluate(-0.6), 1e-15);//from Matlab
        assertEquals(-5.204998778130465e-001, erf.evaluate(-0.5), 1e-15);//from Matlab
        assertEquals(-4.283923550466685e-001, erf.evaluate(-0.4), 1e-15);//from Matlab
        assertEquals(-3.286267594591273e-001, erf.evaluate(-0.3), 1e-15);//from Matlab
        assertEquals(-2.227025892104785e-001, erf.evaluate(-0.2), 1e-15);//from Matlab
        assertEquals(-1.124629160182849e-001, erf.evaluate(-0.1), 1e-15);//from Matlab
        assertEquals(0, erf.evaluate(-0), 1e-15);//from Matlab
        assertEquals(1.124629160182849e-001, erf.evaluate(0.1), 1e-15);//from Matlab
        assertEquals(2.227025892104785e-001, erf.evaluate(0.2), 1e-15);//from Matlab
        assertEquals(3.286267594591273e-001, erf.evaluate(0.3), 1e-15);//from Matlab
        assertEquals(4.283923550466685e-001, erf.evaluate(0.4), 1e-15);//from Matlab
        assertEquals(5.204998778130465e-001, erf.evaluate(0.5), 1e-15);//from Matlab
        assertEquals(6.038560908479259e-001, erf.evaluate(0.6), 1e-15);//from Matlab
        assertEquals(6.778011938374184e-001, erf.evaluate(0.7), 1e-15);//from Matlab
        assertEquals(7.421009647076605e-001, erf.evaluate(0.8), 1e-15);//from Matlab
        assertEquals(7.969082124228322e-001, erf.evaluate(0.9), 1e-15);//from Matlab
        assertEquals(8.427007929497148e-001, erf.evaluate(1), 1e-15);//from Matlab
        assertEquals(9.953222650189527e-001, erf.evaluate(2), 1e-15);//from Matlab
        assertEquals(9.999779095030015e-001, erf.evaluate(3), 1e-15);//from Matlab
    }
}
