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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public final class Sidebar extends IconButtonList {
    private static final double V_GAP = 18;
    public Sidebar() {
        final ActiveButton activeButton = new ActiveButton();
        VBox.setMargin(activeButton, new Insets(0, 0, V_GAP, 0));
        final CompletedButton completedButton = new CompletedButton();
        VBox.setMargin(completedButton, new Insets(0, 0, V_GAP, 0));
        final DeletedButton deletedButton = new DeletedButton();
        VBox.setMargin(deletedButton, new Insets(0, 0, V_GAP, 0));

        this.setPadding(new Insets(17, 10, 10, 10));
        this.setAlignment(Pos.TOP_CENTER);
        this.setMaxHeight(Double.MAX_VALUE);
        this.setBackground(RiceHandler.createBG(RiceHandler.getColour("midnight"), 0, 0));
        this.add(activeButton, completedButton, deletedButton);

        this.select(activeButton);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/
    private static final double SIZE = 17;
    private static final Color COLOUR = RiceHandler.getColour("lightgrey");

    private class ActiveButton extends IconButton {
        ActiveButton() {
            super(IconHandler.getIconAsImage("calendar.png"), COLOUR);
            this.setMinSize(SIZE, SIZE);
            this.setMaxSize(SIZE, SIZE);
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            select(this);
            MainContainer.getInstance().selectActiveView();
        }
    }

    private class CompletedButton extends IconButton {
        CompletedButton() {
            super(IconHandler.getIconAsImage("check-circle.png"), COLOUR);
            this.setMinSize(SIZE, SIZE);
            this.setMaxSize(SIZE, SIZE);
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            select(this);
            MainContainer.getInstance().selectCompletedView();
        }
    }

    private class DeletedButton extends IconButton {
        DeletedButton() {
            super(IconHandler.getIconAsImage("trash-2.png"), COLOUR);
            this.setMinSize(SIZE, SIZE);
            this.setMaxSize(SIZE, SIZE);
        }

        @Override
        public void onMousePressed(MouseEvent event) {
            select(this);
            MainContainer.getInstance().selectDeletedView();
        }
    }
}
