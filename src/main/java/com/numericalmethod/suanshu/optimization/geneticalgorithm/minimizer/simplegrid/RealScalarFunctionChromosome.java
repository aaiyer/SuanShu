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
package com.numericalmethod.suanshu.optimization.geneticalgorithm.minimizer.simplegrid;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.geneticalgorithm.Chromosome;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This chromosome encodes a real valued function.
 *
 * @author Haksun Li
 */
public abstract class RealScalarFunctionChromosome implements Chromosome {

    private Double fx = null;
    private final RealScalarFunction f;
    private final ImmutableVector x;

    /**
     * Construct an instance of {@code RealScalarFunctionChromosome}.
     *
     * @param f the objective function
     * @param x a candidate solution
     */
    public RealScalarFunctionChromosome(RealScalarFunction f, Vector x) {
        this.f = f;
        this.x = new ImmutableVector(new DenseVector(x));
    }

    /**
     * Get the objective function.
     *
     * @return the objective function
     */
    public RealScalarFunction f() {
        return f;
    }

    /**
     * Get the candidate solution.
     *
     * @return the candidate solution
     */
    public ImmutableVector x() {
        return x;
    }

    @Override
    public double fitness() {
        if (fx == null) {//lazy evaluation
            this.fx = f.evaluate(x);

            if (this.fx == null) {//evaluate only once
                this.fx = Double.NaN;
            }
        }

        return fx;
    }

    @Override
    public int compareTo(Chromosome that) {
        if (this.fitness() < that.fitness()) {
            return -1;
        } else if (this.fitness() > that.fitness()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("x[]: ");

        str.append(x.toArray());
        str.append(", ");

        if (fx != null) {
            str.append("; f(x) = ");
            str.append(fx);
        }

        return str.toString();
    }
}
