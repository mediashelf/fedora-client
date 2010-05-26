package com.yourmediashelf.fedora.util;

import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 *
 *
 * @author Edwin Shin
 */
public class DateUtilityTest {

    @Test
    public void testTimezones() throws Exception {
        String s1 = "2002-10-10T12:00:00-05:00";
        DateTime dt1 = DateUtility.parseXSDDateTime(s1);
        String s2 = "2002-10-10T17:00:00Z";
        DateTime dt2 = DateUtility.parseXSDDateTime(s2);
        assertTrue(dt1.isEqual(dt2));

        String s3 = "2002-10-10T12:00:00Z";
        DateTime dt3 = DateUtility.parseXSDDateTime(s3);
        assertTrue(dt2.minusHours(5).isEqual(dt3));
    }

    @Test
    public void testZeroYearAndEndFrag() throws Exception {
        String s4 = "-0001-12-31T24:00:00Z";
        DateTime dt4 = DateUtility.parseXSDDateTime(s4);
        String s5 = "0000-01-01T00:00:00Z";
        DateTime dt5 = DateUtility.parseXSDDateTime(s5);
        assertTrue(dt4.isEqual(dt5));
    }

}
