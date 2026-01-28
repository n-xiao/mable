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

public abstract class ToggleButton extends Button {
    private boolean isToggled;

    public ToggleButton(String name) {
        this.isToggled = false;
        super(name);
        this.setCustomBackground(null);
    }

    public boolean getIsToggled() {
        return this.isToggled;
    }

    @Override
    protected void onMousePress(MouseEvent event) {
        this.isToggled = !this.isToggled;
        if (this.isToggled) {
            // if toggled on, apply toggle style
            this.useToggledStyle();
        } else {
            this.useUntoggledStyle();
        }
        super.onMousePress(event);
    }

    /**
     * This is only called by the program.
     * It should not be called by a user click or press,
     * since it assumes no mouse is hovering over the button.
     */
    public void untoggle() {
        this.isToggled = false;
        this.useUntoggledStyle();
    }

    public void toggle() {
        this.isToggled = true;
        this.useToggledStyle();
    }

    protected void useUntoggledStyle() {
        this.getCustomBorder().setStrokeColour(RiceHandler.getColour("ghost2"));
        this.getLabel().setTextFill(RiceHandler.getColour("ghost2"));
    }

    protected void useToggledStyle() {
        this.getCustomBorder().setStrokeColour(RiceHandler.getColour("selected"));
        this.getLabel().setTextFill(RiceHandler.getColour("selected"));
    }
}
