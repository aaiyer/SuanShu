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

import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.Realization;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.F_sum_tBtDt;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.Filtration;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.FiltrationFunction;
import com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration.IntegralDt;

/**
 * F = B_dr^
 *
 * @author Ken Yiu
 */
class F_CONSTANT_TIME implements JohansenAsymptoticDistribution.F {

    @Override
    public FiltrationFunction[] evaluate(Realization[] B) {
        final int dim = B.length;

        FiltrationFunction[] F = new FiltrationFunction[dim];
        for (int i = 0; i < dim; ++i) {
            final Filtration B_i = new Filtration(B[i]);
            F[i] = new F_sum_tBtDt() {

                @Override
                public void setFT(Filtration FT) {
                    super.setFT(FT);

                    FiltrationFunction Bt = new FiltrationFunction() {

                        @Override
                        public double evaluate(int t) {
                            double F = B_i.B(t);
                            return F;
                        }
                    };
                    FiltrationFunction tBt = new FiltrationFunction() {

                        @Override
                        public double evaluate(int t) {
                            double Bt = B_i.B(t);
                            double tBt = B_i.T(t) * Bt;
                            return tBt;
                        }
                    };

                    sum_BtDt = new IntegralDt(Bt).integral(B_i);
                    sum_tBtDt = new IntegralDt(tBt).integral(B_i) - 0.5 * sum_BtDt;
                }

                @Override
                public double evaluate(int t) {
                    double F = B_i.B(t);
                    return F - sum_BtDt - 12 * (B_i.T(t) - 0.5) * sum_tBtDt;
                }
            };
        }

        return F;
    }
}
