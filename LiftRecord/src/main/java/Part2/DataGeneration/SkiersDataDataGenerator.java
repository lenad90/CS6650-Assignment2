package Part2.DataGeneration;

import io.swagger.client.model.LiftRide;

// TODO: create a wrapper
public class SkiersDataDataGenerator extends AbstractDataGenerator implements
    IRandomDataGenerator {

  protected static final int DAY = 1;

  public SkiersDataDataGenerator() {
  }


  @Override
  public LiftRide jsonGenerator() {
    Integer liftID = AbstractDataGenerator.idGenerated(40);
    Integer time = AbstractDataGenerator.idGenerated(360);
//    "{ \"time\": 217, \"liftID\": 21 }";
    LiftRide liftRide = new LiftRide();
    liftRide.setLiftID(liftID);
    liftRide.setTime(time);
    return liftRide;
  }
}
