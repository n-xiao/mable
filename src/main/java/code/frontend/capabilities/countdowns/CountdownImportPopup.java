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

import code.backend.data.CountdownHandler;
import code.backend.data.PortableCountdown;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FormatHandler;
import code.frontend.libs.katlaf.buttons.FilledButton;
import code.frontend.libs.katlaf.dragndrop.FileImporter;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser.ExtensionFilter;

public final class CountdownImportPopup extends Popup {
    private static final double WIDTH = 300;
    private static final double HEIGHT = 300;

    private final StackPane container;
    private final CountdownList list;
    private JsonFileDrop drop;

    public CountdownImportPopup(final CountdownList list) {
        this.container = new StackPane();
        this.list = list;
        super(WIDTH, HEIGHT);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void previewFiles(final List<File> files) {
        if (files == null || files.isEmpty())
            return;

        final ArrayList<PortableCountdown> portables = new ArrayList<PortableCountdown>();
        for (File file : files) portables.addAll(CountdownHandler.extractPortables(file));

        final FilledButton accept =
            new FilledButton(RiceHandler.getColour("white"), RiceHandler.getColour("white2")) {
                @Override
                public void onMousePressed(MouseEvent event) {
                    for (PortableCountdown portable : portables)
                        list.addMember(new CountdownListMember(portable.create(), list));

                    drop.clearFiles();
                    Popup.despawn();
                }
            };
        accept.setLabel("Import");
        accept.setLabelColour(RiceHandler.getColour("black"));
        accept.setMinSize(75, 25);
        accept.setMaxSize(75, 25);

        final Label previewLabel =
            new Label("Importing " + FormatHandler.intToString(portables.size()) + " Countdown(s)");
        previewLabel.setTextFill(RiceHandler.getColour("white"));
        previewLabel.setFont(FontHandler.getHeading(4));
        previewLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        previewLabel.setAlignment(Pos.CENTER);

        final BorderPane columnLabels = new BorderPane();
        VBox.setMargin(columnLabels, new Insets(5, 0, 0, 0));
        final Label nameLabel = new Label("NAME");
        nameLabel.setFont(FontHandler.getHeavyMono(11));
        nameLabel.setTextFill(RiceHandler.getColour("white"));
        columnLabels.setLeft(nameLabel);
        BorderPane.setMargin(nameLabel, new Insets(3, 0, 0, 10));

        final Label dueLabel = new Label("DUE");
        dueLabel.setFont(FontHandler.getHeavyMono(11));
        dueLabel.setTextFill(RiceHandler.getColour("white"));
        columnLabels.setRight(dueLabel);
        columnLabels.setBackground(RiceHandler.createBG(RiceHandler.getColour("darkgrey"), 0, 0));
        BorderPane.setMargin(dueLabel, new Insets(3, 55, 0, 0));

        final VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(previewLabel, new Insets(0, 0, 6, 0));
        VBox.setMargin(accept, new Insets(15, 0, 0, 0));
        vbox.getChildren().addAll(
            previewLabel, columnLabels, new CountdownPreviewList(portables), accept);

        this.container.getChildren().forEach(child -> child.setVisible(false));
        this.container.getChildren().clear();
        this.container.getChildren().addAll(vbox);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected String getIdent() {
        return "Import Countdowns";
    }

    @Override
    protected void configureContent(StackPane content) {
        this.drop = new JsonFileDrop();
        StackPane.setMargin(this.drop, new Insets(30, 5, 5, 5));
        this.container.getChildren().addAll(this.drop);
        content.getChildren().addAll(this.container);
        content.setPadding(new Insets(5));
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class JsonFileDrop extends FileImporter {
        @Override
        protected void onFilesReceived() {
            previewFiles(this.getFiles());
        }

        @Override
        protected String[] getAcceptedFileExtensions() {
            return new String[] {"json"};
        }

        @Override
        protected ExtensionFilter[] getExtensionFilters() {
            return new ExtensionFilter[] {new ExtensionFilter("JSON", "*.json")};
        }
    }

    private class CountdownPreviewList extends BorderPane {
        CountdownPreviewList(List<PortableCountdown> portables) {
            final VBox vbox = new VBox();
            vbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            portables.forEach(portable -> {
                final CountdownPreview preview =
                    new CountdownPreview(portable.getName(), portable.getDisplayDue());
                VBox.setMargin(preview, new Insets(0, 0, 2, 0));
                vbox.getChildren().add(preview);
            });

            final ScrollPane scrollPane = new ScrollPane();
            scrollPane.setStyle("-fx-background: transparent;");
            scrollPane.setBackground(null);
            scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
            scrollPane.setFitToWidth(true);
            scrollPane.setContent(vbox);

            this.setCenter(scrollPane);
        }
    }

    private class CountdownPreview extends BorderPane {
        CountdownPreview(final String name, final String date) {
            final Label nameLabel = createLabel(name);
            final Label dateLabel = createLabel(date);
            this.setPadding(new Insets(1, 8, 1, 8));
            this.setLeft(nameLabel);
            this.setRight(dateLabel);
            this.setBackground(RiceHandler.createBG(RiceHandler.getColour("black"), 0, 0));
            this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }

        final static Label createLabel(final String text) {
            final Label label = new Label(text);
            label.setTextFill(RiceHandler.getColour("white2"));
            label.setFont(FontHandler.getMono(11));
            return label;
        }
    }
}
