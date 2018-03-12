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
 * An Abelian group is a group with a binary additive operation (<i>+</i>), satisfying the group axioms:
 * <ol>
 * <li>closure
 * <li>associativity
 * <li>existence of additive identity
 * <li>existence of additive opposite
 * <li>commutativity of addition
 * </ol>
 *
 * @param <G> an Abelian group
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Abelian_group">Wikipedia: Abelian group</a>
 */
public interface AbelianGroup<G> {

    /**
     * <i>+ : G × G → G</i>
     *
     * @param that the object to be added
     * @return <i>this + that</i>
     */
    public G add(G that);

    /**
     * <i>- : G × G → G</i>
     * <p/>
     * The operation "<i>-</i>" is not in the definition of of an additive group but can be deduced.
     * This function is provided for convenience purpose. It is equivalent to
     * <blockquote><code>this.add(that.opposite())</code></blockquote>.
     *
     * @param that the object to be subtracted (subtrahend)
     * @return <i>this - that</i>
     */
    public G minus(G that);

    /**
     * For each <i>a</i> in <i>G</i>, there exists an element <i>b</i> in <i>G</i> such that
     * <i>a + b = b + a = 0</i>.
     * That is, it is the object such as
     * <blockquote><code>this.add(this.opposite()) == this.ZERO</code></blockquote>
     *
     * @return <i>-this</i>, the additive opposite
     * @see <a href="http://en.wikipedia.org/wiki/Additive_inverse"> Wikipedia: Additive inverse</a>
     */
    public G opposite();

    /**
     * The additive element 0 in the group, such that for all elements <i>a</i> in the group,
     * the equation <i>0 + a = a + 0 = a</i> holds.
     *
     * @return <i>0</i>, the additive identity
     */
    public G ZERO();
}
