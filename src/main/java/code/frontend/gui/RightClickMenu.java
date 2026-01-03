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

package code.frontend.gui;

import code.frontend.foundation.CustomBox;
import code.frontend.foundation.CustomLine;
import code.frontend.foundation.CustomLine.Type;
import code.frontend.misc.Vals.Colour;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneView;
import code.frontend.panels.CountdownPaneView.ButtonMode;
import code.frontend.windows.AddWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * man, i should really write more of these things called comments...
 * RightClickMenu is a singleton class which returns the menu that
 * is shown under the user's cursor when a user presses right-click
 * on a button or something that would trigger this to be shown.
 *
 * In order for it to show up anywhere, this VBox is set to unmanaged
 * and should be manually position based on the cursor position.
 *
 * It is imperative to note that this class uses a "lazy loading"
 * concept, where the GUI is only created (and seen by the user)
 * when the user right-clicks on something. Hence, the only calls
 * to getInstance should be from right-click listeners.
 *
 * oh btw this is like the neatest class ever...
 * TODO make every class as tidy as this one
 *
 */
public class RightClickMenu extends VBox {
    private static RightClickMenu instance = null;

    private static final double BG_RADIUS = 12;
    private static final double BG_INSETS = 4;

    private static final double BORDER_CORNER_OFFSET = 0.12;
    private static final double BORDER_DEV = 0;
    private static final double BORDER_CORNER_DEV = 0.014;
    private static final double BORDER_THICKNESS = 2;

    private static final int RIGHTLEFT_INSET = 15;

    private static final double WIDTH = 200;
    private static final double HEIGHT = 225;

    private static final double BUTTON_HEIGHT = 40;

    private final boolean SELECTED_ARE_COMPLETED;
    private final int NUM_OF_SELECTIONS;

    private final String CREATE_STR;
    private final String SELECTOR_STR;
    private final String MARK_COMPLETE_STR;
    private final String EDIT_STR;
    private final String DELETE_STR;

    private Button create; // always enabled
    private Button edit;
    private Button delete;
    // divider
    private Button markAsComplete;
    private Button selector; // always enabled

    private RightClickMenu() {
        SELECTED_ARE_COMPLETED = CountdownPaneView.getInstance().allSelectedAreCompleted();
        NUM_OF_SELECTIONS = CountdownPaneView.getInstance().getNumOfSelections();

        CREATE_STR = "New Task...";
        SELECTOR_STR =
            NUM_OF_SELECTIONS > 0 ? "Deselect all (" + NUM_OF_SELECTIONS + ")" : "Select all";
        MARK_COMPLETE_STR = SELECTED_ARE_COMPLETED ? "Mark as incomplete" : "Mark as complete";
        EDIT_STR = "Edit...";
        DELETE_STR = "Delete";

        this.create = new Button(CREATE_STR) {
            @Override
            public void executeOnClick() {
                AddWindow.getInstance();
                RightClickMenu.close();
            }
        };

        this.selector = new Button(SELECTOR_STR) {
            public void executeOnClick() {
                if (NUM_OF_SELECTIONS > 0)
                    CountdownPaneView.getInstance().deselectAll();
                else
                    CountdownPaneView.getInstance().selectAll();
                RightClickMenu.close();
            };
        };

        this.markAsComplete = new Button(MARK_COMPLETE_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().markSelectedAsComplete(!SELECTED_ARE_COMPLETED);
                RightClickMenu.close();
            }
        };
        this.edit = new Button(EDIT_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().editSelected();
                RightClickMenu.close();
            }
        };
        this.delete = new Button(DELETE_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().deleteSelected();
                RightClickMenu.close();
            }
        };
    }

    public static RightClickMenu openAt(double x, double y) {
        RightClickMenu.close();

        instance = new RightClickMenu();
        instance.initStyling();

        instance.initButtonStylings(instance.create, instance.selector, instance.markAsComplete,
            instance.edit, instance.delete);

        instance.getChildren().addAll(instance.create, instance.edit, instance.delete,
            instance.createDivider(), instance.markAsComplete, instance.createDivider(),
            instance.selector);

        instance.setMode();

        MainContainer mc = MainContainer.getInstance();
        instance.relocate(x, y);
        mc.getChildren().add(instance);
        instance.setViewOrder(-100);

        return instance;
    }

    public static void close() {
        if (instance == null)
            return;
        instance.setVisible(false);
        MainContainer.getInstance().getChildren().remove(instance);
        instance = null;
    }

    private void initStyling() {
        CustomBox border =
            new CustomBox(BORDER_THICKNESS, BORDER_DEV, BORDER_CORNER_DEV, BORDER_CORNER_OFFSET);
        CustomBox.applyToPane(this, border);
        this.setManaged(false);
        this.setBackground(Colour.createBG(Colour.BACKGROUND, BG_RADIUS, BG_INSETS));
        this.setFillWidth(true);
        this.resize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setAlignment(Pos.CENTER);
    }

    private void initButtonStylings(Button... buttons) {
        for (Button button : buttons) {
            button.setPrefSize(WIDTH, BUTTON_HEIGHT);
            button.setMaxSize(WIDTH, BUTTON_HEIGHT);
            button.setAnimationsEnabled(false);
            button.getCustomBorder().setVisible(false);
            button.getLabel().setAlignment(Pos.CENTER_LEFT);
            button.setConsumeEvent(true);
            VBox.setMargin(button, new Insets(0, RIGHTLEFT_INSET, 0, RIGHTLEFT_INSET));
        }

        final CornerRadii CORNER_RADII = new CornerRadii(8);
        final Insets INSETS = new Insets(5, -5, 5, -5);

        BackgroundFill createFill = new BackgroundFill(Colour.BTTN_CREATE, CORNER_RADII, INSETS);
        BackgroundFill selectorFill =
            new BackgroundFill(Colour.BTTN_DESELECT, CORNER_RADII, INSETS);
        BackgroundFill markFill =
            new BackgroundFill(Colour.BTTN_MARK_COMPLETE, CORNER_RADII, INSETS);
        BackgroundFill editFill = new BackgroundFill(Colour.BTTN_EDIT, CORNER_RADII, INSETS);
        BackgroundFill deleteFill = new BackgroundFill(Colour.BTTN_REMOVE, CORNER_RADII, INSETS);

        this.create.setFeedbackBackground(new Background(createFill));
        this.selector.setFeedbackBackground(new Background(selectorFill));
        this.markAsComplete.setFeedbackBackground(new Background(markFill));
        this.edit.setFeedbackBackground(new Background(editFill));
        this.delete.setFeedbackBackground(new Background(deleteFill));
    }

    private Region createDivider() {
        final double DIVIDER_HEIGHT = 5;
        Pane divider = new Pane();
        divider.setPrefSize(WIDTH, DIVIDER_HEIGHT);
        divider.setMaxSize(WIDTH, DIVIDER_HEIGHT);
        divider.setBackground(null);
        divider.setOpacity(0.3);
        CustomLine line = new CustomLine(BORDER_THICKNESS, Type.HORIZONTAL_TYPE);
        line.setPadding(RIGHTLEFT_INSET);
        line.setStrokeColour(Color.WHITE);
        CustomLine.applyToPane(divider, line);

        return divider;
    }

    private void setMode() {
        ButtonMode mode = CountdownPaneView.getInstance().getMode();
        this.create.setEnabled(true);
        this.selector.setEnabled(true);
        switch (mode) {
            case NO_SELECT:
                this.markAsComplete.setEnabled(false);
                this.edit.setEnabled(false);
                this.delete.setEnabled(false);
                break;
            case SINGLE_SELECT:
                this.markAsComplete.setEnabled(true);
                this.edit.setEnabled(true);
                this.delete.setEnabled(true);
                break;
            case MULTI_SELECT:
                this.markAsComplete.setEnabled(true);
                this.edit.setEnabled(false);
                this.delete.setEnabled(true);
                break;
            default:
                break;
        }
    }
}
