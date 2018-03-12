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
package com.numericalmethod.suanshu.stats.pca;

import com.numericalmethod.suanshu.matrix.doubles.Matrix;
import com.numericalmethod.suanshu.vector.doubles.Vector;

/**
 * Principal Component Analysis (PCA) is a mathematical procedure that
 * uses an orthogonal transformation to convert a set of observations of possibly correlated variables into
 * a set of values of uncorrelated variables called principal components.
 * The number of principal components is less than or equal to the number of original variables.
 * This transformation is defined in such a way that the first principal component has as high a variance as possible
 * (that is, accounts for as much of the variability in the data as possible),
 * and each succeeding component in turn has the highest variance possible under the constraint that
 * it be orthogonal to (uncorrelated with) the preceding components.
 * Principal components are guaranteed to be independent only if the data set is jointly normally distributed.
 *
 * @author Kevin Sun
 *
 * @see
 * <ul>
 * <li>K. V. Mardia, J. T. Kent and J. M. Bibby, "Multivariate Analysis," London, Academic Press, 1979.
 * <li>W. N. Venables and B. D. Ripley, "Modern Applied Statistics with S," New York, Springer-Verlag, 2002.
 * <li><a href="http://en.wikipedia.org/wiki/Principal_component_analysis">Wikipedia: Principal component analysis</a>
 * </ul>
 */
public interface PCA {

    /**
     * Get the number of observations in the original data; sample size.
     *
     * @return nObs, the number of observations in the original data
     */
    public int nObs();

    /**
     * Get the number of variables in the original data.
     *
     * @return nFactors, the number of variables in the original data
     */
    public int nFactors();

    /**
     * Get the sample means that were subtracted.
     *
     * @return the sample means of each variable in the original data
     */
    public Vector mean();

    /**
     * Get the scalings applied to each variable.
     *
     * @return the scalings applied to each variable in the original data
     */
    public Vector scale();

    /**
     * Get the (possibly centered and/or scaled) data matrix X used for the PCA.
     *
     * @return the (possibly centered and/or scaled) data matrix X
     */
    public Matrix X();

    /**
     * Get the standard deviations of the principal components.
     *
     * @return the standard deviations of the principal components
     */
    public Vector sdPrincipalComponent();

    /**
     * Get the standard deviation of the i-th principal component.
     *
     * @param i an index, counting from 1
     * @return the standard deviation of the i-th principal component.
     */
    public double sdPrincipalComponent(int i);

    /**
     * Get the matrix of variable loadings.
     * The signs of the columns of the loading are arbitrary.
     *
     * @return the matrix of variable loadings
     */
    public Matrix loadings();

    /**
     * Get the loading vector of the i-th principal component.
     *
     * @param i an index, counting from 1
     * @return the loading vector of the i-th principal component
     */
    public Vector loadings(int i);

    /**
     * Get the proportion of overall variance explained by each of the principal components.
     *
     * @return the proportion of overall variance explained by each of the principal components
     */
    public Vector proportionVar();

    /**
     * Get the proportion of overall variance explained by the i-th principal component.
     *
     * @param i an index, counting from 1
     * @return the proportion of overall variance explained by the i-th principal component
     */
    public double proportionVar(int i);

    /**
     * Get the cumulative proportion of overall variance explained by the principal components
     *
     * @return the cumulative proportion of overall variance explained by the principal components
     */
    public Vector cumulativeProportionVar();

    /**
     * Get the scores of supplied data on the principal components.
     * The signs of the columns of the scores are arbitrary.
     *
     * @return the scores of supplied data on the principal components
     */
    public Matrix scores();
}
