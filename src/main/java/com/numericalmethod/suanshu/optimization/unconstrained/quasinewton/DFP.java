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

/**
 * The Davidon-Fletcher-Powell method is a quasi-Newton method
 * to solve unconstrained nonlinear optimization problems.
 * This method maintains the symmetry and positive definiteness of the Hessian matrix.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Andreas Antoniou, Wu-Sheng Lu, "Section 7.5, Table 7.1," Practical Optimization: Algorithms and Engineering Applications."
 * <li><a href="http://en.wikipedia.org/wiki/Davidon%E2%80%93Fletcher%E2%80%93Powell_formula">Wikipedia: Davidon–Fletcher–Powell formula</a>
 * </ul>
 */
public class DFP extends Huang {

    /**
     * S<sub>k+1</sub> = S<sub>k</sub> + δδ' / γ'δ - Sγγ'S' / γ'Sγ
     *
     * @param S     the inverse of a Hessian
     * @param gamma γ
     * @param delta δ
     * @return an updated Hessian matrix
     * @see "Andreas Antoniou, Wu-Sheng Lu, "eq. 7.29," Practical Optimization: Algorithms and Engineering Applications."
     */
    public static Matrix updateHessianInverse(Matrix S, Matrix gamma, Matrix delta) {
        Matrix gtd = gamma.t().multiply(delta);//γ'δ = γ' %*% δ
        double gtdvalue = gtd.get(1, 1);//γ'δ is a 1x1 matrix

        Matrix term1 = delta.multiply(delta.t());//δδ'
        Matrix term2 = term1.scaled(1.0 / gtdvalue);//δδ' / δ'γ = δδ' / γ'δ

        Matrix term3 = S.multiply(gamma);//Sγ
        Matrix term4 = term3.multiply(term3.t());//Sγγ'S' = Sγ(Sγ)'; note that S' = S
        Matrix gtS = gamma.t().multiply(S);//γ'S = γ' %*% S
        Matrix ltSl = gtS.multiply(gamma);//γ'Sγ
        double ltSlvalue = ltSl.get(1, 1);
        Matrix term5 = term4.scaled(-1.0 / ltSlvalue);//-Sγγ'S / γ'Sγ

        //S<sub>k+1</sub> = S<sub>k</sub> + δδ' / γ'δ - Sγγ'S' / γ'Sγ
        Matrix S1 = S.add(term2).add(term5);
        return S1;
    }

    /**
     * Construct a multivariate minimizer using the DFP method.
     *
     * @param epsilon       a precision parameter: when a number |x| ≤ ε, it is considered 0
     * @param maxIterations the maximum number of iterations
     */
    public DFP(double epsilon, int maxIterations) {
        super(1, 0, 0, 1, epsilon, maxIterations);
    }
}
