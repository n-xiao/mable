/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.panels;

import code.backend.CountdownFolder;
import code.backend.StorageHandler;
import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals.Colour;
import java.util.LinkedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SidebarFolderSelector extends VBox {
    private static final int PREF_HEIGHT = 400;
    private static final int MIN_HEIGHT = 300;

    private static SidebarFolderSelector instance = null;

    public static SidebarFolderSelector getInstance() {
        if (instance == null) {
            instance = new SidebarFolderSelector();
            instance.SEARCH_FIELD.getTextField().setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    // TODO
                }
            });
        }
        return instance;
    }

    private final InputField SEARCH_FIELD;
    private final ScrollPane SCROLL_PANE;
    private final VBox SCROLL_PANE_CONTENT;
    private final LinkedList<FolderPane> FOLDER_PANES;
    private final Label NEW_FOLDER_BTTN;

    private SidebarFolderSelector() {
        // need to make sure searchfield is not stuck on selected
        this.SEARCH_FIELD = new InputField();
        this.configureSearchFieldStyle();

        this.SCROLL_PANE = new ScrollPane();
        this.configureScrollPaneStyle();

        this.SCROLL_PANE_CONTENT = new VBox();
        this.configureScrollPaneContentStyle();

        this.FOLDER_PANES = new LinkedList<FolderPane>();
        this.initFolderPanes();

        this.NEW_FOLDER_BTTN = new Label();
        this.configureNewFolderButtonStyle();

        final CustomBox BORDER = new CustomBox(2, 0, 0, 0);
        BORDER.setStrokeColour(Colour.GHOST);
        CustomBox.applyCustomBorder(this, BORDER);
        this.setBackground(null);
        this.setMinHeight(MIN_HEIGHT);
        this.setPrefHeight(PREF_HEIGHT);
        this.setFillWidth(true);
        this.setBackground(Colour.createBG(Color.BLACK, 13, 8));
        VBox.setMargin(this, new Insets(15)); // TODO set 15 to a sidebar-global constant
    }

    private void configureSearchFieldStyle() {
        this.SEARCH_FIELD.getTextField().selectEnd();
        this.SEARCH_FIELD.getTextField().cancelEdit();
    }

    private void configureScrollPaneStyle() {
        this.SCROLL_PANE.setFitToWidth(true);
        this.SCROLL_PANE.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.SCROLL_PANE.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.SCROLL_PANE.setBackground(null);
        this.SCROLL_PANE.setContent(SCROLL_PANE_CONTENT);
        VBox.setVgrow(SCROLL_PANE, Priority.ALWAYS);
    }

    private void configureScrollPaneContentStyle() {
        this.SCROLL_PANE_CONTENT.setBackground(null);
    }

    private void initFolderPanes() {
        // TODO
    }

    private void configureNewFolderButtonStyle() {
        this.NEW_FOLDER_BTTN.setText("+ New folder");
    }

    private class FolderPane extends ToggleButton {
        final CountdownFolder FOLDER;

        protected FolderPane(CountdownFolder folder) {
            super(folder.getName());
            this.FOLDER = folder;
            this.setFeedbackColour(Colour.BTTN_CREATE); // todo use another colour if needed
            this.getLabel().setAlignment(Pos.CENTER_LEFT);
        }

        @Override
        public void executeOnClick() {
            SidebarFolderSelector.getInstance().FOLDER_PANES.forEach(folder -> {
                if (!folder.getName().equals(this.getName()))
                    folder.setToggled(false);
            });
            StorageHandler.setCurrentlySelectedFolder(this.FOLDER);
        }

        public String getName() {
            return this.FOLDER.getName();
        }
    }
}
