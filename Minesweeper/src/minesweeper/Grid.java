package minesweeper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Tim Barber
 */
public class Grid {

    /*
     * 0 - Untouched - no mine
     * 1 - Safe
     * 2 - Mine
     * 3 - Flagged
     * 4 - Detonated Mine
     * 5 - Flagged mine
     */
    private int width;
    private int length;
    private int[][] playArea;
    private int[][] lastPlayArea;
    private static int[][] savedPlayArea;
    private int numberOfMines;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String deltaTime;

    public Grid() {
        this.width = 10;
        this.length = 10;
        this.playArea = new int[this.length][this.width];
        this.lastPlayArea = this.playArea;
        this.savedPlayArea = new int[this.length][this.width];
        for (int i = 0; i < this.length; i++) {
            Arrays.fill(this.savedPlayArea[i], 0);
        }
        this.numberOfMines = 10;
        this.startTime = null;
        this.deltaTime = "";
    }

    public Grid(int width, int length, int numMines) {
        this.width = width;
        this.length = length;
        this.playArea = new int[this.length][this.width];
        this.lastPlayArea = this.playArea;
        this.savedPlayArea = new int[this.length][this.width];
        for (int i = 0; i < this.length; i++) {
            Arrays.fill(this.savedPlayArea[i], 0);
        }
        this.numberOfMines = numMines;
        fillMines("Filled constructor");
        this.startTime = LocalDateTime.now();
        this.deltaTime = "";
    }

    public String stopTimer() {
        this.endTime = LocalDateTime.now();
        return this.getTimer(endTime);
    }

    public String getTimer() {
        String seconds = String.valueOf((LocalDateTime.now().getSecond() - this.startTime.getSecond()) % 60);
        String minutes = String.valueOf(LocalDateTime.now().getMinute() - this.startTime.getMinute());
        this.deltaTime = minutes + ":" + seconds;
        return this.deltaTime;
    }

    public String getTimer(LocalDateTime endTime) {
        String seconds = String.valueOf(endTime.now().getSecond() - this.startTime.getSecond());
        String minutes = String.valueOf(endTime.now().getMinute() - this.startTime.getMinute());
        this.deltaTime = minutes + ":" + seconds;
        return this.deltaTime;
    }

    public int numFlags() {
        int count = this.countVal(3) + this.countVal(5);
        return count;
    }

    public int getMinesLeft() {
        return this.countVal(2) - this.countVal(3);
    }

    public void fillMines(String method) {
        //System.out.println("fillMines() called from " + method); //debug
        this.lastPlayArea = this.playArea;
        for (int i = 0; i < this.length; i++) { // take out any old mines
            Arrays.fill(this.playArea[i], 0);
        }
        int timeSeed = LocalDateTime.now().getNano();
        Random miner = new Random(timeSeed);
        ArrayList<int[]> positions = new ArrayList<>();
        for (int i = 0; i < this.numberOfMines; i++) {
            int[] position = {-1, -1};
            boi:
            do {
                position[0] = miner.nextInt(this.getWidth());
                position[1] = miner.nextInt(this.getLength());
                for (int a = 0; a < positions.size(); a++) {
                    if (position[0] == positions.get(a)[0] && position[1] == positions.get(a)[1]) {
                        continue boi;
                    }
                }
            } while (position[0] < 0 || position[1] < 0 || positions.contains(position));
            positions.add(position);
        }
        // now we have lists of mine positions
        for (int i = 0; i < this.numberOfMines; i++) {
            this.playArea[positions.get(i)[1]][positions.get(i)[0]] = 2;
        }
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
        return this.playArea[yPos][xPos] == 0 || this.playArea[yPos][xPos] == 2;
    }

    public boolean isFlagged(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 3 || this.playArea[yPos][xPos] == 5;
    }

    public boolean isMine(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 2 || this.playArea[yPos][xPos] == 4 || this.playArea[yPos][xPos] == 5;
    }

    public boolean isDetonated(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 4;
    }

    public boolean isInactiveMine(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 2;
    }

    public boolean isClicked(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 1;
    }

    public boolean isSafe(int xPos, int yPos) {
        int val = this.playArea[yPos][xPos];
        return val == 0 || val == 1 || val == 3;
    }

    public void flag(int xPos, int yPos) {
        this.lastPlayArea = this.playArea;
        if (this.playArea[yPos][xPos] == 0) {// untouched
            this.playArea[yPos][xPos] = 3; // flag
        } else if (this.playArea[yPos][xPos] == 3) { //already flagged
            this.playArea[yPos][xPos] = 0; // untouched
        } else if (this.playArea[yPos][xPos] == 2) { // mine
            this.playArea[yPos][xPos] = 5; // flagged mine
        } else if (this.playArea[yPos][xPos] == 5) { //already flagged mine
            this.playArea[yPos][xPos] = 2; // unflagged mine
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
        this.playArea = new int[this.length][this.width];
    }

    public void setCells(int xPosition, int yPosition, int[][] cells) {
        this.lastPlayArea = this.playArea;
        int[][] newArea = this.playArea;
        for (int y = yPosition; y < yPosition + cells.length; y++) {
            for (int x = xPosition; x < xPosition + cells[0].length; x++) {
                //System.out.println("x: " + x + ", xPosition: " + xPosition + "\ny: " + y + ", yPosition: " + yPosition);
                newArea[y][x] = cells[y - yPosition][x - xPosition];
            }
        }
        this.playArea = newArea;
    }

    public void setCell(int x, int y, int value) {
        this.lastPlayArea = this.playArea;
        this.playArea[y][x] = value;
    }

    public int getCell(int x, int y) {
        return this.playArea[y][x];
    }

    public int safeCheck(int xPos, int yPos) {
        try {
            return this.playArea[yPos][xPos];
        } catch (ArrayIndexOutOfBoundsException b) {
            return -1;
        }
    }

    public int countVal(int value) {
        int count = 0;
        for (int y = 0; y < this.length; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.playArea[y][x] == value) {
                    count++;
                }
            }
        }
        int count2 = 0;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.length; y++) {
                if (safeCheck(x, y) == value) {
                    count2++;
                }
            }
        }
        return count;
    }

    public void click(int x, int y) {
        this.lastPlayArea = this.playArea;

        if (isMine(x, y) && countVal(1) == 0) { // it's kinda unfair to lose right out of the box
            int newX = 0;
            int newY = 0;
            while (1 == 1) {
                if (isMine(newX, newY)) {
                    if (newX < this.width - 1) {
                        newX++;
                    } else {
                        newY++;
                        newX = 0;
                    }
                } else {
                    this.playArea[newY][newX] = 2;
                    // System.out.println("Succesfully saved you from a mine, now at: " + newX + ", " + newY); // debug
                    this.playArea[y][x] = 1;
                    break;
                }
            }
        }
        switch (this.playArea[y][x]) {
            case 0:
                this.playArea[y][x] = 1;
                if (getNeighbors(x, y) == 0) {
                    if (safeCheck(x - 1, y) > -1) {
                        click(x - 1, y);
                    }
                    if (safeCheck(x + 1, y) > -1) {
                        click(x + 1, y);
                    }
                    if (safeCheck(x, y - 1) > -1) {
                        click(x, y - 1);
                    }
                    if (safeCheck(x, y + 1) > -1) {
                        click(x, y + 1);
                    }
                    if (safeCheck(x - 1, y - 1) > -1) {
                        click(x - 1, y - 1);
                    }
                    if (safeCheck(x - 1, y + 1) > -1) {
                        click(x - 1, y + 1);
                    }
                    if (safeCheck(x + 1, y - 1) > -1) {
                        click(x + 1, y - 1);
                    }
                    if (safeCheck(x + 1, y + 1) > -1) {
                        click(x + 1, y + 1);
                    }
                }
                break;
            case 1:
                break;
            case 2:
                this.playArea[y][x] = 4;
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;

        }
    }

    public void setPlayArea(int[][] newPlayArea) {
        this.playArea = newPlayArea;
    }

    public int getNeighbors(int xPos, int yPos) {
        int neighbors = 0;
        //check neighbors (with bounds checks)
        if (xPos > 0) {
            if (yPos > 0) {
                if (isMine(xPos - 1, yPos - 1)) { // if upper left
                    neighbors++;
                }
            }
        }

        if (yPos > 0) {
            if (isMine(xPos, yPos - 1)) { // if upper
                neighbors++;
            }
        }

        if (xPos < (this.length - 1)) {
            if (yPos > 0 && isMine(xPos + 1, yPos - 1)) { // if upper right
                neighbors++;
            }
        }

        if (xPos > 0 && yPos < (this.length - 1)) {
            if (isMine(xPos - 1, yPos + 1)) { // if lower left
                neighbors++;
            }
        }

        if (yPos < (this.length - 1)) {
            if (isMine(xPos, yPos + 1)) { // if lower
                neighbors++;
            }
        }

        if (yPos < (this.length - 1) && xPos < (this.width - 1)) {
            if (isMine(xPos + 1, yPos + 1)) { // if lower right
                neighbors++;
            }
        }

        if (xPos > 0) {
            if (isMine(xPos - 1, yPos)) {// if left
                neighbors++;
            }
        }

        if (xPos < (this.length - 1)) {
            if (isMine(xPos + 1, yPos)) { // if right
                neighbors++;
            }
        }
        return neighbors;

    }

    public boolean isZero(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 0;
    }

    @Override
    public String toString() {
        String output = "";
        for (int y = 0; y < this.width; y++) {
            for (int x = 0; x < this.length; x++) {
                output += String.valueOf(this.playArea[y][x]);
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
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"),
 * to deal in the Software without restriction, including without limitation
 * the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or
 * sell copies of the Software, and to permit persons to whom the Software
 * is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER
 * DEALINGS IN THE SOFTWARE.
 */
