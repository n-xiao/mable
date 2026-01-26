/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code;

import code.backend.utils.StorageHandler;
import code.frontend.gui.containers.MainContainer;
import code.frontend.gui.ricing.RiceHandler;
import code.frontend.misc.Vals;
import code.frontend.misc.Watchdog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void init() throws Exception {
        Vals.FontTools.initFonts();
        StorageHandler.init();
    }

    @Override
    public void start(Stage stage) {
        RiceHandler.updatePalette("DARK");

        stage.setTitle("Mable");
        stage.setMinWidth(Vals.GraphicalUI.MIN_WIDTH);
        stage.setMinHeight(Vals.GraphicalUI.MIN_HEIGHT);
        stage.setWidth(Vals.GraphicalUI.PREF_WIDTH);
        stage.setHeight(Vals.GraphicalUI.PREF_HEIGHT);
        stage.setOnCloseRequest((event) -> { Platform.exit(); });

        MainContainer root = MainContainer.getInstance();
        root.prefWidthProperty().bind(stage.widthProperty());
        root.prefHeightProperty().bind(stage.heightProperty());

        Scene scene = new Scene(root);
        scene.setFill(RiceHandler.getColour("background1"));
        stage.setScene(scene);
        root.init();
        stage.show();

        Watchdog.startWatchdog();
    }

    @Override
    public void stop() throws Exception {
        StorageHandler.save(); // saves on app close
    }

    public static void main(String[] args) {
        launch();
    }
}
