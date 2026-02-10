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
import code.frontend.libs.katlaf.dragndrop.DragStopRegion;
import code.frontend.libs.katlaf.graphics.BorderedRegion;
import code.frontend.libs.katlaf.graphics.CustomLine;
import code.frontend.libs.katlaf.graphics.CustomLine.Type;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

abstract class DraggableListMember extends SimpleListMember {
    private final DraggableList<?> parentList;
    public DraggableListMember(final Listable listable, final DraggableList<?> list) {
        super(listable);
        // create and attach the dragDetector
        DragDetector dragDetector = new DragDetector();
        PlacementHelper placementHelper = new PlacementHelper();

        this.parentList = list;
        this.widthProperty().addListener((event) -> adjustDetectors(dragDetector, placementHelper));
        this.heightProperty().addListener(
            (event) -> adjustDetectors(dragDetector, placementHelper));
        this.getChildren().addAll(dragDetector, placementHelper);
    }

    /*


     STYLING
    -------------------------------------------------------------------------------------*/

    private void adjustDetectors(Region... regions) {
        for (Region region : regions) {
            region.resizeRelocate(0, 0, this.getWidth(), this.getHeight());
        }
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    /**
     * A generalised implementation of reordering of Listables.
     */
    private class DragDetector extends DragStartRegion<Listable> {
        DragDetector() {
            this.setOpacity(0);
            this.setManaged(false);
            this.setViewOrder(-10);
        }

        @Override
        public Listable getData() {
            return DraggableListMember.this.getListable();
        }

        @Override
        public void onDragStart() {
            DraggableListMember.this.setOpacity(0.5);
        }

        @Override
        public void cleanupOnDragEnd() {
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

    /**
     * When the user is hovering over this instance whilst
     * dragging another {@link DraggableListMember}, the
     * {@link PlacementHelper} is the UI component that
     * facilitates highlighting that is shown inbetween
     * members to represent where the new position of
     * the {@link DraggableListMember} would be if it
     * were to be dropped.
     */
    private class PlacementHelper extends VBox {
        final Color highlight;
        PlacementHelper() {
            this.setBackground(null);
            this.setFillWidth(true);
            this.setViewOrder(-12);
            this.setManaged(false);
            this.highlight = RiceHandler.getColour("orange");

            final UpperLineGuide upper = new UpperLineGuide();
            final LowerLineGuide lower = new LowerLineGuide();
            VBox.setVgrow(upper, Priority.SOMETIMES);
            VBox.setVgrow(lower, Priority.SOMETIMES);
            this.getChildren().addAll(upper, lower);
        }

        class UpperLineGuide extends LineGuide {
            @Override
            public void onDragStop(MouseDragEvent event) {
                reorder(getListable().getListIndex());
            }

            @Override
            void positionLine(CustomLine line) {
                line.resizeRelocate(
                    0, 0, PlacementHelper.this.getWidth(), PlacementHelper.this.getHeight());
            }
        }

        class LowerLineGuide extends LineGuide {
            @Override
            public void onDragStop(MouseDragEvent event) {
                reorder(getListable().getListIndex() + 1);
            }

            @Override
            void positionLine(CustomLine line) {
                line.resizeRelocate(0, PlacementHelper.this.getHeight() - 3,
                    PlacementHelper.this.getWidth(), PlacementHelper.this.getHeight());
            }
        }

        abstract class LineGuide extends DragStopRegion<Listable> {
            LineGuide() {
                this.setBackground(null);
                this.setOpacity(0);
                this.prefWidthProperty().bind(PlacementHelper.this.widthProperty());
                this.setMaxHeight(Double.MAX_VALUE);
                this.setMinHeight(3);
                CustomLine line = new CustomLine(2, Type.HORIZONTAL);
                line.setStrokeColour(PlacementHelper.this.highlight);
                this.opacityProperty().addListener(
                    (observable, oldValue, newValue) -> { positionLine(line); });
            }

            /**
             * Varies based on whether this instance represents the bottom or
             * top of a {@link DraggableListMember}.
             */
            abstract void positionLine(CustomLine line);

            protected void reorder(int index) {
                final Listable receivedListable = retrieveData();
                if (receivedListable != null) {
                    DraggableListMember.this.parentList.reorderMember(receivedListable, index);
                }
            }

            @Override
            public Class<Listable> getExpectedType() {
                return Listable.class;
            }

            @Override
            public void onDragRegionEnter(MouseDragEvent event) {
                this.setOpacity(1);
            }

            @Override
            public void onDragRegionExit(MouseDragEvent event) {
                this.setOpacity(0);
            }
        }
    }
}
