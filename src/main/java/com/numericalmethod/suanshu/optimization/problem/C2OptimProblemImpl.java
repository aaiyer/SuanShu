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
package com.numericalmethod.suanshu.optimization.problem;

import com.numericalmethod.suanshu.analysis.differentiation.multivariate.GradientFunction;
import com.numericalmethod.suanshu.analysis.differentiation.multivariate.HessianFunction;
import com.numericalmethod.suanshu.analysis.function.matrix.RntoMatrix;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * This is an optimization problem of a real valued function: \(\max_x f(x)\).
 *
 * @author Haksun Li
 */
public class C2OptimProblemImpl implements C2OptimProblem {

    /** the objective function to be minimized */
    private final RealScalarFunction f;
    /** the gradient function */
    private final RealVectorFunction g;
    /** the Hessian function */
    private final RntoMatrix H;

    /**
     * Construct an optimization problem with an objective function.
     *
     * @param f the objective function to be minimized
     * @param g the gradient of the objective function
     * @param H the Hessian of the objective function
     */
    public C2OptimProblemImpl(RealScalarFunction f, RealVectorFunction g, RntoMatrix H) {
        SuanShuUtils.assertArgument(f.dimensionOfDomain() == g.dimensionOfDomain(),
                                    "objective function and gradient must have the same domain dimension");
        SuanShuUtils.assertArgument(f.dimensionOfDomain() == H.dimensionOfDomain(),
                                    "objective function and Hessian must have the same domain dimension");

        this.f = f;
        this.g = g;
        this.H = H;
    }

    /**
     * Construct an optimization problem with an objective function.
     * This uses a numerical Hessian, if needed.
     *
     * @param f the objective function to be minimized
     * @param g the gradient of the objective function
     */
    public C2OptimProblemImpl(RealScalarFunction f, RealVectorFunction g) {
        this(f, g, new HessianFunction(f));
    }

    /**
     * Construct an optimization problem with an objective function.
     * This uses a numerical gradient and a numerical Hessian, if needed.
     *
     * @param f the objective function to be minimized
     */
    public C2OptimProblemImpl(RealScalarFunction f) {
        this(f, new GradientFunction(f));
    }

    /**
     * Copy Ctor.
     *
     * @param that a {@code C2OptimProblemImpl}
     */
    public C2OptimProblemImpl(C2OptimProblemImpl that) {
        this(that.f);
    }

    @Override
    public int dimension() {
        return f.dimensionOfDomain();
    }

    @Override
    public RealScalarFunction f() {
        return f;
    }

    @Override
    public RealVectorFunction g() {
        return g;
    }

    @Override
    public RntoMatrix H() {
        return H;
    }
}
