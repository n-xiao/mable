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

public interface Listable extends Comparable<Listable> {
    /**
     * The return String value will be used to display
     * the label of a SimpleListMember.
     *
     * @return a String to be displayed
     */
    public String getDisplayString();

    /**
     * This method is used to set the display index (an int)
     * of a Listable so that reordering is possible. This
     * is an optional method, and can be implemented
     * as an empty procedure if deemed appropriate.
     */
    public void setListIndex(int index);
}
