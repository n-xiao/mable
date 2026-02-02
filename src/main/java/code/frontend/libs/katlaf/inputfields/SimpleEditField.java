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

package code.frontend.libs.katlaf.inputfields;

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.buttons.BasicButton;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * A simple edit field that has one accept and one decline button,
 * located directly below the {@link InputField}.
 *
 * This UI component manages its own removal; its parent
 * should be provided so that {@link SimpleEditField}
 * is able to remove itself.
 */
public class SimpleEditField extends Region {
    public SimpleEditField(Pane parent) {
        this.parent = parent;
        this.field = new EditField();
        this.errorLabel = new ErrorLabel();
        final Container container = new Container();
        container.getChildren().addAll(this.errorLabel, this.field, new ButtonContainer());
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/
    private final Pane parent;

    /**
     * Removes this {@link SimpleEditField} instance from
     * the parent Node.
     */
    private void removeSelf() {
        parent.getChildren().remove(this);
    }

    /**
     * This is the private method which is called when the user
     * confirms their current input. A successful execution
     * depends on the validateInput method. Note that this
     * method does nothing to handle errors, since all error handling
     * should be done by (overriding) the validateInput method.
     */
    private void confirmWith(String input) {
        if (validateInput(input)) {
            onSuccess(input);
            removeSelf();
        }
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/
    private final ErrorLabel errorLabel;

    /**
     * This method displays the provided error
     * message to the user.
     */
    public final void displayError(String error) {
        errorLabel.setText(error);
        errorLabel.fadeIn();
    }

    /**
     * This method enables child classes to implement their own input
     * validation. By default, this method only returns true; it will
     * accept any validation. You should call the displayError method
     * when returning false if you're feeling particularly kind.
     */
    public boolean validateInput(String input) {
        return true;
    }

    /**
     * This method enables child classes to implement their own
     * behaviour(s) to be executed whenever the user types
     * something. Its default implementation does nothing.
     */
    public void onInput(String input) {
        // to be overrided
    }

    /**
     * This method enables child classes to implement their own
     * behaviour(s) to be executed whenever the user confirms
     * their valid input. The default implementation does nothing.
     */
    public void onSuccess(String input) {
        // to be overrided
    }

    /*


     STYLING
    -------------------------------------------------------------------------------------*/

    /**
     * This method is used to apply the same styling to both
     * {@link AcceptButton} and {@link CancelButton}.
     */
    private static void styleButton(BasicButton button) {
        button.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(button, Priority.SOMETIMES); // hopefully be evenly distributed
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/
    private final EditField field;

    private class EditField extends InputField {
        private EditField() {
            super();
            this.getTextField().setOnKeyTyped((event) -> {
                SimpleEditField.this.errorLabel.fadeOut();
                onInput(event.getText());
            });
            this.getTextField().setOnAction(
                (event) -> { confirmWith(this.getTextField().getText()); });

            VBox.setMargin(this, new Insets(5, 0, 0, 0));
        }
    }

    private class AcceptButton extends BasicButton {
        private AcceptButton() {
            super("✓");
            styleButton(this);
        }
        @Override
        public void executeOnClick(MouseEvent event) {
            final String input = SimpleEditField.this.field.getTextField().getText();
            confirmWith(input);
        }
    }

    private class CancelButton extends BasicButton {
        private CancelButton() {
            super("X");
            styleButton(this);
        }
        @Override
        public void executeOnClick(MouseEvent event) {
            SimpleEditField.this.removeSelf();
        }
    }

    private class ButtonContainer extends HBox {
        private ButtonContainer() {
            this.setBackground(null);
            this.setFillHeight(true);
            this.prefWidthProperty().bind(SimpleEditField.this.widthProperty());
            this.minHeight(20);
            final AcceptButton accept = new AcceptButton();
            final CancelButton cancel = new CancelButton();
            this.getChildren().addAll(cancel, accept);
        }
    }

    private class ErrorLabel extends Label {
        private final FadeTransition transition;

        private ErrorLabel() {
            this.setBackground(null);
            this.setMaxWidth(Double.MAX_VALUE);
            this.setFont(FontHandler.getSubtitle());
            this.setTextAlignment(TextAlignment.LEFT);
            this.setTextFill(RiceHandler.getColour("error"));
            this.setOpacity(0);
            this.transition = new FadeTransition();
            this.transition.setNode(this);
        }

        private void fadeIn() {
            transition.setFromValue(0);
            transition.setToValue(1);
            transition.setDuration(Duration.millis(300));
            transition.playFromStart();
        }

        private void fadeOut() {
            transition.setFromValue(1);
            transition.setToValue(0);
            transition.setDuration(Duration.millis(100));
            transition.playFromStart();
        }
    }

    /**
     * This is the main container of a {@link SimpleEditField}.
     */
    private class Container extends VBox {
        private Container() {
            this.setBackground(null);
            this.prefWidthProperty().bind(SimpleEditField.this.widthProperty());
            this.prefHeightProperty().bind(SimpleEditField.this.heightProperty());
            this.setFillWidth(true);
        }
    }
}
