package com.numericalmethod.suanshu.optimization.minmax;

import com.numericalmethod.suanshu.analysis.function.rn2r1.RealScalarFunction;
import com.numericalmethod.suanshu.analysis.function.rn2rm.RealVectorFunction;
import java.util.List;

/**
 * A minmax problem is a decision rule used in decision theory, game theory, statistics and philosophy for minimizing the possible loss while maximizing the potential gain.
 * Alternatively, it can be thought of as maximizing the minimum gain (maxmin).
 * Given a family of error functions, parameterized by <i>ω</i>, we try to minimize their maximum.
 *
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Minimax">Wikipedia: Minimax</a>
 */
public interface MinMaxProblem<T> {

    /**
     * <i>e(x, ω)</i> is the error function, or the minmax objective, for a given <i>ω</i>.
     *
     * @param omega a parameterization of a real scalar function
     * @return the error function for a given ω,
     * <i>e(x, ω)</i>
     */
    RealScalarFunction error(T omega);

    /**
     * <i>g(x, ω) = ∇|e(x, ω)|</i> is the gradient function of the <em>absolute</em> error, <i>|e(x, ω)|</i>, for a given <i>ω</i>.
     *
     * @param omega a parameterization of a real scalar function
     * @return <i>g<sub>ω</sub>(x)</i>, the gradient of the absolute error for a given <i>ω</i>
     */
    RealVectorFunction gradient(T omega);

    /**
     * Get the list of omegas, the domain.
     *
     * @return the set of omegas
     */
    List<T> getOmega();
}
