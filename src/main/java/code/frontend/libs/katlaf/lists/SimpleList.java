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

package code.frontend.libs.katlaf.lists;

import code.frontend.libs.katlaf.graphics.BorderedRegion;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * This facilitates a scrollable, vertical list. It is best suited for a page-selection
 * system, where only one (button) element in the list can be selected at a time.
 */
public class SimpleList extends BorderedRegion {
    private final ArrayList<Listable> listables;
    private final ScrollPane scrollPane;
    private final VBox content;
    private Iterator<Listable> listableIterator;

    public SimpleList(ArrayList<Listable> listables) {
        super(2, 0.25, 0.15);
        this.getCustomBorder().setStrokeColour(RiceHandler.getColour("ghost"));
        this.listables = listables;
        listableIterator = listables.iterator();
        scrollPane = new ScrollPane();
        content = new VBox();
        initScrollPaneStyling();
        initContentStyling();
        scrollPane.setContent(content);
        this.getChildren().add(scrollPane);
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    /*


     STYLING
    -------------------------------------------------------------------------------------*/

    private void initScrollPaneStyling() {
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setBackground(null);
        scrollPane.setMinHeight(200);
        scrollPane.prefWidthProperty().bind(this.widthProperty());
        scrollPane.prefHeightProperty().bind(this.heightProperty());
        scrollPane.setStyle("-fx-background: transparent;");
    }

    private void initContentStyling() {
        content.setFillWidth(true);
        content.setPadding(new Insets(0, 5, 0, 5));
        content.setMaxHeight(Double.MAX_VALUE);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * Removes all members from the
     * content of this {@link SimpleList}.
     */
    protected final void clearContent() {
        content.getChildren().clear();
    }

    /**
     * Increments the internal iterator without adding it
     * to the content, effectively skipping a {@link Listable}
     * while using the Iterator.
     */
    protected final void nextListable() {
        if (hasNextListable())
            listableIterator.next();
    }

    /**
     * @return the ArrayList of {@link Listable} instances of this {@link SimpleList}.
     */
    protected final ArrayList<Listable> getListables() {
        return this.listables;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Resets the iterator.
     */
    public final void resetNextListable() {
        listableIterator = listables.iterator();
    }

    /**
     * A method that exposes the `hasNext()` from {@link Iterator}.
     * @return true if there is a next {@link Listable}, false otherwise.
     */
    public final boolean hasNextListable() {
        return listableIterator.hasNext();
    }

    /**
     * Adds the next {@link Listable} if there is one. If not, nothing happens.
     */
    public final void addNextListable() {
        if (hasNextListable())
            content.getChildren().add(new SimpleListMember(listableIterator.next()));
    }

    /**
     * Clears all children from the content of this {@link SimpleList},
     * then repopulates by iterating through all listables. Note that
     * this method does not utilise {@link Iterator}.
     */
    public final void repopulate() {
        clearContent();
        for (Listable listable : listables)
            content.getChildren().add(new SimpleListMember(listable));
    }

    /**
     * Updates the styling of all {@link SimpleListMember} of this {@link SimpleList}
     * based on evidence from the backend.
     */
    public void updateSelections() {
        content.getChildren().forEach(child -> {
            if (child instanceof SimpleListMember) {
                ((SimpleListMember) child).updateSelection();
            }
        });
    }

    /**
     * Sets the background of the content.
     */
    public void setContentBackground(Background bg) {
        content.setBackground(bg);
    }
}
