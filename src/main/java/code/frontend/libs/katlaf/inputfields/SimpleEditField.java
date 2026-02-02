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

import javafx.scene.layout.Region;

/**
 * A simple edit field that has one accept and one decline button,
 * located directly below the {@link InputField}.
 */
public class SimpleEditField extends Region {
    public SimpleEditField() {
        // TODO
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * This method enables child classes to implement their own input
     * validation. By default, this method only returns true; it will
     * accept any validation.
     */
    protected boolean validateInput(String input) {
        return true;
    }

    public void displayError(String error) {
        // TODO
    }
}
