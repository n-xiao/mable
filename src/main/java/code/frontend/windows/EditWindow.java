package code.frontend.windows;

import code.frontend.panels.Button;
import code.frontend.panels.DateInputField;
import code.frontend.panels.InputField;

public class EditWindow extends AddWindow {
    @Override
    protected InputField createNameInputField() {
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
    protected Button createButton(String buttonText) {
        return new Button(buttonText) {
            @Override
            public void executeOnClick() {}
        };
    }
}
