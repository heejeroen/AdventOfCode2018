package nl.jeroennijs.adventofcode2018;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Day04 {
    private static Pattern START_EVENT_PATTERN = Pattern.compile("\\[([^\\]]+)\\] Guard #(\\d+) begins shift");
    private static Pattern SLEEP_EVENT_PATTERN = Pattern.compile("\\[([^\\]]+)\\] (.*)");
    private static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) throws IOException {
        final Map<String, Integer[]> guardSchedules = getSchedules(getGuardEvents());

        System.out.println("Step 1: " + getGuardIdAndMinutes(guardSchedules, entry -> getSum(entry.getValue()))); // 98680
        System.out.println("Step 2: " + getGuardIdAndMinutes(guardSchedules, entry -> getMax(entry.getValue()))); // 9763
    }

    private static int getGuardIdAndMinutes(Map<String, Integer[]> guardSchedules, Function<Map.Entry<String, Integer[]>, Integer> maxFunction) {
        String selectedGuardId = getIdOfGuardForCondition(guardSchedules, maxFunction);
        int selectedMinute = getMinuteWhereMostAsleep(guardSchedules.get(selectedGuardId));

        return Integer.valueOf(selectedGuardId) * selectedMinute;
    }

    private static List<GuardEvent> getGuardEvents() throws IOException {
        return getInputLines()
            .map(GuardEvent::new)
            .sorted(Comparator.comparing(event -> event.timestamp))
            .collect(Collectors.toList());
    }

    private static Map<String, Integer[]> getSchedules(List<GuardEvent> guardEvents) {
        Map<String, Integer[]> guardSchedules = new HashMap<>();
        String currentGuardId = null;
        int currentStartedSleep = 0;
        for (GuardEvent guardEvent : guardEvents) {
            switch (guardEvent.type) {
                case START:
                    currentGuardId = guardEvent.id;
                    break;
                case ASLEEP:
                    currentStartedSleep = guardEvent.timestamp.getMinute();
                    break;
                case WAKE:
                    Integer[] schedule = guardSchedules.computeIfAbsent(currentGuardId, key -> getEmptySchedule());
                    int currentEndedSleep = guardEvent.timestamp.getMinute();
                    for (int i = currentStartedSleep; i < currentEndedSleep; i++) {
                        schedule[i]++;
                    }
                    break;
            }
        }
        return guardSchedules;
    }

    private static Integer[] getEmptySchedule() {
        Integer[] emptySchedule = new Integer[60];
        Arrays.fill(emptySchedule, 0);
        return emptySchedule;
    }

    private static int getMinuteWhereMostAsleep(Integer[] guardSchedule) {
        int selectedMinute = 0;
        int highestAsleepValue = 0;
        for (int i = 0; i < guardSchedule.length; i++) {
            if (guardSchedule[i] > highestAsleepValue) {
                selectedMinute = i;
                highestAsleepValue = guardSchedule[i];
            }
        }
        return selectedMinute;
    }

    private static String getIdOfGuardForCondition(Map<String, Integer[]> guardSchedules, Function<Map.Entry<String, Integer[]>, Integer> maxFunction) {
        return guardSchedules.entrySet().stream()
            .max(Comparator.comparing(maxFunction))
            .map(Map.Entry::getKey)
            .orElse("0");
    }

    private static int getSum(Integer[] values) {
        return Arrays.stream(values).mapToInt(v -> v).sum();
    }

    private static int getMax(Integer[] values) {
        return Arrays.stream(values).mapToInt(v -> v).max().orElse(0);
    }

    private static Stream<String> getInputLines() throws IOException {
        return Files.lines(FileSystems.getDefault().getPath("src/nl/jeroennijs/adventofcode2018/input/day04.txt"));
    }

    public static class GuardEvent {
        LocalDateTime timestamp;
        String id;
        GuardEventType type;

        GuardEvent(String eventString) {
            final Matcher startMatcher = START_EVENT_PATTERN.matcher(eventString);
            if (startMatcher.matches()) {
                timestamp = LocalDateTime.parse(startMatcher.group(1), DATE_TIME_FORMAT);
                id = startMatcher.group(2);
                type = GuardEventType.START;
            } else {
                final Matcher sleepMatcher = SLEEP_EVENT_PATTERN.matcher(eventString);
                if (sleepMatcher.matches()) {
                    timestamp = LocalDateTime.parse(sleepMatcher.group(1), DATE_TIME_FORMAT);
                    switch (sleepMatcher.group(2)) {
                        case "wakes up":
                            this.type = GuardEventType.WAKE;
                            break;
                        case "falls asleep":
                            this.type = GuardEventType.ASLEEP;
                            break;
                        default:
                            throw new IllegalStateException("Could not parse " + eventString);
                    }
                } else {
                    throw new IllegalStateException("Could not parse " + eventString);
                }
            }
        }
    }



    public enum GuardEventType {
        START, ASLEEP, WAKE
    }
}
