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

import code.backend.data.Countdown;
import code.frontend.capabilities.concurrency.Updatable;
import code.frontend.libs.katlaf.lists.SimpleList;
import java.util.List;
import java.util.function.Function;
import javafx.scene.input.MouseButton;

/**
 * The UI component that displays a set of Countdowns.
 *
 * @since v3.0.0-beta
 * @see Countdown
 */
public final class CountdownList extends SimpleList implements Updatable {
    public enum CountdownFilter { ONGOING, COMPLETED, DELETED }

    private boolean populated;
    private final CountdownFilter filter;

    public CountdownList() {
        this(CountdownFilter.ONGOING);
    }

    public CountdownList(final CountdownFilter filter) {
        this.populated = false;
        this.filter = filter;
        this.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.getSelector().deselectAll();
                event.consume();
            }
        });
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    /**
     * Populates the current CountdownList, usually when it is empty (after instantiation)
     * <p>
     * This method can only be run once per CountdownList instance.
     * Any further calls to this method after it has been run will be ignored.
     */
    public void populate(final List<Countdown> countdowns) {
        if (this.populated)
            return;
        else
            this.populated = true;

        /*
         * muahahaha functional go brrr
         */
        final Function<Countdown, Boolean> verification = switch (this.filter) {
            case ONGOING -> countdown -> !countdown.isDone() && !countdown.isDeleted();
            case COMPLETED -> countdown -> countdown.isDone() && !countdown.isDeleted();
            case DELETED -> countdown -> countdown.isDeleted();
            default -> countdown -> false;
        };

        for (Countdown countdown : countdowns) {
            if (verification.apply(countdown)) {
                this.addMember(new CountdownListMember(countdown, this));
            }
        }
    }

    @Override
    public void update() {
        this.getMembers().forEach(member -> {
            if (member instanceof Updatable updatable) {
                updatable.update();
            }
        });
    }
}
