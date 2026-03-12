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

package code.frontend.libs.katlaf.buttons;

import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.transitions.Transitioner;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * This represents a basic on/off toggle. Intended to be used in a settings
 * menu.
 *
 * @since v3.0.0-beta
 */
public class Toggle extends ButtonFoundation {
    protected static double WIDTH = 37;
    protected static double HEIGHT = 15;
    protected static Color ENABLED_COLOUR = RiceHandler.getColour("royalblue");
    protected static Color DISABLED_COLOUR = RiceHandler.getColour("black");

    private final SimpleBooleanProperty isOnline;
    private final TranslateTransition boxTranslate;
    private final FadeTransition bgFade;
    private final PauseTransition pause;
    private final Transitioner transitioner;

    private Runnable runOnceOnline;
    private Runnable runOnceOffline;

    public Toggle() {
        this.setMinSize(WIDTH, HEIGHT);
        this.setMaxSize(WIDTH, HEIGHT);

        final BorderStroke stroke = new BorderStroke(RiceHandler.getColour("lightgrey"),
            BorderStrokeStyle.SOLID, new CornerRadii(7), new BorderWidths(1.5));
        this.setBorder(new Border(stroke));
        this.setBackground(RiceHandler.createBG(DISABLED_COLOUR, 7, 0));

        final Region bgRegion = new Region();
        bgRegion.setBackground(RiceHandler.createBG(ENABLED_COLOUR, 7, 0));
        bgRegion.setOpacity(0);

        final Region box = new Region();
        box.setBackground(RiceHandler.createBG(RiceHandler.getColour("white"), 4, 2));
        box.setMinSize(HEIGHT + 4, HEIGHT - 2);
        box.setMaxSize(HEIGHT + 4, HEIGHT - 2);
        StackPane.setAlignment(box, Pos.CENTER_LEFT);
        StackPane.setMargin(box, new Insets(2, 0, 2, 0));

        this.getChildren().addAll(bgRegion, box);

        this.isOnline = new SimpleBooleanProperty(false);
        this.boxTranslate = new TranslateTransition(Duration.millis(300), box);
        this.boxTranslate.setInterpolator(Interpolator.EASE_BOTH);
        this.bgFade = new FadeTransition(Duration.millis(150), bgRegion);
        this.pause = new PauseTransition(Duration.millis(150));
        final SequentialTransition sequential = new SequentialTransition(this.pause, this.bgFade);
        this.transitioner =
            new Transitioner().prepare().playParallel(sequential, this.boxTranslate);

        this.isOnline.addListener((o, ov, newValue) -> {
            if (newValue.booleanValue() && this.runOnceOnline != null)
                this.runOnceOnline.run();
            else if (!newValue.booleanValue() && this.runOnceOffline != null)
                this.runOnceOffline.run();
        });

        this.runOnceOnline = null;
        this.runOnceOffline = null;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    @Override
    public final void onMousePressed(MouseEvent event) {
        this.toggle();
    }

    public final void toggle() {
        this.transitioner.getTransition().stop();
        if (this.isOnline.get()) {
            this.boxTranslate.setFromX(WIDTH - HEIGHT - 7);
            this.boxTranslate.setToX(0);
            this.bgFade.setToValue(0);
        } else {
            this.boxTranslate.setFromX(0);
            this.boxTranslate.setToX(WIDTH - HEIGHT - 7);
            this.bgFade.setToValue(1);
        }
        this.transitioner.getTransition().play();
        this.isOnline.set(!this.isOnline.get());
    }

    /**
     * The provided Runnable is run when this instance is pressed,
     * and moved from the offline position to the online position.
     *
     * @param runnable      the Runnable that should be run
     *
     * @see Runnable
     */
    public final void setRunOnceOnline(final Runnable runnable) {
        this.runOnceOnline = runnable;
    }

    /**
     * The provided Runnable is run when this instance is pressed,
     * and moved from the online position to the offline position.
     *
     * @param runnable      the Runnable that should be run
     *
     * @see Runnable
     */
    public final void setRunOnceOffline(final Runnable runnable) {
        this.runOnceOffline = runnable;
    }
}
