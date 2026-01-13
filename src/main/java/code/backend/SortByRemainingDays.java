/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend;

import java.time.LocalDate;
import java.util.Comparator;

public class SortByRemainingDays implements Comparator<Countdown> {
    @Override
    public int compare(Countdown c1, Countdown c2) {
        LocalDate now = LocalDate.now();
        int dist1 = c1.daysUntilDue(now);
        int dist2 = c2.daysUntilDue(now);
        int dateDiff = dist1 - dist2;
        int idDiff = c1.getID().compareTo(c2.getID()); // enforces uniqueness for sets
        return (dateDiff == 0) ? idDiff : dateDiff;
    }
}
