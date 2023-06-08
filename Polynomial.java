import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Polynomial {
  double[] coefficients;
  int[] exponents;

  private int count_non_zero(double[] coefficients) {
    // count the number of non-zero coefficients
    int count = 0;
    for (int i = 0; i < coefficients.length; i++)
      if (coefficients[i] != 0)
        count++;

    return count;
  }

  Polynomial(double[] coefficients) {
    // count the number of non-zero coefficients
    int count = count_non_zero(coefficients);

    // create coefficients and exponents arrays
    this.coefficients = new double[count];
    this.exponents = new int[count];

    // fill the arrays
    for (int i = 0, j = 0; i < coefficients.length; i++) {
      if (coefficients[i] != 0) {
        this.coefficients[j] = coefficients[i];
        this.exponents[j] = i;
        j++;
      }
    }
  }

  Polynomial() {
    this.coefficients = new double[0];
    this.exponents = new int[0];
  }

  Polynomial(File file) throws IOException {
    // file content is only one line no whitespace
    // it looks like this: 5-3x2+7x8
    // it means: 5 - 3x^2 + 7x^8

    String content = new String(Files.readAllBytes(file.toPath()))
      .strip() // probably not necessary, but just in case
      .replaceAll("-", "+-");

    String[] terms = content.split("\\+");

    // get degree of polynomial
    int degree = 0;

    for (int i = 0; i < terms.length; i++) {
      // put the degree of the term if it's not there
      if (!terms[i].contains("x"))
        terms[i] += "x0";
      if (terms[i].charAt(terms[i].length()-1) == 'x')
        terms[i] += "1";
      if (terms[i].charAt(0) == 'x')
        terms[i] = "1" + terms[i];

      String[] parts = terms[i].split("x");

      degree = Math.max(degree, Integer.parseInt(parts[1]));
    }

    double[] new_coefficients = new double[degree+1];

    for (int i = 0; i < terms.length; i++) {
      String[] parts = terms[i].split("x");

      new_coefficients[Integer.parseInt(parts[1])] +=
        Double.parseDouble(parts[0]);
    }

    // sheeeeeeesh a constructor calling another constructor
    Polynomial new_poly = new Polynomial(new_coefficients);

    this.coefficients = new_poly.coefficients;
    this.exponents = new_poly.exponents;
  }

  public void saveToFile(String file) throws IOException {
    FileWriter writer = new FileWriter(file);

    for (int i = 0; i < this.coefficients.length; i++) {
      if (i != 0 && this.coefficients[i] > 0)
        writer.write("+");
      writer.write(String.valueOf(this.coefficients[i]));
      if (this.exponents[i] >= 1)
        writer.write("x");
      if (this.exponents[i] >= 2)
        writer.write(String.valueOf(this.exponents[i]));
    }

    writer.close();
  }

  private int get_max(int[] array) {
    int max = array[0];

    for (int i = 1; i < array.length; i++)
      if (array[i] > max)
        max = array[i];

    return max;
  }

  public Polynomial add(Polynomial other) {
    int degree1 = get_max(this.exponents);
    int degree2 = get_max(other.exponents);

    int degree = Math.max(degree1, degree2) + 1;
    double[] new_coefficients = new double[degree];

    for (int i = 0; i < this.coefficients.length; i++)
      new_coefficients[this.exponents[i]] += this.coefficients[i];

    for (int i = 0; i < other.coefficients.length; i++)
      new_coefficients[other.exponents[i]] += other.coefficients[i];

    return new Polynomial(new_coefficients);
  }

  public Polynomial multiply(Polynomial other) {
    int degree1 = get_max(this.exponents);
    int degree2 = get_max(other.exponents);

    int degree = degree1 + degree2 + 1;
    double[] new_coefficients = new double[degree];

    for (int i = 0; i < this.coefficients.length; i++)
      for (int j = 0; j < other.coefficients.length; j++)
        new_coefficients[this.exponents[i] + other.exponents[j]] +=
          this.coefficients[i] * other.coefficients[j];

    return new Polynomial(new_coefficients);
  }

  public double evaluate(double x) {
    double result = 0;

    for (int i = 0; i < this.coefficients.length; i++)
      result += this.coefficients[i] * Math.pow(x, this.exponents[i]);

    return result;
  }

  public boolean hasRoot(double x) {
    return this.evaluate(x) == 0;
  }
}
