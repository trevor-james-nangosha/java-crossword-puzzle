package com.nangosha.puzzle;

public class App {
    public static void main(String[] args) {
        CrosswordPuzzle puzzle = new CrosswordPuzzle(17, 19);
        Grid[] row = puzzle.gridMatrix[3];
        Grid thirdGrid = row[18];
        System.out.println(thirdGrid.letter); // testing out letter allocation
    }
}

