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
package com.numericalmethod.suanshu.algorithm.iterative.tolerance;

/**
 * The stopping criteria is that the norm of the residual <i>r</i> relative to
 * the input {@code base} is equal to or smaller than the specified
 * {@code tolerance}, that is,
 * <blockquote><i>
 * ||r||<sub>2</sub>/base &le; tolerance
 * </i></blockquote>
 *
 * @author Ken Yiu
 */
public class RelativeTolerance implements Tolerance {

    /** default tolerance */
    public static final double DEFAULT_TOLERANCE = 1e-4;
    private final double base;
    private final double tolerance;

    /**
     * Construct an instance with {@link #DEFAULT_TOLERANCE}.
     *
     * @param base the base value for computing the relative residual
     */
    public RelativeTolerance(double base) {
        this(base, DEFAULT_TOLERANCE);
    }

    /**
     * Construct an instance with specified {@code tolerance}.
     *
     * @param base      the base value for computing the relative residual
     * @param tolerance the residual norm criteria
     */
    public RelativeTolerance(double base, double tolerance) {
        this.base = base;
        this.tolerance = tolerance;
    }

    @Override
    public boolean isResidualSmall(double norm) {
        return Double.compare(norm / base, tolerance) <= 0;
    }
}
