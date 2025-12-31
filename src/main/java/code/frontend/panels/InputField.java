/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InputField extends BorderPane {
    private TextField textField;
    private CustomBox border;
    private InputFilter inputFilter;
    private Color borderColour;
    private Label label = null;

    public InputField() {
        borderColour = Color.WHITE;
        border = new CustomBox(Vals.GraphicalUI.INPUT_BORDER_WIDTH);
        CustomBox.applyCustomBorder(this, border);
        textField = new TextField();
        textField.setBackground(null);
        textField.setBorder(null);
        // textField.prefHeightProperty().bind(this.heightProperty());
        // textField.prefWidthProperty().bind(this.widthProperty());
        textField.setFont(new Font(Vals.FontTools.FONT_FAM + " Medium", 15));
        textField.setStyle("-fx-text-fill: white; user-select: none;");
        this.applyFocusLogic();

        inputFilter = new InputFilter();
        TextFormatter<String> tf = new TextFormatter<String>(inputFilter);
        textField.setTextFormatter(tf);

        BorderPane.setAlignment(textField, Pos.CENTER);
        BorderPane.setMargin(textField, new Insets(12, 12, 12, 12));
        this.setCenter(textField);
    }

    private void applyFocusLogic() {
        textField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                borderColour = border.getStrokeColour();
                border.setStrokeColour(Vals.Colour.SELECTED);
                if (label != null)
                    label.setTextFill(Vals.Colour.SELECTED);
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
            // boolean invalid = (maxLength > 0 && text.length() > maxLength) || (numOnly &&
            // !hasNumOnly);
            boolean valid =
                (maxLength <= 0 || text.length() <= maxLength) && (!numOnly || hasNumOnly)
                || text.isEmpty();
            return (valid) ? t : null;
        }
    }
}
