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

package code.backend.utils;

import code.backend.data.CountdownFolder;
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

    public static TreeSet<CountdownFolder> getFolders() {
        return FOLDERS;
    }

    public static Stack<CountdownFolder> getDeletedFolders() {
        return DELETED_FOLDERS;
    }

    private FolderHandler() {}
}
