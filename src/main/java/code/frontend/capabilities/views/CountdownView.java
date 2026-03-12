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
import code.frontend.capabilities.countdowns.CountdownList.CountdownFilter;
import code.frontend.capabilities.legends.LegendTable;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
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
public abstract class CountdownView extends VBox implements Updatable {
    private final CountdownList list;
    private final LegendTable table;

    /**
     * Creates a new instance of a CountdownView.
     *
     * @param title         the String title of this instance
     * @param legends       the Set of Legend instances that should be displayed in this
     *                      CountdownView
     */
    public CountdownView(final String title, final Set<Legend> legends) {
        this.list = new CountdownList();
        this.table = new LegendTable(this.list);
        this.table.setMinHeight(50);
        VBox.setMargin(this.table, new Insets(20, 7, 0, 7));

        final StackPane listContainer = this.initListContainer(CountdownFilter.ONGOING);

        final Top top = new Top(title);
        VBox.setMargin(top, new Insets(0, 7, 10, 7));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(top, listContainer, new Group(this.table));

        this.list.populate(getCountdowns());
        this.table.populate(legends, getCountdowns());

        this.initListeners();
    }

    /**
     * Creates a new instance without a LegendTable. Typically used when viewing
     * deleted or completed Countdowns, where user edit capabilities are limited.
     *
     * @param title         the String title of this instance
     * @param filter        the CountdownFilter which specifies what type of Countdown
     *                      instances should be displayed
     */
    public CountdownView(final String title, final CountdownList.CountdownFilter filter) {
        this.list = new CountdownList(filter);
        this.table = null;

        final StackPane listContainer = this.initListContainer(filter);

        final Top top = new Top(title);
        VBox.setMargin(top, new Insets(0, 7, 10, 7));
        if (filter == CountdownFilter.DELETED || filter == CountdownFilter.COMPLETED)
            top.setRight(null); // remove the button
        this.getChildren().addAll(top, listContainer);

        this.list.populate(getCountdowns());

        this.initListeners();
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private StackPane initListContainer(final CountdownFilter filter) {
        this.setBackground(null);
        this.setFillWidth(true);
        this.setPadding(new Insets(10, 0, 10, 0));

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
            CountdownView.this.list.getSelector().deselectAll();
            event.consume();
        });

        final Bottom bottom = new Bottom();
        bottom.setPrefHeight(20);
        StackPane.setAlignment(bottom, Pos.BOTTOM_CENTER);

        final StackPane listContainer = new StackPane();
        listContainer.getChildren().addAll(listScrollPane, bottom);
        VBox.setVgrow(listContainer, Priority.ALWAYS);

        if (filter == CountdownFilter.DELETED) {
            final Label hint =
                new Label("Countdowns here will be permanently deleted when you close the app.");
            hint.setFont(FontHandler.getItalic());
            hint.setTextFill(RiceHandler.getColour("grey"));
            hint.setAlignment(Pos.CENTER);
            hint.setMouseTransparent(true);
            StackPane.setAlignment(hint, Pos.BOTTOM_CENTER);
            StackPane.setMargin(hint, new Insets(0, 0, 2, 0));
            listContainer.getChildren().add(hint);
        }

        return listContainer;
    }

    private void initListeners() {
        this.widthProperty().addListener(
            (observable, oldValue, newValue) -> { this.list.populate(getCountdowns()); });

        this.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.booleanValue()) {
                this.list.populate(getCountdowns());
                update();
            }
        });
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    @Override
    public synchronized void update() {
        this.list.update();
    }

    public abstract Set<Countdown> getCountdowns();

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    /**
     * Contains the title and the Countdown creation button
     */
    private class Top extends Title {
        Top(final String text) {
            super(text);

            final CountdownCreateButton button = new CountdownCreateButton(CountdownView.this.list);
            button.setMinWidth(50);
            BorderPane.setMargin(button, new Insets(Title.TOP_MARGIN, Title.SIDE_MARGIN, 0, 0));
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
                new LinearGradient(0, 1, 0, 0.96, true, CycleMethod.NO_CYCLE, stops);
            final BackgroundFill fill = new BackgroundFill(gradient, null, null);
            this.setBackground(new Background(fill));
            this.setMaxWidth(Double.MAX_VALUE);
        }
    }
}
