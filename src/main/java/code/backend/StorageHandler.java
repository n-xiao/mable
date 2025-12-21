package code.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;

import code.frontend.misc.DisplayBridge;

public class StorageHandler {
    public static boolean active = true;
    public final static String STORAGE_FILE_PATH;
    public final static File STORAGE_FILE;
    private static TreeSet<DisplayBridge> displayables = new TreeSet<DisplayBridge>(new SortByRemainingDays());

    private StorageHandler() {}

    static {
        // fyi windows is \\ separator while unix is /
        // will support unix first
        STORAGE_FILE_PATH = System.getProperty("user.home") + "/mable_data/storage.mable";
        STORAGE_FILE = new File(STORAGE_FILE_PATH);
        try {
            if (!STORAGE_FILE.exists())
                STORAGE_FILE.createNewFile();
        } catch (Exception e) {
            System.err.println("Failed to activate persistent storage!");
            active = false;
        }
    }

    public static void save() throws IOException {
        if (!active) throw new IOException("Storage is inactive.");
        FileOutputStream outputStream = new FileOutputStream(STORAGE_FILE);
        ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream);
        objOutputStream.writeObject(displayables);
        objOutputStream.flush();
        objOutputStream.close();
    }

    @SuppressWarnings("unchecked")
    public static void load() throws IOException, ClassNotFoundException {
        if (!active) throw new IOException("Storage is inactive.");
        FileInputStream inputStream = new FileInputStream(STORAGE_FILE);
        ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
        displayables = (TreeSet<DisplayBridge>) objInputStream.readObject();
        objInputStream.close();
    }

    public static DisplayBridge[] getDisplayables() {
        return (DisplayBridge[]) displayables.toArray();
    }
}
