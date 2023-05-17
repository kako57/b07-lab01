public class Polynomial {
  double[] coefficients;

  Polynomial(double[] coefficients) {
    this.coefficients = coefficients;
  }

  Polynomial() {
    this.coefficients = new double[1];
    this.coefficients[0] = 0;
  }

  public Polynomial add(Polynomial other) {
    int degree = Math.max(this.coefficients.length, other.coefficients.length);
    double[] new_coefficients = new double[degree];

    for (int i = 0; i < degree; i++) {
      double this_coefficient = 0;
      double other_coefficient = 0;

      if (i < this.coefficients.length) {
        this_coefficient = this.coefficients[i];
      }

      if (i < other.coefficients.length) {
        other_coefficient = other.coefficients[i];
      }

      new_coefficients[i] = this_coefficient + other_coefficient;
    }

    return new Polynomial(new_coefficients);
  }

  public double evaluate(double x) {
    // horner's method
    // source: myself (https://github.com/kako57/pq/blob/master/polynomial.c)
    
    double result = 0;

    for (int i = this.coefficients.length - 1; i >= 0; i--) {
      result = result * x + this.coefficients[i];
    }

    return result;
  }

  public boolean hasRoot(double x) {
    return this.evaluate(x) == 0;
  }
}
