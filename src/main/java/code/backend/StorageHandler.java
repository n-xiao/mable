package code.backend;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
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
    private static final ObjectMapper MAPPER =
        JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();

    private static final TreeSet<Countdown> COUNTDOWNS =
        new TreeSet<Countdown>(new SortByRemainingDays());
    private static final Stack<Countdown> DELETED_COUNTDOWNS = new Stack<Countdown>();

    private StorageHandler() {}

    public static void init() throws Exception {
        final boolean LOADABLE = Files.exists(STORAGE_PATH);
        if (LOADABLE) {
            load();
        } else {
            Files.createDirectories(DATA_DIR);
            if (Files.notExists(STORAGE_PATH))
                Files.createFile(STORAGE_PATH);
        }
    }

    private static void save() {
        final JsonNode JSON_ROOT = MAPPER.readTree(STORAGE_PATH);

        final ObjectNode OBJ_ROOT = JSON_ROOT.isObject() ? ((ObjectNode) JSON_ROOT)
                                                         : MAPPER.createObjectNode().putObject("");

        COUNTDOWNS.forEach(cd -> { OBJ_ROOT.putPOJO(cd.getIdAsString(), cd); });
        DELETED_COUNTDOWNS.forEach(cd -> { OBJ_ROOT.remove(cd.getIdAsString()); });

        MAPPER.writeValue(STORAGE_PATH, OBJ_ROOT);
    }

    private static void load() {
        assert COUNTDOWNS.isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(STORAGE_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            Countdown cd = MAPPER.treeToValue(v, Countdown.class);
            COUNTDOWNS.add(cd);
        });
    }

    public static void addCountdown(Countdown c) {
        COUNTDOWNS.add(c);
        save();
    }

    public static void deleteCountdowns(Collection<Countdown> countdowns) {
        COUNTDOWNS.removeAll(countdowns);
        DELETED_COUNTDOWNS.addAll(countdowns);
    }

    public static NavigableSet<Countdown> getDescendingCountdowns() {
        return COUNTDOWNS.descendingSet();
    }

    public static NavigableSet<Countdown> getAscendingCountdowns() {
        return COUNTDOWNS.descendingSet().reversed();
    }

    public static boolean isCountdownRemoved(Countdown countdown) {
        return DELETED_COUNTDOWNS.contains(countdown);
    }
}
