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
import javafx.scene.layout.HBox;

/**
 * A basic left-aligned title which can be used in various views
 * for consistency. Should be placed at the top of the view.
 *
 * @since v3.0.0-beta
 */
public class Title extends BorderPane {
    protected static final double TOP_MARGIN = 2;
    protected static final double SIDE_MARGIN = 4;

    private final HBox container;

    /**
     * Creates a new instance with the provided text as the title.
     *
     * @param text      the String which should be used as the title
     */
    public Title(final String text) {
        this.setBackground(null);

        final Label label = new Label(text);
        label.setFont(FontHandler.getHeading(1));
        label.setTextFill(RiceHandler.getColour("white"));

        this.container = new HBox();
        this.container.getChildren().add(label);
        BorderPane.setMargin(this.container, new Insets(TOP_MARGIN, 0, 0, SIDE_MARGIN));

        this.setLeft(this.container);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected final HBox getLeftContainer() {
        return this.container;
    }
}
