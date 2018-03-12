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
package com.numericalmethod.suanshu.algorithm.iterative;

/**
 * An iterative method is a mathematical procedure that generates a sequence of
 * improving approximate solutions for a class of problems.
 * A specific implementation of an iterative method, including the termination
 * criteria, is an algorithm of the iterative method.
 * <p/>
 * This interface defines the structure of an iterative algorithm: initials,
 * iteration step, and convergence criterion.
 *
 * @param <S> the solution type
 * @author Haksun Li
 * @see <a href="http://en.wikipedia.org/wiki/Iterative_method">Wikipedia: Iterative method</a>
 */
public interface IterativeMethod<S> {

    /**
     * Supply the starting points for the search.
     * This can also initialize the state of the algorithm for a new search.
     *
     * @param initials the initial guesses
     */
    public void setInitials(S... initials);

    /**
     * Do the next iteration.
     *
     * @return the information about this step
     * @throws Exception when an error occurs during the search
     */
    public Object step() throws Exception;

    /**
     * Search for a solution that optimizes the objective function from the
     * given starting points.
     * This method typically calls first {@link #setInitials(S[]) } and then
     * iteratively {@link #step()}.
     * It implements a default convergence criterion.
     *
     * @param initials the initial guesses
     * @return an (approximate) optimizer
     * @throws Exception when an error occurs during the search
     */
    public S search(S... initials) throws Exception;//some solvers, e.g., SQP, throw exception
}
