package nl.jeroennijs.adventofcode2018;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class Day11 {
    private static final int SERIAL_NR = 1308;
    private static final int GRID_SIZE = 300;

    public static void main(String[] args) {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        calculatePowerPerPoint(grid);

        // Step 1
        GridPoint cellWithSize3 = findMostPowerfulCell(grid, 3);
        System.out.println("Most powerful cell for size 3: x=" + cellWithSize3.x + ", y=" + cellWithSize3.y + ", power=" + cellWithSize3.power); // 21,41 (power 31)

        // Step 2
        List<GridPoint> maxPowerPerSize = calculateMaxPowerPerSize(grid);
        int maxPower = 0;
        int maxCellSize = 0;
        for (int cellSize = 1; cellSize < GRID_SIZE; cellSize++) {
            if (maxPowerPerSize.get(cellSize - 1).power > maxPower) {
                maxPower = maxPowerPerSize.get(cellSize - 1).power;
                maxCellSize = cellSize;
            }
        }
        GridPoint maxPowerCell = maxPowerPerSize.get(maxCellSize);
        System.out.println("Most powerful cell for any size: x=" + maxPowerCell.x + ", y=" + maxPowerCell.y + ", size=" + maxCellSize + ", power=" + maxPower); // 227,199,19
    }

    private static void calculatePowerPerPoint(int[][] grid) {
        for (int y = 1; y <= GRID_SIZE; y++) {
            for (int x = 1; x <= GRID_SIZE; x++) {
                int rackId = x + 10;
                int power = (rackId * y + SERIAL_NR) * rackId;

                grid[y - 1][x - 1] = hundredOf(power) - 5;
            }
        }
    }

    private static GridPoint findMostPowerfulCell(int[][] grid, int cellSize) {
        int maxX = 0;
        int maxY = 0;
        int maxPower = 0;
        for (int y = 0; y < GRID_SIZE - (cellSize - 1); y++) {
            for (int x = 0; x < GRID_SIZE - (cellSize - 1); x++) {
                int cellPower = calculatePower(grid, x, y, cellSize);
                if (cellPower > maxPower) {
                    maxPower = cellPower;
                    maxX = x;
                    maxY = y;
                }
            }
        }
        GridPoint result = new GridPoint();
        result.x = maxX + 1;
        result.y = maxY + 1;
        result.power = maxPower;
        return result;
    }

    private static List<GridPoint> calculateMaxPowerPerSize(final int[][] grid) {
        return IntStream.range(1, GRID_SIZE).parallel().mapToObj(cellSize -> findMostPowerfulCell(grid, cellSize)).collect(Collectors.toList());
    }

    private static int calculatePower(int[][] grid, int x, int y, int cellSize) {
        int cellPower = 0;
        for (int iy = y; iy < y + cellSize; iy++) {
            for (int ix = x; ix < x + cellSize; ix++) {
                cellPower += grid[iy][ix];
            }
        }
        return cellPower;
    }

    private static int hundredOf(int value) {
        String v = String.valueOf(value);
        if (v.length() >= 3) {
            int index = v.length() - 3;
            return Integer.valueOf(v.substring(index, index + 1));
        }
        return 0;
    }

    public static class GridPoint {
        int x;
        int y;
        int power;
    }
}
