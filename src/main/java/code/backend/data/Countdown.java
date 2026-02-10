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

package code.backend.data;

import code.frontend.libs.katlaf.ricing.RiceHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Countdown extends Identifiable {
    private static final String ZONE_ID_STR = "UTC";
    /**
     * Calculates the number of days between two {@link LocalDate} instances.
     */
    public static int getDaysBetween(LocalDate date1, LocalDate date2) {
        ZonedDateTime zoned1 = date1.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        ZonedDateTime zoned2 = date2.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        Duration duration = Duration.between(zoned1, zoned2);
        return (int) duration.toDaysPart();
    }

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
        super(UUID.randomUUID());
        this.name = name;
        this.isDone = false;
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        this.completionDate = null;
        this.colour = RiceHandler.getColourString("white");
    }

    @JsonCreator
    public Countdown(@JsonProperty("ID") String id, @JsonProperty("name") String name,
        @JsonProperty("isDone") boolean isDone, @JsonProperty("due") String due,
        @JsonProperty("completed") String completed, @JsonProperty("colour") String colour) {
        super(id);
        this.name = name;
        this.isDone = isDone;
        this.dueDateTime = ZonedDateTime.parse(due);
        this.completionDate = ZonedDateTime.parse(completed);
        this.colour = colour;
    }

    /*


     BEHAVIOUR
    -------------------------------------------------------------------------------------*/

    /**
     * Converts a date from the user's timezone to UTC and
     * returns it as a ZonedDateTime.
     */
    private ZonedDateTime convertLocalDate(LocalDate date) {
        return date.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    private final ZonedDateTime dueDateTime;
    /*
     * Returns days until due. Keep in mind that this is a vector, so
     * a Countdown that is past due will return a negative integer.
     * Also, a day is not over until the last second of said day is over.
     */
    public int getDaysUntilDue(final LocalDate now) {
        return getDaysUntilDate(now, this.dueDateTime);
    }

    public int getDaysUntilCompletion(final LocalDate now) {
        if (this.completionDate == null)
            return 0;
        return getDaysUntilDate(now, this.completionDate);
    }

    public int getDaysUntilDate(final LocalDate now, final ZonedDateTime zonedDateTime) {
        final ZonedDateTime zonedNow = now.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        final Duration duration = Duration.between(zonedNow, zonedDateTime);
        return (int) duration.toDaysPart();
    }

    /**
     * Returns a {@link LocalDate} that represents the due date.
     * Useful for displaying the due date in the user's local date and
     * time.
     */
    @JsonIgnore
    public LocalDate getLocalDueDate(LocalDate now) {
        return now.plusDays(getDaysUntilDue(now));
    }

    /**
     * Returns a String representation of the current status
     * of this Countdown.
     */
    @JsonIgnore
    public String getStatus(LocalDate now) {
        if (isOverdue(now))
            return "Overdue";
        else if (isDueToday(now))
            return "Due today";
        else if (isDueTomorrow(now))
            return "Due tomorrow";
        else
            return "Ongoing";
    }

    @JsonIgnore
    public boolean isOverdue(LocalDate now) {
        ZonedDateTime nowDateTime = convertLocalDate(now);
        return this.dueDateTime.isBefore(nowDateTime);
    }

    @JsonIgnore
    public boolean isDueToday(LocalDate now) {
        ZonedDateTime nowDateTime = convertLocalDate(now);
        return this.dueDateTime.isEqual(nowDateTime);
    }

    @JsonIgnore
    public boolean isDueTomorrow(LocalDate now) {
        ZonedDateTime nowDateTime = convertLocalDate(now);
        return this.dueDateTime.isEqual(nowDateTime.plusDays(1));
    }

    @JsonProperty("name") private final String name;
    public String getName() {
        return this.name;
    }

    @JsonProperty("due")
    public ZonedDateTime getDueDateTime() {
        return this.dueDateTime;
    }

    @JsonProperty("completed") private ZonedDateTime completionDate;
    public ZonedDateTime getCompletionDateTime() {
        return this.completionDate;
    }

    @JsonProperty("isDone") private boolean isDone;
    public boolean isDone() {
        return this.isDone;
    }

    public void setDone(final boolean isDone) {
        if (!this.isDone && isDone) {
            this.completionDate = convertLocalDate(LocalDate.now());
        }
        this.isDone = isDone;
    }

    /**
     * This is the custom colour that is picked by the user for
     * this Countdown. The default value is "white".
     */
    @JsonProperty("colour") private String colour;
    public String getColour() {
        return this.colour;
    }

    public void setColour(final String colour) {
        this.colour = colour;
    }
}
