// For week 5
// sestoft@itu.dk * 2014-09-19

import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TestDownload {

  private static final String[] urls = 
  { "http://www.itu.dk", "http://www.di.ku.dk", "http://www.miele.de",
    "https://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
    "https://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
    "https://www.ing.dk", "http://www.dtu.dk", "https://www.eb.dk", 
    "https://www.nytimes.com", "https://www.guardian.co.uk", "https://www.lemonde.fr",   
    "https://www.welt.de", "https://www.dn.se", "http://www.heise.de", "http://www.wsj.com", 
    "http://www.bbc.co.uk", "http://www.dsb.dk", "http://www.bmw.com", "https://www.cia.gov" 
  };

  static final ExecutorService executor = Executors.newWorkStealingPool();

  public static void main(String[] args) throws IOException {
    // String url = "https://www.wikipedia.org/";
    // String page = getPage(url, 10);
    // System.out.printf("%-30s%n%s%n", url, page);

    //getPages(urls, 200).entrySet().stream().forEach(kv -> System.out.println(kv.getKey() + ": " + kv.getValue().length()));

    // testGetPages
    double[] times = new double[5];
    Timer outerTimer = new Timer();    
    for (int i = 0; i < times.length; i++) {
      Timer t = new Timer();
      getPages(urls, 200);
      times[i] = t.check();
    }
    System.out.println("Times for testGetPages were: ");
    Arrays.stream(times).forEach(t -> System.out.println(String.format("%7f per 5x run of 23 pages.", t)));
    System.out.println(outerTimer.check() + " for all runs combined.");

    //testGetPagesParallel
    times = new double[5];
    outerTimer = new Timer();    
    for (int i = 0; i < times.length; i++) {
      Timer t = new Timer();
      getPagesParallel(urls, 200);
      times[i] = t.check();
    }
    System.out.println("Times for testGetPagesParallel were: ");
    Arrays.stream(times).forEach(t -> System.out.println(String.format("%7f per 5x run of 23 pages.", t)));
    System.out.println(outerTimer.check() + " for all runs combined.");
  }

  public static String getPage(String url, int maxLines) throws IOException {
    // This will close the streams after use (JLS 8 para 14.20.3):
    try (BufferedReader in 
         = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
      StringBuilder sb = new StringBuilder();
      for (int i=0; i<maxLines; i++) {
        String inputLine = in.readLine();
        if (inputLine == null)
          break;
        else
          sb.append(inputLine).append("\n");
      }
      return sb.toString();
    }
  }

  public static Map<String, String> getPages(String[] urls, int maxLines) throws IOException {
    return Arrays.stream(urls).collect(Collectors.toMap(u -> u, u -> {
      try {
        return getPage(u, maxLines);
      } catch (IOException e) { return ""; }
    }));
  }

  public static Map<String, String> getPagesParallel(String[] urls, int maxLines) throws IOException {
    var tasks = new ArrayList<Callable<AbstractMap.SimpleEntry<String, String>>>();
    //There are no tuples in this horrible language. ^^^^^
    Arrays.stream(urls).forEach(u -> tasks.add(() -> new AbstractMap.SimpleEntry(u, getPage(u, maxLines))));

    TreeMap<String, String> toReturn = new TreeMap<String, String>();
    try {
      for (Future<AbstractMap.SimpleEntry<String, String>> e : executor.invokeAll(tasks)) {
        try {
          AbstractMap.SimpleEntry<String, String> result = e.get();
          toReturn.put(result.getKey(), result.getValue());
        } catch (Exception ex) { }
      }
    } catch (InterruptedException ie) { return null; }
    //We apologise for the nasty exception handling. Code was written to time on, not for actual correctness.

    return toReturn;
  }
}

