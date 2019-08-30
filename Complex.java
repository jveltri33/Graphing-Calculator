public class Complex {
	public static final Complex ZERO = new Complex();
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex I = new Complex(0, 1);

	double realPart;
	double imaginaryPart;

	public Complex() {
		this(0.0, 0.0);
	}

	public Complex(double realPart) {
		this(realPart, 0.0);
	}

	public Complex(double realPart, double imaginaryPart) {
		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;
	}

	public double getRealPart() {
		return realPart;
	}

	public double getImaginaryPart() {
		return imaginaryPart;
	}

	public static boolean isReal(Complex z) {
		return z.getImaginaryPart() == 0.0;
	}

	public boolean isReal() {
		return isReal(this);
	}

	public static boolean isImaginary(Complex z) {
		return z.getRealPart() == 0.0;
	}

	public boolean isImaginary() {
		return isImaginary(this);
	}

	/*
	 * @throws ArithmeticException
	 */

	public static double asReal(Complex z) {
		if (!isReal(z)) {
			throw new ArithmeticException();
		}
		return z.getRealPart();
	}

	/*
	 * @throws ArithmeticException
	 */

	public double asReal() {
		return asReal(this);
	}

	public static Complex add(Complex z, Complex w) {
		Complex number =  new Complex(z.getRealPart() + w.getRealPart(),
						   			  z.getImaginaryPart() + w.getImaginaryPart());
		return GraphingCalculator.roundComplex(number);
	}

	public Complex plus(Complex w) {
		return add(this, w);
	}

	public static Complex add(Complex z, double c) {
		return add(z, new Complex(c));
	}

	public Complex plus(double c) {
		return add(this, new Complex(c));
	}

	public static Complex add(double c, Complex z) {
		return add(z, new Complex(c));
	}

	public static Complex negative(Complex z) {
		return new Complex(-z.getRealPart(), -z.getImaginaryPart());
	}

	public Complex negative() {
		return negative(this);
	}

	public static Complex subtract(Complex z, Complex w) {
		return add(z, negative(w));
	}

	public Complex minus(Complex w) {
		return add(this, negative(w));
	}

	public static Complex subtract(Complex z, double c) {
		return add(z, new Complex(-c));
	}

	public Complex minus(double c) {
		return add(this, -c);
	}

	public static Complex subtract(double c, Complex z) {
		return add(new Complex(c), negative(z));
	}

	public static Complex multiply(Complex z, Complex w) {
		Complex number = new Complex(z.getRealPart() * w.getRealPart() - z.getImaginaryPart() * w.getImaginaryPart(),
						   			 z.getRealPart() * w.getImaginaryPart() + z.getImaginaryPart() * w.getRealPart());
		return GraphingCalculator.roundComplex(number);
	}

	public Complex times(Complex w) {
		return multiply(this, w);
	}

	public static Complex multiply(Complex z, double c) {
		return multiply(z, new Complex(c));
	}

	public Complex times(double c) {
		return multiply(this, c);
	}

	public static double modulusSquared(Complex z) {
		double number = z.getRealPart() * z.getRealPart() + z.getImaginaryPart() * z.getImaginaryPart();
		return GraphingCalculator.roundDouble(number);
	}

	public double modulusSquared() {
		return modulusSquared(this);
	}

	public static double modulus(Complex z) {
		double number = Math.sqrt(modulusSquared(z));
		return GraphingCalculator.roundDouble(number);
	}

	public double modulus() {
		return modulus(this);
	}

	public static double norm(Complex z) {
		return modulus(z);
	}

	public double norm() {
		return modulus(this);
	}

	public static boolean isZero(Complex z) {
		return (z.getRealPart() == 0.0 && z.getImaginaryPart() == 0);
	}

	public boolean isZero() {
		return isZero(this);
	}

	public static Complex inverse(Complex z) {
		if (isZero(z)) {
			return null;
		}
		double modulusSquared = modulusSquared(z);
		Complex number = new Complex(z.getRealPart() / modulusSquared, -z.getImaginaryPart() / modulusSquared);
		return GraphingCalculator.roundComplex(number);
	}

	public Complex inverse() {
		return inverse(this);
	}

	public static Complex divide(Complex z, Complex w) {
		if (inverse(w) == null) {
			return null;
		}
		return multiply(z, inverse(w));
	}

	public Complex dividedBy(Complex w) {
		if (inverse(w) == null) {
			return null;
		}
		return multiply(this, inverse(w));
	}

	public static Complex divide(Complex z, double c) {
		if (c == 0.0) {
			return null;
		}
		return multiply(z, 1 / c);
	}

	public Complex dividedBy(double c) {
		if (c == 0.0) {
			return null;
		}
		return multiply(this, 1 / c);
	}

	public static Complex conjugate(Complex z) {
		return new Complex(z.getRealPart(), -z.getImaginaryPart());
	}

	public Complex conjugate() {
		return conjugate(this);
	}

	public static double arg(Complex z) {
		if (isZero(z)) {
			return 0.0;
		}
		double number = Math.atan2(z.getImaginaryPart(), z.getRealPart());
		if (number == -Math.PI) {
			number = Math.PI;
		}
		return GraphingCalculator.roundDouble(number);
	}

	public static Complex pow(Complex z, Complex w) {
		if (isZero(z)) {
			if (isZero(w)) {
				return ONE;
			}
			if (isReal(w) && asReal(w) > 0) {
				return ZERO;
			}
			return null;
		}
		double c = w.getRealPart();
		double d = w.getImaginaryPart();
		double arg1 = arg(z);
		double modulusSquared1 = modulusSquared(z);
		double modulus = Math.pow(modulusSquared1, c / 2) * Math.exp(-d * arg1);
		double arg = c * arg1 + d * Math.log(modulusSquared1) / 2;
		Complex number = new Complex(modulus * Math.cos(arg), modulus * Math.sin(arg));
		return GraphingCalculator.roundComplex(number);
	}

	public Complex toThe(Complex w) {
		return pow(this, w);
	}

	public static Complex pow(Complex z, double c) {
		return pow(z, new Complex(c));
	}

	public Complex toThe(double c) {
		return pow(this, new Complex(c));
	}

	public static Complex pow(double c, Complex z) {
		return pow(new Complex(c), z);
	}

	public static Complex sin(Complex z) {
		double a = z.getRealPart();
		double b = z.getImaginaryPart();
		Complex number = new Complex(Math.sin(a) * Math.cosh(b), Math.cos(a) * Math.sinh(b));
		return GraphingCalculator.roundComplex(number);
	}

	public Complex sin() {
		return sin(this);
	}

	public static Complex cos(Complex z) {
		double a = z.getRealPart();
		double b = z.getImaginaryPart();
		Complex number = new Complex(Math.cos(a) * Math.cosh(b), -Math.sin(a) * Math.sinh(b));
		return GraphingCalculator.roundComplex(number);
	}

	public Complex cos() {
		return cos(this);
	}

	public static Complex tan(Complex z) {
		return divide(sin(z), cos(z));
	}

	public Complex tan() {
		return tan(this);
	}

	public static Complex csc(Complex z) {
		return inverse(sin(z));
	}

	public Complex csc() {
		return csc(this);
	}

	public static Complex sec(Complex z) {
		return inverse(cos(z));
	}

	public Complex sec() {
		return sec(this);
	}

	public static Complex cot(Complex z) {
		return divide(cos(z), sin(z));
	}

	public Complex cot() {
		return cot(this);
	}

	public static Complex ln(Complex z) {
		if (isZero(z)) {
			return null;
		}
		Complex number = new Complex(Math.log(modulus(z)), arg(z));
		return GraphingCalculator.roundComplex(number);
	}

	public Complex ln() {
		return ln(this);
	}

	public static Complex log(Complex z) {
		if (isZero(z)) {
			return null;
		}
		return divide(ln(z), Math.log(10.0));
	}

	public Complex log() {
		return log(this);
	}

	public static Complex arcsin(Complex z) {
		Complex number = ln(add(multiply(I, z),
								pow(subtract(ONE, pow(z, 2.0)),
									0.5)));
		if (number == null) {
			return null;
		}
		return multiply(negative(I), number);
	}

	public Complex arcsin() {
		return arcsin(this);
	}

	public static Complex arccos(Complex z) {
		Complex number = arcsin(z);
		if (number == null) {
			return null;
		}
		return subtract(Math.PI / 2, number);
	}

	public Complex arccos() {
		return arccos(this);
	}

	public static Complex arctan(Complex z) {
		Complex number = divide(subtract(ONE, multiply(I, z)),
								add(ONE, multiply(I, z)));
		if (number == null || isZero(number)) {
			return null;
		}
		return multiply(divide(I, 2.0), ln(number));
	}

	public Complex arctan() {
		return arctan(this);
	}

	public static Complex arccsc(Complex z) {
		if (isZero(z)) {
			return null;
		}
		return arcsin(inverse(z));
	}

	public Complex arccsc() {
		return arccsc(this);
	}

	public static Complex arcsec(Complex z) {
		if (isZero(z)) {
			return null;
		}
		return arccos(inverse(z));
	}

	public Complex arcsec() {
		return arcsec(this);
	}

	public static Complex arccot(Complex z) {
		if (isZero(z)) {
			return null;
		}
		return arctan(inverse(z));
	}

	public Complex arccot() {
		return arccot(this);
	}

	public String toString() {
		if (isReal()) {
			if (realPart == (int) realPart) {
				return (int) realPart + "";
			}
			return realPart + "";
		}
		if (isImaginary()) {
			if (imaginaryPart == 1.0) {
				return "i";
			}
			if (imaginaryPart == -1.0) {
				return "-i";
			}
			if (imaginaryPart == (int) imaginaryPart) {
				return (int) imaginaryPart + "i";
			}
			return imaginaryPart + "i";
		}
		if (imaginaryPart > 0) {
			if (imaginaryPart == 1.0) {
				if (realPart == (int) realPart) {
					return (int) realPart + "+i";
				}
				return realPart + "+i";
			}
			if (realPart == (int) realPart) {
				if (imaginaryPart == (int) imaginaryPart) {
					return (int) realPart + "+" + (int) imaginaryPart + "i";
				}
				return (int) realPart + "+" + imaginaryPart + "i";
			}
			if (imaginaryPart == (int) imaginaryPart) {
				return realPart + "+" + (int) imaginaryPart + "i";
			}
			return realPart + "+" + imaginaryPart + "i";
		}
		if (imaginaryPart == -1.0) {
			if (realPart == (int) realPart) {
				return (int) realPart + "-i";
			}
			return realPart + "-i";
		}
		if (realPart == (int) realPart) {
			if (imaginaryPart == (int) imaginaryPart) {
				return (int) realPart + "-" + (int) -imaginaryPart + "i";
			}
			return (int) realPart + "-" + -imaginaryPart + "i";
		}
		if (imaginaryPart == (int) imaginaryPart) {
			return realPart + "-" + (int) -imaginaryPart + "i";
		}
		return realPart + "-" + -imaginaryPart + "i";
	}

	/**
	 * Input must be of the form a,
	 *							 bi, i, -bi, -i,
	 *							 a+bi, a+i, a-bi, a-i,
	 * where a and b are doubles and b is nonnegative.
	 *
	 * @throws NumberFormatException
	 */

	public static Complex parseComplex(String s) {
		int length = s.length();
		if (length == 0) {
			throw new NumberFormatException();
		}
		try {
			
			// a
			return new Complex(Double.parseDouble(s));
		}
		catch (NumberFormatException ex) {
			if (s.charAt(length - 1) != 'i') {
				throw new NumberFormatException();
			}
		}
		if (s.equals("i")) {
			
			// i
			return I;
		}
		if (s.equals("-i")) {
			
			// -i
			return negative(I);
		}
		int plusIndex = s.indexOf("+");
		int minusIndex = s.indexOf("-");
		try {
			
			// bi, -bi
			return new Complex(0.0, Double.parseDouble(s.substring(0, length - 1)));
		}
		catch (NumberFormatException ex) {
			if (plusIndex == -1) {
				if (minusIndex == -1) {
					throw new NumberFormatException();
				}
				if (minusIndex == 0) {
					minusIndex = s.indexOf("-", 1);
				}
				if (minusIndex == -1) {
					throw new NumberFormatException();
				}
				if (minusIndex == length - 2) {
					
					// a-i
					return new Complex(Double.parseDouble(s.substring(0, minusIndex)), -1.0);
				}

				// a-bi
				return new Complex(Double.parseDouble(s.substring(0, minusIndex)),
							   	   Double.parseDouble(s.substring(minusIndex, length - 1)));
			}
		}
		try {
			if (plusIndex == length - 2) {
				
				// a+i
				return new Complex(Double.parseDouble(s.substring(0, plusIndex)), 1.0);
			}

			// a+bi
			return new Complex(Double.parseDouble(s.substring(0, plusIndex)),
							   Double.parseDouble(s.substring(plusIndex + 1, length - 1)));
		}
		catch (StringIndexOutOfBoundsException ex) {
			throw new NumberFormatException();
		}
	}

	public boolean equals(Complex w) {
		return realPart == w.getRealPart() && imaginaryPart == w.getImaginaryPart();
	}
}