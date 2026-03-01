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

package code.frontend.libs.katlaf.ricing;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * This class serves as a colour picker. Custom colours are not supported at the moment,
 * but will be added at a later date.
 *
 * @since v3.0.0-beta
 */
public class ColourPicker extends Region {
    private final ArrayList<ColourRep> colourReps;

    /**
     * Creates a new ColourPicker. A List of colours should be specified, as this
     * dictates the width of this entire UI component. The more colours are
     * specified, the wider this instance gets.
     *
     * @param colours                       a list of Colors which should be displayed for the user
     *                                      to choose from
     * @throws IllegalArgumentException     if the provided argument List of Colors is empty
     * @see Color
     */
    public ColourPicker(final List<Color> colours) {
        if (colours.isEmpty())
            throw new IllegalArgumentException(
                "ColourPicker cannot be created with an empty List of Colors");

        this.colourReps = new ArrayList<ColourRep>();
        colours.forEach(colour -> { this.colourReps.add(new ColourRep(colour)); });
        this.colourReps.getFirst().select();
    }

    private void deselectAll() {
        this.colourReps.forEach(colourRep -> colourRep.deselect());
    }

    public Color getSelected() {
        for (ColourRep colourRep : colourReps) {
            if (colourRep.isSelected())
                return colourRep.colour;
        }
        throw new IllegalStateException(
            "ColourPicker is unable to obtain selected colour because none are selected.");
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private final class ColourRep extends StackPane {
        private final Border selectBorder;
        private final Color colour;

        ColourRep(final Color colour) {
            this.colour = colour;
            this.selectBorder = new Border(new BorderStroke(RiceHandler.getColour("lightblue"),
                BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2)));

            this.setOnMousePressed(event -> {
                ColourPicker.this.deselectAll();
                this.select();
                event.consume();
            });
        }

        void deselect() {
            this.setBorder(null);
        }

        void select() {
            this.setBorder(this.selectBorder);
        }

        boolean isSelected() {
            return this.getBorder() != null;
        }
    }
}
