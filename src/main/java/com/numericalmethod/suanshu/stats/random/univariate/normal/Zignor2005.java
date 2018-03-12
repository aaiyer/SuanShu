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
import static java.lang.Math.*;

/**
 * This is an improved version of the Ziggurat algorithm as proposed in the reference.
 *
 * @author Haksun Li
 * @see "J. A. Doornik, "An Improved Ziggurat Method to Generate Normal Random Samples," Nuffield College, University of Oxford, 2005."
 */
public class Zignor2005 implements RandomStandardNormalNumberGenerator {

    // number of blocks
    private static final int ZIGNOR_C = 128;
    // start of the right tail
    private static final double ZIGNOR_R = 3.442619855899;
    // (R * phi(R) + Pr(X>=R)) * sqrt(2\pi)
    private static final double ZIGNOR_V = 9.91256303526217e-3;
    private RandomLongGenerator uniform = new MWC8222();

    /*
     * s_adZigX holds coordinates, such that each rectangle has same area;
     * s_adZigR holds s_adZigX[i + 1] / s_adZigX[i]
     */
    private static double[] s_adZigX = new double[ZIGNOR_C + 1];
    private static double[] s_adZigR = new double[ZIGNOR_C];

    {
        zigNorInit(ZIGNOR_C, ZIGNOR_R, ZIGNOR_V);
    }

    private static void zigNorInit(int iC, double dR, double dV) {
        int i;
        double f;

        f = exp(-0.5 * dR * dR);
        s_adZigX[0] = dV / f; /* [0] is bottom block: V / f(R) */
        s_adZigX[1] = dR;
        s_adZigX[iC] = 0;

        for (i = 2; i < iC; ++i) {
            s_adZigX[i] = sqrt(-2 * log(dV / s_adZigX[i - 1] + f));
            f = exp(-0.5 * s_adZigX[i] * s_adZigX[i]);
        }
        for (i = 0; i < iC; ++i) {
            s_adZigR[i] = s_adZigX[i + 1] / s_adZigX[i];
        }
    }

    @Override
    public void seed(long... seeds) {
        uniform.seed(seeds);
    }

    @Override
    public double nextDouble() {
        return DRanNormalZig();
    }

    private double DRanNormalZig() {
        int i;
        double x, u, f0, f1;

        for (;;) {
            u = 2 * DRanU() - 1;
            i = IRanU() & 0x7F;
            /* first try the rectangular boxes */
            if (abs(u) < s_adZigR[i]) {
                return u * s_adZigX[i];
            }
            /* bottom box: sample from the tail */
            if (i == 0) {
                return DRanNormalTail(ZIGNOR_R, u < 0);
            }
            /* is this a sample from the wedges? */
            x = u * s_adZigX[i];
            f0 = exp(-0.5 * (s_adZigX[i] * s_adZigX[i] - x * x));
            f1 = exp(-0.5 * (s_adZigX[i + 1] * s_adZigX[i + 1] - x * x));
            if (f1 + DRanU() * (f0 - f1) < 1.0) {
                return x;
            }
        }
    }

    private double DRanNormalTail(double dMin, boolean iNegative) {
        double x, y;
        do {
            x = log(DRanU()) / dMin;
            y = log(DRanU());
        } while (-2 * y < x * x);
        return iNegative ? x - dMin : dMin - x;
    }

    private double DRanU() {
        return uniform.nextDouble();
    }

    private int IRanU() {
        return (int) uniform.nextLong();
    }
}
