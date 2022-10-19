package Part2.DataGeneration;

import java.security.SecureRandom;
import java.time.LocalDate;

public abstract class AbstractDataGenerator implements IRandomDataGenerator {

  protected static final int LOWERBOUND = 1;
  protected static final int SKIERID_UPPERBOUND = 100000;
  protected static final int RESORTD_UPPERBOUND = 10;

 public Integer generateResortID() {
   return AbstractDataGenerator.idGenerated(RESORTD_UPPERBOUND);
 }

 public String generateSeasonID() {
   return String.valueOf(LocalDate.now().getYear());
 }

 public String getDay() {
   return String.valueOf(LOWERBOUND);
 }

 public Integer generateSkierID() {
   return AbstractDataGenerator.idGenerated(SKIERID_UPPERBOUND);
 }

  public static Integer idGenerated(int upperBound) {
    SecureRandom secureRandom = new SecureRandom();
    return secureRandom.nextInt(upperBound - LOWERBOUND) + LOWERBOUND;
  }



}
