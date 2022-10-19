package Part1.Threads;

import Part1.Model.SkiersRunner;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {

  private final SkiersApi skierApi;
  private final BlockingQueue<SkiersRunner> dataBuffer;
  private final Integer numPosts;
  private final AtomicInteger successful;
  private final AtomicInteger unsuccessful;


  public Consumer(Integer numPosts, SkiersApi skierApi,
      BlockingQueue<SkiersRunner> dataBuffer, AtomicInteger successful,
      AtomicInteger unsuccessful) {
    this.skierApi = skierApi;
    this.dataBuffer = dataBuffer;
    this.numPosts = numPosts;
    this.successful = successful;
    this.unsuccessful = unsuccessful;
  }

  @Override
  public void run() {
    SkiersRunner skier;
    for (int i = 0; i < this.numPosts; i++) {
      try {
        skier = this.dataBuffer.take();
        this.post(skier);
      } catch (InterruptedException | ApiException e) {
        e.printStackTrace();
      }
    }
  }

  private void post(SkiersRunner skier) throws ApiException {
    int numTries = 0;
    try {
      while (numTries != 5) {
        long start = System.currentTimeMillis();
        ApiResponse<Void> response = skierApi.writeNewLiftRideWithHttpInfo(skier.getLiftRide(),
            skier.getResortID(), skier.getSeasonID(), skier.getDay(), skier.getSkierId());
        long end = System.currentTimeMillis();
        long latency = end - start;
        Phase.latency.add(latency*0.001);
        if (response.getStatusCode() == 201) {
          this.successful.getAndIncrement();
          break;
        } else {
          numTries += 1;
        }
      }
      if (numTries == 5) {
        this.unsuccessful.getAndIncrement();
      }
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }
}
