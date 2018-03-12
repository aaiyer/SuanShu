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

import com.numericalmethod.suanshu.analysis.function.special.gamma.LogGamma;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LogGammaTest {

//    @Test
    public void test_LogGamma_0010() {
        LogGamma logGamma = new LogGamma(LogGamma.Method.LANCZOS, new Lanczos(607.0 / 128.0, 15, 30));

        assertEquals(0.693147180559945, logGamma.evaluate(3), 1e-15);
        assertEquals(0.5723649429247, logGamma.evaluate(0.5), 1e-15);
        assertEquals(-0.1207822376352452, logGamma.evaluate(1.5), 1e-15);
        assertEquals(0.2846828704729192, logGamma.evaluate(2.5), 1e-15);
        assertEquals(2.032442066798749, logGamma.evaluate(0.123456789), 1e-15);
        assertEquals(23.02585092988274, logGamma.evaluate(0.0000000001), 1e-14);
        assertEquals(34.53877639491068, logGamma.evaluate(0.000000000000001), 1e-14);
        assertEquals(57.56462732485114, logGamma.evaluate(0.0000000000000000000000001), 1e-14);
        assertEquals(105.9189142777261, logGamma.evaluate(0.0000000000000000000000000000000000000000000001), 1e-13);
        assertEquals(151.970616137607, logGamma.evaluate(0.000000000000000000000000000000000000000000000000000000000000000001), 1e-12);
        assertEquals(1.791759469228055, logGamma.evaluate(4), 1e-15);
        assertEquals(11.10003103605538, logGamma.evaluate(9.23), 1e-14);
        assertEquals(144.565743946345, logGamma.evaluate(50.00000000000000001), 1e-11);
        assertEquals(2132.933881185736, logGamma.evaluate(423.00000000000000001), 1e-11);
        assertEquals(2132.933881185736, logGamma.evaluate(423.00000000000000001), 1e-11);
        assertEquals(8906.90871565719, logGamma.evaluate(1423.123456789), 1e-11);
        assertEquals(952921.618731999, logGamma.evaluate(91423.123456789), 1e-11);
        assertEquals(0, logGamma.evaluate(1), 1e-15);
        assertEquals(0, logGamma.evaluate(2), 1e-15);
    }

    /**
     * Cannot represent too small a number in double.
     */
    @Test
    public void test_LogGamma_0020() {
        LogGamma logGamma = new LogGamma(LogGamma.Method.LANCZOS_QUICK, new Lanczos(607.0 / 128.0, 15, 30));

        assertEquals(0.693147180559945, logGamma.evaluate(3), 1e-15);
        assertEquals(0.5723649429247, logGamma.evaluate(0.5), 1e-15);
        assertEquals(-0.1207822376352452, logGamma.evaluate(1.5), 1e-15);
        assertEquals(0.2846828704729192, logGamma.evaluate(2.5), 1e-15);
        assertEquals(2.032442066798749, logGamma.evaluate(0.123456789), 1e-15);
        assertEquals(23.02585092988274, logGamma.evaluate(0.0000000001), 1e-7);//worse precision
        assertEquals(34.53877639491068, logGamma.evaluate(0.000000000000001), 1e-2);//worse precision
//        assertEquals(57.56462732485114, logGamma.evaluate(0.0000000000000000000000001), 1e-14);
//        assertEquals(105.9189142777261, logGamma.evaluate(0.0000000000000000000000000000000000000000000001), 1e-13);
//        assertEquals(151.970616137607, logGamma.evaluate(0.000000000000000000000000000000000000000000000000000000000000000001), 1e-12);
        assertEquals(1.791759469228055, logGamma.evaluate(4), 1e-15);
        assertEquals(11.10003103605538, logGamma.evaluate(9.23), 1e-14);
        assertEquals(144.565743946345, logGamma.evaluate(50.00000000000000001), 1e-11);
        assertEquals(2132.933881185736, logGamma.evaluate(423.00000000000000001), 1e-11);
        assertEquals(2132.933881185736, logGamma.evaluate(423.00000000000000001), 1e-11);
        assertEquals(8906.90871565719, logGamma.evaluate(1423.123456789), 1e-11);
        assertEquals(952921.618731999, logGamma.evaluate(91423.123456789), 1e-9);//worse precision
        assertEquals(0, logGamma.evaluate(1), 1e-15);
        assertEquals(0, logGamma.evaluate(2), 1e-15);
    }
}
