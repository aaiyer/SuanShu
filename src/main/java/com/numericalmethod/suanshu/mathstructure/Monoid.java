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
package com.numericalmethod.suanshu.mathstructure;

/**
 * A monoid is a group with a binary operation (<i>×</i>), satisfying the group axioms:
 * <ol>
 * <li>closure
 * <li>associativity
 * <li>existence of multiplicative identity
 * </ol>
 *
 * @param <G> a monoid
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Monoid">Wikipedia: Monoid</a>
 */
public interface Monoid<G> {

    /**
     * <i>× : G × G → G</i>
     *
     * @param that the multiplicand
     * @return <i>this × that</i>
     */
    public G multiply(G that);

    /**
     * The multiplicative element 1 in the group such that for any elements <i>a</i> in the group,
     * the equation <i>1 × a = a × 1 = a</i> holds.
     *
     * @return 1
     */
    public G ONE();
}
