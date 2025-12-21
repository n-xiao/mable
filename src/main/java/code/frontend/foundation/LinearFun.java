package code.frontend.foundation;

public class LinearFun {
    // y = mx + c
    int m, c;

    public LinearFun(int m, int c) {
        this.m = m;
        this.c = c;
    }

    public LinearFun(int x1, int y1, int x2, int y2) {
        this.m = Math.round((y2 - y1) / (x2 - x1));
        this.c = y1 - this.m * x1;
    }

    public int getOutput(int x) {
        return this.m * x + this.c;
    }

    public int getInput(int y) {
        return (y - this.c) / this.m;
    }
}
