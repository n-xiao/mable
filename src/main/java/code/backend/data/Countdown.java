/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend.data;

import code.backend.utils.CountdownHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Countdown extends Identifiable {
    public enum Urgency { OVERDUE, TODAY, TOMORROW, ONGOING, COMPLETED }

    private String name;
    private boolean isDone;
    private ZonedDateTime dueDateTime;

    private static final String ZONE_ID_STR = "UTC";

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
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

    @JsonCreator
    public Countdown(@JsonProperty("ID") String id, @JsonProperty("name") String name,
        @JsonProperty("isDone") boolean isDone, @JsonProperty("due") String due) {
        super(id);
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

    @JsonProperty("isDone")
    public boolean isDone() {
        return isDone;
    }

    @JsonProperty("due")
    public ZonedDateTime getDueDateTime() {
        return dueDateTime;
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

    public void setDueDate(LocalDate dueDate) {
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        // to keep the order
        CountdownHandler.getCountdowns().remove(this);
        CountdownHandler.getCountdowns().add(this);
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
