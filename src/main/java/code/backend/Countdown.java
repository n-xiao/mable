// PLEASE REWRITE THIS, THANKS LUV U


package code.backend;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import code.frontend.misc.DisplayBridge;

public class Countdown implements DisplayBridge
{
    private String name = null;
    private ZonedDateTime dueDateTime;
    private boolean isDone = false;
    public final UUID ID; // represents unique id
    private final String ZONE_ID_STR = "UTC";

    // todo String parser

    /**
     * Creates a Countdown object. The responsibility for sanitising inputs is handed to
     * the frontend; it is assumed that all values passed to this constructor will not
     * cause errors due to incompatible types (e.g integer over/under flow).
     */
    public Countdown(String name, int day, int month, int year)
    {
        this.ID = UUID.randomUUID();
        this.name = name;
        LocalDate dueDate = LocalDate.of(year, month, day);
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

    /*
     * Returns days until due. Keep in mind that this is a vector.
     */
    public int daysUntilDue(LocalDate now)
    {
        ZonedDateTime zonedNow = now.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        Duration duration = Duration.between(zonedNow, this.dueDateTime);
        return (int) duration.toDaysPart();
    }

    @Override
    public Countdown[] getFolderContents()
    {
        // indicates that this is not a folder
        return null;
    }

    public LocalDate getLocalDueDate(LocalDate now)
    {
        return now.plusDays(daysUntilDue(now));
    }

    public String getStringDueDate(LocalDate now)
    {
        LocalDate localDue = getLocalDueDate(now);
        String day = Integer.toString(localDue.getDayOfMonth());
        String month = Integer.toString(localDue.getMonthValue());
        String year = Integer.toString(localDue.getYear());
        return day + "/" + month + "/" + year; // uses the correct format
    }

    public String getName()
    {
        return name;
    }

    public ZonedDateTime getDueDateTime()
    {
        return dueDateTime;
    }

    public String getStatusString(LocalDate now)
    {
        if (isOverdue(now))
            return "Overdue";
        else if (this.isDone)
            return "Completed";
        else
            return "Ongoing";
    }

    public boolean isOverdue(LocalDate now)
    {
        ZonedDateTime nowDateTime = now.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
        return dueDateTime.isBefore(nowDateTime) && !isDone;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDueDate(int day, int month, int year)
    {
        LocalDate dueDate = LocalDate.of(year, month, day);
        this.dueDateTime = dueDate.atTime(0, 0).atZone(ZoneId.of(ZONE_ID_STR));
    }

}
