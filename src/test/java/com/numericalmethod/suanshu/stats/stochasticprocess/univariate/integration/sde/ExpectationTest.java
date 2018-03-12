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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde;

import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.FtWt;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.Ft;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.EvenlySpacedGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.coefficients.Diffusion;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.coefficients.Drift;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.SDE;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ExpectationTest {

    /**
     * /t
     * |  (dW)^2 = t
     * /0
     */
    @Test
    public void test_0010() {
        Expectation instance = new Expectation(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return 0;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return ft.dWt();
                    }
                }),
                0, 1, 200000,//discretization error
                0,//initial value
                200);//number of simulations

        assertEquals(1.0, instance.value, 1e-2);
        assertEquals(0.0, instance.variance, 1e-2);
    }

    /**
     * /t
     * |  (dW)^2 = t
     * /0
     */
    @Test
    public void test_0020() {
        Construction Xt = new Milstein(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return 0;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return ft.dWt();
                    }
                }),
                new EvenlySpacedGrid(0, 1.5, 200000));//discretization error

        Xt.seed(1000000);

        Expectation instance = new Expectation(
                Xt, 0,//initial value
                200);//number of simulations

        assertEquals(1.5, instance.value, 1e-3);
        assertEquals(0.0, instance.variance, 1e-3);
    }

    /**
     * /1
     * |  (B)(dB)
     * /0
     */
    @Test
    public void test_0030() {
        SDE sde = new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return 0;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return ((FtWt) ft).Wt();
                    }
                }) {

            @Override
            public Ft getFt() {
                return new FtWt();
            }
        };

        Construction Xt = new Milstein(sde, new EvenlySpacedGrid(0, 1, 1000));
        Xt.seed(1234567890L);

        Expectation E = new Expectation(
                Xt,
                0,//initial value
                200);//number of simulations

        assertEquals(1.0, E.value, 2e-1);
    }

    /**
     * /1
     * |  (B^2)(dt)
     * /0
     */
    @Test
    public void test_0040() {
        SDE sde = new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        double B = ((FtWt) ft).Wt();
                        return B * B;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return 0;
                    }
                }) {

            @Override
            public Ft getFt() {
                return new FtWt();
            }
        };

        Construction Xt = new Milstein(sde, new EvenlySpacedGrid(0, 1, 1000));
        Xt.seed(1234567890L);

        Expectation E = new Expectation(
                Xt,
                0,//initial value
                200);//number of simulations

        assertEquals(0.5, E.value, 1e-1);
    }
}
