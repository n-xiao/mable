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

package code.frontend.capabilities.sidebar;

import code.frontend.MainContainer;
import code.frontend.libs.katlaf.buttons.IconButton;
import code.frontend.libs.katlaf.icons.IconHandler;
import code.frontend.libs.katlaf.lists.IconButtonList;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public final class Sidebar extends IconButtonList {
    private static final double V_GAP = 18;
    public Sidebar() {
        final ActiveButton activeButton = new ActiveButton();
        final CompletedButton completedButton = new CompletedButton();
        final DeletedButton deletedButton = new DeletedButton();
        final SettingsButton settingsButton = new SettingsButton();
        configureButtons(activeButton, completedButton, deletedButton, settingsButton);
        VBox.setMargin(settingsButton, null);

        this.setPadding(new Insets(17, 10, 14, 10));
        this.setAlignment(Pos.TOP_CENTER);
        this.setMaxHeight(Double.MAX_VALUE);
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("midnight"), 0, 0));
        this.add(activeButton, completedButton, deletedButton);

        final var spacer = new Region();
        spacer.setVisible(false);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Sidebar.setVgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);

        this.add(settingsButton);

        this.select(activeButton);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private static void configureButtons(final IconButton... buttons) {
        for (IconButton button : buttons) {
            VBox.setMargin(button, new Insets(0, 0, V_GAP, 0));
            button.setMaxSize(SIZE, SIZE);
            button.setMinSize(SIZE, SIZE);
        }
    }

    private static Color getButtonColour() {
        return RiceHandler.getColour("lightgrey");
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void match(final Node... nodes) {
        int index = 0;
        for (Node node : nodes) {
            if (node.isVisible()) {
                this.select(index);
                return;
            }
            index++;
        }
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/
    private static final double SIZE = 17;

    private class ActiveButton extends IconButton {
        ActiveButton() {
            super(IconHandler.getIconAsImage("calendar.png"), getButtonColour());
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            select(this);
            MainContainer.getInstance().selectActiveView();
        }
    }

    private class CompletedButton extends IconButton {
        CompletedButton() {
            super(IconHandler.getIconAsImage("check-circle.png"), getButtonColour());
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            select(this);
            MainContainer.getInstance().selectCompletedView();
        }
    }

    private class DeletedButton extends IconButton {
        DeletedButton() {
            super(IconHandler.getIconAsImage("trash-2.png"), getButtonColour());
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            select(this);
            MainContainer.getInstance().selectDeletedView();
        }
    }

    private class SettingsButton extends IconButton {
        SettingsButton() {
            super(IconHandler.getIconAsImage("settings.png"), getButtonColour());
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            select(this);
            MainContainer.getInstance().selectSettingsView();
        }
    }
}
