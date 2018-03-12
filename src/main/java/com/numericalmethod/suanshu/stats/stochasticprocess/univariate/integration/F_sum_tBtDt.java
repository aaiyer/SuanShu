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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.integration;

/**
 * a function of this integral
 *
 * /1
 * |  (t - 0.5) * (B) (dt)
 * /0
 *
 * @author Haksun Li
 */
public abstract class F_sum_tBtDt extends FiltrationFunction {

    protected double sum_BtDt = 0;//Σ(Bt)*(dt)
    protected double sum_tBtDt = 0;//Σ(t-0.5)*(Bt)*(dt)

    @Override
    public void setFT(Filtration FT) {
        super.setFT(FT);

        FiltrationFunction Bt = new Bt();
        FiltrationFunction tBt = new FiltrationFunction() {

            @Override
            public double evaluate(int t) {
                double Bt = FT.B(t);
                double tBt = FT.T(t) * Bt;
                return tBt;
            }
        };

        sum_BtDt = new IntegralDt(Bt).integral(FT);
        sum_tBtDt = new IntegralDt(tBt).integral(FT) - 0.5 * sum_BtDt;
    }
}
