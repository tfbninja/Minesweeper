package minesweeper;

import java.util.Arrays;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Random;

/**
 *
 * @author Tim Barber
 */
public class Grid {

    /*
     * 0 - Untouched
     * 1 - Safe
     * 2 - Mine
     * 3 - Flagged
     * 4 - Detonated Mine
     */
    private int width;
    private int length;
    private int[][] playArea;
    private int[][] lastPlayArea;
    private static int[][] savedPlayArea;
    private int numberOfMines;

    public Grid() {
        this.width = 10;
        this.length = 10;
        this.playArea = new int[this.width][this.length];
        this.lastPlayArea = this.playArea;
        this.savedPlayArea = new int[this.width][this.length];
        for (int i = 0; i < this.width; i++) {
            Arrays.fill(this.savedPlayArea[i], 0);
        }
        this.numberOfMines = 10;
    }

    public Grid(int width, int length, int numMines) {
        this.width = width;
        this.length = length;
        this.playArea = new int[this.width][this.length];
        this.lastPlayArea = this.playArea;
        this.savedPlayArea = new int[this.width][this.length];
        for (int i = 0; i < this.width; i++) {
            Arrays.fill(this.savedPlayArea[i], 0);
        }
        this.numberOfMines = numMines;
    }

    public void fillMines() {
        int timeSeed = Integer.valueOf(LocalDateTime.now().toString().trim());
        Random miner = new Random(timeSeed);
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < indices.size(); i++) {
            int tempNum = -1;
            while (tempNum < 0 || indices.contains(tempNum)) {
                tempNum = miner.nextInt(this.getLength() * this.getWidth());
            }

            tempNum = miner.nextInt(this.getLength() * this.getWidth());
        }
        // now we have a list of mine positions
    }

    public int getWidth() {
        return this.width;
    }

    public int getLength() {
        return this.length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setNumMines(int amt) {
        this.numberOfMines = amt;
    }

    public int getNumMines() {
        return this.numberOfMines;
    }

    public boolean isUntouched(int xPos, int yPos) {
        return this.playArea[xPos][yPos] == 0;
    }

    public boolean isFlagged(int xPos, int yPos) {
        return this.playArea[xPos][yPos] == 3;
    }

    public boolean isMine(int xPos, int yPos) {
        return this.playArea[xPos][yPos] == 2 || this.playArea[xPos][yPos] == 4;
    }

    public boolean isDetonated(int xPos, int yPos) {
        return this.playArea[xPos][yPos] == 4;
    }

    public boolean isInactiveMine(int xPos, int yPos) {
        return this.playArea[xPos][yPos] == 2;
    }

    public boolean isClicked(int xPos, int yPos) {
        return this.playArea[xPos][yPos] == 1;
    }

    public boolean isSafe(int xPos, int yPos) {
        return !isMine(xPos, yPos);
    }

    public void flag(int xPos, int yPos) {
        this.lastPlayArea = this.playArea;
        if (this.playArea[xPos][yPos] == 0) {// untouched
            this.playArea[xPos][yPos] = 3; // flag
        } else if (this.playArea[xPos][yPos] == 3) { //already flagged
            this.playArea[xPos][yPos] = 0; // untouched
        }
    }

    public int[][] getPlayArea() {
        return this.playArea;
    }

    public int[][] getSavedPlayArea() {
        return this.savedPlayArea;
    }

    public void savePlayArea() {
        this.savedPlayArea = this.playArea;
    }

    public void revertToSaved() {
        this.lastPlayArea = this.playArea;
        this.playArea = this.savedPlayArea;
    }

    public void clear() {
        this.lastPlayArea = this.playArea;
        this.playArea = new int[this.width][this.length];
    }

    /*
     * public Grid addGlider(int xPosition, int yPosition, boolean
     * clearSurroundings, int surroundingMargin) { int[][] newArea =
     * this.playArea; if (xPosition < this.width - 3 && yPosition < this.length
     * - 2 && xPosition >= 0 && yPosition >= 0) { this.lastPlayArea =
     * this.playArea; if (clearSurroundings) { for (int y = 0; y <
     * surroundingMargin * 2 + 3; y++) { for (int x = 0; x < surroundingMargin *
     * 2 + 3; x++) { try { newArea[yPosition - surroundingMargin + x][xPosition
     * - surroundingMargin + y] = 0; if (this.lastPlayArea[yPosition -
     * surroundingMargin + x][xPosition - surroundingMargin + y] == 1) {
     * System.out.println("Cleared cell [" + (yPosition - surroundingMargin + x)
     * + ", " + (xPosition - surroundingMargin + y) + "]"); } } catch
     * (ArrayIndexOutOfBoundsException a) { System.out.println("Could not clear
     * index [" + (xPosition - surroundingMargin + x) + ", " + (yPosition -
     * surroundingMargin + y) + "]."); } } } try { newArea[yPosition][xPosition]
     * = 0; newArea[yPosition + 1][xPosition] = 1; newArea[yPosition +
     * 2][xPosition] = 0; newArea[yPosition][xPosition + 1] = 1;
     * newArea[yPosition + 1][xPosition + 1] = 0; newArea[yPosition +
     * 2][xPosition + 1] = 0; newArea[yPosition][xPosition + 2] = 1;
     * newArea[yPosition + 1][xPosition + 2] = 1; newArea[yPosition +
     * 2][xPosition + 2] = 1; } catch (ArrayIndexOutOfBoundsException b) {
     * return this; } this.playArea = newArea; return this; } else { try {
     * newArea[yPosition][xPosition] = 0; newArea[yPosition + 1][xPosition] = 1;
     * newArea[yPosition + 2][xPosition] = 0; newArea[yPosition][xPosition + 1]
     * = 1; newArea[yPosition + 1][xPosition + 1] = 0; newArea[yPosition +
     * 2][xPosition + 1] = 0; newArea[yPosition][xPosition + 2] = 1;
     * newArea[yPosition + 1][xPosition + 2] = 1; newArea[yPosition +
     * 2][xPosition + 2] = 1; } catch (ArrayIndexOutOfBoundsException b) {
     * return this; } this.playArea = newArea; return this; } }
     *
     * return this; }
     */
    public void setCells(int xPosition, int yPosition, int[][] cells) {
        this.lastPlayArea = this.playArea;
        int[][] newArea = this.playArea;
        for (int y = yPosition; y < yPosition + cells.length; y++) {
            for (int x = xPosition; x < xPosition + cells[0].length; x++) {
                System.out.println("x: " + x + ", xPosition: " + xPosition + "\ny: " + y + ", yPosition: " + yPosition);
                newArea[y][x] = cells[y - yPosition][x - xPosition];
            }
        }
        this.playArea = newArea;
    }

    /*
     * public void addTumbler(int xPosition, int yPosition) { int[][] tumbler =
     * { {1, 1, 0, 0, 0, 1, 1}, {1, 0, 1, 0, 1, 0, 1}, {1, 0, 1, 0, 1, 0, 1},
     * {0, 0, 1, 0, 1, 0, 0}, {0, 1, 1, 0, 1, 1, 0}, {0, 1, 1, 0, 1, 1, 0}};
     * setCells(xPosition, yPosition, tumbler); }
     */

 /*
     * public void addLine(int xPosition) { if (xPosition >= 0 && xPosition <
     * this.width) { this.lastPlayArea = this.playArea; for (int y = 0; y <
     * this.length; y++) { this.playArea[y][xPosition] =
     * Math.abs(this.playArea[y][xPosition] - 1); } } }
     */
    public void setCell(int x, int y, boolean value) {
        this.lastPlayArea = this.playArea;
        if (value) {
            this.playArea[y][x] = 1;
        } else {
            this.playArea[y][x] = 0;
        }
    }

    public boolean getCell(int x, int y) {
        if (this.playArea[y][x] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setPlayArea(int[][] newPlayArea) {
        this.playArea = newPlayArea;
    }

    /*
     * public void nextGen() { //change return type (maybe) int[][] oldPlayArea
     * = this.playArea; int[][] newPlayArea = new int[this.width][this.length];
     * for (int y = 0; y < this.width; y++) { for (int x = 0; x < this.length;
     * x++) { int neighbors = 0;
     *
     * //check neighbors (with bounds checks) if (x > 0) { if (y > 0) { if
     * (oldPlayArea[x - 1][y - 1] == 1) { // if upper left neighbors++; } } if
     * (oldPlayArea[x - 1][y] == 1) { // if left neighbors++; } if (y <
     * (this.length - 1)) { if (oldPlayArea[x - 1][y + 1] == 1) { // if lower
     * left neighbors++; } } } if (x < (this.width - 1)) { if (y > 0) { if
     * (oldPlayArea[x + 1][y - 1] == 1) { // if upper right neighbors++; } } if
     * (oldPlayArea[x + 1][y] == 1) { // if right neighbors++; } if (y <
     * (this.length - 1)) { if (oldPlayArea[x + 1][y + 1] == 1) { // if lower
     * right neighbors++; } } } if (y > 0) { if (oldPlayArea[x][y - 1] == 1) {
     * // if upper neighbors++; } } if (y < (this.length - 1)) { // if lower if
     * (oldPlayArea[x][y + 1] == 1) { neighbors++; } } // now that we have
     * neighbors, we can determine cell state if (neighbors < 2) {
     * newPlayArea[x][y] = 0; } else if (neighbors == 2) { newPlayArea[x][y] =
     * oldPlayArea[x][y]; } else if (neighbors == 3) { newPlayArea[x][y] = 1; }
     * else { newPlayArea[x][y] = 0; } } } this.playArea = newPlayArea;
     * this.lastPlayArea = oldPlayArea; }
     */
    @Override
    public String toString() {
        String output = "";
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.length; y++) {
                output += String.valueOf(this.playArea[x][y]);
                output += " ";
            }
            output += "\n";
        }
        return output;
    }
}
/*
 * The MIT License
 *
 * Copyright (c) 2018 Tim Barber.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
