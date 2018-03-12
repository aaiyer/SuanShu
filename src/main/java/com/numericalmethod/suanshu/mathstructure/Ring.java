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
 * A ring is a set <i>R</i> equipped with two binary operations called addition and multiplication:
 * <blockquote><code>+ : R × R → R</code></blockquote> and
 * <blockquote><code>· : R × R → R</code></blockquote>
 * To qualify as a ring, the set and two operations, <i>(R, +, ⋅)</i>, must satisfy the requirements known as the ring axioms.
 *
 * @param <R> a ring
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Ring_(mathematics)">Wikipedia: Ring (mathematics)</a>
 */
public interface Ring<R> extends AbelianGroup<R>, Monoid<R> {
}
