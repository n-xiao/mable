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

package code.frontend.libs.katlaf.tables;

/**
 * A representation of backend data that can be displayed using a table.
 * The frontend represent of a Tablet would be a SimpleTableMember.
 *
 * @see SimpleTableMember
 * @since v3.0.0-beta
 */
public interface Tablet {
    /**
     * Gets the last position of this tablet. Facilitates the implementation of user-specified
     * ordering. If a certain implementation does not require such ordering, this method can
     * return any value strictly less than 0 for the value to be ignored. Otherwise, this
     * will be the index of this Tablet's frontend representation.
     * <p>
     * If there are conflicting positions, this instance will retain its positioning while the
     * instance that had the same position will be shifted right, similar to the behaviour of
     * adding to a specific index which is already occupied.
     *
     * @return the last known index of this instance's frontend representation
     */
    public int getPosition();

    /**
     * Stores the current index of the frontend representation of this instance.
     *
     * @param pos   the index of this instance
     */
    public void setPosition(int pos);

    /**
     * This method will be executed when the frontend representation of this instance
     * is removed by user command.
     */
    public void onRemoved();
}
