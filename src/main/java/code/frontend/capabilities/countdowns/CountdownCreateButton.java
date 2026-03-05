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

package code.frontend.capabilities.countdowns;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.buttons.FilledButton;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.input.MouseEvent;

public class CountdownCreateButton extends FilledButton {
    public CountdownCreateButton() {
        super(RiceHandler.getColour("white"), RiceHandler.getColour("white2"));
        this.setLabel("+");
        this.setLabelColour(RiceHandler.getColour("black"));
        this.setLabelFont(FontHandler.getDedicated(DedicatedFont.SYMBOL));
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    @Override
    public void onMousePressed(MouseEvent event) {
        // TODO THIS
    }
}
