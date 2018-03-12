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
package com.numericalmethod.suanshu.analysis.function.polynomial.root.jenkinstraub;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.ScaledPolynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.root.LinearRoot;
import com.numericalmethod.suanshu.analysis.function.polynomial.root.QuadraticRoot;
import java.util.ArrayList;
import java.util.List;

/**
 * The Jenkins-Traub algorithm is a fast globally convergent iterative method for solving for polynomial roots.
 *
 * @author Ken Yiu
 * @see
 * <ul>
 * <li>Jenkins, M. A. and Traub, J. F. (1970), A Three-Stage Algorithm for Real Polynomials Using QuadraticRoot Iteration, SIAM J. Numer. Anal., 7(4), 545-566.
 * <li>Jenkins, M. A. (1975), Algorithm 493: Zeros of a Real Polynomial, ACM TOMS, 1, 178-189.
 * <li><a href="http://en.wikipedia.org/wiki/Jenkins-Traub_Algorithm_for_Polynomial_Zeros">Wikipedia: Jenkins-Traub algorithm</a>
 * </ul>
 */
// TODO: configurable program constants, e.g., no. of steps in each stage
public class JenkinsTraubReal {

    private final LinearRoot linear = new LinearRoot();//the built-in linear equation solver
    private final QuadraticRoot quadratic = new QuadraticRoot();//the built-in quadratic equation solver

    /**
     * Solve a polynomial equation.
     *
     * @param polynomial a polynomial equation to be solved
     * @return the roots of the polynomial equation
     */
    public List<Number> solve(Polynomial polynomial) {
        List<Number> roots = new ArrayList<Number>();// all zeros found in the process are saved in this list

        Polynomial pPoly = polynomial;
        Polynomial kPoly = null;

        // 1) remove zeros at the origin and deflate the polynomial
        JTStep step = new JTRemoveOriginZeros(pPoly);
        roots.addAll(step.getZeros());
        pPoly = step.getDeflatedPolynomial();

        // 2) main loop to find zeros and deflate the current polynomial until its degree drops to 2 or 1.
        while (pPoly.degree() > 2) {
            // 2.1) scale the polynomial to avoid overflow or underflow
            pPoly = new ScaledPolynomial(pPoly);

            // 2.2) no-shift 5 times
            kPoly = new JTNoShift(pPoly, 5).kPoly;

            // 2.3) fixed-shift at most 20 times (which would jump into variable-shift stage)
            step = new JTFixedShift(pPoly, kPoly, 20);
            roots.addAll(step.getZeros());
            pPoly = step.getDeflatedPolynomial();
        }

        // 3) calculate the final zero or pair of zeros for the remaining simple polynomial which has degree 2 or 1 only
        switch (pPoly.degree()) {
            case 0: // constant
                break;
            case 1: // linear polynomial
                roots.addAll(linear.solve(pPoly));
                break;
            case 2: // quadratic polynomial
                roots.addAll(quadratic.solve(pPoly));
                break;
            default:
                throw new RuntimeException("invalid degree: " + pPoly.degree());
        }

        return roots;
    }
}
