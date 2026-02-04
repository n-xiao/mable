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

package code.frontend.libs.katlaf.lists;

import code.frontend.libs.katlaf.dragndrop.DragStartRegion;
import code.frontend.libs.katlaf.graphics.BorderedRegion;
import javafx.scene.layout.Region;

public abstract class DraggableListMember<T> extends SimpleListMember {
    public DraggableListMember(Listable listable) {
        super(listable);
        // create and attach the dragDetector
        DragDetector dragDetector = new DragDetector();
        this.widthProperty().addListener(
            (event) -> dragDetector.resizeRelocate(0, 0, this.getWidth(), this.getHeight()));
        this.heightProperty().addListener(
            (event) -> dragDetector.resizeRelocate(0, 0, this.getWidth(), this.getHeight()));
        dragDetector.setManaged(false);
        dragDetector.setViewOrder(-10);
        this.getChildren().add(dragDetector);
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    abstract T getData();

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class DragDetector extends DragStartRegion<T> {
        @Override
        public T getData() {
            return DraggableListMember.this.getData(); // other guy's problem haha
        }

        @Override
        public void onDragStart() {
            DraggableListMember.this.setOpacity(0.5);
        }

        @Override
        public void onDragEnd() {
            DraggableListMember.this.setOpacity(1);
        }

        @Override
        public Region getRepresentation() {
            return new Representation();
        }

        private class Representation extends BorderedRegion {
            Representation() {
                super(1.5, 0.8, 0.1);
                this.setPrefSize(90, 15);
                // TODO properly later
            }
        }
    }
}
