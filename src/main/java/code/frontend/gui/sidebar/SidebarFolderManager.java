/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.gui.sidebar;

import code.backend.data.CountdownFolder;
import code.backend.utils.FolderHandler;
import code.frontend.foundation.custom.CustomBox;
import code.frontend.foundation.custom.CustomLine;
import code.frontend.foundation.custom.CustomLine.Type;
import code.frontend.foundation.panels.buttons.ToggleButton;
import code.frontend.foundation.panels.inputs.InputField;
import code.frontend.gui.dragndrop.DragHandler;
import code.frontend.gui.pages.home.CountdownPaneView;
import code.frontend.gui.rightclickmenu.FolderManagerRCM;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.util.Duration;

public class SidebarFolderManager extends VBox {
    private static final int PREF_HEIGHT = 600;
    private static final int MIN_HEIGHT = 300;

    private static FolderPane selectedFolderPane;
    private static SidebarFolderManager instance = null;

    public static SidebarFolderManager getInstance() {
        if (instance == null) {
            instance = new SidebarFolderManager();
            // defines search bar function
            instance.SEARCH_FIELD.getTextField().setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    String search = instance.SEARCH_FIELD.getTextField().getText();
                    instance.repopulate(search);
                }
            });
            instance.heightProperty().addListener((event) -> { instance.moveNewFolderButton(); });
            instance.NEW_FOLDER_BTTN.setOnMouseEntered(
                event -> { instance.NEW_FOLDER_BTTN.setTextFill(Colour.BTTN_CREATE); });
            instance.NEW_FOLDER_BTTN.setOnMouseExited(
                event -> { instance.NEW_FOLDER_BTTN.setTextFill(Colour.TXT_GHOST); });
            instance.NEW_FOLDER_BTTN.setOnMouseClicked(
                event -> { instance.triggerNewFolderInput(""); });

            instance.INCOMPLETED_FOLDER_PANE.executeOnClick(null); // select the folder on startup
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
    public final FolderPane INCOMPLETED_FOLDER_PANE;
    private Pane scrollPaneWrapper;
    private VBox nameFieldContainer;

    private SidebarFolderManager() {
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

        this.COMPLETED_FOLDER_PANE = new FolderPane(FolderHandler.getCompletedFolder());
        this.INCOMPLETED_FOLDER_PANE = new FolderPane(FolderHandler.getIncompletedFolder());

        this.getChildren().addAll(
            this.SEARCH_FIELD, this.scrollPaneWrapper, this.NEW_FOLDER_BTTN_CONTAINER);
        this.setPadding(new Insets(5, 2, 20, 2));
        this.repopulate();
    }

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
        this.SCROLL_PANE.prefHeightProperty().bind(
            this.scrollPaneWrapper.heightProperty().add(-20));
        this.SCROLL_PANE.relocate(0, 10);
        this.SCROLL_PANE.setStyle("-fx-background: transparent;"); // YAY OMG I FOUND A FIX
        this.scrollPaneWrapper.getChildren().add(this.SCROLL_PANE);
        VBox.setVgrow(this.scrollPaneWrapper, Priority.ALWAYS);
    }

    private void configureScrollPaneContentStyle() {
        this.SCROLL_PANE_CONTENT.setBackground(Colour.createBG(Color.BLACK, 13, 8));
        this.SCROLL_PANE_CONTENT.setFillWidth(true);
        this.SCROLL_PANE_CONTENT.setPadding(new Insets(4, 5, 0, 5));
        this.SCROLL_PANE_CONTENT.setMaxHeight(Double.MAX_VALUE);
        this.SCROLL_PANE.setContent(SCROLL_PANE_CONTENT); // dont move this; don't even think
    }

    public void deleteSelectedFolder() {
        int index = this.FOLDER_PANES.indexOf(selectedFolderPane);
        if (index == -1)
            return;

        FolderHandler.removeSelectedFolder();

        this.refreshFolderPaneData();
        this.repopulate();
        this.INCOMPLETED_FOLDER_PANE.executeOnClick(null);
    }

    public void editSelectedFolder() {
        this.SCROLL_PANE_CONTENT.getChildren().clear();
        for (FolderPane folderPane : FOLDER_PANES) {
            if (folderPane.hasSameName(selectedFolderPane)) {
                this.triggerNewFolderInput(folderPane.getName());
            } else {
                this.SCROLL_PANE_CONTENT.getChildren().add(folderPane);
            }
        }
    }

    /**
     * This only initialises the collection that holds FolderPanes based on backend data.
     * It does not update the GUI in any way. Call repopulate() after calling
     * this method to trigger a GUI update with new data.
     */
    public void refreshFolderPaneData() {
        this.FOLDER_PANES.clear();
        TreeSet<CountdownFolder> folders = FolderHandler.getFolders();
        for (CountdownFolder countdownFolder : folders) {
            FolderPane pane = new FolderPane(countdownFolder);
            this.FOLDER_PANES.add(pane);
            if (countdownFolder.equals(FolderHandler.getCurrentlySelectedFolder()))
                pane.toggle();
        }
    }

    /**
     * Repopulates the list of folders displayed to the user in the order
     * of their search.
     */
    public void repopulate(String search) {
        if (search.isEmpty()) {
            this.refreshFolderPaneData();
            this.repopulate();
            return;
        }
        this.FOLDER_PANES.sort(new Comparator<FolderPane>() {
            @Override
            public int compare(FolderPane o1, FolderPane o2) {
                return getLevenshteinDistance(o1.getName(), search)
                    - getLevenshteinDistance(o2.getName(), search);
            }
            /**
             * Edit distance algorithm which measures how closely two strings match. The smaller the
             * distance, the closer the match of the two provided strings. Method has a bias, where
             * having to append characters for a match of the two strings is penalised less than
             * replacement or removal since this method is often called when the user is in the
             * process of typing something.
             *
             * @param input first <code>String</code>
             * @param target second <code>String</code>
             * @return <b>int</b> the levenshtein distance distance between the two strings provided
             *     in the parameters
             */
            private int getLevenshteinDistance(String input, String target) {
                int[][] dp = new int[input.length() + 1][target.length() + 1];

                for (int i = 0; i < dp.length; i++) // scenario where second str is empty
                    dp[i][0] = i;

                for (int i = 0; i < dp[0].length; i++) // scenario where first str is empty
                    dp[0][i] = i;

                for (int i = 1; i < dp.length; i++) {
                    for (int j = 1; j < dp[i].length; j++) {
                        int append = dp[i][j - 1] + 1; // append to input
                        int remove = dp[i - 1][j] + 2; // remove from input (double penalty)
                        int replace = dp[i - 1][j - 1]; // assume no replacement

                        if (input.charAt(i - 1) != target.charAt(j - 1)) // if chars at pos i dont
                                                                         // match
                            replace += 2; // add replacement penalty (double penalty)

                        dp[i][j] = Math.min(append,
                            Math.min(remove,
                                replace)); // find min of append, remove and replace ops
                    }
                }

                return dp[input.length()][target.length()]; // returns the last element of the 2d
                                                            // array (bottom right element)
            }
        });
        repopulate();
    }

    public void repopulate() {
        this.SCROLL_PANE_CONTENT.getChildren().clear();
        this.FOLDER_PANES.forEach(this.SCROLL_PANE_CONTENT.getChildren()::add);

        if (!FOLDER_PANES.isEmpty()) { // creates a visual divider
            final Pane DIVIDER = new Pane();
            final CustomLine LINE = new CustomLine(2, Type.HORIZONTAL_TYPE);

            LINE.setPadding(10);
            CustomLine.applyToPane(DIVIDER, LINE);
            DIVIDER.maxWidthProperty().bind(SCROLL_PANE_CONTENT.widthProperty());
            DIVIDER.setOpacity(0.4);
            DIVIDER.setPrefHeight(10);
            VBox.setMargin(DIVIDER, new Insets(5, 0, 5, 0));
            this.SCROLL_PANE_CONTENT.getChildren().add(DIVIDER);
        }

        // then adds protected folders
        this.SCROLL_PANE_CONTENT.getChildren().addAll(
            this.INCOMPLETED_FOLDER_PANE, this.COMPLETED_FOLDER_PANE);
        moveNewFolderButton();
    }

    private void moveNewFolderButton() {
        if (this.SCROLL_PANE_CONTENT.getChildren().contains(this.nameFieldContainer))
            return; // do not execute if name field container is on-screen
        double viewport = this.SCROLL_PANE.getViewportBounds().getHeight();
        double viewable = this.SCROLL_PANE_CONTENT.getBoundsInParent().getHeight();
        double buttonHeight = this.NEW_FOLDER_BTTN.getBoundsInParent().getHeight();
        this.SCROLL_PANE_CONTENT.getChildren().remove(this.NEW_FOLDER_BTTN);
        this.NEW_FOLDER_BTTN_CONTAINER.setCenter(null);
        if (viewport > viewable + buttonHeight) {
            this.SCROLL_PANE_CONTENT.getChildren().add(this.NEW_FOLDER_BTTN);
        } else {
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
        this.NEW_FOLDER_BTTN.setCursor(Cursor.HAND);
    }

    private void configureNewFolderButtonContainerStyle() {
        this.NEW_FOLDER_BTTN_CONTAINER.setBackground(null);
        this.NEW_FOLDER_BTTN_CONTAINER.maxWidthProperty().bind(this.widthProperty());
        this.NEW_FOLDER_BTTN_CONTAINER.setMinHeight(30);
    }

    private void triggerNewFolderInput(String nameToEdit) {
        // prevent user from clicking the add button (again)
        this.SCROLL_PANE_CONTENT.getChildren().remove(this.NEW_FOLDER_BTTN);
        this.NEW_FOLDER_BTTN_CONTAINER.setCenter(null);

        final String ERR_EXISTS = "no duplicate names!";
        final String ERR_BLANK = "that's... not a name";
        this.nameFieldContainer = new VBox();
        this.nameFieldContainer.setFillWidth(true);
        this.nameFieldContainer.setBackground(null);
        this.nameFieldContainer.setFillWidth(true);
        this.nameFieldContainer.setPadding(new Insets(0, 0, 10, 0)); // adds space at the bottom
                                                                     // so the container can be seen
                                                                     // when scroll pane is scrolled
                                                                     // all the way down
        final Label TOP_HINT = new Label("folder name: ");
        TOP_HINT.maxWidthProperty().bind(this.nameFieldContainer.widthProperty());
        TOP_HINT.minHeight(20);
        TOP_HINT.setAlignment(Pos.CENTER_LEFT);
        TOP_HINT.setFont(Font.font(FontTools.FONT_FAM, FontPosture.ITALIC, 13));
        TOP_HINT.setTextFill(Colour.SELECTED);

        final Label HINT = new Label();
        HINT.maxWidthProperty().bind(this.nameFieldContainer.widthProperty());
        HINT.minHeight(20);
        HINT.setAlignment(Pos.CENTER);
        HINT.setFont(Font.font(FontTools.FONT_FAM, FontPosture.ITALIC, 13));
        HINT.setTextFill(Colour.ERROR);
        HINT.setOpacity(0);

        final FadeTransition TRANSITION = new FadeTransition();

        VBox.setMargin(this.nameFieldContainer, new Insets(10, 2.5, 0, 2.5));
        final InputField NAME_INPUT = new InputField();
        NAME_INPUT.getTextField().setText(nameToEdit);
        NAME_INPUT.getCustomBorder().setCornerOffset(0.34);
        NAME_INPUT.getCustomBorder().setDeviation(0.02);
        NAME_INPUT.getCustomBorder().setCornerDeviation(0.02);
        NAME_INPUT.setFieldMargins(new Insets(3));

        NAME_INPUT.getTextField().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String input = NAME_INPUT.getTextField().getText();

                if (input.isBlank()) {
                    HINT.setText(ERR_BLANK);
                    playHintTransition();
                    return;
                }

                if (FolderHandler.renameFolder(nameToEdit, input)
                    || FolderHandler.createFolder(input)) {
                    instance.refreshFolderPaneData();
                    instance.repopulate();
                } else {
                    HINT.setText(ERR_EXISTS);
                    playHintTransition();
                }
            };

            private void playHintTransition() {
                TRANSITION.setNode(HINT);
                TRANSITION.stop();
                TRANSITION.setDuration(Duration.millis(200));
                TRANSITION.setCycleCount(3);
                TRANSITION.setAutoReverse(true);
                TRANSITION.setFromValue(0);
                TRANSITION.setToValue(1);
                TRANSITION.playFromStart();
            }
        });

        final ChangeListener<Number> AUTO_SCROLL_DOWN_LISTENER = new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (SCROLL_PANE_CONTENT.getChildren().contains(instance.nameFieldContainer)) {
                    SCROLL_PANE.setVvalue(1); // scroll all the way down
                }
            };
        };

        NAME_INPUT.getTextField().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    SCROLL_PANE_CONTENT.heightProperty().removeListener(AUTO_SCROLL_DOWN_LISTENER);
                    instance.repopulate(); // if focus lost, treat it like user wants to cancel
                }
            }
        });

        this.SCROLL_PANE_CONTENT.heightProperty().addListener(AUTO_SCROLL_DOWN_LISTENER);

        this.nameFieldContainer.getChildren().addAll(TOP_HINT, NAME_INPUT, HINT);
        this.SCROLL_PANE_CONTENT.getChildren().add(instance.nameFieldContainer);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                NAME_INPUT.getTextField().requestFocus();
            }
        });
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
            this.setUntoggledColour(Colour.GHOST_2);
            this.setCursor(Cursor.DEFAULT);
            this.setMinHeight(40);
            this.getLabel().setFont(Font.font(FontTools.FONT_FAM, 13));
            this.setConsumeEvent(true);
            VBox.setMargin(this, new Insets(3, 2.5, 5, 2.5));
            if (!FOLDER.isProtectedFolder()) // for now, it's easier to just do this
                this.configureDrop();
        }

        private void configureDrop() {
            this.setOnMouseDragEntered(
                (event) -> { this.getCustomBorder().setStrokeColour(Color.ORANGE); });
            this.setOnMouseDragExited((event) -> {
                if (!this.getIsToggled()) // this interferes with DragRelease if no check
                    this.untoggle();
            });
            this.setOnMouseDragReleased((event) -> {
                CountdownPaneView.getInstance().addAllSelectedToFolder(this.FOLDER);
                DragHandler.close();
            });
        }

        @Override
        public void executeOnClick(MouseEvent event) {
            CountdownPaneView.getInstance().deselectAll(); // important
            SidebarFolderManager.getInstance().FOLDER_PANES.forEach(
                folder -> { folder.untoggle(); });

            if (!this.hasSameName(INCOMPLETED_FOLDER_PANE))
                INCOMPLETED_FOLDER_PANE.untoggle();

            if (!this.hasSameName(COMPLETED_FOLDER_PANE))
                COMPLETED_FOLDER_PANE.untoggle();

            SidebarFolderManager.selectedFolderPane = this;
            FolderHandler.setCurrentlySelectedFolder(this.FOLDER);
            CountdownPaneView.getInstance().repopulate(LocalDate.now());

            if (!this.FOLDER.isProtectedFolder() && event != null
                && event.getButton().equals(MouseButton.SECONDARY)) {
                FolderManagerRCM.spawnInstance(event.getSceneX(), event.getSceneY());
            }

            this.toggle();
        }

        public boolean hasSameName(FolderPane otherPane) {
            return this.getName().equals(otherPane.getName());
        }

        public String getName() {
            return this.FOLDER.getName();
        }
    }
}
