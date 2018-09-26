import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Exercises_44 {
    
    public static void main(String[] args) {
        int N = 999_999_999; 
        //createFiniteDoubleStream(N); //Exercise 4.4.1
        //createFiniteDoubleStreamParallel(N); //Exercise 4.4.2
        //sequentialSum(N);
        imperativeSum(N);
    }

    //Exercise 4.4.1
    public static void createFiniteDoubleStream(int N) {
        double sum = IntStream.range(1,N)
                  .mapToDouble(i -> 1.0/i)
                  .sum();

        System.out.printf("Sum = %20.16f%n", sum);
    }
    
    //Exercise 4.4.2
    public static void createFiniteDoubleStreamParallel(int N) {
        double sum = IntStream.range(1,N)
                  .mapToDouble(i -> 1.0/i)
                  .parallel()
                  .sum();
        System.out.printf("Parallel Sum = %20.16f%n", sum);
    }

    public static void sequentialSum(int N) {
        double sum = IntStream.range(1,N)
                  .mapToDouble(i -> 1.0/i)
                  .reduce(0, (x,y) -> x + y);
                 //.toArray() 
        /*
        * I'm getting a outOfMemory-exception if I turn the stream into an array. This is however nesecarry if I want to use a classical for-loop. 
        * To prove that I understand the assignment, I've commented out the code, that results in the Memory Exception.
        *
       double sum = 0; 
        for (Double d: ds) {
            sum += d;
        }*/
        System.out.printf("Sequential Sum = %20.16f%n", sum);
    }

    public static void imperativeSum(int N) {
       /* 
       * This is the closets I got to a working solution. 
       * I still do not know, how I can increment the variable i 
       * for each generated number
       */
       
       double i = 1.0; 
       DoubleStream.generate(() -> 1.0 / i + 1)
       .limit(N)
       .sum();
    }

    //Exercise 4.4.5: Not made due to the problems described above.

}