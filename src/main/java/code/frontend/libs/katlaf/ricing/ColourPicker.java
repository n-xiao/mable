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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

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
    public ColourPicker(final List<Colour> colours) {
        if (colours.isEmpty())
            throw new IllegalArgumentException(
                "ColourPicker cannot be created with an empty List of Colors");

        final HBox container = new HBox();
        container.setBackground(null);

        this.colourReps = new ArrayList<ColourRep>();
        colours.forEach(colour -> { this.colourReps.add(new ColourRep(colour)); });
        this.colourReps.forEach(colourRep -> HBox.setMargin(colourRep, new Insets(0, 5, 0, 5)));

        container.prefWidthProperty().bind(this.widthProperty());
        container.prefHeightProperty().bind(this.heightProperty());
        container.setAlignment(Pos.CENTER);
        container.setFillHeight(true);
        this.getChildren().add(container);

        container.getChildren().addAll(this.colourReps);
        this.colourReps.getFirst().select();
    }

    /**
     * A convenience constructor which accepts names of colours, registered by RiceHandler
     * (and found in the Themes.json resource).
     * <p>
     * Creates a new ColourPicker. A List of colours should be specified, as this
     * dictates the width of this entire UI component. The more colours are
     * specified, the wider this instance gets.
     *
     * @param colourNames                   the names of colours registered by RiceHandler
     *                                      which should be used to populate the instance
     *
     * @throws IllegalArgumentException     if the provided array of Strings is empty
     *
     * @see RiceHandler
     */
    public ColourPicker(final String... colourNames) {
        if (colourNames.length < 1)
            throw new IllegalArgumentException(
                "ColourPicker cannot be created with an empty array of colour names");

        final ArrayList<Colour> colours = new ArrayList<Colour>();
        for (String name : colourNames) {
            colours.add(new Colour(RiceHandler.getColour(name)));
        }
        this(colours);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * Deselects all colour representatives. A selection for one colour represenative
     * should always be done after this method is called. Otherwise, errors might be
     * thrown when calling getSelected().
     */
    private void deselectAll() {
        this.colourReps.forEach(colourRep -> colourRep.deselect());
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Returns the selected colour.
     *
     * @return Color    the selected colour
     */
    public Colour getSelected() {
        for (ColourRep colourRep : colourReps) {
            if (colourRep.isSelected())
                return colourRep.colour;
        }
        throw new IllegalStateException(
            "ColourPicker is unable to obtain selected colour because none are selected.");
    }

    public final void select(final Colour colour) {
        for (ColourRep colourRep : colourReps) {
            if (colourRep.colour.equals(colour))
                colourRep.select();
        }
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private final class ColourRep extends Region {
        private static final double SIZE = 20;
        private final Border selectBorder;
        private final Colour colour;

        ColourRep(final Colour colour) {
            this.setMinSize(SIZE, SIZE);
            this.setMaxSize(SIZE, SIZE);
            this.colour = colour;
            this.selectBorder = new Border(new BorderStroke(RiceHandler.getColour("white"),
                BorderStrokeStyle.SOLID, new CornerRadii(16), new BorderWidths(2)));

            this.setBackground(RiceHandler.createBG(this.colour.get(), 16, 1));

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
