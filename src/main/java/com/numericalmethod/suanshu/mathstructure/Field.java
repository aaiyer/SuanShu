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
 * As an algebraic structure, every field is a ring, but not every ring is a field.
 * That is, it has the notion of addition, subtraction, multiplication, satisfying certain axioms.
 * The most important difference is that a field allows for division (though not division by zero),
 * while a ring may not possess a multiplicative inverse.
 * In addition, the multiplication operation in a field is required to be commutative.
 *
 * @param <F> a field
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Field_mathematics">Wikipedia: Field (mathematics)</a>
 */
public interface Field<F> extends Ring<F> {

    /**
     * This is the exception thrown when the inverse of a field element does not exist.
     */
    public static class InverseNonExistent extends Exception {

        private static final long serialVersionUID = 1L;

        /** Construct an instance of {@code InverseNonExistent} */
        public InverseNonExistent() {
            super("inverse does not exist");
        }
    }

    /**
     * For each <i>a</i> in <i>F</i>, there exists an element <i>b</i> in <i>F</i> such that <i>a × b = b × a = 1</i>.
     * That is, it is the object such as
     * <blockquote><code>this.multiply(this.inverse()) == this.ONE</code></blockquote>
     *
     * @return <i>1 / this</i> if it exists
     * @throws com.numericalmethod.suanshu.mathstructure.Field.InverseNonExistent if the inverse does not exist
     * @see <a href="http://en.wikipedia.org/wiki/Multiplicative_inverse">Wikipedia: Multiplicative inverse</a>
     */
    public F inverse() throws InverseNonExistent;

    /**
     * <i>/ : F × F → F</i>
     * <p/>
     * That is the same as
     * <blockquote><code>this.multiply(that.inverse())</code></blockquote>
     *
     * @param that the denominator
     * @return <i>this / that</i>
     * @throws com.numericalmethod.suanshu.mathstructure.Field.InverseNonExistent if the inverse does not exist
     */
    public F divide(F that) throws InverseNonExistent;
}
