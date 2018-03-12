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
package com.numericalmethod.suanshu.analysis.function.rn2r1.univariate;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.ContinuedFraction.MaxIterationsExceededException;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ContinuedFractionTest {

    @Test
    public void test_Golden_Ratio_0010() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                return 1;
            }

            public double B(int n, double x) {
                return 1;
            }
        });

        assertEquals((1 + Math.sqrt(5)) / 2, instance.evaluate(0), 1e-15);
    }

    @Test
    public void test_Golden_Ratio_0020() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                return 1;
            }

            public double B(int n, double x) {
                return 1;
            }
        }, 30, 1000);

        assertEquals(new BigDecimal("1.618033988749894848204586834351"),
                instance.evaluate(BigDecimal.ZERO));
    }

    @Test
    public void test_Pi_0010() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                if (n == 1) {
                    return 4;
                } else {
                    return (2 * n - 3) * (2 * n - 3);
                }
            }

            public double B(int n, double x) {
                if (n == 0) {
                    return 0;
                } else if (n == 1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }, 1e-4, Integer.MAX_VALUE);

        // This has a very bad precision.
        assertEquals(Math.PI, instance.evaluate(0), 1e-3);
    }

    @Test
    public void test_Pi_0020() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                return (2 * n - 1) * (2 * n - 1);
            }

            public double B(int n, double x) {
                if (n == 0) {
                    return 3;
                } else {
                    return 6;
                }
            }
        }, 1e-14, Integer.MAX_VALUE);

        assertEquals(Math.PI, instance.evaluate(0), 1e-12);
    }

    @Test
    public void test_Pi_0030() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                if (n == 1) {
                    return 4;
                } else {
                    return (n - 1) * (n - 1);
                }
            }

            public double B(int n, double x) {
                if (n == 0) {
                    return 0;
                } else {
                    return 2 * n - 1;
                }
            }
        });

        assertEquals(Math.PI, instance.evaluate(0), 1e-14);
    }

    @Test
    public void test_Pi_0040() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                if (n == 1) {
                    return 4;
                } else {
                    return (n - 1) * (n - 1);
                }
            }

            public double B(int n, double x) {
                if (n == 0) {
                    return 0;
                } else {
                    return 2 * n - 1;
                }
            }
        }, 30, 1000);

        assertEquals(new BigDecimal("3.141592653589793238462643385186"),
                instance.evaluate(BigDecimal.ZERO));
    }

    @Test
    public void test_Exp_0010() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                if (n == 1) {
                    return x;
                } else if (n == 2) {
                    return -x;
                } else {
                    return (n - 1) * -x;
                }
            }

            public double B(int n, double x) {
                if (n == 0) {
                    return 1;
                } else if (n == 1) {
                    return 1;
                } else {
                    return x + n;
                }
            }
        });

        assertEquals(1, instance.evaluate(0), 1e-15);
        assertEquals(Math.E, instance.evaluate(1), 1e-15);
        assertEquals(Math.pow(Math.E, 2), instance.evaluate(2), 1e-14);
        assertEquals(Math.pow(Math.E, 3.5), instance.evaluate(3.5), 1e-13);
        assertEquals(Math.pow(Math.E, 9.9), instance.evaluate(9.9), 1e-9);
        assertEquals(Math.pow(Math.E, 13), instance.evaluate(13), 1e-7);
        assertEquals(1202604.284164777, instance.evaluate(14), 1e-8);//from R

//        System.out.println(instance.evaluate(13.125));
//        System.out.println(instance.evaluate(BigDecimal.valueOf(13.125), 30, 1000));

        assertEquals(1., 2.7175592936530185E9 / instance.evaluate(21.723), 1e-8);

        //        BigDecimal z = instance.evaluate(BigDecimal.valueOf(21.723), 30, 1000);
//        System.out.println(z.doubleValue());
//        System.out.println(z);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void test_max_iteration_0010() {
        ContinuedFraction instance = new ContinuedFraction(new ContinuedFraction.Partials() {

            public double A(int n, double x) {
                return 1;
            }

            public double B(int n, double x) {
                return 1;
            }
        }, 0., 1);

        assertEquals((1 + Math.sqrt(5)) / 2, instance.evaluate(0), 1e-15);
    }
}
