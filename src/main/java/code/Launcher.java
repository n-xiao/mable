package code;

import code.backend.Countdown;
import code.frontend.misc.Vals;
import code.frontend.panels.CountdownPane;
import code.frontend.windows.AddWindow;
import java.time.LocalDate;
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

        CountdownPane pd = new CountdownPane(new Countdown("hello", 30, 12, 2025), LocalDate.now());
        pd.relocate(30, 30);
        p.getChildren().add(pd);

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
