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
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * {@code SVEC} converts a symmetric matrix <i>K = {K<sub>ij</sub>}</i> into a vector of dimension <i>n(n+1)/2</i>.
 * That is,
 * \[
 * \rm{svec}(K) = [k_{1,1}, \sqrt2k_{1,2}, ..., \sqrt2k_{1,n}, k_{2,2}, \sqrt2k_{2,3}, ..., \sqrt2k_{2,n}, ... k_{n,n},]
 * \]
 * {@code SVEC} is the inverse operator of {@link MAT}.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "eq. 14.37," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SVEC extends DenseVector {

    /**
     * Construct the SVEC of a matrix.
     *
     * @param A a matrix
     */
    public SVEC(Matrix A) {
        super(svec(A));
    }

    private static double[] svec(Matrix A) {
        final int n = A.nRows();

        double[] svec = new double[n * (n + 1) / 2];
        int k = 0;
        for (int i = 1; i <= n; i++) {
            svec[k++] = A.get(i, i);
            for (int j = i + 1; j <= n; j++) {
                svec[k++] = Constant.ROOT_2 * A.get(j, i);
            }
        }

        return svec;
    }
}
