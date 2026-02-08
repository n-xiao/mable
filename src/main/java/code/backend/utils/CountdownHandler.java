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

package code.backend.utils;

import code.backend.data.Countdown;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.TreeSet;

public class CountdownHandler {
    private static final TreeSet<Countdown> COUNTDOWNS =
        new TreeSet<Countdown>(new SortByRemainingDays());
    private static final Stack<Countdown> DELETED_COUNTDOWNS = new Stack<Countdown>();

    public static TreeSet<Countdown> getAll() {
        return COUNTDOWNS;
    }

    public static ArrayList<Countdown> getIncomplete() {
        final ArrayList<Countdown> arr = new ArrayList<Countdown>();
        COUNTDOWNS.forEach(c -> {
            if (!c.isDone())
                arr.add(c);
        });
        return arr;
    }

    public static ArrayList<Countdown> getComplete() {
        final ArrayList<Countdown> arr = new ArrayList<>();
        COUNTDOWNS.forEach(c -> {
            if (c.isDone())
                arr.add(c);
        });
        return arr;
    }

    public static Stack<Countdown> getDeletedCountdowns() {
        return DELETED_COUNTDOWNS;
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

    public static void deleteCountdowns(Collection<Countdown> countdowns) {
        COUNTDOWNS.removeAll(countdowns);
        FolderHandler.getFolders().forEach(
            folder -> { folder.getContents().removeAll(countdowns); });
        DELETED_COUNTDOWNS.addAll(countdowns);
    }

    public static Countdown getCountdownByID(String id) {
        for (Countdown countdown : COUNTDOWNS) {
            if (countdown.getID().toString().equals(id))
                return countdown;
        }
        return null;
    }

    private CountdownHandler() {}
}
