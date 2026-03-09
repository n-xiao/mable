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
import java.util.TreeSet;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Legend extends Identifiable implements Comparable<Legend> {
    private String name;
    private Colour colour;
    private final TreeSet<Countdown> contents;

    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    public Legend(String name) {
        this.name = name;
        this.contents = new TreeSet<Countdown>(new SortByRemainingDays());
        this.colour = new Colour(0, 0, 0, 1);
        super(UUID.randomUUID());
    }

    @JsonCreator
    public Legend(@JsonProperty("ID") String legendId, @JsonProperty("name") String name,
        @JsonProperty("contents") String[] contentAsIDs, @JsonProperty("colour") String colour) {
        super(legendId);
        this.name = name;
        this.contents = new TreeSet<Countdown>(new SortByRemainingDays());
        this.colour = new Colour(colour);
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

    @JsonIgnore
    public Colour getColour() {
        return this.colour;
    }

    public void setColour(final Colour colour) {
        this.colour = colour;
    }

    @Override
    public int compareTo(Legend o) {
        return this.name.compareTo(o.name);
    }
}
