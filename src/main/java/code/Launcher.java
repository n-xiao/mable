package code;

import code.frontend.misc.Vals;
import code.frontend.panels.DateInputField;
import code.frontend.windows.AddWindow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void init() throws Exception {
        Vals.FontTools.initFonts();
        // StorageHandler.init();
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
        p.setBackground(null);

        DateInputField dif = new DateInputField();
        dif.relocate(50, 50);
        p.getChildren().add(dif);

        Scene scene = new Scene(p);
        scene.setFill(Vals.Colour.BACKGROUND);
        stage.setScene(scene);
        stage.show();
        AddWindow.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}
