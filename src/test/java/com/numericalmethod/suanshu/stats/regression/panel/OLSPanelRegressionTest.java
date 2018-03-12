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
package com.numericalmethod.suanshu.stats.regression.panel;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Chung Lee
 */
public class OLSPanelRegressionTest {

    @Test
    public void test_solve_0010() {
        List<Matrix> matrices = new ArrayList<Matrix>(3);
        matrices.add(new DenseMatrix(new double[][]{
                    {61, 32, 63},
                    {34, 85, 66},
                    {77, 78, 29}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {45, 81, 72},
                    {13, 14, 15},
                    {86, 27, 18}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {39, 50, 21},
                    {62, 23, 24},
                    {25, 26, 27}}));

        List<Vector> vectors = new ArrayList<Vector>(3);
        vectors.add(new DenseVector(new double[]{0.06, 0.09, 0.15}));
        vectors.add(new DenseVector(new double[]{0.17, 0.21, 0.28}));
        vectors.add(new DenseVector(new double[]{0.35, 0.44, 0.5}));

        PanelRegression regression = new OLSPanelRegression();
        PanelRegressionResult result = regression.solve(vectors, matrices, true);

        assertEquals(-9.45796611448633E-4, result.getBeta().betaHat.get(1), 1e-15);
        assertEquals(-8.797767933571604E-4, result.getBeta().betaHat.get(2), 1e-15);
        assertEquals(-0.0036286408642275053, result.getBeta().betaHat.get(3), 1e-15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_solve_emptyYtList() {
        List<Matrix> matrices = new ArrayList<Matrix>(3);
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 61, 32, 63},
                    {1, 34, 85, 66},
                    {1, 77, 78, 29}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 45, 81, 72},
                    {1, 13, 14, 15},
                    {1, 86, 27, 18}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 39, 50, 21},
                    {1, 62, 23, 24},
                    {1, 25, 26, 27}}));

        List<Vector> vectors = new ArrayList<Vector>(3);

        PanelRegression regression = new OLSPanelRegression();
        PanelRegressionResult result = regression.solve(vectors, matrices, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_solve_unmatchedListSizes() {
        List<Matrix> matrices = new ArrayList<Matrix>(3);
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 61, 32, 63},
                    {1, 34, 85, 66},
                    {1, 77, 78, 29}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 45, 81, 72},
                    {1, 13, 14, 15},
                    {1, 86, 27, 18}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 39, 50, 21},
                    {1, 62, 23, 24},
                    {1, 25, 26, 27}}));

        List<Vector> vectors = new ArrayList<Vector>(3);
        vectors.add(new DenseVector(new double[]{0.06, 0.09, 0.15}));
        vectors.add(new DenseVector(new double[]{0.17, 0.21, 0.28}));

        PanelRegression regression = new OLSPanelRegression();
        PanelRegressionResult result = regression.solve(vectors, matrices, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_solve_inconsistentAt() {
        List<Matrix> matrices = new ArrayList<Matrix>(3);
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 61, 32},
                    {1, 34, 85},
                    {1, 77, 78}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 45, 81, 72},
                    {1, 13, 14, 15},
                    {1, 86, 27, 18}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 39, 50, 21},
                    {1, 62, 23, 24},
                    {1, 25, 26, 27}}));

        List<Vector> vectors = new ArrayList<Vector>(3);
        vectors.add(new DenseVector(new double[]{0.06, 0.09, 0.15}));
        vectors.add(new DenseVector(new double[]{0.17, 0.21, 0.28}));
        vectors.add(new DenseVector(new double[]{0.35, 0.44, 0.5}));

        PanelRegression regression = new OLSPanelRegression();
        PanelRegressionResult result = regression.solve(vectors, matrices, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_solve_unmatchedMatrixDimension() {
        List<Matrix> matrices = new ArrayList<Matrix>(3);
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 61, 32, 63},
                    {1, 34, 85, 66}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 45, 81, 72},
                    {1, 13, 14, 15},
                    {1, 86, 27, 18}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 39, 50, 21},
                    {1, 62, 23, 24},
                    {1, 25, 26, 27}}));

        List<Vector> vectors = new ArrayList<Vector>(3);
        vectors.add(new DenseVector(new double[]{0.06, 0.09, 0.15}));
        vectors.add(new DenseVector(new double[]{0.17, 0.21, 0.28}));
        vectors.add(new DenseVector(new double[]{0.35, 0.44, 0.5}));

        PanelRegression regression = new OLSPanelRegression();
        PanelRegressionResult result = regression.solve(vectors, matrices, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_solve_unmatchedVectorDimension() {
        List<Matrix> matrices = new ArrayList<Matrix>(3);
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 61, 32, 63},
                    {1, 34, 85, 66},
                    {1, 77, 78, 29}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 45, 81, 72},
                    {1, 13, 14, 15},
                    {1, 86, 27, 18}}));
        matrices.add(new DenseMatrix(new double[][]{
                    {1, 39, 50, 21},
                    {1, 62, 23, 24},
                    {1, 25, 26, 27}}));

        List<Vector> vectors = new ArrayList<Vector>(3);
        vectors.add(new DenseVector(new double[]{0.06, 0.09, 0.15}));
        vectors.add(new DenseVector(new double[]{0.17, 0.21, 0.28}));
        vectors.add(new DenseVector(new double[]{0.35, 0.44}));

        PanelRegression regression = new OLSPanelRegression();
        PanelRegressionResult result = regression.solve(vectors, matrices, false);
    }
}