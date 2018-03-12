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
package com.numericalmethod.suanshu.stats.random.univariate.exp;

import com.numericalmethod.suanshu.stats.random.univariate.uniform.SHR3;
import static java.lang.Math.*;

/**
 * This implements the ziggurat algorithm to sample from the exponential distribution.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"George Marsaglia, Wai Wan Tsang, "The ZigguratExp Method for Generating Random Variables," Journal of Statistical Software 5 (8), 2000."
 * <li><a href="http://www.jstatsoft.org/v05/i08/supp/1">a C implementation of the Ziggurat algorithm</a>
 * <li><a href="http://people.sc.fsu.edu/~jburkardt/c_src/ziggurat/ziggurat.c">another C implementation of the Ziggurat algorithm</a>
 * </ul>
 */
public class Ziggurat2000Exp implements RandomExpGenerator {

    static {
        zigset();//initialization
    }
    private static int[] ke;
    private static double[] we, fe;
    private final SHR3 shr3 = new SHR3();

    @Override
    public void seed(long... seeds) {
        shr3.seed(seeds[0]);
    }

    @Override
    public double nextDouble() {
        return REXP();
    }

    private double REXP() {
        int jz = shr3.nextInt();
        int iz = jz & 255;
        return (Math.abs(jz) < ke[iz]) ? abs(jz) * we[iz] : efix(jz, iz);
    }

    private double efix(int jz, int iz) {
        for (;;) {
            if (iz == 0) {
                return (7.69711 - log(shr3.nextDouble()));//iz==0
            }

            float x = (float) (abs(jz) * we[iz]);
            if (fe[iz] + shr3.nextDouble() * (fe[iz - 1] - fe[iz]) < exp(-x)) {
                return (x);
            }

            //initiate, try to exit for(;;) loop
            jz = shr3.nextInt();
            iz = (jz & 255);
            if (abs(jz) < ke[iz]) {
                return (abs(jz) * we[iz]);
            }
        }
    }

    private static synchronized void zigset() {
        we = new double[256];
        fe = new double[256];
        ke = new int[256];

        double m2 = 2147483648.0;
        double de = 7.697117470131487, te = de, ve = 3.949659822581572e-3, q;
        int i;

        // set up tables for REXP
        q = ve / exp(-de);
        ke[0] = (int) ((de / q) * m2);
        ke[1] = 0;

        we[0] = q / m2;
        we[255] = de / m2;

        fe[0] = 1.;
        fe[255] = exp(-de);

        for (i = 254; i >= 1; i--) {
            de = -log(ve / de + exp(-de));
            ke[i + 1] = (int) ((de / te) * m2);
            te = de;
            fe[i] = exp(-de);
            we[i] = de / m2;
        }
    }
}
