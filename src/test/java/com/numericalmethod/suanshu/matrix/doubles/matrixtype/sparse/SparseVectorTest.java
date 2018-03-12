
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
package com.numericalmethod.suanshu.matrix.doubles.matrixtype.sparse;

import com.numericalmethod.suanshu.vector.doubles.IsVector.SizeMismatch;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ken Yiu
 */
public class SparseVectorTest {

    @Test
    public void test_construction_0010() {
        int size = 99;
        SparseVector result = new SparseVector(size);
        assertEquals(size, result.size());
        assertEquals(0, result.nNonZeros());
    }

    @Test
    public void test_construction_0020() {
        int size = 99;
        SparseVector result = new SparseVector(
                size,
                new int[]{1, 3, 53, 79, 99},
                new double[]{11, 22, 33, 44, 55});

        assertEquals(size, result.size());
        assertEquals(5, result.nNonZeros());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_construction_0030() {
        int size = 99;
        SparseVector result = new SparseVector(
                size,
                new int[]{0, 3, 53, 79, 99}, // index = 0
                new double[]{11, 22, 33, 44, 55});
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_construction_0040() {
        int size = 99;
        SparseVector result = new SparseVector(
                size,
                new int[]{1, 3, -53, 79, 99}, // index = -53
                new double[]{11, 22, 33, 44, 55});
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_construction_0050() {
        int size = 99;
        SparseVector result = new SparseVector(
                size,
                new int[]{1, 3, 53, 79, 109}, // index = 109
                new double[]{11, 22, 33, 44, 55});
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_0060() {
        int size = 99;
        SparseVector result = new SparseVector(
                size,
                new int[]{1, 3, 53, 79, 99, 2}, // one more index
                new double[]{11, 22, 33, 44, 55});
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_construction_0070() {
        int size = 99;
        SparseVector result = new SparseVector(
                size,
                new int[]{1, 3, 53, 79, 79}, // duplicate indices
                new double[]{11, 22, 33, 44, 55});
    }

    @Test
    public void test_construction_0080() {
        int size = 10;
        int[] indices = new int[]{7, 1, 9};
        double[] values = new double[]{11, 22, 33};
        SparseVector vector = new SparseVector(
                size,
                indices,
                values);

        double[] expected = new double[]{
            22, 0, 0, 0, 0, 0, 11, 0, 33, 0
        };

        SparseVector result = new SparseVector(vector); // copy constructor

        assertEquals(3, result.nNonZeros());
        for (int i = 0; i < size; ++i) {
            assertEquals(expected[i], result.get(i + 1), 1e-15);
        }

        // changes to the original
        vector.set(1, 0);
        vector.set(7, 0);
        vector.set(9, 0);

        assertEquals(3, result.nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_get_0010() {
        int size = 10;
        int[] indices = new int[]{7, 1, 9};
        double[] values = new double[]{11, 22, 33};
        SparseVector result = new SparseVector(
                size,
                indices,
                values);

        double[] expected = new double[]{
            22, 0, 0, 0, 0, 0, 11, 0, 33, 0
        };

        assertEquals(3, result.nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_set_0010() {
        int size = 10;
        SparseVector result = new SparseVector(size);

        result.set(5, 11);
        result.set(2, 7);
        result.set(10, -13);

        double[] expected = new double[]{
            0, 7, 0, 0, 11, 0, 0, 0, 0, -13
        };

        assertEquals(3, result.nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_set_0020() {
        int size = 10;
        SparseVector result = new SparseVector(size);

        result.set(10, -13);
        result.set(3, 0); // set zero to non-existing element will be ignored
        result.set(2, 7);
        result.set(5, 11);

        double[] expected = new double[]{
            0, 7, 0, 0, 11, 0, 0, 0, 0, -13
        };

        assertEquals(3, result.nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_set_0030() {
        int size = 10;
        SparseVector result = new SparseVector(size);

        result.set(10, -13);
        result.set(2, 7);
        result.set(5, 11);
        result.set(2, 0); // set zero to existing element will remove the element

        double[] expected = new double[]{
            0, 0, 0, 0, 11, 0, 0, 0, 0, -13
        };

        assertEquals(2, result.nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_iterator_0010() {
        int size = 10;
        SparseVector result = new SparseVector(size);

        result.set(5, 11);
        result.set(2, 7);
        result.set(10, -13);

        SparseVector.Entry[] expected = new SparseVector.Entry[]{
            new SparseVector.Entry(2, 7),
            new SparseVector.Entry(5, 11),
            new SparseVector.Entry(10, -13)
        };

        int i = 0;
        for (SparseVector.Entry entry : result) {
            assertEquals(expected[i].index(), entry.index());
            assertEquals(expected[i].value(), entry.value(), 1e-15);
            i++;
        }
    }

    @Test
    public void test_add_0010() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{7, 3, 9},
                new double[]{44, 33, 99});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{1, 7, 5, 3},
                new double[]{11, 33, 55, -33});
        Vector result = v1.add(v2);

        double[] expected = new double[]{
            11, 0, 0, 0, 55, 0, 77, 0, 99, 0
        };

        assertThat(result, CoreMatchers.is(SparseStructure.class));
        assertEquals(4, ((SparseStructure) result).nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_add_0020() {
        int size = 4;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 2},
                new double[]{1, 2});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{1, 2, 4},
                new double[]{-1, 2, 1});
        Vector result = v1.add(v2);

        double[] expected = new double[]{
            0, 4, 0, 1
        };

        assertThat(result, CoreMatchers.is(SparseStructure.class));
        assertEquals(2, ((SparseStructure) result).nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_add_0030() {
        int size = 4;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{2, 4},
                new double[]{1, -2});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{2, 4},
                new double[]{-1, 2});
        Vector result = v1.add(v2);

        double[] expected = new double[]{
            0, 0, 0, 0
        };

        assertThat(result, CoreMatchers.is(SparseStructure.class));
        assertEquals(0, ((SparseStructure) result).nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_add_0040() {
        int size = 4;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{2, 4},
                new double[]{1, -2});
        DenseVector v2 = new DenseVector(
                new double[]{-1, 2, -3, 4});
        Vector result = v1.add(v2);

        double[] expected = new double[]{
            -1, 3, -3, 2
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test(expected = SizeMismatch.class) // size mismatch
    public void test_add_0100() {
        SparseVector v1 = new SparseVector(
                4, // size = 4
                new int[]{2, 4},
                new double[]{1, -2});
        SparseVector v2 = new SparseVector(
                10, // size = 10
                new int[]{2, 4},
                new double[]{-1, 2});
        Vector result = v1.add(v2);
    }

    @Test
    public void test_minus_0010() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{7, 3, 9},
                new double[]{-44, 33, 99});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{1, 7, 5, 3},
                new double[]{11, 33, -55, 33});
        Vector result = v1.minus(v2);

        double[] expected = new double[]{
            -11, 0, 0, 0, 55, 0, -77, 0, 99, 0
        };

        assertThat(result, CoreMatchers.is(SparseStructure.class));
        assertEquals(4, ((SparseStructure) result).nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_minus_0020() {
        int size = 4;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 2},
                new double[]{11, 22});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{1, 2, 4},
                new double[]{11, 22, 44});
        Vector result = v1.minus(v2);

        double[] expected = new double[]{
            0, 0, 0, -44
        };

        assertThat(result, CoreMatchers.is(SparseStructure.class));
        assertEquals(1, ((SparseStructure) result).nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_minus_0030() {
        int size = 4;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{2, 4},
                new double[]{22, 44});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{2, 4},
                new double[]{22, 44});
        Vector result = v1.minus(v2);

        double[] expected = new double[]{
            0, 0, 0, 0
        };

        assertThat(result, CoreMatchers.is(SparseStructure.class));
        assertEquals(0, ((SparseStructure) result).nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_minus_0040() {
        int size = 4;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{2, 4},
                new double[]{22, 44});
        DenseVector v2 = new DenseVector(
                new double[]{11, 22, -33, 44});
        Vector result = v1.minus(v2);

        double[] expected = new double[]{
            -11, 0, 33, 0
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test(expected = SizeMismatch.class) // size mismatch
    public void test_minus_0100() {
        SparseVector v1 = new SparseVector(
                4, // size = 4
                new int[]{2, 4},
                new double[]{1, -2});
        SparseVector v2 = new SparseVector(
                10, // size = 10
                new int[]{2, 4},
                new double[]{-1, 2});
        Vector result = v1.minus(v2);
    }

    @Test
    public void test_multiply_0010() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{2, 5, 7, 8},
                new double[]{22, 55, 77, 88});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{1, 3, 5, 6, 7},
                new double[]{11, 33, 55, 66, -77});
        SparseVector result = v1.multiply(v2);

        double[] expected = new double[]{
            0, 0, 0, 0, 3025, 0, -5929, 0, 0, 0
        };

        assertEquals(2, result.nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_multiply_0020() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 3, 7},
                new double[]{11, 33, 77});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{8, 10},
                new double[]{88, 110});
        SparseVector result = v1.multiply(v2);

        assertEquals(0, result.nNonZeros());
    }

    @Test
    public void test_multiply_0030() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{2, 5, 7, 8},
                new double[]{22, 55, 77, 88});
        DenseVector v2 = new DenseVector(
                new double[]{11, 22, 33, 44, 55, 66, -77, 88, 99, 110});
        Vector result = v1.multiply(v2);

        double[] expected = new double[]{
            0, 484, 0, 0, 3025, 0, -5929, 7744, 0, 0
        };

        assertThat(result, CoreMatchers.is(SparseStructure.class));
        assertEquals(4, ((SparseStructure) result).nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test(expected = SizeMismatch.class) // size mismatch
    public void test_multiply_0100() {
        SparseVector v1 = new SparseVector(
                4, // size = 4
                new int[]{2, 4},
                new double[]{1, -2});
        SparseVector v2 = new SparseVector(
                10, // size = 10
                new int[]{2, 4},
                new double[]{-1, 2});
        SparseVector result = v1.multiply(v2);
    }

    @Test
    public void test_divide_0010() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 3, 7},
                new double[]{11, 33, 77});
        DenseVector v2 = new DenseVector(
                new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Vector result = v1.divide(v2);

        assertArrayEquals(v1.toArray(), result.toArray(), 1e-15);
    }

    @Test
    public void test_addScalar_0010() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 3, 7},
                new double[]{11, 33, 77});
        double scalar = -11;
        Vector result = v1.add(scalar);

        double[] expected = new double[]{
            0, -11, 22, -11, -11, -11, 66, -11, -11, -11
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_minusScalar_0010() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 3, 7},
                new double[]{11, 33, 77});
        double scalar = 33;
        Vector result = v1.minus(scalar);

        double[] expected = new double[]{
            -22, -33, 0, -33, -33, -33, 44, -33, -33, -33
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_innerProduct_0010() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{2, 5, 7, 8},
                new double[]{22, 55, 77, 88});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{1, 3, 5, 6, 7},
                new double[]{11, 33, 55, 66, -77});
        double result = v1.innerProduct(v2);

        assertEquals(-2904, result, 1e-15);
    }

    @Test
    public void test_innerProduct_0020() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 3, 7},
                new double[]{11, 33, 77});
        SparseVector v2 = new SparseVector(
                size,
                new int[]{8, 10},
                new double[]{88, 110});
        double result = v1.innerProduct(v2);

        assertEquals(0, result, 1e-15);
    }

    @Test
    public void test_innerProduct_0030() {
        int size = 10;
        SparseVector v1 = new SparseVector(
                size,
                new int[]{1, 3, 7},
                new double[]{11, 33, 77});
        DenseVector v2 = new DenseVector(
                new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        double result = v1.innerProduct(v2);

        assertEquals(649, result, 1e-15);
    }

    @Test(expected = SizeMismatch.class) // size mismatch
    public void test_innerProduct_0100() {
        SparseVector v1 = new SparseVector(
                4, // size = 4
                new int[]{2, 4},
                new double[]{1, -2});
        SparseVector v2 = new SparseVector(
                10, // size = 10
                new int[]{2, 4},
                new double[]{-1, 2});
        double result = v1.innerProduct(v2);
    }

    @Test
    public void test_scaled_0010() {
        int size = 10;
        SparseVector v = new SparseVector(
                size,
                new int[]{2, 4, 8, 9},
                new double[]{22, 44, 88, 99});
        double scalar = 1. / 11.;
        Vector result = v.scaled(scalar);

        double[] expected = new double[]{
            0, 2, 0, 4, 0, 0, 0, 8, 9, 0
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_scaled_0020() {
        int size = 10;
        SparseVector v = new SparseVector(
                size,
                new int[]{2, 4, 8, 9},
                new double[]{22, 44, 88, 99});
        double scalar = 0;
        SparseVector result = v.scaled(scalar);

        assertEquals(0, result.nNonZeros());
    }

    @Test
    public void test_opposite_0010() {
        int size = 10;
        SparseVector v = new SparseVector(
                size,
                new int[]{2, 4, 8, 9},
                new double[]{22, -44, -88, 99});
        Vector result = v.opposite();

        double[] expected = new double[]{
            0, -22, 0, 44, 0, 0, 0, 88, -99, 0
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_opposite_0020() {
        int size = 10;
        SparseVector v = new SparseVector(size);
        SparseVector result = v.opposite();

        assertEquals(0, result.nNonZeros());
    }

    @Test
    public void test_pow_0010() {
        int size = 10;
        SparseVector v = new SparseVector(
                size,
                new int[]{1, 5, 8},
                new double[]{11, 55, 88});
        double scalar = 3;
        SparseVector result = (SparseVector) v.pow(scalar);

        double[] expected = new double[]{
            1331, 0, 0, 0, 166375, 0, 0, 681472, 0, 0
        };

        assertEquals(3, result.nNonZeros());
        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_pow_0020() {
        int size = 10;
        SparseVector v = new SparseVector(
                size,
                new int[]{1, 5, 8},
                new double[]{11, 55, 88});
        double scalar = 0;
        Vector result = v.pow(scalar);

        double[] expected = new double[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1
        };

        assertArrayEquals(expected, result.toArray(), 1e-15);
    }

    @Test
    public void test_norm_0010() {
        SparseVector v = new SparseVector(
                10,
                new int[]{3, 5, 9},
                new double[]{33, 55, 99});
        double result = v.norm();
        double expected = 117.9618582423996913529557563937;

        assertEquals(expected, result, 1e-15);
    }

    @Test
    public void test_norm_int_0010() {
        SparseVector v = new SparseVector(
                10,
                new int[]{3, 5, 9},
                new double[]{33, 55, 99});
        double result = v.norm(5);
        double expected = 100.10442222613085733890923880148;

        assertEquals(expected, result, 1e-13);
    }

    @Test
    public void test_angle_0010() {
        SparseVector v1 = new SparseVector(
                2,
                new int[]{1},
                new double[]{10});
        SparseVector v2 = new SparseVector(
                2,
                new int[]{2},
                new double[]{0.5});
        double result = v1.angle(v2);
        double expected = Math.PI / 2; // perpendicular vectors

        assertEquals(expected, result, 1e-15);
    }

    @Test
    public void test_angle_0020() {
        SparseVector v1 = new SparseVector(
                2,
                new int[]{1},
                new double[]{10});
        SparseVector v2 = new SparseVector(
                2,
                new int[]{1},
                new double[]{0.5});
        double result = v1.angle(v2);
        double expected = 0.; // parallel vectors

        assertEquals(expected, result, 1e-15);
    }

    @Test
    public void test_angle_0030() {
        SparseVector v1 = new SparseVector(
                2,
                new int[]{1},
                new double[]{10});
        SparseVector v2 = new SparseVector(
                2,
                new int[]{1},
                new double[]{-0.5});
        double result = v1.angle(v2);
        double expected = Math.PI; // parallel vectors but opposite direction

        assertEquals(expected, result, 1e-15);
    }

    @Test
    public void test_angle_0040() {
        SparseVector v1 = new SparseVector(
                2,
                new int[]{1},
                new double[]{10});
        SparseVector v2 = new SparseVector(
                2,
                new int[]{1, 2},
                new double[]{0.5, 0.5});
        double result = v1.angle(v2);
        double expected = Math.PI / 4; // 45 degree

        assertEquals(expected, result, 1e-15);
    }

    @Test
    public void test_angle_0050() {
        SparseVector v1 = new SparseVector(
                2,
                new int[]{1},
                new double[]{10});
        SparseVector v2 = new SparseVector(
                2,
                new int[]{1, 2},
                new double[]{-0.5, -0.5});
        double result = v1.angle(v2);
        double expected = 3 * Math.PI / 4; // 135 degree

        assertEquals(expected, result, 1e-15);
    }

//    @Test
//    public void test_dropTolerance_0010() {
//        SparseVector v1 = new SparseVector(
//                10,
//                new int[]{1, 3, 5, 6, 9, 10},
//                new double[]{0.000001, 0.3, 0.5, 0.000006, -0.000009, 0.00001});
//
//        double tolerance = 1e-5;
//        int result = v1.dropTolerance(tolerance);
//
//        double[] expected = new double[]{
//            0, 0, 0.3, 0, 0.5, 0, 0, 0, 0, 0
//        };
//
//        assertEquals(4, result);
//        assertEquals(2, v1.nNonZeros());
//        assertArrayEquals(expected, v1.toArray(), 1e-15);
//    }

    @Test
    public void test_toArray_0010() {
        SparseVector v = new SparseVector(10);
        double[] expected = new double[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        assertArrayEquals(expected, v.toArray(), 1e-15);
    }

    @Test
    public void test_toArray_0020() {
        SparseVector v = new SparseVector(
                10,
                new int[]{3, 5, 9},
                new double[]{33, 55, 99});
        double[] expected = new double[]{
            0, 0, 33, 0, 55, 0, 0, 0, 99, 0
        };

        assertArrayEquals(expected, v.toArray(), 1e-15);
    }
}
