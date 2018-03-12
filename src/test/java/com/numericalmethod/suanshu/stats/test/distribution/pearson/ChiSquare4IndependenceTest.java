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
package com.numericalmethod.suanshu.stats.test.distribution.pearson;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class ChiSquare4IndependenceTest {

    @Test
    public void test_0010() {
        Matrix X = new DenseMatrix(new double[][]{
                    {12, 7},
                    {5, 7}
                });

        ChiSquare4Independence instance = new ChiSquare4Independence(X, 0, ChiSquare4Independence.Type.ASYMPTOTIC_CHI_SQUARE);
        assertEquals(0.6411, instance.statistics(), 1e-4);
        assertEquals(0.4233054243224184, instance.pValue(), 1e-15);
    }

    @Test
    public void test_0020() {
        Matrix X = new DenseMatrix(new double[][]{
                    {12, 7},
                    {5, 7}
                });

        ChiSquare4Independence instance = new ChiSquare4Independence(X, 100000, ChiSquare4Independence.Type.EXACT);
        assertEquals(1.371646026831785, instance.statistics(), 1e-4);
        assertEquals(0.2916708329167083, instance.pValue(), 1e-2);
    }

    @Test
    public void test_0030() {
        Matrix X = new DenseMatrix(new double[][]{
                    {9, 9},
                    {12, 6}
                });

        ChiSquare4Independence instance = new ChiSquare4Independence(X, 1000000, ChiSquare4Independence.Type.EXACT);
        assertEquals(1.0286, instance.statistics(), 1e-4);
        assertEquals(0.4994, instance.pValue(), 1e-2);
    }

    @Test
    public void test_0040() {
        Matrix X = new DenseMatrix(new double[][]{
                    {2, 7},
                    {8, 2}
                });

        ChiSquare4Independence instance = new ChiSquare4Independence(X, 1000000, ChiSquare4Independence.Type.EXACT);
        assertEquals(6.3427, instance.statistics(), 1e-4);
        assertEquals(0.0226, instance.pValue(), 1e-3);
    }
}
