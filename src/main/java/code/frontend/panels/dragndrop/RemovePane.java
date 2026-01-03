/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.panels.dragndrop;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals.Colour;
import code.frontend.misc.Vals.FontTools;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class RemovePane extends Pane {
    private final CustomBox BORDER;
    private final Label LABEL;
    private final BorderPane CONTAINER;

    private RemovePane() {
        BORDER = new CustomBox();
        LABEL = new Label();
        CONTAINER = new BorderPane();
        CustomBox.applyToPane(this, BORDER);
        configureStyling();
    }

    public static RemovePane applyTo(Pane pane) {
        RemovePane removePane = new RemovePane();
        removePane.setManaged(false);
        removePane.resize(pane.getWidth(), pane.getHeight());
        removePane.relocate(0, 0);
        removePane.setViewOrder(-1);
        removePane.setVisible(false);
        pane.getChildren().add(removePane);
        return removePane;
    }

    private void configureStyling() {
        BORDER.setStrokeColour(Colour.BTTN_REMOVE);
        final Color BG_COLOUR = Color.rgb(255, 0, 0, 0.5);
        this.setBackground(Colour.createBG(BG_COLOUR, 12, 3));
        LABEL.setTextFill(Color.WHITE);
        LABEL.setText("Drop here to remove");
        LABEL.setFont(Font.font(FontTools.FONT_FAM, FontWeight.BOLD, FontPosture.ITALIC, 13));
        CONTAINER.setCenter(LABEL);
        CONTAINER.setBackground(null);
        CONTAINER.prefWidthProperty().bind(this.widthProperty());
        CONTAINER.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(CONTAINER);
    }
}
