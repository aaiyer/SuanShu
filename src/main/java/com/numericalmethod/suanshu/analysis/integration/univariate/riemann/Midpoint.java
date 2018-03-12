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

/**
 * The midpoint rule computes an approximation to a definite integral,
 * made by finding the area of a collection of rectangles whose heights are determined by the values of the function.
 * Specifically, the interval over which the function is to be integrated is divided into equal subintervals of length.
 * The rectangles are then drawn.
 * The approximation to the integral is then calculated by adding up the areas (base multiplied by height) of the rectangles.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Rectangle_rule">Wikipedia: Rectangle method</a>
 */
public class Midpoint extends NewtonCotes {

    /**
     * Construct an integrator that implements the Midpoint rule.
     *
     * @param precision     the convergence threshold
     * @param maxIterations the maximum number of iterations
     */
    public Midpoint(double precision, int maxIterations) {
        super(3, NewtonCotes.Type.OPEN, precision, maxIterations);
    }
}
