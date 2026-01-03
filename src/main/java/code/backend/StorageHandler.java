/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.backend;

import code.backend.Countdown.Urgency;
import code.backend.CountdownFolder.SpecialType;
import code.frontend.panels.CountdownPaneView;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.Stack;
import java.util.TreeSet;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 *  Handles all mutable file read/writing operations. Note that this does not handle resources
 *  (resources are handled by {@link code.frontend.misc.Vals}) since data in resouces are
 *  immutable by a user.
 */
public class StorageHandler {
    private static final Path DATA_DIR = Path.of(System.getProperty("user.home") + "/mable_data");
    private static final Path STORAGE_PATH = Path.of(DATA_DIR.toString() + "/countdowns.json");
    private static final Path FOLDER_PATH = Path.of(DATA_DIR.toString() + "/folders.json");
    private static final ObjectMapper MAPPER =
        JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();

    private static final TreeSet<Countdown> COUNTDOWNS =
        new TreeSet<Countdown>(new SortByRemainingDays());
    private static final Stack<Countdown> DELETED_COUNTDOWNS = new Stack<Countdown>();

    private static final TreeSet<CountdownFolder> FOLDERS =
        new TreeSet<CountdownFolder>(new Comparator<CountdownFolder>() {
            public int compare(CountdownFolder o1, CountdownFolder o2) {
                return o1.getName().compareTo(o2.getName());
            };
        });
    private static final Stack<CountdownFolder> DELETED_FOLDERS = new Stack<CountdownFolder>();

    private static final CountdownFolder INCOMPLETED_FOLDER =
        new CountdownFolder(SpecialType.ALL_INCOMPLETE);
    private static final CountdownFolder COMPLETED_FOLDER =
        new CountdownFolder(SpecialType.ALL_COMPLETE);

    private static CountdownFolder currentlySelectedFolder = INCOMPLETED_FOLDER;

    private StorageHandler() {}

    public static void init() throws Exception {
        final boolean LOADABLE = Files.exists(STORAGE_PATH) && Files.exists(FOLDER_PATH);
        if (LOADABLE) {
            load();
        } else {
            Files.createDirectories(DATA_DIR);

            if (Files.notExists(STORAGE_PATH))
                Files.createFile(STORAGE_PATH);

            if (Files.notExists(FOLDER_PATH))
                Files.createFile(FOLDER_PATH);
        }
    }

    public static void save() {
        saveCountdowns();
        saveFolders();
    }

    private static void load() {
        loadCountdowns(); // ALWAYS LOAD COUNTDOWNS FIRST
        loadFolders();
    }

    private static void loadCountdowns() {
        assert COUNTDOWNS.isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(STORAGE_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            Countdown cd = MAPPER.treeToValue(v, Countdown.class);
            COUNTDOWNS.add(cd);
        });
    }

    private static void saveCountdowns() {
        final JsonNode JSON_ROOT = MAPPER.readTree(STORAGE_PATH);
        final ObjectNode OBJ_ROOT = JSON_ROOT.isObject() ? ((ObjectNode) JSON_ROOT)
                                                         : MAPPER.createObjectNode().putObject("");
        COUNTDOWNS.forEach(cd -> { OBJ_ROOT.putPOJO(cd.getIdAsString(), cd); });
        DELETED_COUNTDOWNS.forEach(cd -> { OBJ_ROOT.remove(cd.getIdAsString()); });
        MAPPER.writeValue(STORAGE_PATH, OBJ_ROOT);
    }

    public static void addCountdown(Countdown c) {
        COUNTDOWNS.add(c);
        save();
    }

    public static void deleteCountdowns(Collection<Countdown> countdowns) {
        COUNTDOWNS.removeAll(countdowns);
        DELETED_COUNTDOWNS.addAll(countdowns);
    }

    public static Countdown getCountdownByID(String id) {
        for (Countdown countdown : COUNTDOWNS) {
            if (countdown.getIdAsString().equals(id))
                return countdown;
        }
        return null;
    }

    @Deprecated
    public static NavigableSet<Countdown> getDescendingCountdowns() {
        return COUNTDOWNS.descendingSet();
    }

    @Deprecated
    public static NavigableSet<Countdown> getAscendingCountdowns() {
        return COUNTDOWNS.descendingSet().reversed();
    }

    public static boolean isCountdownRemoved(Countdown countdown) {
        return DELETED_COUNTDOWNS.contains(countdown);
    }

    public static int getStatistic(Urgency urgency) {
        LocalDate now = LocalDate.now();
        int stat = 0;
        for (Countdown countdown : COUNTDOWNS) {
            switch (urgency) {
                case OVERDUE:
                    if (countdown.isOverdue(now))
                        stat++;
                    break;
                case TODAY:
                    if (countdown.isDueToday(now))
                        stat++;
                    break;
                case TOMORROW:
                    if (countdown.isDueTomorrow(now))
                        stat++;
                    break;
                case COMPLETED:
                    if (countdown.isDone())
                        stat++;
                    break;
                default:
                    stat++;
                    break;
            }
        }
        return stat;
    }

    private static void loadFolders() {
        assert FOLDERS.isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(FOLDER_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            CountdownFolder cdf = MAPPER.treeToValue(v, CountdownFolder.class);
            FOLDERS.add(cdf);
        });

        for (Countdown countdown : COUNTDOWNS) {
            if (countdown.isDone()) {
                COMPLETED_FOLDER.getContents().add(countdown);
            } else {
                INCOMPLETED_FOLDER.getContents().add(countdown);
            }
        }
    }

    private static void saveFolders() {
        final JsonNode JSON_ROOT = MAPPER.readTree(FOLDER_PATH);
        final ObjectNode OBJ_ROOT = JSON_ROOT.isObject() ? ((ObjectNode) JSON_ROOT)
                                                         : MAPPER.createObjectNode().putObject("");
        FOLDERS.forEach(folder -> { OBJ_ROOT.putPOJO(folder.getName(), folder); });
        DELETED_FOLDERS.forEach(deletedFolder -> { OBJ_ROOT.remove(deletedFolder.getName()); });
        MAPPER.writeValue(FOLDER_PATH, OBJ_ROOT);
    }

    public static boolean folderExists(String name) {
        for (CountdownFolder folder : FOLDERS) {
            if (folder.getName().equals(name))
                return true;
        }
        return false;
    }

    public static boolean createFolder(String name) {
        if (folderExists(name))
            return false;
        FOLDERS.add(new CountdownFolder(name));
        return true;
    }

    public static void removeFolder(String name) {
        for (CountdownFolder folder : FOLDERS) {
            if (folder.getName().equals(name)) {
                DELETED_FOLDERS.add(folder);
                FOLDERS.remove(folder);
                return;
            }
        }
    }

    public static TreeSet<CountdownFolder> getFolders() {
        return FOLDERS;
    }

    public static LinkedList<String> getFolderNames() {
        LinkedList<String> names = new LinkedList<String>();
        for (CountdownFolder folder : FOLDERS) {
            names.add(folder.getName());
        }
        return names;
    }

    public static CountdownFolder getCurrentlySelectedFolder() {
        return currentlySelectedFolder;
    }

    public static void setCurrentlySelectedFolder(CountdownFolder currentlySelectedFolder) {
        StorageHandler.currentlySelectedFolder = currentlySelectedFolder;
        CountdownPaneView.getInstance().repopulate(LocalDate.now());
    }
}
