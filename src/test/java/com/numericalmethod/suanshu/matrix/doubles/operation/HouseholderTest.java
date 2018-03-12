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
package com.numericalmethod.suanshu.matrix.doubles.operation;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder.Context;
import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.vector.doubles.IsVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Haksun Li
 */
public class HouseholderTest {

    //<editor-fold defaultstate="collapsed" desc="tests of reflect method for vector, of class Householder">
    @Test
    public void test_Householder_0010() {
        Vector v4H = new DenseVector(new double[]{1, 0});//reflecting about the y-axis
        Householder instance = new Householder(v4H);

        Vector x = new DenseVector(new double[]{1, 1});
        Vector result = instance.reflect(x);
        assertEquals(new DenseVector(new double[]{-1, 1}), result);
    }

    @Test
    public void test_Householder_0020() {
        Vector v4H = new DenseVector(new double[]{0, 1});//reflecting about the x-axis
        Householder instance = new Householder(v4H);

        Vector x = new DenseVector(new double[]{1, 1});
        Vector result = instance.reflect(x);
        assertEquals(new DenseVector(new double[]{1, -1}), result);
    }

    /**
     * Test for non-unit defining vector.
     */
    @Test
    public void test_Householder_0030() {
        Vector v4H = new DenseVector(new double[]{1, 1});//reflecting about -45-degree line
        Householder instance = new Householder(v4H);

        Vector x = new DenseVector(new double[]{1, 1});
        Vector result = instance.reflect(x);
        assertArrayEquals(new double[]{-1, -1}, result.toArray(), 1e-15);
    }

    /**
     * Test for preserving norm.
     */
    @Test
    public void test_Householder_0040() {
        Vector v4H = new DenseVector(new double[]{-1, 1});//reflecting about 45-degree line
        Householder instance = new Householder(v4H);

        Vector x = new DenseVector(new double[]{0, 10});
        Vector result = instance.reflect(x);
        assertArrayEquals(new double[]{10, 0}, result.toArray(), 1e-14);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tests of H, reflect method for matrix, of class Householder">
    @Test
    public void test_Householder_0050() {
        Vector v4H = new DenseVector(new double[]{2, 3});
        Householder instance = new Householder(v4H);

        Matrix x = new DenseMatrix(new double[]{5, 6}, 2, 1);
        Matrix result = instance.reflect(x);

        Matrix expected = instance.H().multiply(x);
        assertTrue(AreMatrices.equal(expected, result, 1e-14));
    }

    @Test
    public void test_Householder_0060() {
        Vector v4H = new DenseVector(new double[]{2, 3});
        Householder instance = new Householder(v4H);

        Matrix x = new DenseMatrix(new double[]{5, 7, 6, 8}, 2, 2);
        Matrix result = instance.reflect(x);

        Matrix expected = instance.H().multiply(x);
        assertTrue(AreMatrices.equal(expected, result, 1e-14));
    }

    @Test
    public void test_Householder_0070() {
        Vector v4H = new DenseVector(new double[]{2, 3});
        Householder instance = new Householder(v4H);

        Matrix x = new DenseMatrix(new double[]{5, 7, 6, 8}, 2, 2);

        Matrix result1 = instance.reflect(x);
        Matrix expected1 = instance.H().multiply(x);
        assertTrue(AreMatrices.equal(expected1, result1, 1e-14));

        Matrix result2 = instance.reflectRows(x);
        Matrix expected2 = x.multiply(instance.H());
        assertTrue(AreMatrices.equal(expected2, result2, 1e-14));
    }

    @Test
    public void test_Householder_0080() {
        Vector v4H = new DenseVector(new double[]{1, 2, 3, 4, 5});
        Householder instance = new Householder(v4H);

        Matrix x = new DenseMatrix(R.seq(1, 50, 1d), 5, 10);
        Matrix result = instance.reflect(x);

        Matrix expected = instance.H().multiply(x);
        assertTrue(AreMatrices.equal(expected, result, 1e-13));
    }

    @Test
    public void test_Householder_0090() {
        Vector v4H = new DenseVector(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Householder instance = new Householder(v4H);

        Matrix x = new DenseMatrix(R.seq(1, 81, 1d), 9, 9);

        Matrix result1 = instance.reflect(x);
        Matrix expected1 = instance.H().multiply(x);
        assertTrue(AreMatrices.equal(expected1, result1, 1e-13));

        Matrix result2 = instance.reflectRows(x);
        Matrix expected2 = x.multiply(instance.H());
        assertTrue(AreMatrices.equal(expected2, result2, 1e-13));
    }

    @Test
    public void test_Householder_0100() {
        Vector v4H = new DenseVector(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
        Householder instance = new Householder(v4H);

        Matrix x = new DenseMatrix(R.seq(1.5, 50.5, 1d), 5, 10);
        Matrix result = instance.reflect(x);

        Matrix expected = instance.H().multiply(x);
        assertTrue(AreMatrices.equal(expected, result, 1e-13));
    }
    //</editor-fold>

    /**
     * Test of zero defining vector, of class Householder.
     */
    @Test
    public void test_Householder_0110() {
        Vector v4H = new DenseVector(new double[]{0, 0});//zero vector
        Householder instance = new Householder(v4H);

        assertTrue(IsVector.equal(instance.generator(), v4H, 1e-15));
        assertEquals(instance.H(), instance.H().ONE());
    }

    //<editor-fold defaultstate="collapsed" desc="tests of Context, of class Householder">
    @Test
    public void test_Householder_0120() {
        Vector x = new DenseVector(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
        Context defn = Householder.getContext(x);
        Householder instance = new Householder(defn.generator);

        Vector expected = new DenseVector(new double[]{-x.norm(), 0, 0, 0, 0});
        Vector result = instance.reflect(x);
        assertEquals(expected, result);
    }

    @Test
    public void test_Householder_0130() {
        Vector x = new DenseVector(new double[]{1.1e-100, 2.2e-100, 3.3e-100, 4.4e-100, 5.5e-100});
        Context defn = Householder.getContext(x);
        Householder instance = new Householder(defn.generator);

        Vector expected = new DenseVector(new double[]{-x.norm(), 0, 0, 0, 0});
        Vector result = instance.reflect(x);
        assertArrayEquals(expected.toArray(), result.toArray(), 1e-114);
    }
    //</editor-fold>
}
