package code.frontend.windows;

import code.backend.Countdown;
import code.backend.StorageHandler;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneView;
import code.frontend.panels.DateInputField;
import code.frontend.panels.InputField;
import java.time.LocalDate;
import javafx.stage.Stage;

public class AddWindow extends ConfigWindowTemplate {
    private static AddWindow window = null;

    private AddWindow() {
        super();
        this.setTitle("creating countdown");
    }

    public static Stage getInstance() {
        if (window == null) {
            window = new AddWindow();
        }
        window.show();
        window.toFront();
        return window;
    }

    @Override
    protected Button createButton() {
        return new Button("create") {
            @Override
            public void executeOnClick() {
                String name = getNameField().getTextField().getText();
                LocalDate due = getDateField().getLocalDateInput(false);
                if (due == null)
                    return;
                StorageHandler.addCountdown(new Countdown(name, due));
                CountdownPaneView.getInstance().repopulate(LocalDate.now());
                StorageHandler.save();
                window.close();
                window = null;
            }
        };
    }

    @Override
    protected String getNameInputuserHint() {
        return "its name is...";
    }

    @Override
    protected String getDateInputuserHint() {
        return "it is due on...";
    }
}
