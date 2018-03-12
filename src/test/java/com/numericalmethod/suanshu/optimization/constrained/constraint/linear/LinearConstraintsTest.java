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
package com.numericalmethod.suanshu.optimization.constrained.constraint.linear;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.optimization.constrained.constraint.ConstraintsUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearConstraintsTest {

    @Test
    public void test_concat_0010() {
        LinearLessThanConstraints c1 = new LinearLessThanConstraints(
                new DenseMatrix(new double[][]{
                    {1, 1.1}
                }),
                new DenseVector(new double[]{
                    1
                }));

        LinearLessThanConstraints c2 = new LinearLessThanConstraints(
                new DenseMatrix(new double[][]{
                    {2, 2.2}
                }),
                new DenseVector(new double[]{
                    2
                }));

        LinearLessThanConstraints c3 = new LinearLessThanConstraints(
                new DenseMatrix(new double[][]{
                    {1, 1.1},
                    {2, 2.2}
                }),
                new DenseVector(new double[]{
                    1,
                    2
                }));

        LinearLessThanConstraints c1c2 = (LinearLessThanConstraints) LinearConstraints.concat(c1, c2);//test casting
        assertEquals(ConstraintsUtils.evaluate(c3, new DenseVector(1., 2.)),
                     ConstraintsUtils.evaluate(c1c2, new DenseVector(1., 2.)));//test function values
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_concat_0020() {
        LinearLessThanConstraints c1 = new LinearLessThanConstraints(
                new DenseMatrix(new double[][]{
                    {1, 1.1}
                }),
                new DenseVector(new double[]{
                    1
                }));

        LinearGreaterThanConstraints c2 = new LinearGreaterThanConstraints(
                new DenseMatrix(new double[][]{
                    {2, 2.2}
                }),
                new DenseVector(new double[]{
                    2
                }));

        LinearConstraints c1c2 = LinearConstraints.concat(c1, c2);
    }

    @Test
    public void test_toString_0010() {
        LinearLessThanConstraints c1 = new LinearLessThanConstraints(
                new DenseMatrix(new double[][]{
                    {1, 1.1}
                }),
                new DenseVector(new double[]{
                    1
                }));
        assertTrue(c1.toString().indexOf("<=") > 0);
    }

    @Test
    public void test_toString_0020() {
        LinearGreaterThanConstraints c1 = new LinearGreaterThanConstraints(
                new DenseMatrix(new double[][]{
                    {1, 1.1}
                }),
                new DenseVector(new double[]{
                    1
                }));
        assertTrue(c1.toString().indexOf(">=") > 0);
    }

    @Test
    public void test_toString_0030() {
        LinearEqualityConstraints c1 = new LinearEqualityConstraints(
                new DenseMatrix(new double[][]{
                    {1, 1.1}
                }),
                new DenseVector(new double[]{
                    1
                }));
        assertTrue(c1.toString().indexOf("=") > 0);
    }
}
