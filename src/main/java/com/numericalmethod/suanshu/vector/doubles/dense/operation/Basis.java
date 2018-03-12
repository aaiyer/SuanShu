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
package com.numericalmethod.suanshu.vector.doubles.dense.operation;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse.SparseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.List;

/**
 * A basis is a set of linearly independent vectors spanning a vector space.
 * Every element in this space can be uniquely represented by a linear combination of elements in the basis.
 * This implementation is the standard basis of the Euclidean R<sup>n</sup> space.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Basis_(linear_algebra)">Wikipedia: Basis (linear algebra)</a>
 */
public class Basis extends SparseVector {

    /**
     * Construct a vector that corresponds to the <i>i</i>-th dimension in R<sup>n</sup>.
     * That is (a 1 in the <i>i</i>-th entry),
     * \[
     * \begin{bmatrix}
     * 0\\
     * ...\\
     * 1\\
     * ...\\
     * 0
     * \end{bmatrix}
     * \]
     *
     * @param dim the dimension
     * @param i   the <i>i</i>-th dimension in R<sup>n</sup>
     */
    public Basis(int dim, int i) {
        super(dim, new int[]{i}, new double[]{1.});
    }

    /**
     * Get the full set of the standard basis vectors.
     *
     * @param dim the dimension
     * @return the basis vectors
     */
    public static List<Vector> getBasis(int dim) {
        return getBasis(dim, dim);
    }

    /**
     * Get a subset of the standard basis vectors.
     *
     * @param dim   the dimension
     * @param nCols the number of basis vectors requested; it must be smaller than {@code dim}
     * @return the basis vectors
     * @throws IllegalArgumentException if there are more columns requested than the dimension
     */
    public static List<Vector> getBasis(int dim, int nCols) {//TODO: somehow, Java does not let me to declare this as Vector[], or it won't operate properly with another Vector
        if (dim < nCols) {
            throw new IllegalArgumentException("more columns than the dimension is requested");
        }

        List<Vector> basis = new ArrayList<Vector>(nCols);
        for (int i = 1; i <= nCols; ++i) {
            basis.add(new Basis(dim, i));
        }

        return basis;
    }
}
