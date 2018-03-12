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

import static com.numericalmethod.suanshu.Constant.EPSILON;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.integration.univariate.riemann.substitution.SubstitutionRule;
import static com.numericalmethod.suanshu.number.DoubleUtils.isNumber;
import static java.lang.Math.min;

/**
 * This is a wrapper class that integrates a function by using an appropriate integrator together with Romberg's method.
 * The integral can be definite or indefinite.
 * For an indefinite integral, it requires the specification of a substitution rule (change of variable).
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Riemann_integral">Wikipedia: Riemann integral</a>
 */
public class Riemann implements Integrator {

    private final int maxIterations;//the maximum number of iterations
    private final double precision;//the convergence threshold

    /**
     * Construct an integrator.
     *
     * @param precision     the convergence threshold
     * @param maxIterations the maximum number of iterations
     */
    public Riemann(double precision, int maxIterations) {
        this.maxIterations = maxIterations;
        this.precision = precision;
    }

    /**
     * Construct an integrator.
     */
    public Riemann() {
        this(EPSILON, 20);//2^20 abscissas
    }

    @Override
    public double integrate(UnivariateRealFunction f, double a, double b) {
        return integrate(f, a, b, null);
    }

    /**
     * Integrate a function, <i>f</i>, from <i>a</i> to <i>b</i> possibly using change of variable.
     *
     * @param f      a univariate function
     * @param a      the lower limit
     * @param b      the upper limit
     * @param change the substitution rule; {@code null} for a definite integral (no singularity)
     * @return \(\int_a^b\! f(x)\, dx\)
     */
    public double integrate(UnivariateRealFunction f, double a, double b, SubstitutionRule change) {
        double ta = a;
        double tb = b;
        UnivariateRealFunction g = f;
        if (change != null) {
            ta = change.ta();
            tb = change.tb();

            ChangeOfVariable cov = new ChangeOfVariable(change, null);
            g = cov.fdx(f);
        }
        boolean aOK = false;
        try {
            double ga = g.evaluate(ta);
            if (isNumber(ga)) {
                aOK = true;
            }
        } catch (Throwable tt) {
            aOK = false;
        }

        boolean bOK = false;
        try {
            double gb = g.evaluate(tb);
            if (isNumber(gb)) {
                bOK = true;
            }
        } catch (Throwable tt) {
            bOK = false;
        }

        IterativeIntegrator integrator;
        if (aOK && bOK) {
            integrator = new Simpson(precision, min(maxIterations, 20));
        } else if (change != null) {
            integrator = new NewtonCotes(3, NewtonCotes.Type.OPEN, precision, min(maxIterations, 15));
        } else {
            throw new IllegalArgumentException("cannot evaluate integral at endpoints; consider using change of variable");
        }

        Romberg romberg = new Romberg(integrator);

        double result = romberg.integrate(g, ta, tb);
        return result;
    }

    @Override
    public double getPrecision() {
        return precision;
    }
}
