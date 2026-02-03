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

package code.frontend.capabilities.countdown.components;

import code.backend.utils.FolderHandler;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.inputfields.InputField;
import code.frontend.libs.katlaf.inputfields.SearchField;
import code.frontend.libs.katlaf.lists.Listable;
import code.frontend.libs.katlaf.lists.SearchableList;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    private Sidebar() {
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("background2"), 0, 0));
        this.setFillWidth(true);
    }

    /*


     SINGLETON INSTANTIATOR
    -------------------------------------------------------------------------------------*/
    private static Sidebar instance = null;

    public static Sidebar getInstance() {
        if (instance == null) {
            instance = new Sidebar();

            final SectionHeading folderHeading = instance.new SectionHeading("Your Folders");
            final FolderList folderList = instance.new FolderList();
            final FolderSearchField folderSearchField = instance.new FolderSearchField(folderList);
            instance.getChildren().addAll(folderHeading, folderSearchField, folderList);

            VBox.setMargin(folderHeading, new Insets(20, 0, 0, 0));
            VBox.setMargin(folderList, new Insets(0, 10, 5, 10));

            instance.setOnMousePressed((event) -> { // implements click-empty-space-to-deselect
                InputField.escapeAllInputs();
                CountdownTable.getInstance().deselectAll();
                instance.requestFocus();
            });
        }

        return instance;
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class FolderSearchField extends SearchField {
        FolderSearchField(FolderList folderList) {
            super(folderList);
            this.setMaxWidth(Double.MAX_VALUE);
            this.setMinHeight(10);
        }
    }

    private class FolderList extends SearchableList {
        private static final int PREF_HEIGHT = 600;
        private static final int MIN_HEIGHT = 300;

        FolderList() {
            super(new ArrayList<Listable>(FolderHandler.getFolders()));
            this.setHideBadMatches(true);
            this.setPrefHeight(PREF_HEIGHT);
            this.setMinHeight(MIN_HEIGHT);
        }
    }

    private class SectionHeading extends HBox {
        private final static int MIN_HEIGHT = 20;

        SectionHeading(String text) {
            this.setMinHeight(MIN_HEIGHT);
            this.maxWidthProperty().bind(instance.widthProperty());
            this.setBackground(null);
            this.setFillHeight(true);

            Label label = new Label(text);
            label.setTextFill(RiceHandler.getColour("txtGhost2"));
            label.setFont(FontHandler.getHeading(3));
            label.maxHeightProperty().bind(this.heightProperty());
            label.setAlignment(Pos.CENTER);
            HBox.setMargin(label, new Insets(0, 5, 0, 5));

            Pane leftLine = new HorizontalLine();
            leftLine.setMinHeight(MIN_HEIGHT);
            leftLine.setMaxWidth(16);
            leftLine.setMinWidth(16);

            Pane rightLine = new HorizontalLine();
            rightLine.setMinHeight(MIN_HEIGHT);
            rightLine.maxWidthProperty().bind(this.widthProperty());
            rightLine.setVisible(false); // one day, a user setting can make this true :D

            this.getChildren().addAll(leftLine, label, rightLine);
        }
    }

    private class HorizontalLine extends Pane {
        HorizontalLine() {
            this.setBackground(null);
            CustomLine line = new CustomLine(3, Type.HORIZONTAL_TYPE);
            line.setStrokeColour(RiceHandler.getColour("txtGhost2"));
            CustomLine.applyToPane(this, line);
            HBox.setHgrow(this, Priority.ALWAYS);
            this.setOpacity(0.5);
        }
    }
}
