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

import com.numericalmethod.suanshu.misc.R;
import static com.numericalmethod.suanshu.number.DoubleUtils.collection2DoubleArray;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.Realization;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.Ft;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.SDE;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.coefficients.Diffusion;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde.coefficients.Drift;
import com.numericalmethod.suanshu.stats.test.distribution.kolmogorov.KolmogorovSmirnov.Side;
import com.numericalmethod.suanshu.stats.test.distribution.kolmogorov.KolmogorovSmirnov1Sample;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class EulerTest {

    @Test
    public void test_0010() {
        final double mu = 3.5;
        final double sd = 5.9;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                100);

        Xt.seed(12345678L);

        Realization xt = Xt.nextRealization(0);
        Realization.Iterator it = xt.iterator();
        double x0 = it.nextValue();
        assertEquals(0, x0, 0);

        ArrayList<Double> values = new ArrayList<Double>();
        values.add(x0);

        for (; it.hasNext();) {
            values.add(it.nextValue());
        }

        double[] Wt = collection2DoubleArray(values);
        double[] dWt = R.diff(Wt);

        KolmogorovSmirnov1Sample test = new KolmogorovSmirnov1Sample(dWt,
                new NormalDistribution(mu, sd),
                Side.TWO_SIDED);
        assertTrue(test.pValue() > 0.10);
    }

    @Test
    public void test_0020() {
        final double mu = 0.05;
        final double sd = 0.5;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                1000);
        Xt.seed(1000000);

        Realization xt = Xt.nextRealization(0);
        Realization.Iterator it = xt.iterator();
        double x0 = it.nextValue();
        assertEquals(0, x0, 0);

        ArrayList<Double> values = new ArrayList<Double>();
        values.add(x0);

        for (; it.hasNext();) {
            values.add(it.nextValue());
        }

        double[] Wt = collection2DoubleArray(values);
        double[] dWt = R.diff(Wt);

        KolmogorovSmirnov1Sample test = new KolmogorovSmirnov1Sample(dWt,
                new NormalDistribution(mu, sd),
                Side.TWO_SIDED);
        assertTrue(test.pValue() > 0.10);
    }

    @Test
    public void test_0030() {
        final double mu = 0.9;
        final double sd = 2.1;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                1000);

        Xt.seed(1000000);

        Realization xt = Xt.nextRealization(0);
        double[] Wt = xt.toArray();
        double[] dWt = R.diff(Wt);

        KolmogorovSmirnov1Sample test = new KolmogorovSmirnov1Sample(dWt,
                new NormalDistribution(mu, sd),
                Side.TWO_SIDED);
        assertTrue(test.pValue() > 0.10);
    }

    /**
     * Check if the same realization always give the same Brownian path.
     */
    @Test
    public void test_0040() {
        final double mu = 0.9;
        final double sd = 2.1;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                1000);

        Xt.seed(1000000);
        Realization xt1 = Xt.nextRealization(0);

        Xt.seed(1000000);
        Realization xt2 = Xt.nextRealization(0);

        assertArrayEquals(xt1.toArray(), xt2.toArray(), 0);
    }

    /**
     * Check if the same realization always give the same Brownian path.
     */
    @Test
    public void test_0050() {
        final double mu = 0.9;
        final double sd = 2.1;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                1000);

        Xt.seed(1000000);
        Realization xt1 = Xt.nextRealization(0);

        double[] arr1 = xt1.toArray();
        double[] arr2 = xt1.toArray();

        assertArrayEquals(arr1, arr2, 0);
    }

    /**
     * Check if the same realization always give the same Brownian path.
     */
    @Test
    public void test_0060() {
        final double mu = 0.9;
        final double sd = 2.1;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                1000);

        Xt.seed(1000000);
        Realization xt1 = Xt.nextRealization(0);
        double[] arr1 = xt1.toArray();

        ArrayList<Double> values = new ArrayList<Double>();

        for (Realization.Iterator it = xt1.iterator(); it.hasNext();) {
            values.add(it.nextValue());
        }

        double[] arr2 = collection2DoubleArray(values);

        assertArrayEquals(arr1, arr2, 0);
    }

    /**
     * Check if two realizations are different.
     */
    @Test
    public void test_0070() {
        final double mu = 0.9;
        final double sd = 2.1;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                1000);

        for (int trials = 0; trials < 1000; ++trials) {
            Realization xt1 = Xt.nextRealization(0);
            Realization xt2 = Xt.nextRealization(0);

            Realization.Iterator it1 = xt1.iterator(), it2 = xt2.iterator();
            it1.next();//xt(0) = 0
            it2.next();//xt(0) = 0
            assertTrue(it1.nextValue() != it2.nextValue());
        }
    }

    @Test
    public void test_0080() {
        final double mu = 0.9;
        final double sd = 2.1;

        Euler Xt = new Euler(
                new SDE(
                new Drift() {

                    public double evaluate(Ft ft) {
                        return mu;
                    }
                },
                new Diffusion() {

                    public double evaluate(Ft ft) {
                        return sd;
                    }
                }),
                1000);

        Realization xt = Xt.nextRealization(0);
        int i = 0;
        for (Realization.Iterator it = xt.iterator(); it.hasNext(); ++i) {
            assertEquals(i, it.next().getTime(), 0);
        }
    }
}
