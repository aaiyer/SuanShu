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
import com.numericalmethod.suanshu.analysis.function.polynomial.root.jenkinstraub.JenkinsTraubReal;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.NumberUtils.isReal;
import com.numericalmethod.suanshu.number.complex.Complex;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a solver for finding the roots of a polynomial equation.
 * A root (or a zero) of a polynomial <i>p</i> is a member <i>x</i> in the domain of <i>p</i> such that <i>p(x)</i> vanishes.
 * That is, <i>p(x) = 0</i>.
 * By the fundamental theorem of algebra, every polynomial of degree <i>n</i> has <i>n</i> roots.
 * The roots can be both real ({@code double}) or {@link Complex}.
 * The Abel–Ruffini theorem says that we have analytical solution for polynomials of degree up to 4.
 * <p/>
 * This implementation solves a polynomial of degree 1 by {@link LinearRoot}, degree 2 by {@link QuadraticRoot}, degree 3 by {@link CubicRoot},
 * degree 4 by {@link QuarticRoot}, and others by the Jenkins-Traub algorithm {@link JenkinsTraubReal}.
 *
 * @author Haksun Li, Ken Yiu
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Root_(mathematics)">Wikipedia: PolyRoot (mathematics)</a>
 * <li><a href="http://en.wikipedia.org/wiki/Fundamental_theorem_of_algebra">Wikipedia: Fundamental theorem of algebra</a>
 * <li><a href="http://en.wikipedia.org/wiki/Abel-Ruffini_theorem">Wikipedia: Abel–Ruffini theorem</a>
 * <li><a href="http://en.wikipedia.org/wiki/Jenkins-Traub_algorithm">Wikipedia: Jenkins-Traub algorithm</a>
 * </ul>
 */
public class PolyRoot implements PolyRootSolver {

    private final LinearRoot p1 = new LinearRoot();
    private final QuadraticRoot p2 = new QuadraticRoot();
    private final CubicRoot p3 = new CubicRoot();
    private final QuarticRoot p4 = new QuarticRoot();
    private final JenkinsTraubReal poly = new JenkinsTraubReal();

    /**
     * Get the roots/zeros of a polynomial.
     *
     * @param polynomial the polynomial to be solved
     * @return the roots of the polynomial
     * @throws IllegalArgumentException if the polynomial is a constant, hence no solution
     */
    @Override
    public List<? extends Number> solve(Polynomial polynomial) {
        SuanShuUtils.assertArgument(polynomial.degree() != 0, "this polynomial is a constant %f", polynomial.getCoefficient(0));

        int degree = polynomial.degree();
        switch (degree) {
            case 1:
                return p1.solve(polynomial);
            case 2:
                return p2.solve(polynomial);
            case 3:
                return p3.solve(polynomial);
            case 4:
                return p4.solve(polynomial);
            default:
                return poly.solve(polynomial);
        }
    }

    /**
     * Get a copy of only the real roots of a polynomial.
     * The union of these and {@link #getComplexRoots(java.util.List)} are all the roots.
     *
     * @param roots all the roots of a polynomial
     * @return a copy of the real roots
     */
    public static List<Double> getRealRoots(List<? extends Number> roots) {
        List<Double> realRoots = new ArrayList<Double>();
        for (Number number : roots) {
            if (isReal(number)) {
                realRoots.add((Double) number);
            }
        }

        return realRoots;
    }

    /**
     * Get a copy of only the {@link Complex} but not real roots of a polynomial.
     * The union of these and {@link #getRealRoots(java.util.List)} are all the roots.
     *
     * @param roots all the roots of a polynomial
     * @return a copy of the {@link Complex} roots
     */
    public static List<Complex> getComplexRoots(List<? extends Number> roots) {
        List<Complex> complexRoots = new ArrayList<Complex>();
        for (Number number : roots) {
            if (!isReal(number)) {
                complexRoots.add((Complex) number);
            }
        }

        return complexRoots;
    }
}
