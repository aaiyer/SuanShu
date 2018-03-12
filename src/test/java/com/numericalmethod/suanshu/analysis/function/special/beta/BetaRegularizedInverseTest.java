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
package com.numericalmethod.suanshu.analysis.function.special.beta;

import com.numericalmethod.suanshu.analysis.function.special.beta.BetaRegularizedInverse;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BetaRegularizedInverseTest {

    @Test
    public void test_0010() {
        BetaRegularizedInverse IxInv = new BetaRegularizedInverse(0.01, 0.9);
        assertEquals(0, IxInv.evaluate(0), 1e-15);
        assertEquals(1, IxInv.evaluate(1), 1e-15);
        assertEquals(1.192839281955185e-300, IxInv.evaluate(1e-3), 1e-312);//from R
        assertEquals(1.192839281955145e-100, IxInv.evaluate(0.1), 1e-112);//from R
        assertEquals(1.512103431746252e-70, IxInv.evaluate(0.2), 1e-82);//from R
        assertEquals(6.14762551765798e-53, IxInv.evaluate(0.3), 1e-64);//from R
        assertEquals(1.916818822860307e-40, IxInv.evaluate(0.4), 1e-51);//from R
        assertEquals(9.40984275746329e-31, IxInv.evaluate(0.5), 1e-42);//from R
//        assertEquals(1.55431223447522e-15, IxInv.evaluate(0.6), 1e-26);//R seems to give wrong value...
//        assertEquals(3.33066907387547e-16, IxInv.evaluate(0.7), 1e-27);//R seems to give wrong value...
        assertEquals(2.42985631615511e-10, IxInv.evaluate(0.8), 1e-16);//from R
        assertEquals(3.16833805859007e-05, IxInv.evaluate(0.9), 1e-14);//from R
        assertEquals(0.999999999999959, IxInv.evaluate(0.99999999999999), 1e-15);//from R
    }

    @Test
    public void test_0020() {
        BetaRegularizedInverse IxInv = new BetaRegularizedInverse(0.2, 0.6);
        assertEquals(0, IxInv.evaluate(0), 1e-15);
        assertEquals(1, IxInv.evaluate(1), 1e-15);
        assertEquals(2.234461922073728e-15, IxInv.evaluate(1e-3), 1e-29);//from R
        assertEquals(2.234445279396468e-05, IxInv.evaluate(0.1), 1e-19);//from R
        assertEquals(0.000714857411933005, IxInv.evaluate(0.2), 1e-15);//from R
        assertEquals(0.005419923182614, IxInv.evaluate(0.3), 1e-15);//from R
        assertEquals(0.0227069817570522, IxInv.evaluate(0.4), 1e-15);//from R
        assertEquals(0.06821872384997794, IxInv.evaluate(0.5), 1e-15);//from R
        assertEquals(0.1639481600325772, IxInv.evaluate(0.6), 1e-15);//from R
        assertEquals(0.331095484594725, IxInv.evaluate(0.7), 1e-15);//from R
        assertEquals(0.571707284366801, IxInv.evaluate(0.8), 1e-15);//from R
        assertEquals(0.838787911396515, IxInv.evaluate(0.9), 1e-15);//from R
        assertEquals(1, IxInv.evaluate(0.99999999999999), 1e-14);//from R
    }

    @Test
    public void test_0030() {
        BetaRegularizedInverse IxInv = new BetaRegularizedInverse(5.5, 4.123);
        assertEquals(0, IxInv.evaluate(0), 1e-15);
        assertEquals(1, IxInv.evaluate(1), 1e-15);
        assertEquals(0.1385929563387487, IxInv.evaluate(1e-3), 1e-15);//from R
        assertEquals(0.367497352952347, IxInv.evaluate(0.1), 1e-15);//from R
        assertEquals(0.437861168230539, IxInv.evaluate(0.2), 1e-15);//from R
        assertEquals(0.4901010390235138, IxInv.evaluate(0.3), 1e-15);//from R
        assertEquals(0.535009605860113, IxInv.evaluate(0.4), 1e-15);//from R
        assertEquals(0.5766992196374195, IxInv.evaluate(0.5), 1e-15);//from R
        assertEquals(0.617661543228155, IxInv.evaluate(0.6), 1e-15);//from R
        assertEquals(0.6602036210998874, IxInv.evaluate(0.7), 1e-15);//from R
        assertEquals(0.707684706231568, IxInv.evaluate(0.8), 1e-15);//from R
        assertEquals(0.768258993401824, IxInv.evaluate(0.9), 1e-15);//from R
        assertEquals(0.9998694224698526, IxInv.evaluate(0.99999999999999), 1e-5);//from R; TODO: we have quite poor precision here
    }

    @Test
    public void test_0040() {
        BetaRegularizedInverse IxInv = new BetaRegularizedInverse(1.75, 0.8115);
        assertEquals(0.1369755684998413, IxInv.evaluate(0.02354), 1e-15);//from R
    }

    @Test
    public void test_0050() {
        BetaRegularizedInverse IxInv = new BetaRegularizedInverse(10000, 20000);
        assertEquals(0, IxInv.evaluate(0), 1e-15);
        assertEquals(1, IxInv.evaluate(1), 1e-15);
        assertEquals(0.3298477983630155, IxInv.evaluate(0.1), 1e-12);//from R
        assertEquals(0.3310416513477313, IxInv.evaluate(0.2), 1e-13);//from R
    }

    /**
     * nearlySymmetricCase
     */
    @Test
    public void test_0060() {
        BetaRegularizedInverse IxInv = new BetaRegularizedInverse(5000, 1000);
        assertEquals(0.8271443228717066, IxInv.evaluate(0.1), 1e-14);//from R
    }
}
