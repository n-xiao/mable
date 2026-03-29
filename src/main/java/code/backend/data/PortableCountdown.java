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

import code.backend.settings.SettingsHandler;
import code.backend.settings.SettingsHandler.Key;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A class containing the core essentials for creating Countdowns anywhere.
 * Used for import and export operations.
 *
 * @since v3.1.0
 * @see Countdown
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PortableCountdown extends Identifiable {
    @JsonProperty("name") private final String name;
    @JsonProperty("due") private final ZonedDateTime due;

    /**
     * Creates a new PortableCountdown instance. Please note that this constructor
     * is mainly used for serialisation. Use the getPortable() method on an existing
     * Countdown instance to create a PortableCountdown.
     */
    @JsonCreator
    public PortableCountdown(
        @JsonProperty("name") final String name, @JsonProperty("due") final ZonedDateTime due) {
        super(UUID.randomUUID());
        this.name = name;
        this.due = due;
    }

    /*


     PUBLIC API
    -------------------------------------------------------------------------------------*/

    public String getName() {
        return name;
    }

    public ZonedDateTime getDue() {
        return due;
    }

    /**
     * Creates and adds a new Countdown instance using the name and due date values of
     * this instance. Note that this method calls the CountdownHandler.create method.
     *
     * @return Countdown      the newly created Countdown instance
     */
    @JsonIgnore
    public Countdown create() {
        return CountdownHandler.create(name, due.toLocalDate());
    }

    @JsonIgnore
    public String getDisplayDue() {
        final LocalDate date = this.due.toLocalDate();
        final DecimalFormat decimalFormat = new DecimalFormat("00");
        final String day = decimalFormat.format(date.getDayOfMonth());
        final String month = decimalFormat.format(date.getMonthValue());
        final String year = new DecimalFormat("0000").format(date.getYear());
        return (SettingsHandler.getBooleanValue(Key.ALT_DATE) ? month + "/" + day
                                                              : day + "/" + month)
            + "/" + year;
    }
}
