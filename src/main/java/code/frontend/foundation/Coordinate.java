package code.frontend.foundation;

public class Coordinate
{

    double x, y; // defines centre
    private double xDeviation, yDeviation; // defines allowable deviation(s) from centre

    protected Coordinate(double x, double y)
    {
        this.x = x;
        this.y = y;
        this.xDeviation = 0;
        this.yDeviation = 0;
    }

    public static double distanceBetween(Coordinate coord1, Coordinate coord2)
    {
        // use pythag theorem
        double x = coord1.x - coord2.x;
        double y = coord1.y - coord2.y;
        return Math.sqrt(x * x + y * y);
    }

    public boolean canVary()
    {
        return this.xDeviation != 0 && this.yDeviation != 0;
    }

    public void setDeviations(double x, double y)
    {
        this.xDeviation = x;
        this.yDeviation = y;
    }

    public void setDeviations(double both)
    {
        this.xDeviation = both;
        this.yDeviation = both;
    }

    public double getVarX()
    {
        double min = (this.x < this.xDeviation) ? -this.x : -this.xDeviation;
        double max = this.xDeviation;
        double deviation = min + (Math.random() * ((max - min) + 1));
        return this.x + deviation;
    }

    public double getVarY()
    {
        double min = (this.y < this.yDeviation) ? -this.y : -this.yDeviation;
        double max = this.yDeviation;
        double deviation = min + (Math.random() * ((max - min) + 1));
        return this.y + deviation;
    }
}
