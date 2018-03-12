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
package com.numericalmethod.suanshu.analysis.sequence;

/**
 * A sequence is an ordered list of (real) numbers.
 * Although some sequences, e.g., Fibonacci, have infinitely many elements,
 * we can, in practice, compute only finitely many of them.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Sequence">Wikipedia: Sequence</a>
 */
public interface Sequence {

    /**
     * Get the number of <em>computed</em> terms in the sequence.
     *
     * @return the length of sequence
     */
    public int length();

    /**
     * Get the <i>i</i>-th entry in the sequence, counting from 1.
     *
     * @param i an index
     * @return the <i>i</i>-th entry in the sequence
     */
    public double get(int i);

    /**
     * Get a copy of the whole (finite) sequence in {@code double[]}.
     *
     * @return a copy of all the computed terms in the sequence
     */
    public double[] getAll();
}
