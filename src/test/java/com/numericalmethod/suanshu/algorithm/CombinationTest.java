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

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CombinationTest {

    @Test
    public void test_0010() {
        Integer[][] sets = new Integer[][]{
            {1, 2},
            {3, 4}
        };

        Combination<Integer> combination = new Combination<Integer>(sets);

        Integer[][] expResults = new Integer[][]{
            {1, 3},
            {1, 4},
            {2, 3},
            {2, 4}
        };

        int i = 0;
        for (List<Integer> set : combination) {
            assertArrayEquals(expResults[i++], set.toArray(new Integer[0]));
        }
    }

    /**
     * Calling iterator multiple times.
     */
    @Test
    public void test_0025() {
        Integer[][] sets = new Integer[][]{
            {1, 2},
            {3, 4}
        };

        Combination<Integer> combination = new Combination<Integer>(sets);

        Integer[][] expResults = new Integer[][]{
            {1, 3},
            {1, 4},
            {2, 3},
            {2, 4}
        };

        int i = 0;
        for (List<Integer> set : combination) {
            assertArrayEquals(expResults[i++], set.toArray(new Integer[0]));
        }


        i = 0;
        for (List<Integer> set : combination) {
            assertArrayEquals(expResults[i++], set.toArray(new Integer[0]));
        }
    }

    @Test
    public void test_0020() {
        Integer[][] sets = new Integer[][]{
            {1, 2},
            {3, 4, 5},
            {6},
            {7, 8, 9, 10}
        };

        Combination<Integer> combination = new Combination<Integer>(sets);

        Integer[][] expResults = new Integer[][]{
            {1, 3, 6, 7},
            {1, 3, 6, 8},
            {1, 3, 6, 9},
            {1, 3, 6, 10},
            {1, 4, 6, 7},
            {1, 4, 6, 8},
            {1, 4, 6, 9},
            {1, 4, 6, 10},
            {1, 5, 6, 7},
            {1, 5, 6, 8},
            {1, 5, 6, 9},
            {1, 5, 6, 10},
            {2, 3, 6, 7},
            {2, 3, 6, 8},
            {2, 3, 6, 9},
            {2, 3, 6, 10},
            {2, 4, 6, 7},
            {2, 4, 6, 8},
            {2, 4, 6, 9},
            {2, 4, 6, 10},
            {2, 5, 6, 7},
            {2, 5, 6, 8},
            {2, 5, 6, 9},
            {2, 5, 6, 10}
        };

        int i = 0;
        for (List<Integer> set : combination) {
            assertArrayEquals(expResults[i++], set.toArray(new Integer[0]));
        }
    }
}
