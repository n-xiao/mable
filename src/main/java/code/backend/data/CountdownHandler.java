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

package code.backend.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.TreeSet;

public final class CountdownHandler {
    private static final TreeSet<Countdown> COUNTDOWNS =
        new TreeSet<Countdown>(new SortByRemainingDays());
    private static final Stack<Countdown> DELETED_COUNTDOWNS = new Stack<Countdown>();

    public static TreeSet<Countdown> getAll() {
        return COUNTDOWNS;
    }

    static ArrayList<Countdown> getIncomplete() {
        final ArrayList<Countdown> arr = new ArrayList<Countdown>();
        COUNTDOWNS.forEach(c -> {
            if (!c.isDone())
                arr.add(c);
        });
        return arr;
    }

    static ArrayList<Countdown> getComplete() {
        final ArrayList<Countdown> arr = new ArrayList<>();
        COUNTDOWNS.forEach(c -> {
            if (c.isDone())
                arr.add(c);
        });
        return arr;
    }

    static Stack<Countdown> getDeletedCountdowns() {
        return DELETED_COUNTDOWNS;
    }

    public static Countdown create(final String name, final LocalDate dueDate) {
        final Countdown countdown = new Countdown(name, dueDate);
        addCountdown(countdown);
        return countdown;
    }

    /**
     * This method is designed to be called during runtime, by user interaction.
     * It should never be called during load operations. Use the getCountdowns.add()
     * method for that.
     */
    public static void addCountdown(Countdown c) {
        COUNTDOWNS.add(c);
        StorageHandler.save();
    }

    static void deleteCountdown(final Countdown countdown) {
        COUNTDOWNS.remove(countdown);
        DELETED_COUNTDOWNS.add(countdown);
    }

    static void recoverCountdown(final Countdown countdown) {
        DELETED_COUNTDOWNS.remove(countdown);
        COUNTDOWNS.add(countdown);
    }

    static void deleteCountdowns(final Collection<Countdown> countdowns) {
        countdowns.forEach(CountdownHandler::deleteCountdown);
    }

    static void eraseCountdown(final Countdown countdown) {
        COUNTDOWNS.removeIf(c -> c.equals(countdown));
        DELETED_COUNTDOWNS.removeIf(c -> c.equals(countdown));
    }

    static boolean isCountdownDeleted(final Countdown countdown) {
        return DELETED_COUNTDOWNS.contains(countdown);
    }

    static Countdown getCountdownByID(String id) {
        for (Countdown countdown : COUNTDOWNS) {
            if (countdown.getID().toString().equals(id))
                return countdown;
        }
        return null;
    }

    private CountdownHandler() {}
}
