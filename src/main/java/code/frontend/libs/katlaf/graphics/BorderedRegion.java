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

package code.frontend.libs.katlaf.graphics;

import javafx.scene.layout.Region;

/**
 * A wrapper Region which has a MableBorder as a child node.
 *
 * @see MableBorder
 */
public class BorderedRegion extends Region {
    private final MableBorder border;

    public BorderedRegion(
        final double thickness, final double messiness, final double cornerRadii) {
        this.border = new MableBorder(thickness, messiness, cornerRadii);
        this.border.widthProperty().bind(this.widthProperty());
        this.border.heightProperty().bind(this.heightProperty());
        this.getChildren().add(border);
    }

    public BorderedRegion(final MableBorder mableBorder) {
        this.border = mableBorder;
        this.border.widthProperty().bind(this.widthProperty());
        this.border.heightProperty().bind(this.heightProperty());
        this.getChildren().add(border);
    }

    public MableBorder getCustomBorder() {
        return this.border;
    }
}
