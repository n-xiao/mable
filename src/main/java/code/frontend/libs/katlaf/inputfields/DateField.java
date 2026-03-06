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

import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.FontHandler.DedicatedFont;
import code.frontend.libs.katlaf.graphics.LabelledBorderedRegion;
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public final class DateField extends StackPane {
    private static String format;
    private static final String WARNING;

    static {
        format = "dd-MM-yyyy";
        WARNING = "Sorry, the date you provided doesn't follow the accepted format: "
            + DateField.format.toLowerCase();
    }

    private final Warning warning;
    private final Field dayField;
    private final Field monthField;
    private final Field yearField;

    public DateField() {
        this("DATE", RiceHandler.getColour("night"));
    }

    public DateField(final String title, final Color bgColour) {
        this.warning = new Warning();
        this.dayField = new Field();
        this.monthField = new Field();
        this.yearField = new Field();
        /*
         * setup the fields
         */
        setupFields(this.dayField, this.monthField, this.yearField);
        final String dayFormat = DateField.format.substring(0, 3);
        final String monthFormat = dayFormat.equals("dd") ? "mm" : "dd";
        this.dayField.setFieldPrompt(dayFormat);
        this.monthField.setFieldPrompt(monthFormat);
        this.yearField.setFieldPrompt("yyyy");
        this.yearField.setMaxInputLength(4);
        /*
         * now i'll put everything in them containers
         */
        final HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setBackground(null);
        hbox.getChildren().addAll(
            this.dayField, new Separator(), this.monthField, new Separator(), this.yearField);
        hbox.setFillHeight(true);

        final VBox vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.setBackground(null);
        VBox.setMargin(this.warning, new Insets(10, 0, 0, 0));
        vbox.getChildren().addAll(hbox, this.warning);
        /*
         * and last, the border with everything else
         */
        final LabelledBorderedRegion region =
            new LabelledBorderedRegion(new MableBorder(1.5, 0.2, 0.4), title, bgColour);
        this.getChildren().addAll(region, vbox);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    private static void setupFields(final Field... fields) {
        for (Field field : fields) {
            field.setNumOnly(true);
            field.setMaxInputLength(2);
        }
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * This should only be called on startup.
     */
    public static void useAltFormat() {
        DateField.format = "MM-dd-yyy";
    }

    /**
     * Returns a LocalDate while handling sanitisation. If a LocalDate cannot be parsed,
     * this method will return null. Any backend code calling this method should take
     * that into account.
     *
     * @param boolean       if true, this instance will not show a warning when the
     *                      input fails to parse.
     *
     * @return LocalDate    the date inputted by the user
     */
    public LocalDate getLocalDateInput(final boolean silent) {
        String day = this.dayField.getTextField().getText();
        String month = this.monthField.getTextField().getText();
        final String year = this.yearField.getTextField().getText();

        final DecimalFormat decimalFormat = new DecimalFormat("00");
        day = decimalFormat.format(Integer.parseInt(day));
        month = decimalFormat.format(Integer.parseInt(month));
        final String dateString = day + "-" + month + "-" + year;
        final DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(DateField.format);
        try {
            return LocalDate.parse(dateString, dtFormat);
        } catch (DateTimeParseException e) {
            if (!silent)
                this.warning.spawn();
            return null;
        }
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    /**
     * This represents the slash in between the inputs.
     */
    private class Separator extends StackPane {
        Separator() {
            final Label label = new Label("/");
            label.setAlignment(Pos.CENTER);
            label.setFont(FontHandler.getDedicated(DedicatedFont.USER_INPUT));
            label.setTextFill(RiceHandler.getColour("white"));
        }
    }

    /**
     * This represents the warning text shown when the user enters a
     * date that cannot be parsed and the getLocalDateInput() method is
     * called.
     *
     * @see DateField#getLocalDateInput(boolean)
     */
    private class Warning extends StackPane {
        private final FadeTransition ft;

        Warning() {
            this.ft = new FadeTransition();
            final Label label = new Label(WARNING);
            label.setAlignment(Pos.CENTER);
            label.setFont(FontHandler.getNormal());
            label.setTextFill(RiceHandler.getColour("orange"));
            this.getChildren().add(label);
            this.setMouseTransparent(true);
            this.setOpacity(0);
        }

        void spawn() {
            this.ft.setNode(this);
            this.ft.setDuration(Duration.millis(200));
            this.ft.setFromValue(0);
            this.ft.setToValue(1);
            this.ft.playFromStart();
        }
    }
}
