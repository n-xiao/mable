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

package code.backend.data.wrappers;

import code.backend.data.Countdown;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Because of how drag and drop is implemented, classes with generic types
 * seem to need a wrapper class in order to be transmitted.
 * Maybe there's a way to do something like List<Countdown>.class but
 * i'll leave that to smarter minds than me. For now, this wrapper
 * class will have to do.
 */
public final class CountdownPacket {
    private final List<Countdown> countdowns;

    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    public CountdownPacket() {
        this(new ArrayList<Countdown>());
    }

    public CountdownPacket(List<Countdown> countdowns) {
        this.countdowns = countdowns;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public List<Countdown> getCountdowns() {
        return countdowns;
    }

    public Set<Countdown> getCountdownsAsSet() {
        return new HashSet<>(countdowns);
    }
}
