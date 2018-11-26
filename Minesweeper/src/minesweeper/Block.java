package minesweeper;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block implements Locatable {

    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private Color color;

    public Block() {
        this.xPos = 0;
        this.yPos = 0;
        this.width = 0;
        this.height = 0;
        this.color = Color.MINTCREAM;
    }

    public Block(int xPos, int yPos, int width, int height, Color color) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Block(int xPos, int yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.color = Color.BLACK;
    }

    @Override
    public void setX(int xPos) {
        this.xPos = xPos;
    }

    @Override
    public void setY(int yPos) {
        this.yPos = yPos;
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Canvas canvas) {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(color);
        graphics.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    public int getX() {
        return this.xPos;
    }

    public int getY() {
        return this.yPos;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "X: " + this.xPos + ", Y: " + this.yPos + ", Width: " + this.width + ", Height: " + this.height + ", Color: " + this.color;
    }
}
