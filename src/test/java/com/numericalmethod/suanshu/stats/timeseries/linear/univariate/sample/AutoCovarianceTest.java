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
package com.numericalmethod.suanshu.stats.timeseries.linear.univariate.sample;

import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.TimeSeries;
import com.numericalmethod.suanshu.stats.timeseries.univariate.realtime.SimpleTimeSeries;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class AutoCovarianceTest {

    /**
     *
    x = c(
    0.746442032, -1.174846137,  0.457833598,  1.262170385,  2.187507446,  1.362852922, -1.595587454,
    -1.691156037, -0.838506694,  0.819561633, -0.315649541, -0.778667462, -0.310001905, -1.051255680,
    1.070692127, -1.127136624, -0.833129898, -0.592892236, -1.130508277,  0.637755162, -0.935264858,
    0.719018216, -1.014168833,  0.017636674,  0.034173124, -0.493746984,  2.415451292, -1.747250038,
    -1.876075835,  1.071008269, -0.677587176, -0.299224561,  0.177866218, -0.183319668, -0.080333013,
    0.208456857,  1.442948295,  1.253760904,  0.501507561,  0.551381818, -0.260999192, -0.628646641,
    1.604062851,  0.988221699,  0.684230133, -0.529953580,  0.770813643,  1.256948582, -0.066927782,
    0.497271031,  0.323006691,  0.514548070, -0.878707524,  0.060511133,  0.331061940, -0.394567565,
    0.021082132, -0.324927966, -1.797431687,  0.135910680, -0.724331224, -1.656533089,  1.929337547,
    -1.224090277,  0.614220944, -0.590857459, -0.860089127, -1.034636917, -0.519097740, -0.001888407,
    -0.276795633, -0.930469302, -0.498201585, -0.797067695,  0.466758465, -0.919015357,  1.303296000,
    1.644352585,  0.206080588, -0.146273324,  1.637116150,  0.397366511, -0.009142027,  0.816715488,
    1.053010861,  0.761144599, -0.589543535,  1.183723289, -0.154446604, -0.361841328,  0.191375966,
    -1.269286780, -2.409598512, -0.216449910,  0.078073282,  0.862652442, -1.040448916,  1.799058714,
    0.127281101, -0.524543742
    )

    acf(x, lag.max = 20, type = "covariance", plot = FALSE)
     * 
     */
    @Test
    public void test_0010() {
        TimeSeries xt = new SimpleTimeSeries(new double[]{
                    0.746442032, -1.174846137, 0.457833598, 1.262170385, 2.187507446, 1.362852922, -1.595587454,
                    -1.691156037, -0.838506694, 0.819561633, -0.315649541, -0.778667462, -0.310001905, -1.051255680,
                    1.070692127, -1.127136624, -0.833129898, -0.592892236, -1.130508277, 0.637755162, -0.935264858,
                    0.719018216, -1.014168833, 0.017636674, 0.034173124, -0.493746984, 2.415451292, -1.747250038,
                    -1.876075835, 1.071008269, -0.677587176, -0.299224561, 0.177866218, -0.183319668, -0.080333013,
                    0.208456857, 1.442948295, 1.253760904, 0.501507561, 0.551381818, -0.260999192, -0.628646641,
                    1.604062851, 0.988221699, 0.684230133, -0.529953580, 0.770813643, 1.256948582, -0.066927782,
                    0.497271031, 0.323006691, 0.514548070, -0.878707524, 0.060511133, 0.331061940, -0.394567565,
                    0.021082132, -0.324927966, -1.797431687, 0.135910680, -0.724331224, -1.656533089, 1.929337547,
                    -1.224090277, 0.614220944, -0.590857459, -0.860089127, -1.034636917, -0.519097740, -0.001888407,
                    -0.276795633, -0.930469302, -0.498201585, -0.797067695, 0.466758465, -0.919015357, 1.303296000,
                    1.644352585, 0.206080588, -0.146273324, 1.637116150, 0.397366511, -0.009142027, 0.816715488,
                    1.053010861, 0.761144599, -0.589543535, 1.183723289, -0.154446604, -0.361841328, 0.191375966,
                    -1.269286780, -2.409598512, -0.216449910, 0.078073282, 0.862652442, -1.040448916, 1.799058714,
                    0.127281101, -0.524543742
                });

        AutoCovariance instance = new AutoCovariance(xt, AutoCovariance.Type.TYPE_I);
        assertEquals(0.95858, instance.evaluate(0), 1e-5);
        assertEquals(0.02800, instance.evaluate(1), 1e-5);
        assertEquals(0.02253, instance.evaluate(2), 1e-5);
        assertEquals(0.08916, instance.evaluate(3), 1e-5);
        assertEquals(-0.01838, instance.evaluate(4), 1e-5);
        assertEquals(0.13211, instance.evaluate(5), 1e-5);
        assertEquals(-0.00168, instance.evaluate(6), 1e-5);
        assertEquals(0.10220, instance.evaluate(7), 1e-5);
        assertEquals(-0.06272, instance.evaluate(8), 1e-5);
        assertEquals(-0.10223, instance.evaluate(9), 1e-5);
        assertEquals(0.10362, instance.evaluate(10), 1e-5);
        assertEquals(-0.07048, instance.evaluate(11), 1e-5);
        assertEquals(-0.00764, instance.evaluate(12), 1e-5);
        assertEquals(-0.07163, instance.evaluate(13), 1e-5);
        assertEquals(-0.08547, instance.evaluate(14), 1e-5);
        assertEquals(-0.07534, instance.evaluate(15), 1e-5);
        assertEquals(-0.13606, instance.evaluate(16), 1e-5);
        assertEquals(0.03186, instance.evaluate(17), 1e-5);
        assertEquals(-0.04716, instance.evaluate(18), 1e-5);
        assertEquals(-0.19784, instance.evaluate(19), 1e-5);
        assertEquals(-0.04490, instance.evaluate(20), 1e-5);
    }
}
