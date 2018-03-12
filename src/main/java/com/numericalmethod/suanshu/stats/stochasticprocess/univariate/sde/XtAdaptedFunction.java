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
package com.numericalmethod.suanshu.stats.stochasticprocess.univariate.sde;

/**
 * This represents a <i>F<sub>t</sub>-adapted</i> function that depends only on X(t).
 *
 * @author Haksun Li
 *
 * @see "Fima C. Klebaner. Introduction to Stochastic Calculus with Applications. 2nd ed. Section 4.7. Imperial College Press. 2006."
 */
public abstract class XtAdaptedFunction implements FtAdaptedFunction {

    /**
     * Evaluate this function, <i>f</i>, based on only the current value of the stochastic process.
     * 
     * @param Xt the current value of the stochastic process
     * @return <i>f(X<sub>t</sub>)</i>
     */
    public abstract double evaluate(double Xt);

    public double evaluate(Ft ft) {
        return evaluate(ft.Xt());
    }
}
