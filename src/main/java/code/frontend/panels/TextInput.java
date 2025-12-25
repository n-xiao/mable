package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class TextInput extends Pane
{
    private TextField textField;
    private CustomBox border;
    private Label label;

    public TextInput()
    {
        this.border = new CustomBox();
        this.textField = new TextField();
        this.textField.setBackground(null);
        this.textField.setBorder(null);
        this.label = new Label();

        this.getChildren().addAll(border, label, textField);
    }
}
