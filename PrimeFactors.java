import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {
  public static List<Integer> primeFactors(int numbers) {
    int n = numbers;
    List<Integer> factors = new ArrayList<Integer>();
    for (int i = 2; i <= n / i; i++) {
      while (n % i == 0) {
        factors.add(i);
        n /= i;
      }
    }
    if (n > 1) {
      factors.add(n);
    }
    return factors;
  }

  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    System.out.println("Primefactors of "+n);
    for (Integer integer : primeFactors(n)) {
      System.out.println(integer);
    }
  }
} 