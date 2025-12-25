package code.frontend.misc;

import java.time.LocalDate;

import code.backend.Countdown;

public interface DisplayBridge
{
    public int daysUntilDue(LocalDate now);
    public String getName(); // for UI display purposes
    public Countdown[] getFolderContents(); // only for folders, null otherwise
    // maybe composition for ui? meaning strictly no ui element without a Countdown
}
