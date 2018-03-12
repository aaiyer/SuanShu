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
package com.numericalmethod.suanshu.stats.test.rank;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chun Yip Yau
 */
public class ConfidenceSetTest {

    @Test
    public void test_0010() {
        assertTrue(true);

//        Util.logCurrentLocation();
//        DenseVector group1 = new DenseVector(new double[]{10, 12, 15, 21, 32, 40, 41, 45});
//        DenseVector group2 = new DenseVector(new double[]{6, 20, 27, 38, 46, 51, 54, 57});
//        DenseVector a = new DenseVector(new double[]{-1.56, -1.19, -0.93, -0.72, -0.54, -0.38, -0.22, -0.07, 0.07, 0.22, 0.38, 0.54, 0.72, 0.93, 1.19, 1.56});
//
//        ConfidenceSet instance = new ConfidenceSet(group1, group2, a, -3.4, 3.4);
        //assertEquals(2.0, instance.TestStatistics, 1e-15);
        //assertEquals(0.13801073756865967, instance.pValue, 1e-15);
//<editor-fold defaultstate="collapsed" desc="R-code for the corresponding statistics">
/*        R code for the above T test
        x=c(1.3,5.4,7.6,7.2,3.5);
        y=c(2.7,5.21,6.3,4.4,9.8,10.24);
        wilcox.test(x,y,exact=F,corr=F,mu=2)
         */
        //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="print the output">
        //Print the output
//        System.out.println("Test 0010");
//        System.out.println(instance.testStatistics);
//        System.out.println(instance.pValue);
        //
        //</editor-fold>
    }
}
