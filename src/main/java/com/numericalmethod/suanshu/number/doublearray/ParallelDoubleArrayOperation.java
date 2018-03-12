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
package com.numericalmethod.suanshu.number.doublearray;

import com.numericalmethod.suanshu.parallel.LoopBody;
import com.numericalmethod.suanshu.parallel.MultipleExecutionException;
import com.numericalmethod.suanshu.parallel.ParallelExecutor;

/**
 * This is a multi-threaded implementation of the array math operations.
 *
 * @author Ken
 */
public class ParallelDoubleArrayOperation implements DoubleArrayOperation {

    private final ParallelExecutor parallel = new ParallelExecutor();

    @Override
    public double[] add(final double[] arr1, final double[] arr2) {
        final double[] result = new double[arr1.length];
        try {
            parallel.forLoop(0, arr1.length, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    result[i] = arr1[i] + arr2[i];
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    @Override
    public double[] minus(final double[] arr1, final double[] arr2) {
        final double[] result = new double[arr1.length];
        try {
            parallel.forLoop(0, arr1.length, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    result[i] = arr1[i] - arr2[i];
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    @Override
    public double[] scaled(final double[] arr, final double c) {
        final double[] result = new double[arr.length];
        try {
            parallel.forLoop(0, arr.length, new LoopBody() {

                @Override
                public void run(int i) throws Exception {
                    result[i] = arr[i] * c;
                }
            });
        } catch (MultipleExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }
}
