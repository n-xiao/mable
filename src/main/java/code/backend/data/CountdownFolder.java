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
    private String name;
    private int listIndex;
    private final TreeSet<Countdown> CONTENTS;

    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    public CountdownFolder(String name) {
        this.name = name;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
        super(UUID.randomUUID());
    }

    @JsonCreator
    public CountdownFolder(@JsonProperty("ID") String folderId, @JsonProperty("name") String name,
        @JsonProperty("contents") String[] contentAsIDs, @JsonProperty("listIndex") int index) {
        super(folderId);
        this.name = name;
        this.CONTENTS = new TreeSet<Countdown>(new SortByRemainingDays());
        this.listIndex = index;
        for (String id : contentAsIDs) {
            Countdown countdown = CountdownHandler.getCountdownByID(id);
            if (countdown != null)
                this.CONTENTS.add(countdown);
        }
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

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

    @JsonProperty("listIndex")
    @Override
    public int getPriority() {
        return listIndex;
    }

    public TreeSet<Countdown> getContents() {
        return this.CONTENTS;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*


     IMPLEMENTATIONS
    -------------------------------------------------------------------------------------*/

    @JsonIgnore
    @Override
    public String getDisplayString() {
        return name;
    }

    @Override
    public int compareTo(Listable listable) {
        if (listable instanceof CountdownFolder)
            return this.listIndex - ((CountdownFolder) listable).listIndex;
        else
            return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CountdownFolder))
            return false;

        CountdownFolder other = (CountdownFolder) obj;
        return other.name.equals(this.name);
    }

    @Override
    public void setListIndex(int index) {
        this.listIndex = index;
    }
}
