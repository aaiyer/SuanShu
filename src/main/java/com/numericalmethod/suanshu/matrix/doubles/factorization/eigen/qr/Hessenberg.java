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
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;
import java.util.ArrayList;

/**
 * An <em>upper</em> Hessenberg matrix is a square matrix which has zero entries below the first sub-diagonal.
 * For example,
 * \[
 * \begin{bmatrix}
 * 1 & 2 & 3 & 4 & \\
 * 5 & 6 & 7 & 8 & \\
 * 0 & 9 & 10 & 11 & \\
 * 0 & 0 & 12 & 13 &
 * \end{bmatrix}
 * \]
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Hessenberg_matrix>Wikipedia: Hessenberg matrix</a>
 */
public class Hessenberg {

    /**
     * Deflation of an upper Hessenberg matrix splits it into multiple smaller upper Hessenberg matrices
     * when the sub-diagonal entries are sufficiently small. For example, suppose
     * \[
     * H = \begin{bmatrix}
     * 1 & 2 & 3 & 4 & \\
     * 5 & 6 & 7 & 8 & \\
     * 0 & 9 & 10 & 11 & \\
     * 0 & 0 & 12 & 13 &
     * \end{bmatrix}
     * \]
     * We can split <i>H</i> into <i>H<sub>1</sub></i> and <i>H<sub>2</sub></i>, so that
     * \[
     * H_1 = \begin{bmatrix}
     * 1 & 2 \\
     * 5 & 6 \\
     * \end{bmatrix}
     * \]
     * \[
     * H_2 = \begin{bmatrix}
     * 10 & 11 \\
     * 12 & 13 \\
     * \end{bmatrix}
     * \]
     *
     * @see "Golub and van Loan, Section 7.5.1."
     */
    public static interface DeflationCriterion {

        /**
         * Check whether a sub-diagonal element is sufficiently small.
         *
         * @param H       a matrix
         * @param i       a row index
         * @param j       a column index
         * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
         * @return {@code true} is <i>H[i,j]</i> is deemed small enough
         */
        public boolean isNegligible(Matrix H, int i, int j, double epsilon);
    }

    /* the deflation criterion */
    public final DeflationCriterion deflationCriterion;

    /**
     * Construct a Hessenberg utility class.
     *
     * @param dc a deflation criterion
     */
    public Hessenberg(DeflationCriterion dc) {
        deflationCriterion = dc;
    }

    /**
     * Construct a Hessenberg utility class with the default deflation criterion.
     */
    public Hessenberg() {
        this(new DefaultDeflationCriterion());
    }

    /**
     * Check if <i>H</i> is <em>upper</em> Hessenberg.
     * An upper Hessenberg matrix has zero entries below the first sub-diagonal.
     *
     * @param H       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <i>H</i> is upper Hessenberg
     */
    public static boolean isHessenberg(Matrix H, double epsilon) {
        int ncols = H.nCols();
        int nrows = H.nRows();

        for (int i = 1; i <= ncols; ++i) {
            for (int j = i + 2; j <= nrows; ++j) {
                if (compare(H.get(j, i), 0, epsilon) != 0) {
                    return false;//H is NOT upper Hessenberg
                }
            }
        }

        return true;
    }

    /**
     * Check if <i>H</i> is <em>upper</em> Hessenberg and is reducible.
     * Note that matrix <i>|0|</i> is unreducible.
     *
     * @param H       a matrix
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return {@code true} if <i>H</i> is upper Hessenberg and is reducible
     */
    public boolean isReducible(Matrix H, double epsilon) {
        boolean result = !isHessenberg(H, epsilon);

        for (int i = 2; i <= H.nRows() && !result; ++i) {
            if (deflationCriterion.isNegligible(H, i, i - 1, epsilon)) {
                return true;
            }
        }

        return result;
    }

    /**
     * This looks for 0s in the sub-diagonal of a Hessenberg matrix, <i>H</i>,
     * and reduces (splits) it into a number of non-reducible Hessenberg matrices.
     * For example, we reduce
     * \[
     * \begin{matrix}
     * 1 & 2 & 3 & 4 & 5 & 6\\
     * 7 & 8 & 9 & 10 & 11 & 12\\
     * 0 & 14 & 15 & 16 & 17 & 18 \\
     * 0 & 0 & 0 & 22 & 23 & 24 \\
     * 0 & 0 & 0 & 28 & 29 & 30 \\
     * 0 & 0 & 0 & 0 & 35 & 36
     * \end{matrix}
     * \]
     * into
     * \[
     * \begin{matrix}
     * 1 & 2 & 3 \\
     * 7 & 8 & 9 \\
     * 0 & 14 & 15 \\
     * \end{matrix}
     * \]
     * and
     * \[
     * \begin{matrix}
     * 22 & 23 & 24 \\
     * 28 & 29 & 30 \\
     * 0 & 35 & 36 \\
     * \end{matrix}
     * \]
     * <em>Not yet implemented.</em>
     *
     * @param H a matrix
     * @return a list of non-reducible Hessenberg matrices
     * @deprecated Not supported yet.
     */
    @Deprecated
    public ArrayList<Matrix> reduce(Matrix H) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * This class encapsulates the indices for the upper left hand corner and lower right hand corner of <i>H<sub>22</sub></i> as a result of the deflation procedure.
     * Given a Hessenberg matrix,
     * \[
     * \begin{bmatrix}
     * H_{11} & H_{12} & H_{13}\\
     * 0 & H_{22} & H_{23}\\
     * 0 & 0 & H_{33}
     * \end{bmatrix}
     * \]
     * <ul>
     * <li>\(u_l\) is the upper left hand corner index of <i>H<sub>22</sub></i>;
     * <li>\(l_r\) is the lower right hand corner index of <i>H<sub>22</sub></i>
     * </ul>
     * If both \(u_l\) and \(l_r\) are all zeros, it means that <i>H</i> is a quasi-triangular matrix.
     */
    public static class Deflation {

        /**
         * <code>H<sub>22</sub></code> an unreduced Hessenberg in Algorithm 7.5.2 has the dimension \((l_r-r_l+1) \times (l_r-u_l+1)\).
         * We try to minimize \(u_l\) (hence maximize the <i>H<sub>22</sub></i> dimension).
         */
        public final int ul;
        /**
         * <code>H<sub>33</sub></code> an upper quasi-triangular in Algorithm 7.5.2 has dimension \((n-l_r) \times (n-l_r)\).
         * We try to minimize \(l_r\) (hence maximize the <i>H<sub>33</sub></i> dimension).
         */
        public final int lr;
        /**
         * {@code true} if the matrix is a quasi-triangular matrix
         */
        public final boolean isQuasiTriangular;

        private Deflation(int ul, int lr) {
            this.ul = ul;
            this.lr = lr;
            this.isQuasiTriangular = (ul == 0 && lr == 0) ? true : false;
        }
    };

    /**
     * Find <i>H<sub>22</sub></i> such that <i>H<sub>22</sub></i> is the largest unreduced Hessenberg sub-matrix.
     * For performance reason, this implementation has a side effect: the method modifies the input <i>H</i> by rounding the negligible sub-diagonal elements to 0.
     *
     * @param H       a Hessenberg matrix
     * @param n       the start index for the back search
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return deflation information that specifies the upper left hand corner and lower right hand corner indices of <i>H<sub>22</sub></i>
     * @see "G. W. Steward, "Algorithm 3.3," Matrix Algorithms, Volume II"
     */
    public Deflation backSearch(Matrix H, int n, double epsilon) {
        //these are the upper left hand corner indices for H22 and H33
        int ul = n;
        int lr = n;

        for (; ul >= 1;) {
            if (ul == 1 || deflationCriterion.isNegligible(H, ul, ul - 1, epsilon)) {//reducible; deflation point
                if (lr != 2 &&//no need to do rounding for the leading 2x2 matrix
                        ul > 1) {//avoid invalid index
                    //set to 0 all sub-diagonal elements that is negligible
                    H.set(ul - 1, ul, 0);
                }

                if (ul == lr - 1//process a 2x2 block
                    || (ul == lr)) {//process a 1x1 block
                    /*
                     * process a 2x2 block
                     *
                     * p: | * * |
                     * q: | 0 * |
                     *
                     * or,
                     *
                     * found a 1x1 block
                     *
                     * p,q: | 0 * |
                     */

                    --ul;
                    lr = ul;
                } else {
                    break;//TODO: why?
                }
            } else {
                //H22 is unreduced
                //try to maximize it
                --ul;
            }
        }

        return new Deflation(ul, lr);
    }

    /**
     * The default deflation criterion is to use eq. 7.5.2 in Golub and van Loan: <i>H[i,j]</i> is negligible when
     * <pre><i>| H[i,j] | < tol * (| H[i-1,j] | + | H[i,j+1] |)</i></pre>
     * or, when
     * <pre><i>| H[i-1,j] | + | H[i,j+1] | == 0</i></pre>
     * and to use eq. 2.26 in Matrix Algorithms, Volume II by Steward G. W.
     * <pre><i><code>| H[i,j] | < tol * ||A||<sub>F</sub></i></pre>
     */
    public static class DefaultDeflationCriterion implements DeflationCriterion {

        private double threshold = 1e-12;

        /**
         * Construct the default deflation criterion.
         *
         * @param threshold the tolerance in Steward's deflation criterion
         */
        public DefaultDeflationCriterion(double threshold) {
            this.threshold = threshold;
        }

        /**
         * Construct the default deflation criterion.
         */
        public DefaultDeflationCriterion() {
            this(1e-12);
        }

        /**
         * Check if
         * <code>H[i,j]</code> is negligible by Steward's deflation criterion.
         *
         * @param H       a matrix
         * @param i       a row index
         * @param j       a column index
         * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
         * @return {@code true} if
         * <blockquote><code>| H[i,j] | < tol * (| H[i-1,j] | + | H[i,j+1] |)</code></blockquote>
         */
        @Override
        public boolean isNegligible(Matrix H, int i, int j, double epsilon) {
            boolean result = false;

            double Hii = H.get(i, j);
            double Him1_j = H.get(i - 1, j);//H[i-1,j]
            double Hi_jp1 = H.get(i, j + 1);//H[i,j+1]

            double absSum = Math.abs(Him1_j) + Math.abs(Hi_jp1);

            if (equal(absSum, 0, epsilon)) {
                if (compare(Math.abs(Hii), threshold * MatrixMeasure.Frobenius(H), epsilon) <= 0) {
                    result = true;
                }//else, result = false
            } else if (compare(Math.abs(Hii), threshold * absSum, epsilon) <= 0) {//absSum != 0
                result = true;
            }

            return result;
        }
    }

}
