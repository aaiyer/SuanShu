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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chen Chen
 */
public class JordanExchangeTest {

    /*
     * Random test for a 7-by-5 matrix.
     * Matlab code:
     * >>format long
     * >>jx(A,3,4)
     * >>jx(A,7,5)
     */
    @Test
    public void test_JordanExchange_0010() {
        Matrix A = new DenseMatrix(new double[][]{
                    {3.4000, 5.0000, 6.0000, 3.2000, 6.0000},
                    {134.0000, 1.0000, 6.0000, -2.0000, -6.0000},
                    {5.0000, -1.0000, -61.0000, -61.0000, -4.0000},
                    {324.0000, 2.0000, 1.0000, -1.0000, 3.6000},
                    {15.6400, 17.2400, 18.2400, 15.4400, 18.2400},
                    {146.2400, 13.2400, 18.2400, 10.2400, 6.2400},
                    {17.2400, 11.2400, -48.7600, -48.7600, 8.2400}
                });

        //exchange row 3 with column 4
        JordanExchange JE = new JordanExchange(A, 3, 4);
        Matrix expectedJE = new DenseMatrix(new double[][]{
                    {0.03662295081967, 0.04947540983607, 0.02800000000000, -0.00052459016393, 0.05790163934426},
                    {1.33836065573771, 0.01032786885246, 0.08000000000000, 0.00032786885246, -0.05868852459016},
                    {0.00081967213115, -0.00016393442623, -0.01000000000000, -0.00016393442623, -0.00065573770492},
                    {3.23918032786885, 0.02016393442623, 0.02000000000000, 0.00016393442623, 0.03665573770492},
                    {0.16905573770492, 0.16986885245902, 0.02800000000000, -0.00253114754098, 0.17227540983607},
                    {1.47079344262295, 0.13072131147541, 0.08000000000000, -0.00167868852459, 0.05568524590164},
                    {0.13243278688525, 0.12039344262295, 0, 0.00799344262295, 0.11437377049180}}).scaled(100);
        /*{3.6623, 4.9475, 2.8000, -0.0525, 5.7902},
        {133.8361, 1.0328, 8.0000, 0.0328, -5.8689},
        {0.0820, -0.0164, -1.0000, -0.0164, -0.0656},
        {323.9180, 2.0164, 2.0000, 0.0164, 3.6656},
        {16.9056, 16.9869, 2.8000, -0.2531, 17.2275},
        {147.0793, 13.0721, 8.0000, -0.1679, 5.5685},
        {13.2433, 12.0393, 0, 0.7993, 11.4374}*/
        // System.out.print(JE.toString());
        assertTrue(AreMatrices.equal(expectedJE, JE.toMatrix(), 1e-12));

        //exchange row 7 with column 5
        JE = new JordanExchange(A, 7, 5);
        expectedJE = new DenseMatrix(new double[][]{
                    {-0.09153398058252, -0.03184466019417, 0.41504854368932, 0.38704854368932, 0.00728155339806},
                    {1.46553398058252, 0.09184466019417, -0.29504854368932, -0.37504854368932, -0.00728155339806},
                    {0.13368932038835, 0.04456310679612, -0.84669902912621, -0.84669902912621, -0.00485436893204},
                    {3.16467961165049, -0.02910679611650, 0.22302912621359, 0.20302912621359, 0.00436893203883},
                    {-0.22522330097087, -0.07640776699029, 1.26174757281553, 1.23374757281553, 0.02213592233010},
                    {1.33184466019417, 0.04728155339806, 0.55165048543689, 0.47165048543689, 0.00757281553398},
                    {-0.02092233009709, -0.01364077669903, 0.05917475728155, 0.05917475728155, 0.00121359223301}}).scaled(100);

        /*
        {-9.1534, -3.1845, 41.5049, 38.7049, 0.7282},
        {146.5534, 9.1845, -29.5049, -37.5049, -0.7282},
        {13.3689, 4.4563, -84.6699, -84.6699, -0.4854},
        {316.4680, -2.9107, 22.3029, 20.3029, 0.4369},
        {-22.5223, -7.6408, 126.1748, 123.3748, 2.2136},
        {133.1845, 4.7282, 55.1650, 47.1650, 0.7573},
        {-2.0922, -1.3641, 5.9175, 5.9175, 0.1214}
        });*/
        assertTrue(AreMatrices.equal(expectedJE, JE.toMatrix(), 1e-12));
    }

    /*
     * First we exchange the 1st column with the 1st row.
     * Then we exchange the 2nd column with the 2nd row.
     * This has the effect of inverting the matrix A.
     */
    @Test
    public void test_JordanExchange_0020() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 10},
                    {31, 2}
                });
        JordanExchange C = new JordanExchange(new JordanExchange(A, 1, 1), 2, 2);
        Matrix AC = A.multiply(C.toMatrix());
        assertTrue(AreMatrices.equal(AC, AC.ONE(), 1e-14));
    }

    /**
     * The example on p. 21.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_JordanExchange_0030() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, 1},
                    {3, 1}
                });

        JordanExchange JE1 = new JordanExchange(A, 1, 1);
        Matrix expectedJE1 = new DenseMatrix(new double[][]{
                    {0.5, -0.5},
                    {1.5, -0.5}
                });
        assertTrue(AreMatrices.equal(expectedJE1, JE1.toMatrix(), 1e-15));

        JordanExchange JE2 = new JordanExchange(JE1, 2, 2);
        Matrix expectedJE2 = new DenseMatrix(new double[][]{
                    {-1, 1},
                    {3, -2}
                });
        assertTrue(AreMatrices.equal(expectedJE2, JE2.toMatrix(), 1e-15));

        JordanExchange C = new JordanExchange(JE1, 2, 2);
        Matrix AC = A.multiply(C.toMatrix());
        assertTrue(AreMatrices.equal(AC, AC.ONE(), 1e-14));
    }

    /**
     * Example 2-2-1.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_JordanExchange_0040() {
        Matrix A = new DenseMatrix(new double[][]{
                    {1, 2, 4},
                    {3, 4, 8},
                    {5, 6, 12}
                });

        JordanExchange JE1 = new JordanExchange(A, 1, 1);
        Matrix expectedJE1 = new DenseMatrix(new double[][]{
                    {1, -2, -4},
                    {3, -2, -4},
                    {5, -4, -8}
                });
        assertTrue(AreMatrices.equal(expectedJE1, JE1.toMatrix(), 1e-15));

        JordanExchange JE2 = new JordanExchange(JE1, 2, 2);
        Matrix expectedJE2 = new DenseMatrix(new double[][]{
                    {-2, 1, 0},
                    {1.5, -0.5, -2},
                    {-1, 2, 0}
                });
        assertTrue(AreMatrices.equal(expectedJE2, JE2.toMatrix(), 1e-15));
    }

    /**
     * Example 2-2-2.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_JordanExchange_0050() {
        Matrix A = new DenseMatrix(new double[][]{
                    {-1, 0, 3},
                    {2, -2, 4},
                    {0, -2, 10}
                });

        JordanExchange JE1 = new JordanExchange(A, 2, 1);
        JordanExchange JE2 = new JordanExchange(JE1, 1, 2);
        Matrix expectedJE2 = new DenseMatrix(new double[][]{
                    {-0.5, -1, 5},
                    {0, -1, 3},
                    {1, 2, 0}
                });
        assertTrue(AreMatrices.equal(expectedJE2, JE2.toMatrix(), 1e-15));
    }

    /**
     * Example 2-3-1.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     */
    @Test
    public void test_JordanExchange_0060() {
        Matrix A = new DenseMatrix(new double[][]{
                    {2, -1, -1},
                    {0, 2, -1},
                    {0, -1, 1}
                });

        JordanExchange JE1 = new JordanExchange(A, 1, 1);
        Matrix expectedJE1 = new DenseMatrix(new double[][]{
                    {0.5, 0.5, 0.5},
                    {0, 2, -1},
                    {0, -1, 1}
                });
        assertTrue(AreMatrices.equal(expectedJE1, JE1.toMatrix(), 1e-15));

        JordanExchange JE2 = new JordanExchange(JE1, 2, 2);
        Matrix expectedJE2 = new DenseMatrix(new double[][]{
                    {0.5, 0.25, 0.75},
                    {0, 0.5, 0.5},
                    {0, -0.5, 0.5}
                });
        assertTrue(AreMatrices.equal(expectedJE2, JE2.toMatrix(), 1e-15));

        JordanExchange JE3 = new JordanExchange(JE2, 3, 3);
        Matrix expectedJE3 = new DenseMatrix(new double[][]{
                    {0.5, 1, 1.5},
                    {0, 1, 1},
                    {0, 1, 2}
                });
        assertTrue(AreMatrices.equal(expectedJE3, JE3.toMatrix(), 1e-15));

        Matrix AJE3 = A.multiply(JE3.toMatrix());
        assertTrue(AreMatrices.equal(AJE3, AJE3.ONE(), 1e-15));
    }

    /**
     * Example 2-3-2.
     *
     * Linear Programming with MATLAB
     * by Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright.
     *
     * Matlab code:
     * >>format long
     * >>A=[0, 1, -3; 4, -21, 41; 1, -6,12]
     * >>jx(A,2,3)
     * 0.29268292682927  -0.53658536585366  -0.07317073170732
     * -0.09756097560976   0.51219512195122   0.02439024390244
     * -0.17073170731707   0.14634146341463   0.29268292682927
     * 
     */
    @Test
    public void test_JordanExchange_0070() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 1, 3},
                    {4, 3, 2},
                    {1, 6, 6}
                });

        JordanExchange JE1 = new JordanExchange(A, 3, 1);
        Matrix expectedJE1 = new DenseMatrix(new double[][]{
                    {0, 1, 3},
                    {4, -21, -22},
                    {1, -6, -6}
                });
        assertTrue(AreMatrices.equal(expectedJE1, JE1.toMatrix(), 1e-15));

        JordanExchange JE2 = new JordanExchange(JE1, 1, 2);
        Matrix expectedJE2 = new DenseMatrix(new double[][]{
                    {0, 1, -3},
                    {4, -21, 41},
                    {1, -6, 12}
                });
        assertTrue(AreMatrices.equal(expectedJE2, JE2.toMatrix(), 1e-15));

        JordanExchange JE3 = new JordanExchange(JE2, 2, 3);
        Matrix expectedJE3 = new DenseMatrix(new double[][]{
                    {0.29268292682927, -0.53658536585366, -0.07317073170732},
                    {-0.09756097560976, 0.51219512195122, 0.02439024390244},
                    {-0.17073170731707, 0.14634146341463, 0.29268292682927}});
        /*{0.2927, -0.5366, -0.0732},
        {-0.0976, 0.5122, 0.0244},
        {-0.1707, 0.1463, 0.2927}*/
        assertTrue(AreMatrices.equal(expectedJE3, JE3.toMatrix(), 1e-14));//the book provides solutions up to only 4 digits
    }

    /**
     * Make sure 0 pivot throws an exception.
     */
    @Test(expected = RuntimeException.class)
    public void test_JordanExchange_0100() {
        Matrix A = new DenseMatrix(new double[][]{
                    {0, 0},
                    {0, 0}
                });
        JordanExchange JE = new JordanExchange(A, 1, 2);
    }
}
