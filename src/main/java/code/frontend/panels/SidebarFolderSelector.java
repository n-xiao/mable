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
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class SidebarFolderSelector extends VBox {
    private static final int PREF_HEIGHT = 600;
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

            instance.INCOMPLETED_FOLDER_PANE.executeOnClick(); // select the folder on startup
        }

        return instance;
    }

    private final InputField SEARCH_FIELD;
    private final ScrollPane SCROLL_PANE;
    private final VBox SCROLL_PANE_CONTENT;
    private final LinkedList<FolderPane> FOLDER_PANES;
    private final Label NEW_FOLDER_BTTN;
    private final BorderPane NEW_FOLDER_BTTN_CONTAINER;

    private final FolderPane COMPLETED_FOLDER_PANE;
    private final FolderPane INCOMPLETED_FOLDER_PANE;
    private Pane scrollPaneWrapper;

    private SidebarFolderSelector() {
        this.scrollPaneWrapper = new Pane();
        // need to make sure searchfield is not stuck on selected
        this.SEARCH_FIELD = new InputField();
        this.configureSearchFieldStyle();

        this.FOLDER_PANES = new LinkedList<FolderPane>();
        this.refreshFolderPaneData();

        this.NEW_FOLDER_BTTN = new Label();
        this.configureNewFolderButtonStyle();

        this.SCROLL_PANE = new ScrollPane();
        this.configureScrollPaneStyle();

        this.SCROLL_PANE_CONTENT = new VBox();
        this.configureScrollPaneContentStyle();

        this.NEW_FOLDER_BTTN_CONTAINER = new BorderPane();
        this.configureNewFolderButtonContainerStyle();

        this.setMinHeight(MIN_HEIGHT);
        this.setPrefHeight(PREF_HEIGHT);
        this.setFillWidth(true);
        this.setBackground(null);

        this.COMPLETED_FOLDER_PANE = new FolderPane(StorageHandler.getCompletedFolder());
        this.INCOMPLETED_FOLDER_PANE = new FolderPane(StorageHandler.getIncompletedFolder());

        this.getChildren().addAll(
            this.SEARCH_FIELD, this.scrollPaneWrapper, this.NEW_FOLDER_BTTN_CONTAINER);
        this.setPadding(new Insets(5, 2, 20, 2));
        this.repopulate();
    }

    // private Border createBorder() {
    //     Stop[] stops = {new Stop(0, Colour.GHOST), new Stop(1, Color.rgb(0, 0, 0, 0))};
    //     LinearGradient lg = new LinearGradient(0, 0, 0, 0.5, true, CycleMethod.NO_CYCLE, stops);
    //     BorderStroke stroke =
    //         new BorderStroke(lg, BorderStrokeStyle.SOLID, new CornerRadii(25), new
    //         BorderWidths(2));
    //     return new Border(stroke);
    // }

    private void configureSearchFieldStyle() {
        this.SEARCH_FIELD.getTextField().setPromptText("Search...");
        this.SEARCH_FIELD.setMaxHeight(20);
        CustomBox border = new CustomBox(2, 0.022, 0.02, 0.45);
        border.setStrokeColour(Colour.GHOST);
        this.SEARCH_FIELD.setCustomBorder(border);
        this.SEARCH_FIELD.enableManualActivation();
        this.SEARCH_FIELD.setFieldMargins(new Insets(5, 7, 5, 7));
        VBox.setMargin(this.SEARCH_FIELD, new Insets(0, 0, 5, 0));
    }

    private void configureScrollPaneStyle() {
        final CustomBox BORDER = new CustomBox(2, 0.0101, 0.01, 0.12);
        BORDER.setStrokeColour(Colour.GHOST);
        CustomBox.applyToPane(this.scrollPaneWrapper, BORDER);
        this.scrollPaneWrapper.maxWidthProperty().bind(this.widthProperty());
        this.SCROLL_PANE.setFitToWidth(true);
        this.SCROLL_PANE.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.SCROLL_PANE.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.SCROLL_PANE.setBackground(null);
        this.SCROLL_PANE.setMinHeight(200);
        this.SCROLL_PANE.prefWidthProperty().bind(this.scrollPaneWrapper.widthProperty());
        this.SCROLL_PANE.prefHeightProperty().bind(this.scrollPaneWrapper.heightProperty());
        this.SCROLL_PANE.setStyle("-fx-background: transparent;"); // YAY OMG I FOUND A FIX
        this.scrollPaneWrapper.getChildren().add(this.SCROLL_PANE);
        VBox.setVgrow(this.scrollPaneWrapper, Priority.ALWAYS);
    }

    private void configureScrollPaneContentStyle() {
        this.SCROLL_PANE_CONTENT.setBackground(Colour.createBG(Color.BLACK, 13, 8));
        this.SCROLL_PANE_CONTENT.setFillWidth(true);
        this.SCROLL_PANE_CONTENT.setPadding(new Insets(4, 5, 0, 5));
        this.SCROLL_PANE_CONTENT.minHeightProperty().bind(
            this.SCROLL_PANE.heightProperty().add(-2));
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
            this.NEW_FOLDER_BTTN_CONTAINER.setCenter(null);
            if (!this.SCROLL_PANE_CONTENT.getChildren().contains(this.NEW_FOLDER_BTTN))
                this.SCROLL_PANE_CONTENT.getChildren().add(this.NEW_FOLDER_BTTN);
        } else {
            this.SCROLL_PANE_CONTENT.getChildren().remove(this.NEW_FOLDER_BTTN);
            this.NEW_FOLDER_BTTN_CONTAINER.setCenter(this.NEW_FOLDER_BTTN);
        }
    }

    private void configureNewFolderButtonStyle() {
        this.NEW_FOLDER_BTTN.setText("+ new folder");
        this.NEW_FOLDER_BTTN.setFont(Font.font(FontTools.FONT_FAM, FontPosture.ITALIC, 13));
        this.NEW_FOLDER_BTTN.setTextFill(Color.WHITE);
        this.NEW_FOLDER_BTTN.setAlignment(Pos.CENTER);
        this.NEW_FOLDER_BTTN.setMinHeight(40);
        this.NEW_FOLDER_BTTN.maxWidthProperty().bind(this.widthProperty());
    }

    private void configureNewFolderButtonContainerStyle() {
        this.NEW_FOLDER_BTTN_CONTAINER.setBackground(null);
        this.NEW_FOLDER_BTTN_CONTAINER.maxWidthProperty().bind(this.widthProperty());
        this.NEW_FOLDER_BTTN_CONTAINER.setMinHeight(30);
    }

    private class FolderPane extends ToggleButton {
        final CountdownFolder FOLDER;

        protected FolderPane(CountdownFolder folder) {
            super(folder.getName());
            this.FOLDER = folder;
            this.setFeedbackColour(Colour.SELECTED); // todo use another colour if needed
            this.getLabel().setAlignment(Pos.CENTER_LEFT);
            this.getLabel().relocate(15, 0);
            this.getCustomBorder().setCornerOffset(0.45);
            this.getCustomBorder().setThickness(2.3);
            this.getCustomBorder().setCornerDeviation(0.017);
            this.getCustomBorder().setDeviation(0.02);
            this.setToggledColour(Colour.SELECTED);
            this.setUntoggledColour(Colour.GHOST);
            this.setMinHeight(40);
            this.getLabel().setFont(Font.font(FontTools.FONT_FAM, 13));
            VBox.setMargin(this, new Insets(10, 2.5, 0, 2.5));
        }

        @Override
        public void executeOnClick() {
            SidebarFolderSelector.getInstance().FOLDER_PANES.forEach(folder -> {
                if (!this.hasSameName(folder))
                    folder.untoggle();
            });

            if (!this.hasSameName(INCOMPLETED_FOLDER_PANE))
                INCOMPLETED_FOLDER_PANE.untoggle();

            if (!this.hasSameName(COMPLETED_FOLDER_PANE))
                COMPLETED_FOLDER_PANE.untoggle();

            this.toggle();
            StorageHandler.setCurrentlySelectedFolder(this.FOLDER);
        }

        public boolean hasSameName(FolderPane otherPane) {
            return this.getName().equals(otherPane.getName());
        }

        public String getName() {
            return this.FOLDER.getName();
        }
    }
}
