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
package com.numericalmethod.suanshu;

import com.numericalmethod.suanshu.number.ScientificNotation;
import static java.lang.Math.*;

/**
 * This class lists the global parameters and constants in this SuanShu library.
 *
 * @author Haksun Li
 */
public class Constant {

    /* Make sure these parameters are properly configured on startup. */
    static {//TODO: allow reading from a file
        MACH_EPS = getMachineEpsilon();
        MACH_SCALE = abs(new ScientificNotation(Constant.MACH_EPS).exponent()) + 1;
    }
    /**
     * the machine epsilon
     * <p/>
     * This is the difference between 1 and the smallest exactly representable number greater than 1.
     *
     * @see <a href="http://en.wikipedia.org/wiki/Machine_epsilon">Wikipedia: Machine epsilon</a>
     */
    public static final double MACH_EPS;
    /** the scale for the machine epsilon */
    public static final int MACH_SCALE;
    /** the default epsilon used in this library */
    public static final double EPSILON = pow(10, new ScientificNotation(MACH_EPS).exponent() + 1);
    /** \(\sqrt{2}\) */
    public static final double ROOT_2 = sqrt(2d);
    /** \(\sqrt{2\pi}\) */
    public static final double ROOT_2_PI = sqrt(2d * PI);
    /** \(\sqrt{\pi}\) */
    public static final double ROOT_PI = sqrt(PI);
    /** \(\pi^2\) */
    public static final double PI_SQ = PI * PI;
    /**
     * the Euler–Mascheroni constant
     *
     * @see <a href="http://en.wikipedia.org/wiki/Euler%E2%80%93Mascheroni_constant">Wikipedia: Euler–Mascheroni constant</a>
     */
    public static final double EULER_MASCHERONI = 0.57721566490153286060651209008240243104215933593992;
    /**
     * the Golden ratio
     *
     * @see <a href="http://en.wikipedia.org/wiki/Golden_ratio">Wikipedia: Golden ratio</a>
     */
    public static final double GOLDEN_RATIO = (sqrt(5) + 1) / 2;

    private Constant() {
        // utility class has no constructor
    }

    /**
     * The machine epsilon differs per machine so we need to compute it.
     * <p/>
     * This algorithm does not actually determine the machine epsilon.
     * Rather, it determines a number within a factor of two (one order of magnitude) of the true machine epsilon,
     * using a linear search.
     *
     * @return the machine epsilon
     * @see <a href="http://en.wikipedia.org/wiki/Machine_epsilon#Approximation_using_Java">Wikipedia: Approximation using Java</a>
     */
    private static double getMachineEpsilon() {
        double epsilon = 1;

        do {
            epsilon /= 2;
        } while ((1.0 + (epsilon / 2.0)) != 1.0);

        return epsilon;
    }

    /**
     * Get the unit round off as defined in the reference.
     *
     * @param base      base, e.g., 10
     * @param precision precision (number of digits)
     * @return the unit round off
     * @see "G. H. Golub, C. F. van Loan, "Eq. 2.4.4, Section 2.4.1," Matrix Computations, 3rd edition."
     */
    public static double unitRoundOff(int base, int precision) {
        return 0.5 * pow(base, 1 - precision);
    }

    /**
     * Get the default unit round off.
     *
     * @return the default unit round off
     */
    public static double unitRoundOff() {
        return unitRoundOff(10, 56);
    }
}
