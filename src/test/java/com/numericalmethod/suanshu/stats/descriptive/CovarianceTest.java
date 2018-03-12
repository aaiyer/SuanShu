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
package com.numericalmethod.suanshu.stats.descriptive;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CovarianceTest {

    /**
     * Test of class Covariance.
     */
    @Test
    public void testCovariance_0010() {
        Covariance stat = new Covariance();

        //test initial computation
        stat.addData(new double[][]{
                    {1, 3, 5},
                    {2, 4, 6}
                });
        assertEquals(4, stat.value(), 0);
        assertEquals(3, stat.N());

        //test add a datum
        stat.addData(5.5, -6.6);
        assertEquals(-3.958333333333333, stat.value(), 1e-15);
        assertEquals(4, stat.N());

        //test incremental computation
        stat.addData(1.1, 3.3, 5.5, 2.2, 4.4, 6.6);
        assertEquals(-0.649047619047619, stat.value(), 1e-15);
        assertEquals(7, stat.N());

        //test copy ctor
        Covariance copy = new Covariance(stat);
        assertEquals(-0.649047619047619, copy.value(), 1e-15);
        assertEquals(7, copy.N());
    }

    /**
     * Test of class Covariance.
     */
    @Test
    public void testCovariance_0020() {
        Covariance stat = new Covariance();

        stat.addData(1, 2);
        stat.addData(3, 4);

        assertEquals(2, stat.value(), 0);
        assertEquals(2, stat.N());

        stat.addData(5, 6);

        assertEquals(4, stat.value(), 0);
        assertEquals(3, stat.N());

        stat.addData(5.5, -6.6);
        stat.addData(1.1, 2.2);
        stat.addData(3.3, 4.4);
        stat.addData(5.5, 6.6);

        assertEquals(-0.649047619047619, stat.value(), 1e-15);
        assertEquals(7, stat.N());
    }

    /**
     * Test of class Covariance.
     */
    @Test
    public void testCovariance_0030() {
        Covariance stat = new Covariance();
        stat.addData(1, 2);

        assertEquals(Double.NaN, stat.value(), 0);
        assertEquals(1, stat.N());
    }
}
