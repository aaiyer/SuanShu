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
package com.numericalmethod.suanshu.matrix.doubles.factorization.qr;

import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.PermutationMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.UpperTriangularMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.Basis;
import com.numericalmethod.suanshu.vector.doubles.operation.Projection;
import java.util.ArrayList;
import java.util.List;

/**
 * The Gram–Schmidt process is a method for orthogonalizing a set of vectors in an inner product space.
 * It does so by iteratively computing the vector orthogonal to the subspace spanned by the previously found orthogonal vectors.
 * An orthogonal vector is the difference between a column vector and its projection on the subspace.
 * <p/>
 * There is the problem of "loss of orthogonality" during the process.
 * In general, the bigger the matrix is, e.g., dimension = 3500x3500, the less precise the result is.
 * The vectors in <i>Q</i> may not be as orthogonal.
 * This implementation uses a numerically stable Gram–Schmidt process with twice re-orthogonalization
 * to alleviate the problem of rounding errors.
 * <p/>
 * Numerical determination of rank requires a criterion to decide how small a value should be treated as zero.
 * This is a practical choice which depends on both the matrix and the application.
 * For instance, for a matrix with a big first eigenvector,
 * we should accordingly decrease the precision to compute the rank.
 * <p/>
 * While the result for the orthogonal basis may match those of the Householder Reflection {@link HouseholderReflection},
 * the result for the orthogonal complement may differ because the kernel basis is not unique.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Luc Giraud, Julien Langou, Miroslav Rozloznik, "On the loss of orthogonality in the Gram-Schmidt orthogonalization process," Computers & Mathematics with Applications, Volume 50, Issue 7, October 2005, p. 1069-1075. Numerical Methods and Computational Mechanics."
 * <li>"Gene H. Golub, Charles F. Van Loan, Matrix Computations (Johns Hopkins Studies in Mathematical Sciences)(3rd Edition)(Paperback)."
 * <li><a href="http://en.wikipedia.org/wiki/Gram-Schmidt">Wikipedia: Gram–Schmidt process</a>
 * </ul>
 */
//TODO: column pivoting, rank, det, output the diagonal
//
public class GramSchmidt implements QRDecomposition {
    
    private Matrix Q;
    private UpperTriangularMatrix R;
    private PermutationMatrix P;
    private int rank;
    private final DenseVector zero;
    private final int nRows;//the number of rows
    private final int nCols;//the number of columns
    private final double epsilon;

    /**
     * Run the Gram-Schmidt process to orthogonalize a matrix.
     *
     * @param A        a matrix
     * @param pad0Cols when a column is linearly dependent on the previous columns, there is no orthogonal vector. We pad the basis with a 0-vector.
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public GramSchmidt(Matrix A, boolean pad0Cols, double epsilon) {
        this.nRows = A.nRows();
        this.nCols = A.nCols();
        this.epsilon = epsilon;
        
        R = new UpperTriangularMatrix(nCols);
        R.set(1, 1, 0);//allocate space
        P = new PermutationMatrix(nCols);//For a fat matrix A, the permutation matrix P is not square. The rightmost columns are 0.
        rank = 0;
        zero = new DenseVector(nRows);
        
        Vector[] basis = new Vector[nCols];
        basis[0] = new DenseVector(nRows);//in case the one and only column has norm 0, we need to have the lenght for CreateMatrix.cbind

        for (int i = 1; i <= nCols; ++i) {//This loop implements the stabilized version of the modified Gram-Schmidt process.
            Vector orthogonalVector = A.getColumn(i);
            for (int j = 1; j <= 2; ++j) {//reorthogonalization; twice is enough
                for (int k = 1; k <= i - 1; ++k) {
                    if (basis[k - 1] != null) {//basis[i] is not zero vector
                        Projection projection = new Projection(orthogonalVector, basis[k - 1]);
                        orthogonalVector = projection.getOrthogonalVector();
                        R.set(k, i, projection.getProjectionLength(0) + R.get(k, i));
                    }
                }
            }
            
            if (!IsMatrix.zero(orthogonalVector, epsilon)) {
                double norm = orthogonalVector.norm();
                R.set(i, i, norm);
                basis[i - 1] = orthogonalVector.scaled(1. / norm);
                rank++;//increase rank each linearly independent vector is found
            } else {//orthogonalVector is not a basis b/c of linear dependence
                if (pad0Cols) {
                    basis[i - 1] = zero;
                }
                
                R.set(i, i, 0);
                P.moveColumn2End(i);//record column pivoting if this vector is linearly dependent on the previous vectors
            }
        }
        
        Q = CreateMatrix.cbind(basis);
    }

    /**
     * Run the Gram-Schmidt process to orthogonalize a matrix.
     *
     * @param A a matrix
     */
    public GramSchmidt(Matrix A) {
        this(A, true, SuanShuUtils.autoEpsilon(A));
    }
    
    @Override
    public Matrix Q() {
        return Q.deepCopy();
    }
    
    @Override
    public UpperTriangularMatrix R() {
        return new UpperTriangularMatrix(R);
    }
    
    @Override
    public PermutationMatrix P() {
        return new PermutationMatrix(P);
    }
    
    @Override
    public int rank() {
        return rank;
    }

    /**
     * {@inheritDoc}
     *
     * This implementation extends <i>Q</i> by appending <i>A</i>'s orthogonal complement.
     * Suppose <i>Q</i> has the orthogonal basis for a subspace <i>A</i>.
     * To compute the orthogonal complement of <i>A</i>, we apply the Gram-Schmidt procedure to either
     * <ol>
     * <li>the columns of <i>I - P = I - Q * Q'</i>, or
     * <li>the spanning set \(\left \{ u_1, u_2, ..., u_k, e_1, e_2, ..., e_n \right \}\)
     * and keeping only the first <i>n</i> elements of the resulting basis of <i>R<sub>n</sub></i>.
     * The last <i>n-k</i> elements are the basis for the orthogonal complement. <i>k</i> is the rank.
     * </ol>
     * We implemented the second option.
     *
     * @return the spanning set of both the orthogonal basis of <i>A</i> and the orthogonal complement
     */
    @Override
    public Matrix squareQ() {
        if (Q.nRows() == rank) {//<i>A</i> is full rank
            if (Q.nCols() <= Q.nRows()) {//square or tall <i>A</i>
                return Q();//<i>Q</i> is already complete because <i>A</i> is full rank
            }
            
            Matrix squareQ = CreateMatrix.subMatrix(Q, 1, nRows, 1, nRows);//keep only the first <i>nRows</i> columns
            return squareQ;
        }

        /*
         * When <i>A</i> is full rank and there are more columns than rows,
         * we need to do a reduction to make <i>Q</i> <i>nRows x nRows</i>.
         */
        if (Q.nCols() > Q.nRows()) {
            Matrix Qcomplete = CreateMatrix.subMatrix(Q, 1, nRows, 1, nRows);//keep only the first <i>nRows</i> columns
            return Qcomplete;
        }

        /*
         * When A is not full rank, we need to complete the square <i>Q</i>
         * by adding the basis of the null space or range space of <i>A</i>.
         */
        List<Vector> basis = new ArrayList<Vector>();
        for (int i = 1; i <= rank; ++i) {//compute the span for the range space
            basis.add(Q.getColumn(i));
        }
        
        List<Vector> Rn = Basis.getBasis(nRows, nRows);//basis for Euclidean space Rn
        basis.addAll(Rn);
        
        Matrix M = CreateMatrix.cbind(basis);
        GramSchmidt gs = new GramSchmidt(M, false, epsilon);
        
        Matrix squareQ = gs.Q();
        squareQ = CreateMatrix.subMatrix(squareQ, 1, nRows, 1, nRows);//keep only the first <i>nRows</i> columns
        return squareQ;
    }
    
    @Override
    public Matrix tallR() {
        UpperTriangularMatrix myR = R();
        if (nRows < nCols) {//for fat matrices
            return CreateMatrix.subMatrix(myR, 1, nRows, 1, nCols);
        }

        //for tall matrices
        Matrix tallR = new DenseMatrix(nRows, nCols).ZERO();
        CreateMatrix.replace(tallR, 1, Math.min(nCols, nRows), 1, nCols, myR);
        return tallR;
    }
}
