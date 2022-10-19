package Part1;

import static java.lang.Math.round;

import Part1.Model.SkiersRunner;
import Part1.Threads.Phase;
import Part1.Threads.Producer;
import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SkiersClient {
  private static final Integer NUM_POSTS = 200000;

  public static void main(String[] args) throws InterruptedException {
    int processors = Runtime.getRuntime().availableProcessors();

    SkiersApi skierApi = new SkiersApi();
    ApiClient client = skierApi.getApiClient();
    BlockingQueue<SkiersRunner> dataBuffer = new LinkedBlockingQueue<>();

    client.setBasePath("http://localhost:8080/LiftServer_war");

    new Thread(new Producer(NUM_POSTS, dataBuffer)).start();

    int phase1Threads = processors * 4;
    int phase1Post = 2050;
    int phase2Trigger = phase1Threads/4;

    long start = System.currentTimeMillis();
    CountDownLatch phase2Signal = new CountDownLatch(phase2Trigger);
    Phase phase1 = new Phase("Phase 1", phase1Threads, phase1Post, skierApi,
        phase2Signal, dataBuffer);
    phase1.startPhase();

    int phase2Threads = phase1Threads * 2;
    int phase2Post = 1500;
    CountDownLatch phase3Signal = new CountDownLatch((int) (phase2Threads * 0.1));
    Phase phase2 = new Phase("Phase 2", phase2Threads, phase2Post, skierApi,
        phase3Signal, dataBuffer);
    phase2Signal.await();
    phase2.startPhase();


    int phase3Threads = phase2Threads * 2;
    int phase3Post = 300;
    CountDownLatch phase4Trigger = new CountDownLatch((int) (phase3Threads*0.1));
    Phase phase3 = new Phase("Phase 3", phase3Threads,
        phase3Post, skierApi, phase4Trigger, dataBuffer);
    phase3Signal.await();
    phase3.startPhase();
    long end = System.currentTimeMillis();

    phase1.finishPhase();
    phase2.finishPhase();
    phase3.finishPhase();
    phase1.phaseStats();
    phase2.phaseStats();
    phase3.phaseStats();
    double runtime = (end - start)*0.001;
    System.out.println("============ PART 1 STATS =============");
    System.out.println("Total run time after all phases completed: " + round(runtime) + "/s");
    System.out.println("Total throughput after all phases completed: " + round(NUM_POSTS/runtime) + "/s");
  }

}
