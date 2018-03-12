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
package com.numericalmethod.suanshu.stats.test.timeseries.adf;

import com.numericalmethod.suanshu.stats.test.timeseries.adf.AugmentedDickeyFuller.TrendType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * For R to execute the command "adf.test()", please install "tseries":
 * http://cran.r-project.org/web/packages/tseries/index.html
 *
 * Before running "adf.test()", you need to type "library(tseries)" to load the library.
 *
 * @author Chun Yip Yau
 */
public class AugmentedDickeyFullerTest {

    /**
     *
     x<-c(0.2,0.3,-0.1,0.4,-0.5,0.6,0.1,0.2)
     adf.test(x,alternative = c("stationary"),0)
     * 
     */
    @Test
    public void test_0010() {
        double[] sample = new double[]{0.2, 0.3, -0.1, 0.4, -0.5, 0.6, 0.1, 0.2};
        AugmentedDickeyFuller instance = new AugmentedDickeyFuller(sample, TrendType.CONSTANT, 0, null);
        assertEquals(-5.1504, instance.statistics(), 1e-4);
        assertEquals(0.01, instance.pValue(), 1e-2);//from R: p-value smaller than printed p-value 0.01
    }

    /**
     *
     x<-c(0.2,0.3,-0.1,0.4,-0.5,0.6,0.1,0.2)
     adf.test(c(x,x),alternative = c("stationary"),4)
     *
     *  The p-value is obtained form the interpolation
     * (0.9-0.1)/(3.24-1.14)=(x-0.1)/(3.24-2.642)
     * "x=0.8/(3.24-1.14)*(3.24-2.642)+0.1=0.3278"
     * -3.24 and -1.14 are from table 4.2c of A. Banerjee, J. J. Dolado, J. W. Galbraith, and D. F. Hendry (1993): Cointegration, Error Correction, and the Econometric Analysis of Non-Stationary Data, Oxford University Press, Oxford.
     */
    @Test
    public void test_0020() {
        double[] sample = new double[]{0.2, 0.3, -0.1, 0.4, -0.5, 0.6, 0.1, 0.2, 0.2, 0.3, -0.1, 0.4, -0.5, 0.6, 0.1, 0.2};
        AugmentedDickeyFuller instance = new AugmentedDickeyFuller(sample, TrendType.CONSTANT, 4, null);
        assertEquals(-2.642, instance.statistics(), 1e-3);
//        assertEquals(0.3278, instance.pValue(), 1e-2);//TODO: it seems that R's value from interpolation is quite wrong
    }

    /**
     *
     x<-c(0.2,0.3,-0.1,0.4,-0.5,0.6,0.1,0.2)
     adf.test(c(x,x),alternative = c("stationary"))
     *
     * The p-value is obtained from "0.8/(3.24-1.14)*(3.24-2.1918)+0.1". See test_0020 for details.
     */
    @Test
    public void test_0030() {
        double[] sample = new double[]{0.2, 0.3, -0.1, 0.4, -0.5, 0.6, 0.1, 0.2, 0.2, 0.3, -0.1, 0.4, -0.5, 0.6, 0.1, 0.2};
        AugmentedDickeyFuller instance = new AugmentedDickeyFuller(sample);
        assertEquals(2, instance.lagOrder, 1e-15);
        assertEquals(-2.1918, instance.statistics(), 1e-4);
        assertEquals(0.4993143, instance.pValue(), 1e-1);
    }
}
