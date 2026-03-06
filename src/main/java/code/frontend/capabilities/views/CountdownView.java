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

package code.frontend.capabilities.views;

import code.backend.data.Countdown;
import code.backend.data.Legend;
import code.frontend.capabilities.countdowns.CountdownList;
import code.frontend.capabilities.legends.LegendTable;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * This is a combination of a LegendTable instance and a CountdownList instance
 * within a single container.
 *
 * @see LegendTable
 * @see CountdownList
 *
 * @since v3.0.0-beta
 */
public class CountdownView extends VBox {
    private final CountdownList list;
    private final LegendTable table;

    /**
     * Creates a new instance of a CountdownView.
     *
     * @param legends       the List of Legend instances that should be displayed in this
     *                      CountdownView
     * @param countdowns    the List of Countdown instances that should be displayed in
     *                      this CountdownView
     */
    public CountdownView(final List<Legend> legends, final List<Countdown> countdowns) {
        this.setBackground(null);

        this.list = new CountdownList();

        final ScrollPane listScrollPane = new ScrollPane(this.list);
        listScrollPane.setStyle("-fx-background: transparent;");
        listScrollPane.setBackground(null);
        listScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        listScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        listScrollPane.setFitToWidth(true);

        final Bottom bottom = new Bottom();
        bottom.setPrefHeight(20);
        StackPane.setAlignment(bottom, Pos.BOTTOM_CENTER);

        StackPane listContainer = new StackPane();
        listContainer.getChildren().addAll(listScrollPane, bottom);

        this.table = new LegendTable(this.list);

        this.getChildren().addAll(this.table, listContainer);

        this.list.populate(countdowns);
        this.table.populate(legends, countdowns);
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    /**
     * Contains the title and the Countdown creation button
     */
    private class Top extends BorderPane {
        Top(final String title) {
            final Label label = new Label(title);
            label.setFont(FontHandler.getHeading(1));
            label.setTextFill(RiceHandler.getColour("white"));
        }
    }

    /**
     * Creates the fade out effect at the bottom of the CountdownView to hint
     * that the CountdownList is scrollable (and continues beyond the available
     * height).
     */
    private class Bottom extends Region {
        Bottom() {
            this.setMouseTransparent(true);
            final Stop[] stops = {
                new Stop(0, RiceHandler.getColour("night")), new Stop(1, Color.color(0, 0, 0, 0))};
            final LinearGradient gradient =
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
            final BackgroundFill fill = new BackgroundFill(gradient, null, null);
            this.setBackground(new Background(fill));
            this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }
}
