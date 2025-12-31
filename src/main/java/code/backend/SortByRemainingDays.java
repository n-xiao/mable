/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
        int idDiff = c1.ID.compareTo(c2.ID); // enforces uniqueness for sets
        return (dateDiff == 0) ? idDiff : dateDiff;
    }
}
