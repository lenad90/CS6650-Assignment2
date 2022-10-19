package Part1.Threads;

import static java.lang.Math.round;

import Part1.Model.SkiersRunner;
import io.swagger.client.api.SkiersApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Phase {

  private final Integer numThreads;
  private final Integer numPosts;
  private final String phaseOption;
  private final SkiersApi skierApi;
  private final BlockingQueue<SkiersRunner> dataBuffer;
  private final CountDownLatch phaseSignal;
  private final CountDownLatch phaseFinish;
  private long start;
  private long end;
  public static List<Double> latency = Collections.synchronizedList(new ArrayList<>());
  private final AtomicInteger SUCCESSFUL = new AtomicInteger(0);
  private final AtomicInteger UNSUCCESSFUL = new AtomicInteger(0);

  public Phase(String phaseOption, Integer numThreads, Integer numPosts, SkiersApi skierApi,
      CountDownLatch phaseSignal, BlockingQueue<SkiersRunner> dataBuffer) {
    this.phaseOption = phaseOption;
    this.numThreads = numThreads;
    this.numPosts = numPosts;
    this.phaseFinish = new CountDownLatch(this.numThreads);
    this.phaseSignal = phaseSignal;
    this.skierApi = skierApi;
    this.dataBuffer = dataBuffer;
  }

  public void startPhase() throws InterruptedException {
    this.start = System.currentTimeMillis();
    System.out.println(this.phaseOption + " is starting ==========");
    ExecutorService pool = Executors.newFixedThreadPool(this.numThreads);
    for (int i = 0; i < this.numThreads; i++) {
      pool.execute(new Consumer(this.numPosts, this.skierApi, this.dataBuffer, SUCCESSFUL, UNSUCCESSFUL));
      this.phaseFinish.countDown();
      this.phaseSignal.countDown();
    }
    pool.shutdown();
    while (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
      System.out.println("Awaiting for thread to terminate");
    }
    this.end = System.currentTimeMillis();
  }

  public void finishPhase() throws InterruptedException {
    this.phaseFinish.await();
  }

  public void phaseStats() {
    double wallTime = round((this.end - this.start) * 0.001);
    System.out.println("===============" + this.phaseOption + " Statistics ================");
    System.out.println("Number of successful POST requests: " + SUCCESSFUL);
    System.out.println("Number of unsuccessful POST requests: " + UNSUCCESSFUL);
    System.out.println("Wall Time in seconds: " + (wallTime));
    System.out.println(
        "Actual Throughput = " + round(SUCCESSFUL.intValue() + UNSUCCESSFUL.intValue()
            / wallTime) + "/s for " +
            this.numThreads + " threads");

    int MAX_SERVER_THREADS = 200;
    int N = MAX_SERVER_THREADS;
    if (this.numThreads < MAX_SERVER_THREADS) {
      N = this.numThreads;
    }
    System.out.println("Expected throughput = " + round(N / latency.stream().mapToDouble(val -> val)
        .average().orElse(0.0)) + "/s");
  }
}