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
package com.numericalmethod.suanshu.stats.regression.linear.logistic;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.number.DoubleUtils;

/**
 * This class represents a logistic regression problem.
 *
 * <p>
 * Logistic regression is a variation of OLS regression which is used when the dependent (response) variable is a binary variable.
 * The independent (input) variables can be continuous, categorical, or both.
 *
 * @author Haksun Li
 */
public class LogisticProblem extends com.numericalmethod.suanshu.stats.regression.linear.LMProblem {//TODO: extend it to multiple discrete values (Or, use GLM)?

    public LogisticProblem(com.numericalmethod.suanshu.stats.regression.linear.LMProblem problem) {
        super(problem);
    }

    @Override
    protected void checkInputs() {
        super.checkInputs();

        //make sure that y is 0 or 1
        for (int i = 1; i <= y.size(); i++) {
            assertArgument((DoubleUtils.compare(y.get(i), 0, 0) == 0)
                    || DoubleUtils.compare(y.get(i), 0, 0) == 1, "y must take values of either 0 or 1");
        }
    }
}
