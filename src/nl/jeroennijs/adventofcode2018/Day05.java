package nl.jeroennijs.adventofcode2018;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.stream.IntStream;
import java.util.stream.Stream;



public class Day05 {
    private static final int CASE_DISTANCE = 'a' - 'A';

    public static void main(String[] args) throws IOException {
        final String input = getInputLines().findFirst().orElse("");
        System.out.println("Step 1: " + getNumberOfRemainingLetters(input)); // 9172
        System.out.println("Step 2: " + getShortest(input)); // 6550
    }

    private static int getNumberOfRemainingLetters(final String current) {
        boolean reacted = false;
        StringBuilder result = new StringBuilder();
        final char[] chars = current.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i < chars.length - 1 && Math.abs(chars[i] - chars[i + 1]) == CASE_DISTANCE) {
                reacted = true;
                i++;
            } else {
                result.append(chars[i]);
            }
        }
        if (!reacted) {
            return result.length();
        }
        return getNumberOfRemainingLetters(result.toString());
    }

    private static int getShortest(final String current) {
        return IntStream.rangeClosed('A', 'Z')
            .map(i -> getNumberOfRemainingLetters(removePair(current, (char) i)))
            .min()
            .orElse(0);
    }

    private static String removePair(final String current, char pairChar) {
        return current.replaceAll(String.valueOf(pairChar), "").replaceAll(String.valueOf(pairChar).toLowerCase(), "");
    }

    private static Stream<String> getInputLines() throws IOException {
        return Files.lines(FileSystems.getDefault().getPath("src/nl/jeroennijs/adventofcode2018/input/day05.txt"));
    }
}
