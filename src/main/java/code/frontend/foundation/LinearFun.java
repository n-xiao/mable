package code.frontend.foundation;

public class LinearFun
{
    // y = mx + c
    double m, c;

    public LinearFun(double m, double c)
    {
        this.m = m;
        this.c = c;
    }

    public LinearFun(Coordinate coord1, Coordinate coord2)
    {
        this.m = (coord2.y - coord1.y) / (coord2.x - coord1.x);
        this.c = coord1.y - this.m * coord1.x;
    }

    public LinearFun(double m, Coordinate coord)
    {
        this.m = m;
        this.c = coord.y - m * coord.x;
    }

    public double getOutput(double x)
    {
        return this.m * x + this.c;
    }

    public double getInput(double y)
    {
        return (y - this.c) / this.m;
    }
}
