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
package com.numericalmethod.suanshu.stats.stochasticprocess.multivariate.sde;

import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;
import com.numericalmethod.suanshu.DeepCopyable;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import static java.lang.Math.sqrt;

/**
 * This represents the concept 'Filtration', the information available at time <i>t</i>.
 *
 * <p>
 * The information may include (subject to implementation), for example,
 * <ul>
 * <li>time
 * <li>value of the stochastic process
 * <li>values of the driving Brownian motion(s)
 * </ul>
 *
 * @author Haksun Li
 *
 * @see "Fima C. Klebaner. Introduction to Stochastic Calculus with Applications. 2nd ed. pp.23. Imperial College Press. 2006."
 */
public class Ft implements DeepCopyable {

    /**
     * the time differential
     */
    protected double dt;
    /**
     * the value of the stochastic process at time <i>t</i>
     */
    protected Vector Xt;
    /**
     * a sampling from the Gaussian distribution
     */
    protected Vector Zt;

    /**
     * Construct an empty filtration (no information).
     */
    public Ft() {
    }

    /**
     * Copy constructor.
     *
     * @param that another <tt>Ft</tt>
     */
    public Ft(Ft that) {
        this.dt = that.dt;
        this.Xt = new DenseVector(that.Xt);
        this.Zt = new DenseVector(that.Zt);
    }

    public Ft deepCopy() {
        Ft ft = new Ft(this);
        return ft;
    }

    /**
     * Get the dimension of the process.
     *
     * @return the dimension of the process
     */
    public int dim() {
        return Xt.size();
    }

    /**
     * Get the number of independent Brownian motions.
     *
     * @return the number of independent Brownian motions
     */
    public int nB() {
        return Zt.size();
    }

    /**
     * Set the current time differential.
     * 
     * @param dt the time differential
     */
    public void setDt(double dt) {
        this.dt = dt;
    }

    /**
     * Get the current time differential.
     *
     * @return the time differential
     */
    public double dt() {
        return this.dt;
    }

    /**
     * Set the current value of the stochastic process.
     *
     * @param Xt the current value of the stochastic process
     */
    public void setXt(Vector Xt) {
        this.Xt = Xt;
    }

    /**
     * Get the current value of the stochastic process.
     *
     * @return the current value of the stochastic process
     */
    public Vector Xt() {
        return this.Xt;
    }

    /**
     * Set the value of the Gaussian distribution innovation.
     *
     * @param Zt the Gaussian distribution innovation
     */
    public void setZt(Vector Zt) {
        this.Zt = Zt;
    }

    /**
     * Get the current value of the Gaussian distribution innovation.
     *
     * @return the current Gaussian distribution innovation
     */
    public Vector Zt() {
        return this.Zt;
    }

    /**
     * Get the increment of the driving Brownian motion during the time differential.
     *
     * <p>
     * This is the product of the Gaussian distribution innovation and the square root of the time differential.
     *
     * @return the increment of the driving Brownian motion during the time differential
     */
    public Vector dWt() {
        return Zt.scaled(sqrt(dt));
    }
}
