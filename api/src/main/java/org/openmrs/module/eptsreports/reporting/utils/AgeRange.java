/** */
package org.openmrs.module.eptsreports.reporting.utils;

/** @author Stélio Moiane */
public enum AgeRange {
  UNKNOWN("unknown", -1, -1),

  UNDER_ONE("under1", 0, 0),

  ONE_TO_FOUR("oneTo4", 1, 4),

  FIVE_TO_NINE("fiveTo9", 5, 9),

  TEN_TO_FOURTEEN("tenTo14", 10, 14),

  FIFTEEN_TO_NINETEEN("fifteenTo19", 15, 19),

  TWENTY_TO_TWENTY_FOUR("twentyTo24", 20, 24),

  TWENTY_FIVE_TO_TWENTY_NINE("twenty5To29", 25, 29),

  THIRTY_TO_THRITY_FOUR("thirtyTo34", 30, 34),

  THIRTY_FIVE_TO_THIRTY_NINE("thirty5To39", 35, 39),

  FORTY_TO_FORTY_FOUR("fortyTo44", 40, 44),

  FORTY_FIVE_TO_FORTY_NINE("forty5To49", 45, 49),

  ABOVE_FIFTY("above50", 0, 50);

  private final String name;
  private final int min;
  private final int max;

  private AgeRange(final String name, final int min, final int max) {
    this.name = name;
    this.min = min;
    this.max = max;
  }

  public String getName() {
    return this.name;
  }

  public int getMin() {
    return this.min;
  }

  public int getMax() {
    return this.max;
  }
}