/*
   Copyright (C) 2026  Nicholas Siow

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as
   published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.foundation;

public class Coordinate {
    double x, y; // defines centre
    private double xDeviation, yDeviation; // defines allowable deviation(s) from centre
    private double varX, varY;

    protected Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
        this.xDeviation = 0;
        this.yDeviation = 0;
    }

    public static double distanceBetween(Coordinate coord1, Coordinate coord2) {
        // use pythag theorem
        double x = coord1.x - coord2.x;
        double y = coord1.y - coord2.y;
        return Math.sqrt(x * x + y * y);
    }

    public boolean canVary() {
        return this.xDeviation != 0 && this.yDeviation != 0;
    }

    public void setDeviations(double x, double y) {
        this.xDeviation = x;
        this.yDeviation = y;
    }

    public void setDeviations(double both) {
        this.xDeviation = both;
        this.yDeviation = both;
    }

    public double getVarX() {
        // double min = (this.x < this.xDeviation) ? -this.x : -this.xDeviation;
        double min = -this.xDeviation;
        double max = this.xDeviation;
        double deviation = min + (Math.random() * ((max - min) + 1));
        this.varX = this.x + deviation;
        return this.varX;
    }

    public double getVarX(double min, double max) {
        double deviation = min + (Math.random() * ((max - min) + 1));
        this.varX = this.x + deviation;
        return this.varX;
    }

    public double getVarY() {
        // double min = (this.y < this.yDeviation) ? -this.y : -this.yDeviation;
        double min = -this.yDeviation;
        double max = this.yDeviation;
        double deviation = min + (Math.random() * ((max - min) + 1));
        this.varY = this.y + deviation;
        return this.varY;
    }

    public double getVarY(double min, double max) {
        double deviation = min + (Math.random() * ((max - min) + 1));
        this.varY = this.y + deviation;
        return this.varY;
    }
}
