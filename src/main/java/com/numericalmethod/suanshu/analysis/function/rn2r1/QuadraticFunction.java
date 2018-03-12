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
package com.numericalmethod.suanshu.analysis.function.rn2r1;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * A quadratic function takes this form: \(f(x) = \frac{1}{2} \times x'Hx + x'p + c\).
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Section 13.2, Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Quadratic_function">Wikipedia: Quadratic function</a>
 * </ul>
 */
public class QuadraticFunction implements RealScalarFunction {

    private final ImmutableMatrix H;
    private final ImmutableVector p;
    private final double c;

    /**
     * Construct a quadratic function of this form: \(f(x) = \frac{1}{2} \times x'Hx + x'p + c\).
     *
     * @param H a symmetric, positive semi-definite matrix
     * @param p a vector
     * @param c a constant
     */
    public QuadraticFunction(Matrix H, Vector p, double c) {
        this.H = new ImmutableMatrix(H);
        this.p = new ImmutableVector(p);
        this.c = c;
    }

    /**
     * Construct a quadratic function of this form: \(f(x) = \frac{1}{2} \times x'Hx + x'p\).
     *
     * @param H a symmetric, positive semi-definite matrix
     * @param p a vector
     */
    public QuadraticFunction(Matrix H, Vector p) {
        this(H, p, 0);
    }

    /**
     * Copy constructor.
     *
     * @param f a quadratic function
     */
    public QuadraticFunction(QuadraticFunction f) {
        this(f.H, f.p, f.c);
    }

    public ImmutableMatrix Hessian() {
        return H;
    }

    public ImmutableVector p() {
        return p;
    }

    @Override
    public Double evaluate(Vector z) {
        Matrix x = new DenseMatrix(z);
        Matrix xt = x.t();

        Matrix xtHx = xt.multiply(H).multiply(x);
        Vector xtp = xt.multiply(p);

        double f = 0.5 * xtHx.get(1, 1) + xtp.get(1) + c;
        return f;
    }

    @Override
    public int dimensionOfDomain() {
        return p.size();
    }

    @Override
    public int dimensionOfRange() {
        return 1;
    }

    @Override
    public String toString() {
        return String.format("1/2 * x'%sx + x'%s", H, p);
    }
}
