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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

public final class CountdownHandler {
    private static final ObjectMapper MAPPER =
        JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();
    private static final TreeSet<Countdown> COUNTDOWNS =
        new TreeSet<Countdown>(new SortByRemainingDays());
    private static final Stack<Countdown> DELETED_COUNTDOWNS = new Stack<Countdown>();

    private CountdownHandler() {}

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    static Stack<Countdown> getDeletedCountdowns() {
        return DELETED_COUNTDOWNS;
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
            if (countdown.getId().toString().equals(id))
                return countdown;
        }
        return null;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public static TreeSet<Countdown> getCountdowns() {
        return COUNTDOWNS;
    }

    public static Set<Countdown> getAll() {
        final LinkedHashSet<Countdown> result = new LinkedHashSet<Countdown>();
        for (Countdown countdown : COUNTDOWNS) {
            result.add(countdown);
        }
        for (Countdown countdown : DELETED_COUNTDOWNS) {
            result.add(countdown);
        }
        return result;
    }

    public static Countdown create(final String name, final LocalDate dueDate) {
        final Countdown countdown = new Countdown(name, dueDate);
        addCountdown(countdown);
        return countdown;
    }

    public static List<PortableCountdown> extractPortables(final File file) {
        final JsonNode jsonRoot = MAPPER.readTree(file);
        final ArrayList<PortableCountdown> result = new ArrayList<PortableCountdown>();
        jsonRoot.forEachEntry((k, v) -> result.add(MAPPER.treeToValue(v, PortableCountdown.class)));
        return result;
    }

    public static void exportCountdowns(final List<Countdown> countdowns, final Path path)
        throws Exception {
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
        final JsonNode jsonRoot = MAPPER.readTree(path);
        final ObjectNode objRoot =
            jsonRoot.isObject() ? ((ObjectNode) jsonRoot) : MAPPER.createObjectNode().putObject("");

        for (Countdown countdown : countdowns) {
            if (!countdown.isDone() && !countdown.isDeleted()) {
                final PortableCountdown portable = countdown.getPortable();
                objRoot.putPOJO(portable.getId().toString(), portable);
            }
        }

        MAPPER.writeValue(path, objRoot);
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
}
