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
package com.numericalmethod.suanshu.analysis.function.special.gaussian;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;

/**
 * Marsaglia is about 3 times slower but is more accurate to compute the cumulative standard Normal.
 * It has a maximum relative error less than 1e-15 for 0 &lt; <i>x</i> less than 6.23025,
 * and relative error &lt; 1e-12 for bigger <i>x</i>.
 *
 * @author Haksun Li
 */
public class CumulativeNormalMarsaglia extends UnivariateRealFunction implements StandardCumulativeNormal {

    private static final double[] v = new double[]{
        1.2533141373155,
        0.6556795424187985,
        0.4213692292880545,
        0.3045902987101033,
        0.2366523829135607,
        0.1928081047153158,
        0.1623776608968675,
        0.1401041834530502,
        0.1231319632579329,
        0.1097872825783083,
        0.09902859647173193,
        0.09017567550106468,
        0.08276628650136917,
        0.0764757610162485,
        0.07106958053885211
    };
    private static final double c = Math.log(Constant.ROOT_2_PI);

    @Override
    public double evaluate(double x) {
        if (x < -15d) {
            return 0;
        }

        if (x > 15d) {
            return 1;
        }

        double absx = Math.abs(x);

        int j = (int) Math.min(Math.abs(x) + 0.5, 14d);
        double z = j;
        double h = absx - z;
        double a = v[j];
        double b = z * a - 1d;
        double q = 1;
        double s = a + h * b;
        for (int k = 2; k <= 24 - j; k += 2) {
            a = (a + z * b) / k;
            b = (b + z * a) / (k + 1d);
            q = q * h * h;
            s = s + q * (a + h * b);
        }

        double y = s * Math.exp(-0.5 * x * x - c);
        y = x < 0 ? y : 1d - y;
        return y;
    }
}
