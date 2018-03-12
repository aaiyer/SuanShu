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
package com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.solution;

import com.numericalmethod.suanshu.matrix.doubles.AreMatrices;
import com.numericalmethod.suanshu.misc.SuanShuUtils;
import static com.numericalmethod.suanshu.number.DoubleUtils.compare;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.SimplexTable;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting.SimplexPivoting;
import com.numericalmethod.suanshu.optimization.constrained.convex.sdp.socp.qp.lp.simplex.pivoting.SmallestSubscriptRule;
import com.numericalmethod.suanshu.vector.doubles.ImmutableVector;
import com.numericalmethod.suanshu.vector.doubles.Vector;
import java.util.HashSet;
import java.util.Set;
import javax.management.RuntimeErrorException;

/**
 * This is the solution to a bounded linear programming problem.
 * Note that there may be multiple solutions that give the same minimum.
 * This implementation returns all possible optimal solutions.
 *
 * @author Haksun Li
 */
public class LPBoundedMinimizer implements LPSimplexMinimizer {

    private final Set<MyImmutableVector> minimizers = new HashSet<MyImmutableVector>();
    private final SimplexTable table;
    private final double epsilon;

    //<editor-fold defaultstate="collapsed" desc="a way to compare two vectors up to an epsilon">
    /**
     * Since we use {@link Set} to make sure uniqueness of the minimizers, we need a way to compare two vectors.
     */
    private class MyImmutableVector extends ImmutableVector {

        MyImmutableVector(Vector vector) {
            super(vector);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Vector)) {
                return false;
            }
            final Vector that = (Vector) obj;

            if (!AreMatrices.equal(this, that, epsilon)) {//we use approximation comparison to avoid rounding errors and etc
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }
    }
    //</editor-fold>

    /**
     * Construct the solution for a bounded linear programming problem.
     *
     * @param table the solution simplex table, e.g., as a result of phase 2
     */
    public LPBoundedMinimizer(SimplexTable table) {
        this.table = new SimplexTable(table);
        this.epsilon = SuanShuUtils.autoEpsilon(table.toMatrix());

        MyImmutableVector minimizer = new MyImmutableVector(this.table.minimizer());
        this.minimizers.add(minimizer);//there is at least one minimizer
        int nOptimal = 1 + computeOptimalSolutions(table);
        SuanShuUtils.assertOrThrow(nOptimal == minimizers.size() ? null : new RuntimeErrorException(new Error("inconsistent logic")));
    }

    @Override
    public SimplexTable getResultantTableau() {
        return new SimplexTable(table);
    }

    @Override
    public double minimum() {
        return table.minimum();
    }

    @Override
    public ImmutableVector minimizer() {
        return minimizers.toArray(new ImmutableVector[0])[0];//always return the first one
    }

    /**
     * Get all optimal minimizers.
     *
     * @return all optimal minimizers
     */
    public ImmutableVector[] minimizers() {
        return minimizers.toArray(new ImmutableVector[0]);
    }

    /**
     * This computes the optimal minimizers by checking all possible pivoting.
     * It is a brute force exhaustive search.
     *
     * @param root the root simplex table, giving the first solution
     * @see "Michael C. Ferris, Olvi L. Mangasarian, Stephen J. Wright, "p.57 - 59," Linear Programming with MATLAB"
     */
    private int computeOptimalSolutions(SimplexTable root) {//TODO: can the code be more efficient?
        int found = 0;//a solution is already added

        for (int s = 1; s < root.nCols(); ++s) {
            if (compare(root.getCostRow(s), 0, epsilon) != 0) {
                continue;
            }

            if (root.getColLabel(s).type == SimplexTable.LabelType.EQUALITY) {
                continue;
            }

            SimplexTable copy = new SimplexTable(root);
            SimplexPivoting pivoting = new SmallestSubscriptRule();
            int r = pivoting.ratioTest(copy, s);

            if (r == 0) {
                continue;
            }

            copy = copy.swap(r, s);

            if (!copy.isFeasible()) {//TODO: is this necessary
                continue;
            }

            MyImmutableVector v = new MyImmutableVector(copy.minimizer());

            if (!minimizers.contains(v)) {//new minimizer found
                minimizers.add(v);
                ++found;
                found += computeOptimalSolutions(copy);
            }
        }

        return found;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("size = %d\n", minimizers.size()));

        for (Vector v : minimizers) {
            str.append(String.format("%s\n", v));
        }

        return str.toString();
    }
}
