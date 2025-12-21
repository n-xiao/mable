package code;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

        CustomBox box = new CustomBox(4);
        p.getChildren().add(box);
        // p.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

        box.widthProperty().bind(p.widthProperty());
        box.heightProperty().bind(p.heightProperty());

        Scene scene = new Scene(p);
        scene.setFill(Vals.Colour.BACKGROUND);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
