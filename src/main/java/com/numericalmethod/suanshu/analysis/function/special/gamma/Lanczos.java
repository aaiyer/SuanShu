package com.numericalmethod.suanshu.analysis.function.special.gamma;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.generic.matrixtype.RealMatrix;
import com.numericalmethod.suanshu.number.Real;
import static com.numericalmethod.suanshu.number.big.BigDecimalUtils.*;
import static com.numericalmethod.suanshu.number.big.BigIntegerUtils.combination;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The Lanczos approximation is a method for computing the Gamma function numerically, published by Cornelius Lanczos in 1964.
 * This implementation is based on the notes of Paul Godfrey and the discussion of Viktor T. Toth.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Lanczos_approximation">Wikipedia: Lanczos approximation</a>
 * <li><a href="http://my.fit.edu/~gabdo/gamma.txt">A note on the computation of the convergent Lanczos complex Gamma approximation.</a>
 * <li><a href="http://www.rskey.org/lanczos.htm">The Lanczos Approximation</a>
 * </ul>
 */
public class Lanczos {

    final int scale;
    final double g;
    final int n;
    final DenseMatrix P4double;
    final RealMatrix P;
    final RealMatrix B;
    final RealMatrix C;
    final RealMatrix D;
    final RealMatrix F;
//    final RealMatrix f;
//    final Real W;

    /**
     * Construct a Lanczos approximation instance.
     * <p/>
     * Recommended settings are
     * <blockquote><code>
     * g = 607.0 / 128.0;
     * n = 15;
     * scale = 30;
     * </code></blockquote>
     *
     * @param g     <i>g</i>
     * @param n     <i>n</i>
     * @param scale precision
     */
    public Lanczos(double g, int n, int scale) {
        super();
        this.g = g;
        this.n = n;
        this.scale = scale;
        this.B = B();
        this.C = C();
        this.D = D();
        this.F = F();
        this.P = P();
//        this.W = getW();
//        this.f = getf();

        this.P4double = P.doubleValue();
    }

    /**
     * Construct a Lanczos approximation instance using default parameters.
     * The default parameters are:
     * <blockquote><code>
     * g = 607.0 / 128.0;
     * n = 15;
     * scale = 30;
     * </code></blockquote>
     */
    public Lanczos() {
        this.g = 607.0 / 128.0;
        this.n = 15;
        this.scale = 30;
        this.B = null;
        this.C = null;
        this.D = null;
        this.F = null;

        //computed using scale = 60
        P = new RealMatrix(new double[][]{
                    {2.506628274630993212690713212902981043778231317934961102626888},
                    {143.269436391524834116310803080216408071593930149543550005657892},
                    {-149.389932537372300003892850647534301566462333369222299900934464},
                    {35.43394287644168623842616582313594168798594642709681523807824},
                    {-1.23304508011192900199205019045366385661386664593871973232992},
                    {0.00008521195083811379200391668425813572876701246542111017928},
                    {0.000116617443706980676361066723073155328757336705967386186176},
                    {-0.000246588241301200230325289044382556220863229632130949798112},
                    {0.000396269613403314676853385056732651422831369418070840216},
                    {-0.00052705479477514357592036799545594766914055170516840566356},
                    {0.00054504029479255910923352122293918539567195705680611877216},
                    {-0.000411884411878881395461043822782626553775540910172677774048},
                    {0.000211605107132058147537083645155915068665362000457772105024},
                    {-0.0000656506960736953574742190938202579770853525892573352756},
                    {0.00000924925345651558838630300519322709590667494407211468}
                });

        this.P4double = new DenseMatrix(new double[][]{
                    {2.506628274630993212690713212902981043778231317934961102626888},
                    {143.269436391524834116310803080216408071593930149543550005657892},
                    {-149.389932537372300003892850647534301566462333369222299900934464},
                    {35.43394287644168623842616582313594168798594642709681523807824},
                    {-1.23304508011192900199205019045366385661386664593871973232992},
                    {0.00008521195083811379200391668425813572876701246542111017928},
                    {0.000116617443706980676361066723073155328757336705967386186176},
                    {-0.000246588241301200230325289044382556220863229632130949798112},
                    {0.000396269613403314676853385056732651422831369418070840216},
                    {-0.00052705479477514357592036799545594766914055170516840566356},
                    {0.00054504029479255910923352122293918539567195705680611877216},
                    {-0.000411884411878881395461043822782626553775540910172677774048},
                    {0.000211605107132058147537083645155915068665362000457772105024},
                    {-0.0000656506960736953574742190938202579770853525892573352756},
                    {0.00000924925345651558838630300519322709590667494407211468}
                });
    }

    /**
     * Compute log-gamma for a <em>positive</em> value <i>x</i>.
     * All operations are in {@code double} precision.
     *
     * @param x <i>x</i> in {@code double}
     * @return {@code lgamma(x)} in {@code double}
     */
    public double logGammaQuick(double x) {//x > 0
        double z = x - 1;
        DenseMatrix Z = Z(z);
        Matrix ZP = Z.multiply(P4double);
        double term0 = z + g + 0.5;
        double term1 = Math.log(ZP.get(1, 1));
        double term2 = (z + 0.5) * Math.log(term0);
        double term3 = -term0;

        double result = term1 + term2 + term3;
        return result;
    }

    /**
     * <i>Z</i> is a <i>1 x n</i> matrix.
     *
     * @param z a real number
     * @return <i>Z</i>
     */
    DenseMatrix Z(double z) {
        DenseMatrix Z = new DenseMatrix(1, n);
        Z.set(1, 1, 1);
        for (int i = 1; i < n; ++i) {
            double v = 1.0 / (i + z);
            Z.set(1, i + 1, v);
        }
        return Z;
    }

    /**
     * Compute log-gamma for a <em>positive</em> value <i>x</i>.
     * The accuracy of this function is only as good as the precision of {@code double}.
     *
     * @param x <i>x</i> in {@code double}
     * @return {@code lgamma(x)} in {@code double}
     */
    public double logGamma(double x) {//x > 0
        BigDecimal result = logGamma(BigDecimal.valueOf(x));
        return result.doubleValue();
    }

    /**
     * Compute log-gamma for a <em>positive</em> value <i>x</i> to arbitrary precision.
     *
     * @param x <i>x</i> in {@code BigDecimal}
     * @return {@code lgamma(x)} in {@code BigDecimal}
     */
    public BigDecimal logGamma(BigDecimal x) {//x > 0
        BigDecimal z = x.add(BigDecimal.valueOf(-1));
        RealMatrix Z = Z(z);
        RealMatrix ZP = Z.multiply(P);
        BigDecimal term0 = z.add(sum(g, 0.5));
        BigDecimal term1 = log(ZP.get(1, 1).toBigDecimal(), scale);
        BigDecimal term2 = z.add(BigDecimal.valueOf(0.5)).multiply(log(term0));
        BigDecimal term3 = term0.negate();

        BigDecimal result = term1.add(term2).add(term3);
        return result;
    }

    /**
     * <i>Z</i> is a <i>1 x n</i> matrix.
     *
     * @param z a {@code BigDecimal}
     * @return <i>Z</i>
     */
    RealMatrix Z(BigDecimal z) {
        RealMatrix Z = new RealMatrix(1, n);
        Z.set(1, 1, Real.ONE);
        for (int i = 1; i < n; ++i) {
            BigDecimal v = BigDecimal.ONE;
            v = v.divide(sum(z, new BigDecimal(i)), scale, BigDecimal.ROUND_HALF_EVEN);//1.0 / (i + z)
            Z.set(1, i + 1, new Real(v));
        }
        return Z;
    }

    private RealMatrix B() {
        RealMatrix M = new RealMatrix(n, n);

        //the first row has all 1's
        for (int i = 0; i < n; ++i) {
            M.set(1, i + 1, Real.ONE);
        }

        for (int i = 1; i < n; ++i) {
            int sign = 1;
            for (int j = i; j < n; ++j) {
                Real value = new Real(combination(i + j - 1, j - i));
                M.set(i + 1, j + 1, sign == 1 ? value : value.opposite());
                sign *= -1;
            }
        }

        return M;
    }

    private RealMatrix C() {
        RealMatrix M = new RealMatrix(n, n);
        M.set(1, 1, new Real("0.5"));

        for (int i = 1; i < n; ++i) {
            int sign = i % 2 == 1 ? -1 : 1;

            for (int j = 0; j <= i; ++j) {
                BigInteger sum = BigInteger.ZERO;
                for (int k = 0; k <= i; ++k) {
                    BigInteger v1 = combination(2 * i, 2 * k);
                    BigInteger v2 = k + j - i >= 0 ? combination(k, k + j - i) : BigInteger.ZERO;
                    BigInteger v1v2 = v1.multiply(v2);
                    sum = sum.add(v1v2);
                }

                M.set(i + 1, j + 1, sign > 0 ? new Real(sum) : new Real(sum.negate()));
                sign *= -1;
            }
        }

        return M;
    }

    private RealMatrix D() {
        RealMatrix M = new RealMatrix(n, n);

        M.set(1, 1, Real.ONE);
        M.set(2, 2, Real.ONE.opposite());//-1

        for (int i = 2; i < n; ++i) {
            Real value = M.get(i, i);
            value = value.multiply(new Real(2 * (2 * i - 1)));
            value = value.divide(new Real(i - 1), scale);
            M.set(i + 1, i + 1, value);//M.get(i, i) * 2.0 * (2.0 * i - 1.0) / (i - 1.0);
        }

        return M;
    }

    private RealMatrix F() {
        RealMatrix M = new RealMatrix(n, 1);

        for (int i = 0; i < n; ++i) {
            BigDecimal v = BigDecimal.valueOf(2);//v = 2.0;
            for (int j = i + 1; j <= 2 * i; ++j) {
                v = v.multiply(BigDecimal.valueOf(j));//v *= j;
                v = v.divide(BigDecimal.valueOf(4), scale, BigDecimal.ROUND_HALF_EVEN);//v /= 4.0;
            }

            v = v.multiply(exp(sum(i, g, 0.5), scale));//v *= exp(i + g + 0.5);
            v = v.divide(pow(sum(i, g, 0.5), sum(i, 0.5), scale),
                         scale, BigDecimal.ROUND_HALF_EVEN);//v /= (i + g + 0.5) ^ (i + 0.5);

            M.set(i + 1, 1, new Real(v));
        }

        return M;
    }

    /** <i>P = DBCF</i> is an <i>n x 1</i> matrix. */
    private RealMatrix P() {
        RealMatrix R = D.multiply(B);
        R = R.multiply(C);
        R = R.multiply(F);

        return R;
    }

    private Real W() {
        BigDecimal e2g = exp(g, scale);//e^g
        BigDecimal root2pi = pow(PI.multiply(new BigDecimal("2")), new BigDecimal("0.5"), this.scale);//sqrt(2*pi)
        BigDecimal w = e2g.divide(root2pi, scale, BigDecimal.ROUND_HALF_EVEN);//e^g / sqrt(2*pi)
        return new Real(w);
    }

    private RealMatrix f() {
        RealMatrix M = new RealMatrix(n, 1);
        BigDecimal e = exp(1, scale);
        BigDecimal root2 = pow(BigDecimal.valueOf(2), BigDecimal.valueOf(0.5), this.scale);

        for (int i = 0; i < n; ++i) {
            BigDecimal denominator = BigDecimal.valueOf(2).multiply(sum(g, i)).add(BigDecimal.ONE);//(2 * (g + i) + 1)
            BigDecimal value = e.divide(denominator, scale, BigDecimal.ROUND_HALF_EVEN);//exp(1) / (2 * (g + i) + 1)
            value = pow(value, sum(0.5, i), scale);//(exp(1) / (2 * (g + i) + 1)) ^ (i + 0.5)
            value = root2.multiply(value);//root(2) * (exp(1) / (2 * (g + i) + 1)) ^ (i + 0.5)

            M.set(i + 1, 1, new Real(value));
        }

        return M;
    }
}
