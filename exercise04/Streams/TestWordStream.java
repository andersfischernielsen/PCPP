// Week 3
// sestoft@itu.dk * 2015-09-09

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestWordStream {
  public static void main(String[] args) {
    String filename = "/usr/share/dict/words";
    System.out.println(readWords(filename).count());
    print100(filename);
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
    readWords(fileName).limit(100).forEach(x -> System.out.println(x));
  }

  public static boolean isPalindrome(String s) {
    // TO DO: Implement properly
    return false; 
  }

  public static Map<Character,Integer> letters(String s) {
    Map<Character,Integer> res = new TreeMap<>();
    // TO DO: Implement properly
    return res;
  }
}
