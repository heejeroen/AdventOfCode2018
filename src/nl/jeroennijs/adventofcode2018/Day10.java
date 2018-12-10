package nl.jeroennijs.adventofcode2018;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Day10 {
    private static final Pattern INSTRUCTION = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>");
    private static final int VIEWPORT_SIZE_X = 100;
    private static final int VIEWPORT_SIZE_Y = 10;

    public static void main(String[] args) throws IOException {
        final List<MovingPoint> movingPoints = getInputLines()
            .map(INSTRUCTION::matcher)
            .filter(Matcher::matches)
            .map(matcher -> new MovingPoint(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)))
            .collect(Collectors.toList());

        int time = 0;
        boolean found = false;
        while (!found) {
            time++;
            movingPoints.forEach(MovingPoint::move);
            int minX = movingPoints.stream().mapToInt(point -> point.x).min().orElse(0);
            int maxX = movingPoints.stream().mapToInt(point -> point.x).max().orElse(0);
            int minY = movingPoints.stream().mapToInt(point -> point.y).min().orElse(0);
            int maxY = movingPoints.stream().mapToInt(point -> point.y).max().orElse(0);
            if (maxX - minX < VIEWPORT_SIZE_X && maxY - minY < VIEWPORT_SIZE_Y) {
                found = true;
                System.out.println("Time: " + time);
                printPoints(movingPoints, minX, minY);
            }
        }
    }

    private static void printPoints(final List<MovingPoint> movingPoints, final int minX, final int minY) {
        boolean[][] grid = createEmptyGrid();

        movingPoints.forEach(point -> grid[point.y - minY][point.x - minX] = true);

        printGrid(grid);
    }

    private static void printGrid(boolean[][] grid) {
        for (int y = 0; y < VIEWPORT_SIZE_Y * 2; y++) {
            for (int x = 0; x < VIEWPORT_SIZE_X * 2; x++) {
                System.out.print(grid[y][x] ? "#" : " ");
            }
            System.out.println();
        }
    }

    private static boolean[][] createEmptyGrid() {
        boolean[][] grid = new boolean[VIEWPORT_SIZE_Y * 2][VIEWPORT_SIZE_X * 2];
        for (boolean[] gridrow : grid) {
            Arrays.fill(gridrow, false);
        }
        return grid;
    }

    private static Stream<String> getInputLines() throws IOException {
        return Files.lines(FileSystems.getDefault().getPath("src/nl/jeroennijs/adventofcode2018/input/day10.txt"));
    }

    public static class MovingPoint {
        private int x;
        private int y;
        private int vx;
        private int vy;

        MovingPoint(String x, String y, String vx, String vy) {
            this.x = Integer.valueOf(x);
            this.y = Integer.valueOf(y);
            this.vx = Integer.valueOf(vx);
            this.vy = Integer.valueOf(vy);
        }

        void move() {
            x = x + vx;
            y = y + vy;
        }
    }
}
