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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is an upper bound constraints such that for all x<sub>i</sub>'s,
 * <blockquote><i>
 * x<sub>i</sub> &le; b
 * </i></blockquote>
 *
 * @author Haksun Li
 */
public class UpperBoundConstraints extends LinearLessThanConstraints {

    /**
     * Construct an upper bound constraints for all variables in a function.
     *
     * @param f     a function
     * @param lower the upper bound
     */
    public UpperBoundConstraints(RealScalarFunction f, double lower) {
        super(
                new DenseMatrix(f.dimensionOfDomain(), f.dimensionOfDomain()).ONE().scaled(1.),
                new DenseVector(f.dimensionOfDomain(), lower));
    }
}
