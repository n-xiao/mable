package code.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

public class StorageHandler
{
    public static boolean active = true;
    public final static Path STORAGE_FILE_PATH;
    private static TreeSet<Countdown> countdowns = new TreeSet<Countdown>(new SortByRemainingDays());

    private StorageHandler() {}

    static
    {
        // btw, Path should automatically account for different OS path separators
        STORAGE_FILE_PATH = Path.of(System.getProperty("user.home") + "/mable_data/storage.mable");
        try
            {
                if (!Files.exists(STORAGE_FILE_PATH))
                    {
                        Files.createDirectory(Path.of(System.getProperty("user.home") + "/mable_data"));
                        Files.createFile(STORAGE_FILE_PATH);
                    }
            }
        catch (Exception e)
            {
                System.err.println("Failed to activate persistent storage!");
                active = false;
            }
    }

    public static void save()
    {
        try
            {
                if (!active) throw new IOException("Storage is inactive.");
                FileOutputStream outputStream = new FileOutputStream(STORAGE_FILE_PATH.toString());
                ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream);
                objOutputStream.writeObject(countdowns);
                objOutputStream.flush();
                objOutputStream.close();
            }
        catch (Exception e)
            {
                System.err.println("error while saving data!");
                e.printStackTrace();
            }
    }

    @SuppressWarnings("unchecked")
    public static void load()
    {
        try
            {
                if (!active) throw new IOException("Storage is inactive.");
                FileInputStream inputStream = new FileInputStream(STORAGE_FILE_PATH.toString());
                ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
                countdowns = (TreeSet<Countdown>) objInputStream.readObject();
                objInputStream.close();
            }
        catch (Exception e)
            {
                System.err.println("error while loading save data!");
                e.printStackTrace();
            }
    }

    public static void editCountdownName(Countdown c, String name)
    {
        c.setName(name);
        save();
    }


    public static void editCountdownDueDate(Countdown c, int day, int month, int year)
    {
        c.setDueDate(day, month, year);
        save();
    }

    public static void setCountdownDone(Countdown c, boolean isDone)
    {
        c.setDone(isDone);
        save();
    }

    public static void deleteCountdown(Countdown c)
    {
        countdowns.remove(c);
        save();
    }

    public static void addCountdown(Countdown c)
    {
        countdowns.add(c);
        save();
    }

    public static Countdown[] getCountdowns()
    {
        return (Countdown[]) countdowns.toArray();
    }

    public static Countdown[] getAscendingCountdowns()
    {
        Set<Countdown> ascendingSet = countdowns.descendingSet().reversed();
        return (Countdown[]) ascendingSet.toArray();
    }

    public static Countdown[] getDescendingCountdowns()
    {
        Set<Countdown> descendingSet = countdowns.descendingSet();
        return (Countdown[]) descendingSet.toArray();
    }
}
