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
package com.numericalmethod.suanshu.analysis.function.rn2r1.univariate;

import static com.numericalmethod.suanshu.Constant.EPSILON;
import java.math.BigDecimal;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import java.math.BigInteger;

/**
 * A continued fraction representation of a number has this form:
 * \[
 * z = b_0 + \cfrac{a_1}{b_1 + \cfrac{a_2}{b_2 + \cfrac{a_3}{b_3 + \cfrac{a_4}{b_4 + \ddots\,}}}}
 * \]
 * <i>a<sub>i</sub></i> and <i>b<sub>i</sub></i> can be functions of <i>x</i>, which in turn makes <i>z</i> a function of <i>x</i>.
 * <p/>
 * The sequence <i>z<sub>n</sub></i> may or may not converge.
 * In theory,
 * <i>z<sub>n</sub></i> can be written as a fraction: \(z_n = \frac{A_n}{B_n}\).
 * <i>A<sub>n</sub></i> and <i>B<sub>n</sub></i> can be computed by the fundamental recurrence formulas.
 * In practice, we compute <i>z<sub>n</sub></i> using the modified Lentz's method from Thompson and Barnett.
 * This method may suffer from the "false convergence" problem.
 * That is, differences between successive convergents become small, seeming to indicate convergence,
 * but then increase again by many orders of magnitude before finally converging.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"I. J. Thompson, A. R. Barnett, "Coulomb and Bessel functions of complex arguments and order," J. Comput. Phys, 1986."
 * <li>"W. J. Lentz, "Generating Bessel Functions In Mie Scattering Calculations Using Continued Fractions," Applied Optics, Vol. 15, Issue 3, p. 668-671."
 * <li>"W. Gautschi, Math. Comput, 31, 994(1997)."
 * <li><a href="http://en.wikipedia.org/wiki/Generalized_continued_fraction">Wikipedia: Generalized continued fraction</a>
 * <li><a href="http://en.wikipedia.org/wiki/Fundamental_recurrence_formulas">Wikipedia: Fundamental recurrence formulas</a>
 * </ul>
 */
public class ContinuedFraction extends UnivariateRealFunction {

    /**
     * {@link RuntimeException} thrown when the continued fraction fails to converge for a given epsilon before a certain number of iterations.
     */
    public static class MaxIterationsExceededException extends RuntimeException {

        private static final long serialVersionUID = 1L;
        private final int nIterations;

        /**
         * Construct a new {@code MaxIterationsExceededException}, indicating the number of iterations.
         *
         * @param nIterations the number of iterations
         */
        public MaxIterationsExceededException(int nIterations) {
            super(String.format("maximum number of iterations %d exceeded", nIterations));
            this.nIterations = nIterations;
        }

        /**
         * Get the maximum number of iterations.
         *
         * @return the maximum number of iterations
         */
        public int getMaxIterations() {
            return nIterations;
        }
    }

    /**
     * This interface defines a continued fraction in terms of
     * the partial numerators <i>a<sub>n</sub></i>, and the partial denominators <i>b<sub>n</sub></i>.
     * \[
     * z = b_0 + \cfrac{a_1}{b_1 + \cfrac{a_2}{b_2 + \cfrac{a_3}{b_3 + \cfrac{a_4}{b_4 + \ddots\,}}}}
     * \]
     */
    public static interface Partials {

        /**
         * Compute <i>a<sub>n</sub></i>.
         *
         * @param n an index that counts from 1
         * @param x <i>x</i>
         * @return <i>a<sub>n</sub></i>
         */
        double A(int n, double x);

        /**
         * Compute <i>b<sub>n</sub></i>.
         *
         * @param n an index that counts from 0
         * @param x <i>x</i>
         * @return <i>b<sub>n</sub></i>
         */
        double B(int n, double x);
    }

    private final Partials partials;
    private final double epsilon;
    private final int maxIterations;
    private final int scale;

    /**
     * Construct a continued fraction.
     *
     * @param partials      the definition in terms of partial numerators and partial denominators
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public ContinuedFraction(Partials partials, double epsilon, int maxIterations) {
        this.partials = partials;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
        this.scale = 0;//indicate whether to use BigDecimal or not
    }

    /**
     * Construct a continued fraction.
     *
     * @param partials      the definition in terms of partial numerators and partial denominators
     * @param scale         the accuracy
     * @param maxIterations the maximum number of iterations
     */
    public ContinuedFraction(Partials partials, int scale, int maxIterations) {
        this.partials = partials;
        this.epsilon = new BigDecimal(BigInteger.ONE, scale).doubleValue();
        this.maxIterations = maxIterations;
        this.scale = scale;
    }

    /**
     * Construct a continued fraction.
     *
     * @param partials the definition in terms of partial numerators and partial denominators
     */
    public ContinuedFraction(Partials partials) {
        this(partials, EPSILON, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     *
     * This implementation adopts the modified Lentz's method, using {@code double} arithmetics.
     * It is quick.
     * However, the precision is limited by the double precision of the intermediate results.
     * This (and probably other implementations using double precision math) may give poor results for some continued fraction.
     *
     * @param x <i>x</i>
     * @return an <em>approximation</em> of <i>z</i>
     * @throws MaxIterationsExceededException if it does not converge before the maximum number of iterations;
     * repeat with a bigger epsilon, or use the BigDecimal version of the algorithm
     */
    @Override
    public double evaluate(double x) {//TODO: implement a version in BigDecimal?
        double small = 1e-200;//should be < epsilon * |bn|
        double b0 = partials.B(0, x);
        double[] z = new double[]{b0 != 0 ? b0 : small, 0};
        double[] D = new double[]{0, 0};
        double[] C = new double[]{z[0], 0};

        for (int i = 0; i < maxIterations; ++i) {
            double bii = partials.B(i + 1, x);
            double aii = partials.A(i + 1, x);

            D[1] = bii + aii * D[0];
            D[1] = D[1] == 0 ? small : D[1];
            D[1] = 1d / D[1];

            C[1] = bii + aii / C[0];
            C[1] = C[1] == 0 ? small : C[1];

            double delta = D[1] * C[1];
            z[1] = z[0] * delta;

            if (Math.abs(delta - 1d) < epsilon) {//|delta - 1| ~ 0
                return z[1];
            }

            //play musical chair to prepare for the next iteration
            z[0] = z[1];
            D[0] = D[1];
            C[0] = C[1];
        }

        throw new MaxIterationsExceededException(maxIterations);
    }

    /**
     * Evaluate <i>z</i>.
     *
     * This implementation adopts the modified Lentz's method using arbitrary precision arithmetics {@link java.math.BigDecimal}.
     *
     * @param x <i>x</i>
     * @return an <em>approximation</em> of <i>z</i>
     * @throws MaxIterationsExceededException if it does not converge before the maximum number of iterations; repeat with a bigger epsilon
     */
    public BigDecimal evaluate(BigDecimal x) {
        final double u = x.doubleValue();
        final BigDecimal epsilon = BigDecimal.valueOf(this.epsilon);

        BigDecimal small = BigDecimal.valueOf(1e-50);
        BigDecimal b0 = BigDecimal.valueOf(partials.B(0, u));
        BigDecimal[] z = new BigDecimal[]{b0.compareTo(ZERO) == 0 ? small : b0, ZERO};
        BigDecimal[] D = new BigDecimal[]{ZERO, ZERO};
        BigDecimal[] C = new BigDecimal[]{z[0], ZERO};

        for (int i = 0; i < maxIterations; ++i) {
            BigDecimal bii = BigDecimal.valueOf(partials.B(i + 1, u));
            BigDecimal aii = BigDecimal.valueOf(partials.A(i + 1, u));

            D[1] = bii.add(aii.multiply(D[0]));
            D[1] = D[1].compareTo(ZERO) == 0 ? small : D[1];
            D[1] = ONE.divide(D[1], scale, BigDecimal.ROUND_HALF_EVEN);

            C[1] = bii.add(aii.divide(C[0], scale, BigDecimal.ROUND_HALF_EVEN));
            C[1] = C[1].compareTo(ZERO) == 0 ? small : C[1];

            BigDecimal delta = D[1].multiply(C[1]);
            z[1] = z[0].multiply(delta);

            if (delta.add(ONE.negate()).abs().compareTo(epsilon) < 0) {//|delta - 1| ~ 0
                return z[1].setScale(scale, BigDecimal.ROUND_HALF_EVEN);
            }

            //play musical chair to prepare for the next iteration
            z[0] = z[1];
            D[0] = D[1];
            C[0] = C[1];
        }

        throw new MaxIterationsExceededException(maxIterations);
    }
}
