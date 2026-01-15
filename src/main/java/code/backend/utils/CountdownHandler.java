/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend.utils;

import code.backend.data.Countdown;
import code.backend.data.Countdown.Urgency;
import java.time.LocalDate;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.Stack;
import java.util.TreeSet;

public class CountdownHandler {
    private static final TreeSet<Countdown> COUNTDOWNS =
        new TreeSet<Countdown>(new SortByRemainingDays());
    private static final Stack<Countdown> DELETED_COUNTDOWNS = new Stack<Countdown>();

    public static TreeSet<Countdown> getCountdowns() {
        return COUNTDOWNS;
    }

    public static Stack<Countdown> getDeletedCountdowns() {
        return DELETED_COUNTDOWNS;
    }

    /**
     * This method is designed to be called during runtime, by user interaction.
     * It should never be called during load operations. Use the getCountdowns.add()
     * method for that.
     */
    public static void addCountdown(Countdown c) {
        COUNTDOWNS.add(c);
        FolderHandler.getCurrentlySelectedFolder().getContents().add(c);
        if (FolderHandler.getCurrentlySelectedFolder().equals(FolderHandler.getCompletedFolder()))
            c.setDone(true);
        StorageHandler.save();
    }

    public static void deleteCountdowns(Collection<Countdown> countdowns) {
        COUNTDOWNS.removeAll(countdowns);
        FolderHandler.getFolders().forEach(
            folder -> { folder.getContents().removeAll(countdowns); });
        DELETED_COUNTDOWNS.addAll(countdowns);
    }

    public static Countdown getCountdownByID(String id) {
        for (Countdown countdown : COUNTDOWNS) {
            if (countdown.getID().toString().equals(id))
                return countdown;
        }
        return null;
    }

    public static NavigableSet<Countdown> getDescendingCountdowns() {
        return FolderHandler.getCurrentlySelectedFolder().getContents().descendingSet();
    }

    public static NavigableSet<Countdown> getAscendingCountdowns() {
        return getDescendingCountdowns().reversed();
    }

    public static int getStatistic(Urgency urgency) {
        LocalDate now = LocalDate.now();
        int stat = 0;
        for (Countdown countdown : COUNTDOWNS) {
            switch (urgency) {
                case OVERDUE:
                    if (countdown.isOverdue(now))
                        stat++;
                    break;
                case TODAY:
                    if (countdown.isDueToday(now))
                        stat++;
                    break;
                case TOMORROW:
                    if (countdown.isDueTomorrow(now))
                        stat++;
                    break;
                case COMPLETED:
                    if (countdown.isDone())
                        stat++;
                    break;
                default:
                    stat++;
                    break;
            }
        }
        return stat;
    }

    private CountdownHandler() {}
}
