/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.yourmediashelf.fedora.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Date and time utility methods.
 *
 * @author Edwin Shin
 */
public class DateUtility {

    /**
     * <p>
     * Regular expression string for the yearFrag:
     * </p>
     * <p>
     * a numeral consisting of at least four decimal digits, optionally preceded
     * by a minus sign; leading '0' digits are prohibited except to bring the
     * digit count up to four.
     * </p>
     */
    private final static String yearFrag = "(-?([1-9][0-9]{3,}|0[0-9]{3}))";

    /**
     * <p>
     * Regular expression string for the monthFrag:
     * </p>
     * <p>
     * a numeral consisting of exactly two decimal digits.
     * </p>
     */
    private final static String monthFrag = "(0[1-9]|1[0-2])";

    /**
     * <p>
     * Regular expression string for the dayFrag:
     * </p>
     * <p>
     * a numeral consisting of exactly two decimal digits.
     * </p>
     * <p>
     * Note that this regex does not enforce the day-of-month constraint, which
     * states:
     * </p>
     * <p>
     * The day value must be no more than 30 if month is one of 4, 6, 9, or 11;
     * no more than 28 if month is 2 and year is not divisible 4, or is
     * divisible by 100 but not by 400; and no more than 29 if month is 2 and
     * year is divisible by 400, or by 4 but not by 100.
     * </p>
     */
    private final static String dayFrag = "(0[1-9]|[12][0-9]|3[01])";

    /**
     * <p>
     * Regular expression string for the hourFrag:
     * </p>
     * <p>
     * a numeral consisting of exactly two decimal digits.
     * </p>
     */
    private final static String hourFrag = "([01][0-9]|2[0-3])";

    /**
     * <p>
     * Regular expression string for the minuteFrag:
     * </p>
     * <p>
     * a numeral consisting of exactly two decimal digits.
     * </p>
     */
    private final static String minuteFrag = "([0-5][0-9])";

    /**
     * <p>
     * Regular expression string for the secondFrag:
     * </p>
     * <p>
     * a numeral consisting of exactly two decimal digits, or two decimal
     * digits, a decimal point, and one or more trailing digits.
     * </p>
     */
    private final static String secondFrag = "([0-5][0-9])(\\.([0-9]+))?";

    /**
     * <p>
     * Regular expression string for the endOfDayFrag:
     * </p>
     * <p>
     * combines the {@link hourFrag}, {@link minuteFrag}, {@link minuteFrag},
     * and their separators to represent midnight of the day, which is the first
     * moment of the next day.
     * </p>
     */
    private final static String endOfDayFrag = String.format(
            "(%s:%s:%s|(24:00:00(\\.0+)?))", hourFrag, minuteFrag, secondFrag);

    /**
     * <p>
     * Regular expression string for the timezoneFrag:
     * </p>
     * <p>
     * if present, specifies an offset between UTC and local time. Time zone
     * offsets are a count of minutes (expressed in timezoneFrag as a count of
     * hours and minutes) that are added or subtracted from UTC time to get the
     * "local" time. 'Z' is an alternative representation of the time zone
     * offset '00:00', which is, of course, zero minutes from UTC.
     * </p>
     */
    private final static String timezoneFrag =
            "(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";

    /**
     * Concatenation of the {@link yearFrag}, {@link monthFrag}, {@link dayFrag}
     * , {@link endOfDayFrag}, {@link timezoneFrag}, and their separators.
     */
    private final static String combined = String.format("%s-%s-%sT%s%s",
            yearFrag, monthFrag, dayFrag, endOfDayFrag, timezoneFrag);

    private final static Pattern XSD_DATETIME = Pattern.compile(combined);

    private final static ConcurrentMap<String, DateTimeFormatter> formatters =
            new ConcurrentHashMap<String, DateTimeFormatter>();

    /**
     * Parses lexical representations of xsd:dateTimes, e.g.
     * "2010-01-31T14:21:03.001Z". Fractional seconds and timezone offset are
     * optional.
     *
     * <p>Note: fractional seconds are only supported to three digits of
     * precision.</p>
     *
     * @param input
     *        an XML Schema 1.1 dateTime
     * @return a DateTime representing the input
     * @see "http://www.w3.org/TR/xmlschema11-2/#dateTime"
     */
    public static DateTime parseXSDDateTime(String input) {
        Matcher m = XSD_DATETIME.matcher(input);
        if (!m.find()) {
            throw new IllegalArgumentException(input +
                    " is not a valid XML Schema 1.1 dateTime.");
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
            // Parse fractional seconds
            // m.group(9), if not null/empty should be Strings such as ".5" or
            // ".050" which convert to 500 and 50, respectively.
            if (m.group(9) != null && !m.group(9).isEmpty()) {
                // parse as Double as a quick hack to drop trailing 0s.
                // e.g. ".0500" becomes 0.05
                double d = Double.parseDouble(m.group(9));

                // Something like the following would allow for int-sized
                // precision, but joda-time 1.6 only supports millis (i.e. <= 999).
                // see: org.joda.time.field.FieldUtils.verifyValueBounds
                //   int digits = String.valueOf(d).length() - 2;
                //   fractionalSeconds = (int) (d * Math.pow(10, digits));
                millis = (int) (d * 1000);
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
        DateTime dt =
                new DateTime(year, month, day, hour, minute, second, millis,
                        zone);

        if (hasEndOfDayFrag) {
            return dt.plusDays(1);
        }
        return dt;
    }

    /**
     * Convenience method that accepts a {@link java.util.Date}.
     *
     * @param date
     * @return An xsd:dateTime (in UTC) representation of date.
     * @see #getXSDFormatter(DateTime)
     */
    public static String getXSDDateTime(Date date) {
        return getXSDDateTime(new DateTime(date));
    }

    /**
     * Formats a {@link DateTime} as an xsd:dateTime in canonical form.
     *
     * <p>Note: fractional seconds are only supported to a maximum of three
     * digits.</p>
     *
     * @param dateTime
     * @return An xsd:dateTime (in UTC) representation of date.
     * @see "http://www.w3.org/TR/xmlschema11-2/#dateTime"
     */
    public static String getXSDDateTime(DateTime dateTime) {
        return dateTime.withZone(DateTimeZone.UTC).toString(
                getXSDFormatter(dateTime));
    }

    public static DateTimeFormatter getXSDFormatter(DateTime date) {
        int len = 0;
        int millis = date.getMillisOfSecond();

        if (millis > 0) {
            // 0.050 becomes .05 (up to three digits, dropping trailing 0s)
            DecimalFormat df = new DecimalFormat(".###");
            double d = millis / 1000.0;
            len = String.valueOf(df.format(d)).length() - 1;
        }
        return getXSDFormatter(len);
    }

    /**
     * Returns an xsd:dateTime formatter with the specified millisecond precision.
     *
     * @param millisLength number of digits of millisecond precision. Currently,
     * only 0-3 are valid arguments.
     * @return a formatter for yyyy-MM-dd'T'HH:mm:ss[.SSS]Z
     */
    public static DateTimeFormatter getXSDFormatter(int millisLength) {
        String key = String.valueOf(millisLength);
        if (formatters.get(key) == null) {
            DateTimeFormatterBuilder bldr =
                    new DateTimeFormatterBuilder().appendYear(4, 9)
                            .appendLiteral('-').appendMonthOfYear(2)
                            .appendLiteral('-').appendDayOfMonth(2)
                            .appendLiteral('T').appendHourOfDay(2)
                            .appendLiteral(':').appendMinuteOfHour(2)
                            .appendLiteral(':').appendSecondOfMinute(2);
            if (millisLength > 0) {
                bldr =
                        bldr.appendLiteral('.').appendFractionOfSecond(
                                millisLength, millisLength);
            }
            bldr = bldr.appendTimeZoneOffset("Z", true, 2, 4);
            formatters.putIfAbsent(key, bldr.toFormatter());
        }
        return formatters.get(key);
    }
}
