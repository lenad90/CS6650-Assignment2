package Part1.DataGeneration;

import io.swagger.client.model.LiftRide;

public class SkiersDataDataGenerator extends AbstractDataGenerator implements IRandomDataGenerator {

  public SkiersDataDataGenerator() {
  }

  @Override
  public LiftRide jsonGenerator() {
    Integer liftID = AbstractDataGenerator.idGenerated(40);
    Integer time = AbstractDataGenerator.idGenerated(360);
    LiftRide liftRide = new LiftRide();
    liftRide.setLiftID(liftID);
    liftRide.setTime(time);
    return liftRide;
  }
}
