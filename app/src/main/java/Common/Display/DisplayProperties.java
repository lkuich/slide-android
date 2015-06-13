package Common.Display;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DisplayProperties
{
    private static DisplayProperties instance;

    private static Display display;

    private static short displayWidth;
    private static short displayHeight;

    private static int displayX;
    private static int displayY;

    private DisplayProperties(final Context context)
    {
        display =
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        displayWidth = (short) display.getWidth(); // abx
        displayHeight = (short) display.getHeight(); // aby

        final Point size = new Point();
        display.getSize(size);

        displayX = size.x; // dimX
        displayY = size.y; // dimY
    }

    public static DisplayProperties getInstance(final Context context)
    {
        if (instance == null)
        {
            instance = new DisplayProperties(context);
        }
        return instance;
    }

    public static Display getDisplay()
    {
        return display;
    }

    public static short getDisplayWidth()
    {
        return displayWidth;
    }

    public static short getDisplayHeight()
    {
        return displayHeight;
    }

    public static int getDisplayX()
    {
        return displayX;
    }

    public static int getDisplayY()
    {
        return displayY;
    }
}
