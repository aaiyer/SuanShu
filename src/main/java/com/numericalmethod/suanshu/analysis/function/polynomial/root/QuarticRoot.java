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
package com.numericalmethod.suanshu.analysis.function.polynomial.root;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import java.util.List;

/**
 * This is a quartic equation solver that solves \(ax^4 + bx^3 + cx^2 + dx + e = 0\).
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Quartic_function">Wikipedia: QuarticRoot equation</a>
 */
public class QuarticRoot implements PolyRootSolver {

    /** This defines a quartic equation solver. */
    public static interface QuarticSolver {

        /**
         * Solve \(ax^4 + bx^3 + cx^2 + dx + e = 0\).
         *
         * @param a <i>a</i>
         * @param b <i>b</i>
         * @param c <i>c</i>
         * @param d <i>d</i>
         * @param e <i>e</i>
         * @return the list of roots
         */
        public List<Number> solve(double a, double b, double c, double d, double e);
    }

    private final QuarticSolver solver;

    /**
     * Construct a quartic equation solver.
     *
     * @param solver the implementation of a {@code QuarticSolver}
     */
    public QuarticRoot(QuarticSolver solver) {
        this.solver = solver;
    }

    /**
     * Construct a quartic equation solver.
     */
    public QuarticRoot() {
        this(new QuarticRootFormula());
    }

    /**
     * Solve \(ax^4 + bx^3 + cx^2 + dx + e = 0\).
     *
     * @param polynomial a quartic equation to be solved
     * @return the roots of the quartic equation
     * @throws IllegalArgumentException if the polynomial degree is not 4
     */
    @Override
    public List<Number> solve(Polynomial polynomial) {
        SuanShuUtils.assertArgument(polynomial.degree() == 4, "polynomial is not of degree 4");

        double[] coefficients = polynomial.getCoefficients();
        return solver.solve(
                coefficients[0],
                coefficients[1],
                coefficients[2],
                coefficients[3],
                coefficients[4]);
    }
}
