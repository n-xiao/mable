package code.frontend.capabilities.countdown.components;

import code.backend.data.Countdown;
import java.time.LocalDate;

public class CompletedPane extends CountdownPane {
    public CompletedPane(final Countdown cd, final LocalDate now) {
        super(cd, now);
    }

    @Override
    protected String getTextAdverb(boolean isOverdue) {
        return "AGO";
    }

    @Override
    protected String getStatusString(LocalDate now) {
        return "Completed";
    }

    @Override
    protected String getDueString(LocalDate now) {
        // TODO
        return null;
    }
}
