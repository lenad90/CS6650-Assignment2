package Part2.Threads;

import Part2.SkiersClient2;
import Part2.Model.SkiersRunner;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

  private final SkiersApi skierApi;
  private final BlockingQueue<SkiersRunner> dataBuffer;
  private final Integer numPosts;

  public Consumer(Integer numPosts, SkiersApi skierApi,
      BlockingQueue<SkiersRunner> dataBuffer) {
    this.skierApi = skierApi;
    this.dataBuffer = dataBuffer;
    this.numPosts = numPosts;
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

  synchronized private void post(SkiersRunner skier) throws ApiException {
    int numTries = 0;
    try {
      while (numTries != 5) {
        long start = System.currentTimeMillis();
        ApiResponse<Void> response = skierApi.writeNewLiftRideWithHttpInfo(skier.getLiftRide(),
            skier.getResortID(), skier.getSeasonID(), skier.getDay(), skier.getSkierId());
        long latency = System.currentTimeMillis() - start;
        SkiersClient2.dataPerformance.add(new String[]{String.valueOf(start), "POST",
            String.valueOf(latency), String.valueOf(response.getStatusCode())});
        SkiersClient2.responseTime.add(latency);
        if (response.getStatusCode() == 201) {
          break;
        } else {
          numTries += 1;
        }
      }
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }
}
