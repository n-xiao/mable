package code.backend;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
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
    private static final PriorityQueue<Countdown> COUNTDOWN_PQ =
        new PriorityQueue<Countdown>(new SortByRemainingDays());

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

    public static void save() {
        final JsonNode JSON_ROOT = MAPPER.readTree(STORAGE_PATH);

        final ObjectNode OBJ_ROOT = JSON_ROOT.isObject() ? ((ObjectNode) JSON_ROOT)
                                                         : MAPPER.createObjectNode().putObject("");

        COUNTDOWN_PQ.forEach(cd -> { OBJ_ROOT.putPOJO(cd.getIdAsString(), cd); });

        MAPPER.writeValue(STORAGE_PATH, OBJ_ROOT);
    }

    private static void load() {
        assert COUNTDOWN_PQ.isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(STORAGE_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            Countdown cd = MAPPER.treeToValue(v, Countdown.class);
            COUNTDOWN_PQ.add(cd);
        });
    }

    @Deprecated
    public static void setCountdownDone(Countdown c, boolean isDone) {
        COUNTDOWN_PQ.forEach(countdown -> {
            if (countdown.ID.equals(c.ID)) {
                countdown.setDone(isDone);
                return;
            }
        });
        save();
    }

    public static void editCountdown(Countdown c, String name, LocalDate date) {
        COUNTDOWN_PQ.forEach(countdown -> {
            if (countdown.ID.equals(c.ID)) {
                countdown.setName(name);
                countdown.setDueDate(date);
                return;
            }
        });
        save();
    }

    @Deprecated
    public static void deleteCountdown(Countdown c) {
        COUNTDOWN_PQ.remove(c);
        save();
    }

    @Deprecated
    public static void deleteCountdowns(Countdown... cs) {
        for (Countdown countdown : cs) {
            COUNTDOWN_PQ.remove(countdown);
        }
        save();
    }

    @Deprecated
    public static void addCountdown(Countdown c) {
        COUNTDOWN_PQ.add(c);
        save();
    }

    /**
     * Will remove soon. This limits the advantages of a PQ and
     * violates encapsulation by handing control of visual
     * ordering over to the backend instead of being frontend.
     * This method will be replaced with the getCountdowns()
     * method, and ordering logic will be placed in the
     * frontend.
     */
    @Deprecated
    public static Countdown[] getAscendingCountdowns() {
        return COUNTDOWN_PQ.toArray(new Countdown[0]);
    }

    /**
     * Will remove soon. This limits the advantages of a PQ and
     * violates encapsulation by handing control of visual
     * ordering over to the backend instead of being frontend.
     * This method will be replaced with the getCountdowns()
     * method, and ordering logic will be placed in the
     * frontend.
     */
    @Deprecated
    public static Countdown[] getDescendingCountdowns() {
        Countdown[] array = getAscendingCountdowns();
        Collections.reverse(Arrays.asList(array));
        return array;
    }

    public static PriorityQueue<Countdown> getCountdowns() {
        return COUNTDOWN_PQ;
    }
}
