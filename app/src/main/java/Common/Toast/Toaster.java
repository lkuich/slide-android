package Common.Toast;

import android.content.Context;
import android.widget.Toast;

public class Toaster
{
    private static Toaster instance = null;

    private Toaster() { }

    public static Toaster getInstance()
    {
        if (Toaster.instance == null)
        {
            Toaster.instance = new Toaster();
        }
        return instance;
    }

    public void showToast(final String message, final Context context)
    {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT).show();
    }
}
