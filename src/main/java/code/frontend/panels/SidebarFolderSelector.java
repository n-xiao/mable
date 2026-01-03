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
import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.gui.MainContainer;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class SidebarFolderSelector extends VBox {
    private static final int PREF_HEIGHT = 400;
    private static final int MIN_HEIGHT = 300;

    private static SidebarFolderSelector instance = null;

    public static SidebarFolderSelector getInstance() {
        if (instance == null) {
            instance = new SidebarFolderSelector();
            // defines search bar function
            instance.SEARCH_FIELD.getTextField().setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    String search = instance.SEARCH_FIELD.getTextField().getText();
                    instance.repopulate(search);
                }
            });
            instance.SCROLL_PANE.heightProperty().addListener(
                (event) -> { instance.moveNewFolderButton(); });
            instance.NEW_FOLDER_BTTN.setOnMouseEntered(
                event -> { instance.NEW_FOLDER_BTTN.setTextFill(Colour.BTTN_CREATE); });
            instance.NEW_FOLDER_BTTN.setOnMouseExited(
                event -> { instance.NEW_FOLDER_BTTN.setTextFill(Colour.TXT_GHOST); });
            instance.NEW_FOLDER_BTTN.setOnMouseClicked(event -> { System.out.println("hello"); });
        }

        return instance;
    }

    private final InputField SEARCH_FIELD;
    private final ScrollPane SCROLL_PANE;
    private final VBox SCROLL_PANE_CONTENT;
    private final LinkedList<FolderPane> FOLDER_PANES;
    private final Label NEW_FOLDER_BTTN;

    private final FolderPane COMPLETED_FOLDER_PANE;
    private final FolderPane INCOMPLETED_FOLDER_PANE;
    private Group scrollPaneWrapper;

    private SidebarFolderSelector() {
        // need to make sure searchfield is not stuck on selected
        this.SEARCH_FIELD = new InputField();
        this.configureSearchFieldStyle();

        this.SCROLL_PANE = new ScrollPane();
        this.configureScrollPaneStyle();

        this.SCROLL_PANE_CONTENT = new VBox();
        this.configureScrollPaneContentStyle();

        this.FOLDER_PANES = new LinkedList<FolderPane>();
        this.refreshFolderPaneData();

        this.NEW_FOLDER_BTTN = new Label();
        this.configureNewFolderButtonStyle();

        this.setMinHeight(MIN_HEIGHT);
        this.setPrefHeight(PREF_HEIGHT);
        this.setFillWidth(true);
        this.setBackground(Colour.createBG(Color.BLACK, 13, 8));
        VBox.setMargin(this, new Insets(15)); // TODO set 15 to a sidebar-global constant

        this.COMPLETED_FOLDER_PANE = new FolderPane(StorageHandler.getCompletedFolder());
        this.INCOMPLETED_FOLDER_PANE = new FolderPane(StorageHandler.getIncompletedFolder());

        this.getChildren().addAll(this.SEARCH_FIELD, this.scrollPaneWrapper);
        this.repopulate();
    }

    private void configureSearchFieldStyle() {
        this.SEARCH_FIELD.getTextField().selectEnd();
        this.SEARCH_FIELD.getTextField().cancelEdit();
        this.SEARCH_FIELD.getTextField().setPromptText("Search...");
    }

    private void configureScrollPaneStyle() {
        final CustomBox BORDER = new CustomBox(2, 0, 0, 0.2);
        BORDER.setStrokeColour(Color.WHITE);
        this.scrollPaneWrapper = CustomBox.applyToControl(this.SCROLL_PANE, BORDER);
        this.SCROLL_PANE.setFitToWidth(true);
        this.SCROLL_PANE.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.SCROLL_PANE.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.SCROLL_PANE.setBackground(null);
        this.SCROLL_PANE.setMinHeight(200);
        this.SCROLL_PANE.prefWidthProperty().bind(this.widthProperty());
        this.SCROLL_PANE.setStyle("-fx-background: none;"); // YAY OMG I FOUND A FIX STUPUD THING
        VBox.setVgrow(this.scrollPaneWrapper, Priority.ALWAYS);
    }

    private void configureScrollPaneContentStyle() {
        this.SCROLL_PANE_CONTENT.setBackground(Colour.createBG(Color.BLACK, 13, 8));
        this.SCROLL_PANE_CONTENT.setFillWidth(true);
        this.SCROLL_PANE_CONTENT.minHeightProperty().bind(this.heightProperty().add(-2));
        this.SCROLL_PANE.setContent(SCROLL_PANE_CONTENT); // dont move this; don't even think
    }

    /**
     * This only initialises the collection that holds FolderPanes based on backend data.
     * It does not update the GUI in any way. Call repopulate() after calling
     * this method to trigger a GUI update with new data.
     */
    private void refreshFolderPaneData() {
        this.FOLDER_PANES.clear();
        TreeSet<CountdownFolder> folders = StorageHandler.getFolders();
        for (CountdownFolder countdownFolder : folders) {
            FolderPane pane = new FolderPane(countdownFolder);
            this.FOLDER_PANES.add(pane);
            VBox.setMargin(pane, new Insets(5));
        }
    }

    /**
     * Repopulates the list of folders displayed to the user in the order
     * of their search.
     */
    public void repopulate(String search) {
        this.FOLDER_PANES.sort(new Comparator<FolderPane>() {
            @Override
            public int compare(FolderPane o1, FolderPane o2) {
                return o1.getName().compareTo(search) - o2.getName().compareTo(search);
            }
        });
        repopulate();
    }

    public void repopulate() {
        this.SCROLL_PANE_CONTENT.getChildren().clear();
        this.FOLDER_PANES.forEach(this.SCROLL_PANE_CONTENT.getChildren()::add);
        // creates a visual divider
        if (!FOLDER_PANES.isEmpty()) {
            final Pane DIVIDER = new Pane();
            final CustomLine LINE = new CustomLine(2, Type.HORIZONTAL_TYPE);
            LINE.setPadding(10);
            CustomLine.applyToPane(DIVIDER, LINE);
            DIVIDER.maxWidthProperty().bind(SCROLL_PANE_CONTENT.widthProperty());
            DIVIDER.setPrefHeight(10);
            this.SCROLL_PANE_CONTENT.getChildren().add(DIVIDER);
        }
        // then adds protected folders
        this.SCROLL_PANE_CONTENT.getChildren().addAll(
            this.INCOMPLETED_FOLDER_PANE, this.COMPLETED_FOLDER_PANE);
        moveNewFolderButton();
    }

    private void moveNewFolderButton() {
        double viewport = this.SCROLL_PANE.getViewportBounds().getHeight();
        double viewable = this.SCROLL_PANE_CONTENT.getHeight();
        if (viewport >= viewable) {
            this.getChildren().remove(this.NEW_FOLDER_BTTN);
            if (!this.SCROLL_PANE_CONTENT.getChildren().contains(this.NEW_FOLDER_BTTN))
                this.SCROLL_PANE_CONTENT.getChildren().add(this.NEW_FOLDER_BTTN);
        } else {
            this.SCROLL_PANE_CONTENT.getChildren().remove(this.NEW_FOLDER_BTTN);
            if (!this.getChildren().contains(this.NEW_FOLDER_BTTN))
                this.getChildren().add(this.NEW_FOLDER_BTTN);
        }
    }

    private void configureNewFolderButtonStyle() {
        this.NEW_FOLDER_BTTN.setText("+ New folder");
        this.NEW_FOLDER_BTTN.setFont(Font.font(FontTools.FONT_FAM, FontPosture.ITALIC, 13));
        this.NEW_FOLDER_BTTN.setTextFill(Color.WHITE);
        this.NEW_FOLDER_BTTN.setAlignment(Pos.CENTER);
        this.NEW_FOLDER_BTTN.setMinHeight(20);
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
