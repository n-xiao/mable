/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend.data;

import code.backend.utils.CountdownHandler;
import code.backend.utils.SortByRemainingDays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.TreeSet;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountdownFolder extends Identifiable {
    public enum SpecialType { ALL_INCOMPLETE, ALL_COMPLETE }

    private String name;
    @JsonIgnore private final SpecialType TYPE;
    private final TreeSet<Countdown> CONTENTS;

    public CountdownFolder(String name) {
        this.name = name;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
        this.TYPE = null;
        super(UUID.randomUUID());
    }

    @JsonCreator
    public CountdownFolder(@JsonProperty("ID") String folderId, @JsonProperty("name") String name,
        @JsonProperty("contents") String[] contentAsIDs) {
        super(folderId);
        this.name = name;
        this.TYPE = null;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
        for (String id : contentAsIDs) {
            Countdown countdown = CountdownHandler.getCountdownByID(id);
            if (countdown != null)
                this.CONTENTS.add(countdown);
        }
    }

    /**
     * This constructor creates a CountdownFolder that is used by Mable. It should not be added to
     * the set of FOLDERS to prevent accidental deletion by user actions. When instantiating through
     * this constructor, please ensure that you hold a reference to it.
     */
    public CountdownFolder(SpecialType type) {
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
        super(UUID.randomUUID());
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
            stringIDs[index] = countdown.getID().toString();
            index++;
        }
        return stringIDs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpecialType getType() {
        return TYPE;
    }

    @JsonIgnore
    public boolean isProtectedFolder() {
        return this.TYPE != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CountdownFolder))
            return false;

        CountdownFolder other = (CountdownFolder) obj;
        return other.name.equals(this.name);
    }
}
