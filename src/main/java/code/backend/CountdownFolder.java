package code.backend;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class CountdownFolder {
    private static final TreeSet<CountdownFolder> FOLDERS =
        new TreeSet<CountdownFolder>(new Comparator<CountdownFolder>() {
            public int compare(CountdownFolder o1, CountdownFolder o2) {
                return o1.name.compareTo(o2.name);
            };
        });

    public static NavigableSet<CountdownFolder> getFolders(boolean isDescendingOrder) {
        final NavigableSet<CountdownFolder> SET = FOLDERS.descendingSet();
        return isDescendingOrder ? SET : SET.reversed();
    }

    private String name;
    private final TreeSet<Countdown> CONTENTS;

    public CountdownFolder(String name) {
        this.name = name;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
        FOLDERS.add(this);
    }

    public void addCountdown(Countdown countdown) {
        CONTENTS.add(countdown);
    }

    public NavigableSet<Countdown> getContents(boolean isDescendingOrder) {
        final NavigableSet<Countdown> SET = CONTENTS.descendingSet();
        return isDescendingOrder ? SET : SET.reversed();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
