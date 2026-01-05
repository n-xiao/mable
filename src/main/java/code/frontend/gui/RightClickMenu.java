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

import code.frontend.misc.Vals.Colour;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneView;
import code.frontend.panels.CountdownPaneView.ButtonMode;
import code.frontend.windows.AddWindow;
import javafx.scene.paint.Color;

/**
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
 *
 */
public class RightClickMenu extends RightClickMenuTemplate {
    private static RightClickMenu instance = null;

    private static final double WIDTH = 200;
    private static final double HEIGHT = 225;

    private final Button CREATE;
    private final Button EDIT;
    private final Button DELETE;
    private final Button MARK_COMPLETE;
    private final Button SELECTOR;

    public static void spawnInstance(double x, double y) {
        if (instance == null)
            instance = new RightClickMenu();
        instance.openAt(x, y);
    }

    public static void despawn() {
        if (instance == null)
            return;
        instance.close();
    }

    private RightClickMenu() {
        final boolean SELECTED_ARE_COMPLETED =
            CountdownPaneView.getInstance().allSelectedAreCompleted();
        final int NUM_OF_SELECTIONS = CountdownPaneView.getInstance().getNumOfSelections();

        final String CREATE_STR = "New Task...";
        final String SELECTOR_STR =
            NUM_OF_SELECTIONS > 0 ? "Deselect all (" + NUM_OF_SELECTIONS + ")" : "Select all";
        final String MARK_COMPLETE_STR =
            SELECTED_ARE_COMPLETED ? "Mark as incomplete" : "Mark as complete";
        final String EDIT_STR = "Edit...";
        final String DELETE_STR = "Delete";

        Button create = new Button(CREATE_STR) {
            @Override
            public void executeOnClick() {
                AddWindow.getInstance();
                instance.close();
            }
        };

        Button selector = new Button(SELECTOR_STR) {
            public void executeOnClick() {
                if (NUM_OF_SELECTIONS > 0)
                    CountdownPaneView.getInstance().deselectAll();
                else
                    CountdownPaneView.getInstance().selectAll();
                instance.close();
            };
        };

        Button markAsComplete = new Button(MARK_COMPLETE_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().markSelectedAsComplete(!SELECTED_ARE_COMPLETED);
                instance.close();
            }
        };
        Button edit = new Button(EDIT_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().editSelected();
                instance.close();
            }
        };
        Button delete = new Button(DELETE_STR) {
            @Override
            public void executeOnClick() {
                CountdownPaneView.getInstance().deleteSelected();
                instance.close();
            }
        };

        Button[] buttons = {create, edit, delete, null, markAsComplete, null, selector};
        Color[] colours = {Colour.BTTN_CREATE, Colour.BTTN_EDIT, Colour.BTTN_REMOVE, null,
            Colour.BTTN_MARK_COMPLETE, null, Colour.BTTN_DESELECT};

        super(WIDTH, HEIGHT, buttons, colours);

        this.CREATE = create;
        this.EDIT = edit;
        this.DELETE = delete;
        this.MARK_COMPLETE = markAsComplete;
        this.SELECTOR = selector;
    }

    @Override
    public void setMode() {
        ButtonMode mode = CountdownPaneView.getInstance().getMode();
        this.CREATE.setEnabled(true);
        this.SELECTOR.setEnabled(true);
        switch (mode) {
            case NO_SELECT:
                this.MARK_COMPLETE.setEnabled(false);
                this.EDIT.setEnabled(false);
                this.DELETE.setEnabled(false);
                break;
            case SINGLE_SELECT:
                this.MARK_COMPLETE.setEnabled(true);
                this.EDIT.setEnabled(true);
                this.DELETE.setEnabled(true);
                break;
            case MULTI_SELECT:
                this.MARK_COMPLETE.setEnabled(true);
                this.EDIT.setEnabled(false);
                this.DELETE.setEnabled(true);
                break;
            default:
                break;
        }
    }
}
