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

import com.numericalmethod.suanshu.analysis.function.special.gaussian.Gaussian;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GaussianTest {

    /**
     * Test of evaluate method, of class Gaussian.
     */
    @Test
    public void testEvaluate() {
        Gaussian f = new Gaussian();
        assertEquals(0.00443184841193801, f.evaluate(-3), 1e-15);//from R
        assertEquals(0.0175283004935685, f.evaluate(-2.5), 1e-15);//from R
        assertEquals(0.053990966513188, f.evaluate(-2), 1e-15);//from R
        assertEquals(0.129517595665892, f.evaluate(-1.5), 1e-15);//from R
        assertEquals(0.241970724519143, f.evaluate(-1), 1e-15);//from R
        assertEquals(0.266085249898755, f.evaluate(-0.9), 1e-15);//from R
        assertEquals(0.289691552761483, f.evaluate(-0.8), 1e-15);//from R
        assertEquals(0.312253933366761, f.evaluate(-0.7), 1e-15);//from R
        assertEquals(0.333224602891800, f.evaluate(-0.6), 1e-15);//from R
        assertEquals(0.352065326764300, f.evaluate(-0.5), 1e-15);//from R
        assertEquals(0.368270140303323, f.evaluate(-0.4), 1e-15);//from R
        assertEquals(0.381387815460524, f.evaluate(-0.3), 1e-15);//from R
        assertEquals(0.391042693975456, f.evaluate(-0.2), 1e-15);//from R
        assertEquals(0.396952547477012, f.evaluate(-0.1), 1e-15);//from R
        assertEquals(0.398443914094764, f.evaluate(-0.05), 1e-15);//from R
        assertEquals(0.398942280401433, f.evaluate(0d), 1e-15);//from R
        assertEquals(0.398443914094764, f.evaluate(0.05), 1e-15);//from R
        assertEquals(0.396952547477012, f.evaluate(0.1), 1e-15);//from R
        assertEquals(0.391042693975456, f.evaluate(0.2), 1e-15);//from R
        assertEquals(0.381387815460524, f.evaluate(0.3), 1e-15);//from R
        assertEquals(0.368270140303323, f.evaluate(0.4), 1e-15);//from R
        assertEquals(0.352065326764300, f.evaluate(0.5), 1e-15);//from R
        assertEquals(0.333224602891800, f.evaluate(0.6), 1e-15);//from R
        assertEquals(0.312253933366761, f.evaluate(0.7), 1e-15);//from R
        assertEquals(0.289691552761483, f.evaluate(0.8), 1e-15);//from R
        assertEquals(0.266085249898755, f.evaluate(0.9), 1e-15);//from R
        assertEquals(0.241970724519143, f.evaluate(1), 1e-15);//from R
        assertEquals(0.129517595665892, f.evaluate(1.5), 1e-15);//from R
        assertEquals(0.053990966513188, f.evaluate(2), 1e-15);//from R
        assertEquals(0.0175283004935685, f.evaluate(2.5), 1e-15);//from R
        assertEquals(0.00443184841193801, f.evaluate(3), 1e-15);//from R
    }
}
