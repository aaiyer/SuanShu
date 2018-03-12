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

import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaRegularizedPInverse;
import com.numericalmethod.suanshu.analysis.uniroot.NoRootFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class GammaRegularizedPInverseTest {

    /**
     * Test of class GammaRegularizedPInverse.
     * Special cases.
     */
//    @Test
    public void testEvaluate_0010() {
        GammaRegularizedPInverse instance = new GammaRegularizedPInverse();
        assertEquals(0, instance.evaluate(1, 0), 1e-15);
        assertEquals(Double.POSITIVE_INFINITY, instance.evaluate(1, 1), 1e-15);
    }

    /**
     * Test of class GammaRegularizedPInverse.
     * s <= 1
     */
//    @Test
    public void testEvaluate_0020() {
        GammaRegularizedPInverse instance = new GammaRegularizedPInverse();
        assertEquals(1.18822888140494e-12, instance.evaluate(0.5, 0.00000123), 1e-22);//from R
        assertEquals(7.8543928954851e-05, instance.evaluate(0.5, 0.01), 1e-18);//from R
        assertEquals(0.007895387046715614, instance.evaluate(0.5, 0.1), 1e-15);//from R
        assertEquals(0.0320923773336508, instance.evaluate(0.5, 0.2), 1e-15);//from R
        assertEquals(0.0742359309162727, instance.evaluate(0.5, 0.3), 1e-15);//from R
        assertEquals(0.137497948864228, instance.evaluate(0.5, 0.4), 1e-15);//from R
        assertEquals(0.2274682115597864, instance.evaluate(0.5, 0.5), 1e-15);//from R
        assertEquals(0.354163150400397, instance.evaluate(0.5, 0.6), 1e-15);//from R
        assertEquals(0.5370970854287926, instance.evaluate(0.5, 0.7), 1e-15);//from R
        assertEquals(0.8211872075749085, instance.evaluate(0.5, 0.8), 1e-14);//from R
        assertEquals(1.352771727047706, instance.evaluate(0.5, 0.9), 1e-14);//from R
        assertEquals(3.317448300510607, instance.evaluate(0.5, 0.99), 1e-14);//from R
        assertEquals(5.413783085331365, instance.evaluate(0.5, 0.999), 1e-13);//from R
        assertEquals(14.18699368139906, instance.evaluate(0.5, 0.9999999), 1e-10);//from R
        assertEquals(34.38162610583421, instance.evaluate(0.5, 0.9999999999999999), 1e-1);//from R
    }

    /**
     * Test of class GammaRegularizedPInverse.
     * s > 1
     */
//    @Test
    public void testEvaluate_0030() {
        GammaRegularizedPInverse instance = new GammaRegularizedPInverse();
        assertEquals(0.001414880661479343, instance.evaluate(2, 0.000001), 1e-13);//from R
        assertEquals(0.531811608389612, instance.evaluate(2, 0.1), 1e-15);//from R
        assertEquals(0.8243883090329844, instance.evaluate(2, 0.2), 1e-15);//from R
        assertEquals(1.097349210703492, instance.evaluate(2, 0.3), 1e-15);//from R
        assertEquals(1.376421342062887, instance.evaluate(2, 0.4), 1e-15);//from R
        assertEquals(1.678346990016661, instance.evaluate(2, 0.5), 1e-15);//from R
        assertEquals(2.022313245324657, instance.evaluate(2, 0.6), 1e-15);//from R
        assertEquals(2.439216483280204, instance.evaluate(2, 0.7), 1e-15);//from R
        assertEquals(2.994308347002122, instance.evaluate(2, 0.8), 1e-14);//from R
        assertEquals(3.88972016986743, instance.evaluate(2, 0.9), 1e-15);//from R
        assertEquals(6.63835206799381, instance.evaluate(2, 0.99), 1e-14);//from R
        assertEquals(9.23341347645158, instance.evaluate(2, 0.999), 1e-13);//from R
        assertEquals(19.11980005925401, instance.evaluate(2, 0.9999999), 1e-9);//from R
        assertEquals(40.46156748308746, instance.evaluate(2, 0.9999999999999999), 1e0);//from R
    }

    /**
     * Test of class GammaRegularizedPInverse.
     * Lambda is -ve using the approximation formula in eq. 10.42.
     * Need to solve for lamda numerically.
     */
    @Test
    public void testEvaluate_0040() {
        GammaRegularizedPInverse instance = new GammaRegularizedPInverse();
        assertEquals(0.0004891465812788407, instance.evaluate(3.5 / 2, 0.000001), 1e-13);//from R
    }

    /**
     * Test of class GammaRegularizedPInverse.
     */
    @Test
    public void testEvaluate_0050() {
        GammaRegularizedPInverse instance = new GammaRegularizedPInverse();
        assertEquals(1794.57165888084, instance.evaluate(2000, 0.000001), 1e-9);//from R
    }

    /**
     * Test of evaluateByApproximation, of class GammaRegularizedPInverse.
     * s <= 1
     */
    @Test
    public void test_evaluateByApproximation_0010() {
        GammaRegularizedPInverse instance = new GammaRegularizedPInverse();
        assertEquals(0.227468092579000, instance.evaluateByApproximation(0.5, 0.5), 1e-15);//from Numerical Methods for Special Functions, Example 10.1
        assertEquals(0.2274682115597864, instance.evaluate(0.5, 0.5), 1e-15);//from R
    }

    /**
     * Test of evaluateByAsymptoticInversion, of class GammaRegularizedPInverse.
     * s >= 1
     */
    @Test
    public void test_evaluateByAsymptoticInversion_0010() throws NoRootFoundException {
        GammaRegularizedPInverse instance = new GammaRegularizedPInverse();
        assertEquals(Math.log(2), instance.evaluateByAsymptoticInversion(1, 0.5), 1e-2);//from Numerical Methods for Special Functions, Section 10.3.4
        //why is this not 1.67842... the last digit does not match
        assertEquals(1.67842, instance.evaluateByAsymptoticInversion(2, 0.5), 1e-4);//from Numerical Methods for Special Functions, Section 10.3.4
    }
}
