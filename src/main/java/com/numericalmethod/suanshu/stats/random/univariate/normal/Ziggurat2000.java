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
package com.numericalmethod.suanshu.stats.random.univariate.normal;

import com.numericalmethod.suanshu.stats.random.univariate.RandomLongGenerator;
import com.numericalmethod.suanshu.stats.random.univariate.uniform.MWC8222;

/**
 * The Ziggurat algorithm is an algorithm for pseudo-random number sampling from the Normal distribution.
 * This is considerably faster than the two more commonly used methods to generate normally distributed random numbers,
 * the Marsaglia polar method or the Box-Muller transform, which require at least a logarithm and a square root.
 * Empirically, however, the {@linkplain MarsagliaBray1964 Marsaglia} polar method and the {@link BoxMuller} transform seem to have better distributional properties.
 * There are implementation problems in the original uniform random number generator proposed in the paper.
 * To improve the quality, we use {@link MWC8222} for the uniform random number generation.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"George Marsaglia, Wai Wan Tsang, "The Ziggurat Method for Generating Random Variables," Journal of Statistical Software 5 (8), 2000"
 * <li><a href="http://en.wikipedia.org/wiki/Ziggurat_algorithm">Wikipedia: Ziggurat algorithm</a>
 * <li><a href="http://www.jstatsoft.org/v05/i08/supp/1">a C implementation of the Ziggurat algorithm</a>
 * <li><a href="http://people.sc.fsu.edu/~jburkardt/c_src/ziggurat/ziggurat.c">another C implementation of the Ziggurat algorithm</a>
 * <li><a href="http://arxiv.org/PS_cache/math/pdf/0603/0603058v1.pdf">Boaz Nadler. "Design Flaws in the Implementation of the Ziggurat and Monty Python methods (and some remarks on Matlab randn)," The Journal of Business. arXiv:math/0603058v1. 2 March 2006.</a>
 * </ul>
 */
public class Ziggurat2000 implements RandomStandardNormalNumberGenerator {

    static {
        zigset();//initialization
    }
    private static int[] kn;
    private static double[] wn, fn;
    private final RandomLongGenerator uniform = new MWC8222();

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds[0]);
    }

    @Override
    public double nextDouble() {
        return RNOR();
    }

    private double RNOR() {
        int hz = (int) uniform.nextLong();
        int iz = hz & 127;
        return (Math.abs(hz) < kn[iz]) ? hz * wn[iz] : nfix(hz, iz);
    }

    private double nfix(int hz, int iz) {
        double r = 3.442619855899;//the start of the right tail
        double r1 = 1 / r;

        for (double x, y;;) {
            if (iz == 0) {
                do {
                    x = -Math.log(uniform.nextDouble()) * r1;
                    y = -Math.log(uniform.nextDouble());
                } while (y + y < x * x);
                return (hz > 0) ? r + x : -r - x;
            }

            x = hz * wn[iz];//iz==0, handles the base strip

            //iz>0, handle the wedges of other strips
            if (fn[iz] + uniform.nextDouble() * (fn[iz - 1] - fn[iz]) < Math.exp(-0.5 * x * x)) {
                return x;
            }

            //initiate, try to exit for(;;) for loop
            hz = (int) uniform.nextLong();
            iz = hz & 127;

            if (Math.abs(hz) < kn[iz]) {
                return (hz * wn[iz]);
            }
        }
    }

    private static synchronized void zigset() {
        wn = new double[128];
        fn = new double[128];
        kn = new int[128];

        double m1 = 2147483648.0;
        double dn = 3.442619855899, tn = dn, vn = 9.91256303526217e-3, q;
        int i;

        // set up tables for RNOR
        q = vn / Math.exp(-0.5 * dn * dn);
        kn[0] = (int) ((dn / q) * m1);
        kn[1] = 0;

        wn[0] = q / m1;
        wn[127] = dn / m1;

        fn[0] = 1.;
        fn[127] = Math.exp(-0.5 * dn * dn);

        for (i = 126; i >= 1; i--) {
            dn = Math.sqrt(-2. * Math.log(vn / dn + Math.exp(-0.5 * dn * dn)));
            kn[i + 1] = (int) ((dn / tn) * m1);
            tn = dn;
            fn[i] = Math.exp(-0.5 * dn * dn);
            wn[i] = dn / m1;
        }
    }
}
