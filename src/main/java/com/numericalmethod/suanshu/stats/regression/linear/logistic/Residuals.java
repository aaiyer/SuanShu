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
package com.numericalmethod.suanshu.stats.regression.linear.logistic;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.stats.descriptive.moment.Mean;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Binomial;
import com.numericalmethod.suanshu.stats.regression.linear.glm.distribution.Family;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;

/**
 * Residual analysis of the results of a logistic regression.
 *
 * @author Haksun Li
 */
public class Residuals extends com.numericalmethod.suanshu.stats.regression.linear.Residuals {

    /**
     * the residuals, ε
     */
    public final ImmutableVector devianceResiduals;
    /**
     * the null deviance
     */
    public final double nullDeviance;
    /**
     * the residual deviance
     */
    public final double deviance;

    /**
     * Perform the residual analysis for a logistic problem.
     * 
     * @param problem the logistic problem to be solved
     * @param betaHat β^
     */
    Residuals(final LogisticProblem problem, final Vector betaHat) {
        super(problem, logistic(problem.A.multiply(betaHat)));//fitted values = exp(t(A)beta) / (1 + exp(t(A)beta))

        final int m = problem.nFactors();
        final int n = problem.y.size();

        //the log-likelihood function
        RealScalarFunction L = new LogLikelihood().function((LogisticProblem) this.problem);
        double ML = L.evaluate(betaHat);//TODO: computed already in Logistic

        deviance = - 2 * (ML);//p.118

        /*
         * The maximum attainable log-likelihood is 0 at fitted=y.
         *
         * The null deviance is computed as follows.
         *
         * y_{i} ~ e^b/(1+e^b)
         * i.e., without X variables, only intercept
         * MLE of Bernoulli's distribution gives that the estimate of e^b/(1+e^b) equals to mean(y).
         * So, b = log(mean(y) / (1 - mean(y)).
         */
        double yMean = new Mean(problem.y.toArray()).value();
        double nullbeta = log(yMean / (1 - yMean));
        double[] init = R.rep(0., m);
        init[m - 1] = nullbeta;
        nullDeviance = -2 * (L.evaluate(new DenseVector(init)));

        //@see "P. J. MacCullagh and J. A. Nelder. "An algorithm for fitting generalized linear models," in <i>Generalized Linear Models,<i> 2nd ed. pp.34."
        final Family instance = new Binomial();
        double[] devianceResidualsArr = new double[n];
        for (int i = 1; i <= n; ++i) {
            double y_i = problem.y.get(i);
            double fitted_i = fitted.get(i);
            double deviance_i = instance.deviance(y_i, fitted_i);
            devianceResidualsArr[i - 1] = y_i > fitted_i ? sqrt(deviance_i) : -sqrt(deviance_i);
        }
        devianceResiduals = new ImmutableVector(new DenseVector(devianceResidualsArr));
    }

    /**
     * This is the logistic function on vector-values.
     *
     * <p>
     * It applies the univariate logistic function to each entry in the vector one-by-one.
     *
     * @param v a vector
     * @return <i>f(v)</i>
     */
    private static Vector logistic(Vector v) {
        return com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.foreach(v, new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
//                return exp(x) / (1 + exp(x));
                return 1 / (1 + exp(-x));
            }
        });
    }
}
