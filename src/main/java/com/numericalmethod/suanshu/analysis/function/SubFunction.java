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
package com.numericalmethod.suanshu.analysis.function;

import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A sub-function, <i>g</i>, is defined over a subset of the domain of another (original) function, <i>f</i>.
 * <i>g(x) = f(x)</i> when they are both defined.
 * This implementation constructs a sub-function by restricting/fixing the values of a subset of variables of another function.
 *
 * @param <R> the range of a function
 * @author Haksun Li
 */
public abstract class SubFunction<R> implements Function<Vector, R> {

    /** the original, unrestricted function */
    protected final Function<Vector, R> f;
    /** the restrictions or fixed values */
    protected final Map<Integer, Double> fixing;
    private static Entry<Integer, Double> dummy = new Entry<Integer, Double>() {

        @Override
        public Integer getKey() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Double getValue() {
            return Double.POSITIVE_INFINITY;
        }

        @Override
        public Double setValue(Double value) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    /**
     * Construct a sub-function.
     *
     * @param f      the original, unrestricted function
     * @param fixing the values held fixed for a subset of variables
     */
    public SubFunction(Function<Vector, R> f, Map<Integer, Double> fixing) {
        assertArgument(fixing.size() <= f.dimensionOfDomain(), "too many values for fixing");
        for (Integer index : fixing.keySet()) {
            assertArgument(0 < index, "invalid index; index counts from 1");
            assertArgument(index <= f.dimensionOfDomain(), "invalid index");
        }

        this.f = f;
        this.fixing = new HashMap<Integer, Double>(fixing);
    }

    @Override
    public int dimensionOfDomain() {
        return f.dimensionOfDomain() - fixing.size();
    }

    @Override
    public int dimensionOfRange() {
        return f.dimensionOfRange();
    }

    /**
     * Check whether a particular index corresponds a fixed variable/value.
     *
     * @param i an index, counting from 1
     * @return {@code true} if <i>x<sub>i</sub></i> is fixed to some value
     */
    public boolean isFixedIndex(int i) {
        for (Integer index : fixing.keySet()) {
            if (index == i) {
                return true;
            }
        }

        return false;
    }

    /**
     * Given an input to the original function, this extracts the variable parts (excluding the fixed values).
     *
     * @param z an input to the original function
     * @return the variable part in {@code z}
     */
    public double[] getVariablePart(double[] z) {
        final int size = f.dimensionOfDomain() - fixing.size();
        double[] x = new double[size];

        for (int i = 0, j = 0; i < z.length; ++i) {
            if (!isFixedIndex(i + 1)) {//index counts from 1
                x[j] = z[i];
                j++;
            }
        }

        return x;
    }

    /**
     * Combine the variable and fixed values to form an input to the original function.
     *
     * @param variables the non-fixed variables/values to the restricted function
     * @param fixed     the fixed values to the original function
     * @return an input to the original function
     */
    public static Vector getAllParts(Vector variables, Map<Integer, Double> fixed) {
        int dim = variables.size() + fixed.size();

        int p0 = 1;//index to the variables

        Iterator<Entry<Integer, Double>> it = fixed.entrySet().iterator();
        Entry<Integer, Double> p1 = it.hasNext() ? it.next() : dummy;

        Vector z = new DenseVector(dim);
        for (int i = 1; i <= dim; ++i) {
            if (i == p1.getKey()) {
                z.set(i, p1.getValue());
                p1 = it.hasNext() ? it.next() : dummy;
            } else {
                z.set(i, variables.get(p0));
                ++p0;
            }
        }

        return z;
    }
}
