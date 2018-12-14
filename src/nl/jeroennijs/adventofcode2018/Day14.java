package nl.jeroennijs.adventofcode2018;

import java.io.IOException;



public class Day14 {

    private static final String START = "37";

    public static void main(String[] args) throws IOException {
        final int input = 990941;
        System.out.println("Score of next 10 is " + getStep1(input)); // 3841138812
        System.out.println(input + " occurs at " + getStep2(String.valueOf(input))); // 20200561
    }

    private static String getStep1(int inputLength) {
        int elf1CurrentRecipe = 0;
        int elf2CurrentRecipe = 1;
        final int endLength = inputLength + 10;
        final StringBuilder recipeScores = new StringBuilder(START);
        while (recipeScores.length() < endLength) {
            final int elf1CurrentScore = getCurrentScore(elf1CurrentRecipe, recipeScores);
            final int elf2CurrentScore = getCurrentScore(elf2CurrentRecipe, recipeScores);
            recipeScores.append(elf1CurrentScore + elf2CurrentScore);
            final int recipeScoresLength = recipeScores.length();
            elf1CurrentRecipe = (elf1CurrentRecipe + elf1CurrentScore + 1) % recipeScoresLength;
            elf2CurrentRecipe = (elf2CurrentRecipe + elf2CurrentScore + 1) % recipeScoresLength;
        }
        return recipeScores.substring(inputLength, endLength);
    }

    private static int getStep2(String toFind) {
        int elf1CurrentRecipe = 0;
        int elf2CurrentRecipe = 1;
        final int toFindLength = toFind.length();
        final StringBuilder recipeScores = new StringBuilder(START);
        while (true) {
            final int elf1CurrentScore = getCurrentScore(elf1CurrentRecipe, recipeScores);
            final int elf2CurrentScore = getCurrentScore(elf2CurrentRecipe, recipeScores);
            recipeScores.append(elf1CurrentScore + elf2CurrentScore);
            final int recipeScoresLength = recipeScores.length();
            if (recipeScoresLength > toFindLength) {
                if (recipeScores.substring(recipeScoresLength - toFindLength).equals(toFind)) {
                    return recipeScoresLength - toFindLength;
                }
                if (recipeScores.substring(recipeScoresLength - toFindLength - 1, recipeScoresLength - 1).equals(toFind)) {
                    return recipeScoresLength - toFindLength - 1;
                }
            }
            elf1CurrentRecipe = (elf1CurrentRecipe + elf1CurrentScore + 1) % recipeScoresLength;
            elf2CurrentRecipe = (elf2CurrentRecipe + elf2CurrentScore + 1) % recipeScoresLength;
        }
    }

    private static int getCurrentScore(int currentRecipe, StringBuilder recipeScores) {
        return recipeScores.charAt(currentRecipe) - '0';
    }
}
