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

package code.frontend.capabilities.countdown.components;

import code.backend.utils.FolderHandler;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CountdownFolderTitle extends VBox {
    private static CountdownFolderTitle instance = null;

    private final Label LABEL;

    private CountdownFolderTitle() {
        this.setBackground(null);
        this.setFillWidth(true);
        this.LABEL = new Label();
        configureLabel();
        this.getChildren().addAll(this.LABEL, createHorizontalLine());
    }

    public static CountdownFolderTitle getInstance() {
        if (instance == null) {
            instance = new CountdownFolderTitle();
        }
        return instance;
    }

    private void configureLabel() {
        LABEL.setTextFill(RiceHandler.getColour());
        LABEL.setFont(FontHandler.getHeading(1));
        LABEL.setAlignment(Pos.CENTER);
    }

    private static Pane createHorizontalLine() {
        Pane lineContainer = new Pane();
        lineContainer.setBackground(null);
        CustomLine line = new CustomLine(3, Type.HORIZONTAL_TYPE);
        line.setStrokeColour(RiceHandler.getColour("txtGhost2"));
        CustomLine.applyToPane(lineContainer, line);
        lineContainer.setMaxWidth(Double.MAX_VALUE);
        lineContainer.setMinHeight(5);
        lineContainer.setOpacity(0.5);
        VBox.setMargin(lineContainer, new Insets(5, 0, 15, 0));
        return lineContainer;
    }

    public void updateTitleText() {
        this.LABEL.setText(FolderHandler.getCurrentlySelectedFolder().getName());
    }
}
