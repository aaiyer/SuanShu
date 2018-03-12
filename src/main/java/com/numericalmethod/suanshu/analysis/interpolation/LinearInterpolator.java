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
package com.numericalmethod.suanshu.analysis.interpolation;

import com.numericalmethod.suanshu.analysis.function.FunctionOps;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.StepFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.tuple.BinaryRelation;
import com.numericalmethod.suanshu.analysis.function.tuple.OrderedPairs;
import java.util.Arrays;

/**
 * Define a univariate function by linearly interpolating between adjacent points.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Linear_interpolation">Wikipedia: Linear interpolation</a>
 */
public class LinearInterpolator extends UnivariateRealFunction implements OnlineInterpolator {

    private double[] x;//the abscissae
    private double[] y;//the ordinates

    /**
     * Construct a univariate function by linearly interpolating between adjacent points.
     *
     * @param f the points to be interpolated
     */
    public LinearInterpolator(OrderedPairs f) {
        x = f.x();
        y = f.y();
    }

    @Override
    public void addData(OrderedPairs f) {
        StepFunction g = new StepFunction(new BinaryRelation(x, y));
        g.add(f);

        x = g.x();
        y = g.y();
    }

    @Override
    public double evaluate(double u) {
        if (u < x[0]) {
            throw new RuntimeException(String.format("cannot interpolate smaller than %f", x[0]));
        }

        if (u > x[x.length - 1]) {
            throw new RuntimeException(String.format("cannot interpolate bigger than %f", x[x.length - 1]));
        }

        int k = Arrays.binarySearch(x, u);
        if (k >= 0) {
            return y[k];
        }

        int i = -(k + 1);
        if (i == x.length - 1) {
            --i;
        }
        int j = i + 1;//the next point

        double v = FunctionOps.linearInterpolate(u, x[i], y[i], x[j], y[j]);
        return v;
    }
}
