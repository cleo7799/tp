package tutman.tuiniverse.model.lesson;

import static tutman.tuiniverse.commons.util.AppUtil.checkArgument;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Lesson's end time.
 * Guarantees: immutable; is valid as declared in {@link #isValidLessonTime(String, String)}
 */
public class LessonTime implements Comparable<LessonTime> {

    public static final String MESSAGE_CONSTRAINTS = "Time must be given in the format HH:MM, "
            + "and the start time should be before the end time";

    /** Input time format for parsing user input. */
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    public static final String VALIDATION_REGEX = "^([01]\\d|2[0-3]):([0-5]\\d)$";

    private final LocalTime start;
    private final LocalTime end;

    /**
     * Constructs an {@code LessonTime}.
     *
     * @param start The LocalTime start time of the lesson.
     * @param end The LocalTime end time of the lesson.
     */
    private LessonTime(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Factory constructor of an {@code LessonTime}.
     *
     * @param strStart A valid start time.
     * @param strEnd A valid end time.
     */
    public static LessonTime ofLessonTime(String strStart, String strEnd) {
        checkArgument(isValidLessonTime(strStart, strEnd), MESSAGE_CONSTRAINTS);
        LocalTime startTime = parseTime(strStart);
        LocalTime endTime = parseTime(strEnd);
        return new LessonTime(startTime, endTime);
    }

    /**
     * Parses the given string str into a LocalTime according to TIME_FORMAT.
     */
    public static LocalTime parseTime(String str) {
        return LocalTime.parse(str, TIME_FORMAT);
    }

    /**
     * Checks if the given str is a valid time in accordance to TIME_FORMAT.
     */
    public static boolean isValidTime(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the given strings are valid start times and end times
     */
    public static boolean isValidLessonTime(String testStart, String testEnd) {
        if (isValidTime(testStart) && isValidTime(testEnd)) {
            LocalTime start = parseTime(testStart);
            LocalTime end = parseTime(testEnd);
            return start.isBefore(end);
        }
        return false;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }

    /**
     * Converts the Duration of the class to hours
     * @return a double representing number of hours
     */
    public double getDurationDouble() {
        return this.getDuration().toMinutes() / 60.0;
    }

    /**
     * Returns true if both lesson times have overlapping start times and end times
     */
    public boolean hasTimeClash(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LessonTime)) {
            return false;
        }

        LessonTime otherTime = (LessonTime) other;
        return this.start.isBefore(otherTime.end) && otherTime.start.isBefore(this.end);
    }

    @Override
    public int compareTo(LessonTime other) {
        return this.start.isBefore(other.getStart())
                ? -1
                : this.start.equals(other.getStart())
                ? 0
                : 1;
    }

    @Override
    public String toString() {
        return start.toString() + " - " + end.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LessonTime)) {
            return false;
        }

        LessonTime otherTime = (LessonTime) other;
        boolean isSameStart = this.start.equals(otherTime.start);
        boolean isSameEnd = this.end.equals(otherTime.end);
        return isSameStart && isSameEnd;
    }

    @Override
    public int hashCode() {
        return end.hashCode();
    }
}
