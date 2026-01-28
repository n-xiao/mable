/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.capabilities.countdown.windows;

import code.backend.data.Countdown;
import code.frontend.capabilities.countdown.components.CountdownTable;
import code.frontend.libs.katlaf.buttons.Button;
import code.frontend.libs.katlaf.inputfields.DateInputField;
import code.frontend.libs.katlaf.inputfields.InputField;
import code.frontend.libs.katlaf.windows.CountdownModifier;
import java.time.LocalDate;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CountdownEditor extends CountdownModifier {
    private static CountdownEditor window = null;

    private static Countdown countdown;

    private CountdownEditor() {
        super();
        this.setTitle("editing countdown");
    }

    public static Stage getInstance(Countdown countdownToEdit) {
        if (window == null) {
            CountdownEditor.countdown = countdownToEdit;
            window = new CountdownEditor();
            window.setOnHidden(event -> { window = null; });
        }
        window.show();
        window.toFront();
        return window;
    }

    @Override
    protected InputField createNameInputField() {
        InputField input = new InputField();
        input.getTextField().setText(countdown.getName());
        return input;
    }

    @Override
    protected DateInputField createDateInputField() {
        DateInputField input = new DateInputField();
        input.setLocalDateInput(countdown.getLocalDueDate(LocalDate.now()));
        return input;
    }

    @Override
    protected InputField createDaysInputField() {
        InputField input = new InputField();
        input.getTextField().setText(Integer.toString(countdown.daysUntilDue(LocalDate.now())));
        return input;
    }

    @Override
    protected Button createButton() {
        return new Button("confirm edits") {
            @Override
            public void executeOnClick(MouseEvent event) {
                String newName = getNameField().getTextField().getText();
                LocalDate newDue = getDateField().getLocalDateInput(false);
                countdown.setName(newName);
                countdown.setDueDate(newDue);
                CountdownTable cpv = CountdownTable.getInstance();
                cpv.repopulate(LocalDate.now());
                cpv.deselectAll();
                window.close();
                window = null;
            }
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
