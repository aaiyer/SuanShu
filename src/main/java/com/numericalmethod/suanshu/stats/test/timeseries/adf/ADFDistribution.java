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
package com.numericalmethod.suanshu.stats.test.timeseries.adf;

import com.numericalmethod.suanshu.stats.descriptive.rank.Quantile.QuantileType;
import com.numericalmethod.suanshu.stats.distribution.univariate.EmpiricalDistribution;

/**
 *
 * @author Haksun Li
 */
public abstract class ADFDistribution extends EmpiricalDistribution {

    static interface Table {

        double[] getData(int sampleSize);
    }

    /**
     * the factory to construct various ADF distributions
     * 
     * @param type the trend type
     * @param sampleSize the sample size
     * @param lagOrder the lag order; TODO: this parameter is for now ignored
     * @return an ADF distribution per specification
     */
    public static ADFDistribution getDistribution(AugmentedDickeyFuller.TrendType type, int lagOrder, int sampleSize) {
        return type.getDistributionInstance(sampleSize);
    }

    ADFDistribution(double[] data, QuantileType quantileType) {
        super(data, quantileType);
    }
}
