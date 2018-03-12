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

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.CharacteristicPolynomial;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Spectrum;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.Hessenberg.Deflation;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * The QR algorithm is an eigenvalue algorithm by computing the real Schur canonical form of a matrix.
 * That is, <i>Q'AQ = T</i> where <i>Q</i> is orthogonal, and <i>T</i> is quasi-triangular.
 * The eigenvalues of <i>A</i> are the same as those of the diagonal blocks in <i>T</i>.
 * The basic idea of the procedure is to perform the QR decomposition,
 * writing the matrix as a product of an orthogonal matrix and an upper triangular matrix,
 * multiply the factors in the opposite order, and iterate.
 * <p/>
 * This implementation is the implicit double-shift version.
 * It makes the use of multiple shifts easier to introduce.
 * The matrix is first brought to the upper Hessenberg form: <i>A<sub>0</sub> = QAQ'</i> as in the explicit version;
 * then, at each step, the first column of
 * <i>A<sub>k</sub></i> is transformed via a small-size Householder similarity transformation to the first column of <i>p(A<sub>k</sub>)e<sub>1</sub></i>,
 * where <i>p(A<sub>k</sub>)</i>, of degree <i>r</i>, is the polynomial that defines the shifting strategy.
 * Then successive Householder transformation of size <i>r+1</i> are performed in order to return the working matrix <i>A<sub>k</sub></i> to the upper Hessenberg form.
 * This operation is known as bulge chasing, due to the peculiar shape of the non-zero entries of the matrix along the steps of the algorithm.
 * Deflation is performed as soon as one of the sub-diagonal entries of <i>A<sub>k</sub></i> is sufficiently small.
 * No QR decomposition is explicitly performed. Instead, we use the Francis algorithm ({@link FrancisQRStep}), as described in Golub and Van Loan.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/QR algorithm">Wikipedia: QR algorithm</a>
 */
public class QRAlgorithm implements Spectrum {

    private final Matrix A;
    private final Matrix H;
    private final int dim;
    private final List<Matrix> Qs = new ArrayList<Matrix>();
    private final double epsilon;
    private final int maxIterations;

    /**
     * Run the QR algorithm on a <em>square</em> matrix.
     * The algorithm is a convergence algorithm. It stops when either
     * <ol>
     * <li><i>H</i> becomes quasi-triangular, or
     * <li>the maximum number of iterations is reached.
     * </ol>
     *
     * @param A             a <em>square</em> matrix
     * @param maxIterations the maximum number of iterations
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public QRAlgorithm(Matrix A, int maxIterations, double epsilon) {
        if (!DimensionCheck.isSquare(A)) {
            throw new IllegalArgumentException("Eigenvalue decomposition applies only to square matrix");
        }

        this.dim = A.nRows();
        this.A = A;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;

        HessenbergDecomposition decomp = new HessenbergDecomposition(A);
        this.H = decomp.H();//bring A into an uppper Hessenberg form by doing the Hessenberg reduction
        this.Qs.add(decomp.Q());
    }

    /**
     * Run the QR algorithm on a <em>square</em> matrix.
     * The algorithm loops until <i>H</i> becomes quasi-triangular.
     *
     * @param A a matrix
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public QRAlgorithm(Matrix A) {
        this(A, Integer.MAX_VALUE, SuanShuUtils.autoEpsilon(A));//loop till converge
    }

    /**
     * {@inheritDoc}
     * Given a Hessenberg matrix,
     * \[
     * \begin{bmatrix}
     * H_{11} & H_{12} & H_{13}\\
     * 0 & H_{22} & H_{23}\\
     * 0 & 0 & H_{33}
     * \end{bmatrix}
     * \]
     * the real Schur canonical form is <i>Q'HQ = T</i>, where
     * <i>Q</i> is orthogonal, and <i>T</i> is quasi-triangular.
     *
     * <p/>
     * This implementation is a modified version of the algorithm in the reference.
     *
     * @return the list of eigenvalues
     * @see "G. H. Golub, C. F. van Loan, "Algorithm 7.5.2," Matrix Computations, 3rd edition."
     */
    @Override
    public List<Number> getEigenvalues() {
        Hessenberg hss = new Hessenberg();

        Deflation deflation = hss.backSearch(H, dim, epsilon);
        for (int iter = 0;
             iter < maxIterations && !deflation.isQuasiTriangular;//while H is not quasi-triangular
             ++iter, deflation = hss.backSearch(H, dim, epsilon)) {
            Matrix H22 = CreateMatrix.subMatrix(H, deflation.ul, deflation.lr, deflation.ul, deflation.lr);//TODO: avoid copying
            FrancisQRStep francisQR = new FrancisQRStep(H22);
            Matrix H22new = francisQR.ZtHZ();
            //replace H22 with H22new in H
            CreateMatrix.replace(H, deflation.ul, deflation.lr, deflation.ul, deflation.lr, H22new);

            Matrix Q = H.ONE();
            CreateMatrix.replace(Q, deflation.ul, deflation.lr, deflation.ul, deflation.lr, francisQR.Z());//Q hasApproximately changed
            Qs.add(Q);
        }

        List<Number> eigenvalues = new ArrayList<Number>();
        //get the 1x1 and 2x2 matrix blocks
        //for each matrix, compute its eigenvalue analytically
        for (int ul = 1; ul <= dim;) {
            Matrix sub;
            if ((ul == dim)
                || (hss.deflationCriterion.isNegligible(H, ul + 1, ul, epsilon))) {//1x1
                sub = CreateMatrix.subMatrix(H, ul, ul, ul, ul);
                ++ul;
            } else {//2x2
                sub = CreateMatrix.subMatrix(H, ul, ul + 1, ul, ul + 1);
                ul += 2;
            }

            CharacteristicPolynomial cp = new CharacteristicPolynomial(sub);
            List<Number> list = cp.getEigenvalues();
            eigenvalues.addAll(list);
        }

        return eigenvalues;
    }

    /**
     * Get the <i>Q</i> matrix as in the real Schur canonical form <i>Q'MQ = T</i>.
     * The columns of are called Schur vectors.
     * If <i>T</i> is diagonal, then the Schur vectors are the eigenvectors.
     *
     * <p/>
     * This implementation stores all the <i>Q<sub>i</sub></i>'s produced in the process of the QR algorithm.
     * <i>Q</i> is a product of them. That is,
     * \[
     * Q = (Q_1 \times ... \times Q_n)
     * \]
     *
     * @return <i>Q</i>
     */
    public Matrix Q() {
        Matrix Q = H.ONE();
        for (Matrix q : Qs) {
            Q = Q.multiply(q);
        }

        return Q;
    }

    /**
     * Get the <i>T</i> matrix as in the real Schur canonical form <i>Q'MQ = T</i>.
     * <i>T</i> is quasi upper triangular.
     * The eigenvectors of <i>A</i> are the same as those of <i>T</i>.
     *
     * @return <i>T</i>
     * @see "James W. Demmel, Applied Numerical Linear Algebra, SIAM; 1 edition (August 1, 1997)."
     */
    public Matrix T() {//TODO: this implementation may suffer from numerical instability because of the matrix multiplications
        Matrix Q = Q();
        Matrix T = new CongruentMatrix(Q, A);
        return T;
    }
}
