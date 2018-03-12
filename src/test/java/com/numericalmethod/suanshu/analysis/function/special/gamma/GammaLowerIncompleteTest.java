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

import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaLowerIncomplete;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GammaLowerIncompleteTest {

    /**
     * Test of class GammaLowerIncomplete.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluate_0010() {
        GammaLowerIncomplete instance = new GammaLowerIncomplete();
        instance.evaluate(0, 0);
    }

    /**
     * Test of GammaLowerIncomplete.
     */
    @Test
    public void testEvaluate_0020() {
        GammaLowerIncomplete instance = new GammaLowerIncomplete();
        assertEquals(0.63212055882856, instance.evaluate(1, 1), 1e-14);//from Matlab
        assertEquals(0.53915814955614, instance.evaluate(1.5, 1.5), 1e-14);//from Matlab
        assertEquals(1.97740410594306, instance.evaluate(2.987654321, 999.99), 1e-14);//from Matlab
    }

    /**
     * Test of class GammaLowerIncomplete.
     * R: pgamma(x, a, lower=TRUE)*gamma(a)
     */
    @Test
    public void testEvaluate_0030() {
        GammaLowerIncomplete instance = new GammaLowerIncomplete();
        assertEquals(31319.49879330277, instance.evaluate(9, 11.1), 1e-10);//from R
    }
}
