package com.numericalmethod.suanshu.stats.test.rank;

///*
// * Copyright (c) Numerical Method Inc.
// * http://www.numericalmethod.com/
// *
// * THIS SOFTWARE IS LICENSED, NOT SOLD.
// *
// * YOU MAY USE THIS SOFTWARE ONLY AS DESCRIBED IN THE LICENSE.
// * IF YOU ARE NOT AWARE OF AND/OR DO NOT AGREE TO THE TERMS OF THE LICENSE,
// * DO NOT USE THIS SOFTWARE.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITH NO WARRANTY WHATSOEVER,
// * EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION,
// * ANY WARRANTIES OF ACCURACY, ACCESSIBILITY, COMPLETENESS,
// * FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABILITY, NON-INFRINGEMENT,
// * TITLE AND USEFULNESS.
// *
// * IN NO EVENT AND UNDER NO LEGAL THEORY,
// * WHETHER IN ACTION, CONTRACT, NEGLIGENCE, TORT, OR OTHERWISE,
// * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// * ANY CLAIMS, DAMAGES OR OTHER LIABILITIES,
// * ARISING AS A RESULT OF USING OR OTHER DEALINGS IN THE SOFTWARE.
// */
//package com.numericalmethod.suanshu.stats.test.rank;
//
//import com.numericalmethod.suanshu.matrix.doubles.dense.operation.CreateMatrix;
//import com.numericalmethod.suanshu.matrix.doubles.dense.DenseMatrix;
//import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
//import com.numericalmethod.suanshu.matrix.doubles.Matrix;
//import com.numericalmethod.suanshu.stats.descriptive.rank.Rank;
//
///**
// * Construct confidence intervals based on rank statistics for two sample location parameters, two sample scale parameter, and one sample location parameter.
// *
// * @author Chun Yip Yau
// *
// * @see "David F. Bauer. "Constructing confidence sets using rank statistics," Journal of the American Statistical Association 67, 687–690. 1972."
// */
//public class ConfidenceSet {
//
////    public static enum Distribution {
////
////        WILCOXON_RANK_SUM,
////    }
//    /**
//     * the upper and lower confident limits
//     */
//    public final double upperCL;
//    public final double lowerCL;
//
//    /**
//     *
//     * @param group1  // sorted value of group 1 data
//     * @param group2  // sorted value of group 2 data
//     * @param a       // the score vector assigned to the ranks
//     * @param upperLimit // The smallest number u such that if test stat>=u, the test is rejected
//     * @param lowerLimit // The largest number l such that if test stat <= l, the test is rejected
//     *
//     */
//    public ConfidenceSet(DenseVector group1, DenseVector group2, DenseVector a, double lowerLimit, double upperLimit) {
//        // m is the number of observations from group 1
//        // n is the number of observations from group 2
//        int m = group1.length;
//        int n = group2.length;
//        int N = m + n;
//        // d stores the value of dij
//        Matrix d = new DenseMatrix(m, n);
//        Matrix jump = new DenseMatrix(m, n);
//        Matrix S = new DenseMatrix(m, n);
//
//        // Set up the first two rows of the table on page 688 of the paper Bauer(1972)
//        for (int i = 1; i <= m; ++i) {
//            for (int j = 1; j <= n; ++j) {
//                d.set(i, j, group2.get(j) - group1.get(i));
//                jump.set(i, j, a.get(j + i - 1) - a.get(j + i));
//            }
//        }
//
//        // Fill in the S, the 3rd row of the table
//        // Start from the top right corner
//
//        double[] CombinedData = new double[N];
//        for (int i = 0; i < m; ++i) {
//            CombinedData[i] = group1.get(i + 1);
//        }
//        for (int i = 0; i < n; ++i) {
//            CombinedData[m + i] = group2.get(i + 1);
//        }
//        Rank instance = new Rank(CombinedData);
//        double[] RankCombined = instance.ranks();
//        // Find the Sum of the scores for group 2
//        double sumScore = 0;
//        for (int i = 0; i < n; ++i) {
//            //sumScore = sumScore + a.get((int)RankCombined[m+i]);
//            sumScore = sumScore - a.get(m + i + 1);
//        }
//        S.set(1, n, sumScore); // Top right entry of S
//
//        int counterColumn = n;
//        int[] ColumnIndex = new int[n];
//        for (int i = 0; i < n; ++i) {
//            ColumnIndex[i] = 1;
//        }
//        ColumnIndex[n - 1] = 2;
//
//        int actionColumn = 0;
//        int nextColumn = 1;
//        int[] tiedIndex = new int[n];
//        int tiedNumber = 0;
//        double currentScore = sumScore;
//        double tempJ = jump.get(1, n);
//
//        Matrix rankedS = new DenseMatrix(new DenseVector(1, sumScore));
//        Matrix rankedD = new DenseMatrix(new DenseVector(1, d.get(1, n)));
//
//        while (counterColumn > 0) {
//            nextColumn = 1;
//            tiedNumber = 0;
//            actionColumn = counterColumn;
//            while (nextColumn == 1) {
//                nextColumn = 0;
//                for (int i = 1; i < actionColumn; ++i) {
//                    int j = actionColumn - i;
//                    if (d.get(ColumnIndex[actionColumn - 1], actionColumn) == d.get(ColumnIndex[j - 1], j)) {
//                        tiedIndex[tiedNumber] = j;
//                        tiedNumber = tiedNumber + 1;
//                    }
//                    if (d.get(ColumnIndex[actionColumn - 1], actionColumn) < d.get(ColumnIndex[j - 1], j)) {
//                        actionColumn = j;
//                        nextColumn = 1;
//                        tiedNumber = 0;
//                        break;
//                    }
//                }
//            }
//            if (tiedNumber > 0) {
//                currentScore = currentScore - tempJ;
//                tempJ = jump.get(ColumnIndex[actionColumn - 1], actionColumn);
//                for (int j = 0; j < tiedNumber; ++j) {
//                    tempJ = tempJ + jump.get(ColumnIndex[tiedIndex[j] - 1], tiedIndex[j]);
//                }
//                S.set(ColumnIndex[actionColumn - 1], actionColumn, currentScore);
//                rankedS = CreateMatrix.cbind(rankedS, new DenseMatrix(new DenseVector(1, currentScore)));
//                rankedD = CreateMatrix.cbind(rankedD, new DenseMatrix(new DenseVector(1, d.get(ColumnIndex[actionColumn - 1], actionColumn))));
//                ColumnIndex[actionColumn - 1] = ColumnIndex[actionColumn - 1] + 1;
//                for (int j = 0; j < tiedNumber; ++j) {
//                    S.set(ColumnIndex[tiedIndex[j] - 1], tiedIndex[j], currentScore);
//                    ColumnIndex[tiedIndex[j] - 1] = ColumnIndex[tiedIndex[j] - 1] + 1;
//                }
//            } else {
//                currentScore = currentScore - tempJ;
//                S.set(ColumnIndex[actionColumn - 1], actionColumn, currentScore);
//                rankedS = CreateMatrix.cbind(rankedS, new DenseMatrix(new DenseVector(1, currentScore)));
//                rankedD = CreateMatrix.cbind(rankedD, new DenseMatrix(new DenseVector(1, d.get(ColumnIndex[actionColumn - 1], actionColumn))));
//                tempJ = jump.get(ColumnIndex[actionColumn - 1], actionColumn);
//                ColumnIndex[actionColumn - 1] = ColumnIndex[actionColumn - 1] + 1;
//            }
//            if (ColumnIndex[counterColumn - 1] > m) {
//                counterColumn = counterColumn - 1;
//            }
//        }
//
//        // Vectorize d and S
//        DenseVector dVector = new DenseVector(n * m, 0);
//        DenseVector SVector = new DenseVector(n * m, 0);
//        for (int i = 0; i < m; ++i) {
//            for (int j = 0; j < n; ++j) {
//                dVector.set(i * n + j + 1, d.get(i + 1, j + 1));
//                SVector.set(i * n + j + 1, S.get(i + 1, j + 1));
//            }
//        }
//        // Determine the lower and upper limit of the confidence interval
//        int lengthS = rankedS.nCols();
//        double lowerConfidLimit = dVector.get(lengthS);
//        double upperConfidLimit = dVector.get(1);
//
//        for (int i = 1; i <= lengthS - 1; ++i) {
//            if ((rankedS.get(1, i + 1) >= upperLimit) & (rankedS.get(1, i) < upperLimit)) {
//                lowerConfidLimit = rankedD.get(1, i);
//            }
//            if ((rankedS.get(1, i) <= lowerLimit) & (rankedS.get(1, i + 1) > lowerLimit)) {
//                upperConfidLimit = rankedD.get(1, i);
//            }
//        }
//
//        System.out.println("The printed result can be checked with the referenced paper");
//        System.out.println(d);
//        System.out.println(jump);
//        System.out.println(S);
//        System.out.println(upperConfidLimit);
//        System.out.println(lowerConfidLimit);
//
//        upperCL = upperConfidLimit;
//        lowerCL = lowerConfidLimit;
//
//    }
//}
