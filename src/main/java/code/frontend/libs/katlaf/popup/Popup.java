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

package code.frontend.libs.katlaf.popup;

import code.frontend.MainContainer;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * A popup is an overlay which fills the entire content of the user's window.
 * The virtual window of the popup should be specified in the constructor.
 * The virtual window is a StackPane which spawns at the centre of the user's window.
 * The extra space which surrounds the popup is a translucent Region with click detection;
 * it will despawn the popup window when click on.
 * <p>
 * Note that, by intentional design, there should only ever be a single Popup window shown
 * at a time. Additionally, a popup window should not be triggered by another popup window.
 * <p>
 * Additionally, the view order of all Popups is set at -1000. Please be mindful not to place
 * anything above it unless you are certain about what you are doing.
 *
 * @since v3.0.0-beta
 */
public abstract class Popup extends Region {
    /*


     STATIC API
    -------------------------------------------------------------------------------------*/
    private static final double VIEW_ORDER = -1000;
    private static final ChangeListener<Number> sizeCl;
    private static final MainContainer mc;
    private static Popup current = null;

    static {
        mc = MainContainer.getInstance();

        sizeCl = new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number>o, Number ov, Number nv) {
                if (current != null) {
                    current.resizeRelocate(0, 0, mc.getWidth(), mc.getHeight());
                }
            };
        };

        mc.widthProperty().addListener(sizeCl);
        mc.heightProperty().addListener(sizeCl);
    }

    public final static <E extends Popup> void spawn(final E popup) {
        if (current != null && popup.getIdent().equals(current.getIdent())) {
            /*
             * this means that the user was not finished with what they were
             * doing; their work should be restored
             */
            current.setVisible(true);
            return;
        } else if (current != null) {
            despawn();
        }
        current = popup;
        current.setViewOrder(VIEW_ORDER);
        current.setManaged(false);
        mc.getChildren().add(current);
        current.resizeRelocate(0, 0, mc.getWidth(), mc.getHeight());
    }

    public final static void despawn() {
        if (current == null)
            return;
        current.setVisible(false);
        MainContainer.getInstance().getChildren().remove(current);
        current = null;
    }

    /*


     FIELDS & CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    private final StackPane content;
    private final BorderPane container;

    public Popup(final double width, final double height) {
        this.setBackground(null);
        this.content = new StackPane();
        this.content.setBackground(RiceHandler.createBG(RiceHandler.getColour("midnight"), 8, 0));
        this.content.setPrefSize(width, height);

        this.container = new BorderPane();
        this.container.setBackground(RiceHandler.createBG(new Color(0, 0, 0, 0.8), 0, 0));
        this.container.setCenter(this.content);
        this.container.setOnMousePressed(event -> {
            this.setVisible(false);
            event.consume();
        });
        this.container.prefWidthProperty().bind(this.widthProperty());
        this.container.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(this.container);

        this.configureContent(this.content);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    /**
     * This method allows Popup implementations to add child nodes to the content as they see
     * fit. Note that before this method is called, the StackPane already has a background
     * and a pref size set, and has been added to a BorderPane container.
     *
     * @see StackPane
     */
    protected abstract void configureContent(final StackPane content);

    /**
     * Implementations should return a (hard-coded) String which serves as an identity,
     * so that, if a Popup with the same ident is hidden (but in use), it will be shown;
     * any inputs that the user might have done will not be lost.
     */
    protected abstract String getIdent();
}
