package code.backend;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 *  Handles all mutable file read/writing operations. Note that this does not handle resources
 *  (resources are handled by {@link code.frontend.misc.Vals}) since data in resouces are
 *  immutable by a user.
 */
public class StorageHandler {
    private static final Path STORAGE_PATH =
        Path.of(System.getProperty("user.home") + "/mable_data/mable_countdowns.json");
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final PriorityQueue<Countdown> COUNTDOWN_PQ =
        new PriorityQueue<Countdown>(new SortByRemainingDays());

    private StorageHandler() {}

    public static void init() throws Exception {
        if (!Files.exists(STORAGE_PATH)) {
            Files.createDirectory(Path.of(System.getProperty("user.home") + "/mable_data"));
            Files.createFile(STORAGE_PATH);
        } else {
            load();
        }
    }

    public static void save() {
        final ObjectNode OBJ_ROOT = (ObjectNode) MAPPER.readTree(STORAGE_PATH);
        COUNTDOWN_PQ.forEach(cd -> { OBJ_ROOT.putPOJO(cd.getIdAsString(), cd); });
    }

    private static void load() {
        assert COUNTDOWN_PQ.isEmpty();
        final JsonNode JSON_ROOT = MAPPER.readTree(STORAGE_PATH);
        JSON_ROOT.forEachEntry((_k, v) -> {
            Countdown cd = MAPPER.treeToValue(v, Countdown.class);
            COUNTDOWN_PQ.add(cd);
        });
    }

    public static void setCountdownDone(Countdown c, boolean isDone) {
        COUNTDOWN_PQ.forEach(countdown -> {
            if (countdown.ID.equals(c.ID)) {
                countdown.setDone(isDone);
                return;
            }
        });
    }

    public static void editCountdown(Countdown c, String name, LocalDate date) {
        COUNTDOWN_PQ.forEach(countdown -> {
            if (countdown.ID.equals(c.ID)) {
                countdown.setName(name);
                countdown.setDueDate(date);
                return;
            }
        });
    }

    public static void deleteCountdown(Countdown c) {
        COUNTDOWN_PQ.remove(c);
    }

    public static void deleteCountdowns(Countdown... cs) {
        for (Countdown countdown : cs) {
            COUNTDOWN_PQ.remove(countdown);
        }
    }

    public static void addCountdown(Countdown c) {
        COUNTDOWN_PQ.add(c);
    }

    public static Countdown[] getAscendingCountdowns() {
        return COUNTDOWN_PQ.toArray(new Countdown[0]);
    }

    public static Countdown[] getDescendingCountdowns() {
        Countdown[] array = getAscendingCountdowns();
        Collections.reverse(Arrays.asList(array));
        return array;
    }
}
