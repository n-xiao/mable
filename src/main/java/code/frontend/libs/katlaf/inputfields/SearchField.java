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

import javafx.scene.paint.Color;

/**
 * This is a special BorderedField which allows a SearchableUI
 * to be updated based on changes to its input.
 *
 * @see BorderedField
 * @since v3.0.0-beta
 */
public class SearchField extends BorderedField {
    /**
     * Creates a new SearchField instance.
     *
     * @param prompt    the prompt text that should be displayed to the user
     *                  when the text field is unfocused.
     * @param target    the target that should react based on the key typed
     *                  event of the created instance
     * @param bg        the background colour which is forwarded to the BorderedField
     *                  constructor.
     * @see BorderedField
     */
    public SearchField(final String prompt, final SearchableUI target, final Color bg) {
        super("SEARCH", bg);
        this.setOnKeyTyped(event -> target.onSearchChange(this.getTextField().getText()));
        this.getTextField().setPromptText(prompt);
    }
}
