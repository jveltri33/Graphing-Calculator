public class Vector extends Matrix {
	
	public Vector() {
		super();
	}

	public Vector(int n) {
		super(n, 1);
	}

	public Vector(double ... x) {
		super(new double[][] {x});
	}

	public Vector(Complex ... x) {
		super(new Complex[][] {x});
	}

	/**
	 * @throws DimensionException
	 */

	public Complex get(int n) {
		if (n < 0 || n >= getRows()) {
			throw new DimensionException();
		}
		return get(n, 1);
	}

	/**
	 * @throws DimensionException
	 */

	public static Complex dot(Vector x, Vector y) {
		if (x.getRows() != y.getRows()) {
			throw new DimensionException();
		}
		Complex output = Complex.ZERO;
		for (int i = 0; i < x.getRows(); i++) {
			output = output.plus(x.get(i).times(y.get(i).conjugate()));
		}
		return GraphingCalculator.roundComplex(output);
	}

	/**
	 * @throws DimensionException
	 */

	public Complex dot(Vector y) {
		return dot(this, y);
	}

	public static double norm(Vector x) {
		return Math.sqrt(dot(x, x).asReal());
	}

	public double norm() {
		return norm(this);
	}
}