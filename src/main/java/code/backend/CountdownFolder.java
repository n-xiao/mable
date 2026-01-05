/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend;

import com.fasterxml.jackson.annotation.*;
import java.util.TreeSet;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class CountdownFolder {
    protected enum SpecialType { ALL_INCOMPLETE, ALL_COMPLETE }

    private String name;
    private final SpecialType TYPE;
    private final TreeSet<Countdown> CONTENTS;

    public CountdownFolder(String name) {
        this.name = name;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
        this.TYPE = null;
    }

    @JsonCreator
    public CountdownFolder(
        @JsonProperty("name") String name, @JsonProperty("contents") String[] contentAsIDs) {
        this.name = name;
        this.TYPE = null;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
        for (String id : contentAsIDs) {
            Countdown countdown = StorageHandler.getCountdownByID(id);
            if (countdown != null)
                this.CONTENTS.add(countdown);
        }
    }

    /**
     * This constructor creates a CountdownFolder that is used by Mable. It should not be added to
     * the set of FOLDERS to prevent accidental deletion by user actions. When instantiating through
     * this constructor, please ensure that you hold a reference to it.
     */
    protected CountdownFolder(SpecialType type) {
        assert type != null;
        switch (type) {
            case ALL_INCOMPLETE:
                this.name = "All Ongoing";
                break;
            case ALL_COMPLETE:
                this.name = "All Completed";
                break;
            default:
                break;
        }
        this.TYPE = type;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
    }

    public TreeSet<Countdown> getContents() {
        return this.CONTENTS;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("contents")
    public String[] getContentsAsIDs() {
        String[] stringIDs = new String[this.CONTENTS.size()];
        int index = 0;
        for (Countdown countdown : this.CONTENTS) {
            stringIDs[index] = countdown.getIdAsString();
            index++;
        }
        return stringIDs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isProtectedFolder() {
        return this.TYPE != null;
    }
}
