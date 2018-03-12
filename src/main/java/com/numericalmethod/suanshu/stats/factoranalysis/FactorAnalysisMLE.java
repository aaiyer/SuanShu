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
package com.numericalmethod.suanshu.stats.factoranalysis;

import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.diagonal.DiagonalMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.foreach;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import static com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath.min;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LinearLessThanConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.LowerBoundConstraints;
import com.numericalmethod.suanshu.optimization.constrained.constraint.linear.UpperBoundConstraints;
import com.numericalmethod.suanshu.optimization.constrained.general.sqp.activeset.SQPActiveSetSolver;
import com.numericalmethod.suanshu.optimization.constrained.problem.ConstrainedOptimProblemImpl1;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblemImpl;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.optimization.unconstrained.NelderMead;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import java.util.Arrays;

/**
 * This is the MLE optimization used in {@linkplain FactorAnalysis factor analysis}.
 * Letting <i>k</i> denote the number of factors to be fitted in factor analysis,
 * the log-likelihood <i>F<sub>k</sub></i> is defined in (4.2) on p.26 of Lawley and Maxwell (1971).
 * In finding the minimum of <i>F<sub>k</sub></i>, it is convenient to use a two-stage procedure.
 * First we find the conditional minimum for given Psi, denoted by <i>f<sub>k</sub></i>, i.e.
 * (4.16) on p. 29 of Lawley and Maxwell (1971).
 * Then we find the Psi that minimizes <i>f<sub>k</sub></i>.
 *
 * @author Kevin Sun
 * @see
 * <ul>
 * <li>D. N. Lawley and A. E. Maxwell, "Factor Analysis as a Statistical Method", Second Edition, Butterworths, 1971.
 * <li><a href="http://en.wikipedia.org/wiki/Factor_analysis">Wikipedia: Factor analysis</a>
 * </ul>
 */
class FactorAnalysisMLE {

    static enum GRADIENT {

        /**
         * use the analytical gradient function
         */
        ANALYTICAL,
        /**
         * use the finite differencing to compute the gradient
         */
        NUMERICAL
    }

    private final ImmutableMatrix S;
    private final int k;
    private final GRADIENT gradient;
    private final double epsilon;
    private final int maxIterations;
    /** the log-likelihood function */
    final RealScalarFunction nL;

    /**
     * Construct an MLE optimization used for factor analysis.
     *
     * @param S             a covariance (or correlation) matrix to be used for factor analysis
     * @param nFactors      the number of factors to be fitted
     * @param gradient      indicate whether analytical function or numerical (finite differencing) method is used to calculate the gradient
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations used in the MLE optimization
     */
    FactorAnalysisMLE(Matrix S, int nFactors, GRADIENT gradient, double epsilon, int maxIterations) {
        this.S = new ImmutableMatrix(S);
        this.k = nFactors;
        this.gradient = gradient;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
        this.nL = new logLikelihood();
    }

    /**
     * Construct an MLE optimization used for factor analysis.
     *
     * @param S        a covariance (or correlation) matrix to be used for factor analysis
     * @param nFactors the number of factors to be fitted
     */
    FactorAnalysisMLE(Matrix S, int nFactors) {
        this(S, nFactors, GRADIENT.ANALYTICAL, SuanShuUtils.autoEpsilon(S), 500);
    }

    /**
     * Estimate the optimal psi that maximizes the log-likelihood.
     *
     * @param initials the initial guess of psi
     * @return the optimal psi that maximizes the log-likelihood.
     */
    Vector estimate(Vector initials) {//TODO: really need L-BFGS-B
        Vector xmin = null;

        try {
            SQPActiveSetSolver optim3 = new SQPActiveSetSolver(epsilon, maxIterations);
            ConstrainedOptimProblemImpl1 problem = new ConstrainedOptimProblemImpl1(
                    nL,
                    null,
                    (LinearLessThanConstraints) LinearConstraints.concat(new LowerBoundConstraints(nL, 0.005).toLessThanConstraints(),
                                                                         new UpperBoundConstraints(nL, 1.)));
            IterativeMinimizer<Vector> soln = optim3.solve(problem);
            soln.search(initials);
            xmin = soln.minimizer();
        } catch (Exception ex) {
            NelderMead optim1 = new NelderMead(epsilon, maxIterations);
            NelderMead.Solution soln = optim1.solve(new C2OptimProblemImpl(
                    new RealScalarFunction() {

                        @Override
                        public Double evaluate(Vector psi) {
                            //check bounds
                            double[] psiArr = psi.toArray();
                            //TODO: use L-BFGS-B
                            double min = DoubleArrayMath.min(psiArr);
                            if (min < 0.005) {//suggested on p. 32
                                return 1e10;//TODO: changing this number will fail the test cases
                            }

                            double max = DoubleArrayMath.max(psiArr);
                            if (max > 1) {
                                return 1e10;//TODO: changing this number will fail the test cases
                            }

                            return nL.evaluate(psi);
                        }

                        @Override
                        public int dimensionOfDomain() {
                            return nL.dimensionOfDomain();
                        }

                        @Override
                        public int dimensionOfRange() {
                            return nL.dimensionOfRange();
                        }
                    }));
            xmin = soln.search(initials);

//        BFGS optim2 = new BFGS();
//        optim2.solve(nL, 0, maxIterations);
//        optim2.solve(nL, new GradientFunction(), 0, maxIterations);
//
//        if (optim2.getClass().isAssignableFrom(NelderMead.class) || gradient == GRADIENT.NUMERICAL) {
//            optim2.solve(nL, 0, maxIterations);
//        } else {
//            optim2.solve(nL, new GradientFunction(), 0, maxIterations);
//        }
//        xmin = optim2.search(xmin);   
        }

        if (min(xmin.toArray()) < 0.005 - Constant.EPSILON) {
            throw new RuntimeException(String.format("invalid psi: %s", xmin.toString()));
        }

        return xmin;
    }

    /**
     * This is the log-likelihood function used in factor analysis.
     * Letting <i>k</i> denote the number of factors to be fitted in factor analysis,
     * the log-likelihood <i>F<sub>k</sub></i> is defined in (4.2) on p.26 of Lawley and Maxwell (1971).
     * In finding the minimum of <i>F<sub>k</sub></i>, it is convenient to use a two-stage procedure.
     * First we find the conditional minimum for given Psi, denoted by <i>f<sub>k</sub></i>, i.e.
     * (4.16) on p. 29 of Lawley and Maxwell (1971).
     * Then we find the Psi that minimizes <i>f<sub>k</sub></i>.
     */
    private class logLikelihood implements RealScalarFunction {

        private final int p = S.nRows(); //p is the original dimension, i.e. the number of variables in the original data

        //2nd stage in the minimization: find PSI
        @Override
        public Double evaluate(Vector psi) {
            double[] psiArr = psi.toArray();
            Matrix PSI_INV_SQRT = new DiagonalMatrix(foreach(
                    psiArr,
                    new UnivariateRealFunction() {

                        @Override
                        public double evaluate(double psi) {
                            return 1. / sqrt(psi);
                        }
                    })); //forms the diagonal matrix Psi^{-1/2} used in (4.11) of Lawley and Maxwell (1971)
            Matrix S_STAR = PSI_INV_SQRT.multiply(S).multiply(PSI_INV_SQRT); //(4.11) on p. 28 of Lawley and Maxwell (1971)

            Eigen eigen = new Eigen(S_STAR);
            double[] theta = Arrays.copyOfRange(eigen.getRealEigenvalues(), k, p);//There are always p real eigenvalues for a properly defined p by p (sample) covariance/correlation matrix.

            //1st stage in the minimization:
            //calculate the conditional minimum f_k, i.e. (4.16) on p. 29 of Lawley and Maxwell (1971)
            double logLikelihood = 0.;
            for (int j = 0; j < p - k; ++j) { //sum_{j=k+1}^p (theta_j - log(theta_j))
                logLikelihood += theta[j] - log(theta[j]);
            }
            logLikelihood -= p - k; //f_k = \sum_{j=k+1}^p (theta_j - log(theta_j)) - (p - nFactors)

            return logLikelihood; //returns f_k
        }

        @Override
        public int dimensionOfDomain() {
            return p;
        }

        @Override
        public int dimensionOfRange() {
            return 1;
        }
    };

    /**
     * This is the analytic gradient function of log-likelihood used
     * in factor analysis. The function <i>f<sub>k</sub></i> is minimized by the use of a method
     * of Fletcher and Powell (1963), with a few modifications to make it
     * suitable for our special purpose. This method is described in detail
     * in Section A2.5 of Appendix II of Lawley and Maxwell (1971).
     */
    private class GradientFunction implements RealVectorFunction {

        private final int p = S.nRows(); //p is the original dimension, i.e. the number of variables in the original data

        @Override
        public Vector evaluate(Vector psi) {
            Matrix lambda = FactorAnalysis.getLoadings(psi, k, S);

            Matrix PSI = new DiagonalMatrix(psi.toArray());
            Matrix PSI_INV = new Inverse(PSI);

            //eq. 4.18
            Matrix D = lambda.multiply(lambda.t()).add(PSI).minus(S);
            D = PSI_INV.multiply(D).multiply(PSI_INV);

            Vector gradient = new DenseVector(p);
            for (int i = 1; i <= p; ++i) {
                gradient.set(i, D.get(i, i));
            }

            return gradient;
        }

        @Override
        public int dimensionOfDomain() {
            return p;
        }

        @Override
        public int dimensionOfRange() {
            return 1;
        }
    }

}
