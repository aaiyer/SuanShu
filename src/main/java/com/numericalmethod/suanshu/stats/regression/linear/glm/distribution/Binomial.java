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

import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.LinkFunction;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.link.Logit;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.*;

/**
 * The Binomial distribution for the error distribution in a GLM model.
 *
 * <p>
 * The R equivalent function is {@code binomial}.
 *
 * @author Ken Yiu
 */
public class Binomial extends Family {

    /**
     * the biggest integer that a binomial variable can take
     *
     * <p>
     * E.g.,
     * When m = 1, the binomial variable can be 0 or 1. When m = 3, the variable can be 0, 1, 2, 3.
     */
    private final int m = 1;//TODO: we support m = 1 for now so the observation is either 0 or 1

    /**
     * Construct  an instance of {@code Binomial}.
     * The canonical link is {@link Logit}.
     *
     * @see "pp.32. Section 2.2.4. Measuring the goodness-of-fit. Generalized Linear Models. 2nd ed. P. J. MacCullagh and J. A. Nelder."
     */
    public Binomial() {
        this(new Logit());
    }

    /**
     * Construct an instance of {@code Binomial} with an overriding link function.
     * 
     * @param link the overriding link function
     */
    public Binomial(LinkFunction link) {
        super(link);
    }

    @Override
    public double variance(double mu) {
        return mu * (1 - mu);
    }

    /**
     * {@inheritDoc}
     *
     * @see "logit. p.31. Chapter 2. Generalized Linear Models, Second edition. P. MuCullage and J. A. Nelder"
     */
    @Override
    public double theta(double mu) {
        return log(abs(mu / (1 - mu)));//TODO: add 'abs' to avoid throwing exception for an invalid argument for log
    }

    @Override
    public double cumulant(double theta) {
        return log(1 + exp(theta));
    }

    @Override
    public double deviance(double y, double mu) {
        return 2 * (ylogymu(y, mu) + ylogymu(m - y, m - mu));

    }

    /**
     * y * log(|y/μ|)
     *
     * @param y
     * @param mu
     * @return
     */
    private double ylogymu(double y, double mu) {
        return (DoubleUtils.isZero(y, 0)) ? 0 : y * log(abs(y / mu));//TODO: add 'abs' to avoid throwing exception for an invalid argument for log
    }

    @Override
    public double overdispersion(Vector y, Vector mu, int nFactors) {
        final int nObs = y.size();

        double result = 0.;
        for (int i = 1; i <= nObs; ++i) {
            result += pow(y.get(i) - mu.get(i), 2) / variance(mu.get(i));
        }
        result /= (nObs - nFactors);

        return result;
    }

    @Override
    public double dispersion(Vector y, Vector mu, int nFactors) {
        return 1;
    }

    @Override
    public double AIC(Vector y, Vector mu, Vector weight, double prelogLike, double deviance, int nFactors) {
        double logLike = prelogLike;
        return 2 * (nFactors - logLike);
    }
}
