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
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.buttons.FilledButton;
import code.frontend.libs.katlaf.graphics.LabelledBorderedRegion;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * This component serves to warn users that they are attempting to delete a
 * non-empty Legend. From this popup, they can either delete all Countdowns
 * within the Legend and delete the Legend, or make them "uncategorised"
 *
 * @since v3.0.0-beta
 */
public class LegendDeletePopup extends Popup {
    private final VBox container;
    private final Legend legend;
    private final LegendTable table;

    /**
     * Creates a new instance.
     *
     * @param legend        the Legend to be deleted
     *
     */
    public LegendDeletePopup(final Legend legend, final LegendTable table) {
        super(200, 150);
        if (legend.getContents().isEmpty())
            throw new IllegalArgumentException(
                "LegendDeletePopup spawning when the legend is empty?! What is this madness?");

        this.container = new VBox();
        this.legend = legend;
        this.table = table;
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected void configureContent(StackPane content) {
        final MableBorder mb = new MableBorder(1.5, 0.2, 0.2);
        final LabelledBorderedRegion border =
            new LabelledBorderedRegion(mb, "CAUTION", RiceHandler.getColour("night"));
        border.setColour(RiceHandler.getColour("red"));

        final String num = Integer.toString(this.legend.getContents().size());
        final Label header = new Label("You are trying to delete \"" + this.legend.getName()
            + "\", a Legend containing " + num + " countdown(s).");
        header.setFont(FontHandler.getHeading(3));
        header.setTextFill(RiceHandler.getColour("white"));
        header.setAlignment(Pos.CENTER);
        header.setTextAlignment(TextAlignment.CENTER);
        final Label info = new Label("Please choose what to do.");
        info.setFont(FontHandler.getNormal());
        info.setTextFill(RiceHandler.getColour("white"));
        info.setAlignment(Pos.CENTER);
        info.setTextAlignment(TextAlignment.CENTER);

        final FilledButton deleteAllButton =
            new FilledButton(RiceHandler.getColour("dullred"), RiceHandler.getColour("red")) {
                @Override
                public void onMousePressed(MouseEvent event) {
                    legend.getContents().forEach(countdown -> countdown.delete());
                    LegendHandler.removeLegend(legend);
                    table.removeMember(legend);
                    Popup.despawn();
                }
            };
        deleteAllButton.setLabel("Delete all Countdowns of this Legend, then delete the Legend");

        final FilledButton keepAllButton =
            new FilledButton(RiceHandler.getColour("darkgrey"), RiceHandler.getColour("black")) {
                @Override
                public void onMousePressed(MouseEvent event) {
                    legend.getContents().clear();
                    LegendHandler.removeLegend(legend);
                    table.removeMember(legend);
                    Popup.despawn();
                }
            };
        keepAllButton.setLabel("Keep all Countdowns, but delete the Legend");

        this.container.setPadding(new Insets(0, 20, 0, 20));
        this.container.setAlignment(Pos.CENTER);
        VBox.setMargin(header, new Insets(0, 0, 10, 0));
        VBox.setMargin(info, new Insets(0, 0, 20, 0));
        VBox.setMargin(deleteAllButton, new Insets(0, 0, 10, 0));
        this.container.getChildren().addAll(header, info, deleteAllButton, keepAllButton);
        content.getChildren().addAll(this.container);
    }

    @Override
    protected String getIdent() {
        return "legend delete";
    }
}
