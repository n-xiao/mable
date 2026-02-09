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
        container.setBackground(null);
        container.setFillHeight(true);

        final Line leftLine = new Line();
        leftLine.setPrefWidth(left);

        label.setFont(FontHandler.getHeading(3));
        label.setTextFill(colour);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setMargin(label, new Insets(0, 10, 0, 10));

        final Line rightLine = new Line();
        HBox.setHgrow(rightLine, Priority.ALWAYS);

        container.getChildren().addAll(leftLine, label, rightLine);
        container.prefWidthProperty().bind(this.widthProperty());
        container.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(container);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public final void setLeft(double left) {
        this.left = left;
        this.getChildren().clear();
        init();
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Line extends Pane {
        Line() {
            this.setBackground(null);
            this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            final CustomLine customLine = new CustomLine(thickness, Type.HORIZONTAL_TYPE);
            CustomLine.applyToPane(this, customLine);
        }
    }
}
