package code;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void init() throws Exception {
        Vals.initFonts();
    }

    @Override
    public void start(Stage stage) {
        stage.setMinWidth(Vals.GraphicalUI.MIN_WIDTH);
        stage.setMinHeight(Vals.GraphicalUI.MIN_HEIGHT);
        stage.setWidth(Vals.GraphicalUI.PREF_WIDTH);
        stage.setHeight(Vals.GraphicalUI.PREF_HEIGHT);
        Pane p = new Pane();
        p.setPrefSize(Vals.GraphicalUI.PREF_WIDTH, Vals.GraphicalUI.PREF_HEIGHT);
        p.setMinSize(Vals.GraphicalUI.MIN_WIDTH, Vals.GraphicalUI.MIN_HEIGHT);
        p.relocate(0, 0);

        SwingNode swingNode = new SwingNode();
        CustomBox box = new CustomBox(8);
        box.setSize(200, 200);
        swingNode.setContent(box);
        p.getChildren().add(swingNode);

        Scene scene = new Scene(p);
        scene.setFill(Vals.Colour.BACKGROUND);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
