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
 * It is possible to provide different implementations for different platforms, hardware, etc.
 * For example, there are single vs. multiple threads, single vs. multiple cores, single vs. multiple machines, GPU or not, etc.
 *
 * @author Ken
 */
public interface DoubleArrayOperation {

    /**
     * Add two {@code double} arrays.
     *
     * @param arr1 an array {@code double[]}
     * @param arr2 an array {@code double[]}
     * @return the sum of the two arrays
     */
    public double[] add(double[] arr1, double[] arr2);

    /**
     * Subtract one {@code double} array from another.
     *
     * @param arr1 an array {@code double[]}
     * @param arr2 an array {@code double[]}
     * @return the difference of the two arrays
     */
    public double[] minus(double[] arr1, double[] arr2);

    /**
     * Scale a {@code double} array.
     *
     * @param arr an array {@code double[]}
     * @param c   a scaling constant
     * @return the scaled array
     */
    public double[] scaled(double[] arr, double c);
}
