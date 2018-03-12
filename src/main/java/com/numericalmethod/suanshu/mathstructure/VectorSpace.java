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
 * A vector space is a set <i>V</i> together with two binary operations that combine two entities to yield a third,
 * called vector addition and scalar multiplication.
 *
 * @param <V> a vector space
 * @param <F> a field
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Vector_space">Wikipedia: Vector space</a>
 */
public interface VectorSpace<V, F extends Field<F>> extends AbelianGroup<V> {

    /**
     * <i>× : F × V → V</i>
     * <p/>
     * The result of applying this function to a scalar, <i>c</i>, in <i>F</i> and <i>v</i> in <i>V</i> is denoted <i>cv</i>.
     *
     * @param c a multiplier
     * @return <i>c * this</i>
     * @see <a href="http://en.wikipedia.org/wiki/Scalar_multiplication">Wikipedia: Scalar multiplication</a>
     */
    public V scaled(F c);
}
