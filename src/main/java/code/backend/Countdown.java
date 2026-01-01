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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Countdown {
    public enum Urgency { OVERDUE, TODAY, TOMORROW, ONGOING, COMPLETED }
    private String name = null;
    private boolean isDone = false;
    private ZonedDateTime dueDateTime;
    public final UUID ID; // represents unique id
    private static final String ZONE_ID_STR = "UTC";

    // todo String parser

    /**
     * Creates a Countdown object. The responsibility for sanitising inputs is handed to
     * the frontend; it is assumed that all values passed to this constructor will not
     * cause errors due to incompatible types (e.g integer over/under flow).
     */
    public Countdown(String name, int day, int month, int year) {
        LocalDate dueDate = LocalDate.of(year, month, day);
        this(name, dueDate);
    }

    public Countdown(String name, LocalDate dueDate) {
        this.ID = UUID.randomUUID();
        this.name = name;
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

    @JsonCreator
    public Countdown(@JsonProperty("id") String id, @JsonProperty("name") String name,
        @JsonProperty("isDone") boolean isDone, @JsonProperty("due") String due) {
        this.ID = UUID.fromString(id);
        this.name = name;
        this.isDone = isDone;
        this.dueDateTime = ZonedDateTime.parse(due);
    }

    public static int getDaysBetween(LocalDate date1, LocalDate date2) {
        ZonedDateTime zoned1 = date1.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        ZonedDateTime zoned2 = date2.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        Duration duration = Duration.between(zoned1, zoned2);
        return (int) duration.toDaysPart();
    }

    /*
     * Returns days until due. Keep in mind that this is a vector.
     */
    public int daysUntilDue(LocalDate now) {
        ZonedDateTime zonedNow = now.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        Duration duration = Duration.between(zonedNow, this.dueDateTime);
        return (int) duration.toDaysPart();
    }

    public LocalDate getLocalDueDate(LocalDate now) {
        return now.plusDays(daysUntilDue(now));
    }

    public String getStringDueDate(LocalDate now) {
        LocalDate localDue = getLocalDueDate(now);
        String day = Integer.toString(localDue.getDayOfMonth());
        String month = Integer.toString(localDue.getMonthValue());
        String year = Integer.toString(localDue.getYear());
        return day + "/" + month + "/" + year; // uses the correct format
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("id")
    public String getIdAsString() {
        return this.ID.toString();
    }

    @JsonProperty("isDone")
    public boolean isDone() {
        return isDone;
    }

    @JsonProperty("due")
    public ZonedDateTime getDueDateTime() {
        return dueDateTime;
    }

    @Deprecated
    public String getStatusString(LocalDate now) {
        if (isOverdue(now))
            return "Overdue";
        else if (this.isDone)
            return "Completed";
        else
            return "Ongoing";
    }

    public Urgency getUrgency(LocalDate now) {
        if (isOverdue(now))
            return Urgency.OVERDUE;
        else if (isDueToday(now))
            return Urgency.TODAY;
        else if (isDueTomorrow(now))
            return Urgency.TOMORROW;
        else if (isDone)
            return Urgency.COMPLETED;
        else
            return Urgency.ONGOING;
    }

    public boolean isOverdue(LocalDate now) {
        ZonedDateTime nowDateTime = convertLocalDate(now);
        return dueDateTime.isBefore(nowDateTime) && !isDone;
    }

    public boolean isDueToday(LocalDate now) {
        ZonedDateTime nowDateTime = convertLocalDate(now);
        return dueDateTime.isEqual(nowDateTime) && !isDone;
    }

    public boolean isDueTomorrow(LocalDate now) {
        ZonedDateTime nowDateTime = convertLocalDate(now);
        return dueDateTime.isEqual(nowDateTime.plusDays(1)) && !isDone;
    }

    private ZonedDateTime convertLocalDate(LocalDate date) {
        return date.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDueDate(int day, int month, int year) {
        LocalDate dueDate = LocalDate.of(year, month, day);
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Countdown))
            return false;
        else {
            Countdown other = (Countdown) obj;
            return other.ID.equals(this.ID);
        }
    }
}
