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
package com.numericalmethod.suanshu.stats.descriptive.rank;

import com.numericalmethod.suanshu.misc.R;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MinTest {

    /**
     * Test of class Min.
     */
    @Test
    public void testMin_0010() {
        Min stat = new Min();
        assertEquals(Double.NaN, stat.value(), 0);
        assertEquals(0, stat.N());
    }

    /**
     * Test of class Min.
     */
    @Test
    public void testMin_0020() {
        Min stat = new Min();

        //test initial computation
        stat.addData(R.seq(1d, 10d, 1d));
        assertEquals(1d, stat.value(), 0);
        assertEquals(10, stat.N());

        //test add a datum
        stat.addData(11d);
        assertEquals(1d, stat.value(), 0);
        assertEquals(11, stat.N());

        //test incremental computation
        stat.addData(R.seq(12d, 20d, 1d));
        assertEquals(1d, stat.value(), 0);
        assertEquals(20, stat.N());

        //test copy ctor
        Min copy = new Min(stat);
        assertEquals(1d, copy.value(), 0);
        assertEquals(20, copy.N());

        //should be same as incremental computation
        Min single = new Min(R.seq(1d, 20d, 1d));
        assertEquals(1d, single.value(), 0);
        assertEquals(20, single.N());
    }

    /**
     * Test of class Min.
     */
    @Test
    public void testMin_0030() {
        Min stat = new Min();

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
        assertEquals(0.116647826876888, stat.value(), 0);
        assertEquals(10, stat.N());

        //test add a datum
        stat.addData(11d);
        assertEquals(0.116647826876888, stat.value(), 0);
        assertEquals(11, stat.N());

        //test incremental computation
        stat.addData(new double[]{
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(0.109701163135469, stat.value(), 0);
        assertEquals(21, stat.N());

        //test copy ctor
        Min copy = new Min(stat);
        assertEquals(0.109701163135469, copy.value(), 0);
        assertEquals(21, copy.N());

        //should be same as incremental computation
        Min single = new Min(new double[]{
                    1.050339964176429, 0.906121669295144, 0.116647826876888, 4.579895872370673,
                    1.714264543643022, 0.436467861756682, 0.860735191921604, 1.771233864044571,
                    0.623149028640023, 1.058291583279980, 11,
                    0.948204531821121, 0.161634152190506, 0.256991963658032, 0.109701163135469,
                    1.382998617323947, 0.417712283320725, 0.620563593227416, 0.614991265814751,
                    2.283592665441966, 3.408645607363034
                });
        assertEquals(0.109701163135469, single.value(), 0);
        assertEquals(21, single.N());
    }
}
