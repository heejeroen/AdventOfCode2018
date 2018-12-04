package nl.jeroennijs.adventofcode2018;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.*;



public class Day02 {
    public static void main(String[] args) throws IOException {
        System.out.println("Step 1: " + getSizeOfFrequency(2) * getSizeOfFrequency(3)); // 5456
        System.out.println("Step 2: " + getRightId()); // megsdlpulxvinkatfoyzxcbvq
    }

    private static String getRightId() throws IOException {
        final List<String> inputLines = getInputLines().collect(Collectors.toList());
        for (int i = 0; i < inputLines.size(); i++) {
            for (int j = 0; j < inputLines.size(); j++) {
                if (j != i && numberOfDifferentLetters(inputLines.get(i), inputLines.get(j)) == 1) {
                    return sameLetters(inputLines.get(i), inputLines.get(j));
                }
            }
        }
        return "";
    }

    private static long getSizeOfFrequency(int frequency) throws IOException {
        return getInputLines().map(Day02::getFrequencies).filter(frequencies -> contains(frequencies, frequency)).count();
    }

    private static int[] getFrequencies(final String line) {
        int[] frequencies = new int[26];
        for (char lineChar : line.toCharArray()) {
            frequencies[lineChar - 'a']++;
        }
        return frequencies;
    }

    private static Stream<String> getInputLines() throws IOException {
        return Files.lines(FileSystems.getDefault().getPath("src/nl/jeroennijs/adventofcode2018/input/day02.txt"));
    }

    private static boolean contains(int[] values, int valueToFind) {
        return IntStream.of(values).anyMatch(value -> value == valueToFind);
    }

    private static String sameLetters(String value1, String value2) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value1.length(); i++) {
            if (value1.charAt(i) == value2.charAt(i)) {
                result.append(value1.charAt(i));
            }
        }
        return result.toString();
    }

    private static int numberOfDifferentLetters(String value1, String value2) {
        int difference = 0;
        for (int i = 0; i < value1.length(); i++) {
            if (value1.charAt(i) != value2.charAt(i)) {
                difference++;
            }
        }
        return difference;
    }

}
