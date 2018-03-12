package com.numericalmethod.suanshu.optimization.constrained.general.penaltymethod;

import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * This is a dummy zero cost (no cost) penalty function.
 */
public class ZERO extends PenaltyFunction {

    private int dimension;

    /**
     * Construct a no-cost penalty function.
     *
     * @param dimension the problem dimension
     */
    public ZERO(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public Double evaluate(Vector x) {
        return 0.;
    }

    @Override
    public int dimensionOfDomain() {
        return dimension;
    }
}
