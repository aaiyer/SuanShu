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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.mathoperation;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.MatrixAccess;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This interface defines some standard operations for generic matrices.
 * An implementation of such provides a default implementation of certain matrix definitions.
 * Moreover, it allows these definitions to change to another implementation easily.
 * Note that the return type of the operations is the general {@link Matrix} interface.
 * An implementation can override these return types.
 *
 * @author Haksun Li
 */
public interface MatrixMathOperation {//TODO: is this a good design?

    /**
     * <i>A<sub>1</sub> + A<sub>2</sub></i>
     *
     * @param A1 a matrix
     * @param A2 a matrix
     * @return the sum of <i>A<sub>1</sub> and A<sub>2</sub></i>
     */
    public Matrix add(MatrixAccess A1, MatrixAccess A2);

    /**
     * <i>A<sub>1</sub> - A<sub>2</sub></i>
     *
     * @param A1 a matrix
     * @param A2 a matrix
     * @return the difference between <i>A<sub>1</sub> and A<sub>2</sub></i>
     */
    public Matrix minus(MatrixAccess A1, MatrixAccess A2);

    /**
     * <i>A<sub>1</sub> * A<sub>2</sub></i>
     *
     * @param A1 a matrix
     * @param A2 a matrix
     * @return the product of <i>A<sub>1</sub> and A<sub>2</sub></i>
     */
    public Matrix multiply(MatrixAccess A1, MatrixAccess A2);

    /**
     * <i>A * v</i>
     *
     * @param A a matrix
     * @param v a vector
     * @return the product of <i>A</i> and <i>v</i>
     */
    public Vector multiply(MatrixAccess A, Vector v);

    /**
     * <i>c * A</i>
     *
     * @param A a matrix
     * @param c a scalar
     * @return <i>A</i> scaled by <i>c</i>
     */
    public Matrix scaled(MatrixAccess A, double c);

    /**
     * Get the transpose of <i>A</i>.
     *
     * @param A a matrix
     * @return the transpose of <i>A</i>
     */
    public Matrix transpose(MatrixAccess A);
}
