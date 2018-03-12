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
package com.numericalmethod.suanshu.optimization.constrained.general.penaltymethod;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;

/**
 * A function P: R<sup>n</sup> -> R is a penalty function for a constrained optimization problem if it has these properties.
 * <ul>
 * <li>P is continuous;
 * <li>P(x) >= 0;
 * <li>P(x) = 0 if x is feasible; i.e., all constraints are satisfied
 * </ul>
 *
 * @author Haksun Li
 * @see "Edwin K. P. Chong, Stanislaw H. Zak. "Definition 22.1. Chapter 22. Algorithms for Constrained Optimization," An Introduction to Optimization. Wiley-Interscience. 2001."
 */
public abstract class PenaltyFunction implements RealScalarFunction {

    @Override
    public int dimensionOfRange() {
        return 1;
    }
}
