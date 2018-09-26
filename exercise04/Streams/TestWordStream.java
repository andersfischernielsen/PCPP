// Week 3
// sestoft@itu.dk * 2015-09-09

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestWordStream {
  public static void main(String[] args) {
    String filename = "/usr/share/dict/words";
    Stream<String> words = readWords(filename);
  }

  private static void printStream(Stream stream) {
    stream.forEach(w -> System.out.println(w));
  }

  public static Stream<String> readWords(String filename) {
    // 4.3.1
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      return reader.lines();
    } catch (IOException exn) { 
      return Stream.<String>empty();
    }
  }

  //4.3.2
  public static void print100(String fileName) {
    printStream(readWords(fileName).limit(100));
  }

  //4.3.3
  public static void printAllWordsMoreThan22(String fileName) {
    printStream(readWords(fileName).filter(w -> w.length() >= 22));
  }

  //4.3.4
  public static void printSomeWordsMoreThan22(String fileName) {
    Optional<String> some = readWords(fileName).filter(w -> w.length() >= 22).findAny();
    if (some.isPresent()) System.out.println(some.get());
  }

  //4.3.5
  public static boolean isPalindrome(String s) {
    return new StringBuilder(s).reverse().toString() == s;
  }

  public static void palindromePrint(String filename) {
    printStream(readWords(filename).filter(w -> isPalindrome(w)));
  }

  //4.3.6
  public static void parallelPalindromePrint(String filename) {
      printStream(readWords(filename).parallel().filter(w -> isPalindrome(w)));
  }

  //4.3.7
  public static void printMinMeanAndMaxLength(String filename) {
    IntStream lengths = readWords(filename).map(w -> w.length()).mapToInt(l -> l);
    int max = lengths.max().orElse(0);
    double mean = lengths.average().orElse(0)/lengths.count();
    int min = lengths.min().orElse(0);

    System.out.println(String.format("Min: %d, mean: %d, max: %d", min, mean, max));
  }

  //4.3.8
  public static void groupByLength(String filename) {
    Map<Integer, List<String>> grouped = 
      readWords(filename).collect(Collectors.groupingBy((String w) -> w.length()));
    grouped.forEach((k, v) -> System.out.println(k + ": " + String.join(" ", v)));
  }

  //4.3.9
  public static TreeMap<Character, Long> buildCharacterTree(String s) {
    return s.chars()
      .mapToObj(c -> (char) c)
      .collect(Collectors.groupingBy(c -> c, TreeMap::new, Collectors.counting()));
  }

  public static Map<Character,Integer> letters(String s) {
    Map<Character,Integer> res = new TreeMap<>();
    // TO DO: Implement properly
    return res;
  }
}
