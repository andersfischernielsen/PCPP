// For week 5
// sestoft@itu.dk * 2014-09-23

// A pipeline of transformers connected by bounded queues.  Each
// transformer consumes items from its input queue and produces items
// on its output queue.

// This is illustrated by generating URLs, fetching the corresponding
// webpages, scanning the pages for links to other pages, and printing
// those links; using four threads connected by three queues:

// UrlProducer --(BlockingQueue<String>)--> 
// PageGetter  --(BlockingQueue<Webpage>)--> 
// LinkScanner --(BlockingQueue<Link>)--> 
// LinkPrinter


// For reading webpages
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// For regular expressions
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class TestPipeline {
  public static void main(String[] args) {
    runAsThreads();
  }

  private static void runAsThreads() {
    //final ExecutorService executor = Executors.newWorkStealingPool();
    //final ExecutorService executor = Executors.newFixedThreadPool(6);
    final ExecutorService executor = Executors.newFixedThreadPool(3);

    final BlockingQueue<String> urls = new OneItemQueue<String>();
    final BlockingQueue<Webpage> pages = new OneItemQueue<Webpage>();
    final BlockingQueue<Link> refPairs = new OneItemQueue<Link>();
    final BlockingQueue<Link> uniqueLinks = new OneItemQueue<Link>();

    List<Future<?>> futures = new ArrayList<Future<?>>();

    Runnable t1 = new UrlProducer(urls);
    Runnable t21 = new PageGetter(urls, pages);
    Runnable t22 = new PageGetter(urls, pages);
    Runnable t3 = new LinkScanner(pages, uniqueLinks);
    Runnable t4 = new Uniquifier(uniqueLinks, refPairs);
    Runnable t5 = new LinkPrinter(refPairs);

    futures.add(executor.submit(t1));
    futures.add(executor.submit(t2));
    futures.add(executor.submit(t3));
    futures.add(executor.submit(t4));
    futures.add(executor.submit(t5));

    for (Future f : futures) {
      try {
        f.get();
      } catch (Exception e) {}
    }
  }
}

class UrlProducer implements Runnable {
  private final BlockingQueue<String> output;

  public UrlProducer(BlockingQueue<String> output) {
    this.output = output;
  }

  public void run() { 
    for (int i=0; i<urls.length; i++)
      output.put(urls[i]);
  }

  private static final String[] urls = 
  { "http://www.itu.dk", "http://www.itu.dk", "http://www.di.ku.dk", "http://www.miele.de",
    "http://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
    "http://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
    "http://www.ing.dk", "http://www.dtu.dk", "http://www.bbc.co.uk"
  };
}

class Uniquifier<T> implements Runnable {
  private final BlockingQueue<T> input;
  private final BlockingQueue<T> output;
  private final HashSet<T> items;

  public Uniquifier(BlockingQueue<T> input, BlockingQueue<T> output) {
    this.output = output;
    this.input = input;
    this.items = new HashSet<>();
  }

  public void run() { 
    while (true) {
      T item = input.take();
      if (!items.contains(item)) { 
        items.add(item); 
        output.put(item); 
      }
    }
  }
}

class PageGetter implements Runnable {
  private final BlockingQueue<String> input;
  private final BlockingQueue<Webpage> output;

  public PageGetter(BlockingQueue<String> input, BlockingQueue<Webpage> output) {
    this.input = input;
    this.output = output;
  }

  public void run() { 
    while (true) {
      String url = input.take();
      //      System.out.println("PageGetter: " + url);
      try { 
        String contents = getPage(url, 200);
        output.put(new Webpage(url, contents));
      } catch (IOException exn) { System.out.println(exn); }
    }
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
}

class LinkScanner implements Runnable {
  private final BlockingQueue<Webpage> input;
  private final BlockingQueue<Link> output;

  public LinkScanner(BlockingQueue<Webpage> input, 
                     BlockingQueue<Link> output) {
    this.input = input;
    this.output = output;
  }

  private final static Pattern urlPattern 
    = Pattern.compile("a href=\"(\\p{Graph}*)\"");

  public void run() { 
    while (true) {
      Webpage page = input.take();
      //      System.out.println("LinkScanner: " + page.url);
      // Extract links from the page's <a href="..."> anchors
      Matcher urlMatcher = urlPattern.matcher(page.contents);
      while (urlMatcher.find()) {
        String link = urlMatcher.group(1);
        output.put(new Link(page.url, link));
      }
    }
  }
}

class LinkPrinter implements Runnable {
  private final BlockingQueue<Link> input;

  public LinkPrinter(BlockingQueue<Link> input) {
    this.input = input;
  }

  public void run() { 
    while (true) {
      Link link = input.take();
      //      System.out.println("LinkPrinter: " + link.from);
      System.out.printf("%s links to %s%n", link.from, link.to);
    }
  }
}


class Webpage {
  public final String url, contents;
  public Webpage(String url, String contents) {
    this.url = url;
    this.contents = contents;
  }
}

class Link {
  public final String from, to;
  public Link(String from, String to) {
    this.from = from;
    this.to = to;
  }

  // Override hashCode and equals so can be used in HashSet<Link>

  public int hashCode() {
    return (from == null ? 0 : from.hashCode()) * 37
         + (to == null ? 0 : to.hashCode());
  }

  public boolean equals(Object obj) {
    Link that = obj instanceof Link ? (Link)obj : null;
    return that != null 
      && (from == null ? that.from == null : from.equals(that.from))
      && (to == null ? that.to == null : to.equals(that.to));
  }
}

// Different from java.util.concurrent.BlockingQueue: Allows null
// items, and methods do not throw InterruptedException.

interface BlockingQueue<T> {
  void put(T item);
  T take();
}

class OneItemQueue<T> implements BlockingQueue<T> {
  private T item;
  private boolean full = false;

  public void put(T item) {
    synchronized (this) {
      while (full) {
        try { this.wait(); } 
        catch (InterruptedException exn) { }
      }
      full = true;
      this.item = item;
      this.notifyAll();
    }
  }

  public T take() {
    synchronized (this) {
      while (!full) {
        try { this.wait(); } 
        catch (InterruptedException exn) { }
      }
      full = false;
      this.notifyAll();
      return item;
    }
  }
}
