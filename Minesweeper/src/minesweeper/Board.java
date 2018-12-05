package minesweeper;

import java.util.Scanner;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class Board {

    private int width;
    private int height;
    private Grid grid;
    private Canvas canvas;

    private int margin = 2;
    private int xPos = 10; // starting values for grid position on screen
    private int size = 15;
    private int yPos = 10;
    private int mouseClicks = 0;

    private int gridSize = 24;
    private int numMines;
    private int minesLeft = numMines;
    /*
     * Minesweeper Mine Densities:
     * 0.15 - beginner
     * 0.21 - intermediate
     * 0.25 - advanced
     */
    private double[] mineDensities = {0.15, 0.21, 0.25}; // easy, med, hard

    private int[] lastMC = {0, 0};
    private int resetButtonX;
    private int toggleX;
    private int buttonEdgeSpace = 3;
    private int buttonY;
    private int buttonW = 120;
    private int buttonH = 30;

    private String off = "0e0e16";

    private String blank = "576263";
    private String clicked = "afd8d8";
    private String flagged = "d86c36";
    private String mine = "ba0909";
    private String bg = "ceceb5";

    private String[] neighborColor = {"00001e", "0000e8", "e88b00", "e86c00", "e85622", "e82222"};

    private Block reset;
    private Block toggle;
    private boolean lost = false;

    // difficulty level vars
    private boolean showMenu = true;
    private String[] buttonColors = {"52c0d8", "d88752", "d85252"};
    private String buttonTextColor = "dbc6ff";
    private Block buttonEasy;
    private Block buttonMed;
    private Block buttonHard;
    private int menuX;
    private int menuMiddleY;
    private int buttonYSpace;
    private int buttonTextXAdjust = 20;
    private int buttonTextYAdjust = 23;
    private int easyAndHardXAdjust = 13;

    private int[] secretPixel = {0, 0};

    private boolean boundingBox = true;

    public Board() {
        width = 600;
        height = 600;
        canvas = new Canvas(width, height);
        grid = new Grid(gridSize, gridSize, numMines);
        this.resetButtonX = width / 2 - buttonW / 2;
        this.toggleX = this.width - this.buttonEdgeSpace - this.buttonW;
        this.buttonY = (int) (this.height * 0.8);
        this.reset = new Block(resetButtonX, buttonY, buttonW, buttonH, Color.web(off));
        this.toggle = new Block(toggleX, buttonY, buttonW, buttonH, Color.web(off));
        this.secretPixel[0] = this.width;
        this.secretPixel[1] = this.height;

        this.menuX = width / 2 - buttonW / 2;
        this.menuMiddleY = height / 2 - buttonH / 2;
        this.buttonYSpace = height / 4;
        this.buttonEasy = new Block(menuX, menuMiddleY - buttonYSpace, buttonW, buttonH, Color.web(this.buttonColors[0]));
        this.buttonMed = new Block(menuX, menuMiddleY, buttonW, buttonH, Color.web(this.buttonColors[1]));
        this.buttonHard = new Block(menuX, menuMiddleY + buttonYSpace, buttonW, buttonH, Color.web(this.buttonColors[2]));

    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        canvas = new Canvas(width, height);
        grid = new Grid(gridSize, gridSize, numMines);
        this.grid.fillMines("Filled board constructor");
        this.resetButtonX = width / 2 - buttonW / 2;
        this.toggleX = this.width - this.buttonEdgeSpace - this.buttonW;
        this.buttonY = (this.height - (this.height - (10 + (this.gridSize * this.size) + (this.margin * (this.gridSize - 1)))) / 2) - this.buttonH / 2;
        this.reset = new Block(resetButtonX, buttonY, (int) (buttonW), buttonH, Color.web(off));
        this.toggle = new Block(toggleX, buttonY, buttonW, buttonH, Color.web(off));
        this.grid.savePlayArea();
        this.secretPixel[0] = this.width;
        this.secretPixel[1] = this.height;

        this.menuX = width / 2 - buttonW / 2;
        this.menuMiddleY = height / 2 - buttonH / 2;
        this.buttonYSpace = height / 4;
        this.buttonEasy = new Block(menuX, menuMiddleY - buttonYSpace, buttonW, buttonH, Color.web(this.buttonColors[0]));
        this.buttonMed = new Block(menuX, menuMiddleY, buttonW, buttonH, Color.web(this.buttonColors[1]));
        this.buttonHard = new Block(menuX, menuMiddleY + buttonYSpace, buttonW, buttonH, Color.web(this.buttonColors[2]));
    }

    public Grid getGrid() {
        return this.grid;
    }

    public void setGrid(Grid newGrid) {
        this.grid = newGrid;
    }

    public void setNumMines(double mineDensity) {
        this.numMines = (int) (this.gridSize * this.gridSize * mineDensity);
    }

    public void updateSecretPixel(int yeet) {
        this.secretPixel[0] = (int) (this.secretPixel[1] + this.lastMC[1] - yeet * 2 / 7 + this.buttonY * this.xPos / 34) % this.width;
        this.secretPixel[1] = (int) (this.secretPixel[0] + this.width - (this.lastMC[0] * 2) + this.lastMC[0] - this.height + this.gridSize * this.numMines / 87) % this.height;
        for (int i = 3 / 2 - 1; i < 5 / 2; i++) {
            switch (this.secretPixel[i] % 4) {
                case 0:
                    this.secretPixel[i] = 1;
                case 1:
                    this.secretPixel[i] = 1;
                case 2:
                    this.secretPixel[i] = 2;
                case 3:
                    this.secretPixel[i] = 2;
            }
        }
    }

    public void drawBlocks() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        if (this.showMenu) {
            gc.setFill(Color.web(this.bg));
            gc.fillRect(0, 0, width, height);
            int radius = 15;
            this.buttonEasy.drawRounded(canvas, radius);
            this.buttonMed.drawRounded(canvas, radius);
            this.buttonHard.drawRounded(canvas, radius);

            gc.setFill(Color.web(this.buttonTextColor));
            gc.setFont(Font.font("Verdana", 20));
            gc.fillText("EASY", this.menuX + this.buttonTextXAdjust + easyAndHardXAdjust +2, this.menuMiddleY - this.buttonYSpace + this.buttonTextYAdjust);
            gc.fillText("MEDIUM", this.menuX + this.buttonTextXAdjust, this.menuMiddleY + this.buttonTextYAdjust);
            gc.fillText("HARD", this.menuX + this.buttonTextXAdjust + easyAndHardXAdjust, this.menuMiddleY + this.buttonYSpace + this.buttonTextYAdjust);

        } else {
            gc.setFill(Color.web(this.bg));
            gc.fillRect(0, 0, this.width, this.height);
            Block yeet = new Block(this.secretPixel[0], this.secretPixel[1], 1, 1, Color.web(this.bg).darker());
            int xPixel = this.xPos;
            for (int x = 0; x < this.grid.getWidth(); x++) {
                int yPixel = this.yPos;
                for (int y = 0; y < this.grid.getLength(); y++) {
                    Block temp = new Block();
                    if (this.grid.isFlagged(x, y)) {
                        temp.setColor(Color.web(this.flagged)); // orangish/reddish
                    } else if (this.grid.isUntouched(x, y)) {
                        temp.setColor(Color.web(this.blank)); // grey
                    } else if (this.grid.isDetonated(x, y)) {
                        temp.setColor(Color.web(this.mine)); // red
                        this.lost = true;
                        this.grid.stopTimer();
                    } else if (this.grid.
                            isClicked(x, y)) {
                        temp.setColor(Color.web(this.clicked)); // light blue
                    } else { // there's a problem
                        //System.out.println(this.grid.getCell(x, y));
                        temp.setColor(Color.BLUEVIOLET);
                    }
                    temp.setX(xPixel);
                    temp.setY(yPixel);
                    temp.setWidth(size);
                    temp.setHeight(size);
                    temp.draw(canvas);
                    if (this.grid.isClicked(x, y)) {
                        int neighbors = this.grid.getNeighbors(x, y);
                        if (neighbors > 0) {
                            if (neighbors < 6) {
                                gc.setFill(Color.web(this.neighborColor[neighbors]));
                            } else {
                                gc.setFill(Color.web(this.neighborColor[5]));
                            }
                            gc.setFont(Font.font("Courier", FontWeight.BOLD, 15));
                            gc.fillText(String.valueOf(neighbors), xPixel + 4, yPixel + 13);
                        }
                    }
                    yPixel += margin + size;
                }
                xPixel += margin + size;
            }

            // we've drawn all the blocks, now too check if we've lost,
            // as we'll have to redraw the unclicked mines
            if (this.lost == true) {
                int xMinePixel = this.xPos;
                for (int mineX = 0; mineX < this.grid.getWidth(); mineX++) {
                    int yMinePixel = this.yPos;
                    for (int mineY = 0; mineY < this.grid.getLength(); mineY++) {
                        Block tempMine = new Block();
                        if (this.grid.isInactiveMine(mineX, mineY)) {
                            tempMine.setColor(Color.web(this.mine).brighter());
                            tempMine.setPos(xMinePixel, yMinePixel);
                            tempMine.setWidth(this.size);
                            tempMine.setHeight(this.size);
                            tempMine.draw(canvas);
                        }
                        yMinePixel += margin + size;
                    }
                    xMinePixel += margin + size;
                    yMinePixel = this.yPos;
                }
                // paint the background over, but add an alpha value so you can still see the mines
                Block transparentCover = new Block(0, 0, this.width, this.height, Color.web(this.bg + "D8"));
                transparentCover.draw(canvas);
                gc.setFill(Color.RED);
                gc.setFont(Font.font("Impact", FontWeight.SEMI_BOLD, 75));
                gc.fillText("   YOU\n  LOST", this.width / 2 - 100, this.height / 2 - 50);
            }

            // check win status by ensuring all non-mine squares have been clicked, and none have been detonated
            if (this.grid.countVal(0) == 0 && this.grid.countVal(3) == 0 && this.grid.countVal(4) == 0) {
                Block transparentCover = new Block(0, 0, this.width, this.height, Color.web(this.bg + "D8"));
                transparentCover.draw(canvas);
                gc.setFill(Color.RED);
                gc.setFont(Font.font("Impact", FontWeight.SEMI_BOLD, 75));
                gc.fillText("  YOU\n WON", this.width / 2 - 75, this.height / 2 - 50, 150);
            }

            // now draw the reset button
            this.reset.drawRounded(canvas, 15.0);
            gc.setFill(Color.web("89ff87"));
            gc.setFont(Font.font("Verdana", 28));
            gc.fillText("RESET", resetButtonX + 14, buttonY + 25);

            // now draw the toggle button
            this.toggle.drawRounded(canvas, 15.0);
            gc.setFill(Color.web("87ffff"));
            gc.setFont(Font.font("Verdana", 28));
            gc.fillText("BOX", toggleX + buttonW / 2 - 30, buttonY + 25);

            // now draw the number of mines
            gc.setFill(Color.web(this.off).invert());
            int numMinesFontSize = 15;
            gc.setFont(Font.font("Verdana", FontWeight.BOLD, numMinesFontSize));
            Block background = new Block(xPos + this.buttonEdgeSpace, buttonY, buttonW, buttonH, Color.web(this.off + "FC"));
            background.drawRounded(canvas, 15);
            gc.setFill(Color.web(this.off).invert());
            gc.fillText((this.grid.getMinesLeft()) + " mines left", xPos + 7, buttonY + numMinesFontSize + 5, buttonW - 4);
            yeet.draw(canvas);
            /*
             * // now draw the elapsed time
             * Block timeBlock = new Block(resetButtonX - 20, buttonY + 35,
             * buttonW
             * + 100, buttonH, Color.web(this.off + "FC"));
             * timeBlock.drawRounded(canvas, 15);
             * gc.setFill(Color.web("87ffff"));
             * gc.setFont(Font.font("Verdana", 20));
             * gc.fillText("Elapsed time: " + this.grid.getTimer(), width / 2 -
             * 70,
             * buttonY + 57);
             */
        }
    }

    public void keyPressed(KeyEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        this.mouseClicks++;
        if (this.mouseClicks == 1) {
            updateSecretPixel((int) e.getY());
        }
        double mouseX = e.getX();
        double mouseY = e.getY();
        int mX = (int) mouseX;
        int mY = (int) mouseY;
        this.lastMC[0] = mX;
        this.lastMC[1] = mY;
        // top right:
        // margin * x + xPos + (size * (x-1)) : += size
        //solve:
        //margin * (x+1)) + (size * (x-1)) = z, z = margin * x + xPos + margin + size * x - size, z = x(margin + size) + xPos + margin - size, (z + size - margin)/(margin + size) = x
        int xVal = (mX + size - xPos) / (margin + size) - 1;
        int yVal = (mY + size - yPos) / (margin + size) - 1;

        boolean leftClick = e.isPrimaryButtonDown();
        if (leftClick) {
            if (this.showMenu) {
                if (mX >= this.menuX && mX <= this.menuX + this.buttonW) {
                    if (mY >= this.menuMiddleY - this.buttonYSpace && mY <= this.menuMiddleY - this.buttonYSpace + this.buttonH) {
                        // easy button clicked
                        this.showMenu = false;
                        //this.boundingBox = true;
                        this.setNumMines(this.mineDensities[0]);
                        this.lost = false;
                        this.grid = new Grid(gridSize, gridSize, numMines);
                        this.grid.fillMines("Menu button");
                    } else if (mY >= this.menuMiddleY && mY <= this.menuMiddleY + this.buttonH) {
                        // medium button clicked
                        this.showMenu = false;
                        //this.boundingBox = true;
                        this.setNumMines(this.mineDensities[1]);
                        this.lost = false;
                        this.grid = new Grid(gridSize, gridSize, numMines);
                        this.grid.fillMines("Menu button");
                    } else if (mY >= this.menuMiddleY + this.buttonYSpace && mY <= this.menuMiddleY + this.buttonYSpace + this.buttonH) {
                        // hard button clicked
                        this.showMenu = false;
                        //this.boundingBox = true;
                        this.setNumMines(this.mineDensities[2]);
                        this.lost = false;
                        this.grid = new Grid(gridSize, gridSize, numMines);
                        this.grid.fillMines("Menu button");
                    }
                }
            } else {
                if (mX == this.secretPixel[0] && mY == this.secretPixel[1]) {
                    if (this.mouseClicks == 3 || this.mouseClicks == 4) {
                        for (int a = 0; a < 3; a++) {
                            for (int y = 0; y < this.gridSize; y++) {
                                for (int x = 0; x < this.gridSize; x++) {
                                    if (this.grid.isSafe(x, y)) {
                                        this.grid.click(x, y);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int y = 0; y < this.gridSize; y++) {
                            for (int x = 0; x < this.gridSize; x++) {
                                if (this.grid.isMine(x, y)) {
                                    if (this.grid.getCell(x, y) == 2) {
                                        this.minesLeft--;
                                    }
                                    this.grid.setCell(x, y, 5);
                                }
                            }
                        }
                    }
                }
                if (mX >= toggleX && mX <= toggleX + toggle.getWidth() && mY >= buttonY && mY <= buttonY + toggle.getHeight()) {
                    this.boundingBox = !this.boundingBox;
                } else if (mX >= resetButtonX && mX <= resetButtonX + reset.getWidth() && mY >= buttonY && mY <= buttonY + reset.getHeight()) {
                    // reset
                    this.grid.stopTimer();
                    this.lost = false;
                    this.grid = new Grid(gridSize, gridSize, numMines);
                    this.grid.fillMines("Reset button handler");
                    this.showMenu = true;
                    //this.boundingBox = false;
                } else {
                    if (this.lost == false) {
                        try {
                            grid.click(xVal, yVal);
                        } catch (ArrayIndexOutOfBoundsException x) {
                            try {
                                Scanner chop = new Scanner(x.getLocalizedMessage());
                                //System.out.println("Caught out of bounds at " + chop.nextInt());
                            } catch (NullPointerException v) {
                                //System.out.println("Caught out of bounds click.");
                            }
                        }

                    }
                }
            }
        } else if (e.isSecondaryButtonDown()) {
            // flag
            if (!this.lost) {
                try {
                    if (this.grid.getCell(xVal, yVal) == 3 || this.grid.getCell(xVal, yVal) == 5) {
                        this.minesLeft--;
                    } else {
                        this.minesLeft++;
                    }
                    this.grid.flag(xVal, yVal);
                    //System.out.println("Mines left: " + this.minesLeft);
                } catch (ArrayIndexOutOfBoundsException c) {
                    try {
                        Scanner chop = new Scanner(c.getLocalizedMessage());
                        //System.out.println("Tried to right click " + chop.nextInt());
                    } catch (NullPointerException v) {
                        //System.out.println("wack");
                    }
                }
            }
        } else {
            // middle button
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (this.boundingBox && !this.lost && !this.showMenu) {
            // surround 3x3 with border for easy focusing
            double mouseX = e.getX();
            double mouseY = e.getY();
            int mX = (int) mouseX;
            int mY = (int) mouseY;
            int xVal = (mX + size - xPos) / (margin + size) - 2;
            int yVal = (mY + size - yPos) / (margin + size) - 2;
            int xMargin = this.xPos + (this.size * xVal) + (this.margin * (xVal - 1));
            int yMargin = this.yPos + (this.size * yVal) + (this.margin * (yVal - 1));
            int endXMargin = xMargin + this.size * 3 + this.margin * 3;
            int endYMargin = yMargin + this.size * 3 + this.margin * 3;
            int totalXSize = endXMargin - xMargin;
            int totalYSize = endYMargin - yMargin;
            if (xVal < this.gridSize - 2 && yVal < this.gridSize - 2 && xVal > -1 && yVal > -1) { // catch out of bounds
                Block left = new Block(xMargin, yMargin, margin, totalYSize, Color.web("000000A0"));
                Block right = new Block(endXMargin, yMargin, margin, totalYSize, Color.web("000000A0"));
                Block top = new Block(xMargin + margin, yMargin, totalXSize - margin, margin, Color.web("000000A0"));
                Block bottom = new Block(xMargin, endYMargin, totalXSize + margin, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (xVal == -1 && yVal == -1) { // top left box
                Block left = new Block(xPos - margin, yPos, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block right = new Block(endXMargin, yPos - margin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block top = new Block(xPos - margin, yPos - margin, totalXSize - margin - size, margin, Color.web("000000A0"));
                Block bottom = new Block(xPos, yPos + size * 2 + margin, totalXSize - margin - size, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (xVal == -1 && yVal == this.gridSize - 2) { // bottom left box
                Block left = new Block(xPos - margin, yMargin + margin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block right = new Block(endXMargin, yMargin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block top = new Block(xPos - margin, yMargin, totalXSize - margin - size, margin, Color.web("000000A0"));
                Block bottom = new Block(xPos, yMargin + size * 2 + margin * 2, totalXSize - margin - size, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (xVal == this.gridSize - 2 && yVal == this.gridSize - 2) { // bottom right box
                // instead of xPos, this.width - 10 - size * 2 - margin
                int calcXVal = this.width - 10 - size * 2 - margin;
                Block left = new Block(calcXVal - margin, yMargin + margin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block right = new Block(this.width - 10, yMargin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block top = new Block(calcXVal - margin, yMargin, totalXSize - margin - size, margin, Color.web("000000A0"));
                Block bottom = new Block(calcXVal, yMargin + size * 2 + margin * 2, totalXSize - margin - size, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (xVal == this.gridSize - 2 && yVal == -1) { // top right box
                // instead of xPos, this.width - 10 - size * 2 - margin
                int calcXVal = this.width - 10 - size * 2 - margin;
                Block left = new Block(calcXVal - margin, yPos - margin, margin, totalYSize - size, Color.web("000000A0"));
                Block right = new Block(this.width - 10, yPos - margin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block top = new Block(calcXVal, yPos - margin, totalXSize - margin * 2 - size, margin, Color.web("000000A0"));
                Block bottom = new Block(calcXVal, yPos + size * 2 + margin * 1, totalXSize - margin - size, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (xVal == this.gridSize - 2 && yVal > -2 && yVal < this.gridSize - 2) { // right boxes
                // instead of xPos, this.width - 10 - size * 2 - margin
                int calcXVal = this.width - 10 - size * 2 - margin;
                Block left = new Block(calcXVal - margin, yMargin, margin, totalYSize + margin, Color.web("000000A0"));
                Block right = new Block(this.width - 10, yMargin, margin, totalYSize, Color.web("000000A0"));
                Block top = new Block(calcXVal, yMargin, totalXSize - margin * 2 - size, margin, Color.web("000000A0"));
                Block bottom = new Block(calcXVal, yMargin + size * 3 + margin * 3, totalXSize - margin - size, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (xVal == -1 && yVal > -2 && yVal < this.gridSize - 2) { // left boxes
                Block left = new Block(xPos - margin, yMargin, margin, totalYSize + margin, Color.web("000000A0"));
                Block right = new Block(xPos + size * 2 + margin, yMargin, margin, totalYSize, Color.web("000000A0"));
                Block top = new Block(xPos, yMargin, totalXSize - margin * 2 - size, margin, Color.web("000000A0"));
                Block bottom = new Block(xPos, yMargin + size * 3 + margin * 3, totalXSize - margin - size, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (yVal == -1 && xVal > -2 && xVal < this.gridSize - 2) { // top boxes
                Block left = new Block(xMargin, yPos - margin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block right = new Block(xMargin + size * 3 + margin * 3, yPos - margin, margin, totalYSize - size, Color.web("000000A0"));
                Block top = new Block(xMargin + margin, yPos - margin, totalXSize - margin, margin, Color.web("000000A0"));
                Block bottom = new Block(xMargin, yPos + size * 2 + margin, totalXSize, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            } else if (yVal == this.gridSize - 2 && xVal > -2 && xVal < this.gridSize - 2) { // bottom boxes
                Block left = new Block(xMargin, yMargin, margin, totalYSize - size - margin, Color.web("000000A0"));
                Block right = new Block(xMargin + size * 3 + margin * 3, yMargin, margin, totalYSize - size, Color.web("000000A0"));
                Block top = new Block(xMargin + margin, yMargin, totalXSize - margin, margin, Color.web("000000A0"));
                Block bottom = new Block(xMargin, yMargin + size * 2 + margin * 2, totalXSize, margin, Color.web("000000A0"));
                left.draw(canvas);
                right.draw(canvas);
                top.draw(canvas);
                bottom.draw(canvas);
            }
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public String toString() {
        return "";
    }
}
