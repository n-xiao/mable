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

package code.frontend.capabilities.concurrency;

/**
 * This allows the {@link Watchdog} to send updates to
 * implementations of this interface.
 */
public interface Updatable {
    /**
     * This method will be called by the Watchdog's thread at regular intervals.
     * Implementations should use this method to update any UI elements that
     * may change throughout time.
     *
     * Note: Implementations need to call {@link Watchdog#watch(Updatable)}
     * and add themselves to the list of {@link Updatable} stuff that
     * the Watchdog will watch.
     */
    public void update();
}
