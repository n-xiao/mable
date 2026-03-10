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
import code.frontend.capabilities.countdowns.CountdownList;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.dragndrop.DragReaction;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.interfaces.Colourable;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.tables.SimpleTableMember;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * This component presents itself as tablet-shaped. Every LegendTableMember
 * should have the same height, but should have variable widths to accomodate
 * text. However, a max width is specified and spillover text is just "..."
 * <p>
 * The layout would be: colour indicator, name, delete button -- surrounded
 * by a MableBorder.
 *
 * @since v3.0.0-beta
 */
class LegendTableMember extends SimpleTableMember implements DragReaction, Colourable {
    static final double HEIGHT = 20;
    static final double MAX_WIDTH = 200;

    private final Legend legend;

    private final Region colourIndicator;
    private final Label label;
    private final StackPane delete;
    private final HBox container;
    private final MableBorder border;

    protected LegendTableMember(
        final Legend legend, final LegendTable table, final CountdownList list) {
        this.setMaxWidth(MAX_WIDTH);
        this.setMaxHeight(HEIGHT);
        this.setMinHeight(HEIGHT);

        this.legend = legend;

        this.colourIndicator = new Region();
        this.colourIndicator.setBackground(RiceHandler.createBG(legend.getColour().get(), 10, 4));
        this.colourIndicator.setMinSize(HEIGHT, HEIGHT);
        this.colourIndicator.setMaxSize(HEIGHT, HEIGHT);

        this.label = new Label(legend.getName());
        this.label.setFont(FontHandler.getNormal());
        this.label.setTextFill(RiceHandler.getColour("white"));
        HBox.setHgrow(this.label, Priority.ALWAYS);
        HBox.setMargin(this.label, new Insets(0, 0, 0, 5));

        this.delete = new StackPane();
        final Label cross = new Label("X");
        cross.setFont(FontHandler.getDedicated(DedicatedFont.SYMBOL_IT));
        cross.setTextFill(RiceHandler.getColour("lightgrey"));
        cross.setMouseTransparent(true);
        this.delete.getChildren().add(cross);
        this.delete.setMaxSize(HEIGHT, HEIGHT);
        this.delete.setMinSize(HEIGHT, HEIGHT);
        this.delete.setOpacity(0.5);
        this.delete.setOnMousePressed(event -> {
            if (this.legend.getContents().isEmpty()) {
                table.removeMember(this.legend);
                LegendHandler.removeLegend(this.legend);
            } else {
                Popup.spawn(new LegendDeletePopup(this.legend, table, list));
            }
        });

        this.container = new HBox();
        this.container.setBackground(null);
        this.container.getChildren().addAll(this.colourIndicator, this.label, this.delete);
        this.container.setAlignment(Pos.CENTER);

        this.border = new MableBorder(0.5, 0.05, 0.9);
        this.border.bindSize(this.widthProperty(), this.heightProperty());
        this.getChildren().addAll(this.container, this.border);

        this.setOnMouseEntered(event -> { this.delete.setOpacity(1); });

        this.setOnMouseExited(event -> { this.delete.setOpacity(0.5); });
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    final Legend getLegend() {
        return this.legend;
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected final StackPane getDeletePane() {
        return this.delete;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void update() {
        this.label.setText(this.legend.getName());
        this.colourIndicator.setBackground(RiceHandler.createBG(legend.getColour().get(), 10, 0.5));
    }

    @Override
    public void onDragEntered() {
        this.setColour("orange");
    }

    @Override
    public void onDragExited() {
        this.resetColour();
    }

    /**
     * This sets the colour of the label and border of this instance.
     * It does not set the colour of the associated Legend.
     */
    @Override
    public void setColour(Color colour) {
        this.label.setTextFill(colour);
        this.border.setColour(colour);
    }

    @Override
    public void resetColour() {
        this.label.setTextFill(RiceHandler.getColour("white"));
        this.border.resetColour();
    }
}
