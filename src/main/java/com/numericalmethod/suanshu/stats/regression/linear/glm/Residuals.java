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
package com.numericalmethod.suanshu.stats.regression.linear.glm;

import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.sqrt;

/**
 * Residual analysis of the results of a Generalized Linear Model regression.
 *
 * <p>
 * Overdispersion occurs when the observed variance of the data is larger than the predicted variance.
 *
 * <p>
 * Deviance measures the goodness-of-fit of a model
 *
 * @author Haksun Li
 */
public class Residuals extends com.numericalmethod.suanshu.stats.regression.linear.Residuals {

    /**
     * overdispersion
     */
    public final double overdispersion;
    /**
     * deviance
     */
    public final double deviance;
    /**
     * deviances of observations
     */
    protected final double[] deviances;
    /**
     * deviances residuals
     */
    public final ImmutableVector devianceResiduals;

    /**
     * Perform the residual analysis for a GLM problem.
     * 
     * @param problem the GLM problem to be solved
     * @param fitted the fitted values
     */
    protected Residuals(GLMProblem problem, Vector fitted) {
        super(problem, fitted);

        final int nObs = problem.nObs();

        this.deviances = deviances();
        this.deviance = DoubleArrayMath.sum(deviances);

        this.overdispersion = overdispersion();

        //@see "P. J. MacCullagh and J. A. Nelder. "An algorithm for fitting generalized linear models" in <i>Generalized Linear Models,<i> 2nd ed. pp.39."
        double[] tmp = new double[nObs];
        for (int i = 0; i < nObs; i++) {
            tmp[i] = residuals.get(i + 1) > 0 ? sqrt(deviances[i]) : -sqrt(deviances[i]);
        }
        devianceResiduals = new ImmutableVector(new DenseVector(tmp));
    }

    /**
     * Compute the overdispersion of this GLM.
     *
     * @return the overdispersion
     */
    public double overdispersion() {
        final GLMProblem p = (GLMProblem) problem;
        return p.family.dispersion(problem.y, fitted, problem.nFactors());
    }

    /*
     * Compute the deviances of the observations.
     *
     * @return the deviances
     * 
     * @see "P. J. MacCullagh and J. A. Nelder. "An algorithm for fitting generalized linear models" in <i>Generalized Linear Models,<i> 2nd ed. pp.34."
     */
    public double[] deviances() {
        final int nObs = problem.nObs();
        final GLMProblem p = (GLMProblem) problem;

        double[] deviances = new double[nObs];
        for (int i = 1; i <= nObs; ++i) {
            deviances[i - 1] = p.family.deviance(problem.y.get(i), fitted.get(i));
        }

        return deviances;
    }
}
