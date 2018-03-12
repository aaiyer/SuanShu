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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a collection of linear equality constraints.
 * <blockquote><pre><i>
 * A * x = b
 * </i></pre></blockquote>
 *
 * @author Haksun Li
 */
public class LinearEqualityConstraints extends LinearConstraints implements EqualityConstraints {

    /**
     * Construct a collection of linear equality constraints.
     *
     * @param A the equality coefficients
     * @param b the equality values
     */
    public LinearEqualityConstraints(Matrix A, Vector b) {
        super(A, b);

        SuanShuUtils.assertArgument(size() < dimension(),
                                    "the number of equality constraints must be less than the number of variables");
    }

    /**
     * Check if we can reduce the number of linear equalities. That is, they are linearly dependent.
     *
     * @return {@code true} if <i>rank[A b] = rank[A]</i>, and <i>rank[A] &lt; #equalities<i>
     * @see "Section 10.2.2, p. 268- 270. Practical Optimization: Algorithms and Engineering Applications. Andreas Antoniou, Wu-Sheng Lu."
     */
    public boolean isReducible() {
        int p = size();
        Matrix Ab = CreateMatrix.cbind(A(), new DenseMatrix(b()));
        int rA = MatrixMeasure.rank(A());
        if (rA >= p) {
            return false;
        }

        int rAb = MatrixMeasure.rank(Ab);
        if (rAb != rA) {
            return false;
        }

        return true;
    }

    /**
     * Get the collection of linearly independent linear constraints.
     *
     * @return the collection of linearly independent linear constraints
     * @Deprecated Not supported yet.
     */
    public LinearEqualityConstraints getReducedLinearEqualityConstraints() {
        if (!isReducible()) {
            return this;// cannot be reduced
        }

        int p1 = MatrixMeasure.rank(A());// p'

        throw new UnsupportedOperationException("Not supported yet.");// TODO
    }
}
