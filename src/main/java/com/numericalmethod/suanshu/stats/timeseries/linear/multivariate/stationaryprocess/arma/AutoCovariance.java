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
package com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.stationaryprocess.arma;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LSProblem;
import com.numericalmethod.suanshu.matrix.doubles.linearsystem.LUSolver;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import com.numericalmethod.suanshu.number.DoubleUtils;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.AutoCovarianceFunction;
import com.numericalmethod.suanshu.stats.timeseries.linear.multivariate.arima.ARIMAModel;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import java.util.Arrays;

/**
 * Compute the Auto-CoVariance Function (ACVF) for a vector AutoRegressive Moving Average (ARMA) model, assuming that
 * EX<sub>t</sub> = 0.
 *
 * <p>
 * This implementation solves the Yule-Walker equation.
 *
 * <p>
 * The R equivalent function are {@code ARMAacf} and {@code TacvfAR} in package {@code FitAR}.
 *
 * @author Haksun Li, Kevin Sun
 *
 * @see "P. J. Brockwell and R. A. Davis, "p. 420. Eq. 11.3.15. The Covariance Matrix Function of a Causal ARMA Process. Chapter 11.3. Multivariate Time Series," in <i>Time Series: Theory and Methods</i>, Springer, 2006."
 */
public class AutoCovariance extends AutoCovarianceFunction {

    /**
     * the number of lags in the result
     */
    public final int nLags;
    /**
     * the estimated ACVF
     */
    private final Matrix[] ACVF;
    private final int m;
    private final int p;
    private final int q;
    private final int p1mm;
    private LinearRepresentation linearRep;

    /** 
     * Compute the auto-covariance function of a vector ARMA model.
     *
     * <p>
     * To solve Eq. 11.3.15, we "expand" the (p+1) matrix equations into (p+1)*m*m linear equations.
     * m is the dimension of Gamma (ACVF).
     * 
     * @param model an ARIMA specification
     * @param nLags the number of lags in the result
     */
    public AutoCovariance(ARIMAModel model, int nLags) {
        SuanShuUtils.assertArgument(DoubleUtils.isZero(model.mu().norm(), 0), "EX<sub>t</sub> = 0");

        this.nLags = nLags;

        m = model.dimension();
        p = model.p();
        q = model.q();
        p1mm = (p + 1) * m * m;

        linearRep = new LinearRepresentation(model.getArma(), nLags);

        Matrix[] B = B(model, model.sigma(), nLags);
        Vector b = b(B);

        Matrix A = A(model);

        LUSolver solver = new LUSolver();
        Vector x = solver.solve(new LSProblem(A, b));

        //parse x to get the ACVF
        ACVF = getACVF(model, model.sigma(), x.toArray());
    }

    private Matrix[] getACVF(ARIMAModel model, Matrix wnVariance, double[] x) {
        Matrix[] ACVF = new Matrix[nLags + 1];

        for (int i = 0; i <= p; ++i) {
            double[] data = Arrays.copyOfRange(x, i * m * m, (i + 1) * m * m);
            ACVF[i] = new DenseMatrix(data, m, m);
        }

        for (int j = p + 1; j <= nLags; ++j) {
            ACVF[j] = new DenseMatrix(m, m).ZERO();

            for (int r = 1; r <= p; ++r) {
                Matrix term = model.AR(r).multiply(ACVF[j - r]);
                ACVF[j] = ACVF[j].add(term);
            }

            Matrix[] PSI = linearRep.PSI;
            for (int r = j; r <= q; ++r) {
                Matrix term = model.MA(r).multiply(wnVariance).multiply(PSI[r - j].t());
                ACVF[j] = ACVF[j].add(term);
            }
        }

        return ACVF;
    }

    /**
     * Expand the (p+1) matrix equations into one big linear system.
     *
     * @param model
     * @return
     */
    private Matrix A(ARIMAModel model) {
        Matrix A = new DenseMatrix(p1mm, p1mm).ZERO();

        //each entry in b (or B's) corresponds to one linear equation of Gamma entries, indexed by u
        int u = 0;
        for (int i = 0; i <= p; ++i) {//B's; the original matrix equations
            for (int j = 1; j <= m; ++j) {//by row
                for (int k = 1; k <= m; ++k) {//by column
                    ++u;//filling the u-th row of A; the u-th linear equation in the "expanded" system

                    for (int r = 0; r <= p; ++r) {//the (p+1) terms in a matrix equation
                        boolean transposed = r > i ? true : false;//the latter gamma terms are transposed
                        int g = Math.abs(i - r);//the index to the gamma variable to be solved, Γ(g); Γ(-g) = Γ(g).t()
                        Matrix phi = transposed ? model.AR(r).t() : model.AR(r);

                        //the k column in Γ(g) or Γ(g).t() are the variables
                        //the j row in phi's are the coefficients
                        for (int s = 1; s <= m; ++s) {//going thru the row/column in the phi and Γ
                            double coeff = r == 0 ? phi.get(j, s) : -phi.get(j, s);
                            int v = colA(g, s, k, transposed);//the index to a variable in {x}; the column in A
                            A.set(u, v, A.get(u, v) + coeff);
                        }
                    }
                }
            }
        }

        return A;
    }

    /**
     *
     * @param g index to Γ
     * @param row row index in a Γ
     * @param col column index in a Γ
     * @param transposed indicate to use Γ(g) or Γ(g).t()
     * @return
     */
    private int colA(int g, int row, int col, boolean transposed) {
        if (transposed) {
            return colA(g, col, row, false);
        }

        int result = g * m * m;//number of Γ before the g-th Γ
        result += (row - 1) * m;//number of rows before the k-th row in this Γ
        result += col;
        return result;
    }

    /**
     * Expand the B's into a (p+1)*m*m vector (equations) by rows.
     *
     * @param B
     * @return
     */
    private Vector b(Matrix[] B) {
        Vector b = new DenseVector(p1mm);
        int bi = 0;//index for b
        for (int i = 0; i <= p; ++i) {//B's
            for (int j = 1; j <= m; ++j) {//by row
                for (int k = 1; k <= m; ++k) {//by column
                    b.set(++bi, B[i].get(j, k));
                }
            }
        }

        return b;
    }

    /**
     * Compute the RHS of Eq. 11.3.15.
     *
     * @param model
     * @param wnVariance
     * @param nLags
     * @return
     */
    private Matrix[] B(ARIMAModel model, Matrix wnVariance, int nLags) {
        Matrix[] B = new Matrix[p + 1];

        Matrix[] PSI = linearRep.PSI;

        for (int i = 0; i <= p; ++i) {//p+1 of them
            B[i] = new DenseMatrix(m, m).ZERO();
            for (int r = i; r <= q; ++r) {
                Matrix term1 = model.MA(r).multiply(wnVariance).multiply(PSI[r - i].t());
                B[i] = B[i].add(term1);
            }
        }

        return B;
    }

    @Override
    public Matrix evaluate(double x1, double x2) {
        return evaluate(Math.abs(x2 - x1));
    }

    /**
     * Get the i-th auto-covariance matrix.
     *
     * @param i the lag
     * @return the i-th auto-covariance matrix
     */
    public Matrix evaluate(double i) {
        if (i < 0) {
            return evaluate(-i).t();//p. 420
        }

        if (i <= nLags) {
            return ACVF[(int) i];
        }

        throw new RuntimeException(String.format("the maximum number of lags available is %d", nLags));
    }
}
