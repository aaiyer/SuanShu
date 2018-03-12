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
package com.numericalmethod.suanshu.matrix.doubles.factorization.svd;

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.diagonalization.BiDiagonalization;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.GivensMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.BidiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.SubMatrixRef;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import static com.numericalmethod.suanshu.number.DoubleUtils.hasZero;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.diagonal;
import static com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector.superDiagonal;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Gloub-Kahan algorithm does the SVD decomposition of a <em>tall</em> matrix in two stages.
 * <ol>
 * <li>First, it reduces the matrix to a bidiagonal matrix using a sequence of Householder transformations.
 * <li>Second, it reduces the super-diagonal of the bidiagonal matrix to 0s, using a sequence of Givens transformations.
 * </ol>
 *
 * @author Haksun Li
 * @see "G. H. Golub, C. F. van Loan, "Algorithm 8.6.2," Matrix Computations, 3rd edition."
 */
public class GloubKahanSVD implements SVDDecomposition {

    private DiagonalMatrix D;
    private Matrix Ut;
    private Matrix V;
    private final Matrix A;//TODO: remove the reference of A
    private final int nrows;
    private final int ncols;
    private final boolean doUV;
    private final boolean normalize;
    public final double epsilon;
    private final int maxIterations = 100;//TODO: how to set the max iteration?

    /**
     * Run the Gloub-Kahan SVD decomposition on a <em>tall</em> matrix.
     *
     * @param A         a <em>tall</em> matrix
     * @param doUV      {@code false} if to compute only the singular values but not <i>U</i> and <i>V</i>
     * @param normalize {@code true} if to sort the singular values in descending order and make them positive
     * @param epsilon   a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if <i>A</i> is not tall
     */
    public GloubKahanSVD(Matrix A, boolean doUV, boolean normalize, double epsilon) {
        SuanShuUtils.assertArgument(DimensionCheck.isTall(A), "A must be tall");

        this.A = A;
        this.nrows = A.nRows();
        this.ncols = A.nCols();
        this.doUV = doUV;
        this.normalize = normalize;
        this.epsilon = epsilon;

        doGloubKahanSVD();
    }

    private void doGloubKahanSVD() {//the implementation
        //Bi-diagonalize A
        BiDiagonalization bidiagonalization = new BiDiagonalization(A);
        Matrix Ua = bidiagonalization.U();
        Matrix Va = bidiagonalization.V();
        Matrix B = bidiagonalization.B();//square
        int Bdim = B.nRows();

        Matrix Ubt = null;
        Matrix Vb = null;

        int lr = 0, ul = 0;//lower right, upper left hand corners of B22
        for (int k = 0; k < maxIterations; ++k) {
            //find the max diagonal matrix B33
            lr = Bdim;
            for (; lr > 1; --lr) {
                if (compare(B.get(lr - 1, lr), 0, epsilon) != 0) {//super-diagonal entry B[lr-1, lr] != 0
                    break;
                }
            }

            if (lr <= 1) {
                break;//B is now a diagonal matrix
            }

            //find the max unreduced bidiagonal matrix B22
            ul = lr - 1;
            for (; ul >= 2; --ul) {
                if (compare(B.get(ul - 1, ul), 0, epsilon) == 0) {//super-diagonal entry B[ul-1, ul] == 0
                    break;
                }
            }

            SubMatrixRef B22ref = new SubMatrixRef(B, ul, lr, ul, lr);//square matrix
            Matrix Mut = new DenseMatrix(Bdim, Bdim).ONE();
            Matrix Mv = new DenseMatrix(Bdim, Bdim).ONE();

            if (compare(B22ref.get(B22ref.nRows(), B22ref.nCols()), 0, epsilon) == 0) {
                //zero out the last column when the last diagonal entry in B22 is 0
                Matrix GsV = zeroOutLastB22Column(B22ref);
                CreateMatrix.replace(Mv, ul, lr, ul, lr, GsV);
            } else if (hasZero(diagonal(B22ref).toArray(), epsilon)) {
                //zero out the row where a diagonal entry in B22 is zero
                Matrix GsU = zeroOutB22Rows(B22ref);
                CreateMatrix.replace(Mut, ul, lr, ul, lr, GsU);
            } else {
                //apply the Gloub-Kahan step on B22
                BidiagonalMatrix B22 = new BidiagonalMatrix(
                        new double[][]{
                            superDiagonal(B22ref).toArray(),
                            diagonal(B22ref).toArray()});
                GloubKahanSVDStep gkStep = new GloubKahanSVDStep(B22);
                CreateMatrix.replace(Mut, ul, lr, ul, lr, gkStep.U().t());
                CreateMatrix.replace(Mv, ul, lr, ul, lr, gkStep.V());
            }

            B = Mut.multiply(B).multiply(Mv);

            if (doUV) {
                Ubt = Ubt == null ? Mut : Mut.multiply(Ubt);
                Vb = Vb == null ? Mv : Vb.multiply(Mv);
            }
        }

        D = new DiagonalMatrix(diagonal(B).toArray());

        if (doUV) {
            if (Ubt == null) {
                Ut = CreateMatrix.subMatrix(Ua.t(), 1, Bdim, 1, Ua.nRows());
            } else {
                Matrix zeroPaddedUbt = Ubt;
                if (this.nrows > Bdim) {
                    zeroPaddedUbt = CreateMatrix.cbind(new Matrix[]{Ubt, new DenseMatrix(Bdim, this.nrows - Bdim).ZERO()});
                }
                Ut = zeroPaddedUbt.multiply(Ua.t());
            }
            V = Vb == null ? Va : Va.multiply(Vb);
        }

        if (normalize && doUV) {
            normalize();
        }
    }

    /**
     * When <i>B[i,i]</i> is 0, we zero out the i-th row by a sequence of column rotations.
     * <i>i != n</i>.
     *
     * @param B a square matrix
     * @return the product of the Givens matrices
     */
    private Matrix zeroOutB22Rows(SubMatrixRef B) {//B must be square
        int dim = B.nCols();

        //find the i-th row where B[i, i] is 0
        int i = 1;
        for (; i <= dim; ++i) {
            if (compare(B.get(i, i), 0, epsilon) == 0 && compare(B.get(i, i + 1), 0, epsilon) != 0) {
                break;
            }
        }

        if (i >= dim) {
            throw new RuntimeException("no zero diagonal entry");//no zero diagonal entry
        }

        Matrix BB = B;
        Matrix Gs = new DenseMatrix(dim, dim).ONE();
        for (int j = i + 1; j <= dim; ++j) {
            GivensMatrix G = GivensMatrix.CtorToRotateRows(dim, j, i, BB.get(j, j), BB.get(i, j));//zero out the i-th row
            BB = G.multiply(BB);
            Gs = G.multiply(Gs);
        }

        return Gs;
    }

    /**
     * When <i>B[n,n]</i> is 0, we zero out the last column by a sequence of column rotations.
     *
     * @param B a square matrix
     * @return the product of the Givens matrices
     */
    private Matrix zeroOutLastB22Column(SubMatrixRef B) {//B must be square
        int dim = B.nRows();
        Matrix BB = B;
        Matrix Gs = new DenseMatrix(dim, dim).ONE();
        for (int i = dim - 1; i >= 1; --i) {
            GivensMatrix G = GivensMatrix.CtorToRotateColumns(dim, i, dim, BB.get(i, i), BB.get(i, dim));
            BB = BB.multiply(G);
            Gs = Gs.multiply(G);
        }

        return Gs;
    }

    /**
     * Normalize <i>U</i>, <i>D</i>, <i>V</i> such that diagonal entries in <i>D</i> are in descending order, and all positive.
     */
    private void normalize() {
        /**
         * Parse <i>U</i>, <i>D</i>, <i>V</i>.
         * <i>
         * A = U %*% D %*% V.t()
         * = sum {u<sub>i</sub> * d<sub>i</sub> * vt<sub>i</sub>}
         * = sum {column u<sub>i</sub> * d<sub>i</sub> * row vt<sub>i</sub>}
         * </i>
         */
        class UDVt {

            Double sv;
            Vector u;
            Vector vt;

            UDVt(Double sv, Vector u, Vector vt) {
                this.sv = sv;
                this.u = u;
                this.vt = vt;
            }
        }

        /**
         * To sort UtDV in descending order
         */
        class UtDVComparator implements Comparator<UDVt> {

            @Override
            public int compare(UDVt sv1, UDVt sv2) {
                return -1 * Double.compare(sv1.sv, sv2.sv);
            }
        }

        int nSV = D.nRows();
        UDVt[] svd = new UDVt[nSV];
        for (int i = 1; i <= nSV; ++i) {
            double d = D.get(i, i);
            svd[i - 1] = new UDVt(
                    d < 0 ? -d : d,//make singular values positive
                    d < 0 ? Ut.getRow(i).scaled(-1) : Ut.getRow(i),
                    V.getColumn(i));
        }

        Arrays.sort(svd, new UtDVComparator());

        Vector[] UCols = new Vector[Ut.nRows()];
        Vector[] VtRows = new Vector[V.nCols()];

        for (int i = 0; i < nSV; ++i) {
            UCols[i] = svd[i].u;
            D.set(i + 1, i + 1, svd[i].sv);
            VtRows[i] = svd[i].vt;
        }

        for (int i = nSV; i < Ut.nRows(); ++i) {
            UCols[i] = new DenseVector(Ut.nCols());
        }

        Ut = CreateMatrix.rbind(UCols);
        V = CreateMatrix.cbind(VtRows);
    }

    @Override
    public double[] getSingularValues() {
        double[] diag = D.getDiagonal().toArray();
        for (int i = 0; i < diag.length; ++i) {
            if (diag[i] < 0) {
                diag[i] *= -1;
            }
        }

        return diag;
    }

    @Override
    public DiagonalMatrix D() {
        return new DiagonalMatrix(D);
    }

    @Override
    public Matrix U() {
        SuanShuUtils.assertOrThrow(doUV ? null
                                   : new RuntimeException("only singular values were computed; U not available"));

        return Ut.t();
    }

    @Override
    public Matrix Ut() {
        SuanShuUtils.assertOrThrow(doUV ? null
                                   : new RuntimeException("only singular values were computed; U not available"));

        return Ut.deepCopy();
    }

    @Override
    public Matrix V() {
        SuanShuUtils.assertOrThrow(doUV ? null
                                   : new RuntimeException("only singular values were computed; V not available"));

        return V.deepCopy();
    }
}
