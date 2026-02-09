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

package code.frontend.capabilities.countdown.components;

import code.backend.data.Countdown;
import code.frontend.libs.katlaf.FormatHandler;
import java.time.LocalDate;

public class CompletedPane extends CountdownPane {
    public CompletedPane(final Countdown cd, final LocalDate now) {
        super(cd, now);
    }

    @Override
    protected String getTextAdverb(boolean isOverdue) {
        return "AGO";
    }

    @Override
    protected String getStatusString(LocalDate now) {
        return "Completed";
    }

    @Override
    protected String getStringDueDate(LocalDate now) {
        final String day = Integer.toString(getCountdown().getCompletionDateTime().getDayOfYear());
        final String month =
            Integer.toString(getCountdown().getCompletionDateTime().getMonthValue());
        final String year = Integer.toString(getCountdown().getCompletionDateTime().getYear());
        return "On: " + day + "/" + month + "/" + year; // TODO: MAKE THIS CONFIGURABLE
    }

    @Override
    protected String getDaysLeftString(LocalDate now) {
        return FormatHandler.intToString(Math.abs(this.getCountdown().getDaysUntilCompletion(now)));
    }
}
