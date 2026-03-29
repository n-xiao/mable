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

package code.frontend.capabilities.countdowns;

import code.backend.data.Countdown;
import code.backend.data.CountdownHandler;
import code.backend.data.Legend;
import code.backend.data.LegendHandler;
import code.backend.processing.NatLangProcessor;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.inputfields.Field;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import code.frontend.libs.katlaf.transitions.Transitioner;
import java.time.LocalDate;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * A modified text field which allows users to quickly create a Countdown
 * using natural language.
 *
 * @since v3.1.0
 * @see Field
 */
public final class CountdownQuickCreate extends StackPane {
    private final Field field;
    private final CountdownList list;
    private final StatusIndicator status;

    public CountdownQuickCreate(final CountdownList list) {
        this.field = new Field();
        this.field.setFieldPrompt("> what's your next countdown?");
        this.field.getTextField().setOnAction(event -> {
            this.field.setDisable(true);
            this.onAction();
            this.field.setDisable(false);
        });
        this.field.getTextField().setFont(FontHandler.getMono(12));
        this.field.getTextField().setFocusTraversable(false);
        this.field.getTextField().setStyle(
            "-fx-prompt-text-fill: " + RiceHandler.getColourString("grey") + ";"
            + "-fx-text-fill: " + RiceHandler.getColourString("white") + ";");
        this.list = list;

        this.field.setBackground(
            RiceHandler.createBG(RiceHandler.adjustOpacity(RiceHandler.getColour("black"), 0.9),
                new CornerRadii(8), new Insets(-4, 0.5, -4, -4)));
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        this.status = new StatusIndicator();
        VBox.setMargin(this.status, new Insets(0, 3, 5, 0));

        this.getChildren().add(new VBox(this.status, this.field));
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private void onAction() {
        final String text = this.field.getTextField().getText();
        final NatLangProcessor processor = new NatLangProcessor(text);
        final LocalDate date = processor.extractLocalDate(true);
        final String name = processor.extractTopicPhrase();

        final Countdown countdown;
        if (date != null && !name.isBlank()) {
            countdown = CountdownHandler.create(name, date);
            list.addMember(new CountdownListMember(countdown, list));
            this.field.setFieldText("");
        } else {
            this.status.showFailure();
            return;
        }

        /*
         * auto-detect keyword to place countdown in relevant legend
         */
        final String[] words = text.split(" ");
        for (String word : words) {
            final Legend legend = LegendHandler.findLegend(word, false);
            if (legend != null) {
                countdown.moveToLegend(legend);
            }
        }

        this.status.showSuccess();
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class StatusIndicator extends BorderPane {
        static final String SUCCESS = "Countdown added!";
        static final String FAILURE = "can't understand that :/";

        final FadeTransition fade;
        final Transition transition;
        final Label label;

        StatusIndicator() {
            this.fade = new FadeTransition();
            this.label = new Label();
            this.label.setFont(FontHandler.getMono(11));
            this.label.setOpacity(0);
            this.label.setAlignment(Pos.CENTER_RIGHT);
            this.setRight(this.label);
            this.fade.setNode(this.label);
            this.transition = new Transitioner()
                                  .prepare()
                                  .hold(Duration.millis(3000))
                                  .play(this.fade)
                                  .getTransition();
        }

        void showSuccess() {
            this.transition.stop();
            this.label.setOpacity(1);
            this.label.setText(SUCCESS);
            this.label.setTextFill(RiceHandler.getColour("lightgreen"));
            this.fade.setFromValue(1);
            this.fade.setToValue(0);
            this.transition.play();
        }

        void showFailure() {
            this.transition.stop();
            this.label.setOpacity(1);
            this.label.setText(FAILURE);
            this.label.setTextFill(RiceHandler.getColour("red"));
            this.fade.setFromValue(1);
            this.fade.setToValue(0);
            this.transition.play();
        }
    }
}
