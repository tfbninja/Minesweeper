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

    private int numMines = 30;
    private int resetButtonX;
    private int buttonY;
    private int buttonW = 120;
    private int buttonH = 30;

    private String off = "0e0e16";

    private String blank = "576263";
    private String clicked = "afd8d8";
    private String flagged = "d86c36";
    private String mine = "ba0909";

    private String[] neighborColor = {"00001e", "0000e8", "e8b900", "e89f22", "e85622", "e82222"};

    private Block reset;
    private int gridSize = 20;

    public Board() {
        width = 600;
        height = 600;
        canvas = new Canvas(width, height);
        grid = new Grid(gridSize, gridSize, numMines);
        this.resetButtonX = width / 2 - buttonW / 2;
        this.buttonY = (int) (this.height * 0.8);
        this.reset = new Block(resetButtonX, buttonY, buttonW, buttonH, Color.web(off));
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        canvas = new Canvas(width, height);
        grid = new Grid(gridSize, gridSize, numMines);
        this.grid.fillMines();
        this.resetButtonX = width / 2 - buttonW / 2;
        this.buttonY = (int) (this.height * 0.8);
        this.reset = new Block(resetButtonX, buttonY, (int) (buttonW), buttonH, Color.web(off));
        this.grid.savePlayArea();
    }

    public Grid getGrid() {
        return this.grid;
    }

    public void setGrid(Grid newGrid) {
        this.grid = newGrid;
    }

    public void drawBlocks() {
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
                } else if (this.grid.isClicked(x, y)) {
                    temp.setColor(Color.web(this.clicked)); // light blue
                } else { // there's a problem
                    System.out.println(this.grid.getCell(x, y));
                    temp.setColor(Color.BLUEVIOLET);
                }
                temp.setX(xPixel);
                temp.setY(yPixel);
                temp.setWidth(size);
                temp.setHeight(size);
                temp.draw(canvas);
                if (this.grid.isClicked(x, y)) {
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    int neighbors = this.grid.getNeighbors(x, y);
                    if (neighbors < 6) {
                        gc.setFill(Color.web(this.neighborColor[neighbors]));
                    } else {
                        gc.setFill(Color.web(this.neighborColor[5]));
                    }
                    gc.setFont(Font.font("Courier", FontWeight.BOLD, 15));
                    gc.fillText(String.valueOf(neighbors), xPixel + 4, yPixel + 13);
                }
                yPixel += margin + size;
            }
            xPixel += margin + size;
        }
        this.reset.drawRounded(canvas, 15.0);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("89ff87"));
        gc.setFont(Font.font("Verdana", 28));
        gc.fillText("RESET", resetButtonX + 14, buttonY + 25);
    }

    public void keyPressed(KeyEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();
        int mX = (int) mouseX;
        int mY = (int) mouseY;
        // top right:
        // margin * x + xPos + (size * (x-1)) : += size
        //solve:
        //margin * (x+1)) + (size * (x-1)) = z, z = margin * x + xPos + margin + size * x - size, z = x(margin + size) + xPos + margin - size, (z + size - margin)/(margin + size) = x
        int xVal = (mX + size - xPos) / (margin + size) - 1;
        int yVal = (mY + size - yPos) / (margin + size) - 1;

        boolean leftClick = e.isPrimaryButtonDown();
        if (leftClick) {
            if (mX >= resetButtonX && mX <= resetButtonX + reset.getWidth() && mY >= buttonY && mY <= buttonY + reset.getHeight()) {
                System.out.println("Reset");
                this.grid.clear();
                this.grid.revertToSaved();
            } else {

                try {
                    grid.click(xVal, yVal);
                    System.out.println("Cell changed: [" + xVal + ", " + yVal + "]");
                } catch (ArrayIndexOutOfBoundsException x) {
                    try {
                        Scanner chop = new Scanner(x.getLocalizedMessage());
                        System.out.println("Tried to click " + chop.nextInt());
                    } catch (NullPointerException v) {
                        System.out.println("wack");
                    }
                }
            }
        } else if (e.isSecondaryButtonDown()) {
            // flag
            try {
                this.grid.flag(xVal, yVal);
            } catch (ArrayIndexOutOfBoundsException c) {
                try {
                    Scanner chop = new Scanner(c.getLocalizedMessage());
                    System.out.println("Tried to right click " + chop.nextInt());
                } catch (NullPointerException v) {
                    System.out.println("wack");
                }
            }
        } else {
            // middle button
        }
    }

    public void mouseMoved(MouseEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();
        int mX = (int) mouseX;
        int mY = (int) mouseY;
        int xVal = (mX + size - xPos) / (margin + size) - 1;
        int yVal = (mY + size - yPos) / (margin + size) - 1;

    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public String toString() {
        return "";
    }
}
