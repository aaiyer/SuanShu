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
package com.numericalmethod.suanshu.analysis.function.rn2r1;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class QuadraticFunctionTest {

    /**
     * example 13.1
     * "Andreas Antoniou, Wu-Sheng Lu. "Convex QP Problems with Equality Constraints," Practical Optimization: Algorithms and Engineering Applications."
     */
    @Test
    public void test_0010() {
        QuadraticFunction instance = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
                }),
                new DenseVector(2., 1., -1.));

        double fx = instance.evaluate(new DenseVector(1., 2., 3.));
        assertEquals(3.5, fx, 0.0);
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());
    }

    @Test
    public void test_0020() {
        QuadraticFunction instance = new QuadraticFunction(
                new DenseMatrix(new double[][]{
                    {1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 0}
                }),
                new DenseVector(2., 1., -1.),
                10);

        double fx = instance.evaluate(new DenseVector(1., 2., 3.));
        assertEquals(13.5, fx, 0.0);
        assertEquals(3, instance.dimensionOfDomain());
        assertEquals(1, instance.dimensionOfRange());
    }
}
