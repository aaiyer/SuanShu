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
package com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset.equalityconstraint;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;

/**
 * This particular implementation of {@link SQPActiveSetSolverForOnlyEqualityConstraint1} uses {@link SQPASEVariation2}.
 *
 * @author Haksun Li
 */
public class SQPActiveSetSolverForOnlyEqualityConstraint2 extends SQPActiveSetSolverForOnlyEqualityConstraint1 {

    /**
     * Construct an SQP Active Set minimizer to solve general minimization problems with equality constraints.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public SQPActiveSetSolverForOnlyEqualityConstraint2(double epsilon, int maxIterations) {
        super(
                new VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation2 impl = new SQPASEVariation2();
                        impl.set(f, equal);
                        return impl;
                    }
                },
                epsilon, maxIterations);
    }

    /**
     * Construct an SQP Active Set minimizer to solve general minimization problems with equality constraints.
     *
     * @param r              Han's exact penalty function coefficient, the bigger the better, e.g., eq. 15.20
     * @param lower          the lower bound of alpha; the smaller the better but cannot be zero
     * @param discretization the number of points between <i>[lower, 1]</i> to search for alpha; the bigger the better
     * @param epsilon        a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations  the maximum number of iterations
     */
    public SQPActiveSetSolverForOnlyEqualityConstraint2(final double r, final double lower, final int discretization, double epsilon, int maxIterations) {
        super(
                new VariationFactory() {

                    @Override
                    public SQPASEVariation newVariation(RealScalarFunction f, EqualityConstraints equal) {
                        SQPASEVariation2 impl = new SQPASEVariation2(r, lower, discretization);
                        impl.set(f, equal);
                        return impl;
                    }
                },
                epsilon, maxIterations);
    }
}
