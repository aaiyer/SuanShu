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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.number.DoubleUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.collection2DoubleArray;
import com.numericalmethod.suanshu.stats.distribution.univariate.NormalDistribution;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.EvenlySpacedGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.Realization;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde.Euler;
import com.numericalmethod.suanshu.stats.test.distribution.kolmogorov.KolmogorovSmirnov.Side;
import com.numericalmethod.suanshu.stats.test.distribution.kolmogorov.KolmogorovSmirnov1Sample;
import static java.lang.Math.*;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class GeometricBrownianTest {

    @Test
    public void test_0010() {
        final double mu = 2;
        final double sd = 1.2;

        int N = 100;
        double dt = 1. / N;

        Euler Xt = new Euler(
                new GeometricBrownian(mu, sd),
                new EvenlySpacedGrid(0, 1, N));

        Xt.seed(2000000);

        Realization xt = Xt.nextRealization(0.5);
        Realization.Iterator it = xt.iterator();
        double x0 = it.nextValue();
        assertEquals(0.5, x0, 0);

        ArrayList<Double> values = new ArrayList<Double>();
        values.add(x0);

        for (; it.hasNext();) {
            values.add(it.nextValue());
        }

        double[] Wt = collection2DoubleArray(values);

        Wt = DoubleUtils.foreach(Wt, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return log(x);
            }
        });

        double[] dWt = R.diff(Wt);

        KolmogorovSmirnov1Sample test = new KolmogorovSmirnov1Sample(dWt,
                new NormalDistribution((mu - 0.5 * sd * sd) * dt, sd * sqrt(dt)),
                Side.TWO_SIDED);
        assertTrue(test.pValue() > 0.10);
    }

    @Test
    public void test_0020() {
        final double mu = 2;
        final double sd = 1.2;

        int N = 1000;
        double dt = 1. / N;

        Euler Xt = new Euler(
                new GeometricBrownian(mu, sd),
                new EvenlySpacedGrid(0, 1, N));

        Xt.seed(2000000);

        Realization xt = Xt.nextRealization(0.5);
        Realization.Iterator it = xt.iterator();
        double x0 = it.nextValue();
        assertEquals(0.5, x0, 0);

        ArrayList<Double> values = new ArrayList<Double>();
        values.add(x0);

        for (; it.hasNext();) {
            values.add(it.nextValue());
        }

        double[] Wt = collection2DoubleArray(values);

        Wt = DoubleUtils.foreach(Wt, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return log(x);
            }
        });

        double[] dWt = R.diff(Wt);

        KolmogorovSmirnov1Sample test = new KolmogorovSmirnov1Sample(dWt,
                new NormalDistribution((mu - 0.5 * sd * sd) * dt, sd * sqrt(dt)),
                Side.TWO_SIDED);
        assertTrue(test.pValue() > 0.10);
    }

    @Test
    public void test_0030() {
        final double mu = 2;
        final double sd = 1.2;

        int N = 20000;
        double dt = 1. / N;

        Euler Xt = new Euler(
                new GeometricBrownian(mu, sd),
                new EvenlySpacedGrid(0, 1, N));

        Xt.seed(123456789L);

        Realization xt = Xt.nextRealization(0.5);
        Realization.Iterator it = xt.iterator();
        double x0 = it.nextValue();
        assertEquals(0.5, x0, 0);

        ArrayList<Double> values = new ArrayList<Double>();
        values.add(x0);

        for (; it.hasNext();) {
            values.add(it.nextValue());
        }

        double[] Wt = collection2DoubleArray(values);

        Wt = DoubleUtils.foreach(Wt, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return log(x);
            }
        });

        double[] dWt = R.diff(Wt);

        KolmogorovSmirnov1Sample test = new KolmogorovSmirnov1Sample(dWt,
                new NormalDistribution((mu - 0.5 * sd * sd) * dt, sd * sqrt(dt)),
                Side.TWO_SIDED);
        assertTrue(test.pValue() > 0.10);
    }

    @Test
    public void test_0040() {
        final double mu = 2;
        final double sd = 1.2;

        int N = 2000000;
        double dt = 10. / N;

        Euler Xt = new Euler(
                new GeometricBrownian(mu, sd),
                new EvenlySpacedGrid(0, 10, N));

        Xt.seed(1234567890L);

        Realization xt = Xt.nextRealization(0.5);
        Realization.Iterator it = xt.iterator();
        double x0 = it.nextValue();
        assertEquals(0.5, x0, 0);

        ArrayList<Double> values = new ArrayList<Double>();
        values.add(x0);

        for (; it.hasNext();) {
            values.add(it.nextValue());
        }

        double[] Wt = collection2DoubleArray(values);

        Wt = DoubleUtils.foreach(Wt, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return log(x);
            }
        });

        double[] dWt = R.diff(Wt);

        KolmogorovSmirnov1Sample test = new KolmogorovSmirnov1Sample(dWt,
                new NormalDistribution((mu - 0.5 * sd * sd) * dt, sd * sqrt(dt)),
                Side.TWO_SIDED);
        assertTrue(test.pValue() > 0.10);
    }
}
