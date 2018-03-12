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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.root.QuadraticRoot;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseData;
import java.util.List;

/**
 * This implementation stores data for triangular matrices.
 *
 * @author Ken Yiu
 */
abstract class TriangularData extends DenseData {

    /** the (square, triangular) matrix dimension */
    final int dim;

    /**
     * Construct a storage.
     * Initialize the content to 0.
     *
     * @param dim the matrix dimension
     */
    TriangularData(int dim) {
        super(dataLength(dim));
        this.dim = dim;
    }

    /**
     * Construct a storage.
     *
     * @param data the data
     */
    TriangularData(double[] data) {
        super(data);

        //solve for dim
        QuadraticRoot solver = new QuadraticRoot();
        List<Number> soln = solver.solve(new Polynomial(new double[]{1, 1, -2 * data.length}));
        this.dim = soln.get(0).doubleValue() > 0 ? Math.round(soln.get(0).floatValue()) : Math.round(soln.get(1).floatValue());
    }

    private static int dataLength(int dim) {
//        return ((dim * (dim - 1)) >> 1) + dim; // >> 1 is a (much) faster version of / 2
        return (dim * (dim + 1)) >> 1; // >> 1 is a (much) faster version of / 2
    }
}
