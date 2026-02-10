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
import java.util.function.UnaryOperator;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.Region;

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
public class Field extends Region {
    private final TextField textField;

    public Field() {
        this.setBackground(null);
        this.setCursor(Cursor.TEXT);
        // textField setup
        this.textField = new TextField();
        this.textField.setBackground(null);
        this.textField.setBorder(null);
        this.textField.setFont(FontHandler.getDedicated(DedicatedFont.USER_INPUT));
        this.textField.setStyle("-fx-text-fill: white; user-select: none;");
        this.textField.setFocusTraversable(false);
        this.textField.prefWidthProperty().bind(this.widthProperty());
        this.textField.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(this.textField);

        // filter setup
        this.filter = new InputFilter();
        this.textField.setTextFormatter(new TextFormatter<String>(this.filter));
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
    public final void setNumOnly(final boolean numOnly) {
        this.filter.numOnly = numOnly;
    }

    /**
     * Sets the maximum number of characters this InputField will accept.
     * The user's input will simply be ignored if it violates this property.
     *
     * @param length    the maximum number of characters allowed
     */
    public final void setMaxInputLength(final int length) {
        this.filter.maxLength = length;
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
