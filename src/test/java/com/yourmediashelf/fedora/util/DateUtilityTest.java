package com.yourmediashelf.fedora.util;

import static org.junit.Assert.assertEquals;
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
    public void testFormatting() throws Exception {
        String s = "1986-11-05T05:57:17.3Z";
        DateTime dt = DateUtility.parseXSDDateTime(s);
        System.out.println("f: " + DateUtility.getXSDDateTime(dt));
    }

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

    @Test
    public void testMillis() throws Exception {
        String m1 = "1999-12-31T22:33:44.500Z";
        DateTime dt1 = DateUtility.parseXSDDateTime(m1);
        assertEquals(m1, DateUtility.getXSDDateTime(dt1));

        String m2 = "1999-12-31T22:33:44.050Z";
        DateTime dt2 = DateUtility.parseXSDDateTime(m2);
        assertEquals(m2, DateUtility.getXSDDateTime(dt2));

        String m3 = "1999-12-31T22:33:44.005Z";
        DateTime dt3 = DateUtility.parseXSDDateTime(m3);
        assertEquals(m3, DateUtility.getXSDDateTime(dt3));
    }

}
