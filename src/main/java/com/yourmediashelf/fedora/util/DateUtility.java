package com.yourmediashelf.fedora.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;


/**
 * Date and time utility methods.
 *
 * @author Edwin Shin
 */
public class DateUtility {

    /**
     * <p>Regular expression string for the yearFrag:</p>
     *
     * <p>a numeral consisting of at least four decimal digits, optionally
     * preceded by a minus sign; leading '0' digits are prohibited except to
     * bring the digit count up to four.</p>
     */
    private final static String yearFrag = "(-?([1-9][0-9]{3,}|0[0-9]{3}))";

    /**
     * <p>Regular expression string for the monthFrag:</p>
     *
     * <p>a numeral consisting of exactly two decimal digits.</p>
     */
    private final static String monthFrag = "(0[1-9]|1[0-2])";

    /**
     * <p>Regular expression string for the dayFrag:</p>
     *
     * <p>a numeral consisting of exactly two decimal digits.</p>
     *
     * <p>Note that this regex does not enforce the day-of-month constraint,
     * which states:</p>
     *
     * <p>The day value must be no more than 30 if month is one of 4, 6, 9,
     * or 11; no more than 28 if month is 2 and year is not divisible 4,
     * or is divisible by 100 but not by 400; and no more than 29 if month is
     * 2 and year is divisible by 400, or by 4 but not by 100.</p>
     */
    private final static String dayFrag = "(0[1-9]|[12][0-9]|3[01])";

    /**
     * <p>Regular expression string for the hourFrag:</p>
     *
     * <p>a numeral consisting of exactly two decimal digits.</p>
     */
    private final static String hourFrag = "([01][0-9]|2[0-3])";

    /**
     * <p>Regular expression string for the minuteFrag:</p>
     *
     * <p>a numeral consisting of exactly two decimal digits.</p>
     */
    private final static String minuteFrag = "([0-5][0-9])";

    /**
     * <p>Regular expression string for the secondFrag:</p>
     *
     * <p>a numeral consisting of exactly two decimal digits, or two decimal
     * digits, a decimal point, and one or more trailing digits.</p>
     */
    private final static String secondFrag = "([0-5][0-9])(\\.([0-9]+))?";

    /**
     * <p>Regular expression string for the endOfDayFrag:</p>
     *
     * <p>combines the {@link hourFrag}, {@link minuteFrag}, {@link minuteFrag},
     * and their separators to represent midnight of the day, which is the first
     * moment of the next day.</p>
     */
    private final static String endOfDayFrag = String.format("(%s:%s:%s|(24:00:00(\\.0+)?))", hourFrag, minuteFrag, secondFrag);

    /**
     * <p>Regular expression string for the timezoneFrag: </p>
     *
     * <p>if present, specifies an offset between UTC and local time. Time zone
     * offsets are a count of minutes (expressed in timezoneFrag  as a count of
     * hours and minutes) that are added or subtracted from UTC time to get the
     * "local" time.  'Z' is an alternative representation of the time zone
     * offset '00:00', which is, of course, zero minutes from UTC.</p>
     */
    private final static String timezoneFrag = "(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";

    /**
     * Concatenation of the {@link yearFrag}, {@link monthFrag},
     * {@link dayFrag}, {@link endOfDayFrag}, {@link timezoneFrag}, and their
     * separators.
     */
    private final static String combined = String.format("%s-%s-%sT%s%s", yearFrag, monthFrag, dayFrag, endOfDayFrag, timezoneFrag);

    private final static Pattern XSD_DATETIME = Pattern.compile(combined);


    /**
     *
     * @param input an XML Schema 1.1 dateTime
     * @return a DateTime representing the input
     * @see http://www.w3.org/TR/xmlschema11-2/#dateTime
     */
    public static DateTime parseXSDDateTime(String input) {
        Matcher m = XSD_DATETIME.matcher(input);
        if (!m.find()) {
            throw new IllegalArgumentException(input + " is not a valid XML Schema 1.1 dateTime.");
        }

        int year = Integer.parseInt(m.group(1));
        int month = Integer.parseInt(m.group(3));
        int day = Integer.parseInt(m.group(4));

        int hour = 0, minute = 0, second = 0, millis = 0;
        boolean hasEndOfDayFrag = (m.group(11) != null);
        if (!hasEndOfDayFrag) {
            hour = Integer.parseInt(m.group(6));
            minute = Integer.parseInt(m.group(7));
            second = Integer.parseInt(m.group(8));
            if (m.group(10) != null && !m.group(10).isEmpty()) {
                millis = Integer.parseInt(m.group(10));
            }
        }

        DateTimeZone zone = null;
        if (m.group(13) != null) {
            String tmp = m.group(13);
            if (tmp.equals("Z")) {
                tmp = "+00:00";
            }
            zone = DateTimeZone.forID(tmp);
        }
        DateTime dt = new DateTime(year, month, day, hour, minute, second, millis, zone);

        if (hasEndOfDayFrag) {
            return dt.plusDays(1);
        }
        return dt;
    }

    /**
     *
     * @param date
     * @return An xsd:dateTime (in UTC) representation of date.
     */
    public static String getXSDDateTime(Date date) {
        return getXSDDateTime(new DateTime(date));
    }

    /**
     *
     * @param date
     * @return An xsd:dateTime (in UTC) representation of date.
     */
    public static String getXSDDateTime(DateTime date) {
        if (date.getMillis() == 0) {
            return date.withZone(DateTimeZone.UTC).toString(ISODateTimeFormat.dateTimeNoMillis());
        } else {
            return date.withZone(DateTimeZone.UTC).toString(ISODateTimeFormat.dateTime());
        }
    }
}
