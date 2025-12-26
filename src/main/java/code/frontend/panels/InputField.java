package code.frontend.panels;

import java.util.function.UnaryOperator;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InputField extends BorderPane
{
    private TextField textField;
    private CustomBox border;
    private InputFilter inputFilter;
    private Color borderColour;

    public InputField()
    {
        borderColour = Color.WHITE;
        border = new CustomBox(Vals.GraphicalUI.INPUT_BORDER_WIDTH);
        CustomBox.applyCustomBorder(this, border);
        textField = new TextField();
        textField.setBackground(null);
        textField.setBorder(null);
        textField.prefHeightProperty().bind(this.heightProperty());
        textField.prefWidthProperty().bind(this.widthProperty());
        textField.setFont(new Font(Vals.FontTools.FONT_FAM + " Medium", 13));
        textField.setStyle("-fx-text-fill: white; user-select: none;");
        this.applyFocusLogic();

        inputFilter = new InputFilter();
        TextFormatter<String> tf = new TextFormatter<String>(inputFilter);
        textField.setTextFormatter(tf);

        BorderPane.setAlignment(textField, Pos.CENTER);
        BorderPane.setMargin(textField, new Insets(6));
        this.setCenter(textField);
    }

    private void applyFocusLogic()
    {
        textField.focusedProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue)
                {
                    borderColour = border.getStrokeColour();
                    border.setStrokeColour(Vals.Colour.SELECTED);
                }
            else
                {
                    border.setStrokeColour(borderColour);
                }
        }));
    }

    public TextField getTextField()
    {
        return textField;
    }

    public void setTextLimit(int chars)
    {
        inputFilter.maxLength = chars;
    }

    public void setNumInputOnly(boolean numOnly)
    {
        inputFilter.numOnly = numOnly;
    }

    public void setBorderColour(Color borderColour)
    {
        this.borderColour = borderColour;
        border.setStrokeColour(borderColour);
    }

    private class InputFilter implements UnaryOperator<TextFormatter.Change>
    {
        private int maxLength;
        private boolean numOnly;

        public InputFilter()
        {
            this.maxLength = 0;
            this.numOnly = false;
        }

        @Override
        public Change apply(Change t)
        {
            String text = t.getControlNewText();
            boolean hasNumOnly = text.matches("^[0-9]+$");
            // boolean invalid = (maxLength > 0 && text.length() > maxLength) || (numOnly && !hasNumOnly);
            boolean valid = (maxLength <= 0 || text.length() <= maxLength) && (!numOnly || hasNumOnly);
            return (valid) ? t : null;
        }
    }
}
