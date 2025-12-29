package code;

import code.frontend.gui.MainContainer;
import code.frontend.misc.Vals;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void init() throws Exception {
        Vals.FontTools.initFonts();
        // StorageHandler.init();
    }

    @Override
    public void start(Stage stage) {
        stage.setMinWidth(Vals.GraphicalUI.PREF_WIDTH);
        stage.setMinHeight(Vals.GraphicalUI.PREF_HEIGHT);
        stage.setWidth(Vals.GraphicalUI.PREF_WIDTH);
        stage.setHeight(Vals.GraphicalUI.PREF_HEIGHT);

        MainContainer root = MainContainer.getInstance();
        root.prefWidthProperty().bind(stage.widthProperty());
        root.prefHeightProperty().bind(stage.heightProperty());

        Scene scene = new Scene(root);
        scene.setFill(Vals.Colour.BACKGROUND);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
