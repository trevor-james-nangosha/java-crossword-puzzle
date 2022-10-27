package com.nangosha.puzzle;

import java.util.ArrayList;

public class Grid {
    int length, width, startMark;
    boolean fillable;
    Grid neighborLeft, neighborRight, neighborTop, neighborBottom;
    String letter;
    ArrayList<Grid> neighborsRight = new ArrayList<Grid>();
    ArrayList<Grid> neighborsBottom = new ArrayList<Grid>();

    public Grid(int length, int width){
        this.length = length;
        this.width = width;

        // defaults
        this.fillable = true;
        this.neighborTop = null;
        this.neighborRight = null;
        this.neighborBottom = null;
        this.neighborLeft = null;
    }

    public ArrayList<Grid> getAllNeighborsRight(){
        Grid currentGrid = this;
        while (currentGrid.neighborRight != null) {
            Grid neighborRight = currentGrid.neighborRight;
            neighborsRight.add(neighborRight);
            currentGrid = neighborRight;
        }

        return neighborsRight;
    }

    public ArrayList<Grid> getAllNeighborsBottom(){
        Grid currentGrid = this;
        while (currentGrid.neighborBottom != null) {
            Grid neighborBottom = currentGrid.neighborBottom;
            neighborsBottom.add(neighborBottom);
            currentGrid = neighborBottom;
        }

        return neighborsBottom;
    }

}