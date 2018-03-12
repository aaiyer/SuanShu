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
package com.numericalmethod.suanshu.optimization.constrained.general.penaltymethod;

import com.numericalmethod.suanshu.analysis.function.rn2r1.BivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.optimization.constrained.constraint.general.GeneralEqualityConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.general.GeneralLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.NelderMead;
import com.numericalmethod.suanshu.optimization.unconstrained.conjugatedirection.ConjugateGradient;
import com.numericalmethod.suanshu.optimization.unconstrained.quasinewton.BFGS;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class PenaltyMethodMinimizerTest {

    /**
     * Use BFGS.
     */
    @Test
    public void test_0010() throws Exception {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {//f = (x+1)^2 + (y+1)^2
                return (x + 1) * (x + 1) + (y + 1) * (y + 1);
            }
        };

        RealScalarFunction c1 = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {//y = 0
                return y;
            }
        };

        RealScalarFunction c2 = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {//x >= 1
                return 1 - x;
            }
        };

        ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(f,
                new GeneralEqualityConstraints(c1),
                new GeneralLessThanConstraints(c2));

        PenaltyMethodMinimizer optim = new PenaltyMethodMinimizer(PenaltyMethodMinimizer.DEFAULT_PENALTY_FUNCTION_FACTORY,
                1e30,
                new BFGS(false, 1e-8, 200));
        IterativeMinimizer<Vector> soln = optim.solve(problem);
        Vector xmin = soln.search(new DenseVector(new double[]{0, 0}));

        double fxmin = f.evaluate(xmin);

        assertArrayEquals(new double[]{1, 0}, xmin.toArray(), 1e-8);//less precision than using NelderMead
        assertEquals(5, fxmin, 1e-7);//less precision than using NelderMead
    }

    /**
     * Use defaults.
     */
    @Test
    public void test_0020() throws Exception {
        RealScalarFunction f = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {//f = (x+1)^2 + (y+1)^2
                return (x + 1) * (x + 1) + (y + 1) * (y + 1);
            }
        };
        RealScalarFunction c1 = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {//y = 0
                return y;
            }
        };

        RealScalarFunction c2 = new BivariateRealFunction() {

            @Override
            public double evaluate(double x, double y) {//x >= 1
                return 1 - x;
            }
        };

        ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(f,
                new GeneralEqualityConstraints(c1),
                new GeneralLessThanConstraints(c2));

        PenaltyMethodMinimizer optim = new PenaltyMethodMinimizer(
                PenaltyMethodMinimizer.DEFAULT_PENALTY_FUNCTION_FACTORY,
                1e30,
                new NelderMead(1e-8, 200));
        IterativeMinimizer<Vector> soln = optim.solve(problem);
        Vector xmin = soln.search(new DenseVector(new double[]{0, 0}));

        double fxmin = f.evaluate(xmin);

        assertArrayEquals(new double[]{1, 0}, xmin.toArray(), 1e-9);
        assertEquals(5, fxmin, 1e-8);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-8);
    }

    /**
     * minimize a univariate function with a customized solver
     *
     * @see R. Fletcher. Example 22.1. Practical Methods of Optimization. 2nd ed. Wiley. May 2000.
     */
    @Test
    public void test_0030() throws Exception {
        RealScalarFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {//f = (x+2)^2
                return pow((x + 2), 2);
            }
        };
        RealScalarFunction c1 = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x - 2;
            }
        };

        RealScalarFunction c2 = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return -pow((x + 1), 3);
            }
        };

        ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(f,
                null,
                new GeneralLessThanConstraints(c1, c2));

        PenaltyMethodMinimizer optim = new PenaltyMethodMinimizer(
                PenaltyMethodMinimizer.DEFAULT_PENALTY_FUNCTION_FACTORY,
                1e30,
                new NelderMead(1e-8, 200));
        IterativeMinimizer<Vector> soln = optim.solve(problem);
        Vector xmin = soln.search(
                new DenseVector(new double[]{-2}),//lower bound
                new DenseVector(new double[]{0}),//initial guess
                new DenseVector(new double[]{1}));//upper bound

        double fxmin = f.evaluate(xmin);

        assertArrayEquals(new double[]{-1}, xmin.toArray(), 1e-6);
        assertEquals(1, fxmin, 1e-5);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-5);
    }

    /**
     * minimize a univariate function using NelderMead which is not designed for optimizing univariate functions
     *
     * @see R. Fletcher. Example 22.1. Practical Methods of Optimization. 2nd ed. Wiley. May 2000.
     */
    @Test
    public void test_0040() throws Exception {
        RealScalarFunction f = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {//f = (x+2)^2
                return pow((x + 2), 2);
            }
        };
        RealScalarFunction c1 = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return x - 2;
            }
        };

        RealScalarFunction c2 = new UnivariateRealFunction() {

            @Override
            public double evaluate(double x) {
                return -pow((x + 1), 3);
            }
        };

        ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(f,
                null,
                new GeneralLessThanConstraints(c1, c2));

        PenaltyMethodMinimizer optim = new PenaltyMethodMinimizer(
                PenaltyMethodMinimizer.DEFAULT_PENALTY_FUNCTION_FACTORY,
                1e30,
                new NelderMead(1e-8, 200));
        IterativeMinimizer<Vector> soln = optim.solve(problem);
        Vector xmin = soln.search(new DenseVector(new double[]{0}));

        double fxmin = f.evaluate(xmin);

        assertArrayEquals(new double[]{-1}, xmin.toArray(), 1e-6);
        assertEquals(1, fxmin, 1e-5);
        assertEquals(f.evaluate(xmin), soln.minimum(), 1e-5);
    }

    /**
     * from "warori", complaining returning a null value
     *
     * @see http://numericalmethod.com/forum/index.php?PHPSESSID=melhq1s18mn3otthp26863t926&topic=48.0
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_0050() throws Exception {
        final double[] variables = new double[500];
//        final int finalcounter;

//        final int fdimension = ((numberofvariables - 6) / 3) + 1;
        final int fdimension = ((variables.length - 6) / 3) + 1;

        //here goes the function to be maximized
        RealScalarFunction f = new RealScalarFunction() {

            public int dimensionOfDomain() {
                return fdimension;
            }

            public int dimensionOfRange() {
                return 1;
            }

            @Override
            public Double evaluate(Vector z) {
                double[] x = z.toArray();
                double result = 0.0;
                if ((variables[0] < 1.5) && (variables[0] > 0)) {
                    result = result + (variables[1] * (x[0] - variables[4]));
                    int numbertocount1 = fdimension - 1;
                    for (int i = 0, j = 0; j < numbertocount1; i += 3, j += 1) {
                        result += (variables[1] * variables[i + 8] * (x[j + 1] - variables[i + 6]));
                        result += (variables[2] * (x[j + 1] - variables[i + 6]));
                        result -= (1 + (variables[2] * (x[j + 1] - variables[i + 6]))) * Math.log(1 + (variables[2] * (x[j + 1] - variables[i + 6])));
                    }
                }
                if ((variables[0] > 1.6) && (variables[0] < 2.5)) {
                    result = result + (variables[1] * (x[0] - variables[4]));
                    int numbertocount1 = fdimension - 1;
                    for (int i = 0, j = 0; j < numbertocount1; i += 3, j += 1) {
                        result += (variables[1] * variables[i + 8] * (x[j + 1] - variables[i + 6]));
                        result += (variables[2] * (x[j + 1] - variables[i + 6]));
                        result -= (1 + (variables[2] * (x[j + 1] - variables[i + 6]))) * Math.log(1 + (variables[2] * (x[j + 1] - variables[i + 6])));
                    }
                }
                if (variables[0] > 2.7) {
                    double g = 4 * variables[3];
                    double partresult = 0.0;
                    result = result + (g * (x[0] - variables[4]));
                    int numbertocount1 = fdimension - 1;
                    for (int i = 0, j = 0; j < numbertocount1; i += 3, j += 1) {
                        result += (g * variables[i + 8] * (x[j + 1] - variables[i + 6]));
                        partresult += (x[j + 1] - variables[i + 6]);
                    }
                    result = result - (2 * partresult * partresult);
                }
                if ((variables[0] < 0) && (variables[0] > -1.5)) {
                    result = result + (variables[1] * (x[0] - variables[4]));
                    int numbertocount1 = fdimension - 1;
                    for (int i = 0, j = 0; j < numbertocount1; i += 3, j += 1) {
                        result += (variables[1] * variables[i + 8] * (x[j + 1] - variables[i + 6]));
                        result += (variables[2] * (x[j + 1] - variables[i + 6]));
                        result -= (1 + (variables[2] * (x[j + 1] - variables[i + 6]))) * Math.log(1 + (variables[2] * (x[j + 1] - variables[i + 6])));
                    }
                }
                if ((variables[0] < -1.6) && (variables[0] > -2.5)) {
                    result = result + (variables[1] * (x[0] - variables[4]));
                    int numbertocount1 = fdimension - 1;
                    for (int i = 0, j = 0; j < numbertocount1; i += 3, j += 1) {
                        result += (variables[1] * variables[i + 8] * (x[j + 1] - variables[i + 6]));
                        result += (variables[2] * (x[j + 1] - variables[i + 6]));
                        result -= (1 + (variables[2] * (x[j + 1] - variables[i + 6]))) * Math.log(1 + (variables[2] * (x[j + 1] - variables[i + 6])));
                    }
                }
                if (variables[0] < -2.7) {
                    double g = 4 * variables[3];
                    double partresult = 0.0;
                    result = result + (g * (x[0] - variables[4]));
                    int numbertocount1 = fdimension - 1;
                    for (int i = 0, j = 0; j < numbertocount1; i += 3, j += 1) {
                        result += (g * variables[i + 8] * (x[j + 1] - variables[i + 6]));
                        partresult += (x[j + 1] - variables[i + 6]);
                    }
                    result = result - (2 * partresult * partresult);
                }
                return -result;
            }
        };

        //here go the constraints to be satisfied
        RealScalarFunction[] test = new RealScalarFunction[fdimension];
        if (variables[0] < 0) {
//            int finalcounter = 0;
            for (int i = 0; i < fdimension; i++) {
                final int finalcounter = i;
                test[finalcounter] = new RealScalarFunction() {
                    //non-negativity constraint

                    @Override
                    public int dimensionOfDomain() {
                        return fdimension;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }

                    @Override
                    public Double evaluate(Vector x) {
                        return (-1 * x.get(finalcounter + 1));
                    }
                };
            }

        }

        if (variables[0] > 0) {
//            int finalcounter = 0;
            for (int i = 0; i < fdimension; i++) {
                final int finalcounter = i;
                test[finalcounter] = new RealScalarFunction() {
                    //limitation on the negative values which can be realized

                    @Override
                    public int dimensionOfDomain() {
                        return fdimension;
                    }

                    public int dimensionOfRange() {
                        return 1;
                    }

                    @Override
                    public Double evaluate(Vector x) {
                        return (-100000000 - (1 * x.get(finalcounter + 1)));
                    }
                };
            }
        }

        RealScalarFunction c16 = new RealScalarFunction() {
            //this is a balance sheet constraint

            @Override
            public int dimensionOfDomain() {
                return fdimension;
            }

            @Override
            public int dimensionOfRange() {
                return 1;
            }

            @Override
            public Double evaluate(Vector x) {
                double result = 0.0;
                result = variables[5] - x.get(1);
                for (int i = 2, j = 7; i <= fdimension; i++, j += 3) {
                    result -= (x.get(i) * variables[j]);
                }
                return result;
            }
        };

        //Problem formulation
        ConstrainedOptimProblemImpl1 ConstrainedProblem = new ConstrainedOptimProblemImpl1(f, new GeneralEqualityConstraints(c16), new GeneralLessThanConstraints(test));
//        PenaltyFunction a = new Courant(new GeneralEqualityConstraints(c16));
//        PenaltyFunction b = new Fletcher(new GeneralLessThanConstraints(test));
//        SumOfPenalties sp = new SumOfPenalties(a, b);
        PenaltyMethodMinimizer optim = new PenaltyMethodMinimizer(
                PenaltyMethodMinimizer.DEFAULT_PENALTY_FUNCTION_FACTORY,
                1e30,
                new ConjugateGradient(1e-15, 1000));
        IterativeMinimizer<Vector> soln = optim.solve(ConstrainedProblem);

        double[] inputarray = new double[fdimension];//initial values
        inputarray[0] = variables[4];
        for (int i = 1, j = 6; i < fdimension; i++, j += 3) {
            inputarray[i] = variables[j];
        }
        Vector xmin = new DenseVector(inputarray);
        xmin = soln.search(xmin);
    }
}
