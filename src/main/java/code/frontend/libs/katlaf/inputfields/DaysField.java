/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.libs.katlaf.inputfields;

import code.frontend.libs.katlaf.graphics.LabelledBorderedRegion;
import code.frontend.libs.katlaf.graphics.LabelledBorderedRegion.Loc;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public final class DaysField extends StackPane {
    private final Field field;

    public DaysField() {
        this(RiceHandler.getColour("night"));
    }

    public DaysField(final Color bgColour) {
        this.field = new Field();
        this.field.setFieldAlignment(Pos.CENTER);
        this.field.setNumOnly(true);
        StackPane.setMargin(this.field, new Insets(1.5));

        final LabelledBorderedRegion region =
            new LabelledBorderedRegion(new MableBorder(1.5, 0.2, 0.4), "DAYS", bgColour);

        this.getChildren().addAll(region, this.field);

        region.setMouseTransparent(true);
        region.setLocation(Loc.BOTTOM_RIGHT);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public void linkDateField(final DateField dateField) {
        this.field.getTextField().textProperty().addListener((o, ov, nv) -> {
            if (!this.field.getTextField().isFocused())
                return;
            final LocalDate date;
            try {
                date = LocalDate.now().plusDays(Integer.parseInt(nv));
            } catch (Exception e) {
                /*
                 * This would either be from a NumberFormatException from the
                 * Integer.parseInt or a DateTimeException from the plusDays()
                 * method. Both of which can be chalked up to user error.
                 */
                return;
            }
            dateField.setLocalDateInput(date);
        });
    }

    public void setUserInput(final String text) {
        this.field.getTextField().setText(text);
    }
}
