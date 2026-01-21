/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.backend.utils;

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
