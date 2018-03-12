/*
 * Copyright (c)
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

import java.util.List;

/**
 *
 * @author Ken Yiu
 */
public class CSRSparseMatrixTest extends SparseMatrixTestCase<CSRSparseMatrix> {

    @Override
    public Class<CSRSparseMatrix> getImplementationClass() {
        return CSRSparseMatrix.class;
    }

    @Override
    public CSRSparseMatrix newInstance(int nRows, int nCols) {
        return new CSRSparseMatrix(nRows, nCols);
    }

    @Override
    public CSRSparseMatrix newInstance(int nRows, int nCols, int[] rowIndices, int[] columnIndices, double[] values) {
        return new CSRSparseMatrix(nRows, nCols, rowIndices, columnIndices, values);
    }

    @Override
    public CSRSparseMatrix newInstance(int nRows, int nCols, List<SparseEntry> elementList) {
        return new CSRSparseMatrix(nRows, nCols, elementList);
    }
}
