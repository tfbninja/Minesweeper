
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Tim Barber
 */
public class MinesweeperFX extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static Canvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();

        canvas = new Canvas(WIDTH, HEIGHT);

        root.getChildren().add(canvas);

        Scene primaryScene = new Scene(root, WIDTH, HEIGHT);
        /*
         * ClassExample ex = new ClassExample();
         * ex.paint(canvas);
         * ex.createHouse(canvas);
         */

        // Graphics code goes here
        stage.setScene(primaryScene);
        stage.setTitle("Title");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
