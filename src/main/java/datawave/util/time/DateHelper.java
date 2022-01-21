package datawave.util.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * This class validates date ranges and converts Date objects to and from Strings in yyyyMMdd format in a way that is not dependent on local settings or
 * calendar, timezone, or locale by always using the Zulu timezone and US locale. This class is useful, for example, for converting Date objects received from
 * the webservice in a Query object into yyyyMMdd strings for sending to Accumulo as part of the rowId for the range.
 */
public class DateHelper {
    
    public static final Date MIN_SUPPORTED_DATE = new Date(-62135596800000L); // one millisecond after 0000/12/31; 0001/01/01Z
    public static final Date MAX_SUPPORTED_DATE = new Date(253402300799999L); // one millisecond before 10000/01/01; 9999/12/31Z
    
    private static final String DATE_RANGE_FORMAT = "[%s (%s), %s (%s)]";
    private static final String ERROR_BEGIN_DATE_SHOULD_NOT_BE_NULL = "begin date should not be null; specified range is %s";
    private static final String ERROR_END_DATE_SHOULD_NOT_BE_NULL = "end date should not be null;  specified range is %s";
    private static final String ERROR_BEGIN_DATE_SHOULD_NOT_BE_GREATER_END_DATE = "begin date should not be greater than end date; specified range is %s";
    private static final String ERROR_BEGIN_DATE_LESS_MIN_SUPPORTED_DATE = "begin date less than min supported date; specified range is %s";
    private static final String ERROR_END_DATE_GREATER_MAX_SUPPORTED_DATE = "end date greater than max supported date;  specified range is %s";
    
    public static final String DATE_FORMAT_STRING_TO_DAY = "yyyyMMdd";
    private static final DateTimeFormatter DTF_day = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING_TO_DAY).withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter DTF_day_GMT = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING_TO_DAY).withZone(ZoneOffset.UTC);
    
    public static final String DATE_FORMAT_STRING_TO_HOUR = "yyyyMMddHH";
    private static final DateTimeFormatter DTF_hour = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING_TO_HOUR).withZone(ZoneOffset.UTC);
    
    public static final String DATE_FORMAT_STRING_TO_SECONDS = "yyyyMMddHHmmss";
    private static final DateTimeFormatter DTF_Seconds = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING_TO_SECONDS).withZone(ZoneOffset.UTC);
    
    public static final String DATE_FORMAT_STRING_TO_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final DateTimeFormatter DTF_Full = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING_TO_FULL).withZone(ZoneOffset.UTC);
    
    public static final String DATE_FORMAT_STRING_TO_DETAILS = "EEE MMM d HH:mm:ss zzz yyyy";
    private static final DateTimeFormatter DTF_Details = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING_TO_DETAILS).withZone(ZoneOffset.UTC);
    
    public static final String DATE_FORMAT_STRING_8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final DateTimeFormatter DTF_8601 = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING_8601).withZone(ZoneOffset.UTC);
    
    public static final String DATE_FORMAT_PURGE_CONSTANT = "yyyyMMddHHmmss.SSS";
    private static final DateTimeFormatter DTF_Purge = DateTimeFormatter.ofPattern(DATE_FORMAT_PURGE_CONSTANT).withZone(ZoneOffset.UTC);
    
    /**
     * Return a string representing the given date in the given pattern in a consistent way not dependent on local settings for calendar, timezone, or locale by
     * using Zulu timezone and US locale
     *
     * @param date
     * @param pattern
     * @return The formatted custom date
     */
    public static String formatCustom(Date date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC).format(date.toInstant());
    }
    
    /**
     * Return a string representing the given date in the given pattern in a consistent way not dependent on local settings for calendar, timezone, or locale by
     * using Zulu timezone and US locale
     *
     * @param inMillis
     * @param pattern
     * @return The formatted custom date
     */
    public static String formatCustom(long inMillis, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC).format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given date in yyyyMMdd format in a consistent way not dependent on local settings for calendar, timezone, or locale by
     * using Zulu timezone and US locale.
     *
     * @param date
     * @return the formatted date
     */
    public static String format(Date date) {
        return DTF_day.format(date.toInstant());
    }
    
    /**
     * Return a string representing the given GMT date in yyyyMMdd format in a consistent way not dependent on local settings for calendar, timezone, or locale
     * by using Zulu timezone and US locale.
     *
     * @param date
     * @return the formatted date
     * @deprecated
     */
    public static String formatWithGMT(Date date) {
        return DTF_day_GMT.format(date.toInstant());
    }
    
    /**
     * Return a string representing the given time (in millis) in yyyyMMdd format in a consistent way not dependent on local settings for calendar, timezone, or
     * locale by using Zulu timezone and US locale.
     *
     * @param inMillis
     * @return the formatted date
     */
    public static String format(long inMillis) {
        return DTF_day.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given time (in millis) in yyyyMMddhh format in a consistent way not dependent on local settings for calendar, timezone,
     * or locale by using Zulu timezone and US locale.
     *
     * @param inMillis
     * @return the formatted date
     */
    public static String formatToHour(long inMillis) {
        return DTF_hour.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given time (in millis) in yyyyMMddhh format in a consistent way not dependent on local settings for calendar, timezone,
     * or locale by using Zulu timezone and US locale.
     *
     * @param date
     * @return the formatted date
     */
    public static String formatToHour(Date date) {
        return DTF_hour.format(date.toInstant());
    }
    
    /**
     * Return a string representing the given long representation of date in yyyyMMddHH format in a consistent way not dependent on local settings for calendar,
     * timezone, or locale by using Zulu timezone and US locale.
     *
     * @param inMillis
     * @return the formatted date
     */
    public static String formatHour(long inMillis) {
        return DTF_hour.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given long representation of date in yyyyMMddHHmmss format in a consistent way not dependent on local settings for
     * calendar, timezone, or locale by using Zulu timezone and US locale.
     *
     * @param inMillis
     * @return the formatted date
     */
    public static String formatToTimeExactToSeconds(long inMillis) {
        return DTF_Seconds.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given date in yyyyMMddHHmmss format in a consistent way not dependent on local settings for calendar, timezone, or
     * locale by using Zulu timezone and US locale.
     *
     * @param date
     * @return the formatted date
     */
    public static String formatToTimeExactToSeconds(Date date) {
        return DTF_Seconds.format(date.toInstant());
    }
    
    /**
     * Return a string representing the given date in EEE MMM d HH:mm:ss zzz yyyy format in a consistent way not dependent on local settings for calendar,
     * timezone, or locale by using Zulu timezone and US locale.
     *
     * @param inMillis
     * @return
     */
    public static String formatToDetails(long inMillis) {
        return DTF_Details.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given date in EEE MMM d HH:mm:ss zzz yyyy format in a consistent way not dependent on local settings for calendar,
     * timezone, or locale by using Zulu timezone and US locale.
     *
     * @param date
     * @return
     */
    public static String formatToDetails(Date date) {
        return DTF_Details.format(date.toInstant());
    }
    
    /**
     * Return a string representing the given date in yyyy-MM-dd'T'HH:mm:ss.SSSZ format in a consistent way not dependent on local settings for calendar,
     * timezone, or locale by using Zulu timezone and US locale.
     *
     * @param inMillis
     * @return
     */
    public static String formatToFull(long inMillis) {
        return DTF_Full.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given date in yyyy-MM-dd 'T' HH:mm:ss.SSSZ format in a consistent way not dependent on local settings for calendar,
     * timezone, or locale by using Zulu timezone and Us locale.
     *
     * @param date
     * @return
     */
    public static String formatToFull(Date date) {
        return DTF_Full.format(date.toInstant());
    }
    
    /**
     * Return a string representing the given date in yyyyMMddHHmmss.SSS format in a consistent way not dependent on local settings for calendar, timezone, or
     * locale by using Zulu timezone and US locale
     *
     * @param inMillis
     * @return
     */
    public static String formatPurge(long inMillis) {
        return DTF_Purge.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Return a string representing the given date in yyyyMMddHHmmss.SSS format in a consistent way not dependent on local settings for calendar, timezone, or
     * locale by using Zulu timezone and US locale
     *
     * @param date
     * @return
     */
    public static String formatPurge(Date date) {
        return DTF_Purge.format(date.toInstant());
    }
    
    /**
     * Converts a String in yyyyMMdd format to a Date object in a consistent way not dependent on local settings for calendar, timezone, or locale by using Zulu
     * timezone and US locale.
     *
     * @param date
     * @return the {@code Date} object
     */
    public static Date parse(String date) {
        return lenientParseHelper(date, DTF_day, DATE_FORMAT_STRING_TO_DAY, false);
    }
    
    /**
     * Converts a string in a custom format to a Date object in a consistent way not dependent on local settings for calendar, timezone, or locale by using Zulu
     * timezone and US locale.
     *
     * @param date
     * @param pattern
     * @return the {@code Date} object
     */
    public static Date parseCustom(String date, String pattern) {
        return lenientParseHelper(date, DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC), pattern, true);
    }
    
    /**
     * Converts a String in yyyyMMddHH format to a Date object in a consistent way not dependent on local settings for calendar, timezone, or locale by using
     * Zulu timezone and US locale.
     *
     * @param date
     * @return the {@code Date} object
     */
    public static Date parseHour(String date) {
        return lenientParseHelper(date, DTF_hour, DATE_FORMAT_STRING_TO_HOUR, true);
    }
    
    /**
     * Converts a string in EEE MM d HH:mm:ss zzz yyyy format to a Date object in a consistent way not dependent on local settings for calendar, timezone, or
     * locale by using Zulu timezone and US locale.
     *
     * @param date
     * @return the {@code Date} object
     */
    public static Date parseDetails(String date) {
        return lenientParseHelper(date, DTF_Details, DATE_FORMAT_STRING_TO_DETAILS, true);
    }
    
    /**
     * Return a string representing the given date in yyyy-MM-dd 'T' HH:mm:ss.SSSZ format in a consistent way not dependent on local settings for calendar,
     * timezone, or locale by using Zulu timezone and US locale
     *
     * @param date
     * @return
     */
    public static Date parseFull(String date) {
        return lenientParseHelper(date, DTF_Full, DATE_FORMAT_STRING_TO_FULL, true);
    }
    
    public static Date parsePurge(String date) {
        return lenientParseHelper(date, DTF_Purge, DATE_FORMAT_PURGE_CONSTANT, true);
    }
    
    /**
     * Only use this for formats that can allow for leniency (i.e. not ISO standard formats).
     */
    private static Date lenientParseHelper(String date, DateTimeFormatter parser, String formatStr, boolean hasTime) {
            String lenientDate = convertToLenient(date, formatStr);
            if (hasTime) {
                return Date.from(ZonedDateTime.parse(lenientDate, parser).toInstant());
            } else {
                return Date.from(LocalDate.parse(lenientDate, parser).atStartOfDay(parser.getZone()).toInstant()); // Fails testFailIfMissingDigits test
            }
    }
    
    /*
     * At the time this was written, some code used SimpleDateFormat's lenient parsing. For example: a SimpleDateFormat of yyyyMMdd would ignore the last 3
     * characters of 20140101_01, using 20140101. When that code was converted to use DateHelper, DateHelper threw an exception because it relies upon Joda time
     * which does strict parsing. DateHelper needed to be adjusted to ensure that those one-off date strings are parsed correctly without throwing exceptions,
     * mainly because it's unknown how many places need the lenient parsing
     */
    private static String convertToLenient(String date, final String formatString) {
        return date.substring(0, Math.min(date.length(), formatString.length()));
    }
    
    /**
     * Converts a String in yyyyMMddHHmmss format to a Date object in a consistent way not dependent on local settings for calendar, timezone, or locale by
     * using Zulu timezone and US locale.
     *
     * @param date
     * @return the {@code Date} object
     */
    public static Date parseTimeExactToSeconds(String date) {
        return lenientParseHelper(date, DTF_Seconds, DATE_FORMAT_STRING_TO_SECONDS, true);
    }
    
    /**
     * Converts a String in yyyyMMdd format to a GMT Date object in a consistent way not dependent on local settings for calendar, timezone, or locale by using
     * Zulu timezone and US locale.
     *
     * @param date
     * @return the {@code Date} object
     * @deprecated
     */
    public static Date parseWithGMT(String date) {
        return lenientParseHelper(date, DTF_day_GMT, DATE_FORMAT_STRING_TO_DAY, false);
    }
    
    /**
     * Return a string representing the given date in 8601 format
     *
     * @param date
     * @return the formatted date
     */
    public static String format8601(Date date) {
        return DTF_8601.format(date.toInstant());
    }
    
    /**
     * Return a string representing the given date in 8601 format
     *
     * @param inMillis
     * @return the formatted date
     */
    public static String format8601(long inMillis) {
        return DTF_8601.format(Instant.ofEpochMilli(inMillis));
    }
    
    /**
     * Converts a String in 8601 format to a Date object
     *
     * @param date
     * @return the {@code Date} object
     */
    public static Date parse8601(String date) {
        return Date.from(ZonedDateTime.parse(date, DTF_8601).toInstant());
    }
    
    /**
     * Convenience method for wrapping the static validateDateRange method in a non-static method.
     *
     * @param beginDate
     * @param endDate
     */
    public static void validateRange(Date beginDate, Date endDate) {
        validateDateRange(beginDate, endDate);
    }
    
    /**
     * This method validates that the specified date range is valid and throws an exception if it is not. Specifically, it:
     * <ol>
     * <li>checks that beginDate is not null or throws a NullPointerException</li>
     * <li>checks that endDate is not null or throws a NullPointerException</li>
     * <li>checks that beginDate &lt;= endDate or throws an IllegalArgumentException</li>
     * <li>checks that both beginDate and endDate are within the allowable date range for DATAWAVE, which is one millisecond after -0001/12/31 (0001/01/01) and
     * one millisecond before 10000/01/01 (9999/12/31) or throws an IllegalArgumentException otherwise</li>
     * </ol>
     *
     * @param beginDate
     * @param endDate
     * @throws NullPointerException
     *             if beginDate or endDate is null.
     * @throws IllegalArgumentException
     *             if beginDate &gt; endDate or either beginDate or endDate are outside the allowable date range for DATAWAVE.
     */
    public static void validateDateRange(Date beginDate, Date endDate) {
        
        /*
         * Get a string that looks like [yyyyMMdd (timeInMillis), yyyyMMdd (timeInMillis)] in case we have to throw an exception.
         */
        String dateRangeStr = formatDateRange(beginDate, endDate);
        
        /*
         * Validate beginDate not null.
         */
        if (beginDate == null) {
            throw new NullPointerException(String.format(ERROR_BEGIN_DATE_SHOULD_NOT_BE_NULL, dateRangeStr));
        }
        
        /*
         * Validate endDate not null.
         */
        if (endDate == null) {
            throw new NullPointerException(String.format(ERROR_END_DATE_SHOULD_NOT_BE_NULL, dateRangeStr));
        }
        
        /*
         * Validate beginDate <= endDate.
         */
        if (beginDate.getTime() > endDate.getTime()) {
            throw new IllegalArgumentException(String.format(ERROR_BEGIN_DATE_SHOULD_NOT_BE_GREATER_END_DATE, dateRangeStr));
        }
        
        /*
         * Validate beginDate >= MIN_SUPPORTED_DATE; no need to validate right end of range since already validated beginDate <= endDate and going to check
         * endDate next.
         */
        if (beginDate.getTime() < MIN_SUPPORTED_DATE.getTime()) {
            throw new IllegalArgumentException(String.format(ERROR_BEGIN_DATE_LESS_MIN_SUPPORTED_DATE, dateRangeStr));
        }
        
        /*
         * Validate endDate <= MAX_SUPPORTED_DATE; no need to validate left end of range since already validated beginDate <= endDate and already validated
         * beginDate is not null.
         */
        if (endDate.getTime() > MAX_SUPPORTED_DATE.getTime()) {
            throw new IllegalArgumentException(String.format(ERROR_END_DATE_GREATER_MAX_SUPPORTED_DATE, dateRangeStr));
        }
    }
    
    /**
     * Returns a String that looks like [yyyyMMdd (timeInMillis), yyyyMMdd (timeInMillis)] for the date range. The returned String will contain null (null) for
     * the begin and end dates if either is null. The yyyyMMdd format will look like -yyyyMMdd if the date is before the MIN_SUPPORTED_DATE (0001/01/01).
     *
     * @param beginDate
     * @param endDate
     * @return the formatted dates
     */
    private static String formatDateRange(Date beginDate, Date endDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String beginDateYyyyMMdd = beginDate == null ? "null" : dtf.format(beginDate.toInstant());
        String endDateYyyyMMdd = endDate == null ? "null" : dtf.format(endDate.toInstant());
        String beginDateMillis = beginDate == null ? "null" : "" + beginDate.getTime();
        String endDateMillis = endDate == null ? "null" : "" + endDate.getTime();
        return String.format(Locale.US, DATE_RANGE_FORMAT, beginDateYyyyMMdd, beginDateMillis, endDateYyyyMMdd, endDateMillis);
    }
    
    /**
     * Adds the given number of days to the given date and returns the result.
     *
     * @param date
     * @param days
     * @return the new date
     */
    public static Date addDays(Date date, int days) {
        return Date.from(date.toInstant().plus(days, ChronoUnit.DAYS));
    }
    
    /**
     * Adds the given number of hours to the given date and returns the result.
     *
     * @param date
     * @param hours
     * @return the new Date.
     */
    public static Date addHours(Date date, int hours) {
        return Date.from(date.toInstant().plus(hours, ChronoUnit.HOURS));
    }
    
    /**
     * Returns whether or not the given date occurred within the given hour. The hour must be a number of 0 through 23.
     *
     * @param date
     * @param hour
     * @return {@code true} if the given date occurred within the given hour. Otherwise returns {@code false}.
     */
    public static boolean dateAtHour(Date date, int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be a number of 0 through 23.");
        }
        return date.toInstant().atZone(ZoneOffset.UTC).get(ChronoField.HOUR_OF_DAY) == hour;
    }
}
