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
package com.numericalmethod.suanshu.matrix.generic;

import com.numericalmethod.suanshu.datastructure.Table;
import com.numericalmethod.suanshu.mathstructure.Field;
import com.numericalmethod.suanshu.mathstructure.Ring;
import com.numericalmethod.suanshu.mathstructure.VectorSpace;

/**
 * This class defines a matrix over a field.
 * This interface is made minimal so that we do not list all possible matrix operations.
 * Instead, matrix operations are grouped into packages and classes by their properties.
 * This is to avoid interface "pollution", lengthy and cumbersome design.
 *
 * @param <T> the matrix type
 * @param <F> the field type
 * @author Ken Yiu
 * @see Field
 * @see Table
 */
public interface Matrix<T extends Matrix<T, F>, F extends Field<F>> extends
        Table,
        MatrixAccess<F>,
        Ring<T>,
        VectorSpace<T, F> {
}
