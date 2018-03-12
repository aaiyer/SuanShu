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

import com.numericalmethod.suanshu.stats.random.univariate.exp.InverseTransformSamplingExpRng;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MomentsTest {

    /**
     * Test of class Moments.
     * Data generated from rexp in R.
     */
    @Test
    public void testMoments_0010() {
        Moments instance = new Moments(6);

        //test initial computation
        instance.addData(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980
                });
        assertEquals(1.31171474060050, instance.centralMoment(1), 1e-13);
        assertEquals(1.42230052089239, instance.centralMoment(2), 1e-13);
        assertEquals(3.21734252889006, instance.centralMoment(3), 1e-13);
        assertEquals(11.7083343074488, instance.centralMoment(4), 1e-13);
        assertEquals(36.9740101113727, instance.centralMoment(5), 1e-13);
        assertEquals(122.202769810141, instance.centralMoment(6), 1e-12);
        assertEquals(10, instance.N());

        //test add a datum
        instance.addData(11d);
        assertEquals(2.19246794600046, instance.centralMoment(1), 1e-13);
        assertEquals(9.05026256176148, instance.centralMoment(2), 1e-13);
        assertEquals(60.9985149534564, instance.centralMoment(3), 1e-13);
        assertEquals(553.951265706201, instance.centralMoment(4), 1e-12);
        assertEquals(4818.24189315293, instance.centralMoment(5), 1e-13);
        assertEquals(42465.2775667858, instance.centralMoment(6), 1e-9);
        assertEquals(11, instance.N());

        //test incremental computation
        instance.addData(new double[]{
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(1.63438967853819, instance.centralMoment(1), 1e-13);
        assertEquals(5.5724084720016, instance.centralMoment(2), 1e-13);
        assertEquals(39.6112382438216, instance.centralMoment(3), 1e-13);
        assertEquals(371.750513550052, instance.centralMoment(4), 1e-12);
        assertEquals(3440.9433593691, instance.centralMoment(5), 1e-9);
        assertEquals(32171.4550953691, instance.centralMoment(6), 1e-9);
        assertEquals(21, instance.N());

        //test copy ctor
        Moments copy = new Moments(instance);
        assertEquals(1.63438967853819, copy.centralMoment(1), 1e-13);
        assertEquals(5.5724084720016, copy.centralMoment(2), 1e-13);
        assertEquals(39.6112382438216, copy.centralMoment(3), 1e-13);
        assertEquals(371.750513550052, copy.centralMoment(4), 1e-12);
        assertEquals(3440.9433593691, copy.centralMoment(5), 1e-9);
        assertEquals(32171.4550953691, copy.centralMoment(6), 1e-9);
        assertEquals(21, copy.N());

        //should be same as incremental computation
        Moments single = new Moments(6, new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980, 11,
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(1.63438967853819, single.centralMoment(1), 1e-13);
        assertEquals(5.5724084720016, single.centralMoment(2), 1e-13);
        assertEquals(39.6112382438216, single.centralMoment(3), 1e-13);
        assertEquals(371.750513550052, single.centralMoment(4), 1e-12);
        assertEquals(3440.9433593691, single.centralMoment(5), 1e-9);
        assertEquals(32171.4550953691, single.centralMoment(6), 1e-9);
        assertEquals(21, single.N());
    }

    /**
     * Test of class Moments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoments_0020() {
        Moments instance = new Moments(6);

        //test initial computation
        instance.addData(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980
                });
        instance.centralMoment(99);
    }

    /**
     * Test of class Moments.
     */
    @Test
    public void testMoments_0030() {
        int size = 5000000;

        Moments skew1 = new Moments(3);

        InverseTransformSamplingExpRng rng = new InverseTransformSamplingExpRng();
        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            skew1.addData(x[i]);
        }

        Moments skew2 = new Moments(3, x);//better accuracy
        assertEquals(skew2.value(),
                skew1.value(), 1e-11);
    }
}
