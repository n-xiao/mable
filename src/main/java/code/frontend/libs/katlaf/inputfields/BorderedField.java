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
import code.frontend.libs.katlaf.graphics.MableBorder;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * This UI component presents itself as a Field within a LabelledBorderedRegion.
 * The LabelledBorderedRegion will indicate when the Field has the user focus by being
 * recoloured to the "selected" colour, specified by the current theme.
 *
 * @see LabelledBorderedRegion
 * @see Field
 * @since v3.0.0-beta
 */
public class BorderedField extends StackPane {
    private final Field field;
    private final LabelledBorderedRegion border;

    /**
     * Creates a new BorderedField instance.
     *
     * @param label     the text to be used by the label of the border
     * @param bg        the background colour that should be used
     * @see LabelledBorderedRegion
     */
    public BorderedField(final String label, final Color bg) {
        this.field = new Field();

        final MableBorder mableBorder = new MableBorder(1.5, 0.2, 0.35);
        this.border = new LabelledBorderedRegion(mableBorder, label, bg);
        this.border.setMouseTransparent(true); // to be used for cosmetic purposes only

        this.setBackground(null);
        this.field.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            final Color colour =
                newValue ? RiceHandler.getColour("skyblue") : RiceHandler.getColour("white");
            this.border.getCustomBorder().setColour(colour);
            this.field.setTextFill(colour);
        }));

        this.getChildren().addAll(this.border, this.field);
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected final TextField getTextField() {
        return this.field.getTextField();
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public final String getUserInput() {
        return this.getTextField().getText();
    }

    public final void setUserInput(final String input) {
        this.getTextField().setText(input);
    }

    /**
     * Wrapper method which just forwards the call to the Field.
     * <p>
     * Sets whether or not the InputField accepts only numerical characters or not.
     * The user's input will simply be ignored if it violates this property.
     *
     * @param numOnly   true if only characters from 0-9 inclusive should be committed
     */
    public final void setNumOnly(final boolean numOnly) {
        this.field.setNumOnly(numOnly);
    }

    /**
     * Wrapper method which just forwards the call to the Field.
     * <p>
     * Sets the maximum number of characters this InputField will accept.
     * The user's input will simply be ignored if it violates this property.
     *
     * @param length    the maximum number of characters allowed
     */
    public final void setMaxInputLength(final int length) {
        this.field.setMaxInputLength(length);
    }

    /**
     * Wrapper method which just forwards the call to the Field.
     * <p>
     * Sets the value of the text field's text property.
     *
     * @param text      the text that should be set
     */
    public final void setFieldText(final String text) {
        this.field.setFieldText(text);
    }
}
