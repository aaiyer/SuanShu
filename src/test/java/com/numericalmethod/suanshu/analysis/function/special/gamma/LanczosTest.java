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
package com.numericalmethod.suanshu.analysis.function.special.gamma;

import com.numericalmethod.suanshu.analysis.function.special.gamma.Lanczos;
import com.numericalmethod.suanshu.matrix.generic.matrixtype.RealMatrix;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LanczosTest {

    /**
     * Test of class Gamma.Lanczos.
     */
    @Test
    public void testGamma_Lanczos_0010() {
        Lanczos lanczos = new Lanczos(5, 7, 14);
        RealMatrix B = lanczos.B;
        RealMatrix C = lanczos.C;
        RealMatrix D = lanczos.D;

        RealMatrix expectedB = new RealMatrix(new double[][]{
                    {1, 1, 1, 1, 1, 1, 1},
                    {0, 1, -2, 3, -4, 5, -6},
                    {0, 0, 1, -4, 10, -20, 35},
                    {0, 0, 0, 1, -6, 21, -56},
                    {0, 0, 0, 0, 1, -8, 36},
                    {0, 0, 0, 0, 0, 1, -10},
                    {0, 0, 0, 0, 0, 0, 1}
                });
        assertEquals(expectedB, B);

        RealMatrix expectedC = new RealMatrix(new double[][]{
                    {0.5, 0, 0, 0, 0, 0, 0},
                    {-1, 2, 0, 0, 0, 0, 0},
                    {1, -8, 8, 0, 0, 0, 0},
                    {-1, 18, -48, 32, 0, 0, 0},
                    {1, -32, 160, -256, 128, 0, 0},
                    {-1, 50, -400, 1120, -1280, 512, 0},
                    {1, -72, 840, -3584, 6912, -6144, 2048}
                });
        assertEquals(expectedC, C);

        RealMatrix expectedD = new RealMatrix(new double[][]{
                    {1, 0, 0, 0, 0, 0, 0},
                    {0, -1, 0, 0, 0, 0, 0},
                    {0, 0, -6, 0, 0, 0, 0},
                    {0, 0, 0, -30, 0, 0, 0},
                    {0, 0, 0, 0, -140, 0, 0},
                    {0, 0, 0, 0, 0, -630, 0},
                    {0, 0, 0, 0, 0, 0, -2772}
                });
        assertEquals(expectedD, D);

        RealMatrix Z = lanczos.Z(BigDecimal.ONE);
        RealMatrix expectedZ = new RealMatrix(new double[][]{
                    {1, 0.5, 0.33333333333333, 0.25, 0.2, 0.16666666666667, 0.14285714285714}
                });

        assertEquals(expectedZ, Z);
//
//        Real W = lanczos.getW();
//        assertEquals(new Real("59.208284133962556000612295105264547536942921098"), W);
//
//        RealMatrix f = lanczos.getf();
//        RealMatrix expectedf = new RealMatrix(new double[][]{//from R
//                    {
//                        0.7030171119679993,
//                        0.1352202368254988,
//                        0.01977074592280813,
//                        0.002311928824453675,
//                        0.0002241036665723114,
//                        0.0000184896430607698,
//                        0.000001324946054076433
//                    }
//                });
//        assertEquals(expectedf, f);
    }

    /**
     * Test of class Gamma.Lanczos.
     *
     * TODO: investigate why changing EPSILON changes the result a bit.
     * It should not change because BigDecimal does not use EPSILON.
     */
    @Test
    public void testGamma_Lanczos_0020() {
        Lanczos lanczos = new Lanczos();
//        Lanczos lanczos = new Lanczos(607.0 / 128.0, 15, 30);//apache
//        Lanczos lanczos = new Lanczos(12.2252227365970611572265625, 17, 25);//boost
//        Lanczos lanczos = new Lanczos(20.3209821879863739013671875, 24, 35);//boost

//        System.out.println(lanczos.B);
//        System.out.println(lanczos.C);
//        System.out.println(lanczos.D);
//        System.out.println(lanczos.F);
//        System.out.println(lanczos.P);
//        System.out.println(lanczos.P4double);

        assertEquals(0.693147180559945, lanczos.logGammaQuick(3), 1e-15);
        assertEquals(0.5723649429247, lanczos.logGammaQuick(0.5), 1e-15);
        assertEquals(-0.1207822376352452, lanczos.logGammaQuick(1.5), 1e-15);
        assertEquals(0.2846828704729192, lanczos.logGammaQuick(2.5), 1e-15);
        assertEquals(2.032442066798749, lanczos.logGammaQuick(0.123456789), 1e-15);
        assertEquals(new BigDecimal("23.025850929882736295154601153204"),//copied from debugger
                lanczos.logGamma(new BigDecimal("0.0000000001")));//R has 23.02585092988274; more accurate value w/ scale = 60 is 23.025850929882736333885856799734780123049919531443746715288998
    }

    /**
     * Test of class Gamma.Lanczos.
     * These do not match the values from R when computed using double precision.
     */
    @Test
    public void testGamma_Lanczos_0030() {
        Lanczos lanczos = new Lanczos();
        assertEquals(1.2009736023470765, lanczos.logGammaQuick(3.5), 1e-15);//R has 1.200973602347074
        assertEquals(-0.059399817855879, lanczos.logGammaQuick(1.1234), 1e-17);//R has -0.0593998178558785
        assertEquals(23.02585092988274, lanczos.logGammaQuick(0.0000000001), 1e-6);//R has 23.02585092988274
    }

    /**
     * Test of class Gamma.Lanczos.
     */
    @Test
    public void testGamma_Lanczos_0040() {
        Lanczos lanczos = new Lanczos();
        assertEquals(23.02585092988274, lanczos.logGamma(0.0000000001), 1e-14);
        assertEquals(34.53877639491068, lanczos.logGamma(0.000000000000001), 1e-14);
        assertEquals(57.56462732485114, lanczos.logGamma(0.0000000000000000000000001), 1e-14);
        assertEquals(105.9189142777261, lanczos.logGamma(0.0000000000000000000000000000000000000000000001), 1e-13);
        assertEquals(151.970616137607, lanczos.logGamma(0.000000000000000000000000000000000000000000000000000000000000000001), 1e-12);
    }

    /**
     * Test of class Gamma.Lanczos.
     */
    @Test
    public void testGamma_Lanczos_0050() {
        Lanczos lanczos = new Lanczos();
        assertEquals(1.791759469228055, lanczos.logGamma(4), 1e-15);
        assertEquals(11.10003103605538, lanczos.logGamma(9.23), 1e-14);
        assertEquals(144.565743946345, lanczos.logGamma(50.00000000000000001), 1e-11);
        assertEquals(2132.933881185736, lanczos.logGamma(423.00000000000000001), 1e-11);
        assertEquals(2132.933881185736, lanczos.logGamma(423.00000000000000001), 1e-11);
        assertEquals(8906.90871565719, lanczos.logGamma(1423.123456789), 1e-11);
        assertEquals(952921.618731999, lanczos.logGamma(91423.123456789), 1e-11);
    }

    /**
     * Test of class Gamma.Lanczos.
     * Very big numbers.
     *
     * TODO: investigate why changing EPSILON changes the result a bit.
     * It should not change because BigDecimal does not use EPSILON.
     */
    @Test
    public void testGamma_Lanczos_0060() {
        Lanczos lanczos = new Lanczos();
        assertEquals(new BigDecimal("2847512102458407.335799123634153678443679372327"),//copied from debugger
                lanczos.logGamma(new BigDecimal("91423123456789")));//R has 2847512102458408
    }

    /**
     * Test of class Gamma.Lanczos.
     */
    @Test
    public void testGamma_Lanczos_0070() {
        Lanczos lanczos = new Lanczos();
        assertEquals(0, lanczos.logGamma(1), 1e-15);
        assertEquals(0, lanczos.logGamma(2), 1e-15);
    }
}
