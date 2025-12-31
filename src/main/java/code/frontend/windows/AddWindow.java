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
import code.backend.StorageHandler;
import code.frontend.panels.Button;
import code.frontend.panels.CountdownPaneView;
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
                CountdownPaneView.getInstance().deselectAll();
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
