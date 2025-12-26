package code.frontend.panels;

import code.frontend.foundation.CustomBox;
import code.frontend.misc.Vals;
import javafx.scene.layout.HBox;

public class DateInputField extends HBox
{
    private CustomBox border;
    private InputField dayInput;
    private InputField monthInput;
    private InputField yearInput;

    public DateInputField()
    {
        border = new CustomBox(Vals.GraphicalUI.INPUT_BORDER_WIDTH);
        CustomBox.applyCustomBorder(this, border);
    }
}
