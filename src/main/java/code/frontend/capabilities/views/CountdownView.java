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
import code.frontend.capabilities.concurrency.Updatable;
import code.frontend.capabilities.countdowns.CountdownCreateButton;
import code.frontend.capabilities.countdowns.CountdownList;
import code.frontend.capabilities.legends.LegendTable;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseButton;
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
public class CountdownView extends VBox implements Updatable {
    private static final double GLOBAL_PADDING = 10;
    private final CountdownList list;
    private final LegendTable table;

    /**
     * Creates a new instance of a CountdownView.
     *
     * @param title         the String title of this instance
     * @param legends       the Set of Legend instances that should be displayed in this
     *                      CountdownView
     * @param countdowns    the Set of Countdown instances that should be displayed in
     *                      this CountdownView
     */
    public CountdownView(
        final String title, final Set<Legend> legends, final Set<Countdown> countdowns) {
        this.setBackground(null);
        this.setFillWidth(true);
        this.setPadding(new Insets(GLOBAL_PADDING));

        this.list = new CountdownList();

        final ScrollPane listScrollPane = new ScrollPane(this.list);
        listScrollPane.setStyle("-fx-background: transparent;");
        listScrollPane.setBackground(null);
        listScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        listScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        listScrollPane.setFitToWidth(true);
        /*
         * click "anywhere" to deselect
         */
        listScrollPane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                CountdownView.this.list.getSelector().deselectAll();
                event.consume();
            }
        });

        final Bottom bottom = new Bottom();
        bottom.setPrefHeight(20);
        StackPane.setAlignment(bottom, Pos.BOTTOM_CENTER);

        final StackPane listContainer = new StackPane();
        listContainer.getChildren().addAll(listScrollPane, bottom);

        this.table = new LegendTable(this.list);
        this.table.setMinHeight(50);
        VBox.setMargin(this.table, new Insets(20, 0, 0, 0));

        this.getChildren().addAll(new Top(title), this.table, listContainer);

        this.list.populate(countdowns);
        this.table.populate(legends, countdowns);
    }

    /**
     * Creates a new instance without a LegendTable. Typically used when viewing
     * deleted or completed Countdowns, where user edit capabilities are limited.
     *
     * @param title         the String title of this instance
     * @param countdowns    the Set of Countdown instances that should be displayed in
     *                      this CountdownView
     * @param filter        the CountdownFilter which specifies what type of Countdown
     *                      instances should be displayed
     */
    public CountdownView(final String title, final Set<Countdown> countdowns,
        final CountdownList.CountdownFilter filter) {
        this.table = null;
        this.setBackground(null);
        this.setPadding(new Insets(GLOBAL_PADDING));

        this.list = new CountdownList(filter);

        final ScrollPane listScrollPane = new ScrollPane(this.list);
        listScrollPane.setStyle("-fx-background: transparent;");
        listScrollPane.setBackground(null);
        listScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        listScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        listScrollPane.setFitToWidth(true);

        final Bottom bottom = new Bottom();
        bottom.setMinHeight(20);
        bottom.setMaxHeight(20);
        StackPane.setAlignment(bottom, Pos.BOTTOM_CENTER);

        final StackPane listContainer = new StackPane();
        listContainer.getChildren().addAll(listScrollPane, bottom);

        this.getChildren().addAll(new Top(title), listContainer);

        this.list.populate(countdowns);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    @Override
    public void update() {
        this.list.update();
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    /**
     * Contains the title and the Countdown creation button
     */
    private class Top extends BorderPane {
        private static final double TOP_MARGIN = 2;
        private static final double SIDE_MARGIN = 4;

        Top(final String title) {
            this.setBackground(null);

            final Label label = new Label(title);
            label.setFont(FontHandler.getHeading(1));
            label.setTextFill(RiceHandler.getColour("white"));
            label.setMouseTransparent(true);
            BorderPane.setMargin(label, new Insets(TOP_MARGIN, 0, 0, SIDE_MARGIN));
            this.setLeft(label);

            final CountdownCreateButton button = new CountdownCreateButton(CountdownView.this.list);
            button.setMinWidth(40);
            BorderPane.setMargin(button, new Insets(TOP_MARGIN, SIDE_MARGIN, 0, 0));
            this.setRight(button);
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
            this.setMaxWidth(Double.MAX_VALUE);
        }
    }
}
