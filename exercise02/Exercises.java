public class Exercises {
    public static void Exercise2_1() {
        int factors = 0;
        for (int i = 0; i < 4999999; i++) 
            factors += TestCountFactors.countFactors(i);

        System.out.println(String.format("Factors were: %d", factors));
    }

    public static void main(String[] args) {
        Exercise2_1();
    }
}