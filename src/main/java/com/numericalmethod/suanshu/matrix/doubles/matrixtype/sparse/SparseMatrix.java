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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.Densifiable;
import java.util.List;

/**
 * A sparse matrix stores only non-zero values. When there are only a few
 * non-zeros in a matrix, sparse matrix saves memory space for storing the
 * matrix. In addition, the matrix operations based on sparse matrix are usually
 * more efficient. The time complexities are proportional to the number of
 * non-zero values instead of the dimension-squared of dense matrix.
 *
 * @author Ken Yiu
 * @see <a href="http://en.wikipedia.org/wiki/Sparse_matrix"> Wikipedia: Sparse
 * matrix</a>
 */
public interface SparseMatrix extends
        Matrix,
        Densifiable,
        SparseStructure {

    /**
     * Export the non-zero values in the matrix as a list of {@link SparseEntry}s.
     * This is useful for converting between the different formats of {@link SparseMatrix}.
     * For example,
     * <blockquote><pre><code>
     * // construct matrix using DOK
     * DOKSparseMatrix dok = new DOKSparseMatrix(5, 5);
     * // ... insert some values to DOK matrix
     * // convert to CSR matrix for efficient matrix operations
     * CSRSparseMatrix csr = new CSRSparseMatrix(5, 5, dok.getEntrytList());
     * </code></pre></blockquote>
     *
     * @return the sparse entries
     */
    public List<SparseEntry> getEntrytList();
}
