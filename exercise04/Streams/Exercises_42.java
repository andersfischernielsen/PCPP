import java.util.Arrays;

public class Exercises_42 {
    private static int[] a; 

    public static void main(String[] args) {
        int N = 10_000_001; 
        a = new int[N];
        initializeArray(); //Exercise 4.2.1
        prefixSum(); //Exercise 4.2.2
        System.out.println("a[10_000_000] should be 664,579 : " + a[N-1]);
        
        //Exercise 4.2.3
        for(int i = N/10; i < N; i=i+N/10) {
            double ratio = a[i] / (i/Math.log10(i));
            System.out.println(ratio);
        }
    }

    public static int primePosition(int n) {
        if (isPrime(n)) return 1;
        else return 0;
    }

    private static boolean isPrime(int n) {
        int k = 2;
        while (k * k <= n && n % k != 0)
          k++;
        return n >= 2 && k * k > n;
    }

    public static void initializeArray() {
        Arrays.parallelSetAll(a, i -> primePosition(i));
    }

    public static void prefixSum() {
        Arrays.parallelPrefix(a, (n,k) -> n + k);
    }

    
}