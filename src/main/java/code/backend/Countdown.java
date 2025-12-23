package code.backend;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import code.frontend.misc.DisplayBridge;

public class Countdown implements DisplayBridge {
    private String name = null;
    private Instant deadline = null;
    boolean isDone = false;
    public final UUID ID; // represents unique id

    // todo String parser
    private Countdown(String name, LocalDate rawDate) {
        this.ID = UUID.randomUUID(); // hopefully no collision occurs
        this.name = name;
        LocalDate now = LocalDate.now();
        long dist = ChronoUnit.DAYS.between(now, rawDate); // gets days between now and due
        this.deadline = Instant.now().plus(dist, ChronoUnit.DAYS); // inits due date in UTC
    }

    public static Countdown create(String name, int day, int month, int year) {
        LocalDate dueDate = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        long dist = ChronoUnit.DAYS.between(now, dueDate);
        if (!Guardian.integerCanFit(dist)) return null;
        Countdown countdown = new Countdown(name, dueDate);
        return countdown;
    }

    /*
     * Returns days until due. Keep in mind that this is a vector.
     */
    public int daysUntilDue(Instant now) {
        // Instant now has to be provided to "freeze" time during ops
        double days = (double) ChronoUnit.DAYS.between(now, this.deadline);
        return (int) ((days < 0) ? days : ++days);
    }

    @Override
    public Countdown[] getFolderContents() {
        // indicates that this is not a folder
        return null;
    }

    public LocalDate getLocalDueDate(Instant now, LocalDate today) {
        long dist = daysUntilDue(now);
        return today.plusDays(dist);
    }

    public String getStringDueDate(Instant now, LocalDate today) {
        LocalDate localDue = getLocalDueDate(now, today);
        String day = Integer.toString(localDue.getDayOfMonth());
        String month = Integer.toString(localDue.getMonthValue());
        String year = Integer.toString(localDue.getYear());
        return day + "/" + month + "/" + year; // uses the correct format
    }

    public String getName() {
        return name;
    }

    public Instant getDueDate() {
        return deadline;
    }

    public String getStatusString(Instant now) {
        if (isOverdue(now))
            return "Overdue";
        else if (this.isDone)
            return "Completed";
        else
            return "Ongoing";
    }

    public boolean isOverdue(Instant now) {
        return deadline.isBefore(now) && !isDone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean setDueDate(int day, int month, int year) {
        LocalDate dueDate = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        long dist = ChronoUnit.DAYS.between(now, dueDate);
        if (!Guardian.integerCanFit(dist)) return false;
        this.deadline = Instant.now().plus(dist, ChronoUnit.DAYS);
        return true;
    }

}
