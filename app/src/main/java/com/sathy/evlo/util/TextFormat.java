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
    private static final SimpleDateFormat osdf = new SimpleDateFormat("MMM dd");
    private static final SimpleDateFormat isdf = new SimpleDateFormat("dd-MMM-yyyy");

    private static final String TODAY = "Today";
    private static final String YESTERDAY = "Yesterday";

    public static String toDecimalText(double value) {

        return decimalformat.format(value);
    }

    public static String toDisplayDateText(Date date){
        return isdf.format(date);
    }

    public static String toDateText(String value){

        Calendar calendar = Calendar.getInstance();
        String today = isdf.format(calendar.getTime());

        if(value.equals(today))
            return TODAY;

        calendar.add(Calendar.DATE, -1);
        String yday = isdf.format(calendar.getTime());
        if(value.equals(yday))
            return YESTERDAY;

        try {
            Date date = isdf.parse(value);
            return osdf.format(date);
        } catch (ParseException e) {
            return value;
        }
    }
}
