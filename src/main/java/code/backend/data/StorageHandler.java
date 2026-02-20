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

import java.nio.file.Files;
import java.nio.file.Path;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 *  Handles all mutable file read/writing operations. Note that this does not handle resources
 *  (resources are handled by Vals) since data in resouces are
 *  immutable by a user.
 */
public final class StorageHandler {
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
        loadCountdowns(); // ALWAYS LOAD COUNTDOWNS FIRST
        loadFolders();
    }

    private static void loadCountdowns() throws JacksonException {
        assert CountdownHandler.getAll().isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(COUNTDOWNS_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            Countdown cd = MAPPER.treeToValue(v, Countdown.class);
            CountdownHandler.getAll().add(cd);
        });
    }

    private static void saveCountdowns() {
        final JsonNode JSON_ROOT = MAPPER.readTree(COUNTDOWNS_PATH);
        final ObjectNode OBJ_ROOT = JSON_ROOT.isObject() ? ((ObjectNode) JSON_ROOT)
                                                         : MAPPER.createObjectNode().putObject("");
        CountdownHandler.getAll().forEach(cd -> { OBJ_ROOT.putPOJO(cd.getID().toString(), cd); });
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
