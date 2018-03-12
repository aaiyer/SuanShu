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
package com.numericalmethod.suanshu.stats.test.variance;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BrownForsytheTest {

    /**
    library(HH)
    x=c(1.3,5.4,7.6,7.2,3.5,2.7,5.21,6.3,4.4,9.8,10.24);
    g=c(1,1,1,1,1,2,2,2,2,2,2)
    data = data.frame(cbind(g,x))
    names(data) = c("g","x")
    hov(x~g, data)
     */
    @Test
    public void test_0010() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};

        BrownForsythe instance = new BrownForsythe(samples);
        assertEquals(0.1174, instance.statistics(), 1e-4);
        assertEquals(0.7397, instance.pValue(), 1e-4);
    }

    /**
    x1 = c(1.3, 5.4, 7.6, 7.2, 3.5)
    x2 = c(2.7, 5.21, 6.3, 4.4, 9.8, 10.24)
    x3 = c(-2.3, -5.3, -4.33, -5.4)
    x4 = c(0.21, 0.34, 0.27, 0.86, 0.902, 0.663)

    ax1 = abs(x1 - median(x1))
    ax2 = abs(x2 - median(x2))
    ax3 = abs(x3 - median(x3))
    ax4 = abs(x4 - median(x4))

    x = c(ax1, ax2, ax3, ax4)
    g = c(1,1,1,1,1,2,2,2,2,2,2,3,3,3,3,4,4,4,4,4,4)

    summary(aov(x~factor(g)))
     *
     * TODO: why doesn't hov work for this example?
     *
     */
    @Test
    public void test_0020() {
        double[][] samples = new double[4][];
        samples[0] = new double[]{1.3, 5.4, 7.6, 7.2, 3.5};
        samples[1] = new double[]{2.7, 5.21, 6.3, 4.4, 9.8, 10.24};
        samples[2] = new double[]{-2.3, -5.3, -4.33, -5.4};
        samples[3] = new double[]{0.21, 0.34, 0.27, 0.86, 0.902, 0.663};

        BrownForsythe instance = new BrownForsythe(samples);
        assertEquals(3.22581, instance.statistics(), 1e-4);
        assertEquals(0.048737, instance.pValue(), 1e-5);
    }

    /**
     * TODO: why can't we reproduce this book example?
     *
     *
     *
    x1 = c(60.8, 68.7, 102.6, 87.9)
    x2 = c(68.7, 67.7, 74, 66.3, 69.8)
    x3 = c(102.6, 102.1, 100.2, 96.5)
    x4 = c(87.9, 84.2, 83.1, 85.7, 90.3)

    ax1 = abs(x1 - median(x1))
    ax2 = abs(x2 - median(x2))
    ax3 = abs(x3 - median(x3))
    ax4 = abs(x4 - median(x4))

    x = c(ax1, ax2, ax3, ax4)
    g = c(1,1,1,1,2,2,2,2,2,3,3,3,3,4,4,4,4,4)
    
    summary(aov(x~factor(g)))
     *
     *
     *Brown-Forsythe's Test for Homogeneity of Variances.
     *[In the Brown-Forsythe's test the data are transforming to yij = abs[xij - median(xj)]
     *and uses the F distribution performing an one-way ANOVA using y as the
     *dependent variable. The Brown-Frosythe statistic is corrected for artificial zeros
     *occurring in odd sized samples.
     *
     *   Syntax: function [BFtest] = BFtest(X,alpha)
     *
     *     Inputs:
     *          X - data matrix (Size of matrix must be n-by-2; data=column 1, sample=column 2).
     *       alpha - significance level (default = 0.05).
     *     Outputs:
     *          - Sample variances vector.
     *          - Whether or not the homoscedasticity was met.
     *
     *    Example: From the example 10.1 of Zar (1999, p.180), to test the Brown-Forsythe's
     *             homoscedasticity of data with a significance level = 0.05.
     *
     *                                 Diet
     *                   ---------------------------------
     *                       1       2       3       4
     *                   ---------------------------------
     *                     60.8    68.7   102.6    87.9
     *                     57.0    67.7   102.1    84.2
     *                     65.0    74.0   100.2    83.1
     *                     58.6    66.3    96.5    85.7
     *                     61.7    69.8            90.3
     *                   ---------------------------------
     *
     *           Data matrix must be:
     *            X=[60.8 1;57.0 1;65.0 1;58.6 1;61.7 1;68.7 2;67.7 2;74.0 2;66.3 2;69.8 2;
     *            102.6 3;102.1 3;100.2 3;96.5 3;87.9 4;84.2 4;83.1 4;85.7 4;90.3 4];
     *
     *     Calling on Matlab the function:
     *             BFtest(X)
     *
     *       Answer is:
     *
     * The number of samples are: 4
     *
     * ----------------------------
     * Sample    Size      Variance
     * ----------------------------
     *   1        5         9.3920
     *   2        5         8.5650
     *   3        4         7.6567
     *   4        5         8.3880
     * ----------------------------
     *
     * Brown-Forsythe's Test for Equality of Variances F=0.0831, df1= 3, df2=15
     * Probability associated to the F statistic = 0.9682
     * The associated probability for the F test is larger than 0.05
     * So, the assumption of homoscedasticity was met.
     *
     *  Created by A. Trujillo-Ortiz and R. Hernandez-Walls
     *             Facultad de Ciencias Marinas
     *             Universidad Autonoma de Baja California
     *             Apdo. Postal 453
     *             Ensenada, Baja California
     *             Mexico.
     *             atrujo@uabc.mx
     *
     *  April 19, 2003.
     *
     *  To cite this file, this would be an appropriate format:
     *  Trujillo-Ortiz, A. and R. Hernandez-Walls. (2003). BFtest: Brown-Forsythe's test for homogeneity of
     *    variances. A MATLAB file. [WWW document]. URL http://www.mathworks.com/matlabcentral/fileexchange/
     *    loadFile.do?objectId=3412&objectType=FILE
     *
     *  References:
     *
     *  Brown, M. B. and Forsythe, A. B. (1974), Robust Tests for
     *           the Equality of Variances. Journal of the American
     *           Statistical Association, 69:364-367.
     *  Zar, J. H. (1999), Biostatistical Analysis (2nd ed.).
     *           NJ: Prentice-Hall, Englewood Cliffs. p. 180.
     *
     */
    @Test
    public void test_0030() {
        double[][] samples = new double[4][];
        samples[0] = new double[]{60.8, 68.7, 102.6, 87.9};
        samples[1] = new double[]{68.7, 67.7, 74, 66.3, 69.8};
        samples[2] = new double[]{102.6, 102.1, 100.2, 96.5};
        samples[3] = new double[]{87.9, 84.2, 83.1, 85.7, 90.3};

        BrownForsythe instance = new BrownForsythe(samples);
        assertEquals(13.42781, instance.statistics(), 1e-5);//TODO: why is this different from the Matlab/book answer?
        assertEquals(0.00020981, instance.pValue(), 1e-8);//TODO: why is this different from the Matlab/book answer?
    }
}
