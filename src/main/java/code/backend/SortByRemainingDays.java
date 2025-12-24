package code.backend;

import java.time.Instant;
import java.util.Comparator;

import code.frontend.misc.DisplayBridge;

public class SortByRemainingDays implements Comparator<DisplayBridge>
{
    @Override
    public int compare(DisplayBridge d1, DisplayBridge d2)
    {
        Instant now = Instant.now();
        int dist1 = d1.daysUntilDue(now);
        int dist2 = d2.daysUntilDue(now);
        return dist1 - dist2;
    }
}
