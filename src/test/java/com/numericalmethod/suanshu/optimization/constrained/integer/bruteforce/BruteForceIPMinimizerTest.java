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

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.optimization.constrained.integer.bruteforce.BruteForceIPProblem.IntegerDomain;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.optimization.constrained.constraint.general.GeneralEqualityConstraints;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class BruteForceIPMinimizerTest {

    /**
     * one out of the two variable is an integer, consecutive
     */
    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        ArrayList<IntegerDomain> domains = new ArrayList<IntegerDomain>();
        domains.add(new BruteForceIPProblem.IntegerDomain(1, -5, 5));//x0 ∈ [-5, 5]
        BruteForceIPProblem problem = new BruteForceIPProblem(f, domains.toArray(new IntegerDomain[0]), 1e-14);
        BruteForceIPMinimizer solver = new BruteForceIPMinimizer(1e-14, 20);
        BruteForceIPMinimizer.Solution soln = solver.solve(problem);
        Vector minimizer = soln.search(new DenseVector(-5.0, -5.0));
        double min = f.evaluate(minimizer);

        assertEquals(0, min, 0);
        assertEquals(min, soln.minimum(), 0);
    }

    /**
     * one out of the two variable is an integer, consecutive
     * with constraints
     */
    @Test
    public void test_0015() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        GeneralEqualityConstraints equal = new GeneralEqualityConstraints(new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) + x.get(2) - 1;//x + y = 1
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        });

        BruteForceIPProblem problem = new BruteForceIPProblem(f, equal, null,
                                                              new IntegerDomain[]{new BruteForceIPProblem.IntegerDomain(1, -5, 5)}, //x0 ∈ [-5, 5]
                                                              1e-14);
        BruteForceIPMinimizer solver = new BruteForceIPMinimizer(1e-14, 20);
        BruteForceIPMinimizer.Solution soln = solver.solve(problem);
        Vector minimizer = soln.search(new DenseVector(-5.0, -5.0));
        double min = f.evaluate(minimizer);

        assertEquals(1, min, 0);
        assertEquals(min, soln.minimum(), 0);
    }

    /**
     * one out of the two variable is an integer, non-consecutive
     */
    @Test
    public void test_0020() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        BruteForceIPProblem problem = new BruteForceIPProblem(
                f,
                new IntegerDomain[]{new BruteForceIPProblem.IntegerDomain(1, new int[]{-5, -4, -3, -2, -1, 1, 2, 3, 4, 5})},//x0
                1e-14);
        BruteForceIPMinimizer solver = new BruteForceIPMinimizer(1e-14, 20);
        BruteForceIPMinimizer.Solution soln = solver.solve(problem);
        Vector minimizer = soln.search(new DenseVector(-5.0, -5.0));
        double min = f.evaluate(minimizer);

        assertEquals(1, min, 0);
        assertEquals(min, soln.minimum(), 0);
    }

    /**
     * both variables are integers
     */
    @Test
    public void test_0030() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        BruteForceIPProblem problem = new BruteForceIPProblem(
                f,
                new IntegerDomain[]{
                    new BruteForceIPProblem.IntegerDomain(1, -5, 5),//x0 ∈ [-5, 5]
                    new BruteForceIPProblem.IntegerDomain(2, -5, 5)//x1 ∈ [-5, 5]
                },
                1e-14);
        BruteForceIPMinimizer solver = new BruteForceIPMinimizer(1e-14, 20);
        BruteForceIPMinimizer.Solution soln = solver.solve(problem);
        Vector minimizer = soln.search(new DenseVector(-5.0, -5.0));
        double min = f.evaluate(minimizer);

        assertEquals(0, min, 0);
        assertEquals(min, soln.minimum(), 0);
    }

    /**
     * both variables are integers
     * with constraints
     */
    @Test
    public void test_0035() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        GeneralEqualityConstraints equal = new GeneralEqualityConstraints(new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) + x.get(2) - 1;//x + y = 1
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        });


        IntegerDomain integer0 = new BruteForceIPProblem.IntegerDomain(1, -5, 5);//x0 ∈ [-5, 5]
        IntegerDomain integer1 = new BruteForceIPProblem.IntegerDomain(2, -5, 5);//x1 ∈ [-5, 5]
        ArrayList<IntegerDomain> domains = new ArrayList<IntegerDomain>();
        domains.add(integer0);
        domains.add(integer1);

        BruteForceIPProblem problem = new BruteForceIPProblem(f, equal, null, domains.toArray(new IntegerDomain[0]), 1e-14);
        BruteForceIPMinimizer solver = new BruteForceIPMinimizer(1e-14, 20);
        BruteForceIPMinimizer.Solution soln = solver.solve(problem);
        Vector minimizer = soln.search(new DenseVector(-5.0, -5.0));
        double min = f.evaluate(minimizer);

        assertEquals(1, min, 0);
        assertEquals(min, soln.minimum(), 0);
    }

    /**
     * no integral constraints
     */
    @Test
    public void test_0040() throws Exception {
        RealScalarFunction f = new RealScalarFunction() {

            public Double evaluate(Vector x) {
                return x.get(1) * x.get(1) + x.get(2) * x.get(2);
            }

            public int dimensionOfDomain() {
                return 2;
            }

            public int dimensionOfRange() {
                return 1;
            }
        };

        BruteForceIPProblem problem = new BruteForceIPProblem(f, new IntegerDomain[0], 0);
        BruteForceIPMinimizer solver = new BruteForceIPMinimizer(1e-14, 100);
        BruteForceIPMinimizer.Solution soln = solver.solve(problem);
        Vector minimizer = soln.search(new DenseVector(-5.0, -5.0));
        double min = f.evaluate(minimizer);

        assertEquals(0, min, 1e-13);
        assertEquals(min, soln.minimum(), 1e-15);
    }
}
