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
package com.numericalmethod.suanshu.optimization.constrained.integer;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is an implementation of a general Integer Programming problem in which some variables take only integers.
 *
 * @author Haksun Li
 */
public class IPProblemImpl1 implements IPProblem {

    private final ConstrainedOptimProblemImpl1 problem;
    private final int[] integers;
    private final double epsilon;

    /**
     * Construct a constrained optimization problem with integral constraints.
     *
     * @param f        the objective function to be minimized
     * @param equal    the set of equality constraints; Use {@code null} if the set is empty.
     * @param less     the set of less-than-or-equal-to inequality constraints; Use {@code null} if the set is empty.
     * @param integers the set of indices of the integral variables, counting from 1
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public IPProblemImpl1(
            RealScalarFunction f,
            EqualityConstraints equal,
            LessThanConstraints less,
            int[] integers,
            double epsilon) {
        this.problem = new ConstrainedOptimProblemImpl1(f, equal, less);
        this.integers = integers != null ? Arrays.copyOf(integers, integers.length) : new int[0];
        this.epsilon = epsilon;
    }

    /**
     * Construct a constrained optimization problem with integral constraints.
     *
     * @param f        the objective function to be minimized
     * @param equal    the set of equality constraints; Use {@code null} if the set is empty.
     * @param less     the set of less-than-or-equal-to inequality constraints; Use {@code null} if the set is empty.
     * @param integers the set of indices of the integral variables, counting from 1
     */
    public IPProblemImpl1(
            RealScalarFunction f,
            EqualityConstraints equal,
            LessThanConstraints less,
            int[] integers) {
        this(f, equal, less, integers, Constant.EPSILON);
    }

    @Override
    public int[] getIntegerIndices() {
        return Arrays.copyOf(integers, integers.length);
    }

    @Override
    public int[] getNonIntegralIndices(double[] x) {
        ArrayList<Integer> list = new ArrayList<Integer>();

        int[] index = getIntegerIndices();
        for (int i = 0; i < index.length; ++i) {
            int j = index[i];
            if (Math.abs(x[j - 1] - Math.round(x[j - 1])) > epsilon) {
                list.add(j);//x[j] not an integer
            }
        }

        return DoubleUtils.collection2IntArray(list);
    }

    /**
     * Get the index of the first integral variable whose value is not an integer, violating the integral constraints.
     * The indices count from 1.
     *
     * @param x an argument to the objective function
     * @return the index of the first integral variable whose value is not an integer.
     * 0 indicates that the values of all integral variables in <i>x</i> are integers.
     */
    public int getFirstNonIntegralIndices(double[] x) {
        int[] index = getIntegerIndices();
        for (int i = 0; i < index.length; ++i) {
            int j = index[i];
            if (Math.abs(x[j - 1] - Math.round(x[j - 1])) > epsilon) {
                return j;//x[j] not an integer
            }
        }

        return 0;
    }

    @Override
    public double epsilon() {
        return epsilon;
    }

    @Override
    public LessThanConstraints getLessThanConstraints() {
        return problem.getLessThanConstraints();
    }

    @Override
    public EqualityConstraints getEqualityConstraints() {
        return problem.getEqualityConstraints();
    }

    @Override
    public int dimension() {
        return problem.dimension();
    }

    @Override
    public RealScalarFunction f() {
        return problem.f();
    }
}
