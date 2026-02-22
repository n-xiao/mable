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

import code.frontend.libs.katlaf.faces.BorderLabelFace;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * A button that reacts to user mouse actions. This class holds
 * references to three colours: idle, hover and active. All selection buttons are
 * first coloured using idle. On mouse hover, the colour of the button will be
 * changed to the hover colour. When the user clicks on this button, the
 * active colour will be used.
 *
 * @see ButtonFoundation
 */
public class SelectionButton extends ButtonFoundation {
    private final BorderLabelFace face;
    private Color idle;
    private Color hover; // colour of button when mouse hovers over
    private Color active; // colour of button when it has been clicked or selected

    public SelectionButton(final MableBorder mableBorder) {
        /*
         * set the default values
         */
        this.idle = RiceHandler.getColour("dullgrey");
        this.hover = RiceHandler.getColour("lightgrey");
        this.active = RiceHandler.getColour("white");
        /*
         * then set up the face of this Button
         */
        this.face = new BorderLabelFace(mableBorder);
        this.face.prefWidthProperty().bind(this.widthProperty());
        this.face.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().addFirst(this.face);
        this.face.setColour(this.idle);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * Responsible for colouring the appearance of this button based on
     * runtime properties.
     */
    private void style() {
        if (this.isEnabled() && this.isToggled()) {
            this.face.setColour(this.active);
        } else if (this.isEnabled() && this.isHover()) {
            this.face.setColour(this.hover);
        } else if (this.isEnabled()) {
            this.face.setColour(this.idle);
        } else {
            this.face.setColour(DISABLED_COLOUR);
        }
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected final void setIdleColour(final Color idle) {
        this.idle = idle;
    }

    protected final void setHoverColour(final Color hover) {
        this.hover = hover;
    }

    protected final void setActiveColour(final Color active) {
        this.active = active;
    }

    /**
     * Makes the face of this instance invisible. Note that the effect of this method cannot be
     * undone, and is a convenience method to get rid of styling if needed.
     */
    protected final void hideFace() {
        this.face.setVisible(false);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToggle(final boolean toggled) {
        super.setToggle(toggled);
        style();
    }

    /**
     * Implements styling when clicked. When overriding this method, please
     * remember to call the superclass method or the styling will not be applied.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void onMousePressed(MouseEvent event) {
        /*
         * button toggling logic occurs before the code below.
         */
        style();
    }

    @Override
    public void onMouseEntered(MouseEvent event) {
        if (this.isEnabled() && !this.isToggled()) {
            this.face.setColour(this.hover);
        }
    }

    @Override
    public void onMouseExited(MouseEvent event) {
        if (this.isEnabled() && !this.isToggled()) {
            this.face.setColour(this.idle);
        }
    }
}
