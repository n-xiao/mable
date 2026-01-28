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
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class InputField extends BorderPane {
    private static ArrayList<InputField> MANUAL_INPUTS = new ArrayList<InputField>();

    public static void escapeAllInputs() {
        MANUAL_INPUTS.forEach((input) -> input.setFocused(false));
    }

    private TextField textField;
    private MableBorder border;
    private InputFilter inputFilter;
    private Color borderColour;
    private Label label = null;

    public InputField() {
        borderColour = RiceHandler.getColour();
        border = new MableBorder(1.8, 0.3, 0.38);
        MableBorder.applyToPane(this, border);
        textField = new TextField();
        textField.setBackground(null);
        textField.setBorder(null);
        textField.setFont(FontHandler.getDedicated(DedicatedFont.USER_INPUT));
        textField.setStyle("-fx-text-fill: white; user-select: none;");
        this.applyFocusLogic();

        inputFilter = new InputFilter();
        TextFormatter<String> tf = new TextFormatter<String>(inputFilter);
        textField.setTextFormatter(tf);

        BorderPane.setAlignment(textField, Pos.CENTER);
        this.setCenter(textField);
        this.setCursor(Cursor.TEXT);
    }

    public void enableManualActivation() {
        this.textField.setFocusTraversable(false);
        this.setOnMousePressed(event -> {
            textField.requestFocus();
            event.consume();
        });
        MANUAL_INPUTS.add(this);
    }

    protected void applyFocusLogic() {
        textField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                borderColour = border.getStrokeColour();
                border.setStrokeColour(RiceHandler.getColour("selected"));
                if (label != null)
                    label.setTextFill(RiceHandler.getColour("selected"));
            } else {
                border.setStrokeColour(borderColour);
                if (label != null)
                    label.setTextFill(borderColour);
            }
        }));
    }

    public TextField getTextField() {
        return textField;
    }

    public void setTextLimit(int chars) {
        inputFilter.maxLength = chars;
    }

    public void setNumInputOnly(boolean numOnly) {
        inputFilter.numOnly = numOnly;
    }

    public void setBorderColour(Color borderColour) {
        this.borderColour = borderColour;
        border.setStrokeColour(borderColour);
    }

    public MableBorder getCustomBorder() {
        return this.border;
    }

    public void setCustomBorder(MableBorder border) {
        this.getChildren().remove(this.border);
        this.border.widthProperty().unbind();
        this.border.heightProperty().unbind();
        this.border = border;
        MableBorder.applyToPane(this, this.border);
    }

    /**
     * (Optional) If there is a label that should be associated with this
     * input, set it here. It is assumed that the border colour and that
     * that label is the same, where the label will be set to the border's
     * colour.
     */
    public void setLabel(Label label) {
        label.setTextFill(borderColour);
        this.label = label;
    }

    private class InputFilter implements UnaryOperator<TextFormatter.Change> {
        private int maxLength;
        private boolean numOnly;

        public InputFilter() {
            this.maxLength = 0;
            this.numOnly = false;
        }

        @Override
        public Change apply(Change t) {
            String text = t.getControlNewText();
            boolean hasNumOnly = text.matches("^-?[0-9]+$");
            boolean valid =
                (maxLength <= 0 || text.length() <= maxLength) && (!numOnly || hasNumOnly)
                || text.isEmpty();
            return (valid) ? t : null;
        }
    }

    public void setFieldMargins(Insets insets) {
        BorderPane.setMargin(textField, insets);
    }
}
