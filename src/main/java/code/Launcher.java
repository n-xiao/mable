package code;

import code.backend.StorageHandler;
import code.frontend.gui.MainContainer;
import code.frontend.misc.Vals;
import code.frontend.misc.Vals.Colour;
import code.frontend.panels.CountdownPaneView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

        MainContainer root = MainContainer.getInstance();
        root.prefWidthProperty().bind(stage.widthProperty());
        root.prefHeightProperty().bind(stage.heightProperty());

        // Pane root = new Pane();
        // root.setBackground(Colour.createBG(Color.BLACK, 0, 0));
        // CountdownPaneView view = CountdownPaneView.getInstance();
        // view.relocate(20, 20);
        // view.setPrefSize(400, 400);
        // root.getChildren().add(view);

        Scene scene = new Scene(root);
        scene.setFill(Vals.Colour.BACKGROUND);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
