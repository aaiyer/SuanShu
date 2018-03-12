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
package com.numericalmethod.suanshu.analysis.differentiation.univariate;

import com.numericalmethod.suanshu.analysis.function.rn2r1.univariate.UnivariateRealFunction;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.StandardCumulativeNormal;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Digamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.Gamma;
import com.numericalmethod.suanshu.analysis.function.special.gamma.GammaLanczosQuick;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.Gaussian;
import com.numericalmethod.suanshu.analysis.function.special.gamma.LogGamma;
import com.numericalmethod.suanshu.analysis.function.special.gaussian.CumulativeNormalMarsaglia;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DfdxTest {

    @Test
    public void test_0010() {
        Polynomial p = new Polynomial(new double[]{3.5, 7.1, 1, 2, 1});
        Polynomial dp = new Polynomial(new double[]{14, 21.3, 2, 2});

        UnivariateRealFunction instance = new Dfdx(p);
        for (double d = 0.001; d < 1; d = d + 0.001) {
            assertEquals(dp.evaluate(d), instance.evaluate(d), 1e-13);
        }
    }

    @Test
    public void test_0020() {
        UnivariateRealFunction f = new LogGamma();
        UnivariateRealFunction df = new Digamma();
        UnivariateRealFunction instance = new Dfdx(f);

        for (double d = 0.001; d < 1; d = d + 0.001) {
            assertEquals(df.evaluate(d), instance.evaluate(d), 1e-15);
        }
    }

    @Test
    public void test_0030() {
        UnivariateRealFunction f = new CumulativeNormalMarsaglia();
        UnivariateRealFunction df = new Gaussian();
        UnivariateRealFunction instance = new Dfdx(f);

        for (double d = 0.001; d < 1; d = d + 0.001) {
            assertEquals(df.evaluate(d), instance.evaluate(d), 1e-15);
        }
    }

    @Test
    public void test_0040() {
        UnivariateRealFunction f = new GammaLanczosQuick();
        UnivariateRealFunction df = new Digamma();
        UnivariateRealFunction instance = new Dfdx(f);

        for (double d = 0.001; d < 1; d = d + 0.001) {
            assertEquals(df.evaluate(d) * f.evaluate(d), instance.evaluate(d), 1e-15);
        }
    }
}
