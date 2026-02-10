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
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This is literally just an extension of ButtonLogic, but with a label
 * component added, with appropriate fonts and colouring stuff.
 *
 * @see ButtonLogic
 */
public abstract class LabelledButton extends ButtonLogic {
    private final Label label;

    /**
     * Creates a new LabelledButton instance. The instance created has a null background,
     * a default text fill of "white" as specified by the RiceHandler, the Normal font
     * specified by FontHandler, and alignment set to Pos.CENTER.
     * <p>
     * Note that the internal Label for this class is transparent to the mouse by default.
     * Also, the Label has its preferred width and height bound to the width and height property
     * of this instance.
     */
    public LabelledButton() {
        this.label = new Label();
        this.label.setBackground(null);
        this.label.setTextFill(RiceHandler.getColour("white")); // default value
        this.label.setFont(FontHandler.getNormal());
        this.label.setAlignment(Pos.CENTER);
        this.label.setMouseTransparent(true);
        this.label.prefWidthProperty().bind(this.widthProperty());
        this.label.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().addLast(this.label);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Sets the text of the internal Label.
     */
    public final void setText(final String text) {
        this.label.setText(text);
    }

    /**
     * Sets the font of the internal Label.
     */
    public final void setFont(final Font font) {
        this.label.setFont(font);
    }

    /**
     * Sets the text fill of the internal Label.
     */
    public final void setTextFill(final Color color) {
        this.label.setTextFill(color);
    }

    /**
     * Sets the alignment of the internal text label.
     * By default, it is set to Pos.CENTER
     */
    public final void setLabelAlignment(final Pos alignment) {
        this.label.setAlignment(alignment);
    }
}
