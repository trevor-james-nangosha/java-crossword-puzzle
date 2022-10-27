package com.nangosha.puzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CrosswordPuzzle extends Grid{
    Grid[][] gridMatrix;
    private final String PATH_ACROSS;
    private final String PATH_DOWN;
    private int[][] image = {
        {0,0,2,1,3,1,1,1,1,1,1,0,0,0,0,0,0,0,4},
        {0,0,0,0,1,0,0,0,0,0,0,5,6,1,0,0,0,0,1},
        {7,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,8,1,1},
        {1,0,0,0,0,0,0,9,0,10,0,11,1,1,1,1,1,0,1},
        {12,1,1,1,13,1,1,1,1,1,0,0,1,0,0,0,1,0,1},
        {1,0,0,0,1,0,0,1,0,1,0,14,1,1,1,0,1,0,1},
        {1,0,15,0,1,0,16,1,1,0,17,0,1,0,0,0,1,0,0},
        {1,0,1,0,1,0,0,0,0,0,1,0,1,0,0,0,1,0,0},
        {1,0,18,1,1,1,1,1,1,0,19,1,1,1,20,1,1,1,0},
        {1,0,1,0,1,0,0,0,0,0,1,0,0,0,1,0,1,0,0},
        {1,0,1,0,1,0,21,1,22,1,1,1,1,1,1,0,1,0,0},
        {1,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,1,0,0},
        {0,0,23,1,1,1,1,1,1,0,1,0,0,0,24,0,0,25,0},
        {0,0,1,0,0,0,0,0,1,0,0,0,26,1,1,1,1,1,1},
        {27,1,1,1,28,1,0,0,1,0,0,0,0,0,1,0,0,1,0},
        {1,0,0,0,1,0,0,29,1,1,1,1,1,1,1,0,0,1,0},
        {1,0,30,1,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0},
    };

    HashMap<String, String> questionsAcross = new HashMap<String, String>();
    HashMap<String, String> questionsDown = new HashMap<String, String>();

    HashMap<String, String> solutionsAcross = new HashMap<String, String>();
    HashMap<String, String> solutionsDown = new HashMap<String, String>();


    public CrosswordPuzzle(int rows, int columns){
        super(rows, columns);
        this.fillable = false;
        this.PATH_ACROSS = "/home/nangosha/Desktop/CrosswordPuzzle/src/com/nangosha/puzzle/questionsAndSolutionsAcross.txt";
        this.PATH_DOWN = "/home/nangosha/Desktop/CrosswordPuzzle/src/com/nangosha/puzzle/questionsAndSolutionsDown.txt";
        
        // this.createPuzzle(rows, columns, gridMatrix);
        // this.allocateNeighbors(rows, columns, gridMatrix);
        
        this.getAcrossQuestionsAndAnswers(this.PATH_ACROSS);
        this.getDownQuestionsAndAnswers(this.PATH_DOWN);
        this.gridMatrix = this.init(rows, columns);

        // this.assignLettersToGrids(rows, columns, gridMatrix);

    }

    Grid[][] init(int rows, int columns){
        Grid[][] matrix = new Grid[rows][columns];
        Grid[][] firstMatrix = createPuzzle(rows, columns, matrix);
        Grid[][] secondMatrix = allocateNeighbors(rows, columns, firstMatrix);
        Grid[][] matrixWithLetters = assignLettersToGrids(rows, columns, secondMatrix);

        return matrixWithLetters;
    }

    private Grid[][] createPuzzle(int rows, int columns, Grid[][] matrix){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new Grid(1, 1);
                if(image[i][j] == 0){
                    matrix[i][j].fillable = false;
                }
                if(image[i][j] > 0){
                    matrix[i][j].letter = ""; // this means we can put a character in this grid
                }
                if(image[i][j] > 1){
                    matrix[i][j].startMark = image[i][j] - 1;
                }
            }
        }

        return matrix;
    }

    private Grid[][] allocateNeighbors(int rows, int columns, Grid[][] matrix){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                /* ------- neighbor bottom and top allocation logic ---------- */
                if((i > 0)){
                    matrix[i][j].neighborTop = matrix[i-1][j];
                }
                if(i < (rows - 1)){ // ignore the very last row
                    matrix[i][j].neighborBottom = matrix[i+1][j];
                }

                /* ------ neighbor right and left allocation logic -------------  */
                if(j > 0){
                    matrix[i][j].neighborLeft = matrix[i][j-1];
                }
                if(j < (columns - 1)){
                    matrix[i][j].neighborRight = matrix[i][j+1];
                }
            }
        }

        return matrix;
    }

    private Grid[][] assignLettersToGrids(int rows, int columns, Grid[][] matrix){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Grid currentGrid = matrix[i][j];
                int mark = currentGrid.startMark;
                String key = String.valueOf(mark);

                if (mark > 0) {
                    //look for that corresponding startMark in any of the solution banks
                    HashMap<String, String> containingStore = solutionsAcross.containsKey(key) ? solutionsAcross : solutionsDown;
                    String direction = solutionsAcross.containsKey(key) ? "across" : "down";
                    String word = containingStore.get(key);

                    if(direction == "across"){
                        assignLettersAcross(currentGrid, word);
                    }else if (direction == "down") {
                        assignLettersDown(currentGrid, word);
                    }

                }
            }
        }

        return matrix;
    }

    private void assignLettersAcross(Grid grid, String word){
        String[] letters = word.split("");
        grid.letter = letters[0];

        String[] lettersNewRange = Arrays.copyOfRange(letters, 1, letters.length);
        ArrayList<Grid> neighbors = grid.getAllNeighborsRight();
        for (int i = 0; i < lettersNewRange.length; i++) {
            if(neighbors.get(i).fillable){
                neighbors.get(i).letter = lettersNewRange[i];
            }
        }
    }

    private void assignLettersDown(Grid grid, String word){
        String[] letters = word.split("");
        grid.letter = letters[0];

        String[] lettersNewRange = Arrays.copyOfRange(letters, 1, letters.length);
        ArrayList<Grid> neighbors = grid.getAllNeighborsBottom();
        for (int i = 0; i < lettersNewRange.length; i++) {
            if(neighbors.get(i).fillable){
                neighbors.get(i).letter = lettersNewRange[i];
            }
        }
    }

    private void getAcrossQuestionsAndAnswers(String path){
        try {
            File acrossFile = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(acrossFile));
            String line = null;
            while ((line= reader.readLine()) != null) {
                addQuestionAndAnswer(line, questionsAcross, solutionsAcross);
            }
            reader.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getDownQuestionsAndAnswers(String path){
        try {
            File downFile = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(downFile));
            String line = null;
            while ((line= reader.readLine()) != null) {
                addQuestionAndAnswer(line, questionsDown, solutionsDown);
            }
            reader.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addQuestionAndAnswer(String lineToParse, HashMap<String, String> questionStore, HashMap<String, String> solutionStore){
        String[] tokens = lineToParse.split("/");
        questionStore.put(tokens[0], tokens[1]);
        solutionStore.put(tokens[0], tokens[2]);
    }

}

