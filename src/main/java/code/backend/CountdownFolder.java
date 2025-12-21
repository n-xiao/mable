package code.backend;

import java.time.Instant;
import java.util.TreeSet;

import code.frontend.misc.DisplayBridge;

public class CountdownFolder implements DisplayBridge {
    private String name;
    private TreeSet<Countdown> map;

    public CountdownFolder(String name) {
        this.name = name;
        this.map = new TreeSet<Countdown>(new SortByRemainingDays());
    }

    @Override
    public Countdown[] getFolderContents() {
        return (Countdown[]) map.toArray();
    }

    public int daysUntilDue(Instant now) {
        // todo
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
