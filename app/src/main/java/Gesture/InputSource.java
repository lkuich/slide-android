package Gesture;

public class InputSource
{
    private int x;
    private int y;
    private int[] coordinates;

    public InputSource()
    {
        this.x = 0;
        this.y = 0;
        coordinates = new int[2];
    }

    public void setX(final int x)
    {
        this.x = x;
    }

    public void setY(final int y)
    {
        this.y = y;
    }

    public void setCoordinates(final int x, final int y)
    {
        this.x = x;
        this.y = y;
        this.coordinates[0] = this.x;
        this.coordinates[1] = this.y;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int get()
    {
        return this.x + this.y;
    }

    public int[] getCoordinates()
    {
        return this.coordinates;
    }
}