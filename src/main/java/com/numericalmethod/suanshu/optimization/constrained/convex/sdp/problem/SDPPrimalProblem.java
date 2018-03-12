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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.problem;

import com.numericalmethod.suanshu.matrix.doubles.matrixtype.dense.triangle.SymmetricMatrix;
import static com.numericalmethod.suanshu.misc.SuanShuUtils.assertArgument;
import java.util.ArrayList;

/**
 * A Primal SDP problem, as in eq. 14.1 in the reference, takes the following form.
 * \[
 * \min_x \mathbf{c'x} \textrm{, s.t., } \\
 * \mathbf{Ax} = \mathbf{b}, \mathbf{x} \geq \mathbf{0}
 * \]
 *
 * @author Haksun Li
 * @see "Andreas Antoniou, Wu-Sheng Lu, "Section 14.2, Primal and Dual SDP Problems," Practical Optimization: Algorithms and Engineering Applications."
 */
public class SDPPrimalProblem {//TODO: convert to SDPDualProblem; implement ConstrainedOptimProblem

    private final int n;//the dimension of the matrices
    private final SymmetricMatrix C;
    private final ArrayList<SymmetricMatrix> A = new ArrayList<SymmetricMatrix>();

    /**
     * Construct a primal SDP problem.
     * \[
     * \min_x \mathbf{c'x} \textrm{, s.t., } \\
     * \mathbf{Ax} = \mathbf{b}, \mathbf{x} \geq \mathbf{0}
     * \]
     *
     * @param C \(C\)
     * @param A \(A\)
     */
    public SDPPrimalProblem(SymmetricMatrix C, SymmetricMatrix[] A) {
        this.n = C.nRows();

        this.C = new SymmetricMatrix(C);
        assertArgument(n == C.nRows(), "C must have the same dimension as all matrices");

        for (SymmetricMatrix a : A) {
            assertArgument(n == a.nRows(), "all matrices must have the same dimension");
            this.A.add(new SymmetricMatrix(a));
        }
    }

    /**
     * Get the dimension of the system, i.e., the dimension of <i>x</i>, the number of variables.
     *
     * @return the dimension of the system
     */
    public int n() {
        return n;
    }

    /**
     * Get the size of <i>b</i>.
     *
     * @return the size of <i>b</i>
     */
    public int p() {
        return A.size();
    }

    /**
     * Get <i>C</i>.
     *
     * @return <i>C</i>
     */
    public SymmetricMatrix C() {
        return new SymmetricMatrix(C);
    }

    /**
     * Get <i>A<sub>i</sub></i>.
     *
     * @param i an index to the A's, counting from 1
     * @return <i>A<sub>i</sub></i>
     */
    public SymmetricMatrix A(int i) {
        return new SymmetricMatrix(A.get(i - 1));
    }
}
