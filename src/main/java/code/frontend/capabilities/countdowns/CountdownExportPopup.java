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

package code.frontend.capabilities.countdowns;

import code.backend.data.Countdown;
import code.backend.data.CountdownHandler;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FormatHandler;
import code.frontend.libs.katlaf.buttons.FilledButton;
import code.frontend.libs.katlaf.inputfields.SaveFileField;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CountdownExportPopup extends Popup {
    private static final double WIDTH = 320;
    private static final double HEIGHT = 240;
    private final List<Countdown> countdowns;

    public CountdownExportPopup(final List<Countdown> countdowns) {
        this.countdowns = countdowns;
        super(WIDTH, HEIGHT);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected String getIdent() {
        return "Exporting Countdowns";
    }

    @Override
    protected void configureContent(StackPane content) {
        content.setOnMousePressed(event -> content.requestFocus());
        final VBox container = new VBox();

        final Label label = new Label("Choose where to export "
            + FormatHandler.intToString(this.countdowns.size()) + " Countdown(s)");
        label.setFont(FontHandler.getHeading(4));
        label.setTextFill(RiceHandler.getColour("white"));
        VBox.setMargin(label, new Insets(0, 0, 10, 0));

        final JsonFileSelectorField field = new JsonFileSelectorField();
        field.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        field.setAlignment(Pos.CENTER);

        final FilledButton accept = FilledButton.createActionButton(() -> {
            field.updateFields();

            try {
                CountdownHandler.exportCountdowns(countdowns, field.getPath());
            } catch (Exception e) {
                System.err.println(e);
                field.showWarning("Failed! Invalid location or file name.");
                return;
            }
            content.getChildren().clear();
            content.getChildren().add(
                new SuccessMessage(countdowns.size(), field.getPath().toString()));
        });
        accept.setLabel("Export");
        VBox.setMargin(accept, new Insets(15, 0, 0, 0));

        container.getChildren().addAll(label, new Group(field), accept);
        container.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        container.setAlignment(Pos.CENTER);
        content.getChildren().add(container);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class JsonFileSelectorField extends SaveFileField {
        JsonFileSelectorField() {
            this.getBrowseButton().setMaxSize(16, 16);
            this.getBrowseButton().setMinSize(16, 16);
        }
        @Override
        protected String getPlaceholderFile() {
            return "mable-countdown-export.json";
        }

        @Override
        protected String getExpectedExtension() {
            return "json";
        }
    }

    private class SuccessMessage extends StackPane {
        SuccessMessage(final int countdownsExported, final String exportPath) {
            final Label heading =
                new Label("Exported " + Integer.toString(countdownsExported) + " Countdown(s)");
            heading.setFont(FontHandler.getHeading(3));
            heading.setTextFill(RiceHandler.getColour("green"));
            heading.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            heading.setAlignment(Pos.CENTER);

            final Label path = new Label(exportPath);
            path.setFont(FontHandler.getMono(8));
            path.setTextFill(RiceHandler.getColour("grey"));
            path.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            path.setAlignment(Pos.CENTER);
            VBox.setMargin(path, new Insets(10, 0, 0, 0));

            final FilledButton dismiss = FilledButton.createActionButton(() -> Popup.despawn());
            dismiss.setLabel("Done");
            VBox.setMargin(dismiss, new Insets(15, 0, 0, 0));

            final VBox vbox = new VBox(heading, path, dismiss);
            vbox.setAlignment(Pos.CENTER);

            this.getChildren().add(vbox);
        }
    }
}
