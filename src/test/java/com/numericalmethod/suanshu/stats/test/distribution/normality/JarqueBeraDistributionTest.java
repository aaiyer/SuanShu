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
package com.numericalmethod.suanshu.stats.test.distribution.normality;

import com.numericalmethod.suanshu.stats.distribution.univariate.ChiSquareDistribution;
import com.numericalmethod.suanshu.stats.distribution.univariate.ProbabilityDistribution;
import com.numericalmethod.suanshu.stats.random.univariate.normal.StandardNormalRng;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class JarqueBeraDistributionTest {

    private StandardNormalRng rnorm = new StandardNormalRng();

    @Before
    public void setUp() {
        rnorm.seed(12345678901L);
    }

    @After
    public void tearDown() {
    }

    /**
     * The R code is adopted from {@code rjb.test} in package {@code lawstat}.
     *
    n = 3
    N = 2e6
    statistic = 0.5312 #fill in the test statistics here

    jb <- double(N)
    for (k in 1:N) {
    e <- rnorm(n, mean = 0, sd = sqrt(1))
    m1 <- sum(e)/n
    m2 <- sum((e - m1)^2)/n
    m3 <- sum((e - m1)^3)/n
    m4 <- sum((e - m1)^4)/n
    b1 <- (m3/m2^(3/2))^2
    b2 <- (m4/m2^2)
    vk <- 24/n
    vs <- 6/n
    ek <- 3
    jb[k] <- b1/vs + (b2 - ek)^2/vk
    }
    y <- sort(jb)

    cdf.value = length(y[I(y < statistic)]) / N
    cdf.value
     */
    @Test
    public void test_0010() {
        JarqueBeraDistribution instance = new JarqueBeraDistribution(3, 50000, rnorm);

        assertEquals(0.9910, instance.cdf(0.5312), 1e-2);
        assertEquals(0.976173, instance.cdf(0.5309), 1e-2);
        assertEquals(0.800048, instance.cdf(0.5074), 1e-2);
        assertEquals(0.500345, instance.cdf(0.4063), 1e-2);
    }

    /**
     * I am not sure if Matlab is correct. The values seem to differ slightly than our code and the R code in test_0010.
     * This is esp. true for the Matlab results that the critical values are not changing for the first few columns for sample size = 3.
     *
     *
     * From Matlab, the statistics toolbox
     *
     * % Tabulated critical values for JB test

    % Significance levels used in the simulation -- logspace(-3,-1,10), plus the
    % other common levels.
    alphas = [0.001 0.0016681 0.0027826 0.0046416 0.0077426 0.01 0.012915 ...
    0.021544 0.025 0.035938 0.05 0.059948 0.1 0.15 0.2 0.25 0.50];

    % Sample sizes used in the simulation.
    sampleSizes = [3 4 5 10 15 20 25 30 35 40 45 50 60 70 80 90 100 ...
    125 150 175 200 250 300 400 500 800 1000 1200 1400 1600 1800 2000 inf];

    % Critical value table for the above sample sizes (rows) and significance
    % levels (cols). Last row is chi2inv(1-alpha,2), an asymptotic approximation.
    criticalValues = [ ...
    0.5312  0.5312  0.5312  0.5312  0.5312  0.5312  0.5311 0.5310 0.5309 0.5305 0.5297 0.5290 0.5251 0.5176 0.5074 0.4946 0.4063; ...
    0.9606  0.9590  0.9564  0.9520  0.9448  0.9396  0.9329 0.9133 0.9056 0.8817 0.8519 0.8316 0.7553 0.6721 0.6303 0.5947 0.4739; ...
    1.8289  1.8053  1.7727  1.7276  1.6661  1.6275  1.5828 1.4723 1.4343 1.3294 1.2185 1.1516 0.9442 0.7945 0.7302 0.6878 0.5285; ...
    10.9719  9.8430  8.6751  7.4815  6.2927  5.7077  5.1350 4.0456 3.7474 3.0691 2.5239 2.2555 1.6231 1.2821 1.1235 1.0198 0.6951; ...
    19.5425 16.7207 14.0454 11.5527  9.2814  8.2365  7.2613 5.5238 5.0729 4.0773 3.2985 2.9215 2.0533 1.5965 1.3779 1.2336 0.7916; ...
    25.0722 21.0001 17.2981 13.9762 11.0602  9.7531  8.5489 6.4401 5.8998 4.7176 3.8011 3.3596 2.3505 1.8239 1.5631 1.3885 0.8577; ...
    28.4885 23.6332 19.2818 15.4635 12.1658 10.7058  9.3658 7.0389 6.4463 5.1501 4.1494 3.6684 2.5707 1.9986 1.7063 1.5079 0.9071; ...
    30.6106 25.2501 20.5242 16.4088 12.8805 11.3263  9.9063 7.4472 6.8222 5.4578 4.4039 3.8968 2.7431 2.1390 1.8216 1.6040 0.9457; ...
    31.9343 26.2695 21.3105 17.0176 13.3572 11.7488 10.2818 7.7395 7.0942 5.6856 4.5973 4.0734 2.8827 2.2547 1.9172 1.6835 0.9771; ...
    32.7514 26.9025 21.8016 17.4145 13.6749 12.0355 10.5399 7.9520 7.2949 5.8598 4.7481 4.2126 2.9987 2.3524 1.9980 1.7508 1.0033; ...
    33.2273 27.2686 22.1030 17.6594 13.8848 12.2302 10.7201 8.1098 7.4469 5.9945 4.8689 4.3258 3.0973 2.4361 2.0674 1.8085 1.0256; ...
    33.4825 27.4765 22.2863 17.8172 14.0345 12.3739 10.8586 8.2340 7.5661 6.1045 4.9697 4.4214 3.1834 2.5097 2.1283 1.8592 1.0449; ...
    33.5009 27.5458 22.3790 17.9431 14.1788 12.5255 11.0182 8.3968 7.7287 6.2620 5.1203 4.5688 3.3246 2.6316 2.2297 1.9434 1.0768; ...
    33.2610 27.3738 22.2848 17.9209 14.2092 12.5801 11.0884 8.4921 7.8286 6.3696 5.2305 4.6803 3.4374 2.7298 2.3115 2.0114 1.1023; ...
    32.8742 27.0975 22.1135 17.8305 14.1911 12.5841 11.1155 8.5520 7.8938 6.4462 5.3134 4.7662 3.5292 2.8104 2.3789 2.0674 1.1231; ...
    32.3418 26.7278 21.8770 17.6977 14.1283 12.5542 11.1112 8.5850 7.9361 6.5028 5.3796 4.8378 3.6071 2.8788 2.4361 2.1151 1.1408; ...
    31.8884 26.3990 21.6407 17.5510 14.0491 12.5067 11.0882 8.6004 7.9589 6.5429 5.4314 4.8957 3.6730 2.9372 2.4851 2.1557 1.1557; ...
    30.6089 25.4748 21.0100 17.1460 13.8271 12.3565 10.9976 8.6020 7.9816 6.6071 5.5277 5.0071 3.8025 3.0523 2.5819 2.2363 1.1852; ...
    29.4290 24.6043 20.3979 16.7425 13.5878 12.1804 10.8779 8.5719 7.9718 6.6387 5.5919 5.0863 3.8987 3.1384 2.6543 2.2964 1.2072; ...
    28.4225 23.8582 19.8658 16.3846 13.3664 12.0139 10.7589 8.5307 7.9496 6.6571 5.6408 5.1474 3.9732 3.2050 2.7106 2.3436 1.2243; ...
    27.5140 23.1914 19.3922 16.0635 13.1643 11.8602 10.6494 8.4904 7.9259 6.6688 5.6783 5.1957 4.0327 3.2584 2.7559 2.3812 1.2382; ...
    26.0239 22.0764 18.5877 15.5123 12.8124 11.5923 10.4542 8.4114 7.8751 6.6803 5.7343 5.2681 4.1224 3.3402 2.8250 2.4388 1.2591; ...
    24.8353 21.1758 17.9384 15.0633 12.5206 11.3653 10.2839 8.3384 7.8276 6.6842 5.7728 5.3194 4.1873 3.3988 2.8749 2.4808 1.2744; ...
    23.0771 19.8542 16.9669 14.3843 12.0743 11.0157 10.0226 8.2266 7.7526 6.6877 5.8248 5.3889 4.2748 3.4790 2.9434 2.5382 1.2956; ...
    21.8170 18.9068 16.2666 13.8903 11.7469 10.7604  9.8318 8.1454 7.6984 6.6882 5.8581 5.4336 4.3320 3.5318 2.9886 2.5760 1.3097; ...
    19.5539 17.1748 14.9865 12.9765 11.1441 10.2914  9.4852 8.0027 7.6040 6.6858 5.9096 5.5043 4.4246 3.6180 3.0631 2.6388 1.3334; ...
    18.6707 16.4879 14.4745 12.6143 10.9072 10.1106  9.3521 7.9501 7.5691 6.6847 5.9282 5.5296 4.4580 3.6500 3.0907 2.6624 1.3421; ...
    18.0126 15.9830 14.0990 12.3493 10.7362  9.9803  9.2587 7.9127 7.5441 6.6832 5.9408 5.5471 4.4814 3.6718 3.1099 2.6787 1.3484; ...
    17.5190 15.6036 13.8149 12.1501 10.6072  9.8811  9.1869 7.8828 7.5239 6.6800 5.9482 5.5587 4.4979 3.6876 3.1237 2.6904 1.3530; ...
    17.1305 15.3029 13.5883 11.9883 10.5059  9.8072  9.1333 7.8611 7.5089 6.6789 5.9546 5.5677 4.5105 3.6997 3.1345 2.6996 1.3564; ...
    16.8158 15.0600 13.4102 11.8682 10.4296  9.7478  9.0911 7.8446 7.4979 6.6777 5.9600 5.5755 4.5214 3.7101 3.1433 2.7072 1.3595; ...
    16.5585 14.8633 13.2657 11.7666 10.3636  9.6981  9.0549 7.8296 7.4872 6.6763 5.9635 5.5806 4.5289 3.7173 3.1499 2.7129 1.3619; ...
    13.8155 12.7921 11.7687 10.7454 9.7220   9.2103  8.6987 7.6753 7.3778 6.6519 5.9915 5.6286 4.6052 3.7942 3.2189 2.7726 1.3863];
     */
    /**
     * sample size = 3
     */
    @Test
    public void test_0020() {
        JarqueBeraDistribution instance = new JarqueBeraDistribution(3, 60000, rnorm);

        double level = 0.001;
        assertEquals(1 - level, instance.cdf(0.5312), 1e-2);//0.9910 in our code
        assertEquals(0.5312, instance.quantile(1 - level), 1e-4);

        level = 0.0016681;
        assertEquals(1 - level, instance.cdf(0.5312), 1e-2);//0.9910 in our code
        assertEquals(0.5312, instance.quantile(1 - level), 1e-4);

        level = 0.025;
        assertEquals(1 - level, instance.cdf(0.5309), 1e-2);//0.976173 in our code
        assertEquals(0.5309, instance.quantile(1 - level), 1e-4);

        level = 0.2;
        assertEquals(1 - level, instance.cdf(0.5074), 1e-2);//0.800048 in our code
        assertEquals(0.5074, instance.quantile(1 - level), 1e-3);

        level = 0.5;
        assertEquals(1 - level, instance.cdf(0.4063), 1e-2);//0.500345 in our code
        assertEquals(0.4063, instance.quantile(1 - level), 1e-3);
    }

    /**
     * sample size = 100
     *
     * from R
     */
    @Test
    public void test_0030() {
        JarqueBeraDistribution instance = new JarqueBeraDistribution(100, 400000, rnorm);

        assertEquals(0.9989935, instance.cdf(31.8884), 1e-3);
        assertEquals(0.4996065, instance.cdf(1.1557), 1e-3);
    }

    /**
     * sample size = 100
     */
    @Test
    public void test_0040() {
        JarqueBeraDistribution instance = new JarqueBeraDistribution(100, 100000, rnorm);

        double level = 0.5;
        assertEquals(1 - level, instance.cdf(1.1557), 1e-2);//R value is 0.4996065
        assertEquals(1.1557, instance.quantile(1 - level), 1e-1);

        level = 0.001;
        assertEquals(1 - level, instance.cdf(31.8884), 1e-2);//R value is 0.9990335
//        assertEquals(31.8884, instance.quantile(1 - level), 1e-0);//TODO: can't get this working with Ziggurat
    }

    /**
     * sample size -> infinity
     */
//    @Test
    public void test_0050() {
        JarqueBeraDistribution instance = new JarqueBeraDistribution(5000, 100000, rnorm);
        ProbabilityDistribution X2 = new ChiSquareDistribution(2);

        double x = 7;
        double expResult = X2.cdf(x);
        double result = instance.cdf(x);
        assertEquals(expResult, result, 1e-2);
    }
}
