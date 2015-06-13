package Settings;

import android.graphics.Color;
import android.graphics.Paint;

public class CanvasSettings
{
    private Paint paint;

    public CanvasSettings()
    {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
    }

    // Getters
    public Paint getPaint()
    {
        return this.paint;
    }

    // Setters
    public void setPaint(final Paint paint)
    {
        this.paint = paint;
    }
}
