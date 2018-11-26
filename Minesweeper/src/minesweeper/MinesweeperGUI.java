package minesweeper;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MinesweeperGUI extends Application {

    private static final int WIDTH = 618;
    private static final int HEIGHT = 700;
    private int startTime;

    public void start(Stage stage) {

        HBox root = new HBox();

        Board board = new Board(WIDTH, HEIGHT);
        Block bg = new Block(0, 0, WIDTH, HEIGHT, Color.web("87a2a5")); // nice light gray
        bg.draw(board.getCanvas());

        board.drawBlocks();

        root.getChildren().add(board.getCanvas());

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Minesweeper");
        stage.show();

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                board.mouseClicked(event);
                board.drawBlocks();
            }
        });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent eventa) {
                board.keyPressed(eventa);
                board.drawBlocks();
            }
        });
        /*
         * scene.setOnMouseMoved(new EventHandler<MouseEvent>() { public void
         * handle(MouseEvent eventb) {
         *
         * board.drawBlocks(); } });
         *
         */
    }

    public static void main(String args[]) {
        launch(args);
    }
}
