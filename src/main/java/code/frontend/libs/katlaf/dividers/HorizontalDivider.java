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

package code.frontend.libs.katlaf.dividers;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * A horizontal divider which extends a Region. This should be used to visually
 * separate different parts of the user interface to the user. This consists
 * of a left horizontal line, a Label and a right horizontal line. The
 * left horizontal line has a length of 20 by default, but can be set. The
 * right horizontal line will fill the remaining space after the left
 * horizontal line and the Label is added.
 */
public class HorizontalDivider extends Region {
    private final HBox container;
    private final double thickness;
    private final Label label;
    private final Color colour;
    private double left; // amount of space on the left

    public HorizontalDivider(final double thickness, final String labelText, final Color colour) {
        this.container = new HBox();
        this.thickness = thickness;
        this.label = new Label(labelText);
        this.colour = colour;
        this.left = 20; // default value
        init();
    }

    private void init() {
        this.container.setBackground(null);
        this.container.setFillHeight(true);

        final Line leftLine = new Line();
        leftLine.setPrefWidth(left);

        this.label.setFont(FontHandler.getHeading(3));
        this.label.setTextFill(colour);
        this.label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setMargin(label, new Insets(0, 10, 0, 10));

        final Line rightLine = new Line();
        HBox.setHgrow(rightLine, Priority.ALWAYS);

        this.container.getChildren().addAll(leftLine, label, rightLine);
        this.container.prefWidthProperty().bind(this.widthProperty());
        this.container.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(this.container);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Sets the length of the left horizontal line and redraws all
     * visual components of this.
     */
    public final void setLeft(double left) {
        this.left = left;
        this.getChildren().clear();
        this.init();
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Line extends Pane {
        Line() {
            this.setBackground(null);
            this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            final CustomLine customLine = new CustomLine(thickness, Type.HORIZONTAL);
            CustomLine.applyToPane(this, customLine);
        }
    }
}
