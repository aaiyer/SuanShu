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
package com.numericalmethod.suanshu.dsp.univariate.operation.system.doubles;

/**
 * A filter, for signal processing, takes (real) input signal and transforms it to (real) output signal.
 * Often, a filter system performs mathematical operations on a sampled, discrete-time signal to reduce or enhance certain aspects of that signal.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Filter_design">Wikipedia: Filter design</a>
 * <li><a href="http://en.wikipedia.org/wiki/Digital_filter">Wikipedia: Digital filter</a>
 * </ul>
 */
public interface Filter {

    /**
     * Transforms the input signal into the output signal.
     *
     * @param xt the input signal
     * @return the filtered signal
     */
    public double[] transform(double[] xt);
}
