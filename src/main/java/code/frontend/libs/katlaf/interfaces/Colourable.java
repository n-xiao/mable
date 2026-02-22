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

package code.frontend.libs.katlaf.interfaces;

import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.paint.Color;

/**
 * An interface that can be implemented by (probably) any UI component.
 * <p>
 * Since many UI components are composed of, or contain, many other Nodes,
 * it is difficult and tedious to set the colour of each Node and their
 * children (if any) individually. Hence, this interface acts as a "standard".
 *
 * @see Node
 * @since v3.0.0-beta
 */
public interface Colourable {
    /**
     * Sets the colour of this node.
     */
    public void setColour(Color colour);

    /**
     * Convenience method. Removes the need to call RiceHandler.
     * Does not need to be overriden.
     */
    default void setColour(String colourString) {
        setColour(RiceHandler.getColour(colourString));
    }

    /**
     * Resets the colour of this node to a default colour.
     */
    public void resetColour();
}
