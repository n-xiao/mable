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

package code.frontend.libs.katlaf.dragndrop;

import code.frontend.MainContainer;
import javafx.scene.CacheHint;
import javafx.scene.layout.Region;

/**
 * This is an unmanaged Region which implements drag and drop
 * visuals, as well as facilitates some communication between
 * {@link DragStarter} instances and {@link DragStopper}
 * instances.
 *
 * Note: it should be impossible for a user to resize a window
 * whilst dragging and dropping something. Hence that case
 * will not be handled unless enough geniuses convince me otherwise.
 */
final class DragDropOverlay extends Region {
    private static DragDropOverlay activeOverlay = null; // max of one instance should exist
    private final DragStarter<?> dragStarter;

    private DragDropOverlay(DragStarter<?> dragStarter) {
        DragDropOverlay.activeOverlay = this;
        this.dragStarter = dragStarter;
        init();
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    /**
     * Initialises this {@link DragDropOverlay} and adds it to the {@link MainContainer}.
     */
    private void init() {
        // hijack scene
        final MainContainer mc = MainContainer.getInstance();
        mc.getScene().setOnMouseDragOver((event) -> {
            // must be attached to scene so nodes can still detect drag release, enter and exit
            dragStarter.getRepresentation().relocate(event.getSceneX(), event.getSceneY());
        });

        // additional config
        this.setMouseTransparent(true);
        this.setManaged(false);
        this.resize(mc.getScene().getWidth(), mc.getScene().getHeight());
        this.setViewOrder(-200);

        dragStarter.getRepresentation().setCacheHint(CacheHint.SPEED);
        this.getChildren().add(dragStarter.getRepresentation());
        mc.getChildren().add(this);
    }

    /*


     UTILITIES
    -------------------------------------------------------------------------------------*/

    /**
     * Spawns a new {@link DragDropOverlay}
     */
    public static void spawnOverlay(DragStarter<?> dragStarter) {
        new DragDropOverlay(dragStarter);
    }

    /**
     * Kills the current instance of {@link DragDropOverlay},
     * removing it from the {@link MainContainer}.
     */
    public static void killOverlay() {
        MainContainer.getInstance().getChildren().remove(activeOverlay);
        activeOverlay = null;
    }

    /**
     * A method which compares the class of the data that is being
     * transferred by the current drag and drop process with a provided
     * class.
     *
     * @return true if the class of the data is equal to the provided class.
     */
    protected static <E> boolean checkMatchingTypes(Class<? extends E> c) {
        if (activeOverlay.dragStarter == null || activeOverlay.dragStarter.getData() == null)
            return false; // if null checks fail
        return activeOverlay.dragStarter.getData().getClass().equals(c);
    }
}
