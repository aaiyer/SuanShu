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
package com.numericalmethod.suanshu.matrix.doubles.factorization.gaussianelimination;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.triangle.LUDecomposition;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.LowerTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;

/**
 * This is a wrapper for {@link GaussianElimination} but applies only to <em>square</em> matrices.
 *
 * @author Haksun Li
 */
public class GaussianElimination4SquareMatrix implements LUDecomposition {

    private final GaussianElimination instance;

    /**
     * Run the Gaussian elimination algorithm on a square matrix.
     *
     * @param A       a square matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public GaussianElimination4SquareMatrix(Matrix A, double epsilon) {
        if (!DimensionCheck.isSquare(A)) {
            throw new IllegalArgumentException("A must be a square matrix");
        }

        instance = new GaussianElimination(A, true, epsilon);
    }

    /**
     * Run the Gaussian elimination algorithm on a square matrix.
     *
     * @param A a square matrix
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public GaussianElimination4SquareMatrix(Matrix A) {
        this(A, SuanShuUtils.autoEpsilon(A));
    }

    @Override
    public LowerTriangularMatrix L() {
        return new LowerTriangularMatrix(instance.L());
    }

    @Override
    public UpperTriangularMatrix U() {
        return new UpperTriangularMatrix(instance.U());
    }

    @Override
    public PermutationMatrix P() {
        return instance.P();
    }
}
