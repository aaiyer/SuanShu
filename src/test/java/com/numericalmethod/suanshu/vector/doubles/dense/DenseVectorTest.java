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
package com.numericalmethod.suanshu.vector.doubles.dense;

import com.numericalmethod.suanshu.vector.doubles.IsVector.VectorAccessException;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class DenseVectorTest {

    //<editor-fold defaultstate="collapsed" desc="tests for Ctors">
    @Test
    public void testCtor_0010() {
        double[] arr1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Matrix A1 = new DenseMatrix(arr1, 10, 1);
        Vector v1 = new DenseVector(A1);
        Vector v2 = new DenseVector(arr1);
        assertEquals(v1, v2);
    }

    @Test
    public void testCtor_0020() {
        double[] arr1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Matrix A1 = new DenseMatrix(arr1, 1, 10);
        Vector v1 = new DenseVector(A1);
        Vector v2 = new DenseVector(arr1);
        assertEquals(v1, v2);
    }

    @Test
    public void testCtor_0030() {
        double[] arr1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Vector v1 = new DenseVector(arr1);
        Vector v2 = new DenseVector(v1);//independent copy
        assertEquals(v1, v2);

        v2.set(1, 99);//v1 does not change
        assertFalse(v1.equals(v2));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for set">
    /**
     * Test of set method, of class Vector.
     */
    @Test
    public void testReplace_001() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        DenseVector v2 = new DenseVector(new double[]{11, 12, 13});
        v1.set(1, v2);
        assertEquals(new DenseVector(new double[]{11, 12, 13, 4, 5, 6, 7, 8, 9, 10}), v1);

        DenseVector v3 = new DenseVector(new double[]{16, 17});
        v1.set(6, v3);
        assertEquals(new DenseVector(new double[]{11, 12, 13, 4, 5, 16, 17, 8, 9, 10}), v1);

        DenseVector v4 = new DenseVector(new double[]{19, 20});
        v1.set(9, v4);
        assertEquals(new DenseVector(new double[]{11, 12, 13, 4, 5, 16, 17, 8, 19, 20}), v1);
    }

    /**
     * Test of set method, of class Vector.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReplace_002() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        DenseVector v2 = new DenseVector(new double[]{11, 12, 13});
        v1.set(1, v2);
        assertEquals(new DenseVector(new double[]{11, 12, 13, 4, 5, 6, 7, 8, 9, 10}), v1);

        DenseVector v3 = new DenseVector(new double[]{19, 20, 21});
        v1.set(9, v3);
    }

    /**
     * Test of set method, of class Vector.
     */
    @Test(expected = VectorAccessException.class)
    public void testReplace_003() {
        DenseVector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        DenseVector v2 = new DenseVector(new double[]{0, 1, 2,});
        v1.set(0, v2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for the norm operation">
    /**
     * Test of norm method, of class VectorOps.
     */
    @Test
    public void test_norm_0010() {
        Vector v = new DenseVector(1., 2., 3.);
        int p = 2;

        double result = v.norm(p);
        assertEquals(3.74165738677394, result, 1e-14);

        result = v.norm(Integer.MAX_VALUE);
        assertEquals(3, result, 1e-14);

        result = v.norm(Integer.MIN_VALUE);
        assertEquals(1, result, 1e-14);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for the equal method">
    @Test
    public void test_equal_0010() {
        Vector v1 = new DenseVector(1., 2., 3.);
        Vector v2 = new DenseVector(1 + 1e-20, 2, 3);
        assertArrayEquals(v1.toArray(), v2.toArray(), 1e-15);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests for "arithmetics" operations">
    @Test
    public void test_ArithmeticsMethods_0010() {
        Vector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        Vector v2 = v1.minus(1);
        Vector v3 = new DenseVector(new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertEquals(v3, v2);

        Vector v4 = v1.minus(0);
        assertEquals(v1, v4);

        Vector v5 = v1.add(1);
        Vector v6 = new DenseVector(new double[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
        assertEquals(v6, v5);

        Vector v7 = v1.add(0);
        assertEquals(v1, v7);

        Vector v8 = v1.scaled(2);
        Vector v9 = new DenseVector(new double[]{2, 4, 6, 8, 10, 12, 14, 16, 18, 20});
        assertEquals(v9, v8);
    }

    @Test
    public void test_ArithmeticsMethods_0020() {
        Vector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        Vector v2 = v1.scaled(2);

        Vector v3 = v2.divide(v1);
        Vector v4 = new DenseVector(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        assertEquals(v4, v3);
    }

    @Test
    public void test_ArithmeticsMethods_0030() {
        Vector v1 = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        Vector v2 = v1.pow(2);

        Vector v3 = new DenseVector(new double[]{1, 4, 9, 16, 25, 36, 49, 64, 81, 100});
        assertEquals(v3, v2);
    }
    //</editor-fold>
}
