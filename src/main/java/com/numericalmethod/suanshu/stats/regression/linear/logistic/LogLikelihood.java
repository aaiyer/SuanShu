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

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;

/**
 * This is the log-likelihood function for binary data.
 *
 * <p>
 * This implementation assumes m<sub>i</sub> = 1 for all <i>i</i>.
 *
 * @author Haksun Li
 *
 * @see "P. J. MacCullagh and J. A. Nelder, "Eq. 4.13, Likelihood functions for binary data," in <i>Generalized Linear Models</i>, 2nd ed. pp. 115."
 */
class LogLikelihood {

    /**
     * Construct the log-likelihood function for a logistic regression problem.
     * 
     * @param problem a logistic regression problem
     * @return the log-likelihood function
     */
    RealScalarFunction function(final LogisticProblem problem) {
        return new RealScalarFunction() {

            @Override
            public Double evaluate(Vector b) {
                Vector Ab = problem.A.multiply(b);

                double result = 0.;
                for (int i = 1; i <= problem.nObs(); ++i) {
                    result += problem.y.get(i) * Ab.get(i) - log(1 + exp(Ab.get(i)));
                }

                return result;
            }

            @Override
            public int dimensionOfDomain() {
                return problem.nFactors();
            }

            @Override
            public int dimensionOfRange() {
                return 1;
            }
        };
    }
}
