package Part2;

import java.util.Collections;
import java.util.List;

public class Calculations {

  private final List<Long> responseTime;

  public Calculations(List<Long> responseTime) {
    Collections.sort(responseTime);
    this.responseTime = responseTime;
  }

  public double mean() {
    return this.responseTime.stream().mapToDouble(val -> val)
        .average().orElse(0.0);
  }

  public double median() {
    return responseTime.get(responseTime.size()/2);
  }

  public double max() {
    return responseTime.get(responseTime.size() - 1);
  }

  public double min() {
    return responseTime.get(0);
  }

  public long percentile(double percentile) {
    int index = (int) Math.ceil(percentile / 100.0 * this.responseTime.size());
    return this.responseTime.get(index-1);
  }
}
