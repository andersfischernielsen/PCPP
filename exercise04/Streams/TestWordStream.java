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
    System.out.println(countAnagrams(filename));
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
  public static TreeMap<Character, Long> letters(String s) {
    return s.toLowerCase().chars()
      .mapToObj(c -> (char) c)
      .collect(Collectors.groupingBy(c -> c, TreeMap::new, Collectors.counting()));
  }

  //4.3.10
  public static long countEs(String filename) {
    String allWords = readWords(filename).reduce("", (w, acc) -> w + acc);
    return letters(allWords).get('e');
  }

  //4.3.11
  public static int countAnagrams(String filename) {
    Stream<String> allWords = readWords(filename);
    //There's a bug here. So, every word is converted to a TreeMap, and then grouped by the equalities of the TreeMap. 
    //Or so we'd expect. But it seems that the equality check in groupingBy() doesn't "realize" that two TreeMaps are equal.
    //We therefore get way too many results. We have tried forcin a comparison using TreeMap::equals without success.
    //We even tried collecting on m -> m.toString, without success, since the trees are ordered according to the natural 
    //ordering of keys (chars here), toString on the TreeMap should produce the same string for two identical maps, 
    //but this still gives too many results. 
    var groupedMaps = allWords.map(w -> letters(w)).collect(Collectors.groupingBy(m -> m));
    //                                                                            ^^^^^^ should result in a.compareTo(b) for TreeMap a and TreeMap b, right? 
    return groupedMaps.size();
  }

  //4.3.12 & 4.3.13
  public static int countAnagramsParallel(String filename) {
    //Slapping on parallel seems to make the execution slower. GroupingByConcurrent is a better variant.
    Stream<String> allWords = readWords(filename);
    var groupedMaps = allWords./*parallel().*/map(w -> letters(w)).collect(Collectors.groupingByConcurrent(m -> m));
    return groupedMaps.size();
  }
}
