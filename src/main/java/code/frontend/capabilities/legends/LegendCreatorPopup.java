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

package code.frontend.capabilities.legends;

import code.backend.data.Legend;
import code.backend.data.LegendHandler;
import code.frontend.libs.katlaf.buttons.FilledButton;
import code.frontend.libs.katlaf.inputfields.BorderedField;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.Colour;
import code.frontend.libs.katlaf.ricing.ColourPicker;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class LegendCreatorPopup extends Popup {
    private final BorderedField nameField;
    private final ColourPicker colourPicker;
    private Legend oldLegend;
    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    /**
     * Creates a new instance. Used when a new Legend needs to be created.
     */
    public LegendCreatorPopup() {
        super(200, 150);

        this.nameField = new BorderedField("LEGEND NAME", RiceHandler.getColour("night"));
        this.colourPicker =
            new ColourPicker("green", "royalblue", "purple", "pink", "teal", "lightred");
        this.oldLegend = null;
    }

    /**
     * Creates a new instance. Used when an existing Legend needs to be edited.
     *
     * @param legend        the Legend to edit
     */
    public LegendCreatorPopup(final Legend legend) {
        this();
        this.oldLegend = legend;
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected String getIdent() {
        return "create legend";
    }

    @Override
    protected void configureContent(final StackPane content) {
        final boolean isEditing = this.oldLegend != null;
        if (isEditing) {
            this.nameField.setUserInput(this.oldLegend.getName());
            this.colourPicker.select(this.oldLegend.getColour());
        }

        VBox.setMargin(this.nameField, new Insets(0, 20, 10, 20));
        VBox.setMargin(this.colourPicker, new Insets(0, 20, 20, 20));

        final VBox container = new VBox();
        container.setBackground(null);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(
            this.nameField, this.colourPicker, new ButtonPair(isEditing));

        content.getChildren().add(container);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    private class ButtonPair extends HBox {
        ButtonPair(final boolean isEditing) {
            this.setAlignment(Pos.CENTER);
            this.setBackground(null);

            final FilledButton accept =
                new FilledButton(RiceHandler.getColour("dullblue"), RiceHandler.getColour("blue")) {
                    @Override
                    public void onMousePressed(MouseEvent event) {
                        final String name = nameField.getUserInput();
                        final Colour colour = colourPicker.getSelected();
                        LegendHandler.createLegend(name, colour);

                        Popup.despawn();
                    }
                };
            accept.setLabel(isEditing ? "Confirm" : "Create");
            VBox.setMargin(accept, new Insets(0, 2.5, 0, 0));

            final FilledButton decline =
                new FilledButton(RiceHandler.getColour("dullpink"), RiceHandler.getColour("red")) {
                    @Override
                    public void onMousePressed(MouseEvent event) {
                        Popup.despawn();
                    }
                };
            decline.setLabel(isEditing ? "Discard" : "Cancel");
            VBox.setMargin(decline, new Insets(0, 0, 0, 2.5));

            this.getChildren().addAll(decline, accept);
        }
    }
}
