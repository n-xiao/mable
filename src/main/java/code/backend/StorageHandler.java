// FIX REQUIRED: SAVE & RESTORE IS PROBABLY NOT BACKWARDS COMPATIBLE AT ALL.
// ANY CHANGE TO THE Countdown CLASS IN FUTURE UPDATES COULD BREAK LOAD OPS.
// WILL NEED TO IMPLEMENT Json or smthing

package code.backend;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
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
    public static Path storagePath;
    private static PriorityQueue<Countdown> countdowns =
        new PriorityQueue<Countdown>(new SortByRemainingDays());

    private StorageHandler() {}

    public static void init() throws Exception {
        // btw, Path should automatically account for different OS path separators
        storagePath =
            Path.of(System.getProperty("user.home") + "/mable_data/mable_countdowns.json");
        if (!Files.exists(storagePath)) {
            Files.createDirectory(Path.of(System.getProperty("user.home") + "/mable_data"));
            Files.createFile(storagePath);
        } else {
            load();
        }
    }

    public static void save() {}

    public static void saveCountdown(Countdown c) {
        ObjectMapper mapper = new ObjectMapper();
        // ObjectNode root = mapper.createObjectNode();
        // root.putPOJO(c.getIdAsString(), c);
        // System.out.println(mapper.writeValueAsString(root));
        PriorityQueue<Countdown> priorityQueue =
            new PriorityQueue<Countdown>(new SortByRemainingDays());
        priorityQueue.add(new Countdown("hello", 1, 1, 2026));
        priorityQueue.add(new Countdown("hello", 1, 1, 2026));
        priorityQueue.add(new Countdown("hello2", 1, 1, 2025));
        String json = mapper.writeValueAsString(priorityQueue);
        System.out.println(json);
    }

    private static void load() {}

    public static void setCountdownDone(Countdown c, boolean isDone) {
        countdowns.forEach(countdown -> {
            if (countdown.ID.equals(c.ID)) {
                countdown.setDone(isDone);
                return;
            }
        });
    }

    public static void editCountdown(Countdown c, String name, LocalDate date) {
        countdowns.forEach(countdown -> {
            if (countdown.ID.equals(c.ID)) {
                countdown.setName(name);
                countdown.setDueDate(date);
                return;
            }
        });
    }

    public static void deleteCountdown(Countdown c) {
        countdowns.remove(c);
    }

    public static void deleteCountdowns(Countdown... cs) {
        for (Countdown countdown : cs) {
            countdowns.remove(countdown);
        }
    }

    public static void addCountdown(Countdown c) {
        countdowns.add(c);
    }

    public static Countdown[] getAscendingCountdowns() {
        return countdowns.toArray(new Countdown[0]);
    }

    public static Countdown[] getDescendingCountdowns() {
        Countdown[] array = getAscendingCountdowns();
        Collections.reverse(Arrays.asList(array));
        return array;
    }
}
