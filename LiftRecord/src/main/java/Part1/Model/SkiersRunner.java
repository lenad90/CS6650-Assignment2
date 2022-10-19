package Part1.Model;

import Part1.DataGeneration.SkiersDataDataGenerator;
import io.swagger.client.model.LiftRide;

public class SkiersRunner {

  private final LiftRide liftRide;

  private final Integer resortID;

  private final String seasonID;

  private final String day;

  private final Integer skierId;

  public SkiersRunner() {
    SkiersDataDataGenerator generator = new SkiersDataDataGenerator();
    this.liftRide = generator.jsonGenerator();
    this.resortID = generator.generateResortID();
    this.seasonID = generator.generateSeasonID();
    this.day = generator.getDay();
    this.skierId = generator.generateSkierID();
  }

  public LiftRide getLiftRide() {
    return liftRide;
  }

  public Integer getResortID() {
    return resortID;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public String getDay() {
    return day;
  }

  public Integer getSkierId() {
    return skierId;
  }

  @Override
  public String toString() {
    return "SkiersDataWrapper{" +
        "liftRide=" + liftRide +
        ", resortID=" + resortID +
        ", seasonID='" + seasonID + '\'' +
        ", day='" + day + '\'' +
        ", skierId=" + skierId +
        '}';
  }

}
