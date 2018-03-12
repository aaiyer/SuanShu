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
package com.numericalmethod.suanshu.analysis.uniroot;

/**
 * This is the {@link Exception} thrown when it fails to find a root.
 * It may contain some information about the state before it throws the exception.
 * It may be be useful in situations where an (approximate) root is needed at the sacrifice of accuracy.
 *
 * @author Haksun Li
 */
public class NoRootFoundException extends Exception {

    private final double x;
    private final double fx;
    private static final long serialVersionUID = 1L;

    /**
     * Construct a {@code NoRootFoundException}.
     * This object gives a snapshot/information of the uniroot algorithm before it throws the exception.
     *
     * @param x  the best approximate root found before throwing exception
     * @param fx the function value <i>f(x)</i>
     */
    public NoRootFoundException(double x, double fx) {
        this.x = x;
        this.fx = fx;
    }

    /**
     * the best approximate root found so far
     *
     * @return a root
     */
    public double x() {
        return x;
    }

    /**
     * Get <i>f(x)</i>.
     *
     * @return <i>f(x)</i>
     */
    public double fx() {
        return fx;
    }
}
