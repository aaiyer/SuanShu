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
package com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr;

import com.numericalmethod.suanshu.matrix.doubles.factorization.eigen.qr.FrancisQRStep;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Householder;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class FrancisQRStepTest {

    @Test
    public void test_0010() {
        Matrix H = new DenseMatrix(new double[][]{
                    {2, 3, 4, 5, 6},
                    {4, 4, 5, 6, 7},
                    {0, 3, 6, 7, 8},
                    {0, 0, 2, 8, 9},
                    {0, 0, 0, 1, 10}});

        FrancisQRStep instance = new FrancisQRStep(H);
        Matrix A = instance.M();

        Matrix expected = new DenseMatrix(new double[][]{
                    {51, -24, -15, 12, 62},
                    {-48, 42, -12, 26, 90},
                    {12, -24, 28, -2, 68},
                    {0, 6, -8, 14, 16},
                    {0, 0, 2, 0, 0}});

        assertEquals(expected, A);
    }

    @Test
    public void test_0020() {
        Matrix H = new DenseMatrix(new double[][]{
                    {2, 3, 4, 5, 6},
                    {4, 4, 5, 6, 7},
                    {0, 3, 6, 7, 8},
                    {0, 0, 2, 8, 9},
                    {0, 0, 0, 1, 10}});

        FrancisQRStep instance = new FrancisQRStep(H);

        Matrix expected1 = new DenseMatrix(new double[][]{
                    {-0.795008912655971, -0.8475745324105156, 0.4313082689196342, -0.5747326555754999, -1.172124355097738},
                    {1.2420341725727861, 2.1034718707366435, 3.193615339834914, -1.2441269059705118, -1.2949542740652833},
                    {0, 0.6958500651868615, 18.46282755933433, -8.419880322685358, -10.665976338550696},
                    {0, 0, 3.3880195498640093, 4.9095628262048185, -2.5916167692},
                    {0, 0, 0, 4.556122394761527, 5.31914665638018}});

        Matrix ZtHZ = instance.ZtHZ();
        assertTrue(AreMatrices.equal(expected1, ZtHZ, 1e-15));

        ArrayList<Householder> Hs = instance.getHouseholderTransformations();

        Matrix expected2 = new DenseMatrix(new double[][]{
                    {-0.7177405625652731, 0.6755205294731985, -0.16888013236829963, 0, 0},
                    {0.6755205294731985, 0.7343440588849632, 0.0664139852787592, 0, 0},
                    {-0.16888013236829963, 0.0664139852787592, 0.9833965036803102, 0, 0},
                    {0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 1}});
        assertTrue(AreMatrices.equal(expected2, Hs.get(0).H(), 1e-15));

        Matrix expected3 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, -0.5908385855098244, 0.759577348924757, -0.27194120113213366, 0},
                    {0, 0.759577348924757, 0.6373247705613946, 0.12984370538960918, 0},
                    {0, -0.27194120113213366, 0.12984370538960918, 0.95351381494843, 0},
                    {0, 0, 0, 0, 1}});

        assertTrue(AreMatrices.equal(expected3, Hs.get(1).H(), 1e-15));

        Matrix expected4 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0},
                    {0, 0, -0.9064621674637037, -0.15999479405628042, -0.390804305032446},
                    {0, 0, -0.15999479405628042, 0.9865728601584752, -0.03279721746755534},
                    {0, 0, -0.390804305032446, -0.03279721746755534, 0.9198893073052283}});

        assertEquals(expected4, Hs.get(2).H());

        Matrix expected5 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, -0.26171737508874693, 0.9651445568290046},
                    {0, 0, 0, 0.9651445568290046, 0.26171737508874726}});

        assertTrue(AreMatrices.equal(expected5, Hs.get(3).H(), 1e-15));

        Matrix expected6 = new DenseMatrix(new double[][]{
                    {-0.7177405625652731, -0.5274011173471673, -0.3346513004865198, -0.07635739091257082, -0.29811843654016107},
                    {0.6755205294731985, -0.38343224615956706, -0.5134136177106923, -0.14584075879894548, -0.334348107647472},
                    {-0.16888013236829963, 0.7077257640871927, -0.63138644377506, -0.25884412381735594, -0.07038907529420334},
                    {0, -0.27194120113213366, -0.27025565307147603, -0.3199207473270893, 0.866406706194844},
                    {0, 0, -0.390804305032446, 0.896409759496668, 0.20909695896218217}});

        Matrix Z = instance.Z();
        assertTrue(AreMatrices.equal(expected6, Z, 1e-15));

        Matrix Hnext = Z.t().multiply(H).multiply(Z);
        assertTrue(AreMatrices.equal(expected1, Hnext, 1e-13));
    }

    /**
     * Same as in test_0020, but using a different precision, 1e-8.
     */
    @Test
    public void test_0025() {
        Matrix H = new DenseMatrix(new double[][]{
                    {2, 3, 4, 5, 6},
                    {4, 4, 5, 6, 7},
                    {0, 3, 6, 7, 8},
                    {0, 0, 2, 8, 9},
                    {0, 0, 0, 1, 10}});

        FrancisQRStep instance = new FrancisQRStep(H);

        Matrix expected1 = new DenseMatrix(new double[][]{
                    {-0.795008912655971, -0.8475745324105156, 0.4313082689196342, -0.5747326555754999, -1.172124355097738},
                    {1.2420341725727861, 2.1034718707366435, 3.193615339834914, -1.2441269059705118, -1.2949542740652833},
                    {0, 0.6958500651868615, 18.46282755933433, -8.419880322685358, -10.665976338550696},
                    {0, 0, 3.3880195498640093, 4.9095628262048185, -2.5916167692},
                    {0, 0, 0, 4.556122394761527, 5.31914665638018}});

        Matrix ZtHZ = instance.ZtHZ();
        assertTrue(AreMatrices.equal(expected1, ZtHZ, 1e-15));

        ArrayList<Householder> Hs = instance.getHouseholderTransformations();

        Matrix expected2 = new DenseMatrix(new double[][]{
                    {-0.7177405625652731, 0.6755205294731985, -0.16888013236829963, 0, 0},
                    {0.6755205294731985, 0.7343440588849632, 0.0664139852787592, 0, 0},
                    {-0.16888013236829963, 0.0664139852787592, 0.9833965036803102, 0, 0},
                    {0, 0, 0, 1, 0},
                    {0, 0, 0, 0, 1}});
        assertTrue(AreMatrices.equal(expected2, Hs.get(0).H(), 1e-15));

        Matrix expected3 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, -0.5908385855098244, 0.759577348924757, -0.27194120113213366, 0},
                    {0, 0.759577348924757, 0.6373247705613946, 0.12984370538960918, 0},
                    {0, -0.27194120113213366, 0.12984370538960918, 0.95351381494843, 0},
                    {0, 0, 0, 0, 1}});

        assertTrue(AreMatrices.equal(expected3, Hs.get(1).H(), 1e-15));

        Matrix expected4 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0},
                    {0, 0, -0.9064621674637037, -0.15999479405628042, -0.390804305032446},
                    {0, 0, -0.15999479405628042, 0.9865728601584752, -0.03279721746755534},
                    {0, 0, -0.390804305032446, -0.03279721746755534, 0.9198893073052283}});

        assertEquals(expected4, Hs.get(2).H());

        Matrix expected5 = new DenseMatrix(new double[][]{
                    {1, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, -0.26171737508874693, 0.9651445568290046},
                    {0, 0, 0, 0.9651445568290046, 0.26171737508874726}});

        assertTrue(AreMatrices.equal(expected5, Hs.get(3).H(), 1e-15));

        Matrix expected6 = new DenseMatrix(new double[][]{
                    {-0.7177405625652731, -0.5274011173471673, -0.3346513004865198, -0.07635739091257082, -0.29811843654016107},
                    {0.6755205294731985, -0.38343224615956706, -0.5134136177106923, -0.14584075879894548, -0.334348107647472},
                    {-0.16888013236829963, 0.7077257640871927, -0.63138644377506, -0.25884412381735594, -0.07038907529420334},
                    {0, -0.27194120113213366, -0.27025565307147603, -0.3199207473270893, 0.866406706194844},
                    {0, 0, -0.390804305032446, 0.896409759496668, 0.20909695896218217}});

        Matrix Z = instance.Z();
        assertTrue(AreMatrices.equal(expected6, Z, 1e-15));

        Matrix Hnext = Z.t().multiply(H).multiply(Z);
        assertTrue(AreMatrices.equal(expected1, Hnext, 1e-13));
    }
}
