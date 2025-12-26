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
import javafx.scene.text.Font;

public class TextInput extends BorderPane
{
    private TextField textField;
    private CustomBox border;

    public TextInput()
    {
        this.border = new CustomBox(2);
        CustomBox.applyToPane(this, border);
        this.textField = new TextField();
        this.textField.setBackground(null);
        this.textField.setBorder(null);
        this.textField.prefHeightProperty().bind(this.heightProperty());
        this.textField.prefWidthProperty().bind(this.widthProperty());
        this.textField.setFont(new Font(Vals.FontTools.FONT_FAM + " Medium", 13));
        this.textField.setStyle("-fx-text-fill: white");

        BorderPane.setAlignment(this.textField, Pos.CENTER);
        BorderPane.setMargin(this.textField, new Insets(6));
        this.setCenter(this.textField);
    }

    public TextField getTextField()
    {
        return textField;
    }

    public void setTextLimit(int chars)
    {
        MaxLengthFilter mlf = new MaxLengthFilter(chars);
        TextFormatter<String> tf = new TextFormatter<String>(mlf);
        textField.setTextFormatter(tf);
    }

    private class MaxLengthFilter implements UnaryOperator<TextFormatter.Change>
    {
        private int maxLength;

        public MaxLengthFilter(int maxLength)
        {
            this.maxLength = maxLength;
        }

        @Override
        public Change apply(Change t)
        {
            return (t.getControlNewText().length() > maxLength) ? null : t;
        }
    }
}
