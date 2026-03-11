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

package code.frontend.capabilities.views;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class Title extends BorderPane {
    protected static final double TOP_MARGIN = 2;
    protected static final double SIDE_MARGIN = 4;

    public Title(final String text) {
        this.setBackground(null);

        final Label label = new Label(text);
        label.setFont(FontHandler.getHeading(1));
        label.setTextFill(RiceHandler.getColour("white"));
        label.setMouseTransparent(true);
        BorderPane.setMargin(label, new Insets(TOP_MARGIN, 0, 0, SIDE_MARGIN));
        this.setLeft(label);
    }
}
