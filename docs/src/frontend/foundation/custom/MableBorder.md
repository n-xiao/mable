# Perfecting Imperfection (MableBorder)

## Specifications
The `MableBorder` is the second iteration of implementing a "hand-drawn" look-and-feel in Mable;
it has improved upon the old implementation, `CustomBox`.

The success criterions (SCs) for `MableBorder`'s appearance/behaviour are as follows:
1. A `MableBorder` must have only a single messiness (or sloppiness) value.
1. Each `MableBorder` must have varying thickness.
3. A `MableBorder` must be **consistently inconsistent**. See Appendix A for examples of inconsistent inconsistency.
4. A `MableBorder` must have a non-zero probability that there is at least one double-stroked corner. Refer to Appendix B for an example.

Note that the success criteria **does not** include self-expanatory criterions, such as:
- `MableBorder` has to be able to be added to a JavaFX `Pane`.
- A `MableBorder` should have a modifiable colour.
- etc.
These points are deemed unnecessary as this documentation's purpose, like all other developer documentation for Mable,
is to clarify advanced and difficult topics. Obtaining an understanding of aspects of the source code that are trivial
is left as an exercise for readers. *Muahahahahaaa.*

## Summary
There are **two types** of bezier curves used: quadratic (for the rounded corners) and cubic (for the edges).
To achieve SC1, SC2 and SC3, the control points of the 4 quadratic curves
that make up the border are the only points that are **directly** manipulated by the `messiness` value;
the two control points of each cubic curve are **indirectly** affected by the `messiness` value, and are instead determined by the
gradient of a quadratic curve's control point and the start or end of a rounded corner.

> [!IMPORTANT]
> All rounded corners of a `MableBorder` are computed first, in a clockwise direction, **BEFORE**
> computations for the edges occur (in a clockwise direction).
>
> Additionally, two-dimensional JavaFX coordinates start from the **top-left-most** corner, at $(0,0)$, of a space
> and end at the **bottom-right-most** corner of the space. In layman's terms: a rightwards direction is in the
> positive direction along the $x$ axis and a downwards direction is in the positive direction along the $y$ axis.
>


## Characteristics of a MableBorder
A `MableBorder`'s constructor accepts the following required `double`s:
- `thickness`: a value from `0` to infinity which is passed to the JavaFX canvas to determine stroke width.
- `cornerRadii`: a value from `0` to `1.0` (inclusive) which determines how rounded the corners of a `MableBorder` appears.
- `messiness`: a value from `0` to infinity which determines the sloppiness/difference-from-a-perfect-rectangle.

> [!NOTE]
> Any value above `1.0` will be treated as `1.0` for `cornerRadii`.
>

## The Rounded Corners
A `MableBorder` is **composed of** `Corner`s. A `Corner` has four `Coordinate`s with one of the three
being an instance of the `ControlCoordinate` class:

- `Coordinate origin`: This would be the corner of an actual rectangle (without rounded corners).
- `Coordinate start`: This is the start of the (rounded) corner.
- `Coordinate end`: This is the end of the (rounded) corner.
- `ControlCoordinate ctrl` This is the control point of the (rounded) corner.

> [!IMPORTANT]
> `start` is always before `end` for all corners, clockwise.
>

Coordinates `origin`, `start`, and `end` are relatively simple: `start` and `end` are both
`cornerDelta` distance away from the origin.

<div align="center">
<figure>
  <img src="https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/MableBorder-sketch-1.png"/>
  <figcaption><sub><br></br>Illustration of the top-left rounded corner</sub></figcaption>
</figure>
</div>
<br></br>

The value of `cornerDelta` is determined by a `MableBorder`'s `cornerRadii` value:
```java
private double getCornerDelta() {
    double len = Math.min(this.BORDER.getPaddedWidth(), this.BORDER.getPaddedHeight());
    return Math.min(0.5 * len, 0.5 * this.getCornerRadii() * len);
}
```

*For more info, you can see Appendix C and D*.

## Messiness
Here's where it gets a little *messy*... not really... just punning about.

Recall from the previous subsection that `ctrl` is the control point of the quadratic
bezier curve (of type `ControlCoordinate`) which makes up the shape of a `Corner`.

The coordinate position of `ctrl` is randomly generated, but within the range
of the displacement of `start` from `origin` and the displacement of `end` from `origin`,
as demonstrated below.

<div align="center">
<figure>
  <img src="https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/MableBorder-sketch-2.png"/>
  <figcaption><sub><br></br>Illustration of possible values of ctrl.x and ctrl.y using a number line</sub></figcaption>
</figure>
</div>
<br></br>

Note that in the image above, `this.ctrl` is used; it is just an explicit reference to a `Corner` instance
and doesn't change any meaning discussed here.

By keeping since `messiness` is used to determine the positioning of `ctrl`, SC1 is achieved.
Code is also kept rather tidy... *wink wink* — get it? `messiness` and *tidiness* heheheeheheeeee.

## Connecting the corners
Here's where the cubic bezier curve shines. When the `ctrl` positioning has been determined for all
four rounded corners, the edges of the `MableBorder` is drawn.

In order to achieve SC3, the control points of the cubic curve **must** be determined by the
position of the closest `ctrl` point of the closest `Corner`. To do this, either the `x` or `y` value of
the control point of the cubic curve is determined *randomly*, depending on whether the edge is to be drawn
in a horizontal or vertical direction, respectively. Next, the either the `y` or `x` value that **was not set
randomly** of the same (cubic) control point would be determined through the use of a linear regression
line, which intersects the `ctrl` point and **either** the `start` **or** `end` point, depending
on whether the edge is to be drawn in either the horizontal or vertical direction, as well as the orientation
of the corner(s) in question.

> [!Note]
> If $R$ is the *randomly-determined* value of either `x` or `y` of any given cubic control point,
> $K \leq R < K \pm \Delta$ where $K$ is either `start.x`, `start.y`, `end.x` or `end.y` and
> $\Delta$ is the value of `cornerDelta` (discussed previously).
>
> The $\pm$ symbol is used because $R$ is a vector quantity, with the sign determined by
> the orientation of the `Corner` in question.
>

I think it's a little tough to explain, so hopefully the diagram below would help.
Note that, for save and restore operations, calculated edges have their coordinate data stored in
the variable `cbd` of type `CubicBezierData` (record), and is referenced in the illustration.

<div align="center">
<figure>
  <img src="https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/MableBorder-sketch-3.png"/>
  <figcaption><sub><br></br>Example of a cubic control point determined by the quadratic control point of a corner</sub></figcaption>
</figure>
</div>
<br></br>

Here, the top edge of a `MableBorder` is being drawn between the top left rounded corner and the top right rounded corner (not shown).
Note that **only one of two** cubic control points is shown, as `cbd.c1`. Since this is a connection in the
**horizontal direction**, `cbd.c1.x` is randomly generated (marked with a dotted orange line). In this situation (and
for similar situations), the range of possible values that `cbd.c1.x` could have been assigned
is `start.x <= cbd.c1.x < start.x + cornerDelta`.

Next, a linear regression line is drawn through the `ctrl` and `end` points (marked in blue). Using the value of `cbd.c1.x`,
`cbd.c1.y` would be the $y$ value of the linear regression line at $x = $ `cbd.c1.x` (marked in purple).

Another linear regression line is drawn through the `ctrl` and `start` points to illustrate how the same process
would look like when connecting two corners vertically (the bottom-left and the top-left corners in this case).

This is how SC3 is achieved; the edges are adjusted to seemlessly connect with each corner, giving the user
interface a natural and more spacious look.

## Styling
To achieve SC2 and SC4, 3 iterations (or passes) of drawing are done on a single `MableBorder`.

For the first and second iteration, no modifications are made, and the random characteristics
help achieve SC2.

On the third iteration, all `Corner`s' have a 50% chance of being marked as being decorative:
```java
this.IS_DECOR = Math.random() < 0.5 && pass > 1;
```

If a `Corner` is decorative, a multiplier $M$ is applied to its `cornerRadii` value, where $M$ is
a random `double` within the range of $1.18 \leq M < 1.25$, effectively implementing
SC4. To prevent decorative corners from affecting the overall appearance of the `MableBorder`,
edges **are not** drawn when `pass > 1`.

> [!NOTE]
> Many constants in this styling section have no mathemtical justification
> because this is mostly for aesthetics.
>

*But thanks for reading till the end! ur the best!*

## Appendix
### A — Inconsistent inconsistency
Before, and inclusive of, Mable `v2.0.0-beta`, a (now deprecated) class `CustomBox` was used to implement the
look and feel. It was made up of 8 completely separate components: the top left corner, the top edge,
the top right corner, the right edge, the bottom right corner, the bottom edge, the bottom left corner,
and the left edge; they all used randomly-generated quadratic bezier curves with points
that were **completely independent** of each other.

As a result, there were multiple instances where the gradient of the quadratic bezier curve at the end
of one component had a different sign (positive instead of negative or vice versa) to the start of the next component.
This led to small "hitches" appearing where the start and end of different curves met, as shown below.
<div align="center">
<figure>
  <img src="https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/MableBorder-hitch-annotated.png"/>
  <figcaption><sub><br></br>Example of a hitch from the deprecated CustomBox</sub></figcaption>
</figure>
</div>
<br></br>
Despite being rather small, this negatively impacts the viewing experience when there are many of such instances.

Additionally, the lack of control over the control points on the edges created situations where the border
could cave inwards by a slight margin. Combined with the hitches, it was a recipe for creating a rather
sad-looking, flaccid user interface.

### B — Double-stroked corner
I must admit that I have taken quite a bit of inspiration from the [Excalidraw](https://github.com/excalidraw/excalidraw)
look-and-feel when designing Mable's user interface. So, it is a must to include
double-stroked corners, as shown below.

<div align="center">
<figure>
  <img src="https://github.com/n-xiao/mable-artifacts/blob/main/media/dev-docs/MableBorder-double-stroked-annotated.png"/>
  <figcaption><sub><br></br>A Countdown with a double-stroked corner</sub></figcaption>
</figure>
</div>
<br></br>

### C — Coordinate, ControlCoordinate and LinearFun classes
**Coordinate**

For the purpose of this document, the `Coordinate` class can be thought of as a simple
Java class which holds coordinates of a two-dimensional space as type `double`.

**ControlCoordinate**

The `ControlCoordinate` class **extends** the `Coordinate` class.

```java
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
```

**LinearFun**

The `LinearFun` class is used to create instances that serve as mathematical functions for a linear regression line,
following the formula of `y = mx + c`, where `m` is the gradient of the line and `c` is the y-intercept value.

```java
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
```




### D — MableBorder Source Code
As the MableBorder class may be updated/modified in the future, a copy of the source code that
this document is based off of is included.

```java
/*
 * Copyright (C) 2026  Nicholas Siow <nxiao.dev@gmail.com>
 */

package code.frontend.foundation.custom;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class MableBorder extends ResizableCanvas {
    private static final int EDGES = 4;
    private static final int PASSES = 3;

    private final Corner[][] CORNERS;
    private final CubicBezierData[][] CONNECTIONS;
    private double thickness;
    private double messiness;
    private double cornerRadii;

    public MableBorder(double thickness, double messiness, double cornerRadii) {
        this.CORNERS = new Corner[PASSES][EDGES];
        this.CONNECTIONS = new CubicBezierData[PASSES][EDGES];
        this.thickness = thickness;
        this.messiness = messiness;
        this.cornerRadii = cornerRadii;
    }

    @Override
    protected void draw(GraphicsContext gc, boolean recompute) {
        for (int p = 0; p < PASSES; p++) {
            if (recompute || !notNull((Object[]) this.CORNERS)
                || !notNull((Object[]) this.CONNECTIONS)) {
                this.CORNERS[p][0] = new Corner(this, p, 0);
                this.CORNERS[p][0].recompute();

                for (int i = 1; i < EDGES; i++) {
                    this.CORNERS[p][i] = new Corner(this, p, i);
                    this.CORNERS[p][i].recompute();

                    Corner prev = this.CORNERS[p][i - 1];
                    Coordinate[] ctrls = joinCorners(prev, this.CORNERS[p][i]);
                    this.CONNECTIONS[p][i - 1] = new CubicBezierData(ctrls[0], ctrls[1]);
                }

                Coordinate[] ctrls = joinCorners(this.CORNERS[p][EDGES - 1], this.CORNERS[p][0]);
                this.CONNECTIONS[p][EDGES - 1] = new CubicBezierData(ctrls[0], ctrls[1]);
            }

            gc.setLineWidth(this.thickness);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setLineJoin(StrokeLineJoin.ROUND);

            gc.beginPath();

            for (int i = 0; i < EDGES - 1; i++) {
                this.CORNERS[p][i].draw(gc);
                CubicBezierData cbd = this.CONNECTIONS[p][i];
                if (p < 2)
                    gc.bezierCurveTo(cbd.c1().x, cbd.c1().y, cbd.c2().x, cbd.c2().y,
                        this.CORNERS[p][i + 1].start.x, this.CORNERS[p][i + 1].start.y);
            }

            this.CORNERS[p][EDGES - 1].draw(gc);
            CubicBezierData cbd = this.CONNECTIONS[p][EDGES - 1];
            if (p < 2)
                gc.bezierCurveTo(cbd.c1().x, cbd.c1().y, cbd.c2().x, cbd.c2().y,
                    this.CORNERS[p][0].start.x, this.CORNERS[p][0].start.y);

            gc.stroke();
            gc.closePath();
        }
    }

    private Coordinate[] joinCorners(Corner corner1, Corner corner2) {
        final boolean H_MATCH = !(corner1.isBottom() ^ corner2.isBottom());
        final boolean V_MATCH = !(corner1.isRight() ^ corner2.isRight());
        if (!(H_MATCH ^ V_MATCH))
            throw new IllegalArgumentException("Attempted to create a weird rectangle.");

        double cx1, cy1, cx2, cy2;
        if (H_MATCH) {
            cx1 = getRandom(corner1.end.x, extrapolateCornerDeltaX(corner1, corner1.end));
            cy1 = corner1.ctrl.getXmodel().getYfromX(cx1);
            cx2 = getRandom(corner2.start.x, extrapolateCornerDeltaX(corner2, corner2.start));
            cy2 = corner2.ctrl.getXmodel().getYfromX(cx2);
        } else {
            cy1 = getRandom(corner1.end.y, extrapolateCornerDeltaY(corner1, corner1.end));
            cx1 = corner1.ctrl.getYmodel().getXfromY(cy1);
            cy2 = getRandom(corner2.start.y, extrapolateCornerDeltaY(corner2, corner2.start));
            cx2 = corner2.ctrl.getYmodel().getXfromY(cy2);
        }

        Coordinate[] coords = {new Coordinate(cx1, cy1), new Coordinate(cx2, cy2)};
        return coords;
    }

    public void setMessiness(double messiness) {
        this.messiness = messiness;
        super.resizeAndDraw(true);
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        super.resizeAndDraw(true);
    }

    public void setCornerRadii(double cornerRadii) {
        this.cornerRadii = cornerRadii;
        super.resizeAndDraw(true);
    }

    public double getPaddingDist() {
        return this.thickness * (this.messiness + 1);
    }
    private double getPaddedHeight() {
        return this.getHeight() - 2 * getPaddingDist();
    }

    private double getPaddedWidth() {
        return this.getWidth() - 2 * getPaddingDist();
    }

    protected static boolean notNull(Object... objects) {
        for (Object object : objects) {
            if (object == null)
                return false;
        }
        return true;
    }

    protected static double getRandom(double d1, double d2) {
        double diff = d2 - d1;
        return d1 + diff * Math.random();
    }

    protected static double plusMinus(double d, boolean b) {
        return b ? d : -d;
    }

    protected static double extrapolateCornerDeltaX(Corner corner, Coordinate coord) {
        return coord.x - plusMinus(corner.getCornerDelta(), corner.isRight());
    }

    protected static double extrapolateCornerDeltaY(Corner corner, Coordinate coord) {
        return coord.y - plusMinus(corner.getCornerDelta(), corner.isBottom());
    }

    private static class Corner {
        final MableBorder BORDER;
        final boolean IS_DECOR;
        final int INDEX; // used for bitmask

        Coordinate origin;
        Coordinate start;
        Coordinate end;
        ControlCoordinate ctrl;

        Corner(final MableBorder BORDER, int pass, int index) {
            this.BORDER = BORDER;
            this.IS_DECOR = Math.random() < 0.5 && pass > 1;
            this.INDEX = index;
        }

        void draw(GraphicsContext gc) {
            if (!notNull(this.origin, this.start, this.end, this.ctrl))
                recompute();
            gc.moveTo(this.start.x, this.start.y);
            gc.quadraticCurveTo(this.ctrl.x, this.ctrl.y, this.end.x, this.end.y);
        }

        void recompute() {
            this.origin = this.newOrigin();
            this.start = this.newStart();
            this.end = this.newEnd();
            this.ctrl = this.newControlCoord();
        }

        private Coordinate newOrigin() {
            double ox, oy;
            ox = this.isRight() ? BORDER.getWidth() - BORDER.getPaddingDist()
                                : BORDER.getPaddingDist();
            oy = this.isBottom() ? BORDER.getHeight() - BORDER.getPaddingDist()
                                 : BORDER.getPaddingDist();

            return new Coordinate(ox, oy);
        }

        private Coordinate newStart() {
            double delta = this.getCornerDelta();

            delta *= this.isRight() ? -1 : 1; // delta is negative if origin is on rhs

            double sx, sy;
            sx = this.isRight() ^ this.isBottom() ? this.origin.x + delta : this.origin.x;
            sy = !(this.isRight() ^ this.isBottom()) ? this.origin.y + delta : this.origin.y;

            return new Coordinate(sx, sy);
        }

        private Coordinate newEnd() {
            double delta = this.getCornerDelta();

            delta *= this.isBottom() ? -1 : 1; // delta is negative if origin is at bottom

            double ex, ey;
            ex = !(this.isRight() ^ this.isBottom()) ? this.origin.x + delta : this.origin.x;
            ey = this.isRight() ^ this.isBottom() ? this.origin.y + delta : this.origin.y;

            return new Coordinate(ex, ey);
        }
        private ControlCoordinate newControlCoord() {
            double delta = this.getCtrlDelta();

            double x = this.origin.x - getRandom(0, plusMinus(delta, this.isRight()));
            double y = this.origin.y - getRandom(plusMinus(delta, this.isBottom()), 0);

            Coordinate xCoord = this.isRight() ^ this.isBottom() ? this.start : this.end;
            Coordinate yCoord = !(this.isRight() ^ this.isBottom()) ? this.start : this.end;

            return new ControlCoordinate(x, y, xCoord, yCoord);
        }

        private boolean isRight() {
            return Integer.bitCount(this.INDEX) == 1;
        }

        private boolean isBottom() {
            return Integer.highestOneBit(this.INDEX) == 2;
        }

        private double getCornerDelta() {
            double len = Math.min(this.BORDER.getPaddedWidth(), this.BORDER.getPaddedHeight());
            return Math.min(0.5 * len, 0.5 * this.getCornerRadii() * len);
        }

        private double getCtrlDelta() {
            return this.getMessiness() * (this.BORDER.thickness + this.getCornerDelta());
        }

        private double getMessiness() {
            return BORDER.messiness;
        }

        private double getCornerRadii() {
            return this.IS_DECOR ? this.BORDER.cornerRadii * getRandom(1.18, 1.25)
                                 : this.BORDER.cornerRadii;
        }
    }

    private static record CubicBezierData(Coordinate c1, Coordinate c2) {}
}
```

---

This file is part of Mable.

Mable is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

Mable is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with Mable. If not, see <https://www.gnu.org/licenses/>.

<sub>Copyright © 2026 Nicholas Siow  <nxiao.dev@gmail.com> </sub>
