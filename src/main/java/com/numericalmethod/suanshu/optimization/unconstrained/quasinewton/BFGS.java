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
package com.numericalmethod.suanshu.optimization.unconstrained.quasinewton;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CongruentMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Broyden-Fletcher-Goldfarb-Shanno method is a quasi-Newton method
 * to solve unconstrained nonlinear optimization problems.
 * The Hessian matrix of the objective function needs not to be computed at any stage.
 * The Hessian is updated by analyzing successive gradient vectors instead.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Eq. 7.57, Section 7.6," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/BFGS">Wikipedia: BFGS method</a>
 * </ul>
 */
public class BFGS extends QuasiNewton {

    /**
     * S<sub>k+1</sub> = S<sub>k</sub> + (1 + γ'Sγ/γ'δ)/γ'δ * δδ' -(δγ'S + Sγδ') / γ'δ, where S = H<sup>-1</sup>
     *
     * @param S     the inverse of a Hessian
     * @param gamma γ
     * @param delta δ
     * @return an updated Hessian matrix
     * @see "Andreas Antoniou, Wu-Sheng Lu, "eq. 7.57," Practical Optimization: Algorithms and Engineering Applications."
     */
    public static Matrix updateHessianInverse1(Matrix S, Matrix gamma, Matrix delta) {
        Matrix gtS = gamma.t().multiply(S);//γ'S = γ' %*% S
        Matrix gtd = gamma.t().multiply(delta);//γ'δ = γ' %*% δ
        double gtdValue = gtd.get(1, 1);//γ'δ is a 1x1 matrix

        double coeff1 = (1 + gtS.multiply(gamma).get(1, 1) / gtdValue) / gtdValue;//(1 + γ'Sγ/γ'δ)/γ'δ
        Matrix term1 = delta.multiply(delta.t());//δδ'
        Matrix term2 = term1.scaled(coeff1);//(1 + γ'Sγ/γ'δ)/γ'δ * δδ'

        Matrix term3 = delta.multiply(gtS);//δγ'S
        Matrix term4 = S.multiply(gamma).multiply(delta.t());//Sγδ'
        Matrix term5 = term3.add(term4).scaled(-1.0 / gtdValue);//-(δγ'S + Sγδ') / γ'δ

        Matrix S1 = S.add(term2).add(term5);//replace Sk by I then we get a memoryless BFGS, i.e., no tracking of Sk
        return S1;
    }

    /**
     * P + γγ' / γ'δ - P %*% γγ' %*% P / γ'Pδ, where P = S<sup>-1</sup> is the Hessian.
     *
     * @param S     the inverse of a Hessian
     * @param gamma γ
     * @param delta δ
     * @return an updated Hessian matrix
     */
    public static Matrix updateHessianInverse2(Matrix S, Matrix gamma, Matrix delta) {
        Matrix P = new Inverse(S, 0);
        Matrix P1 = DFP.updateHessianInverse(P, delta, gamma);//note that we exchange gamma with delta positions
        Matrix S1 = new Inverse(P1, 0);
        return S1;
    }

    /**
     * Damped BFGS Hessian update.
     *
     * @param H     a Hessian matrix
     * @param gamma γ
     * @param delta δ
     * @return an updated Hessian matrix
     * @see "Jorge Nocedal, Stephen Wright, "Procedure 18.2, Damped BFGS Updating," Numerical Optimization."
     */
    public static Matrix dampedBFGSHessianUpdate(Matrix H, Vector gamma, Vector delta) {
        // eq. 15.30
        double dgamma = delta.innerProduct(gamma);//d'γ
        double dYd = delta.innerProduct(H.multiply(delta));//d'Yd;
        double theta = 1.;
        if (dgamma < 0.2 * dYd) {
            theta = 0.8 * dYd;
            theta /= dYd - dgamma;
        }

        // 15.29
        Vector eta1 = gamma.scaled(theta);
        Vector eta2 = H.multiply(delta).scaled(1. - theta);
        Vector eta = eta1.add(eta2);

        // 15.26
        Matrix W1 = DFP.updateHessianInverse(H, new DenseMatrix(delta), new DenseMatrix(eta));// note the positions of d and gamma/eta

        return W1;
    }

    /**
     * an implementation of the BFGS algorithm
     */
    protected class BFGSImpl extends QuasiNewtonImpl {

        public BFGSImpl(C2OptimProblem problem) throws Exception {
            super(problem);
        }

        @Override
        void updateSk(Vector dg) {//TODO: is there a way to make these matrix/vector manipulation easier?
            Matrix gamma = new DenseMatrix(dg);
            Matrix deltak = new DenseMatrix(dk.scaled(ak));//δk = ak * dk in matrix form
            Matrix gtd = gamma.t().multiply(deltak);//γ'δ = γ' %*% δ
            double gtdvalue = gtd.get(1, 1);//γ'δ is a 1x1 matrix

            Matrix Sk1 = updateHessianInverse1(Sk, gamma, deltak);

            if (isFletcherSwitch) {//Section 7.8.1
                Matrix ltSk1l = new CongruentMatrix(gamma, Sk1);//γ' %*% S<sub>k+1</sub> %*% γ;
                double ltSk1lValue = ltSk1l.get(1, 1);//γ' %*% S<sub>k+1</sub> %*% γ is a 1x1 matrix

                if (gtdvalue - ltSk1lValue > 0) {//use DFP, eq. 7.29
                    Sk1 = DFP.updateHessianInverse(Sk, gamma, deltak);
                }
            }

            //update Sk
            Sk = Sk1;
        }
    }

    /** {@code true} if Fletcher's modification to switch between BFGS and DFP is applied */
    private final boolean isFletcherSwitch;

    /**
     * Construct a multivariate minimizer using the BFGS method.
     *
     * @param epsilon          a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations    the maximum number of iterations
     * @param isFletcherSwitch indicate whether to use the Fletcher switch
     */
    public BFGS(boolean isFletcherSwitch, double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
        this.isFletcherSwitch = isFletcherSwitch;
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return new BFGSImpl(problem);
    }
}
