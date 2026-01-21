/*
   Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
*/

package code.frontend.foundation.custom;

import code.backend.utils.LinearFun;

public class ControlCoordinate extends Coordinate {
    private LinearFun xModel;
    private LinearFun yModel;

    public ControlCoordinate(double x, double y, Coordinate xCoord, Coordinate yCoord) {
        super(x, y);
        this.xModel = new LinearFun(x, y, xCoord.x, xCoord.y);
        this.yModel = new LinearFun(x, y, yCoord.x, yCoord.y);
    }

    public LinearFun getXmodel() {
        return xModel;
    }

    public LinearFun getYmodel() {
        return yModel;
    }
}
