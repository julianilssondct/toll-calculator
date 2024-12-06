package Tests;

import Domain.TollCalculator;
import Models.Car;
import Models.Motorbike;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TollCalculatorTest {

  TollCalculator TollCalculator = new TollCalculator();

  // TODO: The date constructor used in the tests is deprecated, look into date.from() or find another alternative, or update TollCalulator

  @Test
  void testDifferentVehicles() {
    Date date = new Date(124, 11, 5, 8, 15, 0);

    Car car = new Car();
    int totalTollFee = TollCalculator.getTotalTollFee(car, date);
    Assertions.assertEquals(13, totalTollFee);

    Motorbike motorbike = new Motorbike();
    totalTollFee = TollCalculator.getTotalTollFee(motorbike, date);
    Assertions.assertEquals(0, totalTollFee);
  }

  @Test
  void testTollFreeDay() {
    Date date = new Date(124, 0, 1, 8, 15, 0);
    Car car = new Car();
    int totalTollFee = TollCalculator.getTotalTollFee(car, date);
    Assertions.assertEquals(0, totalTollFee);

    //Saturdays should be a toll-free day
    date = new Date(124, 11, 7, 8, 15, 0);
    car = new Car();
    totalTollFee = TollCalculator.getTotalTollFee(car, date);
    Assertions.assertEquals(0, totalTollFee);
  }

  @Test
  void testMultipleFeesInOneHour() {
    Date date = new Date(124, 11, 5, 8, 15, 0);
    Date date2 = new Date(124, 11, 5, 8, 45, 0);

    Car car = new Car();
    int totalTollFee = TollCalculator.getTotalTollFee(car, date, date2);
    Assertions.assertEquals(13, totalTollFee);
  }

  @Test
  void testMaxFeePerDay() {
    Date date = new Date(124, 11, 5, 7, 15, 0);
    Date date2 = new Date(124, 11, 5, 8, 16, 0);
    Date date3 = new Date(124, 11, 5, 14, 17, 0);
    Date date4 = new Date(124, 11, 5, 15, 18, 0);
    Date date5 = new Date(124, 11, 5, 16, 19, 0);
    Date date6 = new Date(124, 11, 5, 17, 20, 0);

    Car car = new Car();
    int totalTollFee = TollCalculator.getTotalTollFee(car, date, date2, date3, date4, date5, date6);
    Assertions.assertEquals(60, totalTollFee);
  }

  @Test
  void testVehicleOrDateIsNull() {
    Assertions.assertEquals(0, TollCalculator.getTotalTollFee(null, new Date(124, 11, 5, 8, 15, 15)));
    Assertions.assertEquals(0, TollCalculator.getTotalTollFee(new Car(), null));
  }

  @Test
  void testGetTollFeeTimeOfDay() {
    Car car = new Car();

    Assertions.assertEquals(0, TollCalculator.getTollFee(new Date(124, 11, 5, 5, 59, 59), car));
    Assertions.assertEquals(8, TollCalculator.getTollFee(new Date(124, 11, 5, 6, 0, 0), car));
    Assertions.assertEquals(13, TollCalculator.getTollFee(new Date(124, 11, 5, 6, 30, 0), car));
    Assertions.assertEquals(18, TollCalculator.getTollFee(new Date(124, 11, 5, 7, 0, 0), car));
    Assertions.assertEquals(13, TollCalculator.getTollFee(new Date(124, 11, 5, 8, 0, 0), car));
    Assertions.assertEquals(8, TollCalculator.getTollFee(new Date(124, 11, 5, 8, 30, 0), car));
    Assertions.assertEquals(8, TollCalculator.getTollFee(new Date(124, 11, 5, 14, 59, 59), car));
    Assertions.assertEquals(13, TollCalculator.getTollFee(new Date(124, 11, 5, 15, 29, 59), car));
    Assertions.assertEquals(18, TollCalculator.getTollFee(new Date(124, 11, 5, 15, 30, 0), car));
    Assertions.assertEquals(13, TollCalculator.getTollFee(new Date(124, 11, 5, 17, 0, 0), car));
    Assertions.assertEquals(8, TollCalculator.getTollFee(new Date(124, 11, 5, 18, 0, 0), car));
    Assertions.assertEquals(0, TollCalculator.getTollFee(new Date(124, 11, 5, 18, 30, 0), car));
  }
}
