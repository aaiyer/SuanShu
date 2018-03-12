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
package com.numericalmethod.suanshu.stats.markovchain;

import com.numericalmethod.suanshu.stats.hmm.HmmInnovation;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MCUtilsTest {

    @Test
    public void test_getStateCounts_0010() {
        int[] states = new int[]{1, 1, 1, 1, 2, 2, 3, 3, 3, 1};
        Vector instance = MCUtils.getStateCounts(states);
        assertArrayEquals(new double[]{5, 2, 3}, instance.toArray(), 0);
    }

    @Test
    public void test_getTransitionCounts_0010() {
        int[] states = new int[]{1, 1, 1, 1, 2, 2, 3, 3, 3, 1};
        Matrix instance = MCUtils.getTransitionCounts(states);

        assertTrue(AreMatrices.equal(
                new DenseMatrix(new double[][]{
                    {3, 1, 0},
                    {0, 1, 1},
                    {1, 0, 2}
                }),
                instance,
                0));

    }

    @Test
    public void test_getObservations_0010() {
        HmmInnovation[] innovations = new HmmInnovation[]{
            new HmmInnovation(1, 1.0),
            new HmmInnovation(1, 1.0),
            new HmmInnovation(1, 1.0),
            new HmmInnovation(2, 2.0),
            new HmmInnovation(2, 2.0),
            new HmmInnovation(3, 3.0)
        };

        double[] obs = MCUtils.getObservations(innovations, 1);
        assertArrayEquals(new double[]{1, 1, 1}, obs, 0);

        obs = MCUtils.getObservations(innovations, 2);
        assertArrayEquals(new double[]{2, 2}, obs, 0);

        obs = MCUtils.getObservations(innovations, 3);
        assertArrayEquals(new double[]{3}, obs, 0);
    }
}
