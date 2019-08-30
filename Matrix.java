public class Matrix {
	private int rows;
	private int columns;
	private Complex[][] matrix;

	public Matrix() {
		this(1, 1);
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix(int m, int n) {
		if (m <= 0 || n <= 0) {
			throw new DimensionException();
		}
		matrix = new Complex[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = Complex.ZERO;
			}
		}
		rows = m;
		columns = n;
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix(Complex[][] array) {
		if (array == null) {
			throw new DimensionException();
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] == null) {
					throw new DimensionException();
				}
			}
		}
		rows = array.length;
		columns = array[0].length;
		matrix = array;
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix(double[][] array) {
		if (array == null) {
			throw new DimensionException();
		}
		rows = array.length;
		columns = array[0].length;
		matrix = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				matrix[i][j] = new Complex(array[i][j]);
			}
		}
	}

	public static class DimensionException extends RuntimeException {
		public DimensionException() {
			super();
		}
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	/**
	 * @throws DimensionException
	 */

	public Complex get(int i, int j) {
		if (i < 0 || i >= rows || j < 0 || j >= columns) {
			throw new DimensionException();
		}
		return matrix[i][j];
	}

	public boolean isReal() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (!matrix[i][j].isReal()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @throws DimensionException
	 */

	public static Vector asVector(Matrix A) {
		if (A.getColumns() > 1) {
			throw new DimensionException();
		}
		Complex[] array = new Complex[A.getRows()];
		for (int i = 0; i < A.getRows(); i++) {
			array[i] = A.get(i, 0);
		}
		return new Vector(array);
	}

	/**
	 * @throws DimensionException
	 */

	public Vector asVector() {
		return asVector(this);
	}
/*
	public void set(int i, int j, double a) throws DimensionException {
		if (i < 0 || i >= rows || j < 0 || j >= columns) {
			throw new DimensionException();
		}
		matrix[i][j] = a;
	}
*/

	/**
	 * @throws DimensionException
	 */

	public static Matrix identity(int n) {
		if (n <= 0) {
			throw new DimensionException();
		}
		Complex[][] array = new Complex[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (i == j) {
					array[i][j] = Complex.ONE;
				}
				else {
					array[i][j] = Complex.ZERO;
					array[j][i] = Complex.ZERO;
				}
			}
		}
		return new Matrix(array);
	}

	public static boolean isSquare(Matrix A) {
		return A.getRows() == A.getColumns();
	}

	public boolean isSquare() {
		return rows == columns;
	}

	/**
	 * @throws DimensionException
	 */

	public static Matrix add(Matrix A, Matrix B) {
		if (A.getRows() != B.getRows() || A.getColumns() != B.getColumns()) {
			throw new DimensionException();
		}
		int rows = A.getRows();
		int columns = A.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = A.get(i, j).plus(B.get(i, j));
			}
		}
		return new Matrix(array);
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix plus(Matrix B) {
		return add(this, B);
	}

	public static Matrix negative(Matrix A) {
		int rows = A.getRows();
		int columns = A.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = A.get(i, j).negative();
			}
		}
		return new Matrix(array);
	}

	public Matrix negative() {
		return negative(this);
	}

	/**
	 * @throws DimensionException
	 */

	public static Matrix subtract(Matrix A, Matrix B) {
		return add(A, negative(B));
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix minus(Matrix B) {
		return add(this, negative(B));
	}

	/**
	 * @throws DimensionException
	 */

	public static Matrix multiply(Matrix A, Matrix B) {
		if (A.getColumns() != B.getRows()) {
			throw new DimensionException();
		}
		int rows = A.getRows();
		int columns = B.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Complex newElement = Complex.ZERO;
				for (int k = 0; k < A.getColumns(); k++) {
					newElement = newElement.plus(A.get(i, k).times(B.get(k, j)));
				}
				array[i][j] = GraphingCalculator.roundComplex(newElement);
			}
		}
		return new Matrix(array);
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix times(Matrix B) {
		return multiply(this, B);
	}

	public static Matrix multiply(Matrix A, double c) {
		int rows = A.getRows();
		int columns = A.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = GraphingCalculator.roundComplex(A.get(i, j).times(c));
			}
		}
		return new Matrix(array);
	}

	public Matrix times(double c) {
		return multiply(this, c);
	}

	/**
	 * @throws ArithmeticException
	 */

	public static Matrix divide(Matrix A, double c) {
		if (c == 0.0) {
			throw new ArithmeticException();
		}
		return multiply(A, 1 / c);
	}

	/**
	 * @throws ArithmeticException
	 */

	public Matrix dividedBy(double c) {
		return divide(this, c);
	}

	/**
	 * @throws DimensionException
	 */

	public static Vector multiply(Matrix A, Vector x) {
		return multiply(A, (Matrix) x).asVector();
	}

	/**
	 * @throws DimensionException
	 */

	public Vector times(Vector x) {
		return multiply(this, (Matrix) x).asVector();
	}

	/**
	 * @throws DimensionException
	 */

	public static Matrix power(Matrix A, int n) {
		if (!isSquare(A)) {
			throw new DimensionException();
		}
		if (n >= 0) {
			Matrix power = identity(A.getRows());
			for (int i = 0; i < n; i++) {
				power = multiply(power, A);
			}
			return power;
		}
		if (isSingular(A)) {
			return null;
		}
		return power(inverse(A), -n);
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix toThe(int n) {
		return power(this, n);
	}

	public static Matrix transpose(Matrix A) {
		int rows = A.getColumns();
		int columns = A.getRows();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = A.get(j, i);
			}
		}
		return new Matrix(array);
	}

	public Matrix transpose() {
		return transpose(this);
	}

	public static Matrix adjoint(Matrix A) {
		int rows = A.getColumns();
		int columns = A.getRows();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = A.get(j, i).conjugate();
			}
		}
		return new Matrix(array);
	}

	public Matrix adjoint() {
		return adjoint(this);
	}

	public static Matrix ref(Matrix A) {
		int rows = A.getRows();
		int columns = A.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = A.get(i, j);
			}
		}
		int pivotRow = 0;
		for (int j = 0; j < columns; j++) {
			System.out.println("The matrix is now: " + new Matrix(array));
			Complex pivot = array[pivotRow][j];
			System.out.println("Initial pivot: " + pivot);
			int i0 = pivotRow;
			
			// Find the first nonzero number in this column at or below the pivot position.
			while (pivot.isZero()) {
				i0++;
				if (i0 == rows) {
					break;
				}
				pivot = array[i0][j];
			}

			// If everything at or below the pivot position is zero,
			if (i0 == rows) {
				System.out.println("It was all zeros below that. Onward!");
				continue;
			}

			// If that initial pivot was zero,
			if (i0 > pivotRow) {
				// Switch that number's row with the pivot row.
				System.out.println("R" + pivotRow + " <-> R" + i0);
				Complex[] temp = array[pivotRow];
				array[pivotRow] = array[i0];
				array[i0] = temp;
			}

			// Zero out everything in the column below the pivot by elementary row operations.
			for (int i = i0 + 1; i < rows; i++) {
				Complex factor = array[i][j].dividedBy(pivot);
				System.out.println("R" + i + " -> R" + i + " - (" + array[i][j] + ")/(" + pivot + ")R" + pivotRow);
				for (int k = 0; k < columns; k++) {
					array[i][k] = array[i][k].minus(factor.times(array[pivotRow][k]));
				}
			}

			pivotRow++;
		}

		// Round the entries in the matrix.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = GraphingCalculator.roundComplex(array[i][j]);
			}
		}
		return new Matrix(array);
	}

	public Matrix ref() {
		return ref(this);
	}

	public static Matrix rref(Matrix A) {
		int rows = A.getRows();
		int columns = A.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = A.get(i, j);
			}
		}
		int pivotRow = 0;
		for (int j = 0; j < columns && pivotRow < rows; j++) {
			System.out.println("The matrix is now: " + new Matrix(array));
			Complex pivot = array[pivotRow][j];
			System.out.println("Initial pivot: " + pivot);
			int i0 = pivotRow;
			
			// Find the first nonzero number in this column at or below the pivot position.
			while (pivot.isZero()) {
				i0++;
				if (i0 == rows) {
					break;
				}
				pivot = array[i0][j];
			}

			// If everything at or below the pivot position is zero,
			if (i0 == rows) {
				System.out.println("It was all zeros below that. Onward!");
				continue;
			}

			// If that initial pivot was zero,
			if (i0 > pivotRow) {
				// Switch that number's row with the jth row.
				System.out.println("R" + pivotRow + " <-> R" + i0);
				Complex[] temp = array[pivotRow];
				array[pivotRow] = array[i0];
				array[i0] = temp;
			}

			// Scale the pivot row so that the pivot is 1.
			System.out.println("R" + pivotRow + " -> R" + pivotRow + " / (" + pivot + ")");
			for (int k = 0; k < columns; k++) {
				array[pivotRow][k] = array[pivotRow][k].dividedBy(pivot);
			}

			// Zero out everything in the column of the pivot by elementary row operations.
			for (int i = 0; i < rows; i++) {
				if (i == pivotRow) {
					continue;
				}
				Complex factor = array[i][j];
				System.out.println("R" + i + " -> R" + i + " - (" + factor + ")/(" + pivot + ")R" + pivotRow);
				for (int k = 0; k < columns; k++) {
					array[i][k] = array[i][k].minus(factor.times(array[pivotRow][k]));
				}
			}

			pivotRow++;
		}

		// Round the entries in the matrix.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = GraphingCalculator.roundComplex(array[i][j]);
			}
		}

		System.out.println("Final Matrix: " + new Matrix(array));
		return new Matrix(array);
	}

	public Matrix rref() {
		return rref(this);
	}

	/**
	 * @throws DimensionException
	 */

	public static Matrix submatrix(Matrix A, int rowStart, int rowEnd, int columnStart, int columnEnd) {
		int rows = A.getRows();
		int columns = A.getColumns();
		if (rowStart < 0      || rowStart >= rows       ||
			rowEnd < 0        || rowEnd >= rows         ||
			columnStart < 0   || columnStart >= columns ||
			columnEnd < 0     || columnEnd >= columns   ||
			rowStart > rowEnd || columnStart > columnEnd) {
			System.out.println("rowStart: " + rowStart);
			System.out.println("rowEnd: " + rowEnd);
			System.out.println("columnStart: " + columnStart);
			System.out.println("columnEnd: " + columnEnd);
			System.out.println("rows: " + rows);
			System.out.println("columns: " + columns);
			throw new DimensionException();
		}
		int newRows = rowEnd - rowStart + 1;
		int newColumns = columnEnd - columnStart + 1;
		Complex[][] array = new Complex[newRows][newColumns];
		for (int i = 0; i < newRows; i++) {
			for (int j = 0; j < newColumns; j++) {
				array[i][j] = A.get(i + rowStart, j + columnStart);
			}
		}
		return new Matrix(array);
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix submatrix(int rowStart, int rowEnd, int columnStart, int columnEnd) {
		return submatrix(this, rowStart, rowEnd, columnStart, columnEnd);
	}

	/**
	 * @throws DimensionException
	 */

	public static Matrix delete(Matrix A, int deletedRow, int deletedColumn) {
		int rows = A.getRows();
		int columns = A.getColumns();
		if (deletedRow < -1    || deletedRow > rows ||
			deletedColumn < -1 || deletedColumn > columns) {
			throw new DimensionException();
		}
		if (deletedRow == 0    && rows == 1 ||
			deletedColumn == 0 && columns == 1) {
			return null;
		}
		int newRows = (deletedRow == -1 ? rows : rows - 1);
		int newColumns = (deletedColumn == -1 ? columns : columns - 1);
		Complex[][] array = new Complex[newRows][newColumns];
		for (int i = 0; i < rows; i++) {
			if (i == deletedRow) {
				continue;
			}
			int rowOffset = (deletedRow == -1 || i < deletedRow ? 0 : 1);
			for (int j = 0; j < columns - 1; j++) {
				if (j == deletedColumn) {
					continue;
				}
				int columnOffset = (deletedColumn == -1 || j < deletedColumn ? 0 : 1);
				array[i - rowOffset][j - columnOffset] = A.get(i, j);
			}
		}
		return new Matrix(array);
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix delete(int deletedRow, int deletedColumn) {
		return delete(this, deletedRow, deletedColumn);
	}

	/**
	 * @throws DimensionException
	 */

	public static Matrix augment(Matrix A, Matrix B) {
		if (A.getRows() != B.getRows()) {
			throw new DimensionException();
		}
		int rows = A.getRows();
		int columns = A.getColumns() + B.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if  (j < A.getColumns()) {
					array[i][j] = A.get(i, j);
				}
				else {
					array[i][j] = B.get(i, j - A.getColumns());
				}
			}
		}
		return new Matrix(array);
	}

	/**
	 * @throws DimensionException
	 */

	public Matrix augment(Matrix B) {
		return augment(this, B);
	}

	public static Matrix directSum(Matrix A, Matrix B) {
		int rows = A.getRows() + B.getRows();
		int columns = A.getColumns() + B.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (i < A.getRows() && j < A.getColumns()) {
					array[i][j] = A.get(i, j);
				}
				else if (i >= A.getRows() && j >= A.getColumns()) {
					array[i][j] = B.get(i - A.getRows(), j - A.getColumns());
				}
			}
		}
		return new Matrix(array);
	}

	public Matrix directSum(Matrix B) {
		return directSum(this, B);
	}

	/**
	 * @throws DimensionException
	 */

	public static Complex trace(Matrix A) {
		if (!isSquare(A)) {
			throw new DimensionException();
		}
		Complex output = Complex.ZERO;
		for (int i = 0; i < A.getRows(); i++) {
			output = output.plus(A.get(i, i));
		}
		return GraphingCalculator.roundComplex(output);
	}

	/**
	 * @throws DimensionException
	 */

	public Complex trace() {
		return trace(this);
	}

	/**
	 * @throws DimensionException
	 */

	public static Complex tr(Matrix A) {
		return trace(A);
	}

	/**
	 * @throws DimensionException
	 */

	public Complex tr() {
		return trace(this);
	}

	/**
	 * @throws DimensionException
	 */

	public static Complex determinant(Matrix A) {
		if (!isSquare(A)) {
			throw new DimensionException();
		}
		int permutations = 0;
		int rows = A.getRows();
		int columns = A.getColumns();
		Complex[][] array = new Complex[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = A.get(i, j);
			}
		}
		int pivotRow = 0;
		for (int j = 0; j < columns; j++) {
			System.out.println("The matrix is now: " + new Matrix(array));
			Complex pivot = array[pivotRow][j];
			System.out.println("Initial pivot: " + pivot);
			int i0 = pivotRow;
			
			// Find the first nonzero number in this column at or below the pivot position.
			while (pivot.isZero()) {
				i0++;
				if (i0 == rows) {
					break;
				}
				pivot = array[i0][j];
			}

			// If everything at or below the pivot position is zero,
			if (i0 == rows) {
				System.out.println("It was all zeros below that. Onward!");
				continue;
			}

			// If that initial pivot was zero,
			if (i0 > pivotRow) {

				// Switch that number's row with the pivot row.
				System.out.println("R" + pivotRow + " <-> R" + i0);
				Complex[] temp = array[pivotRow];
				array[pivotRow] = array[i0];
				array[i0] = temp;
				permutations++;
			}

			// Zero out everything in the column below the pivot by elementary row operations.
			for (int i = i0 + 1; i < rows; i++) {
				Complex factor = array[i][j].dividedBy(pivot);
				System.out.println("R" + i + " -> R" + i + " - (" + array[i][j] + ")/(" + pivot + ")R" + pivotRow);
				for (int k = 0; k < columns; k++) {
					array[i][k] = array[i][k].minus(factor.times(array[pivotRow][k]));
				}
			}

			pivotRow++;
		}

		// Round the entries in the matrix.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				array[i][j] = GraphingCalculator.roundComplex(array[i][j]);
			}
		}

		Complex answer = (permutations % 2 == 0 ? Complex.ONE : Complex.ONE.negative());
		for (int i = 0; i < rows; i++) {
			answer = answer.times(array[i][i]);
			if (GraphingCalculator.roundComplex(answer).isZero()) {
				return Complex.ZERO;
			}
		}
		return GraphingCalculator.roundComplex(answer);
	}

	/**
	 * @throws DimensionException
	 */

	public Complex determinant() {
		return determinant(this);
	}

	/**
	 * @throws DimensionException
	 */

	public static Complex det(Matrix A) {
		return determinant(A);
	}

	/**
	 * @throws DimensionException
	 */

	public Complex det() {
		return determinant(this);
	}

	public static boolean isInvertible(Matrix A) {
		return isSquare(A) && !determinant(A).isZero();
	}

	public boolean isInvertible() {
		return isInvertible(this);
	}

	public static boolean isSingular(Matrix A) {
		return !isInvertible(A);
	}

	public boolean isSingular() {
		return isSingular(this);
	}

	public static Matrix inverse(Matrix A) {
		if (!isSquare(A)) {
			return null;
		}
		int n = A.getRows();
		Matrix I = identity(n);
		Matrix augmentedMatrix = augment(A, I);
		Matrix reducedMatrix = rref(augmentedMatrix);
		if (!submatrix(reducedMatrix, 0, n - 1, 0, n - 1).equals(I)) {
			return null;
		}
		return submatrix(reducedMatrix, 0, n - 1, n, 2 * n - 1);
	}

	public Matrix inverse() {
		return inverse(this);
	}

	public String toString() {
		String output = "\n";
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				output += matrix[i][j];
				if (j < columns - 1) {
					output += ", ";
				}
			}
			output += "\n";
		}
		return output;
	}

	public boolean equals(Matrix B) {
		if (rows != B.getRows() || columns != B.getColumns()) {
			return false;
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (!matrix[i][j].equals(B.get(i, j))) {
					return false;
				}
			}
		}
		return true;
	}
}