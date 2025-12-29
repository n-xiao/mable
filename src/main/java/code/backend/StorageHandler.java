// FIX REQUIRED: SAVE & RESTORE IS PROBABLY NOT BACKWARDS COMPATIBLE AT ALL.
// ANY CHANGE TO THE Countdown CLASS IN FUTURE UPDATES COULD BREAK LOAD OPS.
// WILL NEED TO IMPLEMENT Json or smthing

package code.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

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
        storagePath = Path.of(System.getProperty("user.home") + "/mable_data/storage.mable");
        if (!Files.exists(storagePath)) {
            Files.createDirectory(Path.of(System.getProperty("user.home") + "/mable_data"));
            Files.createFile(storagePath);
        } else {
            load();
        }
    }

    public static void save() {
        // try {
        //     // if (!active) throw new IOException("Storage is inactive.");
        //     FileOutputStream outputStream = new FileOutputStream(storagePath.toString());
        //     ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream);
        //     objOutputStream.writeObject(countdowns);
        //     objOutputStream.flush();
        //     objOutputStream.close();
        // } catch (Exception e) {
        //     System.err.println("error while saving data!");
        //     e.printStackTrace();
        // }
    }

    @SuppressWarnings("unchecked")
    private static void load() {
        try {
            // if (!active) throw new IOException("Storage is inactive.");
            FileInputStream inputStream = new FileInputStream(storagePath.toString());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            countdowns = (PriorityQueue<Countdown>) objInputStream.readObject();
            objInputStream.close();
        } catch (Exception e) {
            System.err.println("error while loading save data!");
            e.printStackTrace();
        }
    }

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
