/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend.utils;

import code.backend.data.Countdown;
import code.backend.data.CountdownFolder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import tools.jackson.core.JacksonException;
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
    private static final Path COUNTDOWNS_PATH = Path.of(DATA_DIR.toString() + "/countdowns.json");
    private static final Path FOLDER_PATH = Path.of(DATA_DIR.toString() + "/folders.json");
    private static final ObjectMapper MAPPER =
        JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();

    public static void init() throws Exception {
        final boolean LOADABLE = Files.exists(COUNTDOWNS_PATH) && Files.exists(FOLDER_PATH);
        if (LOADABLE) {
            load();
        } else {
            Files.createDirectories(DATA_DIR);

            if (Files.notExists(COUNTDOWNS_PATH)) {
                Files.createFile(COUNTDOWNS_PATH);
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
        try {
            loadCountdowns(); // ALWAYS LOAD COUNTDOWNS FIRST
        } catch (JacksonException e) {
            salvageCountdowns();
        }

        try {
            loadFolders();
        } catch (JacksonException e) {
            salvageSavedFolders();
        }
    }

    private static void loadCountdowns() throws JacksonException {
        assert CountdownHandler.getCountdowns().isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(COUNTDOWNS_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            Countdown cd = MAPPER.treeToValue(v, Countdown.class);
            CountdownHandler.getCountdowns().add(cd);
        });
    }

    /**
     * This attempts to extract required Countdown information to reconstruct
     * COUNTDOWNS. This method is useful when a user is migrating from an older
     * version of Mable (< v1.0.4-beta), with slightly different backend functionality.
     * To put it plainly, this method attempts (but does not guarantee) to
     * provide backwards compatibility for all previous Mable versions.
     *
     * This method should only be run if the main loading method throws a
     * Jackson exception.
     *
     * This method has literally been deprecated just as it was created,
     * given that it is technically unnecessary for such a young & small project.
     * This method will be removed in the first stable release of Mable.
     */
    @Deprecated
    private static void salvageCountdowns() {
        CountdownHandler.getCountdowns().clear(); // ensures empty
        final JsonNode JSON_ROOT = MAPPER.readTree(COUNTDOWNS_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            String id = MAPPER.convertValue(v.get("id"), String.class); // assumes "id", not "ID"
            String name = MAPPER.convertValue(v.get("name"), String.class);
            boolean isDone = MAPPER.convertValue(v.get("isDone"), Boolean.class).booleanValue();
            String due = MAPPER.convertValue(v.get("due"), String.class);
            CountdownHandler.getCountdowns().add(new Countdown(id, name, isDone, due));
        });

        deleteAllCountdownsSafely();
        saveCountdowns();
    }

    /**
     * This method creates a copy of the current countdowns.json file before deleting all its
     * contents. The backup .json file is stored in the same mable_data directory, with the
     * inclusion of the current LocalDateTime formatted as: year, day of year, hour of day, minute
     * of hour and second of hour, separated by dashes. The LocalDateTime string is followed by
     * "-countdowns-backup.json"
     */
    private static void deleteAllCountdownsSafely() {
        final Path COUNTDOWN_BACKUP = Path.of(DATA_DIR.toString() + "/"
            + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-DDD-HH-mm-ss"))
                .toString()
            + "-countdowns-backup.json");
        try { // make a copy of the existing data
            if (Files.notExists(COUNTDOWN_BACKUP))
                Files.copy(COUNTDOWNS_PATH, COUNTDOWN_BACKUP);
        } catch (IOException _e) {
        }

        // clears json file contents
        MAPPER.writeValue(COUNTDOWNS_PATH, MAPPER.createObjectNode().putObject(""));
    }

    private static void saveCountdowns() {
        final JsonNode JSON_ROOT = MAPPER.readTree(COUNTDOWNS_PATH);
        final ObjectNode OBJ_ROOT = JSON_ROOT.isObject() ? ((ObjectNode) JSON_ROOT)
                                                         : MAPPER.createObjectNode().putObject("");
        CountdownHandler.getCountdowns().forEach(
            cd -> { OBJ_ROOT.putPOJO(cd.getID().toString(), cd); });
        CountdownHandler.getDeletedCountdowns().forEach(
            cd -> { OBJ_ROOT.remove(cd.getID().toString()); });
        MAPPER.writeValue(COUNTDOWNS_PATH, OBJ_ROOT);
    }

    private static void loadFolders() throws JacksonException {
        assert FolderHandler.getFolders().isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(FOLDER_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            CountdownFolder cdf = MAPPER.treeToValue(v, CountdownFolder.class);
            FolderHandler.getFolders().add(cdf);
        });
        // initialises protected folders
        FolderHandler.organiseProtectedFolders();
    }

    /**
     * This attempts to extract required folder information to reconstruct
     * FOLDERS. This method is useful when a user is migrating from an older
     * version of Mable (< v1.0.4-beta), with slightly different backend functionality.
     * To put it plainly, this method attempts (but does not guarantee) to
     * provide backwards compatibility for all previous Mable versions.
     *
     * This method should only be run if the main loading method throws a
     * Jackson exception.
     *
     * This method has literally been deprecated just as it was created,
     * given that it is technically unnecessary for such a young & small project.
     * This method will be removed in the first stable release of Mable.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    private static void salvageSavedFolders() {
        FolderHandler.getFolders().clear(); // ensures empty
        final JsonNode JSON_ROOT = MAPPER.readTree(FOLDER_PATH);
        JSON_ROOT.forEachEntry((k, v) -> {
            String name = MAPPER.convertValue(v.get("name"), String.class);
            String jsonContentsArray = MAPPER.writeValueAsString(v.get("contents"));
            ArrayList<String> contents = MAPPER.readValue(jsonContentsArray, ArrayList.class);

            CountdownFolder folder = new CountdownFolder(name);
            contents.forEach( // rebuild contents
                stringID -> folder.getContents().add(CountdownHandler.getCountdownByID(stringID)));

            FolderHandler.getFolders().add(folder); // adds the serialised folder to the FOLDERS
        });

        deleteAllFoldersSafely();
        saveFolders();
    }

    /**
     * This method creates a copy of the current folders.json file before deleting all its
     * contents. The backup .json file is stored in the same mable_data directory, with the
     * inclusion of the current LocalDateTime formatted as: year, day of year, hour of day, minute
     * of hour and second of hour, separated by dashes. The LocalDateTime string is followed by
     * "-folders-backup.json"
     */
    private static void deleteAllFoldersSafely() {
        final Path FOLDER_BACKUP = Path.of(DATA_DIR.toString() + "/"
            + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-DDD-HH-mm-ss"))
                .toString()
            + "-folders-backup.json");
        try {
            if (Files.notExists(FOLDER_BACKUP))
                Files.copy(FOLDER_PATH, FOLDER_BACKUP);
        } catch (IOException _e) {
        }

        // clears json file contents
        MAPPER.writeValue(FOLDER_PATH, MAPPER.createObjectNode().putObject(""));
    }

    private static void saveFolders() {
        final JsonNode JSON_ROOT = MAPPER.readTree(FOLDER_PATH);
        final ObjectNode OBJ_ROOT = JSON_ROOT.isObject() ? ((ObjectNode) JSON_ROOT)
                                                         : MAPPER.createObjectNode().putObject("");
        FolderHandler.getFolders().forEach(
            folder -> { OBJ_ROOT.putPOJO(folder.getID().toString(), folder); });
        FolderHandler.getDeletedFolders().forEach(
            deletedFolder -> { OBJ_ROOT.remove(deletedFolder.getID().toString()); });
        MAPPER.writeValue(FOLDER_PATH, OBJ_ROOT);
    }

    private StorageHandler() {}
}
