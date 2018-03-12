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
package com.numericalmethod.suanshu.stats.markovchain;

import com.numericalmethod.suanshu.matrix.doubles.ImmutableMatrix;
import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.DenseMatrix;
import com.numericalmethod.suanshu.matrix.doubles.operation.Inverse;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import com.numericalmethod.suanshu.stats.random.multivariate.MultinomialRvg;
import com.numericalmethod.suanshu.stats.random.univariate.RandomNumberGenerator;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is a time-homogeneous Markov chain with a finite state space.
 * It is a mathematical system that undergoes transitions from one state to another,
 * between a finite or countable number of possible states.
 * It is a random process characterized as memoryless:
 * the next state depends only on the current state and not on the sequence of events that preceded it.
 * This specific kind of "memorylessness" is called the Markov property.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Markov_chain">Wikipedia: Markov chain</a>
 */
public class SimpleMC implements RandomNumberGenerator {

    /** the initial state probabilities */
    private final ImmutableVector PI;
    /** the state transition probabilities */
    private final ImmutableMatrix A;
    private int qt_1 = -1;//q[t-1] = the last state
    private MultinomialRvg[] multinomial;

    /**
     * Construct a time-homogeneous Markov chain with a finite state space.
     *
     * @param PI the initial state probabilities
     * @param A  the state transition probabilities
     */
    public SimpleMC(Vector PI, Matrix A) {
        int N = A.nRows(); //the number of states

        //check input arguments
        assertArgument(PI.size() == N, "the length of PI should be the same as the number of states");
        assertArgument(A.nCols() == N, "A should be a square matrix");

        this.PI = new ImmutableVector(PI);
        this.A = new ImmutableMatrix(A);

        this.multinomial = new MultinomialRvg[1 + A.nRows()];
        this.multinomial[0] = new MultinomialRvg(1, PI.toArray());
        for (int i = 1; i < multinomial.length; ++i) {
            this.multinomial[i] = new MultinomialRvg(1, A.getRow(i).toArray());
        }
    }

    /**
     * Construct a time-homogeneous Markov chain with a finite state space using stationary state probabilities.
     *
     * @param A the state transition probabilities
     */
    public SimpleMC(Matrix A) {
        this(getStationaryProbabilities(A), A);
    }

    @Override
    public void seed(long... seeds) {
        for (int i = 0; i < multinomial.length; ++i) {
            multinomial[i].seed(seeds);
        }
    }

    /**
     * Get the next simulated state.
     *
     * @return next state
     */
    @Override
    public double nextDouble() {
        return nextState();
    }

    /**
     * Get the next simulated state.
     *
     * @return next state
     */
    public int nextState() {
        //simulate state
        MultinomialRvg rvg = multinomial[0];//for initial state

        if (qt_1 != -1) {
            rvg = multinomial[qt_1];
        }

        int qt = bin(rvg);//TODO: how to seed rvg?

        //update
        qt_1 = qt;

        return qt;
    }

    /**
     * Get the initial state probabilities.
     *
     * @return the initial state probabilities
     */
    public ImmutableVector PI() {
        return PI;
    }

    /**
     * Get the state transition probabilities.
     *
     * @return the state transition probabilities
     */
    public ImmutableMatrix A() {
        return A;
    }

    /**
     * Get the number of states.
     *
     * @return the number of states
     */
    public int nStates() {
        return A.nRows();
    }

    /**
     * Get the stationary state probabilities of a Markov chain
     * that is irreducible, aperiodic and strongly connected (positive recurrent).
     * A stationary distribution vector <i>x</i> (if exists) satisfies
     * \[
     * x' \times A = x'
     * \]
     * where <i>x'</i> denotes the transpose of <i>x</i>, and the sum of all elements of <i>x</i> is 1.
     *
     * @param A the transition matrix
     * @return the stationary state probabilities
     */
    public static Vector getStationaryProbabilities(Matrix A) {
        int dim = A.nRows(); //the dimension of A

        //the stationary distribution x satisfies (A' - I) * x = 0; however the last row is redundant
        Matrix B = A.t().minus(A.ONE()); //B = A' - I
        ((DenseMatrix) B).setRow(dim, new DenseVector(dim, 1.)); //set the last row of B to (1, ..., 1)

        Vector b = new DenseVector(dim, 0);
        b.set(dim, 1.); //b = (0, ..., 0, 1)

        /*
         * The stationary distribution x satisfies B * x = b, since the sum of all elements of x should be 1.
         * B * x = b implies x = B^{-1} * b
         */
        Vector x = new Inverse(B).multiply(b);

        return x;
    }

    /**
     * Pick the first non-empty bin.
     *
     * @param rvg a {@link MultinomialRvg}
     * @return the first non-empty bin
     */
    public static int bin(MultinomialRvg rvg) {
        double[] bin_t = rvg.nextVector();

        int i = 1;
        for (; bin_t[i - 1] == 0; ++i) {
        }

        return i;
    }
}
