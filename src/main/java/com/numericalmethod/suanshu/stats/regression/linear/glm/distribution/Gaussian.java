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

import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Identity;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.*;

/**
 * The Gaussian distribution for the error distribution in a GLM model.
 *
 * <p>
 * The R equivalent function is {@code gaussian}.
 *
 * @author Ken Yiu
 */
public class Gaussian extends Family {

    /**
     * Construct an instance of {@code Gaussian}.
     * The canonical link is {@link Identity}.
     *
     * @see "pp.32. Section 2.2.4. Measuring the goodness-of-fit. Generalized Linear Models. 2nd ed. P. J. MacCullagh and J. A. Nelder."
     */
    public Gaussian() {
        this(new Identity());
    }

    /**
     * Construct an instance of {@code Gaussian} with an overriding link function.
     * 
     * @param link the overriding link function
     */
    public Gaussian(LinkFunction link) {
        super(link);
    }

    @Override
    public double variance(double mu) {
        return 1;
    }

    @Override
    public double theta(double mu) {
        return mu;
    }

    @Override
    public double cumulant(double theta) {
        return (theta * theta) / 2.;
    }

    @Override
    public double deviance(double y, double mu) {
        double x = y - mu;
        return x * x;
    }

    @Override
    public double overdispersion(Vector y, Vector mu, int nFactors) {
        final int nObs = y.size();

        double result = 0.;
        for (int i = 1; i <= nObs; ++i) {
            result += pow(y.get(i) - mu.get(i), 2);
        }
        result /= (nObs - nFactors);

        return result;
    }

    @Override
    public double dispersion(Vector y, Vector mu, int nFactors) {
        return overdispersion(y, mu, nFactors);
    }

    @Override
    public double AIC(Vector y, Vector mu, Vector weight, double preLogLike, double deviance, int nFactors) {
        int nObs = y.size();
        double dispersion = deviance / nObs; // the dispersion parameter of a Gaussian = the variance
        double logLike = preLogLike / dispersion - nObs * log(2 * PI * dispersion) / 2; // log of the 1/sqrt(2*pi*variance) factor of the Gaussian
        for (int i = 1; i <= nObs; ++i) {
            logLike += -0.5 * pow(y.get(i), 2) / dispersion; // the c(y) part of Gaussian family
        }

        return 2 * (nFactors + 1 - logLike);
    }
}
