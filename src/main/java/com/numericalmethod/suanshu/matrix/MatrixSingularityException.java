package com.numericalmethod.suanshu.matrix;

/**
 * This is the runtime exception thrown when an operation acts on a singular matrix, e.g., applying LU decomposition to a singular matrix.
 * A singular matrix is a matrix that is not invertible; equivalently, its determinant is 0.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Singular_matrix"> Wikipedia: Singular matrix</a>
 */
public class MatrixSingularityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of {@code MatrixSingularityException}.
     */
    public MatrixSingularityException() {
        super("singular matrix found");
    }

    /**
     * Construct an instance of {@code MatrixSingularityException} with a message.
     *
     * @param msg an error message
     */
    public MatrixSingularityException(String msg) {
        super(msg);
    }
}
