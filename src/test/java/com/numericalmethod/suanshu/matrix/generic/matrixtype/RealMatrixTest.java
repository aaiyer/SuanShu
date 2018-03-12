/*
 * Copyright (c)
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
package com.numericalmethod.suanshu.matrix.generic.matrixtype;

import com.numericalmethod.suanshu.number.Real;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class RealMatrixTest {

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0010() {
        RealMatrix A1 = new RealMatrix(10, 10);
        assertEquals(A1, A1.ZERO());
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0020() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1"), new Real("0.2")},
                    {new Real("0.3"), new Real("0.4")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("1.1"), new Real("2.2")},
                    {new Real("3.3"), new Real("4.4")}
                });

        RealMatrix A4 = A1.add(A2);
        assertEquals(A3, A4);
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0025() {
        RealMatrix A1 = new RealMatrix(new double[][]{
                    {1, 2},
                    {3, 4}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1"), new Real("0.2")},
                    {new Real("0.3"), new Real("0.4")}
                });

        RealMatrix A3 = new RealMatrix(new double[][]{
                    {1.1, 2.2},
                    {3.3, 4.4}
                });

        RealMatrix A4 = A1.add(A2);
        assertEquals(A3, A4);
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0030() {
        RealMatrix A1 = new RealMatrix(2, 2);
        A1.set(1, 1, new Real("1"));
        A1.set(1, 2, new Real("2"));
        A1.set(2, 1, new Real("3"));
        A1.set(2, 2, new Real("4"));

        assertEquals(A1.get(1, 1), new Real("1"));
        assertEquals(A1.get(1, 2), new Real("2"));
        assertEquals(A1.get(2, 1), new Real("3"));
        assertEquals(A1.get(2, 2), new Real("4"));

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1"), new Real("0.2")},
                    {new Real("0.3"), new Real("0.4")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("1.1"), new Real("2.2")},
                    {new Real("3.3"), new Real("4.4")}
                });

        RealMatrix A4 = A1.add(A2);
        assertEquals(A3, A4);
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0040() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1"), new Real("0.2")},
                    {new Real("0.3"), new Real("0.4")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("0.9"), new Real("1.8")},
                    {new Real("2.7"), new Real("3.6")}
                });

        RealMatrix A4 = A1.minus(A2);
        assertEquals(A3, A4);
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0050() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1"), new Real("0.2")},
                    {new Real("0.3"), new Real("0.4")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("0.9"), new Real("1.8")},
                    {new Real("2.7"), new Real("3.6")}
                });

        RealMatrix A4 = A1.minus(A2);
        assertEquals(A3, A4);

        RealMatrix A5 = A1.add(A2.opposite());
        assertEquals(A3, A5);
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0060() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1"), new Real("0.2")},
                    {new Real("0.3"), new Real("0.4")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("0.7"), new Real("1")},
                    {new Real("1.5"), new Real("2.2")}
                });

        RealMatrix A4 = A1.multiply(A2);
        assertEquals(A3, A4);
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0070() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("0")},
                    {new Real("0"), new Real("1")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("1.00000"), new Real("0.00")},
                    {new Real("0.0000"), new Real("1.000")}
                });

        assertEquals(A2, A3);
        assertEquals(A2, A1.ONE());
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0080() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2"), new Real("5")},
                    {new Real("3"), new Real("4"), new Real("6")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("0"), new Real("0")},
                    {new Real("0"), new Real("1"), new Real("0")}
                });

        assertEquals(A2, A1.ONE());
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0090() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.00000"), new Real("0.00")},
                    {new Real("0.0000"), new Real("0.000")}
                });

        assertEquals(A2, A1.ZERO());
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0100() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2"), new Real("5")},
                    {new Real("3"), new Real("4"), new Real("6")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.00000"), new Real("0.00"), new Real("0")},
                    {new Real("0.0000"), new Real("0.000"), new Real("0")}
                });

        assertEquals(A2, A1.ZERO());
    }

    /**
     * Test of class RealMatrix.
     * Tall matrix.
     */
    @Test
    public void test_RealMatrix_0110() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1")},
                    {new Real("0.3")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("0.7")},
                    {new Real("1.5")}
                });

        RealMatrix A4 = A1.multiply(A2);
        assertEquals(A3, A4);
    }

    /**
     * Test of class RealMatrix.
     * Fat matrix.
     */
    @Test
    public void test_RealMatrix_0120() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")}
                });

        RealMatrix A2 = new RealMatrix(new Real[][]{
                    {new Real("0.1"), new Real("0.2")},
                    {new Real("0.3"), new Real("0.4")}
                });

        RealMatrix A3 = new RealMatrix(new Real[][]{
                    {new Real("0.7"), new Real("1")}
                });

        RealMatrix A4 = A1.multiply(A2);
        assertEquals(A3, A4);
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0130() {
        RealMatrix A1 = new RealMatrix(new double[][]{
                    {1, 2}
                });

        RealMatrix A2 = new RealMatrix(new double[][]{
                    {0.1, 0.2}
                });

        assertEquals(A2, A1.scaled(new Real(0.1)));
    }

    /**
     * Test of class RealMatrix.
     */
    @Test
    public void test_RealMatrix_0140() {
        RealMatrix A1 = new RealMatrix(new Real[][]{
                    {new Real("1"), new Real("2")},
                    {new Real("3"), new Real("4")}
                });

        RealMatrix A2 = new RealMatrix(new double[][]{
                    {7, 14},
                    {21, 28}
                });

        assertEquals(A2, A1.scaled(new Real(7)));
    }
}
