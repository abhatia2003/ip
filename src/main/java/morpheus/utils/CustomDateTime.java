package morpheus.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a custom date-time object that stores a {@link LocalDate}
 * with optional hour and minute fields.
 * <p>
 * This class extends the functionality of {@link LocalDate} by allowing
 * partial date inputs (with or without a year) and flexible time formats.
 * It is primarily used by tasks (e.g., Deadline and Event) in the Morpheus
 * task manager.
 * </p>
 *
 * <h3>Supported date formats:</h3>
 * <ul>
 *   <li><code>d/m/yyyy</code> (e.g., 28/8/2025)</li>
 *   <li><code>d/m</code> (defaults to the current year)</li>
 * </ul>
 *
 * <h3>Supported time formats:</h3>
 * <ul>
 *   <li><code>hh:mm</code> or <code>h:mm</code> (24-hour or 12-hour)</li>
 *   <li><code>hhmm</code> (e.g., 0930, 1805)</li>
 *   <li><code>hmm</code> (e.g., 930)</li>
 *   <li><code>hAM/PM</code> or <code>hhAM/PM</code> (e.g., 8PM, 12AM)</li>
 *   <li><code>h:mm AM/PM</code> (e.g., 8:30 PM)</li>
 * </ul>
 *
 * This class also provides pretty-printing for user-facing output and
 * implements {@link Comparable} for natural ordering.
 *
 * Example usage:
 * <pre>
 * CustomDateTime d1 = new CustomDateTime("28/8/2025 1800");
 * CustomDateTime d2 = new CustomDateTime(LocalDate.now());
 * System.out.println(d1); // 28 Aug 2025, 6:00 PM
 * System.out.println(d2); // 28 Aug 2025
 * </pre>
 *
 * @author Aayush
 */
public class CustomDateTime implements Comparable<CustomDateTime> {
    private static final DateTimeFormatter DATE_PRETTY = DateTimeFormatter.ofPattern("d MMM yyyy");

    private final LocalDate date;
    private final Integer hour;
    private final Integer minute;
    private final boolean hasTime;

    /**
     * Creates a {@code CustomDateTime} by parsing a string input.
     *
     * @param date the date string to parse
     * @throws IllegalArgumentException if the string is null, empty, or invalid
     */
    public CustomDateTime(String date) {
        this.date = parse(date).date;
        this.hour = parse(date).hour;
        this.minute = parse(date).minute;
        this.hasTime = parse(date).hasTime;
    }

    /**
     * Creates a {@code CustomDateTime} with a specific date and time.
     *
     * @param date  the date
     * @param hour  the hour (0–23)
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

    private static CustomDateTime parse(String endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException(
                    "Deadline not found --> Add a deadline using the format \"/by dd/mm/yyyy\""
            );
        }
        String trimmed = endTime.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(
                    "Deadline not found --> Add a deadline using the format \"/by dd/mm/yyyy\""
            );
        }

        String[] parts = trimmed.split("\\s+");

        LocalDate date = parseDateFlexible(parts[0]);

        if (parts.length >= 2) {
            String timeStr = parts[1];
            if (parts.length >= 3) {
                timeStr += " " + parts[2];
            }
            int[] time = parseTimeFlexible(timeStr);
            return new CustomDateTime(date, time[0], time[1]);
        } else {
            return new CustomDateTime(date);
        }
    }

    private static LocalDate parseDateFlexible(String s) {
        String[] dmy = s.split("/");
        if (dmy.length == 3) {
            int d = parseVariable(dmy[0], "day");
            int m = parseVariable(dmy[1], "month");
            int y = parseVariable(dmy[2], "year");
            return LocalDate.of(y, m, d);
        } else if (dmy.length == 2) {
            int d = parseVariable(dmy[0], "day");
            int m = parseVariable(dmy[1], "month");
            int y = LocalDate.now().getYear();
            return LocalDate.of(y, m, d);
        } else {
            throw new IllegalArgumentException("Date must be d/m or d/m/yyyy");
        }
    }

    private static int[] parseTimeFlexible(String s) {
        String t = s.trim();
        int h;
        int m;

        if (t.isEmpty()) {
            throw new IllegalArgumentException("Empty time");
        }

        String upper = t.toUpperCase();
        boolean hasMeridiem = false;
        boolean isPM = false;

        if (upper.endsWith("AM") || upper.endsWith("PM")) {
            hasMeridiem = true;
            isPM = upper.endsWith("PM");
            t = t.substring(0, upper.length() - 2).trim();
        }

        if (t.contains(":")) {
            String[] p = t.split(":");
            if (p.length != 2) {
                throw new IllegalArgumentException(
                        "Time must be hh:mm/h:mm or h:mm AM/PM"
                );
            }
            h = parseVariable(p[0], "hour");
            m = parseVariable(p[1], "minute");
        } else {
            if (hasMeridiem && (t.length() == 1 || t.length() == 2)) {
                // e.g., "8pm" → 20:00, "12AM" → 00:00
                h = parseVariable(t, "hour");
                m = 0;
            } else if (t.length() == 4) { // e.g., 0930, 1805
                h = parseVariable(t.substring(0, 2), "hour");
                m = parseVariable(t.substring(2, 4), "minute");
            } else if (t.length() == 3) { // e.g., 930
                h = parseVariable(t.substring(0, 1), "hour");
                m = parseVariable(t.substring(1, 3), "minute");
            } else {
                throw new IllegalArgumentException(
                        "Time must be hhmm, hh:mm/h:mm, h:mm AM/PM, or hAM/PM"
                );
            }
        }

        if (hasMeridiem) {
            if (h < 1 || h > 12) {
                throw new IllegalArgumentException(
                        "hour must be between 1 and 12 for AM/PM time format"
                );
            }
            if (isPM && h != 12) {
                h += 12;
            }
            if (!isPM && h == 12) {
                h = 0;
            }
        }

        if (h < 0 || h > 23 || m < 0 || m > 59) {
            throw new IllegalArgumentException("invalid time");
        }
        return new int[] {h, m};
    }

    private static int parseVariable(String s, String dateTimeType) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + dateTimeType + ": " + s);
        }
    }

    private static String prettyDate(LocalDate d) {
        return d.format(DATE_PRETTY);
    }

    private static String formatTime12(int hour24hFormat, int m) {
        String ampm = (hour24hFormat >= 12) ? "PM" : "AM";
        int hour12hFormat = hour24hFormat % 12;
        if (hour12hFormat == 0) {
            hour12hFormat = 12;
        }
        String twoDigitForm = twoDigitFormat(m);
        return hour12hFormat + ":" + twoDigitForm + " " + ampm;
    }

    private static String twoDigitFormat(int n) {
        return (n < 10 ? "0" : "") + n;
    }

    /**
     * Compares this {@code CustomDateTime} to another, first by date,
     * then by hour, then by minute.
     *
     * @param other the other date-time to compare
     * @return a negative integer, zero, or a positive integer as this
     *         object is earlier than, equal to, or later than the specified object
     */
    @Override
    public int compareTo(CustomDateTime other) {
        int cmp = this.date.compareTo(other.date);
        if (cmp != 0) {
            return cmp;
        }
        cmp = Integer.compare(this.hour, other.hour);
        if (cmp != 0) {
            return cmp;
        }
        return Integer.compare(this.minute, other.minute);
    }

    /**
     * Returns a user-friendly string representation of this date-time.
     * <p>
     * If a time is present, it is shown in 12-hour format with AM/PM.
     * Otherwise, only the date is displayed.
     * </p>
     *
     * @return the formatted string representation
     */
    @Override
    public String toString() {
        return hasTime
                ? prettyDate(date) + ", " + formatTime12(hour, minute)
                : prettyDate(date);
    }
}
