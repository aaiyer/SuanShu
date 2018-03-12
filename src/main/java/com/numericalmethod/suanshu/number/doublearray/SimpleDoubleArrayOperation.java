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
 * This is a simple, single-threaded implementation of the array math operations.
 *
 * @author Ken
 */
public class SimpleDoubleArrayOperation implements DoubleArrayOperation {

    @Override
    public double[] add(double[] arr1, double[] arr2) {
        double[] result = new double[arr1.length];

        for (int i = 0; i < arr1.length; ++i) {
            result[i] = arr1[i] + arr2[i];
        }

        return result;
    }

    @Override
    public double[] minus(double[] arr1, double[] arr2) {
        double[] result = new double[arr1.length];

        for (int i = 0; i < arr1.length; ++i) {
            result[i] = arr1[i] - arr2[i];
        }

        return result;
    }

    @Override
    public double[] scaled(double[] arr, double c) {
        double[] result = new double[arr.length];

        for (int i = 0; i < arr.length; ++i) {
            result[i] = arr[i] * c;
        }

        return result;
    }
}
