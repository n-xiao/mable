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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class LegendCreatorPopup extends Popup {
    private static final double WIDTH = 300;
    private static final double HEIGHT = 220;
    private final LegendTable table;
    private final BorderedField nameField;
    private final ColourPicker colourPicker;
    private LegendTableMember oldMember;
    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    /**
     * Creates a new instance. Used when a new Legend needs to be created.
     */
    public LegendCreatorPopup(final LegendTable table) {
        this.nameField = new BorderedField("NAME", RiceHandler.getColour("night"));
        this.colourPicker =
            new ColourPicker("green", "royalblue", "purple", "pink", "teal", "lightred", "skyblue");
        this.table = table;
        this.oldMember = null;
        super(WIDTH, HEIGHT);
    }

    /**
     * Creates a new instance. Used when an existing Legend needs to be edited.
     *
     * @param member        the LegendTableMember to edit
     */
    public LegendCreatorPopup(final LegendTable table, final LegendTableMember member) {
        this.nameField = new BorderedField("NEW NAME", RiceHandler.getColour("night"));
        this.colourPicker =
            new ColourPicker("green", "royalblue", "purple", "pink", "teal", "lightred", "skyblue");
        this.table = table;
        this.oldMember = member;
        super(WIDTH, HEIGHT);
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
        final boolean isEditing = this.oldMember != null;
        if (isEditing) {
            this.nameField.setUserInput(this.oldMember.getLegend().getName());
            this.colourPicker.select(this.oldMember.getLegend().getColour());
        }

        this.colourPicker.setMinHeight(30);

        final FilledButton accept = new FilledButton(
            RiceHandler.getColour("dullblue"), RiceHandler.getColour("dullblue2")) {
            @Override
            public void onMousePressed(MouseEvent event) {
                final String name = nameField.getUserInput();
                final Colour colour = colourPicker.getSelected();
                if (isEditing) {
                    oldMember.getLegend().setName(name);
                    oldMember.getLegend().setColour(colour);
                    oldMember.update();
                } else {
                    final Legend legend = LegendHandler.createLegend(name, colour);
                    table.addMember(legend);
                }
                Popup.despawn();
            }
        };
        accept.setLabel(isEditing ? "Confirm" : "Create");
        accept.setMinSize(80, 30);
        accept.setMaxSize(80, 30);

        VBox.setMargin(this.nameField, new Insets(0, 20, 10, 20));
        VBox.setMargin(this.colourPicker, new Insets(0, 20, 30, 20));

        final VBox container = new VBox();
        container.setBackground(null);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(this.nameField, this.colourPicker, accept);

        content.getChildren().add(container);
    }
}
