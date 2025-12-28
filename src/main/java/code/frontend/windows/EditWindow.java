package code.frontend.windows;

import code.backend.Countdown;
import code.frontend.panels.Button;
import code.frontend.panels.DateInputField;
import code.frontend.panels.InputField;

public class EditWindow extends ConfigWindowTemplate {
    private Countdown countdown;

    @Override
    protected InputField createNameInputField() {
        InputField input = new InputField();
        input.getTextField().setText("");
        return new InputField();
    }

    @Override
    protected DateInputField createDateInputField() {
        return new DateInputField();
    }

    @Override
    protected InputField createDaysInputField() {
        return new InputField();
    }

    @Override
    protected Button createButton() {
        return new Button("confirm edits") {
            @Override
            public void executeOnClick() {}
        };
    }

    @Override
    protected String getNameInputuserHint() {
        return "its new name shall be...";
    }

    @Override
    protected String getDateInputuserHint() {
        return "its new due date is...";
    }
}
