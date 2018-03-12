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

import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.Arrays;

/**
 * This penalty function sums up the costs from a set of constituent penalty functions.
 * This builds a new composite penalty function from existing simpler ones.
 * It applies to any constrained optimization problems.
 *
 * @author Haksun Li
 */
public class SumOfPenalties extends PenaltyFunction {

    /** the constituent penalty functions */
    private final PenaltyFunction[] penalties;

    /**
     * Construct a sum-of-penalties penalty function from a set of penalty functions.
     * The penalties must have the same dimension.
     *
     * @param penalties the constituent penalty functions
     */
    public SumOfPenalties(PenaltyFunction... penalties) {
        final int dimension = penalties[0].dimensionOfDomain();

        for (int i = 1; i < penalties.length; ++i) {
            SuanShuUtils.assertArgument(dimension == penalties[i].dimensionOfDomain(),
                                        "the penalties must have the same dimension");
        }

        this.penalties = Arrays.copyOf(penalties, penalties.length);
    }

    @Override
    public Double evaluate(Vector x) {
        double sum = 0;

        for (int i = 0; i < penalties.length; ++i) {
            sum += penalties[i].evaluate(x);
        }

        return sum;
    }

    @Override
    public int dimensionOfDomain() {
        return penalties[0].dimensionOfDomain();
    }
}
