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

package code.frontend.libs.katlaf.tables;

import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.skin.ScrollBarSkin;
import javafx.scene.layout.StackPane;

/**
 * Unused at this time
 */
public class CustomScrollBarSkin extends ScrollBarSkin {
    private final StackPane background;
    private final StackPane incremButton;
    private final StackPane decremButton;
    private final StackPane toe;

    // TODO
    public CustomScrollBarSkin(ScrollBar scrollbar) {
        super(scrollbar);
        this.background = retrieveComponent("track-background");
        this.incremButton = retrieveComponent(AccessibleRole.INCREMENT_BUTTON);
        this.decremButton = retrieveComponent(AccessibleRole.DECREMENT_BUTTON);
        this.toe = retrieveComponent(AccessibleRole.THUMB);
    }

    private StackPane retrieveComponent(AccessibleRole role) {
        for (Node node : getChildren()) {
            if (node instanceof StackPane && node.getAccessibleRole().equals(role)) {
                return (StackPane) node;
            }
        }
        throw new IllegalStateException("unlucky");
    }

    private StackPane retrieveComponent(String style) {
        for (Node node : getChildren()) {
            if (node instanceof StackPane && node.getStyleClass().getFirst().equals(style))
                return (StackPane) node;
        }
        throw new IllegalStateException("unlucky");
    }
}
