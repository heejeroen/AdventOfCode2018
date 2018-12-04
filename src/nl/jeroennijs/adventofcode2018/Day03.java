package nl.jeroennijs.adventofcode2018;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;



public class Day03 {

    private static Pattern pattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

    public static void main(String[] args) throws IOException {
        final int[][] field = new int[1000][1000];
        System.out.println("Step 1: " + getAreaWithTwoOrMoreOverlaps(field)); // 116489
        System.out.println("Step 2: " + getNoOverlapAreaId(field)); // 1260
    }

    private static int getAreaWithTwoOrMoreOverlaps(int[][] field) throws IOException {
        getInputLines().forEach(line -> claimArea(line, field));
        int overlapArea = 0;
        for (int[] row : field) {
            for (int ix = 0; ix < field.length; ix++) {
                if (row[ix] >= 2)
                    overlapArea++;
            }
        }
        return overlapArea;
    }

    private static int getNoOverlapAreaId(final int[][] field) throws IOException {
        return getInputLines()
            .map(Area::new)
            .filter(area -> isAreaWithoutOverlap(area, field))
            .findFirst()
            .map(area -> area.id)
            .orElse(-1);
    }

    private static boolean isAreaWithoutOverlap(Area area, int[][] field) {
        for (int iy = area.y; iy < area.y + area.height; iy++) {
            for (int ix = area.x; ix < area.x + area.width; ix++) {
                if (field[iy][ix] != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void claimArea(String line, int[][] field) {
        final Area area = new Area(line);
        for (int iy = area.y; iy < area.y + area.height; iy++) {
            for (int ix = area.x; ix < area.x + area.width; ix++) {
                field[iy][ix]++;
            }
        }
    }

    private static Stream<String> getInputLines() throws IOException {
        return Files.lines(FileSystems.getDefault().getPath("src/nl/jeroennijs/adventofcode2018/input/day03.txt"));
    }

    public static class Area {
        int id;
        int x;
        int y;
        int width;
        int height;

        Area(String line) {
            final Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Could not parse " + line);
            }
            id = Integer.valueOf(matcher.group(1));
            x = Integer.valueOf(matcher.group(2));
            y = Integer.valueOf(matcher.group(3));
            width = Integer.valueOf(matcher.group(4));
            height = Integer.valueOf(matcher.group(5));
        }
    }
}
