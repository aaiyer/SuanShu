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
package com.numericalmethod.suanshu.stats.descriptive.moment;

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.random.univariate.exp.InverseTransformSamplingExpRng;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class VarianceTest {

    /**
     * Test of class Variance.
     */
    @Test
    public void testVariance_0010() {
        Variance stat = new Variance();
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(0, stat.N());
    }

    /**
     * Test of class Variance.
     */
    @Test
    public void testVariance_0020() {
        Variance stat = new Variance();
        stat.addData(1d);
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(1, stat.N());
    }

    /**
     * Test of class Variance.
     */
    @Test
    public void testVariance_0030() {
        Variance stat = new Variance();

        //test initial computation
        stat.addData(R.seq(1d, 10d, 1d));
        assertEquals(9.166666666666666666666666666667, stat.value(), 1e-15);
        assertEquals(10, stat.N());

        //test add a datum
        stat.addData(11d);
        assertEquals(11, stat.value(), 1e-15);
        assertEquals(11, stat.N());

        //test incremental computation
        stat.addData(R.seq(12d, 20d, 1d));
        assertEquals(35, stat.value(), 1e-15);
        assertEquals(20, stat.N());

        //test copy ctor
        Variance copy = new Variance(stat);
        assertEquals(35, copy.value(), 1e-15);
        assertEquals(20, copy.N());

        //should be same as incremental computation
        Variance single = new Variance(R.seq(1d, 20d, 1d), true);
        assertEquals(35, single.value(), 1e-15);
        assertEquals(20, single.N());
    }

    /**
     * Test of class Variance.
     * Data generated from rexp in R.
     */
    @Test
    public void testVariance_0040() {
        Variance stat = new Variance();

        //test initial computation
        stat.addData(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980
                });
        assertEquals(1.58033391210265, stat.value(), 1e-14);
        assertEquals(10, stat.N());

        //test add a datum
        stat.addData(11d);
        assertEquals(9.95528881793762, stat.value(), 1e-14);
        assertEquals(11, stat.N());

        //test incremental computation
        stat.addData(new double[]{
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(5.85102889560168, stat.value(), 1e-14);
        assertEquals(21, stat.N());

        //test copy ctor
        Variance copy = new Variance(stat);
        assertEquals(5.85102889560168, copy.value(), 1e-14);
        assertEquals(21, copy.N());

        //should be same as incremental computation
        Variance single = new Variance(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980, 11,
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                }, true);
        assertEquals(5.85102889560168, single.value(), 1e-14);
        assertEquals(21, single.N());
    }

    /**
     * Test of class Variance.
     */
    @Test
    public void testVariance_0050() {
        int size = 5000000;

        Variance var1 = new Variance();

        InverseTransformSamplingExpRng rng = new InverseTransformSamplingExpRng();
        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            var1.addData(x[i]);
        }

        Variance var2 = new Variance(x);
        assertEquals(var1.value(), var2.value(), 1e-12);
    }
}
