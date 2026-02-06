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

import code.frontend.capabilities.countdown.components.OldCountdownTable.ButtonMode;
import code.frontend.capabilities.countdown.windows.CountdownCreator;
import code.frontend.libs.katlaf.buttons.BasicButton;
import code.frontend.libs.katlaf.menus.RightClickMenu;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.input.MouseEvent;
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
 */
public class CountdownRCM extends RightClickMenu {
    private static CountdownRCM instance = null;

    private static final double WIDTH = 180;
    private static final double HEIGHT = 200;

    private final BasicButton CREATE;
    private final BasicButton EDIT;
    private final BasicButton DELETE;
    private final BasicButton MARK_COMPLETE;
    private final BasicButton SELECTOR;

    public static void spawnInstance(double x, double y) {
        if (instance == null)
            instance = new CountdownRCM();
        instance.openAt(x, y);
    }

    public static void despawn() {
        if (instance == null)
            return;
        instance.close();
        instance = null;
    }

    private CountdownRCM() {
        final boolean SELECTED_ARE_COMPLETED =
            OldCountdownTable.getInstance().allSelectedAreCompleted();
        final int NUM_OF_SELECTIONS = OldCountdownTable.getInstance().getNumOfSelections();

        final String CREATE_STR = "New Task...";
        final String SELECTOR_STR =
            NUM_OF_SELECTIONS > 0 ? "Deselect all (" + NUM_OF_SELECTIONS + ")" : "Select all";
        final String MARK_COMPLETE_STR =
            SELECTED_ARE_COMPLETED ? "Mark as incomplete" : "Mark as complete";
        final String EDIT_STR = "Edit...";
        final String DELETE_STR = "Delete";

        BasicButton create = new BasicButton(CREATE_STR) {
            @Override
            public void executeOnClick(MouseEvent event) {
                CountdownCreator.getInstance();
                CountdownRCM.despawn();
            }
        };

        BasicButton selector = new BasicButton(SELECTOR_STR) {
            public void executeOnClick(MouseEvent event) {
                if (NUM_OF_SELECTIONS > 0)
                    OldCountdownTable.getInstance().deselectAll();
                else
                    OldCountdownTable.getInstance().selectAll();
                CountdownRCM.despawn();
            };
        };

        BasicButton markAsComplete = new BasicButton(MARK_COMPLETE_STR) {
            @Override
            public void executeOnClick(MouseEvent event) {
                OldCountdownTable.getInstance().markSelectedAsComplete(!SELECTED_ARE_COMPLETED);
                CountdownRCM.despawn();
            }
        };
        BasicButton edit = new BasicButton(EDIT_STR) {
            @Override
            public void executeOnClick(MouseEvent event) {
                OldCountdownTable.getInstance().editSelected();
                CountdownRCM.despawn();
            }
        };
        BasicButton delete = new BasicButton(DELETE_STR) {
            @Override
            public void executeOnClick(MouseEvent event) {
                OldCountdownTable.getInstance().deleteSelected();
                CountdownRCM.despawn();
            }
        };

        BasicButton[] buttons = {create, edit, delete, null, markAsComplete, null, selector};
        Color[] colours = {RiceHandler.getColour("bttnCreate"), RiceHandler.getColour("bttnEdit"),
            RiceHandler.getColour("bttnRemove"), null, RiceHandler.getColour("bttnMarkComplete"),
            null, RiceHandler.getColour("bttnDeselect")};

        super(WIDTH, HEIGHT, buttons, colours);

        this.CREATE = create;
        this.EDIT = edit;
        this.DELETE = delete;
        this.MARK_COMPLETE = markAsComplete;
        this.SELECTOR = selector;
    }

    @Override
    public void setMode() {
        ButtonMode mode = OldCountdownTable.getInstance().getMode();
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
