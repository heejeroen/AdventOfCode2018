package nl.jeroennijs.adventofcode2018;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 {
    private static final Pattern POS_REGEX = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");

    public static void main(String[] args) throws IOException {
        System.out.println("Step 1: " + getNumberOfBotsInRange());
    }

    private static long getNumberOfBotsInRange() throws IOException {
        final List<Bot> bots = getBots();
        final long maxRadius = bots.stream()
                .mapToLong(b -> b.radius).max()
                .orElseThrow(() -> new IllegalStateException("No max radius found"));
        final Bot strongestBot = bots.stream()
                .filter(b -> b.radius == maxRadius)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No strongest bot found"));
        return bots.stream()
                .mapToLong(bot -> calculateDistance(bot, strongestBot))
                .filter(distance -> distance <= maxRadius)
                .count();
    }

    private static List<Bot> getBots() throws IOException {
        return getInputLines()
                    .map(POS_REGEX::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> new Bot(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)))
                    .collect(Collectors.toList());
    }

    private static long calculateDistance(Bot bot1, Bot bot2) {
        return Math.abs(bot1.x - bot2.x) + Math.abs(bot1.y - bot2.y) + Math.abs(bot1.z - bot2.z);
    }

    private static Stream<String> getInputLines() throws IOException {
        return Files.lines(FileSystems.getDefault().getPath("src/nl/jeroennijs/adventofcode2018/input/day23.txt"));
    }

    private static class Bot {
        long x, y, z;
        long radius;

        Bot(String x, String y, String z, String radius) {
            this.x = Long.valueOf(x);
            this.y = Long.valueOf(y);
            this.z = Long.valueOf(z);
            this.radius = Long.valueOf(radius);
        }
    }
}
