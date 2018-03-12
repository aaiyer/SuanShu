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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder.Context;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.max;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.concat;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.subVector;
import java.util.ArrayList;

/**
 * A Francis-QR step is an important step in many decomposition operations, e.g., eigen decomposition.
 * Given a real, unreduced, <em>upper</em> Hessenberg matrix, <i>H</i>,
 * whose trailing 2x2 sub-matrix has eigenvalues <i>a<sub>1</sub></i>and <i>a<sub>2</sub></i>,
 * this step computes <i>Z</i> such that,
 * <ul>
 * <li><i>Z</i> is a product of Householder matrices, and
 * <li><i>Z' * (H - a<sub>1</sub>*I) * (H - a<sub>2</sub>*I)</i> is upper triangular.
 * </ul>
 *
 * @author Haksun Li
 * @see "G. H. Golub, C. F. van Loan, "Algorithm 7.5.1," Matrix Computations, 3rd edition."
 */
class FrancisQRStep {

    private final int n;
    private final int m;
    private final Matrix H;
    private final Matrix ZtHZ;//Z.t() %*% H %*% Z
    private final ArrayList<Householder> HouseHolderTransformations = new ArrayList<Householder>();

    /**
     * Run a Francis-QR step on a real, unreduced, <em>upper</em> Hessenberg matrix.
     *
     * @param H a real, unreduced, <em>upper</em> Hessenberg matrix;
     * It is necessary to make sure that
     * <pre><code>
     * Hessenberg.isHessenberg(H, ε) == true
     * </code></pre>
     */
    FrancisQRStep(Matrix H) {
        this.H = H;
        this.n = H.nRows();
        this.m = n - 1;
        this.ZtHZ = doFrancisQR();
    }

    private Matrix doFrancisQR() {
        Matrix result = new DenseMatrix(H);

        Vector col1 = col1();
        for (int k = 1; k <= n - 1; ++k) {
            Context generator = Householder.getContext(col1);
            Householder hh = new Householder(generator.generator);
            HouseHolderTransformations.add(hh);

            //reflect columns
            Matrix Hsub = CreateMatrix.subMatrix(result,
                                                 k, Math.min(k + 2, n),//rows
                                                 Math.max(k - 1, 1), n);//columns
            Matrix HsubReflected = hh.reflect(Hsub);
            CreateMatrix.replace(result,
                                 k, Math.min(k + 2, n),
                                 Math.max(k - 1, 1), n,
                                 HsubReflected);

            //reflect rows
            Hsub = CreateMatrix.subMatrix(result,
                                          1, Math.min(k + 3, n),//rows
                                          k, Math.min(k + 2, n));//columns
            HsubReflected = hh.reflectRows(Hsub);
            CreateMatrix.replace(result,
                                 1, Math.min(k + 3, n),
                                 k, Math.min(k + 2, n),
                                 HsubReflected);

            if (k < n - 1) {//to prevent array out of bound (for the last iteration)
                col1.set(1, result.get(k + 1, k));
                col1.set(2, result.get(k + 2, k));
                if (k <= n - 3) {
                    col1.set(3, result.get(k + 3, k));
                } else {
                    col1 = subVector(col1, 1, 2);//truncate into a 2 entry vector
                }
            }
        }

        return result;
    }

    /**
     * Get a copy of
     * <code>Z'HZ</code>.
     *
     * @return a copy of
     * <code>Z' %*% H %*% Z</code>
     */
    Matrix ZtHZ() {
        return ZtHZ.deepCopy();
    }

    /**
     * Get a copy of
     * <code>Z</code>.
     *
     * @return a copy of
     * <code>Z</code>
     */
    Matrix Z() {
        ArrayList<Householder> Hs = getHouseholderTransformations();
        Householder[] Harr = new Householder[Hs.size()];
        Harr = Hs.toArray(Harr);

        Matrix Z = Householder.product(Harr, 0, Harr.length - 1);
        return Z;
    }

    /**
     * Get a list of the HouseHolder transformations used in this FrancisQRStep step.
     *
     * @return a list of the HouseHolder transformations
     */
    ArrayList<Householder> getHouseholderTransformations() {
        ArrayList<Householder> result = new ArrayList<Householder>();

        int nPre0 = 0;
        for (Householder householder : HouseHolderTransformations) {
            Vector v4H = householder.generator();
            int nPost0 = n - v4H.size() - nPre0;

            Vector normalization = concat(
                    new DenseVector(nPre0),
                    v4H,
                    new DenseVector(nPost0));

            result.add(new Householder(normalization));

            ++nPre0;
        }

        return result;
    }

    /**
     * Get a copy of first column of
     * <code>A</code>, without actually expanding
     * <code>A</code>.
     * c.f., {@link #M}.
     * The column is truncated to contain only the first 3 entries.
     *
     * @return a copy of the truncated first column of
     * <code>A</code>
     */
    DenseVector col1() {
        double h1_1 = H.get(1, 1);
        double h1_2 = H.get(1, 2);
        double h2_1 = H.get(2, 1);
        double h2_2 = H.get(2, 2);
        double h3_2 = H.get(3, 2);
        double hm_m = H.get(m, m);
        double hm_n = H.get(m, n);
        double hn_m = H.get(n, m);
        double hn_n = H.get(n, n);

        //scaling to alleviate the possible overflows
        double scale = max(
                Math.abs(h1_1),
                Math.abs(h1_2),
                Math.abs(h2_1),
                Math.abs(h2_2),
                Math.abs(h3_2),
                Math.abs(hm_m),
                Math.abs(hm_n),
                Math.abs(hn_m),
                Math.abs(hn_n));
        scale = 1d / scale;

        h1_1 = scale * h1_1;
        h1_2 = scale * h1_2;
        h2_1 = scale * h2_1;
        h2_2 = scale * h2_2;
        h3_2 = scale * h3_2;
        hm_m = scale * hm_m;
        hm_n = scale * hm_n;
        hn_m = scale * hn_m;
        hn_n = scale * hn_n;

        double p = hn_n - h1_1;
        double q = hm_m - h1_1;
        double r = h2_2 - h1_1;

        DenseVector col1 = new DenseVector(3);
        col1.set(1, (p * q - hn_m * hm_n) / h2_1 + h1_2);
        col1.set(2, (r - p - q));
        col1.set(3, h3_2);

        return col1;
    }

    /**
     * Get a copy of
     * <code>A</code>, which is
     * <blockquote><code>
     * A = H^2 - trace * H + det * I
     * </blockquote></code>
     *
     * @return a copy of
     * <code>A</code>
     */
    Matrix M() {
        //the 2x2 trailing sub-matrix of H
        Matrix trailingSubMatrix = CreateMatrix.subMatrix(H, m, n, m, n);

        double tr = MatrixMeasure.tr(trailingSubMatrix);
        double det = MatrixMeasure.det(trailingSubMatrix);

        Matrix A = H.multiply(H).minus(H.scaled(tr)).add(H.ONE().scaled(det));
        return A;
    }
}
