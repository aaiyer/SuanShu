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
import com.numericalmethod.suanshu.optimization.problem.C2OptimProblem;
import com.numericalmethod.suanshu.optimization.problem.IterativeMinimizer;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Huang's updating formula is a family of formulas which encompasses
 * the rank-one, DFP, BFGS as well as some other formulas.
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 7.9, Table 7.1," Practical Optimization: Algorithms and Engineering Applications."
 */
public class Huang extends QuasiNewton {

    private final double theta;//θ, a Huang family parameterization
    private final double phi;//φ, a Huang family parameterization
    private final double psi;//ψ, a Huang family parameterization
    private final double omega;//ω, a Huang family parameterization

    /**
     * an implementation of Huang's formula.
     */
    protected class HuangImpl extends QuasiNewtonImpl {

        public HuangImpl(C2OptimProblem problem) throws Exception {
            super(problem);
        }

        @Override
        void updateSk(Vector dg) {//TODO: is there a way to make these matrix/vector manipulation easier?
            Matrix gamma = new DenseMatrix(dg);
            Matrix deltak = new DenseMatrix(dk.scaled(ak));//δk = ak * dk in matrix form
            Matrix StGamma = Sk.t().multiply(gamma);//S'γ = S' %*% γ

            Matrix term1 = deltak.scaled(theta).add(StGamma.scaled(phi)).t();//(θδ + φS'γ)'
            Matrix term2 = deltak.multiply(term1);//δ(θδ + φS'γ)'
            Matrix term3 = term1.multiply(gamma);//(θδ + φS'γ)'γ
            double term3value = term3.get(1, 1);//(θδ + φS'γ)'γ is 1x1

            Matrix term4 = deltak.scaled(psi).add(StGamma.scaled(omega)).t();//(ψδ + ωS'γ)'
            Matrix term5 = Sk.multiply(gamma).multiply(term4);//Sγ(ψδ + ωS'γ)'
            Matrix term6 = term4.multiply(gamma);//(ψδ + ωS'γ)'γ
            double term6value = term6.get(1, 1);//(ψδ + ωS'γ)'γ is 1x1

            /*
             * S<sub>k+1</sub> = S<sub>k</sub> + δ(θδ + φS'γ)' / (θδ + φS'γ)'γ - Sγ(ψδ + ωS'γ)' / (ψδ + ωS'γ)'γ
             *
             * c.f.,
             * <pre>
             * Section 7.9
             * Practical Optimization: Algorithms and Engineering Applications
             * by
             * Andreas Antoniou, Wu-Sheng Lu
             * </pre>
             */
            Matrix Sk1 = Sk.add(term2.scaled(1.0 / term3value)).minus(term5.scaled(1.0 / term6value));
            //update Sk
            Sk = Sk1;
        }
    }

    /**
     * Construct a multivariate minimizer using Huang's method.
     *
     * @param theta         θ in Huang's formula
     * @param phi           φ in Huang's formula
     * @param psi           ψ in Huang's formula
     * @param omega         ω in Huang's formula
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public Huang(double theta, double phi, double psi, double omega, double epsilon, int maxIterations) {
        super(epsilon, maxIterations);

        //the Huang family parameterizations
        this.theta = theta;
        this.phi = phi;
        this.psi = psi;
        this.omega = omega;
    }

    @Override
    public IterativeMinimizer<Vector> solve(C2OptimProblem problem) throws Exception {
        return new HuangImpl(problem);
    }
}
