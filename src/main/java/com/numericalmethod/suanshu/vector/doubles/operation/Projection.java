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
package com.numericalmethod.suanshu.vector.doubles.operation;

import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project a vector <i>v</i> on another vector <i>w</i> or a set of vectors (basis) <i>{w<sub>i</sub>}</i>.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Vector_projection">Wikipedia: Vector projection</a>
 */
public class Projection {

    private final Vector orthogonalVector;
    private final double[] length;
    private final Vector[] projVector;
    private final List<Vector> basis;

    /**
     * Project a vector <i>v</i> onto a set of basis <i>{w<sub>i</sub>}</i>.
     *
     * @param v     a vector
     * @param basis <i>{w<sub>i</sub>}</i>
     */
    public Projection(Vector v, List<Vector> basis) {
        this.basis = new ArrayList<Vector>();
        this.basis.addAll(basis);

        int size = basis.size();
        this.length = new double[size];
        this.projVector = new Vector[size];

        Vector vv = v.deepCopy();
        for (int i = 0; i < size; ++i) {
            Vector w = basis.get(i);
            if (w != null) {
                this.length[i] = vv.innerProduct(w);
                this.projVector[i] = w.scaled(this.length[i]);
                vv = vv.minus(this.projVector[i]);
            }
        }

        this.orthogonalVector = vv;
    }

    /**
     * Project a vector <i>v</i> onto a set of basis <i>{w<sub>i</sub>}</i>.
     *
     * @param v     a vector
     * @param basis <i>{w<sub>i</sub>}</i>
     */
    public Projection(Vector v, Vector[] basis) {
        this(v, Arrays.asList(basis));
    }

    /**
     * Project a vector <i>v</i> onto another vector.
     *
     * @param v a vector
     * @param w another vector
     */
    public Projection(Vector v, Vector w) {
        this.basis = new ArrayList<Vector>();
        this.basis.add(w);

        this.length = new double[1];
        this.projVector = new Vector[1];

        Vector vv = v;
        this.length[0] = vv.innerProduct(w);
        this.projVector[0] = w.scaled(length[0]);
        vv = vv.minus(projVector[0]);

        this.orthogonalVector = vv;
    }

    /**
     * Get the orthogonal vector which is equal to <i>v</i> minus the projection of <i>v</i> on <i>{w<sub>i</sub>}</i>.
     *
     * @return the orthogonal vector
     */
    public ImmutableVector getOrthogonalVector() {
        return new ImmutableVector(orthogonalVector);
    }

    /**
     * Get the length of <i>v</i> projected on each dimension <i>{w<sub>i</sub>}</i>.
     *
     * @param i an index, counting from 0
     * @return the <i>i</i>-th projection length
     */
    public double getProjectionLength(int i) {
        return length[i];
    }

    /**
     * Get the <i>i</i>-th projected vector of <i>v</i> on <i>{w<sub>i</sub>}</i>.
     * It lies on the hyperplane of <i>{w<sub>i</sub>}</i>.
     *
     * @param i an index, counting from 0
     * @return the <i>i</i>-th projection vector
     */
    public ImmutableVector getProjectionVector(int i) {
        return new ImmutableVector(projVector[i]);
    }
}
