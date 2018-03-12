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
 * A Banach space, <i>B</i>, is a complete normed vector space such that
 * every Cauchy sequence (with respect to the metric <i>d(x, y) = |x − y|</i>) in <i>B</i> has a limit in <i>B</i>.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Banach_space">Wikipedia: Banach space</a>
 */
public interface BanachSpace<B, F extends Field<F> & Comparable<F>> extends VectorSpace<B, F> {

    /**
     * <i>|·| : B → F</i>
     * <p/>
     * {@code norm} assigns a strictly positive length or size to all vectors in the vector space,
     * other than the zero vector.
     *
     * @return <i>|this|</i>
     * @see <a href="http://en.wikipedia.org/wiki/Norm_(mathematics)">Wikipedia: Norm (mathematics)</a>
     */
    public double norm();
}
