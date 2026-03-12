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

import code.frontend.libs.katlaf.ricing.Colour;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Legend extends Identifiable implements Comparable<Legend> {
    private String name;
    private Colour colour;
    private int index;
    private final TreeSet<Countdown> contents;

    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    public Legend(String name) {
        this.name = name;
        this.contents = new TreeSet<Countdown>(new SortByRemainingDays());
        this.colour = new Colour("black");
        this.index = 0;
        super(UUID.randomUUID());
    }

    @JsonCreator
    public Legend(@JsonProperty("ID") String legendId, @JsonProperty("name") String name,
        @JsonProperty("contents") String[] contentAsIDs, @JsonProperty("colour") String colour,
        @JsonProperty("index") int index) {
        super(legendId);
        this.name = name;
        this.contents = new TreeSet<Countdown>(new SortByRemainingDays());
        this.colour = new Colour(colour);
        this.index = index;
        for (String id : contentAsIDs) {
            Countdown countdown = CountdownHandler.getCountdownByID(id);
            if (countdown != null)
                this.contents.add(countdown);
        }
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("contents")
    public String[] getContentsAsIDs() {
        String[] stringIDs = new String[this.contents.size()];
        int index = 0;
        for (Countdown countdown : this.contents) {
            stringIDs[index] = countdown.getID().toString();
            index++;
        }
        return stringIDs;
    }

    public TreeSet<Countdown> getContents() {
        return this.contents;
    }

    @JsonProperty("colour")
    public String getColourString() {
        return this.colour.toString();
    }

    @JsonProperty("index")
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @JsonIgnore
    public Colour getColour() {
        return this.colour;
    }

    public void setColour(final Colour colour) {
        this.colour = colour;
    }

    @JsonIgnore
    public boolean isEmpty() {
        final Set<Countdown> countdowns = this.getContents();
        for (Countdown countdown : countdowns) {
            if (!countdown.isDone() && !countdown.isDeleted())
                return false;
        }
        return true;
    }

    @JsonIgnore
    public void disownContents() {
        final Set<Countdown> countdowns = this.getContents();
        countdowns.forEach(LegendHandler::disownCountdown);
    }

    /**
     * Returns the number of Countdown instances contained that are
     * neither marked as completed nor deleted.
     *
     * @see Countdown
     */
    @JsonIgnore
    public int getActiveSize() {
        int count = 0;
        final Set<Countdown> countdowns = this.getContents();
        for (Countdown countdown : countdowns) {
            if (!countdown.isDone() && !countdown.isDeleted())
                count++;
        }
        return count;
    }

    @Override
    @JsonIgnore
    public int compareTo(Legend o) {
        int comp = this.getIndex() - o.getIndex();
        if (comp == 0)
            comp = this.getID().compareTo(o.getID());
        return comp;
    }
}
