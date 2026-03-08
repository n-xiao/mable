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
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public final class IconButton extends ButtonFoundation {
    private static final Color HOVER_COLOUR =
        RiceHandler.adjustOpacity(RiceHandler.getColour("white"), 0.2);

    public IconButton() {}

    private String retrieveSvgString(final Icon icon) {
        return switch (icon) {
            case ONGOING -> "foobar";
            case COMPLETED -> "foobar";
            case DELETED -> "foobar";
            case SETTINGS -> "foobar";
            default -> throw new IllegalArgumentException("Illegal icon specified");
        };
    }

    public enum Icon { ONGOING, COMPLETED, DELETED, SETTINGS }
}
