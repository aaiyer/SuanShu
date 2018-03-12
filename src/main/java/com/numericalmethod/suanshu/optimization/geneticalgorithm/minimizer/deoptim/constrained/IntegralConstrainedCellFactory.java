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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.constrained;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.constrained.integer.IPProblem;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.deoptim.DEOptimCellFactory;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.HashSet;
import java.util.Set;

/**
 * This implementation defines the constrained Differential Evolution operators that solve an Integer Programming problem.
 *
 * @author Haksun Li
 */
public class IntegralConstrainedCellFactory extends ConstrainedCellFactory {//TODO: make consistent with IPProblem

    /**
     * The integral constraints are defined by implementing this {@code interface}.
     */
    public interface IntegerConstraint {

        public Vector round(Vector x);
    }

    /**
     * This integral constraint makes all variables in the objective function integral variables.
     */
    public static class AllIntegers implements IntegerConstraint {

        @Override
        public Vector round(Vector x) {
            Vector z = new DenseVector(x.size());
            for (int i = 1; i <= x.size(); ++i) {
                z.set(i, Math.round(x.get(i)));
            }

            return z;
        }
    };

    /**
     * This integral constraint makes some variables in the objective function integral variables.
     */
    public static class SomeIntegers implements IntegerConstraint {

        private final Set<Integer> indices = new HashSet< Integer>();//the integral indices

        /**
         * Construct the integral constraint from an Integer Programming problem.
         *
         * @param problem an Integer Programming problemI
         */
        public SomeIntegers(IPProblem problem) {//TODO: get rid of the parameter 'problem'
            int[] indices0 = problem.getIntegerIndices();//counting from 1
            for (int i : indices0) {
                this.indices.add(i);
            }
        }

        @Override
        public Vector round(Vector x) {
            Vector z = new DenseVector(x.size());
            for (int i = 1; i <= x.size(); ++i) {
                if (indices.contains(i)) {
                    z.set(i, Math.round(x.get(i)));
                }
            }

            return z;
        }
    };

    private final IntegerConstraint constraint;

    /**
     * Construct an instance of {@code IntegralConstrainedCellFactory}.
     *
     * @param factory    the Differential Operators for unconstrained optimization
     * @param constraint the integral constraints
     */
    public IntegralConstrainedCellFactory(DEOptimCellFactory factory, IntegerConstraint constraint) {
        super(factory);
        this.constraint = constraint;
    }

    @Override
    public ConstrainedCell getSimpleCell(RealScalarFunction f, Vector x) {
        Vector z = constraint.round(x);
        return new ConstrainedCell(f, z);
    }
}
