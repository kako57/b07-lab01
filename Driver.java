import java.io.File;
import java.io.IOException;

public class Driver {
  public static void main(String [] args) throws IOException {
    Polynomial p = new Polynomial();
    System.out.println(p.evaluate(3));
    double [] c1 = {6,0,0,5};
    Polynomial p1 = new Polynomial(c1);
    double [] c2 = {0,-2,0,0,-9};
    Polynomial p2 = new Polynomial(c2);
    Polynomial s = p1.add(p2);
    System.out.println("s(0.1) = " + s.evaluate(0.1));
    if(s.hasRoot(1))
      System.out.println("1 is a root of s");
    else
      System.out.println("1 is not a root of s");

    int n = 10;
    double[] c3 = {1};
    Polynomial product = new Polynomial(c3);
    for (int i = 0; i <= n; i++) {
      double[] c4 = {-i, 1};
      product = product.multiply(new Polynomial(c4));
    }

    for (int i = 0; i <= n; i++) {
      if (!product.hasRoot(i)) {
        System.out.println("Test failed: " + i + " is a root of the product");
        break;
      }
    }

    product.saveToFile("product.txt");

    Polynomial p3 = new Polynomial(new File("product.txt"));

    for (int i = 0; i <= n; i++) {
      if (!p3.hasRoot(i)) {
        System.out.println("Test failed: " + i + " is a root of the product");
        break;
      }
    }

  }
}
