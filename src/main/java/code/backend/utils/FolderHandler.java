package code.backend.utils;

import code.backend.data.Countdown;
import code.backend.data.CountdownFolder;
import code.backend.data.CountdownFolder.SpecialType;
import java.util.Comparator;
import java.util.Stack;
import java.util.TreeSet;

public class FolderHandler {
    private static final TreeSet<CountdownFolder> FOLDERS =
        new TreeSet<CountdownFolder>(new Comparator<CountdownFolder>() {
            public int compare(CountdownFolder o1, CountdownFolder o2) {
                return o1.getName().compareTo(o2.getName());
            };
        });
    private static final Stack<CountdownFolder> DELETED_FOLDERS = new Stack<CountdownFolder>();
    // inits Mable's protected folders
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

    public static void organiseProtectedFolders() {
        COMPLETED_FOLDER.getContents().clear();
        INCOMPLETED_FOLDER.getContents().clear();
        for (Countdown countdown : CountdownHandler.getCountdowns()) {
            if (countdown.isDone()) {
                COMPLETED_FOLDER.getContents().add(countdown);
            } else {
                INCOMPLETED_FOLDER.getContents().add(countdown);
            }
        }
    }

    /**
     * This method is used for the renaming method.
     */
    public static boolean folderExists(String name) {
        for (CountdownFolder folder : FOLDERS) {
            if (folder.getName().equals(name))
                return true;
        }
        return false;
    }

    public static boolean createFolder(String name) {
        try {
            return FOLDERS.add(new CountdownFolder(name));
        } finally {
            StorageHandler.save();
        }
    }

    public static void removeFolder(String name) {
        FOLDERS.removeIf(folder -> folder.getName().equals(name));
    }

    public static void removeSelectedFolder() {
        if (currentlySelectedFolder.isProtectedFolder())
            return;
        FOLDERS.remove(currentlySelectedFolder);
        DELETED_FOLDERS.add(currentlySelectedFolder);
    }

    public static boolean renameFolder(String oldName, String newName) {
        for (CountdownFolder folder : FOLDERS) {
            if (folder.getName().equals(oldName)) {
                folder.setName(newName);
                return true;
            }
        }
        return false;
    }

    public static TreeSet<CountdownFolder> getFolders() {
        return FOLDERS;
    }

    public static Stack<CountdownFolder> getDeletedFolders() {
        return DELETED_FOLDERS;
    }

    public static CountdownFolder getCurrentlySelectedFolder() {
        return currentlySelectedFolder;
    }

    public static void setCurrentlySelectedFolder(CountdownFolder currentlySelectedFolder) {
        FolderHandler.currentlySelectedFolder = currentlySelectedFolder;
    }

    private FolderHandler() {}
}
