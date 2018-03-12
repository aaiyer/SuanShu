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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.analysis.function.polynomial.Polynomial;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.number.NumberUtils;
import com.numericalmethod.suanshu.test.NumberAssert;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class CharacteristicPolynomialTest {

    @Test
    public void test_CharacteristicPolynomial_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });
        CharacteristicPolynomial instance = new CharacteristicPolynomial(A1);

        Polynomial expResult = new Polynomial(new double[]{1, -6, -3});//x^2 -6x -3
        Polynomial result = instance.getCharacteristicPolynomial();
        assertEquals(expResult, result);
    }

    @Test
    public void test_CharacteristicPolynomial_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {3}
                });
        CharacteristicPolynomial instance = new CharacteristicPolynomial(A1);

        Polynomial expResult = new Polynomial(new double[]{1, -3});//x - 3
        Polynomial result = instance.getCharacteristicPolynomial();
        assertEquals(expResult, result);
    }

    @Test
    public void test_CharacteristicPolynomial_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {19, 3},
                    {-2, 26}
                });
        CharacteristicPolynomial instance = new CharacteristicPolynomial(A1);

        Polynomial expResult = new Polynomial(new double[]{1, -45, 500});//x^2 - 45x +500
        Polynomial result = instance.getCharacteristicPolynomial();
        assertEquals(expResult, result);
    }

    @Test
    public void test_getEigenvalues_0010() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1, 4},
                    {2, 5}
                });
        CharacteristicPolynomial instance = new CharacteristicPolynomial(A1);
        List<Number> result = instance.getEigenvalues();
        NumberAssert.assertSameList(Arrays.asList(6.464101615137754, -0.464101615137755), result, 1e-10);
    }

    @Test
    public void test_getEigenvalues_0020() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1.234, 61.972},
                    {3665, 12.361}
                });
        CharacteristicPolynomial instance = new CharacteristicPolynomial(A1);
        List<Number> result = instance.getEigenvalues();
        NumberAssert.assertSameList(Arrays.asList(483.408801305634, -469.813801305634), result, 1e-10);
    }

    @Test
    public void test_getEigenvalues_0030() {
        Matrix A1 = new DenseMatrix(new double[][]{
                    {1.234, -61.972},
                    {3665, 12.361}
                });
        CharacteristicPolynomial instance = new CharacteristicPolynomial(A1);
        List<Number> result = instance.getEigenvalues();
        NumberAssert.assertSameList(
                Arrays.asList(NumberUtils.parseArray(new String[]{"6.7975+476.5463539549432i", "6.7975-476.5463539549432i"})),
                result,
                1e-10);//copied from debugger
    }
}
