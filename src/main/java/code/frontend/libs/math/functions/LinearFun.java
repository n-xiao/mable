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

package code.frontend.libs.math.functions;

public class LinearFun {
    // y = mx + c
    double m, c;

    public LinearFun(double m, double c) {
        this.m = m;
        this.c = c;
    }

    public LinearFun(double x1, double y1, double x2, double y2) {
        this.m = (y2 - y1) / (x2 - x1);
        this.c = y1 - this.m * x1;
    }

    public double getYfromX(double x) {
        return this.m * x + this.c;
    }

    public double getXfromY(double y) {
        return (y - this.c) / this.m;
    }

    @Override
    public String toString() {
        return "y = " + m + "x + " + c;
    }
}
