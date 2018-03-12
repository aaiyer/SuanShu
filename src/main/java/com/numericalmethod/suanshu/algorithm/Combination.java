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
package com.numericalmethod.suanshu.algorithm;

import java.util.*;

/**
 * A combination is a way of selecting several things out of a larger group, where (unlike permutations) order does not matter.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Combination">Wikipedia: Combination</a>
 */
public class Combination<T> implements Iterable<List<T>> {

    private final T[][] sets;

    /**
     * Construct an {@link Iterable} of all combinations of arrays, taking one element from each array.
     * <p/>
     * For example, suppose we have two arrays:
     * <pre>
     * {1, 2}
     * {3, 4, 5}
     * </pre>
     *
     * The possible combinations are:
     * <pre>
     * {1, 3}
     * {1, 4}
     * {1, 5}
     * {2, 3}
     * {2, 4}
     * {2, 5}
     * </pre>
     *
     * @param sets arrays of elements
     */
    public Combination(T[]... sets) {//TODO: pass in instead an array of List to make consistent of input and output?
        this.sets = sets;
    }

    @Override
    public Iterator<List<T>> iterator() {
        final int[] pointers = new int[sets.length];//assume that they are initialized to 0s
        pointers[sets.length - 1] = -1;

        return new Iterator<List<T>>() {

            private List<T> nextList = nextList();

            private List<T> nextList() {
                List<T> list = new ArrayList<T>(sets.length);
                if (advanced(pointers.length - 1)) {
                    for (int i = 0; i < pointers.length; ++i) {
                        list.add(sets[i][pointers[i]]);
                    }

                    return list;
                }

                return null;
            }

            private boolean advanced(int level) {
                if (level < 0) {
                    return false;
                }

                ++pointers[level];
                if (pointers[level] >= sets[level].length) {
                    pointers[level] = 0;
                    return advanced(level - 1);
                }

                return true;
            }

            @Override
            public boolean hasNext() {
                return nextList != null;
            }

            @Override
            public List<T> next() {
                if (nextList == null) {
                    throw new NoSuchElementException();
                }

                List<T> result = Collections.unmodifiableList(nextList);
                nextList = nextList();
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
