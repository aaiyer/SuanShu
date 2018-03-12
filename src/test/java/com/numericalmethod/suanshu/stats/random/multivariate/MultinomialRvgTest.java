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
package com.numericalmethod.suanshu.stats.random.multivariate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class MultinomialRvgTest {

    @Test
    public void test_0010() {
        MultinomialRvg instance = new MultinomialRvg(100000, new double[]{0.5, 0.5});
        double[] bin = instance.nextVector();

        double total = 0;
        for (int i = 0; i < bin.length; ++i) {
            total += bin[i];
        }

        assertEquals(0.5, bin[0] / total, 1e-2);
        assertEquals(0.5, bin[1] / total, 1e-2);
    }

    @Test
    public void test_0020() {
        MultinomialRvg instance = new MultinomialRvg(100000, new double[]{0.7, 0.3});
        double[] bin = instance.nextVector();

        double total = 0;
        for (int i = 0; i < bin.length; ++i) {
            total += bin[i];
        }

        assertEquals(0.7, bin[0] / total, 1e-2);
        assertEquals(0.3, bin[1] / total, 1e-2);
    }

    @Test
    public void test_0030() {
        MultinomialRvg instance = new MultinomialRvg(100000, new double[]{0.0, 1.0});
        double[] bin = instance.nextVector();

        double total = 0;
        for (int i = 0; i < bin.length; ++i) {
            total += bin[i];
        }

        assertEquals(0, bin[0] / total, 1e-15);
        assertEquals(1, bin[1] / total, 1e-15);
    }

    @Test
    public void test_0040() {
        MultinomialRvg instance = new MultinomialRvg(100000, new double[]{0.5, 1.0});
        double[] bin = instance.nextVector();

        double total = 0;
        for (int i = 0; i < bin.length; ++i) {
            total += bin[i];
        }

        assertEquals(1. / 3, bin[0] / total, 1e-2);
        assertEquals(2. / 3, bin[1] / total, 1e-2);
    }

    @Test
    public void test_0050() {
        MultinomialRvg instance = new MultinomialRvg(100000, new double[]{0.1, 0.2, 0.3, 0.4});
        double[] bin = instance.nextVector();

        double total = 0;
        for (int i = 0; i < bin.length; ++i) {
            total += bin[i];
        }

        assertEquals(0.1, bin[0] / total, 1e-2);
        assertEquals(0.2, bin[1] / total, 1e-2);
        assertEquals(0.3, bin[2] / total, 1e-2);
        assertEquals(0.4, bin[3] / total, 1e-2);
    }

    /**
     * test seeding
     */
    @Test
    public void test_0060() {
        MultinomialRvg instance = new MultinomialRvg(100000, new double[]{0.1, 0.2, 0.3, 0.4});
        instance.seed(1234567890L);
        double[] bin = instance.nextVector();
        assertArrayEquals(new double[]{10107.0, 19833.0, 29915.0, 40145.0}, bin, 0);
    }
}
