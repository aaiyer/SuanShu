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
package com.numericalmethod.suanshu;

/**
 * This interface provides a way to do polymorphic copying.
 * <p/>
 * An interface (a class) that extends (implements) this interface should
 * override the return type of {@link #deepCopy()} in the interface (class)
 * itself to provide as much information as possible to avoid unnecessary casting.
 * <p/>
 * For example,
 * <pre><code>
 * public interface Matrix extends DeepCopyable {
 *     ...
 *     Matrix deepCopy();
 * }
 * </code></pre>
 *
 * @author Ken Yiu
 */
public interface DeepCopyable {

    /**
     * The implementation returns an instance created from {@code this} by the copy
     * constructor of the class, or just {@code this} if the instance itself is
     * <em>immutable</em>.
     *
     * @return an independent (deep) copy of the instance
     */
    Object deepCopy();
}
