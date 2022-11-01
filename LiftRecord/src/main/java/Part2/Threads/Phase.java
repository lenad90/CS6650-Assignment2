package Part2.Threads;

import Part2.Model.SkiersRunner;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Phase {
  private final Integer numThreads;
  private final Integer numPosts;
  private final String phaseOption;
  private final SkiersApi skierApi;
  private final BlockingQueue<SkiersRunner> dataBuffer;
  private final CountDownLatch phaseSignal;
  private final CountDownLatch phaseFinish;

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
    System.out.println(this.phaseOption + " is starting ==========");
    ExecutorService pool = Executors.newFixedThreadPool(this.numThreads);
    for (int i = 0; i < this.numThreads; i++) {
      pool.execute(new Consumer(this.numPosts, this.skierApi, this.dataBuffer));
      this.phaseFinish.countDown();
      this.phaseSignal.countDown();
    }
    pool.shutdown();
    while (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
      System.out.println("Awaiting for thread to terminate");
    }
  }

  public void finishPhase() throws InterruptedException {
    this.phaseFinish.await();
  }
}
