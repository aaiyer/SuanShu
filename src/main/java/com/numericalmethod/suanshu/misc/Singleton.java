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
package com.numericalmethod.suanshu.misc;

/**
 * This class demonstrates how a singleton can be implemented.
 * A singleton is a class which can be instantiated only once.
 * This is to ensure that there can be only one instance of the class per jvm.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Singleton_pattern">Wikipedia: Singleton pattern</a>
 */
class Singleton {

    /**
     * This singleton implementation is by Bill Pugh.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Singleton_pattern#The_solution_of_Bill_Pugh">
     */
    private Singleton() {
    }

    private static class Inner {

        private static final Singleton INSTANCE = new Singleton();
    }

    /**
     * Get the instance of the singleton.
     *
     * @return the reference to the one and only one instance
     */
    public static Singleton getInstance() {
        return Inner.INSTANCE;
    }
}
