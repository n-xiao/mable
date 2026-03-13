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

package code.frontend.capabilities.views;

import code.backend.settings.SettingsHandler;
import code.backend.settings.SettingsHandler.Key;
import code.frontend.libs.katlaf.options.BooleanOption;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public final class SettingsView extends VBox {
    public SettingsView() {
        this.setPadding(new Insets(10, 0, 10, 0));
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.setVisible(false);
        this.setBackground(null);
        final Title title = new Title("Settings");
        SettingsView.setMargin(title, new Insets(0, 7, 10, 7));
        this.getChildren().add(title);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * This method is used to set up a given SettingsView instance for Mable's specific
     * usage. This is because I did not want to clutter the MainContainer lololol
     *
     * I know the code is kinda ugly but i'm rushing a little... will clean it up when
     * I introduce options other than boolean ones (so I know how to design the program
     * properly)
     */
    public static SettingsView setup() {
        final String restartWarning = "Restart the application for changes to take effect.";

        /*
         * light mode option
         */
        final boolean lightModeEnabled = SettingsHandler.getBooleanValue(Key.LIGHT_MODE);
        final BooleanOption lightModeOption = new BooleanOption("Use light mode");
        if (lightModeEnabled) {
            lightModeOption.getToggle().toggle();
        }
        lightModeOption.getSubtitle().setText("Switches the current theme to light mode.");
        lightModeOption.getWarning().setText(restartWarning);
        lightModeOption.getToggle().setRunOnceOnline(() -> {
            if (!lightModeEnabled)
                lightModeOption.showWarning();
            else
                lightModeOption.hideWarning();
            SettingsHandler.setBooleanValue(Key.LIGHT_MODE, true);
        });
        lightModeOption.getToggle().setRunOnceOffline(() -> {
            if (lightModeEnabled)
                lightModeOption.showWarning();
            else
                lightModeOption.hideWarning();
            SettingsHandler.setBooleanValue(Key.LIGHT_MODE, false);
        });

        /*
         * alt date option
         */
        final boolean altDateEnabled = SettingsHandler.getBooleanValue(Key.ALT_DATE);
        final BooleanOption altDateOption = new BooleanOption("Use alternate date format");
        if (altDateEnabled) {
            altDateOption.getToggle().toggle();
        }
        altDateOption.getSubtitle().setText("When enabled, dates will be formatted in the order: "
            + "\nmonth, day, year instead of: day, month, year.");
        altDateOption.getToggle().setRunOnceOnline(
            () -> { SettingsHandler.setBooleanValue(Key.ALT_DATE, true); });
        altDateOption.getToggle().setRunOnceOffline(
            () -> { SettingsHandler.setBooleanValue(Key.ALT_DATE, false); });

        return new SettingsView().addBooleanOption(lightModeOption).addBooleanOption(altDateOption);
    }

    /**
     * Adds a new BooleanOption to this instance.
     *
     * @param  booleanOption        the BooleanOption instance to add
     * @return SettingsView         this instance to support method chaining
     * @see BooleanOption
     */
    public SettingsView addBooleanOption(final BooleanOption booleanOption) {
        booleanOption.setBackground(RiceHandler.createBG(RiceHandler.getColour("black"), 8, 0));
        SettingsView.setMargin(booleanOption, new Insets(6, 10, 6, 10));
        this.getChildren().add(booleanOption);
        return this;
    }
}
