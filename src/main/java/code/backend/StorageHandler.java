/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend;

import code.backend.Countdown.Urgency;
import code.backend.CountdownFolder.SpecialType;
import code.frontend.gui.pages.home.CountdownPaneView;
import java.awt.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // in the backend, currentlySelectedFolder is always init to INCOMPLETED_FOLDER
    private static CountdownFolder currentlySelectedFolder = INCOMPLETED_FOLDER;

    public static CountdownFolder getIncompletedFolder() {
        return INCOMPLETED_FOLDER;
    }

    public static CountdownFolder getCompletedFolder() {
        return COMPLETED_FOLDER;
    }

    protected static TreeSet<Countdown> getCountdowns() {
        return COUNTDOWNS;
    }

    private StorageHandler() {}

    public static void init() throws Exception {
        final boolean LOADABLE = Files.exists(STORAGE_PATH) && Files.exists(FOLDER_PATH);
        if (LOADABLE) {
            load();
        } else {
            Files.createDirectories(DATA_DIR);

            if (Files.notExists(STORAGE_PATH)) {
                Files.createFile(STORAGE_PATH);
                saveCountdowns();
            }
            if (Files.notExists(FOLDER_PATH)) {
                Files.createFile(FOLDER_PATH);
                saveFolders();
            }

            load();
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
        COUNTDOWNS.forEach(cd -> { OBJ_ROOT.putPOJO(cd.getID().toString(), cd); });
        DELETED_COUNTDOWNS.forEach(cd -> { OBJ_ROOT.remove(cd.getID().toString()); });
        MAPPER.writeValue(STORAGE_PATH, OBJ_ROOT);
    }

    public static void addCountdown(Countdown c) {
        COUNTDOWNS.add(c);
        currentlySelectedFolder.getContents().add(c);
        if (currentlySelectedFolder.equals(COMPLETED_FOLDER))
            c.setDone(true);
        save();
    }

    public static void deleteCountdowns(Collection<Countdown> countdowns) {
        COUNTDOWNS.removeAll(countdowns);
        FOLDERS.forEach(folder -> { folder.getContents().removeAll(countdowns); });
        DELETED_COUNTDOWNS.addAll(countdowns);
    }

    public static Countdown getCountdownByID(String id) {
        for (Countdown countdown : COUNTDOWNS) {
            if (countdown.getID().toString().equals(id))
                return countdown;
        }
        return null;
    }

    public static NavigableSet<Countdown> getDescendingCountdowns() {
        return currentlySelectedFolder.getContents().descendingSet();
    }

    public static NavigableSet<Countdown> getAscendingCountdowns() {
        return getDescendingCountdowns().reversed();
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
        // initialises protected folders
        organiseProtectedFolders();
    }

    /**
     * This attempts to extract required folder information to reconstruct
     * FOLDERS. This method is useful when a user is migrating from an older
     * version of Mable, which may slightly different backend functionality.
     * To put it plainly, this method attempts (but does not guarantee) to
     * provide backwards compatibility for all previous Mable versions.
     *
     * This method should only be run if the main loading method throws a
     * Jackson exception.
     */
    private static void salvageSavedFolders() {
        assert FOLDERS.isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(FOLDER_PATH);
        JSON_ROOT.forEachEntry((k, v) -> {
            String name = MAPPER.writeValueAsString(v.get("name"));
            String jsonContentsArray = MAPPER.writeValueAsString(v.get("contents"));
            ArrayList<String> contents = MAPPER.readValue(jsonContentsArray, ArrayList.class);

            CountdownFolder folder = new CountdownFolder(name);
            contents.forEach(
                string -> folder.getContents().add(StorageHandler.getCountdownByID(string)));

            FOLDERS.add(folder); // adds the serialised folder to the FOLDERS
        });

        deleteAllCountdownsSafely();
        saveFolders();
    }

    private static void deleteAllCountdownsSafely() {
        final Path COUNTDOWN_BACKUP = Path.of(
            DATA_DIR.toString() + "/" + LocalDateTime.now().toString() + "-countdowns-backup.json");
        try { // make a copy of the existing data
            if (Files.notExists(COUNTDOWN_BACKUP))
                Files.copy(STORAGE_PATH, COUNTDOWN_BACKUP);
        } catch (IOException _e) {
        }

        // delete countdown data
        // final JsonNode COUNTDOWN_JSON_ROOT = MAPPER.readTree(STORAGE_PATH);
        // if (!COUNTDOWN_JSON_ROOT.isObject())
        //     return;
        // final ObjectNode COUNTDOWN_OBJ_ROOT = (ObjectNode) COUNTDOWN_JSON_ROOT;
        // COUNTDOWN_OBJ_ROOT.removeAll();
        MAPPER.writeValue(STORAGE_PATH, MAPPER.createObjectNode().putObject(""));
    }

    private static void deleteAllFoldersSafely() {
        final Path FOLDER_BACKUP = Path.of(
            DATA_DIR.toString() + "/" + LocalDateTime.now().toString() + "-folders-backup.json");
        try {
            if (Files.notExists(FOLDER_BACKUP))
                Files.copy(FOLDER_PATH, FOLDER_BACKUP);
        } catch (IOException _e) {
        }
        // delete folder data
        // final JsonNode FOLDER_JSON_ROOT = MAPPER.readTree(STORAGE_PATH);
        // if (!FOLDER_JSON_ROOT.isObject())
        //     return;
        // final ObjectNode FOLDER_OBJ_ROOT = (ObjectNode) FOLDER_JSON_ROOT;
        // FOLDER_OBJ_ROOT.removeAll();
        MAPPER.writeValue(FOLDER_PATH, MAPPER.createObjectNode().putObject(""));
    }

    public static void organiseProtectedFolders() {
        COMPLETED_FOLDER.getContents().clear();
        INCOMPLETED_FOLDER.getContents().clear();
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
        FOLDERS.forEach(folder -> { OBJ_ROOT.putPOJO(folder.getID().toString(), folder); });
        DELETED_FOLDERS.forEach(
            deletedFolder -> { OBJ_ROOT.remove(deletedFolder.getID().toString()); });
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

    public static void removeSelectedFolder() {
        if (currentlySelectedFolder.isProtectedFolder())
            return;
        FOLDERS.remove(currentlySelectedFolder);
        DELETED_FOLDERS.add(currentlySelectedFolder);
    }

    public static boolean setSelectedFolderName(String name) {
        if (name.equals(currentlySelectedFolder.getName()))
            return true;

        if (folderExists(name))
            return false;

        currentlySelectedFolder.setName(name);

        return true;
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
