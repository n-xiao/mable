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

package code.frontend.libs.katlaf.options;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.buttons.Toggle;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * This UI component represents a box containing a title, (optional) subtitle and
 * a Toggle instance. It is intended to be used in a user settings or
 * user preferences page, where a user can enable or disable certain features.
 *
 * @since v3.0.0-beta
 *
 * @see Toggle
 */
public final class BooleanOption extends HBox {
    private final Label title;
    private final Label subtitle;
    private final VBox textBox;
    private final Label warning;
    private final Toggle toggle;
    private final FadeTransition warningFade;

    public BooleanOption(final String title) {
        this.title = new Label(title);
        this.title.setTextFill(RiceHandler.getColour("white"));
        this.title.setFont(FontHandler.getHeading(2));

        this.subtitle = new Label();
        this.subtitle.setTextFill(RiceHandler.getColour("grey2"));
        this.subtitle.setFont(FontHandler.getNormal());

        this.warning = new Label();
        this.warning.setTextFill(RiceHandler.getColour("orange"));
        this.warning.setFont(FontHandler.getMono());

        this.textBox = new VBox(this.title, this.subtitle);
        this.textBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        final var spacer = new Region();
        spacer.setVisible(false);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.toggle = new Toggle();
        final StackPane toggleContainer = new StackPane(this.toggle);
        toggleContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        this.setAlignment(Pos.CENTER);
        this.setFillHeight(true);
        this.setPadding(new Insets(8, 5, 8, 5));
        this.getChildren().addAll(this.textBox, spacer, toggleContainer);

        this.warningFade = new FadeTransition(Duration.millis(200), this.warning);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public Label getTitle() {
        return title;
    }

    public Label getSubtitle() {
        return subtitle;
    }

    public Label getWarning() {
        return warning;
    }

    public Toggle getToggle() {
        return toggle;
    }

    public void showWarning() {
        this.warningFade.setOnFinished(null);
        this.warning.setOpacity(0);
        this.textBox.getChildren().add(this.warning);
        this.warningFade.setFromValue(0);
        this.warningFade.setToValue(1);
        this.warningFade.playFromStart();
    }

    public void hideWarning() {
        this.warningFade.setOnFinished(f -> { this.textBox.getChildren().remove(this.warning); });
        this.warningFade.setFromValue(1);
        this.warningFade.setToValue(0);
        this.warningFade.playFromStart();
    }
}
