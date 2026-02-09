package code.frontend.capabilities.countdown.components;

import code.backend.data.Countdown;
import code.frontend.libs.katlaf.FormatHandler;
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
    protected String getStringDueDate(LocalDate now) {
        final String day = Integer.toString(getCountdown().getCompletionDateTime().getDayOfYear());
        final String month =
            Integer.toString(getCountdown().getCompletionDateTime().getMonthValue());
        final String year = Integer.toString(getCountdown().getCompletionDateTime().getYear());
        return "On: " + day + "/" + month + "/" + year; // TODO: MAKE THIS CONFIGURABLE
    }

    @Override
    protected String getDaysLeftString(LocalDate now) {
        return FormatHandler.intToString(Math.abs(this.getCountdown().getDaysUntilCompletion(now)));
    }
}
