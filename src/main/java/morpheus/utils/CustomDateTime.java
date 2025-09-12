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

    /** Creates a {@code CustomDateTime} by parsing a string input. */
    public CustomDateTime(String input) {
        CustomDateTime parsed = parse(input);
        this.date = parsed.date;
        this.hour = parsed.hour;
        this.minute = parsed.minute;
        this.hasTime = parsed.hasTime;
    }

    /** Creates a {@code CustomDateTime} with a specific date and time. */
    public CustomDateTime(LocalDate date, Integer hour, Integer minute) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.hasTime = true;
    }

    /** Creates a {@code CustomDateTime} with a date only (no time). */
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
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Date/time string cannot be empty");
        }

        String trimmed = input.trim();

        // Try "12 Sep 2025, 3:00 PM"
        try {
            DateTimeFormatter formatterWithTime =
                    DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", java.util.Locale.ENGLISH);
            LocalDateTime dt = LocalDateTime.parse(trimmed, formatterWithTime);
            return new CustomDateTime(dt.toLocalDate(), dt.getHour(), dt.getMinute());
        } catch (Exception e) {
            // continue to next format
        }

        // Try "12 Sep 2025"
        try {
            DateTimeFormatter formatterWithoutTime =
                    DateTimeFormatter.ofPattern("d MMM yyyy", java.util.Locale.ENGLISH);
            LocalDate date = LocalDate.parse(trimmed, formatterWithoutTime);
            return new CustomDateTime(date);
        } catch (Exception e) {
            // continue to fallback
        }

        // Fallback: numeric formats (ddmmyy, ddmmyyyy, d/m/yyyy, etc.)
        try {
            String[] parts = trimmed.split("\\s+", 2);
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

    /** Parses dates in formats like d/m/yyyy, d-m-yyyy, d.m.yyyy, ddmmyy, ddmmyyyy. */
    private static LocalDate parseDateFlexible(String s) {
        if (s.matches("\\d{6}")) {
            int d = Integer.parseInt(s.substring(0, 2));
            int m = Integer.parseInt(s.substring(2, 4));
            int yy = Integer.parseInt(s.substring(4, 6));
            int y = (yy <= 29) ? 2000 + yy : 1900 + yy;
            return LocalDate.of(y, m, d);
        } else if (s.matches("\\d{8}")) {
            int d = Integer.parseInt(s.substring(0, 2));
            int m = Integer.parseInt(s.substring(2, 4));
            int y = Integer.parseInt(s.substring(4, 8));
            return LocalDate.of(y, m, d);
        }

        String[] dmy = s.split("[/.-]");
        try {
            if (dmy.length == 3) {
                int d = Integer.parseInt(dmy[0]);
                int m = Integer.parseInt(dmy[1]);
                int yearVal = Integer.parseInt(dmy[2]);

                int y = (dmy[2].length() == 2)
                        ? (yearVal <= 29 ? 2000 + yearVal : 1900 + yearVal)
                        : yearVal;

                return LocalDate.of(y, m, d);
            } else if (dmy.length == 2) {
                int d = Integer.parseInt(dmy[0]);
                int m = Integer.parseInt(dmy[1]);
                int y = LocalDate.now().getYear();
                return LocalDate.of(y, m, d);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(ERROR_DATE_FORMAT);
        }

        throw new IllegalArgumentException(ERROR_DATE_FORMAT);
    }

    /** Parses times in formats like 930, 0930, 9:30, 19:45, etc. */
    private static int[] parseTimeFlexible(String s) {
        String t = s.trim().toUpperCase();

        boolean hasMeridiem = t.endsWith("AM") || t.endsWith("PM");
        boolean isPM = t.endsWith("PM");
        if (hasMeridiem) {
            t = t.substring(0, t.length() - 2).trim();
        }

        int h;
        int m;
        try {
            if (t.contains(":") || t.contains(".")) {
                String[] parts = t.split("[:.]");
                if (parts.length != 2) {
                    throw new IllegalArgumentException(ERROR_TIME_FORMAT);
                }
                h = Integer.parseInt(parts[0]);
                m = Integer.parseInt(parts[1]);
            } else if (t.length() == 4) {
                h = Integer.parseInt(t.substring(0, 2));
                m = Integer.parseInt(t.substring(2, 4));
            } else if (t.length() == 3) {
                h = Integer.parseInt(t.substring(0, 1));
                m = Integer.parseInt(t.substring(1, 3));
            } else if (t.length() == 1 || t.length() == 2) {
                h = Integer.parseInt(t);
                m = 0;
            } else {
                throw new IllegalArgumentException(ERROR_TIME_FORMAT);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_TIME_FORMAT);
        }

        if (hasMeridiem) {
            if (h < 1 || h > 12) {
                throw new IllegalArgumentException("Hour must be 1–12 for AM/PM format");
            }
            if (isPM && h != 12) {
                h += 12;
            }
            if (!isPM && h == 12) {
                h = 0;
            }
        }

        if (h < 0 || h > 23 || m < 0 || m > 59) {
            throw new IllegalArgumentException("Hour must be 0–23 and minute 0–59");
        }

        return new int[] {h, m};
    }

    private static String prettyDate(LocalDate d) {
        return d.format(DATE_PRETTY);
    }

    private static String formatTime12(int hour24, int m) {
        String ampm = (hour24 >= 12) ? "PM" : "AM";
        int hour12 = hour24 % 12;
        if (hour12 == 0) {
            hour12 = 12;
        }
        return hour12 + ":" + String.format("%02d", m) + " " + ampm;
    }

    /** Convert this {@code CustomDateTime} to a {@link LocalDateTime}. */
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
