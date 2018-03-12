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

import com.numericalmethod.suanshu.datastructure.DimensionCheck;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.QRAlgorithm;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.autoEpsilon;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.NumberUtils;
import static com.numericalmethod.suanshu.number.NumberUtils.isReal;
import java.util.*;

/**
 * Given a <em>square</em> matrix <i>A</i>, an eigenvalue <i>λ</i> and its associated eigenvector <i>v</i> are defined by <i>Av = λv</i>.
 * We first find the eigenvalues and then the eigenvector space by solving a system of homogeneous linear equations.
 * That is, <i>(A - λ)v = 0</i>.
 * <p/>
 * <em>
 * TODO: For the moment, we compute both real and complex eigenvalues,
 * but we do not compute the eigenvectors for complex eigenvalues.
 * </em>
 * <p/>
 * The R equivalent function is {@code eigen}.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Eigenvalue_algorithm">Wikipedia: Eigenvalue algorithm</a>
 */
public class Eigen implements Spectrum {//TODO: refactor this lenghty code

    /**
     * the methods available to compute eigenvalues and eigenvectors
     */
    public static enum Method {

        /**
         * for a matrix of dimension 4 or smaller
         */
        CHARACTERISTIC_POLYNOMIAL,
        /**
         * for a symmetric matrix (<em>Not supported yet.</em>)
         */
        SYMMETRY,
        /**
         * for any matrix
         */
        QR
    }

    private final Matrix A;//a copy of the input; will be changed during computation (side effect)
    private final TreeMap<Number, EigenProperty> map;//sort the eigenvalues in descending order

    /**
     * Compute the eigenvalues and eigenvectors for a <em>square</em> matrix.
     * For each eigenvalue, there are infinitely many associated eigenvectors, which forms a vector space.
     * This implementation computes a set of linearly independent basis, any linear combination of them qualifies as an eigenvector.
     * <p/>
     * <em>
     * TODO: For the moment, we compute both real and complex eigenvalues,
     * but we do not compute the eigenvectors for complex eigenvalues.
     * </em>
     *
     * @param A       a <em>square</em> matrix
     * @param method  the eigen decomposition algorithm, c.f., {@link Method}
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @throws IllegalArgumentException if <i>A</i> is not square
     */
    public Eigen(Matrix A, Method method, final double epsilon) {
        assertArgument(DimensionCheck.isSquare(A), "eigen decomposition applies only to square matrix");

        this.A = new DenseMatrix(A);//make a copy b/c it will change during computation (side effect)
        this.map = new TreeMap<Number, EigenProperty>(
                new Comparator<Number>() {//sort the eigenvalues in decending order

                    @Override
                    public int compare(Number o1, Number o2) {
                        return -NumberUtils.compare(o1, o2, epsilon);//eigenvlaue convention: decending order
                    }
                });

        Spectrum impl;
        //select an implementation here
        switch (method) {
            case CHARACTERISTIC_POLYNOMIAL:
                impl = new CharacteristicPolynomial(this.A);
                break;
            case SYMMETRY:
                throw new UnsupportedOperationException("Not supported yet.");
//                impl = null;
//                break;
            case QR:
            default:
                impl = new QRAlgorithm(this.A, Integer.MAX_VALUE, epsilon);
                break;
        }


        //compute the algebraic multiplicity
        TreeMap<Number, Integer> multiplicity = new TreeMap<Number, Integer>(
                new Comparator<Number>() {//sort the eigenvalues in decending order

                    @Override
                    public int compare(Number o1, Number o2) {
                        return -NumberUtils.compare(o1, o2, epsilon);//eigenvlaue convention: decending order
                    }
                });
        for (Number number : impl.getEigenvalues()) {
            if (!multiplicity.containsKey(number)) {
                multiplicity.put(number, 0);
            }
            multiplicity.put(number, multiplicity.get(number) + 1);
        }
        for (Number number : impl.getEigenvalues()) {
            this.map.put(number, new EigenProperty(number, multiplicity.get(number), A, epsilon));
        }
    }

    /**
     * Compute the eigenvalues and eigenvectors for a <em>square</em> matrix.
     *
     * @param A a <em>square</em> matrix
     * @see <a href="http://en.wikipedia.org/wiki/QR_algorithm">Wikipedia: QR algorithm</a>
     */
    public Eigen(Matrix A) {
        this(A, Method.QR, autoEpsilon(A));
    }

    /**
     * Get the number of distinct eigenvalues.
     *
     * @return the number of distinct eigenvalues
     */
    public int size() {
        return map.keySet().size();
    }

    @Override
    public List<Number> getEigenvalues() {
        List<Number> result = new ArrayList<Number>();
        result.addAll(map.keySet());
        return result;
    }

    /**
     * Get all real eigenvalues.
     * The eigenvalues are sorted in descending order.
     *
     * @return all real eigenvalues
     */
    public double[] getRealEigenvalues() {
        List<Number> result = new ArrayList<Number>();
        for (Number eigenvalue : map.keySet()) {
            if (isReal(eigenvalue)) {
                result.add(eigenvalue);
            }
        }

        return DoubleUtils.collection2DoubleArray(result);
    }

    /**
     * Get the <i>i</i>-th eigenvalue.
     * The eigenvalues are sorted in descending order. The index counts from 0 to agree with the {@code List<Number>} convention.
     *
     * @param i an index, counting from 0
     * @return return the <i>i</i>-th eigenvalue
     */
    public Number getEigenvalue(int i) {
        Iterator<Number> it = map.keySet().iterator();
        for (int j = 0; j < i; ++j) {
            it.next();
        }

        return it.next();
    }

    /**
     * Get the {@link EigenProperty} by eigenvalue.
     * Note that the number passed in must be exactly the same as the eigenvalue in binary representation.
     * Passing in an approximate number (up to precision) will likely result in an unmatched error,
     * i.e., {@code null} returned.
     *
     * @param eigenvalue an eigenvalue
     * @return the {@code EigenProperty} of the eigenvalue
     */
    public EigenProperty getProperty(Number eigenvalue) {
        return map.get(eigenvalue);
    }

    /**
     * Get the <i>i</i>-th {@link EigenProperty}.
     * The eigenvalues are sorted in descending order. The index counts from 0 to agree with the {@code List<Number>} convention.
     *
     * @param i the index, counting from 0
     * @return the <i>i</i>-th {@code EigenProperty}
     */
    public EigenProperty getProperty(int i) {
        return getProperty(getEigenvalue(i));
    }
}
