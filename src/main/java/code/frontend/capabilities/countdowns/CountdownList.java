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
import code.backend.data.wrappers.CountdownPacket;
import code.frontend.capabilities.concurrency.Updatable;
import code.frontend.libs.katlaf.interfaces.Colourable;
import code.frontend.libs.katlaf.lists.SimpleList;
import code.frontend.libs.katlaf.lists.SimpleListMember;
import code.frontend.libs.katlaf.transitions.Transitioner;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Duration;

/**
 * The UI component that displays a set of Countdowns.
 *
 * @since v3.0.0-beta
 * @see Countdown
 */
public final class CountdownList extends SimpleList implements Updatable {
    private static final int REMOVE_DELAY = 1500;
    public enum CountdownFilter { ONGOING, COMPLETED, DELETED }

    private final CountdownFilter filter;
    private final ObservableList<CountdownListMember> pendingRemoval;
    private final Transitioner rmTransitioner;

    public CountdownList() {
        this(CountdownFilter.ONGOING);
    }

    public CountdownList(final CountdownFilter filter) {
        this.filter = filter;
        this.rmTransitioner = new Transitioner().prepare();
        this.pendingRemoval = FXCollections.observableArrayList();
        this.pendingRemoval.addListener(new ListChangeListener<CountdownListMember>() {
            @Override
            public void onChanged(Change<? extends CountdownListMember> c) {
                if (pendingRemoval.isEmpty())
                    return;

                final FadeTransition[] fades = new FadeTransition[pendingRemoval.size()];
                int i = 0;
                for (CountdownListMember member : pendingRemoval) {
                    fades[i] = new FadeTransition(Duration.millis(200), member);
                    fades[i].setToValue(0);
                    i++;
                }

                rmTransitioner.prepare()
                    .hold(Duration.millis(REMOVE_DELAY))
                    .playParallel(fades)
                    .getTransition()
                    .setOnFinished(f -> {
                        pendingRemoval.forEach(CountdownList.super::removeMember);
                        pendingRemoval.clear();
                    });

                rmTransitioner.getTransition().playFromStart();
            }
        });
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/

    synchronized void requestMarkAsDone(final CountdownListMember member) {
        if (this.pendingRemoval.contains(member))
            abortRemoveMember(member);
        else
            removeMember(member);
        member.getCountdown().setDone(!member.getCountdown().isDone());
    }

    CountdownPacket getSelectedCountdowns() {
        final ArrayList<Countdown> list = new ArrayList<Countdown>();
        this.getSelectedMembers().forEach(member -> list.add(member.getCountdown()));
        return new CountdownPacket(list);
    }

    List<CountdownListMember> getSelectedMembers() {
        final List<SimpleListMember> simpleMembers = this.getMembers();
        final ArrayList<CountdownListMember> members = new ArrayList<CountdownListMember>();
        for (SimpleListMember simpleMember : simpleMembers) {
            if (simpleMember instanceof CountdownListMember member && member.isToggled()) {
                members.add(member);
            }
        }
        return members;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    @Override
    public synchronized void addMember(SimpleListMember member) {
        super.addMember(member);
        if (member instanceof CountdownListMember listMember) {
            final CountdownDragStarter dragStarter = new CountdownDragStarter(this);
            dragStarter.install(listMember);
        }
    }

    @Override
    public synchronized void removeMember(SimpleListMember member) {
        if (member instanceof CountdownListMember countdownMember) {
            if (this.rmTransitioner.getTransition() != null)
                this.rmTransitioner.getTransition().stop();
            this.pendingRemoval.add(countdownMember);
        }
    }

    public synchronized void abortRemoveMember(SimpleListMember member) {
        if (member instanceof CountdownListMember countdownListMember) {
            if (this.rmTransitioner.getTransition() != null)
                this.rmTransitioner.getTransition().stop();
            this.pendingRemoval.removeIf(m -> m.equals(countdownListMember));
        }
    }

    public boolean isPendingRemoval(final CountdownListMember member) {
        return this.pendingRemoval.contains(member);
    }

    /**
     * Populates the current CountdownList
     */
    public void populate(final Set<Countdown> countdowns) {
        this.clearMembers();
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

    public void colourCountdowns() {
        this.getMembers().forEach(simpleListMember -> {
            if (simpleListMember instanceof CountdownListMember
                && simpleListMember instanceof Colourable colourable) {
                colourable.resetColour();
            }
        });
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
