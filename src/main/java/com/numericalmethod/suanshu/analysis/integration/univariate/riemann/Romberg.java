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
package com.numericalmethod.suanshu.analysis.integration.univariate.riemann;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.tuple.BinaryRelation;
import com.numericalmethod.suanshu.analysis.interpolation.NevilleTable;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.relativeError;

/**
 * Romberg's method computes an integral by generating a sequence of estimations of the integral value and then doing an extrapolation.
 * The estimations are extrapolated to where the discretization is 0.
 * Simpson's rule is a special case of Romberg's method, with the number of points for extrapolation equal to 2.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Romberg_integration">Wikipedia: Romberg's method</a>
 */
public class Romberg implements Integrator {//TODO: is this really the Romberg formula, or just Neville?

    /** the iterative integrator */
    private IterativeIntegrator integrator;
    /** the extrapolation formula */
    private final NevilleTable neville;

    /**
     * Extend an integrator using Romberg's method.
     *
     * @param integrator an iterative integrator that must do at least 2 iterations
     */
    public Romberg(IterativeIntegrator integrator) {
        SuanShuUtils.assertArgument(integrator.getMaxIterations() >= 2, "the number of iterations must be > 2 for extrapolation");

        this.integrator = integrator;
        neville = new NevilleTable();
    }

    @Override
    public double integrate(UnivariateRealFunction f, double a, double b) {
        double sum = 0;
        double sum0 = 0, sum1 = 0;

        for (int i = 1; i <= integrator.getMaxIterations(); ++i) {
            sum = integrator.next(i, f, a, b, sum);
            double h = integrator.h();

            sum1 = addPointToExtrapolation(h, sum);//sum1 = an extrapolated sum
            sum0 = getLastExtrapolationPoint();

            if ((i > 3)//avoid spurious convergence by doing at least a few iterations; TODO: why 3?
                && relativeError(sum1, sum0) < getPrecision()) {//the convergence criterion
                break;//converged
            }
        }

        return sum1;
    }

    @Override
    public double getPrecision() {
        return integrator.getPrecision();
    }

    /** Extrapolate the integral value with a newly added item. */
    private double addPointToExtrapolation(double h, double sum) {
        neville.addData(new BinaryRelation(new double[]{h}, new double[]{sum}));

        double result = Double.NaN;//Double.NaN is > Double.POSITIVE_INFINITY
        if (neville.N() >= 2) {
            result = neville.evaluate(0);
        }
        return result;
    }

    /** Get the last extrapolated sum. */
    private double getLastExtrapolationPoint() {
        final int N = neville.N();
        double result = Double.NaN;//Double.NaN is > Double.POSITIVE_INFINITY
        if (N >= 2) {
            result = neville.get(0, N - 2);
        }
        return result;
    }
}
