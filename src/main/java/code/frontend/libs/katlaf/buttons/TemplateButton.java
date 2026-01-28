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

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

abstract class TemplateButton extends Pane {
    private boolean enabled;

    protected TemplateButton() {
        this.enabled = true;
        this.setOnMouseEntered(event -> {
            if (enabled)
                onMouseEnter(event);
        });
        this.setOnMouseExited(event -> {
            if (enabled)
                onMouseLeave(event);
        });
        this.setOnMousePressed(event -> {
            if (enabled) {
                onMousePress(event);
                event.consume();
            }
        });
    }
    protected void onMouseEnter(MouseEvent event) {}
    protected void onMouseLeave(MouseEvent event) {}
    protected void onMousePress(MouseEvent event) {}

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }
}
