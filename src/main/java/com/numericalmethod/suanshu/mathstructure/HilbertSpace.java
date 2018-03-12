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
 * A Hilbert space is an inner product space, an abstract vector space in which distances and angles can be measured.
 * It is also "complete", meaning that if a sequence of vectors is Cauchy, then it converges to some limit in the space.
 *
 * @param <H> a Hilbert space
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Hilbert_space">Wikipedia: Hilbert space</a>
 */
public interface HilbertSpace<H, F extends Field<F> & Comparable<F>> extends BanachSpace<H, F> {

    /**
     * <i>&lt;·,·&gt; : H × H → F</i>
     * <p/>
     * Inner product formalizes the geometrical notions such as the length of a vector and the angle between two vectors.
     * It defines orthogonality between two vectors, where their inner product is 0.
     *
     * @param that the object to form an angle with {@code this}
     * @return <i>&lt;this,that&gt;</i>
     */
    public double innerProduct(H that);

    /**
     * <i>&ang; : H × H → F</i>
     * <p/>
     * Inner product formalizes the geometrical notions such as the length of a vector and the angle between two vectors.
     * It defines orthogonality between two vectors, where their inner product is 0.
     *
     * @param that the object to form an angle with this
     * @return the angle between {@code this} and {@code that}
     */
    public double angle(H that);
}
