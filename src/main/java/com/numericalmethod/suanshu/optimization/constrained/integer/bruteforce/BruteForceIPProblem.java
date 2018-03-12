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
package com.numericalmethod.suanshu.optimization.constrained.integer.bruteforce;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.optimization.constrained.constraint.EqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.LessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.integer.IPProblemImpl1;
import java.util.Arrays;

/**
 * This implementation is an integral constrained minimization problem that has enumerable integral domains.
 * That is, the integral variables have known and bounded integral domains.
 *
 * @author Haksun Li
 */
public class BruteForceIPProblem extends IPProblemImpl1 {

    /**
     * This specifies the integral domain for an integral variable,
     * i.e., the integer values the variable can take.
     */
    public static class IntegerDomain {

        /**
         * the index of an integral variable
         */
        public final int index;
        /**
         * the integer values the variable can take
         */
        public final int[] domain;

        /**
         * Construct the integral domain for an integral variable.
         *
         * @param index  the index of an integral variable
         * @param domain the integer values the variable can take
         */
        public IntegerDomain(int index, int[] domain) {
            this.index = index;
            this.domain = Arrays.copyOf(domain, domain.length);
        }

        /**
         * Construct the integral domain for an integral variable.
         *
         * @param index the index of an integral variable
         * @param lower the lower bound of the integral domain
         * @param upper the upperbound of the integral domain
         */
        public IntegerDomain(int index, int lower, int upper) {
            this(index, R.seq(lower, upper));
        }

        /**
         * Construct the integral domain for an integral variable.
         *
         * @param index the index of an integral variable
         * @param lower the lower bound of the integral domain
         * @param upper the upperbound of the integral domain
         * @param inc   the increment
         */
        public IntegerDomain(int index, int lower, int upper, int inc) {
            this(index, R.seq(lower, upper, inc));
        }
    }

    private IntegerDomain[] integers;

    /**
     * Construct an integral constrained minimization problem with explicit integral domains.
     *
     * @param f        the objective function to be minimized
     * @param equal    the set of equality constraints. Use {@code null} if the set is empty.
     * @param less     the set of less-than-or-equal-to inequality constraints. Use {@code null} if the set is empty.
     * @param integers the integral constraints
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public BruteForceIPProblem(
            RealScalarFunction f,
            EqualityConstraints equal,
            LessThanConstraints less,
            IntegerDomain[] integers,
            double epsilon) {
        super(f, equal, less, getIntegerIndices(integers), epsilon);
        this.integers = Arrays.copyOf(integers, integers.length);
    }

    /**
     * Construct an integral constrained minimization problem with explicit integral domains.
     *
     * @param f        the objective function to be minimized
     * @param integers the integral constraints
     * @param epsilon  a precision parameter: when a number |x| ≤ ε, it is considered 0
     */
    public BruteForceIPProblem(
            RealScalarFunction f,
            IntegerDomain[] integers,
            double epsilon) {
        this(f, null, null, integers, epsilon);
    }

    /**
     * Get the integral domain of a particular integral variable.
     *
     * @param index the index of the integral variable, counting from 1
     * @return the integral domain
     */
    public IntegerDomain getIntegralConstraint(int index) {
        for (IntegerDomain constraint : integers) {
            if (constraint.index == index) {
                return constraint;
            }
        }

        throw new RuntimeException(String.format("no corresponding integer constraint for the integral variable %d",
                                                 index));
    }

    private static int[] getIntegerIndices(IntegerDomain[] integers) {
        int[] indices = new int[integers.length];

        int i = 0;
        for (IntegerDomain domain : integers) {
            indices[i++] = domain.index;
        }

        return indices;
    }
}
