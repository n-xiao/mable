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

package code.frontend.libs.katlaf.inputfields;

import code.frontend.MainContainer;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.buttons.IconButton;
import code.frontend.libs.katlaf.icons.IconHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.io.File;
import java.nio.file.Path;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

/**
 * A field with a button on its right to allow users to type in a path or browse
 * to find a path.
 *
 * @since v3.1.0
 */
public abstract class SaveFileField extends VBox {
    private static final double MIN_INPUT_WIDTH = 230;
    private static final double MIN_INPUT_HEIGHT = 29;
    private final BorderedField dirField;
    private final BorderedField fileField;
    private final BrowseButton browse;
    private final Label hint;
    private boolean fileBrowserOpen;

    public SaveFileField() {
        this.fileBrowserOpen = false;
        this.dirField = new BorderedField("LOCATION", RiceHandler.getColour("night"));
        this.dirField.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.dirField.setMinSize(MIN_INPUT_WIDTH, MIN_INPUT_HEIGHT);
        this.dirField.getTextField().setFont(FontHandler.getNormal(10));
        this.dirField.setFieldText(System.getProperty("user.home") + "/");
        HBox.setMargin(this.dirField, new Insets(0, 4, 0, 0));

        this.fileField = new BorderedField("FILE", RiceHandler.getColour("night"));
        this.fileField.setMinSize(MIN_INPUT_WIDTH, MIN_INPUT_HEIGHT);
        this.fileField.getTextField().setFont(FontHandler.getNormal(10));
        this.fileField.setFieldText(getPlaceholderFile());
        this.fileField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateFields();
            }
        });
        VBox.setMargin(this.fileField, new Insets(8, 0, 0, 0));

        this.browse = new BrowseButton();

        this.hint = new Label();
        this.hint.setFont(FontHandler.getHeavyMono(9));
        this.hint.setTextFill(RiceHandler.getColour("red"));
        this.hint.setVisible(false);
        VBox.setMargin(this.hint, new Insets(2, 0, 0, 0));

        final HBox hBox = new HBox(this.dirField, this.browse);
        hBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(hBox, this.fileField, this.hint);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * Used at first launch.
     *
     * @return String    the name and extension of a placeholder file, to be used
     *                   if no file is specified
     */
    protected abstract String getPlaceholderFile();

    protected abstract String getExpectedExtension();

    protected final BrowseButton getBrowseButton() {
        return this.browse;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void updateFields() {
        final Path path = Path.of(
            this.dirField.getTextField().getText(), this.fileField.getTextField().getText());
        final String absPath = path.toAbsolutePath().toString();
        if (!absPath.endsWith("." + getExpectedExtension()) && !absPath.contains(".")) {
            this.fileField.getTextField().setText(
                this.fileField.getTextField().getText() + "." + getExpectedExtension());
        } else if (absPath.endsWith(".")) {
            this.fileField.getTextField().setText(
                this.fileField.getTextField().getText() + getExpectedExtension());
        }
    }

    public final Path getPath() {
        final Path path = Path.of(
            this.dirField.getTextField().getText(), this.fileField.getTextField().getText());

        return path;
    }

    public final void showWarning(final String warning) {
        this.hint.setText(warning);
        this.hint.setVisible(true);
    }

    public final void hideWarning() {
        this.hint.setVisible(false);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    protected class BrowseButton extends IconButton {
        BrowseButton() {
            super(IconHandler.getIconAsImage("folder.png"), RiceHandler.getColour("lightgrey"));
            this.setCursor(Cursor.HAND);
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            if (fileBrowserOpen)
                return;

            SaveFileField.this.fileBrowserOpen = true;
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Browse");
            directoryChooser.setInitialDirectory(
                Path.of(SaveFileField.this.dirField.getTextField().getText()).toFile());

            final File file = directoryChooser.showDialog(MainContainer.getStage());

            if (file != null) {
                SaveFileField.this.dirField.setFieldText(file.getAbsolutePath());
            } else {
                SaveFileField.this.dirField.setFieldText(System.getProperty("user.home") + "/");
            }

            event.consume();
            SaveFileField.this.fileBrowserOpen = false;
        }
    }
}
