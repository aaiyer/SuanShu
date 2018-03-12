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
package com.numericalmethod.suanshu.stats.dlm.multivariate;

import com.numericalmethod.suanshu.misc.R;
import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.CreateMatrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.stats.timeseries.multivariate.realtime.SimpleMultiVariateTimeSeries;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Haksun Li
 */
public class LinearKalmanFilterTest {

    /**
     * Compare this with the output of R (only fitted values are plotted):
     * 
    library(dlm)
    plot(Nile, type='o', col = c("darkgrey"),xlab = "", ylab = "Level")
    mod1 <- dlmModPoly(order = 1, dV = 15100, dW = 755)
    NileFilt1 <- dlmFilter(Nile, mod1)
    lines(dropFirst(NileFilt1$m), lty = "longdash")
    mod2 <- dlmModPoly(order = 1, dV = 15100, dW = 7550)
    NileFilt2 <- dlmFilter(Nile, mod2)
    lines(dropFirst(NileFilt2$m), lty = "dotdash")
    leg <- c("data", paste("filtered, W/V =", format(c(W(mod1) / V(mod1), W(mod2) / V(mod2)))))
    legend("bottomright", legend = leg,col=c("darkgrey", "black", "black"), lty = c("solid", "longdash", "dotdash"), pch = c(1, NA, NA), bty = "n")
    nile <- as.vector(Nile)
    (diff <- cbind(nile - as.vector(NileFilt1$m[-1]),
    nile - as.vector(NileFilt2$m[-1]),
    nile - as.vector(NileFilt1$f),
    nile - as.vector(NileFilt2$f)))
     */
    @Test
    public void test_0010() {
        DenseMatrix data = new DenseMatrix(new double[][]{{
                        1120, 1160, 963, 1210, 1160, 1160, 813, 1230,
                        1370, 1140, 995, 935, 1110, 994, 1020, 960, 1180, 799, 958, 1140, 1100,
                        1210, 1150, 1250, 1260, 1220, 1030, 1100, 774, 840, 874, 694, 940, 833,
                        701, 916, 692, 1020, 1050, 969, 831, 726, 456, 824, 702, 1120, 1100, 832,
                        764, 821, 768, 845, 864, 862, 698, 845, 744, 796, 1040, 759, 781, 865,
                        845, 944, 984, 897, 822, 1010, 771, 676, 649, 846, 812, 742, 801,
                        1040, 860, 874, 848, 890, 744, 749, 838, 1050, 918, 986, 797, 923, 975,
                        815, 1020, 906, 901, 1170, 912, 746, 919, 718, 714, 740}}).t();//a 1D time series
        SimpleMultiVariateTimeSeries nile = new SimpleMultiVariateTimeSeries(data);

        DenseMatrix F = new DenseMatrix(new double[][]{{1.}});
        DenseMatrix V = new DenseMatrix(new double[][]{{15100.}});
        ObservationEquation observation = new ObservationEquation(F, V);

        DenseMatrix G = new DenseMatrix(new double[][]{{1.}});
        DenseMatrix W1 = new DenseMatrix(new double[][]{{755.}});
        DenseMatrix W2 = new DenseMatrix(new double[][]{{7550.}});
        StateEquation state1 = new StateEquation(G, W1);
        StateEquation state2 = new StateEquation(G, W2);

        DenseVector m0 = new DenseVector(new double[]{0.});
        DenseMatrix C0 = new DenseMatrix(new double[][]{{1e7}});

        DLM model1 = new DLM(m0, C0, observation, state1);
        DLM model2 = new DLM(m0, C0, observation, state2);

        LinearKalmanFilter KF1 = new LinearKalmanFilter(model1);
        KF1.filtering(nile);
        SimpleMultiVariateTimeSeries Xt_1 = KF1.getFittedStates();
        SimpleMultiVariateTimeSeries Yt_1 = KF1.getPredictedObservations();

        LinearKalmanFilter KF2 = new LinearKalmanFilter(model2);
        KF2.filtering(nile);
        SimpleMultiVariateTimeSeries Xt_2 = KF2.getFittedStates();
        SimpleMultiVariateTimeSeries Yt_2 = KF2.getPredictedObservations();

        Matrix diff_states_1 = data.minus(Xt_1.toMatrix());
        Matrix diff_observations_1 = data.minus(Yt_1.toMatrix());

        Matrix diff_states_2 = data.minus(Xt_2.toMatrix());
        Matrix diff_observations_2 = data.minus(Yt_2.toMatrix());

        Matrix diff = CreateMatrix.cbind(diff_states_1, diff_states_2, diff_observations_1, diff_observations_2);
//        System.out.println(diff);

        DenseMatrix expectedDiff = new DenseMatrix(new double[][]{
                    {1.68852284702598, 1.68737808862943, 1120, 1120},
                    {20.3508311986516, 16.685006204694, 41.688522847026, 41.6873780886294},
                    {-113.103524053076, -85.8741459928733, -176.649168801348, -180.314993795306},
                    {94.9803649715652, 79.6172797775803, 133.896475946924, 161.125854007127},
                    {33.5513298997214, 14.7653111022009, 44.9803649715652, 29.6172797775803},
                    {25.7277831232796, 7.37725924708593, 33.5513298997214, 14.7653111022009},
                    {-250.371624513729, -169.780345416900, -321.272216876720, -339.622740752914},
                    {131.132512812269, 123.604181593729, 166.628375486271, 247.219654583100},
                    {214.669307584644, 131.800585847184, 271.132512812269, 263.604181593729},
                    {-12.1841436376592, -49.0995669185829, -15.3306924153558, -98.1994141528162},
                    {-125.221878595649, -97.0497142008494, -157.184143637659, -194.099566918583},
                    {-147.782296675621, -78.524843090841, -185.221878595649, -157.049714200849},
                    {21.7370585316919, 48.2375763030686, 27.2177033243786, 96.475156909159},
                    {-75.3282011379524, -33.88121147067, -94.2629414683081, -67.7624236969314},
                    {-39.4350661519672, -3.94060572435001, -49.3282011379524, -7.88121147067},
                    {-79.5125976710378, -31.9703028398945, -99.4350661519672, -63.94060572435},
                    {112.357871387346, 94.0148485636728, 140.487402328962, 188.029697160105},
                    {-214.87448610964, -143.492575711914, -268.642128612654, -286.985151436327},
                    {-44.6943692469158, 7.75371214395875, -55.87448610964, 15.5074242880864},
                    {109.836296118745, 94.876856071721, 137.305630753084, 189.753712143959},
                    {55.8663650270159, 27.4384280358418, 69.8362961187454, 54.876856071721},
                    {132.689030780196, 68.7192140179093, 165.866365027016, 137.438428035842},
                    {58.1500855793079, 4.35960700895453, 72.6890307801964, 8.71921401790928},
                    {126.518482416978, 52.1798035044767, 158.150085579308, 104.359607008955},
                    {109.213909711048, 31.0899017522383, 136.518482416978, 62.1798035044767},
                    {55.370843457558, -4.45504912388083, 69.2139097110485, -8.91009824776165},
                    {-107.702971303134, -97.2275245619403, -134.629156542442, -194.455049123881},
                    {-30.1623136067844, -13.6137622809701, -37.7029713031343, -27.2275245619403},
                    {-284.929467367499, -169.806881140485, -356.162313606784, -339.61376228097},
                    {-175.143423017525, -51.9034405702425, -218.929467367499, -103.806881140485},
                    {-112.914676161393, -8.95172028512127, -141.143423017525, -17.9034405702425},
                    {-234.331658245778, -94.4758601425606, -292.914676161393, -188.951720285121},
                    {9.33467129540088, 75.7620699287197, 11.6683417542224, 151.524139857439},
                    {-78.1322516715182, -15.6189650356401, -97.6653287045991, -31.2379300712803},
                    {-168.10578578797, -73.80948251782, -210.132251671518, -147.61896503564},
                    {37.5153691487916, 70.59525874109, 46.8942142120301, 141.19051748218},
                    {-149.187699028741, -76.702370629455, -186.484630851208, -153.40474125891},
                    {143.049837308411, 125.648814685272, 178.812300971259, 251.297629370545},
                    {138.439867698366, 77.8244073426362, 173.049837308411, 155.648814685272},
                    {45.9518937023091, -1.58779632868186, 57.4398676983657, -3.17559265736384},
                    {-73.6384845700823, -69.793898164341, -92.0481062976909, -139.587796328682},
                    {-142.910787074698, -87.3969490821704, -178.638484570082, -174.793898164341},
                    {-330.32862879973, -178.698474541085, -412.910787074698, -357.396949082170},
                    {30.1370969099993, 94.6507627294574, 37.67137120027, 189.301525458915},
                    {-73.4903223936294, -13.6746186352713, -91.8629030900007, -27.3492372705426},
                    {275.607741896993, 202.162690682364, 344.509677606371, 404.325381364729},
                    {204.486193428274, 91.0813453411821, 255.607741896993, 182.162690682364},
                    {-50.8110452431765, -88.4593273294089, -63.5138065717263, -176.918654658818},
                    {-95.0488361775356, -78.2296636647044, -118.811045243177, -156.459327329409},
                    {-30.4390689385431, -10.6148318323521, -38.0488361775356, -21.2296636647044},
                    {-66.7512551459427, -31.8074159161761, -83.439068938543, -63.6148318323521},
                    {8.19899588286137, 22.5962920419120, 10.2487448540573, 45.1925840838239},
                    {21.759196705636, 20.798146020956, 27.1989958828614, 41.596292041912},
                    {15.8073573642051, 9.3990730104780, 19.759196705636, 18.7981460209560},
                    {-118.554114107178, -77.3004634947609, -148.192642635795, -154.600926989522},
                    {22.7567087140783, 34.8497682526195, 28.4458858928217, 69.6995365052391},
                    {-62.5946330284221, -33.0751158736903, -78.2432912859217, -66.1502317473805},
                    {-8.47570642271035, 9.46244206315487, -10.5946330284221, 18.9248841263097},
                    {188.419434861443, 126.731221031577, 235.524293577290, 253.462442063155},
                    {-74.0644521107478, -77.1343894842113, -92.580565138557, -154.268778968423},
                    {-41.651561688563, -27.5671947421056, -52.0644521107478, -55.1343894842113},
                    {33.8787506491312, 28.2164026289472, 42.348438311437, 56.4328052578944},
                    {11.1030005193012, 4.1082013144736, 13.8787506491312, 8.2164026289472},
                    {88.0824004154215, 51.5541006572367, 110.103000519301, 103.108201314474},
                    {102.465920332323, 45.7770503286183, 128.082400415421, 91.5541006572367},
                    {12.3727362658570, -20.6114748356908, 15.4659203323226, -41.2229496713817},
                    {-50.1018109873114, -47.8057374178454, -62.627263734143, -95.6114748356908},
                    {110.318551210147, 70.0971312910773, 137.898189012689, 140.194262582155},
                    {-102.945159031880, -84.4514343544613, -128.681448789853, -168.902868708923},
                    {-158.356127225502, -89.7257171772306, -197.94515903188, -179.451434354461},
                    {-148.2849017804, -58.3628585886153, -185.356127225502, -116.725717177231},
                    {38.9720785756798, 69.3185707056923, 48.7150982196001, 138.637141411385},
                    {3.97766286054377, 17.6592853528462, 4.9720785756798, 35.3185707056923},
                    {-52.8178697115649, -26.1703573235769, -66.0223371394562, -52.3407146471538},
                    {4.94570423074811, 16.4148213382115, 6.18213028843513, 32.8296426764231},
                    {195.156563384598, 127.707410669106, 243.945704230748, 255.414821338211},
                    {12.1252507076786, -26.1462946654472, 15.1565633845983, -52.2925893308943},
                    {20.9002005661429, -6.07314733272358, 26.1252507076786, -12.1462946654472},
                    {-4.07983954708573, -16.0365736663617, -5.09979943385713, -32.0731473327236},
                    {30.3361283623315, 12.9817131668191, 37.9201604529143, 25.9634263336383},
                    {-92.5310973101348, -66.5091434165904, -115.663871637669, -133.018286833181},
                    {-70.0248778481078, -30.7545717082952, -87.5310973101348, -61.5091434165904},
                    {15.1800977215137, 29.1227141458523, 18.9751221518922, 58.2454282917048},
                    {181.744078177211, 120.561357072926, 227.180097721514, 241.122714145852},
                    {39.7952625417687, -5.71932146353686, 49.7440781772109, -11.4386429270738},
                    {86.236210033415, 31.1403392682315, 107.795262541769, 62.2806785364631},
                    {-82.211031973268, -78.9298303658842, -102.763789966585, -157.859660731768},
                    {35.0311744213857, 23.5350848170579, 43.7889680267321, 47.0701696341158},
                    {69.6249395371085, 37.7675424085289, 87.0311744213857, 75.5350848170579},
                    {-72.3000483703132, -61.1162287957355, -90.3750604628915, -122.232457591471},
                    {106.159961303749, 71.9418856021322, 132.699951629687, 143.883771204264},
                    {-6.27203095700054, -21.0290571989339, -7.84003869625064, -42.0581143978678},
                    {-9.01762476560043, -13.0145285994669, -11.2720309570005, -26.0290571989339},
                    {207.985900187520, 127.992735700267, 259.982375234400, 255.985471400533},
                    {-40.0112798499842, -65.0036321498667, -50.0140998124804, -130.007264299733},
                    {-164.809023879987, -115.501816074933, -206.011279849984, -231.003632149867},
                    {6.55278089601006, 28.7490919625333, 8.19097612001258, 57.4981839250667},
                    {-155.557775283192, -86.1254540187333, -194.44721910399, -172.250908037467},
                    {-127.646220226554, -45.0627270093667, -159.557775283192, -90.1254540187333},
                    {-81.3169761812428, -9.53136350468333, -101.646220226554, -19.0627270093667}
                });

        assertTrue(AreMatrices.equal(expectedDiff, diff, 1e-5));
    }

//    @Test//TODO: this test case is not asserting (thus testing) anything!!!
    public void test_0020() {
        int T = 20;
        int p = 3;
        int d = 2;

        DenseMatrix F = new DenseMatrix(new double[][]{
                    {0.5, 0.2, 0.3},
                    {0.1, 0.8, 0.1}
                });
        DenseMatrix V = new DenseMatrix(d, d).ONE().scaled(0.1);
        ObservationEquation observation = new ObservationEquation(F, V);

        DenseMatrix G = new DenseMatrix(p, p).ONE();
        DenseMatrix W = new DenseMatrix(p, p).ONE().scaled(0.1);
        StateEquation state = new StateEquation(G, W);

        DenseVector m0 = new DenseVector(R.rep(0., p));
        DenseMatrix C0 = new DenseMatrix(p, p).ONE();

        DLM model = new DLM(m0, C0, observation, state);

        DLMSeries sim = new DLMSeries(T, model);

        SimpleMultiVariateTimeSeries states = sim.getStates();
        Matrix Xt = states.toMatrix();

        SimpleMultiVariateTimeSeries observations = sim.getObservations();
        Matrix Yt = observations.toMatrix();

        LinearKalmanFilter KF = new LinearKalmanFilter(model);
        KF.filtering(observations);
        SimpleMultiVariateTimeSeries Xt_hat = KF.getFittedStates();
        SimpleMultiVariateTimeSeries Yt_hat = KF.getPredictedObservations();

        Matrix diff_states = Xt.minus(Xt_hat.toMatrix());
        Matrix diff_observations = Yt.minus(Yt_hat.toMatrix());

        System.out.println(Xt.toString());
        System.out.println(Yt.toString());
        System.out.println(Xt_hat.toString());
        System.out.println(Yt_hat.toString());
        System.out.println(diff_states.toString());
        System.out.println(diff_observations.toString());
    }
}
