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
package com.numericalmethod.suanshu.analysis.differentiation.multivariate;

import com.numericalmethod.suanshu.analysis.function.matrix.RntoMatrix;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * The Jacobian function, <i>J(x)</i>, evaluates the Jacobian of a real vector-valued function <i>f</i> at a point <i>x</i>.
 * <i>J(x)</i> has the same domain as <i>f(x)</i>.
 *
 * @author Haksun Li
 */
public class JacobianFunction implements RntoMatrix {

    private final RealVectorFunction f;//the real vector function to compute the Jacobian for

    /**
     * Construct the Jacobian function of a real scalar function <i>f</i>.
     *
     * @param f a real scalar function
     */
    public JacobianFunction(RealVectorFunction f) {
        this.f = f;
    }

    @Override
    public Matrix evaluate(Vector x) {
        Jacobian J = new Jacobian(f, x);
        return J;
    }

    @Override
    public int dimensionOfDomain() {
        return f.dimensionOfDomain();
    }

    @Override
    public int dimensionOfRange() {
        return 1;
    }
}
