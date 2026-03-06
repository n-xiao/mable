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
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * This class is a wrapper class for javafx.scene.control.TextField
 * It implements Mable's font style and disables the default appearance
 * of the TextField. It also provides other functionalities, such as
 * filtering based on maximum character length and a numerical-only
 * filter. The internal TextField is a child of this Region, and the
 * size of the TextField is bound to the size of this Region.
 *
 * @since v3.0.0-beta
 * @see TextField
 */
public final class Field extends StackPane {
    private final TextField textField;

    public Field() {
        this.setBackground(null);
        this.setCursor(Cursor.TEXT);
        // textField setup
        this.textField = new TextField();
        this.textField.setBackground(null);
        this.textField.setBorder(null);
        this.textField.setFont(FontHandler.getDedicated(DedicatedFont.USER_INPUT));
        this.textField.setStyle(
            "-fx-text-fill: " + RiceHandler.getColourString("white") + "; user-select: none;");
        this.textField.setFocusTraversable(false);
        this.getChildren().add(this.textField);
        StackPane.setMargin(this.textField, new Insets(6, 2, 6, 2));

        // filter setup
        this.filter = new InputFilter();
        this.textField.setTextFormatter(new TextFormatter<String>(this.filter));
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected TextField getTextField() {
        return this.textField;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/
    private final InputFilter filter;

    /**
     * Sets whether or not the InputField accepts only numerical characters or not.
     * The user's input will simply be ignored if it violates this property.
     *
     * @param numOnly   true if only characters from 0-9 inclusive should be committed
     */
    public void setNumOnly(final boolean numOnly) {
        this.filter.numOnly = numOnly;
    }

    /**
     * Sets the maximum number of characters this InputField will accept.
     * The user's input will simply be ignored if it violates this property.
     *
     * @param length    the maximum number of characters allowed
     */
    public void setMaxInputLength(final int length) {
        this.filter.maxLength = length;
    }

    /**
     * Sets the value of the text field's text property.
     *
     * @param text      the text that should be set
     */
    public void setFieldText(final String text) {
        this.textField.setText(text);
    }

    public void setFieldPrompt(final String prompt) {
        this.textField.setPromptText(prompt);
    }

    public void setFieldAlignment(Pos pos) {
        this.textField.setAlignment(pos);
    }

    /**
     * Turns out, the TextField doesn't even have a Java text fill setter. What is this?
     * So, I implemented it myself. I hate touching CSS. Doing it from Java is a war crime.
     *
     * @param colour    the colour that the text of the TextField should be set to
     * @see TextField
     */
    public void setTextFill(final Color colour) {
        final int red = (int) colour.getRed();
        final int green = (int) colour.getGreen();
        final int blue = (int) colour.getBlue();
        final double alpha = colour.getOpacity();
        final String fill = "rgba(" + Integer.toString(red) + "," + Integer.toString(green) + ","
            + Integer.toString(blue) + "," + Double.toString(alpha) + ")";

        this.textField.setStyle("-fx-text-fill: " + fill + "; user-select: none;");
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class InputFilter implements UnaryOperator<TextFormatter.Change> {
        int maxLength;
        boolean numOnly;

        InputFilter() {
            this.maxLength = 0;
            this.numOnly = false;
        }

        @Override
        public Change apply(Change t) {
            String text = t.getControlNewText();
            boolean hasNumOnly = text.matches("^-?[0-9]+$");
            boolean valid =
                (maxLength <= 0 || text.length() <= maxLength) && (!numOnly || hasNumOnly)
                || text.isEmpty();
            return (valid) ? t : null;
        }
    }
}
