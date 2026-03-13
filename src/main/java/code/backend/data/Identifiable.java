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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Legend.class, name = "Legend")
    , @JsonSubTypes.Type(value = Countdown.class, name = "Countdown")
})
public abstract class Identifiable {
    private final UUID ID;
    @JsonIgnore private boolean markedForDeletion;

    protected Identifiable(final String ID_STRING) {
        this(UUID.fromString(ID_STRING));
    }

    protected Identifiable(final UUID ID) {
        this.ID = ID;
        this.markedForDeletion = false;
    }

    @JsonProperty("ID")
    public UUID getID() {
        return ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Identifiable))
            return false;
        else {
            Identifiable other = (Identifiable) obj;
            return other.ID.equals(this.ID);
        }
    }
}
