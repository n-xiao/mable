/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.windows;

import code.backend.Countdown;
import code.frontend.panels.Button;
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
        window = new EditWindow();
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
                countdown.setName(newName);
                countdown.setDueDate(newDue);
                CountdownPaneView cpv = CountdownPaneView.getInstance();
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
