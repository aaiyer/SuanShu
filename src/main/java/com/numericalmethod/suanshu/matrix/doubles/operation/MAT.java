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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

/**
 * {@code MAT} is the inverse operator of {@link SVEC}. That is,
 * <blockquote><i>
 * mat(svec(A)) = A
 * </i></blockquote>
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "eq. 14.41f," Practical Optimization: Algorithms and Engineering Applications."
 */
public class MAT extends DenseMatrix {

    /**
     * Construct the MAT of a vector.
     *
     * @param v a vector
     */
    public MAT(Vector v) {
        super(dimension(v), dimension(v));
        final int n = this.nRows();

        int k = 1;
        for (int i = 1; i <= n; ++i) {
            for (int j = i; j <= n; ++j) {
                double vk = v.get(k++);
                if (j != i) {
                    vk /= Constant.ROOT_2;
                }

                set(i, j, vk);
                set(j, i, vk);
            }
        }
    }

    private static int dimension(Vector v) {
        int s = v.size();
        int n = (int) round(0.5 * (sqrt(8 * s + 1) - 1));

        return n;
    }
}
