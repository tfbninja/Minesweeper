package minesweeper;

import java.util.Scanner;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class Board {

    private int width;
    private int height;
    private Grid grid;
    private Canvas canvas;

    private int margin = 2;
    private int xPos = 10; // starting values for grid position on screen
    private int size = 10;
    private int yPos = 10;

    private int numMines = 15;
    private int resetButtonX;
    private int buttonY;
    private int buttonW = 50;
    private int buttonH = 30;

    private String off = "8e9196";

    private String blank = "576263";
    private String clicked = "afd8d8";
    private String flagged = "d86c36";
    private String mine = "ba0909";

    private Block reset;
    private int gridSize = 20;

    public Board() {
        width = 600;
        height = 600;
        canvas = new Canvas(width, height);
        grid = new Grid(gridSize, gridSize, numMines);
        this.resetButtonX = 300;
        this.buttonY = 500;
        this.reset = new Block(resetButtonX, buttonY, (int) (buttonW * 2.4), buttonH, Color.web(off));
    }

    public Board(int width, int height) {
        this.width = 600;
        this.height = 600;
        this.resetButtonX = this.width - (width / 4 - this.buttonW / 2) - buttonW * 2;
        this.buttonY = (int) (this.height * 2 - this.height / 1.1);
        this.width = width;
        this.height = height;
        canvas = new Canvas(width, height);
        grid = new Grid(gridSize, gridSize, numMines);
        this.reset = new Block(resetButtonX, buttonY, (int) (buttonW * 2.4), buttonH, Color.web(off));
        int[][] start = {{0, 0}, {0, 0}};
        this.grid.setPlayArea(start);
        this.grid.savePlayArea();

    }

    public Grid getGrid() {
        return this.grid;
    }

    public void setGrid(Grid newGrid) {
        this.grid = newGrid;
    }

    public void drawBlocks() {
        Block bg = new Block();
        bg.setColor(Color.BLACK);
        bg.setPos(0, 0);
        bg.setWidth(this.width);
        bg.setHeight(this.height);
        bg.draw(canvas);
        for (int x = 0; x < this.grid.getWidth(); x++) {
            yPos = 10;
            for (int y = 0; y < this.grid.getLength(); y++) {
                Block temp = new Block();
                if (this.grid.isFlagged(x, y)) {
                    temp.setColor(Color.web(this.flagged)); // orangish/reddish
                } else if (this.grid.isUntouched(x, y)) {
                    temp.setColor(Color.web(this.blank)); // grey
                } else if (this.grid.isDetonated(x, yPos)) { // red
                    temp.setColor(Color.web(this.mine));
                } else if (this.grid.isClicked(x, y)) { // light blue
                    temp.setColor(Color.web(this.clicked));
                }
                temp.setX(xPos);
                temp.setY(yPos);
                temp.setWidth(size);
                temp.setHeight(size);
                temp.draw(canvas);
                yPos += margin + size;
            }
            xPos += margin + size;
        }
        yPos = 10; //reset values
        xPos = 10;

        this.reset.draw(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("000000"));
        gc.setStroke(Color.RED);
        gc.setFont(Font.font("Verdana", 30));
        gc.fillText("RESET", resetButtonX + 10, buttonY + 26);
        /*
         * gc.setFont(Font.font("Arial", 15));
         * gc.setFill(Color.WHITE);
         * gc.setLineWidth(1);
         * gc.fillText("Instructions: Hold any key to play at full speed,
         * left-click a cell to toggle, right click to toggle\n the column of
         * cells, and middle click to spawn a glider.", 10, buttonY - 28);
         *
         */
        //gc.strokeText("Hold any key to play at full speed", this.width / 2 - 95, buttonY - 10);
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
                    grid.setCell(xVal, yVal, !grid.getCell(xVal, yVal));
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
            // make line crossing the grid
            this.grid.flag(xVal, yVal);
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
