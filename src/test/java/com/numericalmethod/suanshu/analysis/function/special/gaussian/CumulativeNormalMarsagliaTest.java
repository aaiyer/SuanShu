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

import com.numericalmethod.suanshu.analysis.function.special.gaussian.CumulativeNormalMarsaglia;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.StandardCumulativeNormal;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CumulativeNormalMarsagliaTest {

    @Test
    public void testCumulativeNormal_0020() {
        StandardCumulativeNormal N = new CumulativeNormalMarsaglia();

        assertEquals(0, N.evaluate(-99999999), 0);
        assertEquals(0, N.evaluate(-9999), 0);
        assertEquals(0, N.evaluate(-1000), 0);
        assertEquals(0, N.evaluate(-100), 0);
        assertEquals(6.05749476441522e-48, N.evaluate(-14.5), 1e-60);//from R
        assertEquals(7.61985302416053e-24, N.evaluate(-10), 1e-35);//from R
        assertEquals(1.12858840595384e-19, N.evaluate(-9), 1e-30);//from R
        assertEquals(6.22096057427178e-16, N.evaluate(-8), 1e-27);//from R
        assertEquals(1.27981254388584e-12, N.evaluate(-7), 1e-23);//from R
        assertEquals(9.86587645037698e-10, N.evaluate(-6), 1e-21);//from R
        assertEquals(2.86651571879194e-07, N.evaluate(-5), 1e-18);//from R
        assertEquals(3.16712418331199e-05, N.evaluate(-4), 1e-16);//from R
        assertEquals(0.00134989803163009, N.evaluate(-3), 1e-15);//from R
        assertEquals(0.00620966532577613, N.evaluate(-2.5), 1e-15);//from R
        assertEquals(0.0227501319481792, N.evaluate(-2), 1e-15);//from R
        assertEquals(0.0287165598160018, N.evaluate(-1.9), 1e-15);//from R
        assertEquals(0.0445654627585430, N.evaluate(-1.7), 1e-15);//from R
        assertEquals(0.066807201268858, N.evaluate(-1.5), 1e-15);//from R
        assertEquals(0.080756659233771, N.evaluate(-1.4), 1e-15);//from R
        assertEquals(0.0968004845856103, N.evaluate(-1.3), 1e-15);//from R
        assertEquals(0.115069670221708, N.evaluate(-1.2), 1e-15);//from R
        assertEquals(0.135666060946383, N.evaluate(-1.1), 1e-15);//from R
        assertEquals(0.158655253931457, N.evaluate(-1), 1e-15);//from R
        assertEquals(0.460172162722971, N.evaluate(-0.1), 1e-15);//from R
        assertEquals(0.496010643685368, N.evaluate(-0.01), 1e-15);//from R
        assertEquals(0.499601057786089, N.evaluate(-0.001), 1e-15);//from R
        assertEquals(0.499960105772026, N.evaluate(-0.0001), 1e-15);//from R
        assertEquals(0.499996010577196, N.evaluate(-0.00001), 1e-15);//from R
        assertEquals(0.5, N.evaluate(0), 1e-15);//from R
        assertEquals(0.500003989422804, N.evaluate(0.00001), 1e-15);//from R
        assertEquals(0.500039894227974, N.evaluate(0.0001), 1e-15);//from R
        assertEquals(0.500398942213911, N.evaluate(0.001), 1e-15);//from R
        assertEquals(0.503989356314632, N.evaluate(0.01), 1e-15);//from R
        assertEquals(0.539827837277029, N.evaluate(0.1), 1e-15);//from R
        assertEquals(0.841344746068543, N.evaluate(1), 1e-15);//from R
        assertEquals(0.864333939053617, N.evaluate(1.1), 1e-15);//from R
        assertEquals(0.884930329778292, N.evaluate(1.2), 1e-15);//from R
        assertEquals(0.90319951541439, N.evaluate(1.3), 1e-15);//from R
        assertEquals(0.919243340766229, N.evaluate(1.4), 1e-15);//from R
        assertEquals(0.933192798731142, N.evaluate(1.5), 1e-15);//from R
        assertEquals(0.955434537241457, N.evaluate(1.7), 1e-15);//from R
        assertEquals(0.971283440183998, N.evaluate(1.9), 1e-15);//from R
        assertEquals(0.977249868051821, N.evaluate(2.0), 1e-15);//from R
        assertEquals(0.993790334674224, N.evaluate(2.5), 1e-15);//from R
        assertEquals(0.99865010196837, N.evaluate(3.0), 1e-15);//from R
        assertEquals(1, N.evaluate(10), 0);//from R
        assertEquals(1, N.evaluate(14.5), 0);//from R
        assertEquals(1, N.evaluate(100), 0);//from R
        assertEquals(1, N.evaluate(1000), 0);//from R
        assertEquals(1, N.evaluate(9999), 0);//from R
        assertEquals(1, N.evaluate(99999999), 0);//from R
    }
}
