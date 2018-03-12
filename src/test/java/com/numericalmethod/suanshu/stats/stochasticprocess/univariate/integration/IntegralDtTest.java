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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class IntegralDtTest {

    /**
     * /t
     * |  (dt) = t
     * /0
     */
    @Test
    public void test_0010() {
        FiltrationFunction f = new FiltrationFunction() {

            @Override
            public double evaluate(int t) {
                return 1;
            }
        };
        IntegralDt I = new IntegralDt(f);
        Expectation E = new Expectation(I, 0, 1, 10000, 300);
        assertEquals(1.0, E.mean(), 1e-3);
    }

    /**
     * /1
     * |  x^2 dx = 0.33333333333333
     * /0
     */
    @Test
    public void test_0020() {
        FiltrationFunction f = new FiltrationFunction() {

            @Override
            public double evaluate(int t) {
                double x = FT.T(t);
                return x * x;
            }
        };
        IntegralDt I = new IntegralDt(f);
        Expectation E = new Expectation(I, 0, 1, 10000, 300);
        assertEquals(1. / 3., E.mean(), 1e-3);
    }

    /**
     * /1
     * |  (B(t))^2 dt = 0.5
     * /0
     */
    @Test
    public void test_0030() {
        FiltrationFunction f = new FiltrationFunction() {

            @Override
            public double evaluate(int t) {
                double Bt = FT.B(t);
                return Bt * Bt;
            }
        };
        IntegralDt I = new IntegralDt(f);
        Expectation E = new Expectation(I, 0, 1, 10000, 400);
        assertEquals(0.5, E.mean(), 1e-1);
    }
}
