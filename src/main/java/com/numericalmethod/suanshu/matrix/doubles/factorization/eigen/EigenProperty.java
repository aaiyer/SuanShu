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

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LinearSystemSolver;
import com.numericalmethod.suanshu.number.NumberUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code Eigen.Property} is a read-only structure that contains the information
 * about a particular eigenvalue,
 * such as its multiplicity and eigenvectors.
 *
 * @author Haksun Li
 */
public class EigenProperty {

    /** the eigenvalue, real or complex */
    private final Number eigenvalue;
    /** algebraic multiplicity of an eigenvalue is defined as the multiplicity of the
     * root of the characteristic polynomial */
    private final int multiplicity;
    /**
     * list of basis of the eigenvector space of this eigenvalue;
     * geometric multiplicity is the number of linearly independent eigenvectors
     */
    private List<Vector> eigenBasis = new ArrayList<Vector>();

    /**
     * Compute the relevant information for an eigenvalue.
     *
     * @param eigenvalue   an eigenvalue
     * @param multiplicity the algebraic multiplicity
     * @param A            the matrix
     * @param epsilon      a precision parameter: when a number |x| ≤ ε, it is
     * considered 0
     */
    EigenProperty(Number eigenvalue, int multiplicity, Matrix A, double epsilon) {
        this.eigenvalue = eigenvalue;
        this.multiplicity = multiplicity;
        if (NumberUtils.isReal(eigenvalue)) {//TODO: complex eigen values and vectors?
            this.eigenBasis.addAll(getEigenVectors(eigenvalue.doubleValue(), multiplicity, A, epsilon));
        }
    }

    //TODO: what is the proper way to numerically compute the eigenvectors?
    private ArrayList<Vector> getEigenVectors(double eigenvalue, int multiplicity, Matrix A, double epsilon) {
        double eps = epsilon != 0 ? epsilon : 1e-15;
        ArrayList<Vector> eigenvectors = new ArrayList<Vector>();

        //TODO: extend the support to complex eigenvalues
        //first, find all linearly non-dependent eigenvectors
        Matrix B = A.minus(A.ONE().scaled(eigenvalue)); //(A - λI)
        /*
         * TODO:
         * Is there a better way to determine precision?
         * We know that the B matrix must be of deficient rank.
         * We thus need to somehow choose the 'big enough' eps to
         * correctly determine
         * the rank of A, hence the kernel, and the null space (homogeneous
         * soln).
         */
        LinearSystemSolver.Solution soln = null;
        for (int i = 0; eigenvectors.size() < multiplicity; ++i) {
            //TODO: i may overflow when it keeps multiplying
            LinearSystemSolver solver = new LinearSystemSolver(eps * Math.pow(10.0, i));//solve for (A - λI)x = 0
            soln = solver.solve(B);
            eigenvectors.clear();
            eigenvectors.addAll(soln.getHomogeneousSoln());
            assert i < 20 : "logical error: cannot solve for the eigen basis";
        }
//        //handle the case where there is a repeated eigenvalue, but are not enough associated linearly independent eigenvectors
//        if (multiplicity > 1) {
//            //second, find the linearly dependent eigenvectors
//            for (Vector b = eigenvectors.get(0), v; eigenvectors.size() < multiplicity; b = v) {
//                v = soln.getParticularSolution(b);
//                eigenvectors.add(v);
//            }
//        }

        return eigenvectors;
    }

    /**
     * Get the eigenvalue.
     *
     * @return the eigenvalue
     */
    public Number eigenvalue() {
        return eigenvalue;
    }

    /**
     * Get the multiplicity of the eigenvalue (a root) of the characteristic
     * polynomial.
     *
     * @return the algebraic multiplicity
     */
    public int algebraicMultiplicity() {
        return multiplicity;
    }

    /**
     * Get the dimension of the vector space spanned by the eigenvectors.
     *
     * @return the geometric multiplicity
     */
    public int geometricMultiplicity() {
        return eigenBasis.size();
    }

    /**
     * Get the eigenvectors.
     *
     * @return the eigenvectors
     */
    public List<Vector> eigenbasis() {
        return new ArrayList<Vector>(eigenBasis);
    }

    /**
     * Get an eigenvector.
     * Note that eigenvector is not unique.
     * This implementation always returns the first vector in the basis.
     * To get a complete set of the basis of the eigenvector space, use {@link #eigenbasis()}.
     *
     * @return an eigenvector
     */
    public Vector eigenVector() {
        return eigenBasis.get(0);
    }
}
