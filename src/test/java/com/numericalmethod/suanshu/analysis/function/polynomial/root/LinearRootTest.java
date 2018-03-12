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
package com.numericalmethod.suanshu.analysis.function.polynomial.root;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearRootTest {

    @Test
    public void test_0010() {
        LinearRoot instance = new LinearRoot();
        Polynomial polynomial = new Polynomial(1, 1);
        Number expResult = new Double(-1);
        assertEquals(expResult, instance.solve(polynomial).get(0));
    }

    @Test
    public void test_0020() {
        LinearRoot instance = new LinearRoot();
        Polynomial polynomial = new Polynomial(3, 4);
        Number expResult = new Double(-4d / 3d);
        assertEquals(expResult, instance.solve(polynomial).get(0));
    }

    @Test
    public void test_0030() {
        LinearRoot instance = new LinearRoot();
        Polynomial polynomial = new Polynomial(5.6789, 1.234);
        Number expResult = new Double(-1.234 / 5.6789);
        assertEquals(expResult, instance.solve(polynomial).get(0));
    }

    @Test
    public void test_0040() {
        LinearRoot instance = new LinearRoot();
        Polynomial polynomial = new Polynomial(-123.456789, 100.123456);
        Number expResult = new Double(100.123456 / 123.456789);
        assertEquals(expResult, instance.solve(polynomial).get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_exception_0010() {
        LinearRoot instance = new LinearRoot();
        instance.solve(new Polynomial(1, -2, 1));
    }
}
