package Domain;

import Models.Vehicle;
import java.util.*;
import java.util.concurrent.*;

public class TollCalculator {

  /**
   * Calculate the total toll fee for one day
   *
   * @param vehicle - the vehicle
   * @param dates   - date and time of all passes on one day
   * @return - the total toll fee for that day
   */
  public int getTotalTollFee(Vehicle vehicle, Date... dates) {
    if (vehicle == null || dates == null) return 0;

    Date intervalStart = dates[0];
    int tempFee = getTollFee(intervalStart, vehicle);

    if (dates.length == 1) return tempFee;

    int totalFee = 0;
    for (Date date : dates) {
      int nextFee = getTollFee(date, vehicle);
      if (nextFee == 0) continue;

      TimeUnit timeUnit = TimeUnit.MINUTES;
      long diffInMillies = date.getTime() - intervalStart.getTime();
      long minutes = timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);

      // In case of multiple tolls in the same hour, the highest one applies
      // Else add toll fee to total fee
      if (minutes <= 60) {
        if (totalFee > 0) totalFee -= tempFee;
        if (nextFee >= tempFee) tempFee = nextFee;
        totalFee += tempFee;
      } else {
        totalFee += nextFee;
      }
      if (totalFee > 60) {
        totalFee = 60;
        break;
      }
    }
    return totalFee;
  }

  /**
   * Calculate the toll fee for one pass, taking consideration of toll-free vehicles and days
   *
   * @param date   - date and time one pass
   * @param vehicle - the type of vehicle
   * @return - the toll fee
   */
  public int getTollFee(final Date date, Vehicle vehicle) {
    if(isTollFreeDate(date) || TollFreeVehicles.isTollFreeVehicle(vehicle)) return 0;
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    if (hour <= 5) return 0;
    else if (hour == 6 && minute <= 29) return 8;
    else if (hour == 6) return 13;
    else if (hour == 7) return 18;
    else if (hour == 8 && minute <= 29) return 13;
    else if (hour <= 14) return 8;
    else if (hour == 15 && minute <= 29) return 13;
    else if (hour == 15 || hour == 16 ) return 18;
    else if (hour == 17) return 13;
    else if (hour == 18 && minute <= 29) return 8;
    else return 0;
  }

  private Boolean isTollFreeDate(Date date) {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) return true;

    // TODO: Fix better implementation of comparing date to toll-free dates + double check toll-free dates for this year
    if (year == 2024) {
      if (month == Calendar.JANUARY && day == 1 ||
          month == Calendar.MARCH && (day == 28 || day == 29) ||
          month == Calendar.APRIL && (day == 1 || day == 30) ||
          month == Calendar.MAY && (day == 1 || day == 8 || day == 9) ||
          month == Calendar.JUNE && (day == 5 || day == 6 || day == 21) ||
          month == Calendar.JULY || // TODO: Ask stakeholders if the full month of July should be toll-free
          month == Calendar.NOVEMBER && day == 1 ||
          month == Calendar.DECEMBER && (day == 24 || day == 25 || day == 26 || day == 31)) {
        return true;
      }
    }
    return false;
  }

  private enum TollFreeVehicles {
    MOTORBIKE("Motorbike"),
    TRACTOR("Tractor"),
    EMERGENCY("Emergency"),
    DIPLOMAT("Diplomat"),
    FOREIGN("Foreign"),
    MILITARY("Military");
    private final String type;

    TollFreeVehicles(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }

    public static boolean isTollFreeVehicle(Vehicle vehicle) {
      // Iterates through the toll-free vehicles
      for (TollFreeVehicles tollFreeVehicle : TollFreeVehicles.values()) {
        if (tollFreeVehicle.getType().equals(vehicle.getType())) {
          return true;
        }
      }
      return false;
    }
  }
}

