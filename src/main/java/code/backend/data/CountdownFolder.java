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

import code.frontend.libs.katlaf.tables.Tablet;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.TreeSet;
import java.util.UUID;
import javafx.scene.paint.Color;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountdownFolder extends Identifiable implements Tablet {
    private String name;
    private String rbgString;
    private int pos;
    private final TreeSet<Countdown> contents;

    /*


     CONSTRUCTORS
    -------------------------------------------------------------------------------------*/

    public CountdownFolder(String name) {
        this.name = name;
        this.contents = new TreeSet<Countdown>(new SortByRemainingDays());
        this.rbgString = "rgb(0, 0, 0)";
        super(UUID.randomUUID());
    }

    @JsonCreator
    public CountdownFolder(@JsonProperty("ID") String folderId, @JsonProperty("name") String name,
        @JsonProperty("contents") String[] contentAsIDs, @JsonProperty("pos") int pos,
        @JsonProperty("colour") String colour) {
        super(folderId);
        this.name = name;
        this.contents = new TreeSet<Countdown>(new SortByRemainingDays());
        this.pos = pos;
        this.rbgString = colour;
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
        return this.rbgString;
    }

    @JsonIgnore
    public Color getColour() {
        return Color.web(this.rbgString);
    }

    public void setColour(final Color colour) {
        final String red = Integer.toString((int) colour.getRed());
        final String green = Integer.toString((int) colour.getGreen());
        final String blue = Integer.toString((int) colour.getBlue());
        this.rbgString = "rgb(" + red + ", " + green + ", " + blue + ")";
    }

    @JsonProperty("pos")
    @Override
    public int getPosition() {
        return this.pos;
    }

    @Override
    public void setPosition(int pos) {
        this.pos = pos;
    }
}
