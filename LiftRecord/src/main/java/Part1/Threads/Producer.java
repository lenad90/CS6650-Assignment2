package Part1.Threads;

import Part1.Model.SkiersRunner;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

  private final BlockingQueue<SkiersRunner> dataBuffer;
  private final Integer numPosts;

  public Producer(Integer numPosts,
      BlockingQueue<SkiersRunner> dataBuffer) {
    this.numPosts = numPosts;
    this.dataBuffer = dataBuffer;
  }

  private void generateAndPutSkiersWrapper() throws InterruptedException {
    SkiersRunner skiersDataWrapper = new SkiersRunner();
    dataBuffer.put(skiersDataWrapper);
  }

  @Override
  public void run() {
    try {
      for (int i = 0; i < this.numPosts; i++) {
        generateAndPutSkiersWrapper();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
