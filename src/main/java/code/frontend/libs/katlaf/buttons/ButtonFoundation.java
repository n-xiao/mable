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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * This is the foundation class of all buttons. It implements button click logic
 * and toggle button logic. Implementations of this class do not need to utilise
 * all of its features; it is only present to maximise extensibility.
 *
 * This class inherits a(n) (empty) StackPane as child classes may wish
 * to add other UI effects to this class.
 *
 * @since v3.0.0-beta
 * @see StackPane
 */
public abstract class ButtonFoundation extends StackPane {
    static final Color DISABLED_COLOUR = RiceHandler.getColour("dullgrey");
    private boolean toggled;
    private boolean enabled;

    public ButtonFoundation() {
        this.enabled = true;
        // set up the click detection stuff
        setOnMousePressed(event -> {
            toggle();
            onMousePressed(event);
        });
        setOnMouseReleased(this::onMouseReleased);
        setOnMouseEntered(this::onMouseEntered);
        setOnMouseExited(this::onMouseExited);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * When this button is active, the toggle property will be replaced with the opposite
     * of its previous value - NOT will be applied to it.
     * */
    final void toggle() {
        if (this.enabled)
            this.toggled = !this.toggled;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * This method provides a way to set the toggle value of this button
     * during runtime. It should not be called by user input. Please
     * use the toggle() method for that. Note that this method does not
     * check if the button is enabled or not. It will be executed even if the
     * button has been disabled.
     *
     * @param toggled   true if the button should be toggled
     */
    public void setToggle(final boolean toggled) {
        this.toggled = toggled;
    }

    /**
     * Executed when this button is pressed, but not released.
     */
    public void onMousePressed(final MouseEvent event){};

    /**
     * Executed when this button is released from a mouse press.
     */
    public void onMouseReleased(final MouseEvent event){};

    /**
     * Executed when a mouse enters this button.
     */
    public void onMouseEntered(final MouseEvent event){};

    /**
     * Executed when a mouse leaves this button.
     */
    public void onMouseExited(final MouseEvent event){};

    /**
     * Returns true if this button is toggled. False otherwise.
     * A button is not toggled by default. The user will have to click
     * the button in order to toggle it. If the button is already toggled when
     * a user clicks it, the button will be untoggled.
     *
     * @return true if this is toggled
     */
    public final boolean isToggled() {
        return this.toggled;
    }

    /**
     * Returns true if this button is enabled. False otherwise.
     * By default, this property and method does nothing. However, child classes
     * should use this method for checks when implementing the abstract methods of
     * this class.
     */
    public final boolean isEnabled() {
        return this.enabled;
    }

    /**
     * This method just sets the enabled property to the provided value. It is up to
     * implementations of this class to utilise the isEnabled() method within the
     * implementations of the abstract methods.
     * <p>
     * By default, the only time that the value of this property matters is during the
     * onMousePressed event, since this button will be toggled if enabled.
     *
     * @param true if this button should be marked as enabled.
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
