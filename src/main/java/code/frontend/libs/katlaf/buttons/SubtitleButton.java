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

package code.frontend.libs.katlaf.buttons;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public abstract class SubtitleButton extends TemplateButton {
    private final Label label;

    public SubtitleButton(String text) {
        this.setBackground(null);
        label = new Label(text);
        label.setFont(FontHandler.getItalic());
        label.setTextFill(RiceHandler.getColour("txtGhost"));
        label.setAlignment(Pos.CENTER);
        label.maxWidthProperty().bind(this.widthProperty());
        label.maxHeightProperty().bind(this.heightProperty());
        label.setCursor(Cursor.HAND);
    }

    @Override
    protected void onMouseEnter(MouseEvent event) {
        label.setTextFill(RiceHandler.getColour("selected"));
    }

    @Override
    protected void onMouseLeave(MouseEvent event) {
        label.setTextFill(RiceHandler.getColour("txtGhost"));
    }

    @Override
    protected void onMousePress(MouseEvent event) {
        executeOnClick();
    }

    public abstract void executeOnClick();
}
