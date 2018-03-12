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
import java.util.Arrays;
import java.util.List;

/**
 * This is a solver for finding the roots of a linear equation.
 * A linear equation is an algebraic equation in which
 * each term is either a constant or the product of a constant and (the first power of) a single variable.
 * That is, it has this form: <i>ax + b = 0</i>.
 * The solution is simply
 * <pre><i>x = -b/a</i></pre>
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Linear_equation">Wikipedia: LinearRoot equation</a>
 */
public class LinearRoot implements PolyRootSolver {

    /**
     * Solve <i>ax + b = 0</i>.
     *
     * @param polynomial a linear equation to be solved
     * @return the root of the linear equation
     * @throws IllegalArgumentException if the polynomial degree is not 1
     */
    @Override
    public List<Double> solve(Polynomial polynomial) {
        SuanShuUtils.assertArgument(polynomial.degree() == 1, "a linear solver cannot solve for polynomial with degree %d", polynomial.degree());
        double root = -polynomial.getCoefficient(1) / polynomial.getCoefficient(0);
        return Arrays.asList(new Double(root));
    }
}
