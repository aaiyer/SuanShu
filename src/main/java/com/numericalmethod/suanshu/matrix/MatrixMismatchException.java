package com.numericalmethod.suanshu.matrix;

/**
 * This is the runtime exception thrown when an operation acts on matrices that have incompatible dimensions.
 * E.g.,
 * <ul>
 * <li><i>A + B</i> fails when <i>A</i> and <i>B</i> have different numbers of rows and columns.
 * <li><i>A x B</i> fails when <i>A</i> has a number of columns different from the number of rows <i>B</i> has.
 * </ul>
 */
public class MatrixMismatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construct an instance of {@code MatrixMismatchException}.
     */
    public MatrixMismatchException() {
        super("the matrices are of incomptabile dimensions");
    }

    /**
     * Construct an instance of {@code MatrixMismatchException} with a message.
     *
     * @param msg an error message
     */
    public MatrixMismatchException(String msg) {
        super(msg);
    }
}
