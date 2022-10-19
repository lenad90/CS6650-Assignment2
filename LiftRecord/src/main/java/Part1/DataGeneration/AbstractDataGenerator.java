package Part1.DataGeneration;

import java.security.SecureRandom;
import java.time.LocalDate;

public abstract class AbstractDataGenerator implements IRandomDataGenerator {

  protected static final int LOWERBOUND = 1;
  protected static final int SKIERSID_UPPERBOUND = 100000;
  protected static final int RESORTID_LOWERBOUND = 10;


 public Integer generateResortID() {
   return AbstractDataGenerator.idGenerated(RESORTID_LOWERBOUND);
 }

 public String generateSeasonID() {
   return String.valueOf(LocalDate.now().getYear());
 }

 public String getDay() {
   return String.valueOf(LOWERBOUND);
 }

 public Integer generateSkierID() {
   return AbstractDataGenerator.idGenerated(SKIERSID_UPPERBOUND);
 }

  public static Integer idGenerated(int upperBound) {
    SecureRandom secureRandom = new SecureRandom();
    return secureRandom.nextInt(upperBound - LOWERBOUND) + LOWERBOUND;
  }



}
