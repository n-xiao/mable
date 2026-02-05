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

package code.frontend.libs.katlaf.tables;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

public class SimpleTable extends Region {
    public SimpleTable() {
        // TODO
    }

    /*


     PROTECTED API
    -------------------------------------------------------------------------------------*/

    protected void setHgap(double hgap) {
        // TODO
    }

    protected void setVgap(double vgap) {
        // TODO
    }

    /*


     COMPOSITIONS
    -------------------------------------------------------------------------------------*/

    private class Table extends ScrollPane {
        private class Contents extends FlowPane {
            // TODO
        }
    }
}
