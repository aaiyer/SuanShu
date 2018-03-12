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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen;

import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.polynomial.root.PolyRoot;
import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import java.util.ArrayList;
import java.util.List;

/**
 * The characteristic polynomial of a <em>square</em> matrix is the function
 * <pre><i>
 * p(λ) = det(A - λI)
 * </i></pre>
 * The zeros of this polynomial are the eigenvalues of <i>A</i>. That is,
 * <i>λ</i> being an eigenvalue of <i>A</i> is equivalent to stating that the system of linear equations
 * <pre><i>
 * (A - λI) v = 0
 * </i></pre>
 * where <i>I</i> is an identity matrix, has a non-zero solution <i>v</i> (namely an eigenvector).
 * <p/>
 * The Cayley-Hamilton theorem states that
 * every <em>square</em> matrix satisfies its own characteristic polynomial, that is, <i>p(A) = 0 </i>.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Eigenvalue_algorithm">Wikipedia: Eigenvalue algorithm</a>
 * <li><a href="http://en.wikipedia.org/wiki/Cayley-Hamilton_theorem">Wikipedia: Cayley–Hamilton theorem</a>
 * </ul>
 */
public class CharacteristicPolynomial implements Spectrum {

    final private Matrix A;
    final private Polynomial polynomial;

    /**
     * Construct the characteristic polynomial for a <em>square</em> matrix.
     *
     * @param A a square matrix
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public CharacteristicPolynomial(Matrix A) {
        if (!DimensionCheck.isSquare(A)) {
            throw new IllegalArgumentException("Eigenvalue decomposition applies only to square matrix");
        }

        this.A = A;
        this.polynomial = getCharacteristicPolynomial();
    }

    /**
     * Get the characteristic polynomial.
     *
     * @return the characteristic polynomial
     */
    public Polynomial getCharacteristicPolynomial() {
        switch (A.nRows()) {
            case 1:
                return new Polynomial(new double[]{1d, -A.get(1, 1)});
            case 2:
                return new Polynomial(new double[]{1d, -MatrixMeasure.tr(A), MatrixMeasure.det(A)});
            case 3:
                return null;//TODO:
            case 4:
                return null;//TODO:
            default:
                return null;//TODO:
        }
    }

    @Override
    public List<Number> getEigenvalues() {
        List<Number> result = new ArrayList<Number>();
        switch (A.nRows()) {
            case 1:
                result.add(new Double(A.get(1, 1)));
                break;
            case 2:
            case 3:
            case 4:
                result.addAll(new PolyRoot().solve(polynomial));
                break;
            default:
                throw new IllegalArgumentException("by Abel-Ruffini theorem, a general polynomial of order n > 4 cannot be solved by a finite sequence of arithmetic operations and radicals");
        }

        return result;
    }

    @Override
    public String toString() {
        return polynomial.toString();
    }
}
