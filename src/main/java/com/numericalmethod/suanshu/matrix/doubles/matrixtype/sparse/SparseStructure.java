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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse;

/**
 * This interface defines common operations on sparse structures such as sparse
 * vector or sparse matrix.
 *
 * @author Ken Yiu
 */
public interface SparseStructure {

    /**
     * Get the number of non-zero entries in the structure.
     *
     * @return the number of non-zero entries in the structure
     */
    public int nNonZeros();
    //TODO: we remove this method for now as the only method allowed to change a matrix/vector is "set"
//    /**
//     * Remove non-zero entries <i>x</i> whose magnitude is less than or equal to the tolerance, i.e., <i>|x| &le; tolerance</i>.
//     *
//     * @param tol the tolerance for non-zeros
//     * @return the number of non-zeros removed
//     */
//    public int dropTolerance(double tol);
}
