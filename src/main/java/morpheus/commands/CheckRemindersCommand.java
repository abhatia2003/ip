package morpheus.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;



/**
 * Represents a command that checks for upcoming reminders on tasks.
 * <p>
 * This command allows users to filter reminders by timeframe:
 * <ul>
 *   <li><code>reminders</code> or <code>reminders all</code> → all future reminders</li>
 *   <li><code>reminders today</code> → reminders before the end of today</li>
 *   <li><code>reminders tomorrow</code> → reminders before the end of tomorrow</li>
 *   <li><code>reminders weekly</code> → reminders in the next 7 days</li>
 * </ul>
 * Results are displayed in chronological order, so that the earliest reminders appear first.
 */
public class CheckRemindersCommand extends Command {

    private static final String UNKNOWN_OPTION_MSG =
            "I don’t recognize that option. Try: `reminders`, `reminders today`, "
                    + "`reminders tomorrow`, or `reminders weekly`.";

    private static final int END_OF_DAY_HOUR = 23;
    private static final int END_OF_DAY_MINUTE = 59;

    /**
     * Creates a new {@code CheckRemindersCommand}.
     *
     * @param input the raw user input that triggered this command
     */
    public CheckRemindersCommand(String input) {
        super(input);
    }

    /**
     * Executes the reminders command by filtering tasks that have reminders
     * and returning them in chronological order.
     *
     * @param taskList the list of tasks to check
     * @param storage  the storage handler (not used here but required by the interface)
     * @param ui       the user interface handler (not used here but required by the interface)
     * @return a formatted string listing upcoming reminders, or a message if none are found
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        LocalDate today = LocalDate.now();
        String mode = extractMode(input);

        TimeFilter filter = resolveFilter(mode, today);
        if (filter == null) {
            return UNKNOWN_OPTION_MSG;
        }

        List<Task> upcomingReminders = findUpcomingReminders(taskList, LocalDateTime.now(), filter.cutoff);

        return formatReminders(upcomingReminders, filter.phrase);
    }

    /**
     * Extracts the filter mode (e.g., "today", "tomorrow", "weekly", "all") from the raw input.
     */
    private String extractMode(String input) {
        return input.trim().toLowerCase().replaceFirst("reminders", "").trim();
    }

    /**
     * Resolves the user-specified mode into a cutoff time and descriptive phrase.
     * Returns {@code null} if the mode is not recognized.
     */
    private TimeFilter resolveFilter(String mode, LocalDate today) {
        switch (mode) {
        case "today":
            return new TimeFilter(today.atTime(END_OF_DAY_HOUR, END_OF_DAY_MINUTE), "before the end of today");
        case "tomorrow":
            return new TimeFilter(today.plusDays(1).atTime(END_OF_DAY_HOUR, END_OF_DAY_MINUTE),
                    "before the end of tomorrow");
        case "weekly":
            return new TimeFilter(today.plusWeeks(1).atTime(END_OF_DAY_HOUR, END_OF_DAY_MINUTE),
                    "in the next week");
        case "":
        case "all":
            return new TimeFilter(null, "soon");
        default:
            return null;
        }
    }

    /**
     * Collects upcoming reminders that occur after {@code now}
     * and, if provided, before the cutoff.
     */
    private List<Task> findUpcomingReminders(List<Task> taskList, LocalDateTime now, LocalDateTime cutoff) {
        return taskList.stream()
                .filter(t -> t.getReminder().isPresent())
                .filter(t -> isWithinTimeWindow(t, now, cutoff))
                .sorted(Comparator.comparing(t -> t.getReminder().get().toLocalDateTime()))
                .toList();
    }

    /**
     * Checks whether a task's reminder is valid (i.e., in the future
     * and not beyond the cutoff if one is set).
     */
    private boolean isWithinTimeWindow(Task task, LocalDateTime now, LocalDateTime cutoff) {
        LocalDateTime reminderTime = task.getReminder().get().toLocalDateTime();
        if (reminderTime.isBefore(now)) {
            return false; // Past reminders are skipped
        }
        return cutoff == null || !reminderTime.isAfter(cutoff);
    }

    /**
     * Formats reminders for display to the user.
     * If none are found, a friendly message is returned instead.
     */
    private String formatReminders(List<Task> reminders, String phrase) {
        if (reminders.isEmpty()) {
            return "No tasks found " + phrase;
        }

        StringBuilder sb = new StringBuilder("Here are reminders that are due " + phrase + ":\n\n");
        for (int i = 0; i < reminders.size(); i++) {
            sb.append(i + 1).append(". ").append(reminders.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Helper class encapsulating a cutoff time and descriptive phrase
     * for filtering reminders.
     */
    private static class TimeFilter {
        private final LocalDateTime cutoff;
        private final String phrase;

        TimeFilter(LocalDateTime cutoff, String phrase) {
            this.cutoff = cutoff;
            this.phrase = phrase;
        }
    }
}
