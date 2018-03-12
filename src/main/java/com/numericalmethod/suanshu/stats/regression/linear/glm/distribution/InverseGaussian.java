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
package com.numericalmethod.suanshu.stats.regression.linear.glm.distribution;

import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.InverseSquared;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.*;

/**
 * The Inverse Gaussian distribution for the error distribution in a GLM model.
 *
 * <p>
 * The R equivalent function is {@code inverse.gaussian}.
 *
 * @author Ken Yiu
 */
public class InverseGaussian extends Family {

    /**
     * Construct an instance of {@code InverseGaussian}.
     * The canonical link is {@link InverseSquared}.
     *
     * @see "pp.32. Section 2.2.4. Measuring the goodness-of-fit. Generalized Linear Models. 2nd ed. P. J. MacCullagh and J. A. Nelder."
     */
    public InverseGaussian() {
        this(new InverseSquared());
    }

    /**
     * Construct an instance of {@code InverseGaussian} with an overriding link function.
     * 
     * @param link the overriding link function
     */
    public InverseGaussian(LinkFunction link) {
        super(link);
    }

    @Override
    public double variance(double mu) {
        return mu * mu * mu;
    }

    @Override
    public double theta(double mu) {
        return -1 / (2 * mu * mu);//note: there is a typo in the Table 2.1. for the Canonoical link for Gamma; we use μ = (-2θ)^-0.5 to derive this code
    }

    @Override
    public double cumulant(double theta) {
        return -sqrt(-2 * theta);
    }

    @Override
    public double deviance(double y, double mu) {
        double ymu = y - mu;
        return (ymu * ymu) / (y * mu * mu);
    }

    @Override
    public double overdispersion(Vector y, Vector mu, int nFactors) {
        final int nObs = y.size();

        double result = 0.;
        for (int i = 1; i <= nObs; ++i) {
            result += pow((y.get(i) - mu.get(i)) / pow(mu.get(i), 1.5), 2);
        }
        result /= (nObs - nFactors);

        return result;
    }

    @Override
    public double dispersion(Vector y, Vector mu, int nFactors) {
        return overdispersion(y, mu, nFactors);
    }

    @Override
    public double AIC(Vector y, Vector mu, Vector weight, double prelogLike, double deviance, int nFactors) {
        int nObs = y.size();
        double dispersion = deviance / nObs;
        double logLike = prelogLike / dispersion - nObs * log(2 * PI * dispersion) / 2.;
        for (int i = 1; i <= nObs; ++i) {
            logLike += -(3 * log(y.get(i)) + 1. / (dispersion * y.get(i))) / 2.;
        }

        return 2 * (nFactors + 1 - logLike);
    }
}
