/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.capabilities.countdown.windows;

import code.backend.data.Countdown;
import code.backend.utils.CountdownHandler;
import code.frontend.capabilities.countdown.components.CountdownTable;
import code.frontend.libs.katlaf.buttons.Button;
import code.frontend.libs.katlaf.windows.CountdownModifier;
import java.time.LocalDate;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CountdownCreator extends CountdownModifier {
    private static CountdownCreator window = null;

    private CountdownCreator() {
        super();
        this.setTitle("creating countdown");
    }

    public static Stage getInstance() {
        if (window == null) {
            window = new CountdownCreator();
            window.setOnHidden(event -> { window = null; });
        }
        window.show();
        window.toFront();
        return window;
    }

    @Override
    protected Button createButton() {
        return new Button("create") {
            @Override
            public void executeOnClick(MouseEvent event) {
                String name = getNameField().getTextField().getText();
                LocalDate due = getDateField().getLocalDateInput(false);
                if (due == null)
                    return;
                CountdownHandler.addCountdown(new Countdown(name, due));
                CountdownTable.getInstance().repopulate(LocalDate.now());
                CountdownTable.getInstance().deselectAll();
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
