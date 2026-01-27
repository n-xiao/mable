/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code;

import code.backend.utils.StorageHandler;
import code.frontend.MainContainer;
import code.frontend.capabilities.countdown.concurrency.Watchdog;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    // public static final int PREF_WIDTH = 1170;
    // public static final int PREF_HEIGHT = 730;
    private static final int MIN_WIDTH = 890;
    private static final int MIN_HEIGHT = 600;

    @Override
    public void init() throws Exception {
        FontHandler.initFonts();
        StorageHandler.init();
    }

    @Override
    public void start(Stage stage) {
        RiceHandler.updatePalette("DARK");

        stage.setTitle("Mable");
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
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
