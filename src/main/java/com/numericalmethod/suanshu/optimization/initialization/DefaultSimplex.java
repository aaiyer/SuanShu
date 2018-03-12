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
package com.numericalmethod.suanshu.optimization.initialization;

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static java.lang.Math.abs;

/**
 * A simplex optimization algorithm, e.g., Nelder–Mead, requires an initial simplex to start the search.
 * To create a simplex from an initial point, we add an increment factor to each component/direction of the point.
 * The increments are computed as a percentage of the maximal absolute value of the components of the initial point.
 * For an initial point of 0, we set the increment to 1.
 *
 * @author Haksun Li
 */
public class DefaultSimplex implements BuildInitials {

    private final double scale;

    /**
     * Construct a simplex builder.
     *
     * @param scale a percentage (of the maximal absolute value of the components of the initial point)
     */
    public DefaultSimplex(double scale) {
        this.scale = scale;
    }

    /**
     * Construct a simplex builder.
     */
    public DefaultSimplex() {
        this(0.1);
    }

    /**
     * Build a simplex of <i>N+1</i> vertices from an initial point, where <i>N</i> is the dimension of the initial points.
     *
     * @param initials the initial points
     * @return a simplex
     */
    @Override
    public Vector[] getInitials(Vector... initials) {
        Vector initial = initials[0];

        double inc = 0;
        for (int i = 0; i < initials.length; ++i) {
            Vector v = initials[i];
            for (int j = 1; j <= v.size(); ++j) {
                if (inc < abs(v.get(j))) {
                    inc = abs(v.get(j));
                }
            }
        }

        inc = inc == 0 ? 1 : scale * inc;
        //getInitials the initial simplex
        Vector[] simplex = new Vector[initial.size() + 1];
        simplex[0] = new DenseVector(initial);
        for (int i = 1; i <= initial.size(); ++i) {
            simplex[i] = new DenseVector(initial);
            simplex[i].set(i, simplex[i].get(i) + inc);
        }
        return simplex;
    }
}
