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
package com.numericalmethod.suanshu.analysis.sequence;

import com.numericalmethod.suanshu.analysis.function.FunctionOps;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class SummationTest {

    /**
     * Test of class Summation.
     * s = ∑(n)
     */
    @Test
    public void test_Summation_0010() {
        Summation series = new Summation(new Summation.Term() {

            public double evaluate(double i) {
                return i;
            }
        });

        double sum = series.sum(1, 100);
        assertEquals(5050, sum, 0);

        sum = series.sum(1, 100, 1);
        assertEquals(5050, sum, 0);

        sum = series.sum(1d, 100d, 1d);
        assertEquals(5050, sum, 0);
    }

    /**
     * Test of class Summation.
     * s = ∑(1 / n)
     */
    @Test
    public void test_Summation_0020() {
        Summation series = new Summation(new Summation.Term() {

            public double evaluate(double i) {
                return 1d / i;
            }
        }, 0.0001);

        double sum = series.sumToInfinity(1);
        assertEquals(9.78760603604438, sum, 1e-13);

        sum = series.sumToInfinity(1, 1);
        assertEquals(9.78760603604438, sum, 1e-13);

        sum = series.sumToInfinity(1d, 1d);
        assertEquals(9.78760603604438, sum, 1e-13);
    }

    /**
     * Test of class Summation.
     * s = ∑(n^2)
     */
    @Test
    public void test_Summation_0030() {
        Summation series = new Summation(new Summation.Term() {

            public double evaluate(double i) {
                return i * i;
            }
        });

        double sum = series.sum(1, 100);
        assertEquals(338350, sum, 0);

        sum = series.sum(1, 100, 1);
        assertEquals(338350, sum, 0);

        sum = series.sum(1d, 100d, 1d);
        assertEquals(338350, sum, 0);
    }

    /**
     * Test of class Summation.
     * s = ∑(n^4)
     */
    @Test
    public void test_Summation_0040() {
        Summation series = new Summation(new Summation.Term() {

            public double evaluate(double i) {
                return i * i * i * i;
            }
        });

        double sum = series.sum(1, 100);
        assertEquals(2050333330, sum, 0);

        sum = series.sum(1, 100, 1);
        assertEquals(2050333330, sum, 0);

        sum = series.sum(1d, 100d, 1d);
        assertEquals(2050333330, sum, 0);
    }

    /**
     * Test of class Summation.
     * e = ∑(1 / n!)
     */
    @Test
    public void test_Summation_0050() {
        Summation series = new Summation(new Summation.Term() {

            public double evaluate(double i) {
                return 1d / FunctionOps.factorial((int) i);
            }
        }, 0.0001);

        double sum = series.sumToInfinity(0);
        assertEquals(Math.E, sum, 0.0001);

        sum = series.sumToInfinity(0, 1);
        assertEquals(Math.E, sum, 0.0001);

        sum = series.sumToInfinity(0d, 1d);
        assertEquals(Math.E, sum, 0.0001);
    }
}
