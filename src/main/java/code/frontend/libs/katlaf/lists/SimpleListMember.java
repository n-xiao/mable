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

package code.frontend.libs.katlaf.lists;

import code.frontend.libs.katlaf.buttons.ToggleButton;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SimpleListMember extends ToggleButton {
    private final Listable listable;

    protected SimpleListMember(Listable listable) {
        this.listable = listable;
        super(listable.getDisplayString());
        this.getLabel().setAlignment(Pos.CENTER_LEFT);
        this.getLabel().relocate(15, 0);
        this.getCustomBorder().setThickness(1.5);
        this.getCustomBorder().setCornerRadii(0.8);
        this.getCustomBorder().setMessiness(0.1);
        this.setMinHeight(40);
        VBox.setMargin(this, new Insets(3, 2.5, 5, 2.5));
    }

    /**
     * Updates the styling of this {@link SimpleListMember} based on
     * verification from the backend.
     */
    protected void updateSelection() {
        if (listable.isEngaged())
            this.toggle();
        else
            this.untoggle();
    }

    @Override
    public void executeOnClick(MouseEvent event) {
        listable.onButtonClick();
    }

    @Override
    protected Color getUntoggledColour() {
        return RiceHandler.getColour("disabled");
    }

    @Override
    protected Color getToggledColour() {
        return RiceHandler.getColour();
    }
}
