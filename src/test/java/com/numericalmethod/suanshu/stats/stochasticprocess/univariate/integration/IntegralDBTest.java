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
public class IntegralDBTest {

    /**
     * /t
     * |  (dW)^2 = t
     * /0
     */
    @Test
    public void test_0010() {
        FiltrationFunction f = new FiltrationFunction() {

            @Override
            public double evaluate(int t) {
                double dBt = FT.dB(t);
                return dBt;
            }
        };
        IntegralDB I = new IntegralDB(f);
        Expectation E = new Expectation(I, 0, 1, 10000, 300);
        assertEquals(1.0, E.mean(), 1e-2);
    }

    /**
     * non adapted function
     *
     * /1
     * |  A(t) dB(t);
     * /0
     *
     *
     * A(t) =
     *        /1
     * B(t) - |  B(t) dt = 1
     *        /0
     */
    @Test
    public void test_0020() {
        FiltrationFunction f1 = new FiltrationFunction() {

            double I2value = 0;

            @Override
            public void setFT(Filtration FT) {
                super.setFT(FT);

                FiltrationFunction f2 = new FiltrationFunction() {

                    @Override
                    public double evaluate(int t) {
                        double Bt = FT.B(t);
                        return Bt;
                    }
                };

                /*
                 * /1
                 * |  B(t) dt = 1
                 * /0
                 */
                IntegralDt I2 = new IntegralDt(f2);
                I2value = I2.integral(FT);
            }

            @Override
            public double evaluate(int t) {
                double Bt = FT.B(t);
                return Bt - I2value;
            }
        };

        IntegralDB I = new IntegralDB(f1);
        Expectation E = new Expectation(I, 0, 1, 1000, 200);
        assertEquals(-0.5, E.mean(), 1e-1);
    }
}
