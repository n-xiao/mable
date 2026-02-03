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

package code.frontend.libs.katlaf.lists;

/**
 * An abstract representation of a (backend) component,
 * such as a {@link code.backend.data.CountdownFolder},
 * such that it can be represented as a {@link SimpleListMember}
 * in a {@link SimpleList}.
 */
public interface Listable extends Comparable<Listable> {
    /**
     * The return String value will be used to display
     * the label of a {@link SimpleListMember}
     *
     * @return a String to be displayed
     */
    public String getDisplayString();

    /**
     * This code is executed whenever the button associated with this
     * {@link Listable} is clicked. This usually means that the
     * button is now selected in a {@link SimpleList}
     *
     */
    public void onSelect();

    /**
     * This method is used to verify a current selection through the backend, instead of
     * having all logic in the frontend. For example, this method should return true
     * to indicate that the user is currently using a {@link Listable}, such as
     * when the currently selected {@link code.backend.data.CountdownFolder} is
     * this {@link Listable}.
     *
     * @return true if this {@link Listable} is in use, false otherwise.
     *
     */
    public boolean isEngaged();

    /**
     * This method is used to set the display index (an int)
     * of a Listable so that reordering is possible. This
     * is an optional method, and can be implemented
     * as an empty procedure if deemed appropriate.
     *
     * As an example, this method is necessary for the
     * folder selection list as user-defined ordering is
     * implemented with persistent storage.
     */
    public void setListIndex(int index);
}
