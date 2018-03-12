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
public class SkewnessTest {

    @Test
    public void testSkewness_0010() {
        Skewness stat = new Skewness();
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(0, stat.N());
    }

    @Test
    public void testSkewness_0020() {
        Skewness stat = new Skewness();
        stat.addData(10d);
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(1, stat.N());
    }

    @Test
    public void testSkewness_0030() {
        Skewness stat = new Skewness();
        stat.addData(10d);
        stat.addData(20d);
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(2, stat.N());
    }

    @Test
    public void testSkewness_0040() {
        Skewness stat = new Skewness();

        //test initial computation
        stat.addData(R.seq(1d, 10d, 1d));
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(10, stat.N());

        //test add a datum
        stat.addData(11d);
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(11, stat.N());

        //test incremental computation
        stat.addData(R.seq(12d, 20d, 1d));
        assertEquals(0, stat.value(), 1e-15);
        assertEquals(20, stat.N());

        //test copy ctor
        Skewness copy = new Skewness(stat);
        assertEquals(0, copy.value(), 1e-15);
        assertEquals(20, copy.N());

        //should be same as incremental computation
        Skewness single = new Skewness(R.seq(1d, 20d, 1d));
        assertEquals(0, single.value(), 1e-15);
        assertEquals(20, single.N());
    }

    /**
     * Data generated from rexp in R.
     */
    @Test
    public void testSkewness_0050() {
        Skewness stat = new Skewness();

        //test initial computation
        stat.addData(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980
                });
        assertEquals(1.6194741130904, stat.value(), 1e-12);
        assertEquals(10, stat.N());

        //test add a datum
        stat.addData(11d);
        assertEquals(1.94195188718783, stat.value(), 1e-12);
        assertEquals(11, stat.N());

        //test incremental computation
        stat.addData(new double[]{
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(2.79878832928478, stat.value(), 1e-12);
        assertEquals(21, stat.N());

        //test copy ctor
        Skewness copy = new Skewness(stat);
        assertEquals(2.79878832928478, copy.value(), 1e-12);
        assertEquals(21, copy.N());

        //should be same as incremental computation
        Skewness single = new Skewness(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980, 11,
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(2.79878832928478, single.value(), 1e-12);
        assertEquals(21, single.N());
    }

    /**
     * Data generated from rexp in R.
     */
    @Test
    public void testSkewness_0060() {
        Skewness stat = new Skewness();

        //test initial computation
        stat.addData(1.050339964176429);
        stat.addData(0.906121669295144);
        stat.addData(0.116647826876888);
        stat.addData(4.579895872370673);
        stat.addData(1.714264543643022);
        stat.addData(0.436467861756682);
        stat.addData(0.860735191921604);
        stat.addData(1.771233864044571);
        stat.addData(0.623149028640023);
        stat.addData(1.058291583279980);
        assertEquals(1.6194741130904, stat.value(), 1e-12);
        assertEquals(10, stat.N());

        //test add a datum
        stat.addData(11d);
        assertEquals(1.94195188718783, stat.value(), 1e-12);
        assertEquals(11, stat.N());

        //test incremental computation
        stat.addData(new double[]{
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(2.79878832928478, stat.value(), 1e-12);
        assertEquals(21, stat.N());

        //test copy ctor
        Skewness copy = new Skewness(stat);
        assertEquals(2.79878832928478, copy.value(), 1e-12);
        assertEquals(21, copy.N());

        //should be same as incremental computation
        Skewness single = new Skewness(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980, 11,
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(2.79878832928478, single.value(), 1e-12);
        assertEquals(21, single.N());
    }

    @Test
    public void testSkewness_0070() {
        int size = 2500000;

        Skewness skew1 = new Skewness();

        InverseTransformSamplingExpRng rng = new InverseTransformSamplingExpRng();
        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            skew1.addData(x[i]);
        }

        Skewness skew2 = new Skewness(x);//better accuracy
        assertEquals(skew2.value(), skew1.value(), 1e-12);
    }
}
