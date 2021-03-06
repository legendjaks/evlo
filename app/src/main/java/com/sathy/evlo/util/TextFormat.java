package com.sathy.evlo.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sathy on 28/6/15.
 */
public class TextFormat {

  private static final DecimalFormat decimalformat = new DecimalFormat("##,##,##,##0.00");
  private static final SimpleDateFormat osdf = new SimpleDateFormat("EEE, MMM dd");
  private static final SimpleDateFormat isdf = new SimpleDateFormat("yyyy-MM-dd");
  private static final SimpleDateFormat month = new SimpleDateFormat("MMM yyyy");

  private static final String TODAY = "Today";
  private static final String YESTERDAY = "Yesterday";

  private static final String SingleQuote = "'";

  public static String toDecimalText(double value) {

    return decimalformat.format(value);
  }

  public static String getCurrentMonth() {
    return month.format(new Date()).toUpperCase();
  }

  public static String[] getCurrentMonthDates() {
    Calendar from = Calendar.getInstance();
    from.set(Calendar.DATE, 1);

    Calendar to = Calendar.getInstance();
    to.set(Calendar.DATE, 1);
    to.add(Calendar.MONTH, 1);
    to.add(Calendar.DATE, -1);

    return new String[]{dateValue(from), dateValue(to)};
  }

  public static String toDisplayDateText(Date date) {
    return isdf.format(date);
  }

  public static String dateValue(Calendar calendar) {
    return isdf.format(calendar.getTime());
  }

  public static String toDateText(String value) {

    Calendar calendar = Calendar.getInstance();
    String today = isdf.format(calendar.getTime());

    if (value.equals(today))
      return TODAY;

    calendar.add(Calendar.DATE, -1);
    String yday = isdf.format(calendar.getTime());
    if (value.equals(yday))
      return YESTERDAY;

    try {
      Date date = isdf.parse(value);
      return osdf.format(date);
    } catch (ParseException e) {
      return value;
    }
  }

  public static String getSourceSymbol(String text) {

    int gap = text.indexOf(" ");
    if (gap == -1)
      return String.valueOf(text.charAt(0));

    if ((gap + 1) < text.length())
      return new String(new char[]{text.charAt(0), text.charAt(gap + 1)});

    return String.valueOf(text.charAt(0));
  }

  public static String quotes(String text) {
    return SingleQuote + text + SingleQuote;
  }
}
