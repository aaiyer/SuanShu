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

import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaUpperIncomplete;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GammaUpperIncompleteTest {

    /**
     * Test of class GammaUpperIncomplete.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluate_0010() {
        GammaUpperIncomplete instance = new GammaUpperIncomplete();
        instance.evaluate(0, 0);
    }

    /**
     * Test of class GammaUpperIncomplete.
     * R: pgamma(x, a, lower=FALSE)*gamma(a)
     */
    @Test
    public void testEvaluate_0020() {
        GammaUpperIncomplete instance = new GammaUpperIncomplete();
        assertEquals(9000.50120669723, instance.evaluate(9, 11.1), 1e-10);//from R
    }
}
