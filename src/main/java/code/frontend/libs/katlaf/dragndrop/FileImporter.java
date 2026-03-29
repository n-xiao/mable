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

package code.frontend.libs.katlaf.dragndrop;

import code.frontend.MainContainer;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.interfaces.Colourable;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public abstract class FileImporter extends StackPane implements Colourable {
    private Border border;
    private final Label heading;
    private final Label body;
    private final ArrayList<File> files;
    private boolean fileBrowserOpen;

    public FileImporter() {
        this.fileBrowserOpen = false;
        this.files = new ArrayList<File>();
        this.heading = new Label("Drag and drop files here");
        this.heading.setFont(FontHandler.getHeading(3));
        this.heading.setAlignment(Pos.CENTER);
        this.body = new Label("\nAccepted file type(s): " + getAcceptedFileExtensionsString()
            + "\nYou can drop multiple files at once.\nOr, you can click here to choose files.");
        this.body.setFont(FontHandler.getNormal());
        this.body.setAlignment(Pos.CENTER);
        this.body.setTextAlignment(TextAlignment.CENTER);
        this.body.setCursor(Cursor.HAND);
        resetColour();

        final VBox vbox = new VBox(this.heading, this.body);
        vbox.setAlignment(Pos.CENTER);
        StackPane.setAlignment(vbox, Pos.CENTER);

        this.setOnDragOver(event -> {
            if (!DragDropOverlay.isActive() && event.getGestureSource() != this
                && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
            setColour("orange");
        });

        this.setOnDragDropped(event -> {
            final Dragboard dragboard = event.getDragboard();

            if (!dragboard.hasFiles())
                return;

            final List<File> incomingFiles = dragboard.getFiles();
            if (incomingFiles == null || incomingFiles.isEmpty())
                return;

            if (hasValidExtensions(incomingFiles)) {
                this.files.addAll(incomingFiles);
                onFilesReceived();
                resetColour();
            } else {
                setColour("red");
            }

            event.consume();
        });

        this.setOnMouseClicked(event -> {
            if (this.fileBrowserOpen)
                return;

            this.fileBrowserOpen = true;

            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Files");
            fileChooser.getExtensionFilters().addAll(this.getExtensionFilters());
            final List<File> incomingFiles =
                fileChooser.showOpenMultipleDialog(MainContainer.getStage());

            this.fileBrowserOpen = false;

            if (incomingFiles == null || incomingFiles.isEmpty())
                return;
            this.files.addAll(incomingFiles);
            onFilesReceived();

            event.consume();
        });

        this.getChildren().add(vbox);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private boolean hasValidExtensions(final List<File> files) {
        final String[] extensions = getAcceptedFileExtensions();
        for (File file : files) {
            final String fileName = file.getName();
            if (fileName == null || fileName.isBlank())
                return false;

            final int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex <= 0)
                return false;

            final String ext = fileName.substring(dotIndex + 1);
            for (String extension : extensions) {
                if (ext.equals(extension))
                    return true;
            }
        }

        return false;
    }

    private String getAcceptedFileExtensionsString() {
        final String[] extensions = getAcceptedFileExtensions();

        String result = "";
        for (int i = 0; i < extensions.length; i++)
            result += i < extensions.length - 1 ? extensions[i] + ", " : extensions[i];

        return result;
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * This method should return an array of Strings which contain
     * the accepted file extensions for this FileDropArea.
     *
     * @return String[]         an array of Strings
     */
    protected abstract String[] getAcceptedFileExtensions();

    protected abstract ExtensionFilter[] getExtensionFilters();

    protected abstract void onFilesReceived();

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Sets the files field to null. Should be called when all operations related
     * to the received files have been completed, so that references to them will
     * be cleared appropriately.
     */
    public final void clearFiles() {
        this.files.clear();
    }

    @Override
    public void setColour(Color colour) {
        this.border = new Border(new BorderStroke(
            colour, BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(2)));
        this.setBorder(this.border);
    }

    @Override
    public void resetColour() {
        this.border = new Border(new BorderStroke(RiceHandler.getColour("white2"),
            BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(2)));
        this.setBorder(this.border);
        this.heading.setTextFill(RiceHandler.getColour("white"));
        this.body.setTextFill(RiceHandler.getColour("lightgrey"));
    }

    /**
     * Gets the files received by this instance. Note that it is the implementing class's
     * responsibility to reset the files field to null at an appropriate time to prevent
     * old files from being referenced.
     *
     * @return List<File>       the List of received files
     */
    public final List<File> getFiles() {
        return this.files;
    }
}
