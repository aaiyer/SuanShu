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

import com.numericalmethod.suanshu.analysis.function.special.gaussian.StandardCumulativeNormal;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.CumulativeNormalInverse;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CumulativeNormalInverseTest {

    /**
     * Test of evaluate method, of class CumulativeNormalInverse.
     */
    @Test
    public void testEvaluate_0010() {
        CumulativeNormalInverse Ninv = new CumulativeNormalInverse();
        StandardCumulativeNormal N = new CumulativeNormalMarsaglia();

        double x, y;
        for (double u = 0.000001; u < 1d; u += 0.000001) {
            x = Ninv.evaluate(u);
            y = N.evaluate(x);
            assertEquals(u, y, 1e-9);
        }
    }

    /**
     * Test of evaluate method, of class CumulativeNormalInverse.
     */
    @Test
    public void testEvaluate_0020() {
        CumulativeNormalInverse Ninv = new CumulativeNormalInverse();
        assertEquals(0, Ninv.evaluate(0.5), 1e-15);
    }
}
