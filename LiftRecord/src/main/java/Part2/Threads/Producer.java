package Part2.Threads;

import Part2.Model.SkiersRunner;
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
    SkiersRunner skiersWrapper = new SkiersRunner();
    dataBuffer.put(skiersWrapper);
  }

  // task for producer thread - generate SkiersWrapper and PostRecords to add ot linked blocking queue
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
