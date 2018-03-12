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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.brownian;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.stats.test.distribution.normality.ShapiroWilk;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.descriptive.Covariance;
import com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.MultiVariateRealization;
import static java.lang.Math.abs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RandomWalkTest {

    @Test
    public void test_0010() {
        RandomWalk Xt = new RandomWalk(2, 5000);
        Xt.seed(123456790L);

        MultiVariateRealization xt = Xt.nextRealization(new DenseVector(2, 0));
        MultiVariateRealization.Iterator it = xt.iterator();
        Vector x0 = it.nextValue();
        assertEquals(x0.ZERO(), x0);

        double[] x1 = new double[xt.size()];
        x1[0] = 0;
        double[] x2 = new double[xt.size()];
        x2[0] = 0;

        for (int i = 1; it.hasNext(); ++i) {
            Vector x = it.nextValue();
            x1[i] = x.get(1);
            x2[i] = x.get(2);
        }

        double[] dx1 = R.diff(x1);
        ShapiroWilk test = new ShapiroWilk(dx1);
        assertTrue(test.pValue() > 0.10);

        double[] dx2 = R.diff(x2);
        test = new ShapiroWilk(dx2);
        assertTrue(test.pValue() > 0.10);

        Covariance cov = new Covariance(new double[][]{dx1, dx2});
        assertEquals(0, abs(cov.value()), 1e-2);
    }

    /**
     * Check if the same realization always give the same Brownian path.
     */
    @Test
    public void test_0030() {
        RandomWalk Xt = new RandomWalk(2, 20);

        Xt.seed(1000000);
        MultiVariateRealization xt1 = Xt.nextRealization(new DenseVector(2, 0));

        Xt.seed(1000000);
        MultiVariateRealization xt2 = Xt.nextRealization(new DenseVector(2, 0));

        Matrix M1 = xt1.toMatrix();
        Matrix M2 = xt1.toMatrix();

        assertEquals(M1, M2);
    }

    /**
     * Check if the same realization always give the same Brownian path.
     */
    @Test
    public void test_0040() {
        RandomWalk Xt = new RandomWalk(2, 20);

        Xt.seed(1000000);
        MultiVariateRealization xt1 = Xt.nextRealization(new DenseVector(2, 0));

        Matrix M1 = xt1.toMatrix();
        Matrix M2 = xt1.toMatrix();

        assertEquals(M1, M2);

    }

    /**
     * Check if the same realization always give the same Brownian path.
     */
    @Test
    public void test_0050() {
        RandomWalk Xt = new RandomWalk(2, 20);

        Xt.seed(1000000);
        MultiVariateRealization xt1 = Xt.nextRealization(new DenseVector(2, 0));

        Matrix M1 = xt1.toMatrix();

        int i = 1;
        for (MultiVariateRealization.Iterator it = xt1.iterator(); it.hasNext(); ++i) {
            assertEquals(M1.getRow(i), it.nextValue());
        }
    }

    /**
     * Check if the same realization always give the same last value.
     */
    @Test
    public void test_0055() {
        RandomWalk Xt = new RandomWalk(2, 20);

        Xt.seed(1000000);
        MultiVariateRealization xt1 = Xt.nextRealization(new DenseVector(2, 0));

        Vector v1 = xt1.lastValue();
        Vector v2 = xt1.lastValue();

        assertTrue(v1.equals(v2));
    }

    /**
     * Check if two realizations are different.
     */
    @Test
    public void test_0060() {
        RandomWalk Xt = new RandomWalk(2, 20);

        for (int trials = 0; trials < 1000; ++trials) {
            MultiVariateRealization xt1 = Xt.nextRealization(new DenseVector(2, 0));
            MultiVariateRealization xt2 = Xt.nextRealization(new DenseVector(2, 0));

            MultiVariateRealization.Iterator it1 = xt1.iterator(), it2 = xt2.iterator();
            it1.next();//xt(0) = 0
            it2.next();//xt(0) = 0
            assertFalse(it1.nextValue().equals(it2.nextValue()));
        }
    }

    @Test
    public void test_0070() {
        RandomWalk Xt = new RandomWalk(2, 20);

        MultiVariateRealization xt = Xt.nextRealization(new DenseVector(2, 0));
        int i = 0;
        for (MultiVariateRealization.Iterator it = xt.iterator(); it.hasNext(); ++i) {
            assertEquals(i, it.next().getTime(), 0);
        }
    }
}
