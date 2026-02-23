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

import code.backend.data.interfaces.Recoverable;
import code.frontend.libs.katlaf.lists.Listable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import javafx.scene.paint.Color;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Countdown extends Identifiable implements Listable<Countdown>, Recoverable {
    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

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
    }

    @JsonCreator
    public Countdown(@JsonProperty("ID") String id, @JsonProperty("name") String name,
        @JsonProperty("isDone") boolean isDone, @JsonProperty("due") String due,
        @JsonProperty("completed") String completed) {
        super(id);
        this.name = name;
        this.isDone = isDone;
        this.dueDateTime = ZonedDateTime.parse(due);
        this.completionDate = ZonedDateTime.parse(completed);
    }

    /*


     PRIVATE API
    -------------------------------------------------------------------------------------*/
    private static final String ZONE_ID_STR = "UTC";

    private static int getDaysUntilDate(final LocalDate now, final ZonedDateTime zonedDateTime) {
        final ZonedDateTime zonedNow = now.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        final Duration duration = Duration.between(zonedNow, zonedDateTime);
        return (int) duration.toDaysPart();
    }

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

    /**
     * Returns a {@link LocalDate} that represents the due date.
     * Useful for displaying the due date in the user's local date and
     * time.
     */
    @JsonIgnore
    public LocalDate getLocalDueDate(LocalDate now) {
        return now.plusDays(getDaysUntilDue(now));
    }

    public LocalDate getLocalCompletionDate(LocalDate now) {
        return now.plusDays(getDaysUntilCompletion(now));
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

    /**
     * Updates the completion date to the return value of LocalDate.now().
     */
    public void updateCompletionDateTime() {
        this.completionDate = convertLocalDate(LocalDate.now());
    }

    @JsonProperty("isDone") private boolean isDone;
    public boolean isDone() {
        return this.isDone;
    }

    public void setDone(final boolean isDone) {
        this.isDone = isDone;
        if (this.isDone)
            this.completionDate = convertLocalDate(LocalDate.now());
    }

    @JsonIgnore
    public Color getColour() {
        return FolderHandler.lookupColour(this);
    }

    @Override
    public void delete() {
        CountdownHandler.deleteCountdown(this);
    }

    /**
     * Removes this Countdown instance from all possible storage mediums. Note that this method
     * should only be called on a Countdown that has already been deleted. This operation cannot be
     * undone.
     * <p>
     * Note that a save to persistent storage is done before erasing the runtime storage
     * because the save procedure utilises the collection of deleted Countdowns to remove it from
     * the json file.
     *
     * @see StorageHandler#save()
     */
    @Override
    public void deleteForever() {
        if (this.isDeleted()) {
            StorageHandler.save();
            CountdownHandler.eraseCountdown(this);
            FolderHandler.eraseCountdown(this);
        }
    }

    @Override
    public boolean isDeleted() {
        return CountdownHandler.isCountdownDeleted(this);
    }

    @Override
    public void recover() {
        CountdownHandler.recoverCountdown(this);
    }

    @Override
    public int compareTo(Countdown other) {
        final LocalDate now = LocalDate.now();
        final int daysDiff = this.getDaysUntilDue(now) - other.getDaysUntilDue(now);
        return daysDiff == 0 ? this.getID().compareTo(other.getID()) : daysDiff;
    }

    @Override
    public String getDisplayString() {
        return this.getName();
    }
}
