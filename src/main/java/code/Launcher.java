/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code;

import code.backend.data.StorageHandler;
import code.backend.settings.SettingsHandler;
import code.backend.settings.SettingsHandler.Key;
import code.frontend.MainContainer;
import code.frontend.capabilities.concurrency.Watchdog;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.icons.IconHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    private static final int PREF_HEIGHT = 600;
    private static final int PREF_WIDTH = 500;
    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT = 400;

    @Override
    public void init() throws Exception {
        FontHandler.init();
        IconHandler.init();
        StorageHandler.init();
    }

    @Override
    public void start(Stage stage) {
        if (SettingsHandler.getBooleanValue(Key.LIGHT_MODE))
            RiceHandler.updatePalette("LIGHT");
        else
            RiceHandler.updatePalette("DARK");

        if (System.getProperty("os.name").startsWith("Windows"))
            stage.setTitle("Mable"); // only set title if on windows
        else
            stage.setTitle("");
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setWidth(PREF_WIDTH);
        stage.setHeight(PREF_HEIGHT);
        stage.setOnCloseRequest((event) -> { Platform.exit(); });

        final MainContainer root = MainContainer.getInstance();
        // root.prefWidthProperty().bind(stage.widthProperty());
        // root.prefHeightProperty().bind(stage.heightProperty());

        final Scene scene = new Scene(root);
        scene.setFill(RiceHandler.getColour("night"));
        stage.setScene(scene);
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
