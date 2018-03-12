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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.Constraints;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.operation.CreateVector;
import static java.lang.Math.abs;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * This is a collection of linear constraints for a real-valued optimization problem.
 *
 * @author Haksun Li
 */
public abstract class LinearConstraints implements Constraints {

    private final ImmutableMatrix A;
    private final ImmutableVector b;

    /**
     * Construct a collection of linear constraints.
     *
     * @param A the constraint coefficients
     * @param b the constraint values
     */
    public LinearConstraints(Matrix A, Vector b) {
        this.A = new ImmutableMatrix(A);
        this.b = new ImmutableVector(b);
    }

    @Override
    public ArrayList<RealScalarFunction> getConstraints() {
        ArrayList<RealScalarFunction> f = new ArrayList<RealScalarFunction>();

        for (int i = 1; i <= A.nRows(); ++i) {
            final int j = i;//make the complier happy
            RealScalarFunction g = new RealScalarFunction() {

                @Override
                public Double evaluate(Vector x) {
                    Vector a = A.getRow(j);
                    double ax = a.innerProduct(x);
                    return ax - b.get(j);
                }

                @Override
                public int dimensionOfDomain() {
                    return A.nCols();
                }

                @Override
                public int dimensionOfRange() {
                    return 1;
                }
            };

            f.add(g);
        }

        return f;
    }

    @Override
    public int dimension() {
        return A.nCols();
    }

    @Override
    public int size() {
        return A.nRows();
    }

    /**
     * Get the constraint coefficients.
     *
     * @return the constraint coefficients
     */
    public ImmutableMatrix A() {
        return A;
    }

    /**
     * Get the constraint values.
     *
     * @return the constraint values
     */
    public ImmutableVector b() {
        return b;
    }

    /**
     * Get the active constraint indices.
     * The active constraints are those with a_i(x) = 0.
     *
     * @param x       a point or a potential solution
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return the active (row) indices
     */
    public ArrayList<Integer> getActiveRows(Vector x, double epsilon) {
        ArrayList<Integer> rows = new ArrayList<Integer>();

        for (int i = 1; i <= A.nRows(); ++i) {
            Vector a = A.getRow(i);
            double ax = a.innerProduct(x) - b.get(i);
            if (abs(ax) < epsilon) {
                rows.add(i);
            }
        }

        return rows;
    }

    /**
     * Get the active constraint.
     * The active constraints are those with a_i(x) = 0.
     *
     * @param x       a point or a potential solution
     * @param epsilon a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @return the active constraints
     */
    public Matrix getActiveConstraints(Vector x, double epsilon) {
        int[] rows = DoubleUtils.collection2IntArray(getActiveRows(x, epsilon));
        int[] cols = R.seq(1, A.nCols());
        Matrix Aa = CreateMatrix.subMatrix(A, rows, cols);
        return Aa;
    }

    /**
     * Concatenate collections of linear constraints into one collection.
     *
     * @param groups collections of linear constraints
     * @return a collection of linear constraints
     */
    public static LinearConstraints concat(LinearConstraints... groups) {
        Matrix[] As = new Matrix[groups.length];
        Vector[] bs = new Vector[groups.length];

        Class<? extends LinearConstraints> clazz = null;

        for (int i = 0; i < groups.length; ++i) {
            if (groups[i] != null) {
                if (clazz == null) {
                    clazz = groups[i].getClass();
                } else {
                    SuanShuUtils.assertArgument(clazz.isAssignableFrom(groups[i].getClass()), "gropus must all have the same constraint type");
                }

                As[i] = groups[i].A();
                bs[i] = groups[i].b();
            }
        }

        Matrix Anew = CreateMatrix.rbind(As);
        Vector bnew = CreateVector.concat(bs);

        try {
            Constructor<?> ctor = clazz.getConstructor(Matrix.class, Vector.class);
            LinearConstraints n2 = (LinearConstraints) ctor.newInstance(Anew, bnew);
            return n2;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(A().toString());

        if (this.getClass() == LinearGreaterThanConstraints.class) {
            str.append(">=");
        } else if (this.getClass() == LinearLessThanConstraints.class) {
            str.append("<=");
        } else if (this.getClass() == LinearEqualityConstraints.class) {
            str.append("=");
        } else {
            throw new RuntimeException("unrecognized LinearConstraints subclass");
        }

        str.append("\n");
        str.append(b().toString());

        return str.toString();
    }
}
