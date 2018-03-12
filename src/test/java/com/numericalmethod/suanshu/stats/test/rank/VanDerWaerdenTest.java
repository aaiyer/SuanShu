/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.numericalmethod.suanshu.stats.test.rank;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class VanDerWaerdenTest {

    /**
     * Handbook of Parametric and Non-parametric Statistical Procedures, 2nd edition
     * David Sheskin
     *
     * Table 23.1. Section 23.4.
     */
    @Test
    public void test_0010() {
        double[][] samples = new double[3][];
        samples[0] = new double[]{8, 10, 9, 10, 9};
        samples[1] = new double[]{7, 8, 5, 8, 5};
        samples[2] = new double[]{4, 8, 7, 5, 7};

        VanDerWaerden instance = new VanDerWaerden(samples);
        assertEquals(8.10, instance.statistics(), 1e0);//the book seem to have a big truncation error
    }

    /**
     * Handbook of Parametric and Non-parametric Statistical Procedures, 2nd edition
     * David Sheskin
     *
     * Table 23.3. Section 23.4.
     */
    @Test
    public void test_0020() {
        double[][] samples = new double[2][];
        samples[0] = new double[]{11, 1, 0, 2, 0};
        samples[1] = new double[]{11, 11, 5, 8, 4};

        VanDerWaerden instance = new VanDerWaerden(samples);
        assertEquals(3.16, instance.statistics(), 1e-2);
    }
}
