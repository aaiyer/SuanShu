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
package com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite;

import com.numericalmethod.suanshu.matrix.doubles.operation.positivedefinite.MatthewsDavies;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.IsMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MatthewsDaviesTest {

    /**
     * Example 5.2
     * Practical Optimization: Algorithms and Engineering Applications
     * by
     * Andreas Antoniou and Wu-Sheng Lu
     */
    @Test
    public void test_0010() {
        Matrix H = new DenseMatrix(new double[][]{
                    {3, -6},
                    {-6, 59 / 5}
                });
        assertFalse(IsMatrix.positiveDefinite(H));

        MatthewsDavies Hhat = new MatthewsDavies(H);
        assertTrue(IsMatrix.positiveDefinite(Hhat));
        assertTrue(IsMatrix.positiveDefinite(Hhat.Dhat()));
    }

    @Test
    public void test_0020() {
        Matrix H = new DenseMatrix(new double[][]{
                    {110, 0, 0, -220},
                    {0, 6, -60, 0},
                    {0, -60, 600, 0},
                    {-220, 0, 0, 440}
                });
        assertFalse(IsMatrix.positiveDefinite(H));

        MatthewsDavies Hhat = new MatthewsDavies(H);
        assertTrue(IsMatrix.positiveDefinite(Hhat));
        assertTrue(IsMatrix.positiveDefinite(Hhat.Dhat()));
    }
}
