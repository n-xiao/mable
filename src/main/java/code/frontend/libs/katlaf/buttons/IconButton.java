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

import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class IconButton extends ButtonFoundation {
    private static final Color HOVER_COLOUR =
        RiceHandler.adjustOpacity(RiceHandler.getColour("white"), 0.14);

    private final ImageView imageView;
    private final ColorInput colourInput;
    private final Color colour;
    private Color selectedColour;

    public IconButton(final Image image, final Color colour) {
        this.imageView = new ImageView(image);
        this.colour = colour;

        this.setSelectedColour("white");

        this.colourInput = new ColorInput();
        this.colourInput.widthProperty().bind(this.widthProperty());
        this.colourInput.heightProperty().bind(this.heightProperty());
        this.colourInput.setPaint(this.colour);
        this.useDeselectedStyle();
        this.imageView.setMouseTransparent(true);

        this.getChildren().add(this.imageView);

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.imageView.setFitWidth(newValue.doubleValue());
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.imageView.setFitHeight(newValue.doubleValue());
        });
    }

    private void updateStyle() {
        final ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1);
        final Blend blend = new Blend(BlendMode.SRC_ATOP, monochrome, this.colourInput);
        this.imageView.setEffect(blend);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public final void useSelectedStyle() {
        this.colourInput.setPaint(this.selectedColour);
        updateStyle();
    }

    public final void useDeselectedStyle() {
        this.colourInput.setPaint(this.colour);
        updateStyle();
    }

    public final void setSelectedColour(final String colourName) {
        setSelectedColour(RiceHandler.getColour(colourName));
    }

    public final void setSelectedColour(final Color colour) {
        this.selectedColour = colour;
    }

    @Override
    public final void onMouseEntered(MouseEvent event) {
        this.setBackground(RiceHandler.createBG(HOVER_COLOUR, 3, 1));
    }

    @Override
    public final void onMouseExited(MouseEvent event) {
        this.setBackground(null);
    }
}
