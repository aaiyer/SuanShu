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
package com.numericalmethod.suanshu.analysis.function.tuple;

/**
 * An ordered pair (x,y) is a pair of mathematical objects.
 * In the ordered pair (x,y), the object a is called the first entry, and the object b the second entry of the pair.
 * Alternatively, the objects are called the first and second coordinates, or the left and right projections of the ordered pair.
 * The order in which the objects appear in the pair is significant: the ordered pair (x,y) is different from the ordered pair (y,x) unless x = y.
 * <p/>
 * This class is immutable.
 *
 * @author Haksun Li
 */
public class Pair {

    /** <i>x</i> */
    public final double x;
    /** <i>y</i> */
    public final double y;

    /**
     * Construct a pair.
     *
     * @param x <i>x</i>
     * @param y <i>y</i>
     */
    public Pair(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
