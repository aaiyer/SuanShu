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

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.number.doublearray.DoubleArrayMath;
import com.numericalmethod.suanshu.stats.hmm.HmmInnovation;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;

/**
 * These are the utility functions to examine a Markov chain.
 *
 * @author Haksun Li
 */
public class MCUtils {

    private MCUtils() {
        // private constructor for utility class
    }

    /**
     * Count the numbers of occurrences of states.
     *
     * @param states the Markov state labels, counting from 1
     * @return the numbers of occurrences
     */
    public static Vector getStateCounts(int[] states) {
        int N = DoubleArrayMath.max(states);

        double[] counts = new double[N];
        for (int s : states) {
            ++counts[s - 1];
        }

        return new DenseVector(counts);
    }

    /**
     * Count the numbers of times the state goes from one state to another.
     *
     * @param states the Markov state labels, counting from 1
     * @return the counts of transitions
     */
    public static DenseMatrix getTransitionCounts(int[] states) {
        int N = DoubleArrayMath.max(states);

        DenseMatrix A = new DenseMatrix(N, N);
        for (int i = 1; i < states.length; ++i) {
            int qt_1 = states[i - 1];
            int qt = states[i];

            A.set(qt_1, qt, 1 + A.get(qt_1, qt));
        }

        return A;
    }

    /**
     * Get all observations that occur in a particular state.
     *
     * @param innovations the HMM innovations
     * @param state       a state
     * @return the observations in the state
     */
    public static double[] getObservations(HmmInnovation[] innovations, int state) {
        ArrayList<Double> obs = new ArrayList<Double>();

        for (HmmInnovation innovation : innovations) {
            if (innovation.getState() == state) {
                obs.add(innovation.getObservation());
            }
        }

        return DoubleUtils.collection2DoubleArray(obs);
    }
}
