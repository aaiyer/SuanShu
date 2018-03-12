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
package com.numericalmethod.suanshu.stats.cointegration;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.Eigen;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.matrix.doubles.operation.MatrixMeasure;
import com.numericalmethod.suanshu.stats.descriptive.rank.Max;
import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.UniformRng;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.EvenlySpacedGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.timepoints.TimeGrid;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.Realization;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.brownian.RandomWalk;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.Filtration;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.FiltrationFunction;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.IntegralDB;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.IntegralDt;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.sde.Construction;

/**
 * Johansen provides the asymptotic distributions of the two {@linkplain Test hypothesis testings} (Eigen and Trace tests),
 * each for 5 different {@linkplain TrendType trend types}.
 *
 * @author Kevin Sun
 * @see "Kevin Sun, Notes on Cointegration, February 23, 2011."
 */
public class JohansenAsymptoticDistribution extends EmpiricalDistribution {

    /**
     * the types of Johansen cointegration tests available
     */
    public static enum Test {

        /**
         * Johansen EIGEN test
         */
        EIGEN,
        /**
         * Johansen TRACE test
         */
        TRACE
    }

    /**
     * the types of trends available
     */
    public static enum TrendType {

        /**
         * This is trend type I: no constant, no linear trend:
         * <blockquote><pre><i>
         * μ0 = 0
         * μ1 = 0
         * </i></pre></blockquote>
         */
        NO_CONSTANT(new F_NO_CONSTANT()),
        /**
         * This is trend type II: no restricted constant, no linear trend:
         * <blockquote><pre><i>
         * μ0 = -Πμ
         * μ1 = 0
         * </i></pre></blockquote>
         */
        RESTRICTED_CONSTANT(new F_RESTRICTED_CONSTANT()),
        /**
         * This is trend type III: constant, no linear trend:
         * <blockquote><pre><i>
         * μ0 ≠ 0
         * μ1 = 0
         * </i></pre></blockquote>
         */
        CONSTANT(new F_CONSTANT()),
        /**
         * This is trend type IV: constant, restricted linear trend:
         * <blockquote><pre><i>
         * μ0 ≠ 0
         * μ1 = -Πγ
         * </i></pre></blockquote>
         */
        CONSTANT_RESTRICTED_TIME(new F_CONSTANT_RESTRICTED_TIME()),
        /**
         * This is trend type V: constant, linear trend:
         * <blockquote><pre><i>
         * μ0 ≠ 0
         * μ1 ≠ 0
         * </i></pre></blockquote>
         */
        CONSTANT_TIME(new F_CONSTANT_TIME());
        private final F F;

        private TrendType(JohansenAsymptoticDistribution.F F) {
            this.F = F;
        }
    }

    /**
     * This is a filtration function.
     */
    public static interface F {

        /**
         * <i>F(B)</i>.
         *
         * @param B a multivariate Brownian motion
         * @return <i>F(B)</i>
         */
        public FiltrationFunction[] evaluate(Realization[] B);
    }

    /**
     * Construct the asymptotic distribution of a Johansen test.
     *
     * @param test  the type of Johansen cointegration test
     * @param trend the trend type
     * @param dr    the dimension of the multivariate time series (<i>d</i>) minus the number of cointegrating vectors (<i>r</i>)
     * @param nSim  the number of simulations
     * @param nT    the number of grid points in interval <i>[0, 1]</i>. The bigger {@code nT} is, the finer the time discretization is, the smaller the discretization error is, and the more accurate the results are.
     * @param seed  a seed
     */
    public JohansenAsymptoticDistribution(Test test, TrendType trend, int dr, int nSim, int nT, long seed) {
        super(simulation(test, trend, dr, nSim, nT, seed));
    }

    /**
     * Construct the asymptotic distribution of a Johansen test.
     *
     * @param test  the type of Johansen cointegration test
     * @param trend the trend type
     * @param dr    the dimension of the multivariate time series (<i>d</i>) minus the number of cointegrating vectors (<i>r</i>)
     */
    public JohansenAsymptoticDistribution(Test test, TrendType trend, int dr) {
        this(test, trend, dr, 10000, 5000, new UniformRng().nextLong());
    }

    private static double[] simulation(Test test, TrendType trend, int dr, int nSim, int nT, long seed) {
        TimeGrid T = new EvenlySpacedGrid(0, 1, nT);
        Construction RW = new RandomWalk(T);
        RW.seed(seed);

        //generate a test statistics for each filtration
        double[] stats = new double[nSim];
        for (int i = 0; i < nSim; ++i) {
            //a standard (d-r) dimensional Brownian motion
            //Haksun: this seems to be a more efficient way to generate a high dimensional Brownian motion,
            //when each dimension is used repeatedly and independently
            Realization[] B = new Realization[dr];
            for (int j = 0; j < dr; ++j) {
                B[j] = RW.nextRealization(0);
            }

            FiltrationFunction[] F = trend.F.evaluate(B);//TODO: have a multi dimensional Filtration(Function) class?

            Matrix m1 = getMatrix1(B, F);
            Matrix m2 = getMatrix2(F, new Filtration(RW.nextRealization(0)));

            double stat = getTestStat(test, m1, m2);
            stats[i] = stat;
        }

        return stats;
    }

    private static Matrix getMatrix1(Realization[] B, FiltrationFunction[] F) {
        int n1 = B.length;
        int n2 = F.length; //n2 = n1 or n2 = n1 + 1
        Matrix A = new DenseMatrix(n1, n2);

        //create matrix_1
        for (int i = 0; i < n1; ++i) {
            Filtration B_i = new Filtration(B[i]);
            for (int j = 0; j < n2; ++j) {
                double stat = new IntegralDB(F[j]).integral(B_i);
                A.set(i + 1, j + 1, stat);
            }
        }

        return A;
    }

    private static Matrix getMatrix2(FiltrationFunction[] F, Filtration W) {
        int n2 = F.length;
        Matrix A = new DenseMatrix(n2, n2);

        //create matrix_2
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n2; ++j) {
                final FiltrationFunction F1 = F[i];
                final FiltrationFunction F2 = F[j];

                FiltrationFunction F1F2 = new FiltrationFunction() {

                    @Override
                    public void setFT(Filtration FT) {
                        super.setFT(FT);
                    }

                    @Override
                    public double evaluate(int t) {
                        double B1 = F1.evaluate(t);
                        double B2 = F2.evaluate(t);
                        return B1 * B2;
                    }
                };

                double stat = new IntegralDt(F1F2).integral(W);
                A.set(i + 1, j + 1, stat);
            }
        }

        return A;
    }

    private static double getTestStat(Test test, Matrix m1, Matrix m2) {
        Matrix m121 = new CongruentMatrix(m1.t(), new Inverse(m2));

        switch (test) {
            case EIGEN:
                Eigen eigen = new Eigen(m121);
                double[] eigenvalues = eigen.getRealEigenvalues();
                Max max = new Max(eigenvalues);
                return max.value();
            case TRACE:
                return MatrixMeasure.tr(m121);
            default:
                throw new RuntimeException("please specify a valid test specification");
        }
    }
}
