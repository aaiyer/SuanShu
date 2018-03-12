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

import com.numericalmethod.suanshu.vector.doubles.Vector;
import com.numericalmethod.suanshu.vector.doubles.dense.DenseVector;

/**
 * This is a filtration implementation that includes the path-dependent information,
 * e.g., <i>W<sub>t</sub></i>.
 *
 * @author Haksun Li
 *
 * @see Ft
 */
public class FtWt extends Ft {

    /**
     * the current time
     */
    private double t = 0;
    /**
     * the current value(s) of the driving Brownian motion(s)
     */
    private Vector Wt = null;

    /**
     * Construct an empty filtration (no information).
     */
    public FtWt() {
    }

    /**
     * Copy constructor.
     *
     * @param that another <tt>Ft</tt>
     */
    public FtWt(FtWt that) {
        super(that);
        this.t = that.t;
        this.Wt = new DenseVector(that.Wt);
    }

    @Override
    public FtWt deepCopy() {
        FtWt ftWt = new FtWt(this);
        return ftWt;
    }

    /**
     * Get the current time.
     *
     * @return the current time
     */
    public double t() {
        return t;
    }

    /**
     * Get the current value(s) of the driving Brownian motion(s).
     *
     * @return the current value(s) of the driving Brownian motion(s)
     */
    public Vector Wt() {
        return Wt;
    }

    @Override
    public void setDt(double dt) {
        super.setDt(dt);
        t += dt;
    }

    @Override
    public void setZt(Vector Zt) {
        super.setZt(Zt);
        Wt = Wt == null ? dWt() : Wt.add(dWt());
    }
}
