package code.frontend.windows;

import code.backend.Countdown;
import code.backend.StorageHandler;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneControls;
import code.frontend.panels.CountdownPaneView;
import code.frontend.panels.DateInputField;
import code.frontend.panels.InputField;
import java.time.LocalDate;
import javafx.stage.Stage;

public class EditWindow extends ConfigWindowTemplate {
    private static EditWindow window = null;

    private static Countdown countdown;

    private EditWindow() {
        super();
        this.setTitle("editing countdown");
    }

    public static Stage getInstance(Countdown countdownToEdit) {
        EditWindow.countdown = countdownToEdit;
        if (window == null) {
            window = new EditWindow();
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
            public void executeOnClick() {
                String newName = getNameField().getTextField().getText();
                LocalDate newDue = getDateField().getLocalDateInput(false);
                StorageHandler.editCountdown(countdown, newName, newDue);
                StorageHandler.save();
                window.close();
                CountdownPaneView.getInstance().repopulate(LocalDate.now());
                CountdownPaneControls.getInstance().deselectAll();
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
