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
package com.numericalmethod.suanshu.vector.doubles.operation;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.Constant;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static com.numericalmethod.suanshu.number.DoubleUtils.equal;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class VectorSpaceTest {

    /**
     * Test of basis and getComplement methods, of class VectorSpace.
     *
     * The assert answers for basis and getComplement are copied from the debugger.
     * So, it is not so much a test.
     * The purpose is to catch any code change in the future that gives answers very different from these.
     */
    @Test
    public void test_basis_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        VectorSpace vs = new VectorSpace(A1, 1e-14);

        List<Vector> basis = vs.getBasis();

        assertEquals(2, basis.size());
        assertTrue(basis.contains(new DenseVector(new double[]{
                    -0.034199278402838214, -0.20519567041703082, -0.3761920624312232, -0.5471884544454155, -0.7181848464596079
                })));
        assertTrue(basis.contains(new DenseVector(new double[]{
                    -0.7738413334506781, -0.5078333750770092, -0.2418254167033382, 0.02418254167033419, 0.29019050004400515
                })));

        List<Vector> complement = vs.getComplement();
        assertEquals(3, complement.size());
        assertTrue(complement.contains(new DenseVector(new double[]{
                    -0.3595391142537903, -0.031675301369154064, 0.8680554446241512, -0.20292852812568576, -0.2739125008755226
                })));
        assertTrue(complement.contains(new DenseVector(new double[]{
                    -0.36511993864944847, 0.36117369987466635, -0.14551171230890098, 0.6679820795915937, -0.5185241285079113
                })));
        assertTrue(complement.contains(new DenseVector(new double[]{
                    -0.3707007630451072, 0.7540227011184854, -0.1590788692419533, -0.4611073126911269, 0.23686424385970062
                })));
    }

    /**
     * Test of basis and getComplement methods, of class VectorSpace.
     *
     * The assert answers for basis and getComplement are copied from the debugger.
     * So, it is not so much a test.
     * The purpose is to catch any code change in the future that gives answers very different from these.
     */
    @Test
    public void test_basis_0015() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        VectorSpace vs = new VectorSpace(A1);

        List<Vector> basis = vs.getBasis();

        assertEquals(2, basis.size());
        assertTrue(basis.contains(new DenseVector(new double[]{
                    -0.034199278402838214, -0.20519567041703082, -0.3761920624312232, -0.5471884544454155, -0.7181848464596079
                })));
        assertTrue(basis.contains(new DenseVector(new double[]{
                    -0.7738413334506781, -0.5078333750770092, -0.2418254167033382, 0.02418254167033419, 0.29019050004400515
                })));

        List<Vector> complement = vs.getComplement();
        assertEquals(3, complement.size());
        assertTrue(complement.contains(new DenseVector(new double[]{
                    -0.3595391142537903, -0.031675301369154064, 0.8680554446241512, -0.20292852812568576, -0.2739125008755226
                })));
        assertTrue(complement.contains(new DenseVector(new double[]{
                    -0.36511993864944847, 0.36117369987466635, -0.14551171230890098, 0.6679820795915937, -0.5185241285079113
                })));
        assertTrue(complement.contains(new DenseVector(new double[]{
                    -0.3707007630451072, 0.7540227011184854, -0.1590788692419533, -0.4611073126911269, 0.23686424385970062
                })));
    }

    @Test
    public void test_isSpanned_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        VectorSpace vs = new VectorSpace(A1, 1e-14);

        List<Vector> basis = vs.getBasis();
        Vector b1 = basis.get(0);
        Vector b2 = basis.get(1);

        Vector v1 = b1.scaled(1d).add(b2.scaled(2.5));//v1 = b1 + 2.5*b2
        Vector coeff1 = vs.getSpanningCoefficients(v1);
        assertTrue(equal(new double[]{1.0, 2.5}, coeff1.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v1));

        Vector v2 = b1.scaled(1.23456).add(b2.scaled(2.4689));//v2 = 1.23456*b1 + 2.4689*b2
        Vector coeff2 = vs.getSpanningCoefficients(v2);
        assertTrue(equal(new double[]{1.23456, 2.4689}, coeff2.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v2));

        Vector v3 = b2.scaled(7.23);//v3 = 7.23*b2
        Vector coeff3 = vs.getSpanningCoefficients(v3);
        assertTrue(equal(new double[]{0, 7.23}, coeff3.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v3));

        Vector v4 = b2.scaled(0);//v4 = 0
        Vector coeff4 = vs.getSpanningCoefficients(v4);
        assertTrue(equal(new double[]{0, 0}, coeff4.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v4));

        Vector v5 = new DenseVector(new double[]{123, 456});
        Vector coeff5 = vs.getSpanningCoefficients(v5);
        assertNull(coeff5);
        assertFalse(vs.isSpanned(v5));
    }

    @Test
    public void test_isSpanned_0015() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        VectorSpace vs = new VectorSpace(A1);

        List<Vector> basis = vs.getBasis();
        Vector b1 = basis.get(0);
        Vector b2 = basis.get(1);

        Vector v1 = b1.scaled(1d).add(b2.scaled(2.5));//v1 = b1 + 2.5*b2
        Vector coeff1 = vs.getSpanningCoefficients(v1);
        assertTrue(equal(new double[]{1.0, 2.5}, coeff1.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v1));

        Vector v2 = b1.scaled(1.23456).add(b2.scaled(2.4689));//v2 = 1.23456*b1 + 2.4689*b2
        Vector coeff2 = vs.getSpanningCoefficients(v2);
        assertTrue(equal(new double[]{1.23456, 2.4689}, coeff2.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v2));

        Vector v3 = b2.scaled(7.23);//v3 = 7.23*b2
        Vector coeff3 = vs.getSpanningCoefficients(v3);
        assertTrue(equal(new double[]{0, 7.23}, coeff3.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v3));

        Vector v4 = b2.scaled(0);//v4 = 0
        Vector coeff4 = vs.getSpanningCoefficients(v4);
        assertTrue(equal(new double[]{0, 0}, coeff4.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v4));

        Vector v5 = new DenseVector(new double[]{123, 456});
        Vector coeff5 = vs.getSpanningCoefficients(v5);
        assertNull(coeff5);
        assertFalse(vs.isSpanned(v5));
    }

    @Test
    public void test_isSpanned_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        VectorSpace vs = new VectorSpace(A1, Math.sqrt(Constant.EPSILON));//1e-8

        List<Vector> basis = vs.getBasis();
        Vector b1 = basis.get(0);
        Vector b2 = basis.get(1);

        Vector v1 = b1.scaled(1d).add(b2.scaled(2.5));//v1 = b1 + 2.5*b2
        Vector coeff1 = vs.getSpanningCoefficients(v1);
        assertTrue(equal(new double[]{1.0, 2.5}, coeff1.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v1));

        Vector v2 = b1.scaled(1.23456).add(b2.scaled(2.4689));//v2 = 1.23456*b1 + 2.4689*b2
        Vector coeff2 = vs.getSpanningCoefficients(v2);
        assertTrue(equal(new double[]{1.23456, 2.4689}, coeff2.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v2));

        Vector v3 = b2.scaled(7.23);//v3 = 7.23*b2
        Vector coeff3 = vs.getSpanningCoefficients(v3);
        assertTrue(equal(new double[]{0, 7.23}, coeff3.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v3));

        Vector v4 = b2.scaled(0);//v4 = 0
        Vector coeff4 = vs.getSpanningCoefficients(v4);
        assertTrue(equal(new double[]{0, 0}, coeff4.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v4));

        Vector v5 = new DenseVector(new double[]{123, 456});
        Vector coeff5 = vs.getSpanningCoefficients(v5);
        assertNull(coeff5);
        assertFalse(vs.isSpanned(v5));
    }

    @Test
    public void test_isSpanned_0025() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 2, 3},
                    {6, 7, 8},
                    {11, 12, 13},
                    {16, 17, 18},
                    {21, 22, 23}});

        VectorSpace vs = new VectorSpace(A1);

        List<Vector> basis = vs.getBasis();
        Vector b1 = basis.get(0);
        Vector b2 = basis.get(1);

        Vector v1 = b1.scaled(1d).add(b2.scaled(2.5));//v1 = b1 + 2.5*b2
        Vector coeff1 = vs.getSpanningCoefficients(v1);
        assertTrue(equal(new double[]{1.0, 2.5}, coeff1.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v1));

        Vector v2 = b1.scaled(1.23456).add(b2.scaled(2.4689));//v2 = 1.23456*b1 + 2.4689*b2
        Vector coeff2 = vs.getSpanningCoefficients(v2);
        assertTrue(equal(new double[]{1.23456, 2.4689}, coeff2.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v2));

        Vector v3 = b2.scaled(7.23);//v3 = 7.23*b2
        Vector coeff3 = vs.getSpanningCoefficients(v3);
        assertTrue(equal(new double[]{0, 7.23}, coeff3.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v3));

        Vector v4 = b2.scaled(0);//v4 = 0
        Vector coeff4 = vs.getSpanningCoefficients(v4);
        assertTrue(equal(new double[]{0, 0}, coeff4.toArray(), 1e-14));
        assertTrue(vs.isSpanned(v4));

        Vector v5 = new DenseVector(new double[]{123, 456});
        Vector coeff5 = vs.getSpanningCoefficients(v5);
        assertNull(coeff5);
        assertFalse(vs.isSpanned(v5));
    }
}
