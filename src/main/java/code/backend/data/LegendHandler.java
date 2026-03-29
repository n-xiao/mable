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

import code.frontend.libs.katlaf.ricing.Colour;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.Stack;
import java.util.TreeSet;
import javafx.scene.paint.Color;

public final class LegendHandler {
    private LegendHandler() {}

    private static final TreeSet<Legend> LEGENDS = new TreeSet<Legend>();
    private static final Stack<Legend> DELETED_LEGENDS = new Stack<Legend>();

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    /**
     * Removes the specified Countdown from all Legends.
     */
    static void disownCountdown(final Countdown countdown) {
        LEGENDS.forEach(legend -> legend.getContents().removeIf(c -> c.equals(countdown)));
    }

    static void eraseCountdown(final Countdown countdown) {
        disownCountdown(countdown);
        DELETED_LEGENDS.forEach(legend -> legend.getContents().removeIf(c -> c.equals(countdown)));
    }

    static Color lookupColour(final Countdown countdown) {
        for (Legend legend : LEGENDS) {
            if (legend.getContents().contains(countdown))
                return legend.getColour().get();
        }
        return RiceHandler.getColour("white");
    }

    static boolean lookupCountdown(final Countdown countdown) {
        for (Legend legend : LEGENDS) {
            if (legend.getContents().contains(countdown))
                return true;
        }
        return false;
    }

    static Legend getLegend(final Countdown countdown) {
        for (Legend legend : LEGENDS) {
            if (legend.getContents().contains(countdown))
                return legend;
        }
        return null;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public static Legend createLegend(final String name, final Colour colour, final int index) {
        final Legend legend = new Legend(name);
        legend.setIndex(index);
        legend.setColour(colour);
        LEGENDS.add(legend);
        StorageHandler.save();
        return legend;
    }

    public static void removeLegend(final Legend legend) {
        legend.disownContents();
        LEGENDS.remove(legend);
        DELETED_LEGENDS.add(legend);
    }

    public static TreeSet<Legend> getLegends() {
        return LEGENDS;
    }

    public static Legend findLegend(final String name, final boolean matchCase) {
        for (Legend legend : LEGENDS) {
            if (name.equals(legend.getName()) && matchCase
                || name.toLowerCase().equals(legend.getName().toLowerCase()) && !matchCase)
                return legend;
        }
        return null;
    }

    public static Stack<Legend> getDeletedLegends() {
        return DELETED_LEGENDS;
    }
}
