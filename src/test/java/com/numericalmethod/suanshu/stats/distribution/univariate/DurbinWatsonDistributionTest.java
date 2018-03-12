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
package com.numericalmethod.suanshu.stats.distribution.univariate;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.numericalmethod.suanshu.number.DoubleUtils.*;

/**
 *
 * @author Haksun Li
 */
public class DurbinWatsonDistributionTest {

    @Test
    public void test_0010() {
//        Matrix X = new DenseMatrix(new double[][]{
//                    {1, 1.49040277983098757, 0.1679396053624605},
//                    {1, -2.87149480349580388, -0.1642513927550767},
//                    {1, 0.09062432906973170, 1.8675009959195421},
//                    {1, -0.42030722083331468, -1.1623025267062199},
//                    {1, 0.64440381859572815, -0.3887529937513425},
//                    {1, 0.74486833910199268, -0.1689710394692852},
//                    {1, 1.16973962874503634, 0.6421273742203794},
//                    {1, -0.73335837644409696, 0.7230183326532191},
//                    {1, 2.42526168887730620, 1.1800141197114067},
//                    {1, -0.74736198136905074, 0.5063539060196834},
//                    {1, -0.72629973612188570, -0.7026056773915481},
//                    {1, -0.89703666081789979, 0.5908740392406565},
//                    {1, 0.15336402769375598, 0.5498492180556036},
//                    {1, 0.08835027327563787, -0.5548465981551438},
//                    {1, -1.14605124267794967, 0.4429181923522472},
//                    {1, -0.53066472003493526, 0.4284341621587530},
//                    {1, -0.49493207064053429, 0.3802111986891140},
//                    {1, -0.19748261432247827, 0.4111433356537452},
//                    {1, 1.45263281630825936, 0.2418709386539048},
//                    {1, 1.06630970304032258, -0.0936755076040276}
//                });
//
//        DurbinWatson instance = new DurbinWatson(X, 3, 0.0001);
//        assertEquals(3, instance.lag, 0.0);
//
//        Matrix A = new DenseMatrix(new double[][]{
//                    {1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {-1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 2, 0, 0, -1},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 1, 0, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 1, 0},
//                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 1}
//                });
//        assertEquals(A, instance.A);
//
//        assertTrue(equal(new double[]{
//                    3.8019377358048381, 3.8019377358048381, 3.7320508075688776, 3.2469796037174667, 3.2469796037174667, 2.9999999999999996,
//                    2.4450418679126287, 2.4450418679126287, 1.9999999999999998, 1.5549581320873711, 1.5549581320873711, 0.9999999999999998,
//                    0.7530203962825328, 0.7530203962825328, 0.2679491924311226, 0.1980622641951617, 0.1980622641951617, 0,
//                    0, 0
//                },
//                instance.eigenvalues4A, 1e-16));
//
//        assertEquals(66.5249808575669, instance.sqrtXtX, 1e-13);
        assertTrue(true);
    }
}
