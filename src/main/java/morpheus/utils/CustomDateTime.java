package morpheus.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a custom date-time object that stores a {@link LocalDate}
 * with optional hour and minute fields. Accepts multiple date and time
 * input formats for flexibility.
 */
public class CustomDateTime implements Comparable<CustomDateTime> {
    private static final DateTimeFormatter DATE_PRETTY =
            DateTimeFormatter.ofPattern("d MMM yyyy");

    private static final String ERROR_DATE_FORMAT =
            "Invalid date. Accepted formats: d/m/yyyy, d-m-yyyy, d.m.yyyy, d/m, d-m, d.m, "
                    + "ddmmyy, ddmmyyyy, d MMM yyyy";
    private static final String ERROR_TIME_FORMAT =
            "Invalid time. Accepted formats: hh:mm, hh.mm, hhmm, hmm, h:mm AM/PM, "
                    + "h.mm AM/PM, hAM/PM, hhAM/PM";

    private final LocalDate date;
    private final Integer hour;
    private final Integer minute;
    private final boolean hasTime;

    /**
     * Creates a {@code CustomDateTime} by parsing a string input.
     *
     * @param input a date or date-time string in a supported format
     * @throws IllegalArgumentException if the input cannot be parsed
     */
    public CustomDateTime(String input) {
        CustomDateTime parsed = parse(input);
        this.date = parsed.date;
        this.hour = parsed.hour;
        this.minute = parsed.minute;
        this.hasTime = parsed.hasTime;
    }

    /**
     * Creates a {@code CustomDateTime} with a specific date and time.
     *
     * @param date   the date
     * @param hour   the hour (0–23)
     * @param minute the minute (0–59)
     */
    public CustomDateTime(LocalDate date, Integer hour, Integer minute) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.hasTime = true;
    }

    /**
     * Creates a {@code CustomDateTime} with a date only (no time).
     *
     * @param date the date
     */
    public CustomDateTime(LocalDate date) {
        this.date = date;
        this.hour = null;
        this.minute = null;
        this.hasTime = false;
    }

    /**
     * Attempts to parse an input string into a {@code CustomDateTime},
     * supporting multiple formats.
     *
     * @param input the date/time string
     * @return a parsed {@code CustomDateTime}
     * @throws IllegalArgumentException if none of the supported formats match
     */
    private static CustomDateTime parse(String input) {
        validateNotEmpty(input);
        String trimmed = input.trim();

        if (isLongDateTimeFormat(trimmed)) {
            return parseLongDateTime(trimmed);
        }
        if (isLongDateFormat(trimmed)) {
            return parseLongDate(trimmed);
        }
        return parseNumericOrDelimitedDateTime(trimmed);
    }

    /** Ensures the input string is not null or empty. */
    private static void validateNotEmpty(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Date/time string cannot be empty");
        }
    }

    /** @return true if string matches "d MMM yyyy, h:mm a" */
    private static boolean isLongDateTimeFormat(String s) {
        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", java.util.Locale.ENGLISH);
            LocalDateTime.parse(s, f);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** @return true if string matches "d MMM yyyy" */
    private static boolean isLongDateFormat(String s) {
        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMM yyyy", java.util.Locale.ENGLISH);
            LocalDate.parse(s, f);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Parses "d MMM yyyy, h:mm a". */
    private static CustomDateTime parseLongDateTime(String s) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", java.util.Locale.ENGLISH);
        LocalDateTime dt = LocalDateTime.parse(s, f);
        return new CustomDateTime(dt.toLocalDate(), dt.getHour(), dt.getMinute());
    }

    /** Parses "d MMM yyyy". */
    private static CustomDateTime parseLongDate(String s) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("d MMM yyyy", java.util.Locale.ENGLISH);
        LocalDate d = LocalDate.parse(s, f);
        return new CustomDateTime(d);
    }

    /** Fallback: numeric formats like ddmmyy, ddmmyyyy, d/m/yyyy, etc. */
    private static CustomDateTime parseNumericOrDelimitedDateTime(String s) {
        try {
            String[] parts = s.split("\\s+", 2);
            LocalDate date = parseDateFlexible(parts[0]);
            if (parts.length == 2) {
                int[] time = parseTimeFlexible(parts[1]);
                return new CustomDateTime(date, time[0], time[1]);
            }
            return new CustomDateTime(date);
        } catch (Exception e) {
            throw new IllegalArgumentException(ERROR_DATE_FORMAT);
        }
    }

    /**
     * Parses a date string in one of the supported numeric or delimited formats.
     *
     * @param s the date string
     * @return a {@link LocalDate} object
     */
    private static LocalDate parseDateFlexible(String s) {
        if (isSixDigitFormat(s)) {
            return parseSixDigitDate(s);
        }
        if (isEightDigitFormat(s)) {
            return parseEightDigitDate(s);
        }
        if (isDelimitedFormat(s)) {
            return parseDelimitedDate(s);
        }
        throw new IllegalArgumentException(ERROR_DATE_FORMAT);
    }

    /** @return true if the string is a 6-digit ddmmyy format */
    private static boolean isSixDigitFormat(String s) {
        return s.matches("\\d{6}");
    }

    /** @return true if the string is an 8-digit ddmmyyyy format */
    private static boolean isEightDigitFormat(String s) {
        return s.matches("\\d{8}");
    }

    /** @return true if the string contains '/', '-' or '.' as delimiters */
    private static boolean isDelimitedFormat(String s) {
        return s.contains("/") || s.contains("-") || s.contains(".");
    }

    /** Parses a 6-digit date string (ddmmyy). */
    private static LocalDate parseSixDigitDate(String s) {
        int day = toInt(s.substring(0, 2));
        int month = toInt(s.substring(2, 4));
        int yy = toInt(s.substring(4, 6));
        int year = normalizeTwoDigitYear(yy);
        return LocalDate.of(year, month, day);
    }

    /** Parses an 8-digit date string (ddmmyyyy). */
    private static LocalDate parseEightDigitDate(String s) {
        int day = toInt(s.substring(0, 2));
        int month = toInt(s.substring(2, 4));
        int year = toInt(s.substring(4, 8));
        return LocalDate.of(year, month, day);
    }

    /** Parses a delimited date string (d/m/yyyy, d-m, etc.). */
    private static LocalDate parseDelimitedDate(String s) {
        String[] dmy = s.split("[/.-]");
        try {
            if (dmy.length == 3) {
                return parseDmyWithYear(dmy);
            } else if (dmy.length == 2) {
                return parseDmyWithoutYear(dmy);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(ERROR_DATE_FORMAT);
        }
        throw new IllegalArgumentException(ERROR_DATE_FORMAT);
    }

    /** Parses a delimited date string with year (d/m/yyyy or d-m-yy). */
    private static LocalDate parseDmyWithYear(String[] dmy) {
        int day = toInt(dmy[0]);
        int month = toInt(dmy[1]);
        int yearVal = toInt(dmy[2]);

        int year = (dmy[2].length() == 2)
                ? normalizeTwoDigitYear(yearVal)
                : yearVal;

        return LocalDate.of(year, month, day);
    }

    /** Parses a delimited date string without year (d/m or d-m). Uses the current year. */
    private static LocalDate parseDmyWithoutYear(String[] dmy) {
        int day = toInt(dmy[0]);
        int month = toInt(dmy[1]);
        int year = LocalDate.now().getYear();
        return LocalDate.of(year, month, day);
    }

    /** Converts a string to an int. */
    private static int toInt(String s) {
        return Integer.parseInt(s);
    }

    /** Normalizes a 2-digit year into a 4-digit year (<=29 → 2000s, else 1900s). */
    private static int normalizeTwoDigitYear(int yy) {
        return (yy <= 29) ? 2000 + yy : 1900 + yy;
    }

    /**
     * Parses times in flexible formats like 930, 0930, 9:30, 19:45, 3 PM, etc.
     *
     * @param s the time string
     * @return array of two integers {hour, minute}
     */
    private static int[] parseTimeFlexible(String s) {
        String raw = s.trim().toUpperCase();
        boolean hasMeridiem = endsWithMeridiem(raw);
        boolean isPM = raw.endsWith("PM");
        String stripped = stripMeridiem(raw, hasMeridiem);

        int[] hm = parseNumericTime(stripped);

        if (hasMeridiem) {
            hm[0] = applyMeridiemAdjustment(hm[0], isPM);
        }

        validateTime(hm[0], hm[1]);
        return hm;
    }

    /** @return true if the string ends with AM/PM */
    private static boolean endsWithMeridiem(String t) {
        return t.endsWith("AM") || t.endsWith("PM");
    }

    /** Removes the AM/PM suffix if present. */
    private static String stripMeridiem(String t, boolean hasMeridiem) {
        return hasMeridiem ? t.substring(0, t.length() - 2).trim() : t;
    }

    /**
     * Parses numeric time strings: "9:30", "9.30", "0930", "930", "9", etc.
     *
     * @param t the numeric time string
     * @return array of two integers {hour, minute}
     */
    private static int[] parseNumericTime(String t) {
        try {
            if (t.contains(":") || t.contains(".")) {
                return splitHourMinute(t.split("[:.]"));
            }
            if (t.length() == 4) {
                return new int[] {toInt(t.substring(0, 2)), toInt(t.substring(2, 4))};
            }
            if (t.length() == 3) {
                return new int[] {toInt(t.substring(0, 1)), toInt(t.substring(1, 3))};
            }
            if (t.length() == 1 || t.length() == 2) {
                return new int[] {toInt(t), 0};
            }
            throw new IllegalArgumentException(ERROR_TIME_FORMAT);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_TIME_FORMAT);
        }
    }

    /** Converts string array to hour/minute ints. */
    private static int[] splitHourMinute(String[] parts) {
        if (parts.length != 2) {
            throw new IllegalArgumentException(ERROR_TIME_FORMAT);
        }
        return new int[] {toInt(parts[0]), toInt(parts[1])};
    }

    /** Adjusts hour according to AM/PM rules. */
    private static int applyMeridiemAdjustment(int hour, boolean isPM) {
        if (hour < 1 || hour > 12) {
            throw new IllegalArgumentException("Hour must be 1–12 for AM/PM format");
        }
        if (isPM && hour != 12) {
            return hour + 12;
        }
        if (!isPM && hour == 12) {
            return 0;
        }
        return hour;
    }

    /** Validates hour and minute ranges. */
    private static void validateTime(int h, int m) {
        if (h < 0 || h > 23 || m < 0 || m > 59) {
            throw new IllegalArgumentException("Hour must be 0–23 and minute 0–59");
        }
    }

    /** Formats a date into a human-friendly string like "12 Sep 2025". */
    private static String prettyDate(LocalDate d) {
        return d.format(DATE_PRETTY);
    }

    /** Formats a 24-hour time into a 12-hour AM/PM string. */
    private static String formatTime12(int hour24, int m) {
        String ampm = (hour24 >= 12) ? "PM" : "AM";
        int hour12 = hour24 % 12;
        if (hour12 == 0) {
            hour12 = 12;
        }
        return hour12 + ":" + String.format("%02d", m) + " " + ampm;
    }

    /**
     * Convert this {@code CustomDateTime} to a {@link LocalDateTime}.
     *
     * @return a {@link LocalDateTime} object
     */
    public LocalDateTime toLocalDateTime() {
        int h = (hour != null) ? hour : 0;
        int m = (minute != null) ? minute : 0;
        return LocalDateTime.of(date, java.time.LocalTime.of(h, m));
    }

    @Override
    public int compareTo(CustomDateTime other) {
        int cmp = this.date.compareTo(other.date);
        if (cmp != 0) {
            return cmp;
        }

        if (this.hour == null && other.hour != null) {
            return -1;
        }
        if (this.hour != null && other.hour == null) {
            return 1;
        }
        if (this.hour != null) {
            cmp = Integer.compare(this.hour, other.hour);
            if (cmp != 0) {
                return cmp;
            }
        }

        if (this.minute == null && other.minute != null) {
            return -1;
        }
        if (this.minute != null && other.minute == null) {
            return 1;
        }
        if (this.minute != null) {
            return Integer.compare(this.minute, other.minute);
        }

        return 0;
    }

    @Override
    public String toString() {
        return hasTime
                ? prettyDate(date) + ", " + formatTime12(hour, minute)
                : prettyDate(date);
    }
}
