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

import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.HashSet;
import java.util.Stack;
import javafx.scene.paint.Color;

public final class FolderHandler {
    private static final HashSet<CountdownFolder> FOLDERS = new HashSet<CountdownFolder>();
    private static final Stack<CountdownFolder> DELETED_FOLDERS = new Stack<CountdownFolder>();

    static CountdownFolder createFolder(final String name) {
        final CountdownFolder folder = new CountdownFolder(name);
        FOLDERS.add(folder);
        StorageHandler.save();
        return folder;
    }

    static void removeFolder(final CountdownFolder folder) {
        FOLDERS.remove(folder);
    }

    static HashSet<CountdownFolder> getFolders() {
        return FOLDERS;
    }

    static Stack<CountdownFolder> getDeletedFolders() {
        return DELETED_FOLDERS;
    }

    static void eraseCountdown(final Countdown countdown) {
        FOLDERS.forEach(folder -> folder.getContents().removeIf(c -> c.equals(countdown)));
        DELETED_FOLDERS.forEach(folder -> folder.getContents().removeIf(c -> c.equals(countdown)));
    }

    static Color lookupColour(final Countdown countdown) {
        for (CountdownFolder countdownFolder : FOLDERS) {
            if (countdownFolder.getContents().contains(countdown))
                return countdownFolder.getColour();
        }
        return RiceHandler.getColour("white");
    }

    private FolderHandler() {}
}
