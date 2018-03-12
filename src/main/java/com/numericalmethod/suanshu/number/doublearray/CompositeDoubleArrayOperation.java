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

/**
 * It is desirable to have multiple implementations and switch between them for, e.g., performance reason.
 *
 * @author Ken Yiu
 */
public class CompositeDoubleArrayOperation implements DoubleArrayOperation {

    /**
     * Specify which implementation to use.
     */
    public interface ImplementationChooser {

        /**
         * Get an implementation based on the inputs.
         *
         * @param arr1 an array {@code double[]}
         * @param arr2 an array {@code double[]}
         * @return an implementation
         */
        public DoubleArrayOperation getOperation(double[] arr1, double[] arr2);
    }

    private final ImplementationChooser chooser;

    /**
     * Construct a {@code CompositeDoubleArrayOperation} by supplying the multiplexing criterion and the multiple {@code DoubleArrayOperation}s.
     *
     * @param chooser an {@link ImplementationChooser}
     */
    public CompositeDoubleArrayOperation(ImplementationChooser chooser) {
        this.chooser = chooser;
    }

    /**
     * Construct a {@code CompositeDoubleArrayOperation} that chooses an implementation by array length.
     *
     * @param arrayLengthThreshold the array length threshold to switch implementation
     * @param impl1                implementation 1
     * @param impl2                implementation 2
     */
    public CompositeDoubleArrayOperation(
            final int arrayLengthThreshold,
            final DoubleArrayOperation impl1,
            final DoubleArrayOperation impl2) {
        this(
                new ImplementationChooser() {

                    @Override
                    public DoubleArrayOperation getOperation(double[] arr1, double[] arr2) {
                        if (arr1.length < arrayLengthThreshold) {
                            return impl1;
                        } else {
                            return impl2;
                        }
                    }
                });
    }

    @Override
    public double[] add(double[] arr1, double[] arr2) {
        DoubleArrayOperation op = chooser.getOperation(arr1, arr2);
        return op.add(arr1, arr2);
    }

    @Override
    public double[] minus(double[] arr1, double[] arr2) {
        DoubleArrayOperation op = chooser.getOperation(arr1, arr2);
        return op.minus(arr1, arr2);
    }

    @Override
    public double[] scaled(double[] arr, double c) {
        DoubleArrayOperation op = chooser.getOperation(arr, arr);
        return op.scaled(arr, c);
    }
}
