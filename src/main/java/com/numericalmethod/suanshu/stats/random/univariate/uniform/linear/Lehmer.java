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
package com.numericalmethod.suanshu.stats.random.univariate.uniform.linear;

import com.numericalmethod.suanshu.analysis.function.FunctionOps;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Lehmer proposed a general linear congruential generator that generates pseudo-random numbers in [0, 1].
 * It has this form:
 * <blockquote><pre><i>
 * x<sub>i+1</sub> = (a * x<sub>i</sub> + c) mod m
 * u<sub>i+1</sub> = x<sub>i+1</sub> / m
 * </i></pre></blockquote>
 * We take <i>c</i> to be 0 because Marsaglia shows that there is little additional generality when <i>c ≠ 0</i>.
 * There are restrictions placed on the selection of <i>(a, m)</i> and the seed.
 * For example,
 * <ul>
 * <li>the seed must be co-prime to <i>m</i>;
 * <li>the modulus <i>m</i> is a prime number or a power of a prime number;
 * <li>the multiplier <i>a</i> is an element of high multiplicative order modulo <i>m</i>
 * </ul>
 * This implementation is essentially doing what {@code Random.next(int)} is doing (for a specific pair <i>a</i> and <i>m</i>),
 * but it computes <i>(ax mod m)</i> in integer arithmetic without overflow under certain conditions.
 * In addition, it allows customized multiplier and modulus.
 * This class is the most fundamental building block for <em>all linear</em> random number generation algorithms in this library.
 *
 * @author Haksun Li
 * @see
 * <ul>
 * <li>"Lehmer, D.H. (1951) Mathematical methods in large-scale computing units, p.141-146. Proceedings of the Second Symposium on Large Scale Digital Computing Machinery. Harvard University Press, Cambridge, Mass."
 * <li><a href="http://en.wikipedia.org/wiki/Lehmer_random_number_generator">Wikipedia: Lehmer random number generator</a>
 * </ul>
 */
public class Lehmer implements LinearCongruentialGenerator {

    /** the multiplier */
    private final long a;
    /** the modulus */
    private final long m;
    /** m / a */
    private final long q;
    /** m % a */
    private final long r;
    private AtomicLong x = new AtomicLong();//the current random value

    /**
     * Construct a Lehmer (pure) linear congruential generator.
     * Suggested values are:
     * <ul>
     * <li> m = 2<sup>31</sup> - 1 = 2147483647; a = 16807 (inferior to the other 3)
     * <li> m = 2<sup>31</sup> - 1 = 2147483647; a = 39373
     * <li> m = 2147483399; a = 40692
     * <li> m = 2147483563; a = 40014
     * </ul>
     * This implementation computes the next random number in <em>long</em> arithmetic without overflow.
     * It is based on L'Ecuyer, P. (1988).
     * Note that <i>a</i> cannot be too big.
     *
     * @param a    the multiplier
     * @param m    the modulus
     * @param seed the seed. It should <em>not</em> be zero.
     * @see
     * <ul>
     * <li>"Paul Glasserman, Monte Carlo Methods in Financial Engineering, 2004."
     * <li>"P. L'Ecuyer, "Efficient and portable combined random number generators," Communications of the ACM 31:742-749, 774, Correspondence 32:1019-1024, 1988."
     * </ul>
     */
    public Lehmer(long a, long m, long seed) {
        SuanShuUtils.assertArgument(a > 0 && m > 0, "a and m must be positive");

        this.a = a;
        this.m = m;
        this.q = m / a;
        this.r = m % a;

        SuanShuUtils.assertArgument(r <= q, "the (a, m) combination is not valid; try a <= sqrt(m)");

        seed(seed);
    }

    /**
     * Construct a skipping ahead Lehmer (pure) linear congruential generator.
     * The pseudo-random sequence is a subset of the original Lehmer sequence,
     * taking every <i>k</i> value.
     * Equivalently, this call is the same as
     * <blockquote><code>
     * Lehmer((a^k)%m, m, seed)
     * </code></blockquote>
     * This implementation computes <i>(a^k)%m</i> more efficiently.
     * Note that <i>a</i> cannot be too big.
     *
     * @param a    the multiplier
     * @param m    the modulus
     * @param k    the exponent
     * @param seed the seed. It should <em>not</em> be zero.
     */
    public Lehmer(long a, long m, long k, long seed) {
        this(FunctionOps.modpow(a, k, m), m, seed);
    }

    /**
     * Construct a Lehmer (pure) linear congruential generator.
     */
    public Lehmer() {
        this(40014, 2147483563, 8682522807148012L + System.nanoTime());
    }

    @Override
    public void seed(long... seeds) {
        x.set(seeds[0] % m);
    }

    @Override
    public int order() {
        return 1;
    }

    @Override
    public long modulus() {
        return m;
    }

    /**
     * <em>All</em> built-in <em>linear</em> random number generators in this library
     * ultimately call this function to generate random numbers.
     * This particular function is thus made thread safe using non-blocking synchronization.
     * This in turn ensures thread-safety for all these rngs.
     * If you are to write your own rng, you should either call this function,
     * or have your own synchronization mechanism.
     *
     * @return a random a {@code long} number
     * @see "Brian Goetz, Tim Peierls, Joshua Bloch and Joseph Bowbeer. Java Concurrency in Practice."
     */
    @Override
    public long nextLong() {
        long xOld, xNew;
        do {
            xOld = x.get();
            xNew = xOld;

            long k = xNew / q;
            xNew = a * (xNew - k * q) - k * r;
            if (xNew < 0) {
                xNew += m;
            }
        } while (!x.compareAndSet(xOld, xNew));

        return xNew;
    }

    @Override
    public double nextDouble() {
        return ((double) nextLong()) / ((double) m);
    }
}
