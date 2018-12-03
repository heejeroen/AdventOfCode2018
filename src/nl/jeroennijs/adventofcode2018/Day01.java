package nl.jeroennijs.adventofcode2018;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Day01 {
    public static void main(String[] args) throws IOException {
        System.out.println("Step 1: " + getInputLines().mapToInt(Integer::valueOf).sum());
        System.out.println("Step 2: " + getFirstDuplicateFrequency());
    }

    private static int getFirstDuplicateFrequency() throws IOException {
        final List<Integer> changes = getInputLines().map(Integer::valueOf).collect(Collectors.toList());
        final Set<Integer> frequencies = new TreeSet<>();
        int frequency = 0;
        while(true) {
            for (Integer change : changes) {
                frequency += change;
                if (frequencies.contains(frequency)) {
                    return frequency;
                }
                frequencies.add(frequency);
            }
        }
    }

    private static Stream<String> getInputLines() throws IOException {
        return Files.lines(FileSystems.getDefault().getPath("src/nl/jeroennijs/adventofcode2018/input/day01.txt"));
    }
}
