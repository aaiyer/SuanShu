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
public class KurtosisTest {

    /**
     * Test of class Kurtosis.
     * Data generated from rexp in R.
     */
    @Test
    public void testKurtosis_0010() {
        Kurtosis instance = new Kurtosis();

        //test initial computation
        instance.addData(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980
                });
        assertEquals(1.68810570125141, instance.value(), 1e-14);
        assertEquals(10, instance.N());

        //test add a datum
        instance.addData(11d);
        assertEquals(2.58938250000125, instance.value(), 1e-14);
        assertEquals(11, instance.N());

        //test incremental computation
        instance.addData(new double[]{
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(7.85893144524532, instance.value(), 1e-14);
        assertEquals(21, instance.N());

        //test copy ctor
        Kurtosis copy = new Kurtosis(instance);
        assertEquals(7.85893144524532, copy.value(), 1e-14);
        assertEquals(21, copy.N());

        //should be same as incremental computation
        Kurtosis single = new Kurtosis(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980, 11,
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(7.85893144524532, single.value(), 1e-14);
        assertEquals(21, single.N());
    }

    /**
     * Test of class Kurtosis.
     */
    @Test
    public void testKurtosis_0020() {
        int size = 5000000;

        Kurtosis kurtosis1 = new Kurtosis();

        InverseTransformSamplingExpRng rng = new InverseTransformSamplingExpRng();
        double[] x = new double[size];
        for (int i = 0; i < size; ++i) {
            x[i] = rng.nextDouble();
            kurtosis1.addData(x[i]);
        }

        Kurtosis kurtosis2 = new Kurtosis(x);
        assertEquals(kurtosis1.value(), kurtosis2.value(), 1e-11);
    }
}
