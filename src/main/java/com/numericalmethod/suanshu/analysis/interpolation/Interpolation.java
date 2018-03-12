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

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.tuple.OrderedPairs;

/**
 * Interpolation is a method of constructing new data points within the range of a discrete set of known data points.
 * In engineering and science, one often has a number of data points, obtained by sampling or experimentation,
 * which represent the values of a function for a limited number of values of the independent variable.
 * It is often required to interpolate (i.e. estimate) the value of that function for an intermediate value of the independent variable.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Interpolation">Wikipedia: Interpolation</a>
 */
public interface Interpolation {

    /**
     * Construct a real valued function from a discrete set of data points.
     *
     * @param f a discrete set of data points
     * @return the interpolated function
     */
    UnivariateRealFunction interpolate(OrderedPairs f);
}
