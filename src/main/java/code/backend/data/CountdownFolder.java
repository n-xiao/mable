/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.backend.data;

import code.backend.utils.CountdownHandler;
import code.backend.utils.FolderHandler;
import code.backend.utils.SortByRemainingDays;
import code.frontend.libs.katlaf.lists.Listable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.TreeSet;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountdownFolder extends Identifiable implements Listable {
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

    @JsonIgnore
    @Override
    public String getDisplayLabel() {
        return name;
    }

    @Override
    public void onButtonClick() {
        FolderHandler.setCurrentlySelectedFolder(this);
    }

    @JsonIgnore
    @Override
    public boolean isEngaged() {
        return FolderHandler.getCurrentlySelectedFolder().equals(this);
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
