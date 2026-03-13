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

package code.frontend.capabilities.countdowns;

import code.backend.data.wrappers.CountdownPacket;
import code.frontend.capabilities.countdowns.CountdownList.CountdownFilter;
import code.frontend.libs.katlaf.FontHandler;
import code.frontend.libs.katlaf.dragndrop.DragStartRegion;
import code.frontend.libs.katlaf.menus.RightClickMenu;
import code.frontend.libs.katlaf.popup.Popup;
import code.frontend.libs.katlaf.ricing.RiceHandler;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

final class CountdownDragStarter extends DragStartRegion<CountdownPacket> {
    private final CountdownList list;
    private boolean installed;

    CountdownDragStarter(final CountdownList list) {
        this.list = list;
        this.installed = false;

        /*
         * implementation of right click behaviour
         */
        if (list.getFilter() != CountdownFilter.DELETED)
            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    final List<CountdownListMember> selectedMembers = list.getSelectedMembers();

                    if (selectedMembers.isEmpty())
                        return;

                    final Runnable edit = new Runnable() {
                        @Override
                        public void run() {
                            Popup.spawn(
                                new CountdownCreatorPopup(list, selectedMembers.getFirst()));
                        }
                    };

                    final Runnable delete = new Runnable() {
                        @Override
                        public void run() {
                            selectedMembers.forEach(member -> member.getCountdown().delete());
                            list.removeMembers(list.getSelectedCountdowns().getCountdownsAsSet());
                        }
                    };

                    if (selectedMembers.size() > 1) {
                        new RightClickMenu()
                            .addButton("Delete", delete)
                            .init(event.getSceneX(), event.getSceneY());
                    } else {
                        new RightClickMenu()
                            .addButton("Edit", edit)
                            .addButton("Delete", delete)
                            .init(event.getSceneX(), event.getSceneY());
                    }

                    event.consume();
                }
            });
        else
            this.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    final List<CountdownListMember> selectedMembers = list.getSelectedMembers();

                    if (selectedMembers.isEmpty())
                        return;

                    final Runnable recover = new Runnable() {
                        @Override
                        public void run() {
                            selectedMembers.forEach(member -> member.getCountdown().recover());
                            list.removeMembers(list.getSelectedCountdowns().getCountdownsAsSet());
                        }
                    };

                    new RightClickMenu()
                        .addButton("Recover", recover)
                        .init(event.getSceneX(), event.getSceneY());

                    event.consume();
                }
            });
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    @Override
    protected void onDragStart() {
        this.list.getSelectedMembers().forEach(member -> member.setOpacity(0.7));
    }

    @Override
    protected void cleanupOnDragEnd() {
        this.list.getSelectedMembers().forEach(member -> member.setOpacity(1));
    }

    @Override
    protected CountdownPacket getData() {
        return this.list.getSelectedCountdowns();
    }

    @Override
    protected Region getRepresentation() {
        final StackPane result = new StackPane();
        result.setBackground(RiceHandler.createBG(RiceHandler.getColour("darkgrey"), 2, 0));
        result.resize(160, 30);

        final int size = getData().getCountdowns().size();
        final String sizeString = size > 9 ? "9+" : Integer.toString(size);
        final Label label = new Label("Dragging " + sizeString + " Countdown(s)");
        label.setFont(FontHandler.getMono());
        label.setTextFill(RiceHandler.getColour("white"));
        label.setAlignment(Pos.CENTER);

        result.getChildren().add(label);

        return result;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Installs this drag detector on a specified CountdownListMember.
     * Note that this creates a one-to-one relationship, and any attempt
     * to install this instance on multiple CountdownListMember instances
     * will be ignored (after the first call).
     *
     * @param member    the CountdownListMember which this should be installed on
     *
     * @see CountdownListMember
     */
    public void install(final CountdownListMember member) {
        if (!this.installed) {
            this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            this.setOpacity(0);
            member.getChildren().addLast(this);
            this.setViewOrder(-1);
            this.installed = true;
            StackPane.setMargin(this, new Insets(0, 0, 0, 30));
        }
    }
}
